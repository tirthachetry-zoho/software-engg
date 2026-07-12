package companywise.GoldmanSachs.hld;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * High-Level Design — Real-Time Risk Management System (Goldman Sachs HLD)
 *
 * Designs a pre-trade and post-trade risk engine that enforces limits and computes
 * exposure across a trading book in real time.
 *
 * Components:
 *  - LimitStore        : per-account / per-symbol limits (notional, position, order rate).
 *  - PositionTracker   : maintains live positions from fills (event-sourced).
 *  - RiskEngine        : evaluates orders against limits BEFORE execution (pre-trade)
 *                        and recomputes exposure AFTER fills (post-trade).
 *  - AlertBus          : emits breaches to monitoring / kill-switch.
 *
 * Scaling techniques: per-account sharding, lock-free position counters, async
 * evaluation off the hot path, circuit-breaker / kill-switch on breach.
 */
public class DesignRiskManagementSystem {

    enum Side { BUY, SELL }

    static class Order {
        final String account, symbol;
        final Side side;
        final int price, qty;
        Order(String account, String symbol, Side side, int price, int qty) {
            this.account = account; this.symbol = symbol; this.side = side; this.price = price; this.qty = qty;
        }
        int notional() { return price * qty; }
    }

    static class Fill {
        final String account, symbol;
        final Side side;
        final int price, qty;
        Fill(String account, String symbol, Side side, int price, int qty) {
            this.account = account; this.symbol = symbol; this.side = side; this.price = price; this.qty = qty;
        }
    }

    // ---- Limits ----
    static class Limits {
        final long maxNotional;
        final int maxPosition;
        final int maxOrdersPerSec;
        Limits(long maxNotional, int maxPosition, int maxOrdersPerSec) {
            this.maxNotional = maxNotional; this.maxPosition = maxPosition; this.maxOrdersPerSec = maxOrdersPerSec;
        }
    }

    // ---- Live position per (account, symbol) ----
    static class Position {
        final AtomicInteger net = new AtomicInteger(0);     // signed shares
        final AtomicLong grossNotional = new AtomicLong(0); // abs(price*qty) summed
        void apply(Fill f) {
            int signed = (f.side == Side.BUY) ? f.qty : -f.qty;
            net.addAndGet(signed);
            grossNotional.addAndGet((long) f.price * f.qty);
        }
    }

    // ---- Risk engine ----
    static class RiskEngine {
        final Map<String, Limits> limits = new ConcurrentHashMap<>();
        final Map<String, Map<String, Position>> positions = new ConcurrentHashMap<>();
        final List<String> alerts = Collections.synchronizedList(new ArrayList<>());

        void setLimits(String account, Limits l) { limits.put(account, l); }

        Position position(String account, String symbol) {
            return positions.computeIfAbsent(account, k -> new ConcurrentHashMap<>())
                            .computeIfAbsent(symbol, k -> new Position());
        }

        /** Pre-trade check: returns true if the order is allowed. */
        boolean preTradeCheck(Order o) {
            Limits l = limits.get(o.account);
            if (l == null) return true; // no limits configured
            Position p = position(o.account, o.symbol);

            // Notional limit
            if (o.notional() > l.maxNotional) { alert(o.account, "NOTIONAL_EXCEEDED"); return false; }

            // Position limit (projected net)
            int projected = p.net.get() + (o.side == Side.BUY ? o.qty : -o.qty);
            if (Math.abs(projected) > l.maxPosition) { alert(o.account, "POSITION_LIMIT"); return false; }

            return true;
        }

        /** Post-trade: update positions from a fill. */
        void onFill(Fill f) { position(f.account, f.symbol).apply(f); }

        void alert(String account, String reason) {
            String msg = "ALERT " + account + ": " + reason;
            alerts.add(msg);
            System.out.println(msg);
        }

        List<String> alerts() { return new ArrayList<>(alerts); }
    }

    public static void main(String[] args) {
        RiskEngine risk = new RiskEngine();
        risk.setLimits("DESK1", new Limits(1_000_000, 500, 100));

        Order ok = new Order("DESK1", "AAPL", Side.BUY, 100, 10);   // notional 1000
        Order bad = new Order("DESK1", "AAPL", Side.BUY, 200, 6000); // notional 1.2M > limit

        System.out.println("pre-trade ok allowed: " + risk.preTradeCheck(ok));
        System.out.println("pre-trade bad allowed: " + risk.preTradeCheck(bad));

        // Simulate a fill and check post-trade position
        risk.onFill(new Fill("DESK1", "AAPL", Side.BUY, 100, 10));
        System.out.println("DESK1 AAPL net position: " + risk.position("DESK1", "AAPL").net.get());
        System.out.println("Alerts: " + risk.alerts());
    }
}