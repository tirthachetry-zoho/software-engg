package companywise.GoldmanSachs.lld;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Low-Level Design — Limit Order Book / Matching Engine (Goldman Sachs classic)
 *
 * Models a price-time-priority limit order book for a single instrument:
 *  - BUY orders rest in a descending-price book (best/highest price first).
 *  - SELL orders rest in an ascending-price book (best/lowest price first).
 *  - Incoming marketable orders match against the opposite book using a
 *    price-time priority queue (implemented with TreeMap<price, Queue<order>>).
 *  - Generates Trade events and supports cancel/replace.
 *
 * Time complexity: add/cancel O(log n) via TreeMap; match O(k log n) for k fills.
 */
public class DesignOrderBook {

    enum Side { BUY, SELL }

    // Immutable order identifier generator
    static final AtomicLong ORDER_ID = new AtomicLong(0);

    static class Order {
        final long id;
        final Side side;
        final String symbol;
        final boolean market;
        final int price;   // ignored for market orders
        final AtomicInteger remaining;
        final long timestamp;

        Order(Side side, String symbol, int price, int qty, boolean market) {
            this.id = ORDER_ID.incrementAndGet();
            this.side = side;
            this.symbol = symbol;
            this.price = price;
            this.remaining = new AtomicInteger(qty);
            this.market = market;
            this.timestamp = System.nanoTime();
        }

        int remaining() { return remaining.get(); }
        boolean isFilled() { return remaining.get() == 0; }
    }

    // A trade event produced by the matching engine
    static class Trade {
        final long buyId, sellId;
        final String symbol;
        final int price;
        final int qty;
        final long timestamp;

        Trade(long buyId, long sellId, String symbol, int price, int qty) {
            this.buyId = buyId; this.sellId = sellId; this.symbol = symbol;
            this.price = price; this.qty = qty; this.timestamp = System.nanoTime();
        }

        @Override public String toString() {
            return "TRADE " + symbol + " @ " + price + " x" + qty + " (buy#" + buyId + "/sell#" + sellId + ")";
        }
    }

    // The order book for one symbol
    static class OrderBook {
        final String symbol;
        // BUY book: highest price first -> use reverse ordering of keys
        final TreeMap<Integer, Queue<Order>> bids = new TreeMap<>(Collections.reverseOrder());
        // SELL book: lowest price first
        final TreeMap<Integer, Queue<Order>> asks = new TreeMap<>();
        final Map<Long, Order> ordersById = new ConcurrentHashMap<>();
        final List<Trade> tradeLog = Collections.synchronizedList(new ArrayList<>());

        OrderBook(String symbol) { this.symbol = symbol; }

        /** Submit an order; returns the list of trades generated. */
        synchronized List<Trade> submit(Order o) {
            List<Trade> trades = new ArrayList<>();
            if (o.side == Side.BUY) match(o, asks, trades);
            else match(o, bids, trades);

            if (!o.isFilled()) {
                TreeMap<Integer, Queue<Order>> book = (o.side == Side.BUY) ? bids : asks;
                book.computeIfAbsent(o.price, k -> new ConcurrentLinkedQueue<>()).offer(o);
                ordersById.put(o.id, o);
            }
            return trades;
        }

        /** Core matching loop: match incoming order against the opposite book. */
        private void match(Order incoming, TreeMap<Integer, Queue<Order>> opposite, List<Trade> trades) {
            Iterator<Map.Entry<Integer, Queue<Order>>> it = opposite.entrySet().iterator();
            while (it.hasNext() && !incoming.isFilled()) {
                Map.Entry<Integer, Queue<Order>> level = it.next();
                int lvlPrice = level.getKey();

                // Price check: a BUY crosses if lvlPrice <= incoming.price (or market buy).
                // A SELL crosses if lvlPrice >= incoming.price (or market sell).
                if (!incoming.market) {
                    if (incoming.side == Side.BUY && lvlPrice > incoming.price) break;
                    if (incoming.side == Side.SELL && lvlPrice < incoming.price) break;
                }

                Queue<Order> q = level.getValue();
                while (!q.isEmpty() && !incoming.isFilled()) {
                    Order resting = q.peek();
                    if (resting.isFilled()) { q.poll(); continue; }

                    int fillQty = Math.min(incoming.remaining(), resting.remaining());
                    incoming.remaining.addAndGet(-fillQty);
                    resting.remaining.addAndGet(-fillQty);

                    long buyId = (incoming.side == Side.BUY) ? incoming.id : resting.id;
                    long sellId = (incoming.side == Side.SELL) ? incoming.id : resting.id;
                    Trade t = new Trade(buyId, sellId, symbol, lvlPrice, fillQty);
                    trades.add(t);
                    tradeLog.add(t);

                    if (resting.isFilled()) { q.poll(); ordersById.remove(resting.id); }
                }
                if (q.isEmpty()) it.remove();
            }
        }

        /** Cancel a resting order by id. */
        synchronized boolean cancel(long orderId) {
            Order o = ordersById.remove(orderId);
            if (o == null) return false;
            TreeMap<Integer, Queue<Order>> book = (o.side == Side.BUY) ? bids : asks;
            Queue<Order> q = book.get(o.price);
            if (q != null) q.remove(o);
            return true;
        }

        int bestBid() { return bids.isEmpty() ? 0 : bids.firstKey(); }
        int bestAsk() { return asks.isEmpty() ? 0 : asks.firstKey(); }
        int restingCount() { return ordersById.size(); }
        List<Trade> trades() { return new ArrayList<>(tradeLog); }
    }

    public static void main(String[] args) {
        OrderBook book = new OrderBook("GS");

        // Seed resting SELL orders
        book.submit(new Order(Side.SELL, "GS", 101, 10, false));
        book.submit(new Order(Side.SELL, "GS", 102, 5, false));
        System.out.println("Best ask: " + book.bestAsk()); // 101

        // Incoming BUY that crosses the book
        List<Trade> trades = book.submit(new Order(Side.BUY, "GS", 102, 12, false));
        trades.forEach(System.out::println);
        // Expect: 10 @ 101, then 2 @ 102

        System.out.println("Best ask after: " + book.bestAsk()); // 102 (3 left)
        System.out.println("Resting orders: " + book.restingCount());

        // Market BUY sweeps remaining asks
        List<Trade> t2 = book.submit(new Order(Side.BUY, "GS", 0, 100, true));
        t2.forEach(System.out::println);
        System.out.println("Best ask now: " + book.bestAsk()); // 0 (empty)
    }
}