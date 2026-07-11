# High-Level Design (HLD) Problems List

Scalability-oriented system designs implemented in `system-design/hld/`. Each entry describes the problem, the components modeled, and the key architectural techniques shown in the code.

## 1. URL Shortener (URLShortener.java)
Design a service like bit.ly that maps long URLs to short codes and redirects users.
- **Components**
  - `DistributedIDGenerator` — Snowflake-like 64-bit unique ID generator (timestamp + node + sequence) to avoid collisions across nodes.
  - `URLStore` — Persists short-code to long-URL mappings with optional TTL/expiry (uses a `Delayed` queue to purge expired links).
  - `RateLimiter` — Token-bucket algorithm to throttle abuse per client.
  - `URLCache` — LRU cache layer in front of storage to serve hot links fast.
- **Key techniques** — Base62 encoding of IDs, distributed ID generation, caching, rate limiting, TTL-based expiry.

## 2. Twitter Timeline (DesignTwitter.java)
Design a tweet feed with follow/unfollow and a home timeline.
- **Components**
  - `User` — Holds followed-user set.
  - `Tweet` — Comparable by timestamp (newest first).
  - `TimelineGenerator` — Merges the user's own tweets with those of people they follow, with pagination.
  - `UserStore` — Caches user graphs.
  - `TweetStore` — Uses fanout-on-write to push tweets to followers' timelines.
- **Key techniques** — Fanout-on-write vs fanout-on-read trade-off, merge of sorted streams, pagination, caching of social graph.

## 3. Chat System (DesignChatSystem.java)
Design a one-to-one / group messaging system.
- **Components**
  - `Message` — Carries sender, body, timestamp, read status.
  - `Conversation` — Groups messages between participants.
  - `User` — Tracks online/offline presence.
  - `MessageStore` — Append-only store with pagination.
  - `ConversationStore` / `UserStore` — Index conversations and presence.
- **Key techniques** — Conversation partitioning, presence tracking, message pagination, separation of metadata vs message bodies.

## 4. File Storage System (DesignFileStorage.java)
Design a Dropbox-like service for uploading, deduplicating, and retrieving files.
- **Components**
  - `FileMetadata` — Name, size, owner, chunk list.
  - `Chunk` — Fixed-size piece of a file.
  - `User` — Ownership and quotas.
  - `ChunkStorage` — Stores chunks with deduplication by checksum (content-addressed storage).
  - `FileMetadataStore` — Maps file IDs to chunk lists.
- **Key techniques** — Chunking + content hashing for deduplication, metadata/data separation, checksum-based storage, multi-part upload support.

## 5. Notification Service (DesignNotificationService.java)
Design a multi-channel (email/push/SMS) notification dispatcher.
- **Components**
  - `Notification` — Channel, payload, status.
  - `UserPreferences` — Per-user channel opt-ins and do-not-disturb windows.
  - `NotificationTemplate` — Reusable message templates.
  - `NotificationStore` / `TemplateStore` — Persistence of notifications and templates.
- **Key techniques** — Template-based rendering, preference/opt-in filtering, do-not-disturb windows, pluggable channel dispatch, idempotent delivery tracking.

## Cross-Copic Themes
- **Caching** — LRU layers in URL shortener and Twitter.
- **Rate limiting** — Token bucket in URL shortener.
- **Pagination** — Chat, Twitter, and file listings.
- **Deduplication** — Checksums in file storage.
- **Presence & preferences** — Chat and notification service.
- **Distributed ID generation** — Snowflake-style in URL shortener.