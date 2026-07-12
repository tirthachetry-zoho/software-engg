package companywise.GoldmanSachs.hld;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

/**
 * High-Level Design — Electronic Trading System (Goldman Sachs HLD)
 *
 * Designs a low-latency equities trading platform with these components:
 *  - Gateway        : accepts client orders (FIX-like), validates, risk-checks.
 *  - MatchingEngine : price-time priority order book per instrument (reuses LLD logic).
 *  - TradeStore     : append-only, sharded by symbol, for audit & settlement.
 *  - MarketDataBus  : publishes trades/quotes to subscribers (pub/sub).
 *  - RiskService    : pre-trade limits (notional, position) — see DesignRiskManagementSystem.
 *
 * Scaling techniques: sharding by symbol, in-memory matching, async trade publishing,
 * idempotent order ids, back-pressure via bounded queues.
 */
public class DesignTradingSystem {

    enum Side { BUY, SELL }

    static class Order {
        final String id;
        final String symbol;
        final Side side;
        final int price;
        int qty;
        final long ts = System.nanoTime();
        Order(String id, String symbol, Side side, int price, int qty) {
            this.id = id; this.symbol = symbol; this.side = side; this.price = price; this.qty = qty;
        }
    }

    static class Trade {
        final String symbol; final int price; final int qty; final long ts;
        Trade(String symbol, int price, int qty) { this.symbol = symbol; this.price = price; this.qty = qty; this.ts = System.nanoTime(); }
        @Override public String toString() { return "TRADE " + symbol + " @ " + price + " x" + qty; }
    }

    // ---- Matching engine (single symbol, simplified price-time priority) ----
    static class MatchingEngine {
        final String symbol;
        final TreeMap<Integer, Queue<Order>> bids = new TreeMap<>(Collections.reverseOrder());
        final TreeMap<Integer, Queue<Order>> asks = new TreeMap<>();
        final List<Trade> tape = new CopyOnWriteArrayList<>();

        MatchingEngine(String symbol) { this.symbol = symbol; }

        List<Trade> submit(Order o) {
            List<Trade> trades = new ArrayList<>();
            TreeMap<Integer, Queue<Order>> book = (o.side == Side.BUY) ? asks : bids;
            Iterator<Map.Entry<Integer, Queue<Order>>> it = book.entrySet().iterator();
            while (it.hasNext() && o.qty > 0) {
                Map.Entry<Integer, Queue<Order>> lvl = it.next();
                int px = lvl.getKey();
                if (!marketable(o, px)) break;
                Queue<Order> q = lvl.getValue();
                while (!q.isEmpty() && o.qty > 0) {
                    Order r = q.peek();
                    int fill = Math.min(o.qty, r.qty);
                    o.qty -= fill; r.qty -= fill;
                    trades.add(new Trade(symbol, px, fill));
                    if (r.qty == 0) q.poll();
                }
                if (q.isEmpty()) it.remove();
            }
            if (o.qty > 0) {
                TreeMap<Integer, Queue<Order>> rest = (o.side == Side.BUY) ? bids : asks;
                rest.computeIfAbsent(o.price, k -> new ConcurrentLinkedQueue<>()).offer(o);
            }
            tape.addAll(trades);
            return trades;
        }

        boolean marketable(Order o, int restingPx) {
            return o.side == Side.BUY ? restingPx <= o.price : restingPx >= o.price;
        }
    }

    // ---- Trade store (sharded append-only) ----
    static class TradeStore {
        final Map<String, List<Trade>> shards = new ConcurrentHashMap<>();
        void record(Trade t) { shards.computeIfAbsent(t.symbol, k -> new CopyOnWriteArrayList<>()).add(t); }
        List<Trade> history(String symbol) { return shards.getOrDefault(symbol, Collections.emptyList()); }
    }

    // ---- Market data pub/sub bus ----
    static class MarketDataBus {
        final Map<String, List<Consumer<Trade>>> subs = new ConcurrentHashMap<>();
        void subscribe(String symbol, Consumer<Trade> c) { subs.computeIfAbsent(symbol, k -> new ArrayList<>()).add(c); }
        void publish(Trade t) { subs.getOrDefault(t.symbol, Collections.emptyList()).forEach(c -> c.accept(t)); }
    }

    // ---- Order gateway with risk pre-check hook ----
    static class OrderGateway {
        final Map<String, MatchingEngine> engines = new ConcurrentHashMap<>();
        final TradeStore store = new TradeStore();
        final MarketDataBus bus = new MarketDataBus();
        final Predicate<Order> riskCheck;

        OrderGateway(Predicate<Order> riskCheck) { this.riskCheck = riskCheck; }

        MatchingEngine engine(String symbol) {
            return engines.computeIfAbsent(symbol, MatchingEngine::new);
        }

        List<Trade> placeOrder(Order o) {
            if (!riskCheck.test(o)) throw new RuntimeException("Risk check failed for " + o.id);
            List<Trade> trades = engine(o.symbol).submit(o);
            trades.forEach(t -> { store.record(t); bus.publish(t); });
            return trades;
        }
    }

    // Helper so submit() can call marketable without exposing Order internals
    static boolean marketable(Order o, int restingPx) {
        return o.side == Side.BUY ? restingPx <= o.price : restingPx >= o.price;
    }

    public static void main(String[] args) {
        // Risk: allow any order for demo
        OrderGateway gw = new OrderGateway(o -> true);
        gw.bus.subscribe("AAPL", t -> System.out.println("[MD] " + t));

        gw.placeOrder(new Order("o1", "AAPL", Side.SELL, 100, 5));
        gw.placeOrder(new Order("o2", "AAPL", Side.SELL, 101, 3));
        List<Trade> trades = gw.placeOrder(new Order("o3", "AAPL", Side.BUY, 101, 6));
        System.out.println("Fills: " + trades);
        System.out.println("Tape size: " + gw.store.history("AAPL").size());
    }
}