package companywise.GoldmanSachs.hld;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

/**
 * High-Level Design — Low-Latency Market Data Distribution (Goldman Sachs HLD)
 *
 * Designs a system that ingests exchange feeds and fans them out to thousands of
 * downstream consumers (trading desks, algos, risk) with minimal latency.
 *
 * Components:
 *  - FeedAdapter      : normalizes heterogeneous exchange protocols into a common Quote.
 *  - Aggregator       : per-symbol best bid/offer (BBO) computation.
 *  - Dispatcher       : pub/sub fan-out, sharded by symbol for horizontal scale.
 *  - SnapshotCache    : last-known-good state for late joiners (replay on connect).
 *
 * Scaling techniques: sharding by symbol, non-blocking dispatchers, back-pressure,
 * sequence numbers for gap detection, UDP/multicast-friendly design.
 */
public class DesignMarketDataFeed {

    static class Quote {
        final String symbol;
        final int bid, ask;
        final long seq;
        final long ts = System.nanoTime();
        Quote(String symbol, int bid, int ask, long seq) { this.symbol = symbol; this.bid = bid; this.ask = ask; this.seq = seq; }
        @Override public String toString() { return symbol + " BBO[" + bid + "/" + ask + "] seq=" + seq; }
    }

    // ---- Normalizes raw exchange messages into Quotes ----
    static class FeedAdapter {
        final Map<String, AtomicLong> seqCounters = new ConcurrentHashMap<>();
        Quote normalize(String symbol, int bid, int ask) {
            long seq = seqCounters.computeIfAbsent(symbol, k -> new AtomicLong()).incrementAndGet();
            return new Quote(symbol, bid, ask, seq);
        }
    }

    // ---- Per-symbol BBO aggregator + snapshot cache ----
    static class Aggregator {
        final Map<String, Quote> bbo = new ConcurrentHashMap<>();
        final Map<String, Quote> snapshot = new ConcurrentHashMap<>();

        Quote update(Quote q) {
            Quote prev = bbo.get(q.symbol);
            // keep the quote with the higher sequence number
            if (prev == null || q.seq > prev.seq) {
                bbo.put(q.symbol, q);
                snapshot.put(q.symbol, q);
                return q;
            }
            return prev;
        }
        Quote getSnapshot(String symbol) { return snapshot.get(symbol); }
    }

    // ---- Pub/sub dispatcher, sharded by symbol hash ----
    static class Dispatcher {
        final int shards;
        final List<Map<String, List<Consumer<Quote>>>> buckets;
        Dispatcher(int shards) {
            this.shards = shards;
            this.buckets = new ArrayList<>();
            for (int i = 0; i < shards; i++) buckets.add(new ConcurrentHashMap<>());
        }
        private Map<String, List<Consumer<Quote>>> bucketOf(String symbol) {
            return buckets.get(Math.floorMod(symbol.hashCode(), shards));
        }
        void subscribe(String symbol, Consumer<Quote> c) {
            bucketOf(symbol).computeIfAbsent(symbol, k -> new CopyOnWriteArrayList<>()).add(c);
        }
        void publish(Quote q) {
            List<Consumer<Quote>> subs = bucketOf(q.symbol).getOrDefault(q.symbol, Collections.emptyList());
            for (Consumer<Quote> c : subs) c.accept(q);
        }
    }

    // ---- Top-level market data service ----
    static class MarketDataService {
        final FeedAdapter adapter = new FeedAdapter();
        final Aggregator aggregator = new Aggregator();
        final Dispatcher dispatcher = new Dispatcher(8);

        void subscribe(String symbol, Consumer<Quote> c) { dispatcher.subscribe(symbol, c); }

        void onRawFeed(String symbol, int bid, int ask) {
            Quote q = adapter.normalize(symbol, bid, ask);
            Quote agg = aggregator.update(q);
            dispatcher.publish(agg);
        }

        Quote snapshot(String symbol) { return aggregator.getSnapshot(symbol); }
    }

    public static void main(String[] args) {
        MarketDataService svc = new MarketDataService();
        svc.subscribe("AAPL", q -> System.out.println("[sub] " + q));
        svc.subscribe("MSFT", q -> System.out.println("[sub] " + q));

        svc.onRawFeed("AAPL", 150, 151);
        svc.onRawFeed("MSFT", 300, 301);
        svc.onRawFeed("AAPL", 150, 152); // improved ask

        System.out.println("AAPL snapshot: " + svc.snapshot("AAPL"));
        System.out.println("MSFT snapshot: " + svc.snapshot("MSFT"));
    }
}