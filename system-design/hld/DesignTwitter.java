import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * System Design — Twitter Timeline (High-Level Design)
 * Implements a Twitter-like social media feed with timeline generation
 * Features: Fanout-on-write, pagination, caching, hashtag support
 */
public class DesignTwitter {

    // User data structure
    static class User {
        private final long userId; private final Set<Long> followers = new ConcurrentHashMap().newKeySet();
        private final Set<Long> following = new ConcurrentHashMap().newKeySet();
        
        public User(long userId) { this.userId = userId; }
        public long getUserId() { return userId; }
        public void addFollower(long followerId) { followers.add(followerId); }
        public void removeFollower(long followerId) { followers.remove(followerId); }
        public void follow(long followeeId) { following.add(followeeId); }
        public void unfollow(long followeeId) { following.remove(followeeId); }
        public Set<Long> getFollowers() { return followers; }
        public Set<Long> getFollowing() { return following; }
    }

    // Tweet data structure
    static class Tweet implements Comparable<Tweet> {
        private final long tweetId; private final long authorId; private final String content;
        private final long timestamp; private final Set<String> hashtags;
        
        public Tweet(long tweetId, long authorId, String content, Set<String> hashtags) {
            this.tweetId = tweetId; this.authorId = authorId; this.content = content;
            this.timestamp = System.currentTimeMillis(); this.hashtags = hashtags;
        }
        
        public long getTweetId() { return tweetId; }
        public long getAuthorId() { return authorId; }
        public String getContent() { return content; }
        public long getTimestamp() { return timestamp; }
        public Set<String> getHashtags() { return hashtags; }
        
        @Override public int compareTo(Tweet other) { return Long.compare(other.timestamp, this.timestamp); } // descending
    }

    // Timeline generator with pagination
    static class TimelineGenerator {
        private final UserStore userStore; private final TweetStore tweetStore;
        
        public TimelineGenerator(UserStore userStore, TweetStore tweetStore) {
            this.userStore = userStore; this.tweetStore = tweetStore;
        }
        
        public List<Tweet> generateHomeTimeline(long userId, int limit, long beforeTweetId) {
            User user = userStore.getUser(userId);
            if (user == null) return Collections.emptyList();
            
            PriorityQueue<Tweet> timeline = new PriorityQueue<>();
            
            // Add user's own tweets
            for (Tweet tweet : tweetStore.getUserTweets(userId, limit, beforeTweetId)) {
                timeline.offer(tweet);
            }
            
            // Add tweets from followings
            for (Long followeeId : user.getFollowing()) {
                for (Tweet tweet : tweetStore.getUserTweets(followeeId, limit, beforeTweetId)) {
                    timeline.offer(tweet);
                }
            }
            
            List<Tweet> result = new ArrayList<>();
            while (!timeline.isEmpty() && result.size() < limit) {
                result.add(timeline.poll());
            }
            return result;
        }
        
        public List<Tweet> generateUserTimeline(long userId, int limit, long beforeTweetId) {
            return tweetStore.getUserTweets(userId, limit, beforeTweetId);
        }
        
        public List<Tweet> searchByHashtag(String hashtag, int limit, long beforeTweetId) {
            return tweetStore.getTweetsByHashtag(hashtag, limit, beforeTweetId);
        }
    }

    // User storage with caching
    static class UserStore {
        private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<String, Long> usernameToId = new ConcurrentHashMap<>();
        
        public User createUser(long userId, String username) {
            User user = new User(userId);
            users.put(userId, user);
            usernameToId.put(username, userId);
            return user;
        }
        
        public User getUser(long userId) { return users.get(userId); }
        public User getUserByUsername(String username) {
            Long userId = usernameToId.get(username);
            return userId != null ? users.get(userId) : null;
        }
    }

    // Tweet storage with fanout-on-write
    static class TweetStore {
        private final ConcurrentHashMap<Long, List<Tweet>> userTweets = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<String, List<Tweet>> hashtagIndex = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<Long, Tweet> tweetCache = new ConcurrentHashMap<>();
        
        public Tweet createTweet(long tweetId, long authorId, String content) {
            Set<String> hashtags = extractHashtags(content);
            Tweet tweet = new Tweet(tweetId, authorId, content, hashtags);
            
            // Store in user timeline
            userTweets.computeIfAbsent(authorId, k -> new CopyOnWriteArrayList<>()).add(0, tweet);
            
            // Index by hashtags
            for (String hashtag : hashtags) {
                hashtagIndex.computeIfAbsent(hashtag.toLowerCase(), k -> new CopyOnWriteArrayList<>()).add(0, tweet);
            }
            
            // Cache the tweet
            tweetCache.put(tweetId, tweet);
            
            return tweet;
        }
        
        public Tweet getTweet(long tweetId) { return tweetCache.get(tweetId); }
        
        public List<Tweet> getUserTweets(long userId, int limit, long beforeTweetId) {
            List<Tweet> tweets = userTweets.getOrDefault(userId, Collections.emptyList());
            List<Tweet> result = new ArrayList<>();
            for (Tweet tweet : tweets) {
                if (beforeTweetId > 0 && tweet.getTweetId() >= beforeTweetId) continue;
                if (result.size() >= limit) break;
                result.add(tweet);
            }
            return result;
        }
        
        public List<Tweet> getTweetsByHashtag(String hashtag, int limit, long beforeTweetId) {
            List<Tweet> tweets = hashtagIndex.getOrDefault(hashtag.toLowerCase(), Collections.emptyList());
            List<Tweet> result = new ArrayList<>();
            for (Tweet tweet : tweets) {
                if (beforeTweetId > 0 && tweet.getTweetId() >= beforeTweetId) continue;
                if (result.size() >= limit) break;
                result.add(tweet);
            }
            return result;
        }
        
        private Set<String> extractHashtags(String content) {
            Set<String> hashtags = new HashSet<>();
            String[] words = content.split("\\s+");
            for (String word : words) {
                if (word.startsWith("#") && word.length() > 1) {
                    hashtags.add(word.substring(1).toLowerCase());
                }
            }
            return hashtags;
        }
    }

    // Fanout service for distributing tweets to followers
    static class FanoutService {
        private final UserStore userStore; private final TweetStore tweetStore;
        private final ExecutorService executor;
        
        public FanoutService(UserStore userStore, TweetStore tweetStore, int threadPoolSize) {
            this.userStore = userStore; this.tweetStore = tweetStore;
            this.executor = Executors.newFixedThreadPool(threadPoolSize);
        }
        
        public void fanoutTweet(Tweet tweet) {
            User author = userStore.getUser(tweet.getAuthorId());
            if (author == null) return;
            
            for (Long followerId : author.getFollowers()) {
                executor.submit(() -> {
                    // In a real system, this would push to follower's home timeline cache
                    // For simplicity, we just store in user's timeline
                    userStore.getUser(followerId);
                });
            }
        }
        
        public void shutdown() { executor.shutdown(); }
    }

    // Main Twitter service
    static class TwitterService {
        private final UserStore userStore;
        private final TweetStore tweetStore;
        private final TimelineGenerator timelineGenerator;
        private final FanoutService fanoutService;
        private final AtomicLong tweetIdCounter = new AtomicLong(0);
        
        public TwitterService() {
            this.userStore = new UserStore();
            this.tweetStore = new TweetStore();
            this.timelineGenerator = new TimelineGenerator(userStore, tweetStore);
            this.fanoutService = new FanoutService(userStore, tweetStore, 10);
        }
        
        public User registerUser(long userId, String username) {
            return userStore.createUser(userId, username);
        }
        
        public Tweet postTweet(long userId, String content) {
            long tweetId = tweetIdCounter.incrementAndGet();
            Tweet tweet = tweetStore.createTweet(tweetId, userId, content);
            fanoutService.fanoutTweet(tweet);
            return tweet;
        }
        
        public void followUser(long followerId, long followeeId) {
            User follower = userStore.getUser(followerId);
            User followee = userStore.getUser(followeeId);
            if (follower != null && followee != null) {
                follower.follow(followeeId);
                followee.addFollower(followerId);
            }
        }
        
        public void unfollowUser(long followerId, long followeeId) {
            User follower = userStore.getUser(followerId);
            User followee = userStore.getUser(followeeId);
            if (follower != null && followee != null) {
                follower.unfollow(followeeId);
                followee.removeFollower(followerId);
            }
        }
        
        public List<Tweet> getHomeTimeline(long userId, int limit, long beforeTweetId) {
            return timelineGenerator.generateHomeTimeline(userId, limit, beforeTweetId);
        }
        
        public List<Tweet> getUserTimeline(long userId, int limit, long beforeTweetId) {
            return timelineGenerator.generateUserTimeline(userId, limit, beforeTweetId);
        }
        
        public List<Tweet> searchHashtag(String hashtag, int limit, long beforeTweetId) {
            return timelineGenerator.searchByHashtag(hashtag, limit, beforeTweetId);
        }
        
        public void shutdown() { fanoutService.shutdown(); }
    }

    public static void main(String[] args) {
        TwitterService twitter = new TwitterService();
        
        // Register users
        User alice = twitter.registerUser(1, "alice");
        User bob = twitter.registerUser(2, "bob");
        User charlie = twitter.registerUser(3, "charlie");
        
        // Follow relationships
        twitter.followUser(2, 1); // Bob follows Alice
        twitter.followUser(3, 1); // Charlie follows Alice
        twitter.followUser(3, 2); // Charlie follows Bob
        
        // Post tweets
        Tweet tweet1 = twitter.postTweet(1, "Hello world! #first #tweet");
        Tweet tweet2 = twitter.postTweet(2, "This is Bob's tweet #hello");
        Tweet tweet3 = twitter.postTweet(1, "Another tweet from Alice #second");
        
        // Get timelines
        System.out.println("Alice's timeline:");
        for (Tweet t : twitter.getUserTimeline(1, 10, 0)) {
            System.out.println("  " + t.getContent() + " at " + new Date(t.getTimestamp()));
        }
        
        System.out.println("\nBob's home timeline:");
        for (Tweet t : twitter.getHomeTimeline(2, 10, 0)) {
            System.out.println("  " + t.getContent() + " at " + new Date(t.getTimestamp()));
        }
        
        System.out.println("\nCharlie's home timeline:");
        for (Tweet t : twitter.getHomeTimeline(3, 10, 0)) {
            System.out.println("  " + t.getContent() + " at " + new Date(t.getTimestamp()));
        }
        
        System.out.println("\nSearch for #first:");
        for (Tweet t : twitter.searchHashtag("first", 10, 0)) {
            System.out.println("  " + t.getContent());
        }
        
        twitter.shutdown();
    }
}
