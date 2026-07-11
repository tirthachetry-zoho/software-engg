import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * System Design — Chat System (High-Level Design)
 * Implements a real-time chat system like WhatsApp/Slack
 * Features: WebSocket support, message persistence, online status, group chats
 */
public class DesignChatSystem {

    // Message data structure
    static class Message {
        private final long messageId; private final long senderId; private final long conversationId;
        private final String content; private final long timestamp; private final MessageType type;
        
        public Message(long messageId, long senderId, long conversationId, String content, MessageType type) {
            this.messageId = messageId; this.senderId = senderId; this.conversationId = conversationId;
            this.content = content; this.type = type; this.timestamp = System.currentTimeMillis();
        }
        
        public long getMessageId() { return messageId; }
        public long getSenderId() { return senderId; }
        public long getConversationId() { return conversationId; }
        public String getContent() { return content; }
        public long getTimestamp() { return timestamp; }
        public MessageType getType() { return type; }
    }
    
    enum MessageType { TEXT, IMAGE, VIDEO, FILE, SYSTEM }

    // Conversation data structure
    static class Conversation {
        private final long conversationId; private final ConversationType type;
        private final Set<Long> participants; private final String name;
        
        public Conversation(long conversationId, ConversationType type, Set<Long> participants, String name) {
            this.conversationId = conversationId; this.type = type; this.participants = participants; this.name = name;
        }
        
        public long getConversationId() { return conversationId; }
        public ConversationType getType() { return type; }
        public Set<Long> getParticipants() { return participants; }
        public String getName() { return name; }
    }
    
    enum ConversationType { DIRECT, GROUP, CHANNEL }

    // User data structure with online status
    static class User {
        private final long userId; private final String username; private volatile boolean online;
        private volatile long lastSeen; private final Set<Long> activeConversations = ConcurrentHashMap.newKeySet();
        
        public User(long userId, String username) { this.userId = userId; this.username = username; this.lastSeen = System.currentTimeMillis(); }
        
        public long getUserId() { return userId; }
        public String getUsername() { return username; }
        public boolean isOnline() { return online; }
        public void setOnline(boolean online) { this.online = online; if (online) lastSeen = System.currentTimeMillis(); }
        public long getLastSeen() { return lastSeen; }
        public void addActiveConversation(long conversationId) { activeConversations.add(conversationId); }
        public void removeActiveConversation(long conversationId) { activeConversations.remove(conversationId); }
        public Set<Long> getActiveConversations() { return activeConversations; }
    }

    // Message store with pagination
    static class MessageStore {
        private final ConcurrentHashMap<Long, List<Message>> conversationMessages = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<Long, Message> messageCache = new ConcurrentHashMap<>();
        private final AtomicLong messageIdCounter = new AtomicLong(0);
        
        public Message createMessage(long senderId, long conversationId, String content, MessageType type) {
            long messageId = messageIdCounter.incrementAndGet();
            Message message = new Message(messageId, senderId, conversationId, content, type);
            
            conversationMessages.computeIfAbsent(conversationId, k -> new CopyOnWriteArrayList<>()).add(message);
            messageCache.put(messageId, message);
            
            return message;
        }
        
        public Message getMessage(long messageId) { return messageCache.get(messageId); }
        
        public List<Message> getConversationMessages(long conversationId, int limit, long beforeMessageId) {
            List<Message> messages = conversationMessages.getOrDefault(conversationId, Collections.emptyList());
            List<Message> result = new ArrayList<>();
            
            for (int i = messages.size() - 1; i >= 0 && result.size() < limit; i--) {
                Message msg = messages.get(i);
                if (beforeMessageId > 0 && msg.getMessageId() >= beforeMessageId) continue;
                result.add(msg);
            }
            
            Collections.reverse(result);
            return result;
        }
        
        public List<Message> getMessagesSince(long conversationId, long sinceTimestamp) {
            List<Message> messages = conversationMessages.getOrDefault(conversationId, Collections.emptyList());
            List<Message> result = new ArrayList<>();
            
            for (Message msg : messages) {
                if (msg.getTimestamp() >= sinceTimestamp) result.add(msg);
            }
            
            return result;
        }
    }

    // Conversation store
    static class ConversationStore {
        private final ConcurrentHashMap<Long, Conversation> conversations = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<Long, Set<Long>> userConversations = new ConcurrentHashMap<>();
        private final AtomicLong conversationIdCounter = new AtomicLong(0);
        
        public Conversation createDirectConversation(long user1Id, long user2Id) {
            long conversationId = conversationIdCounter.incrementAndGet();
            Set<Long> participants = new HashSet<>(Arrays.asList(user1Id, user2Id));
            Conversation conversation = new Conversation(conversationId, ConversationType.DIRECT, participants, null);
            conversations.put(conversationId, conversation);
            
            userConversations.computeIfAbsent(user1Id, k -> ConcurrentHashMap.newKeySet()).add(conversationId);
            userConversations.computeIfAbsent(user2Id, k -> ConcurrentHashMap.newKeySet()).add(conversationId);
            
            return conversation;
        }
        
        public Conversation createGroupConversation(String name, Set<Long> participants) {
            long conversationId = conversationIdCounter.incrementAndGet();
            Conversation conversation = new Conversation(conversationId, ConversationType.GROUP, participants, name);
            conversations.put(conversationId, conversation);
            
            for (Long userId : participants) {
                userConversations.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(conversationId);
            }
            
            return conversation;
        }
        
        public Conversation getConversation(long conversationId) { return conversations.get(conversationId); }
        
        public Set<Long> getUserConversations(long userId) {
            return userConversations.getOrDefault(userId, Collections.emptySet());
        }
        
        public void addParticipant(long conversationId, long userId) {
            Conversation conversation = conversations.get(conversationId);
            if (conversation != null) {
                conversation.getParticipants().add(userId);
                userConversations.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(conversationId);
            }
        }
        
        public void removeParticipant(long conversationId, long userId) {
            Conversation conversation = conversations.get(conversationId);
            if (conversation != null) {
                conversation.getParticipants().remove(userId);
                Set<Long> userConvos = userConversations.get(userId);
                if (userConvos != null) userConvos.remove(conversationId);
            }
        }
    }

    // User store with online status tracking
    static class UserStore {
        private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<String, Long> usernameToId = new ConcurrentHashMap<>();
        
        public User createUser(long userId, String username) {
            User user = new User(userId, username);
            users.put(userId, user);
            usernameToId.put(username, userId);
            return user;
        }
        
        public User getUser(long userId) { return users.get(userId); }
        public User getUserByUsername(String username) {
            Long userId = usernameToId.get(username);
            return userId != null ? users.get(userId) : null;
        }
        
        public void setOnlineStatus(long userId, boolean online) {
            User user = users.get(userId);
            if (user != null) user.setOnline(online);
        }
        
        public List<User> getOnlineUsers() {
            List<User> onlineUsers = new ArrayList<>();
            for (User user : users.values()) {
                if (user.isOnline()) onlineUsers.add(user);
            }
            return onlineUsers;
        }
    }

    // WebSocket connection manager
    static class ConnectionManager {
        private final ConcurrentHashMap<Long, Set<WebSocketConnection>> userConnections = new ConcurrentHashMap<>();
        
        public void addConnection(long userId, WebSocketConnection connection) {
            userConnections.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(connection);
        }
        
        public void removeConnection(long userId, WebSocketConnection connection) {
            Set<WebSocketConnection> connections = userConnections.get(userId);
            if (connections != null) {
                connections.remove(connection);
                if (connections.isEmpty()) userConnections.remove(userId);
            }
        }
        
        public void sendMessageToUser(long userId, Message message) {
            Set<WebSocketConnection> connections = userConnections.get(userId);
            if (connections != null) {
                for (WebSocketConnection conn : connections) {
                    conn.send(message);
                }
            }
        }
        
        public void broadcastToConversation(long conversationId, Message message, UserStore userStore, ConversationStore conversationStore) {
            Conversation conversation = conversationStore.getConversation(conversationId);
            if (conversation != null) {
                for (Long participantId : conversation.getParticipants()) {
                    sendMessageToUser(participantId, message);
                }
            }
        }
    }
    
    static class WebSocketConnection {
        private final long connectionId; private final long userId;
        public WebSocketConnection(long connectionId, long userId) { this.connectionId = connectionId; this.userId = userId; }
        public void send(Message message) { System.out.println("Sending message " + message.getMessageId() + " to user " + userId); }
    }

    // Presence service for online status
    static class PresenceService {
        private final UserStore userStore; private final ScheduledExecutorService scheduler;
        
        public PresenceService(UserStore userStore) {
            this.userStore = userStore;
            this.scheduler = Executors.newSingleThreadScheduledExecutor();
            startHeartbeatCheck();
        }
        
        public void userConnected(long userId) {
            userStore.setOnlineStatus(userId, true);
        }
        
        public void userDisconnected(long userId) {
            userStore.setOnlineStatus(userId, false);
        }
        
        private void startHeartbeatCheck() {
            scheduler.scheduleAtFixedRate(() -> {
                // In a real system, check for inactive connections and mark users offline
                System.out.println("Checking heartbeat for all users");
            }, 30, 30, TimeUnit.SECONDS);
        }
        
        public void shutdown() { scheduler.shutdown(); }
    }

    // Main Chat service
    static class ChatService {
        private final UserStore userStore;
        private final ConversationStore conversationStore;
        private final MessageStore messageStore;
        private final ConnectionManager connectionManager;
        private final PresenceService presenceService;
        
        public ChatService() {
            this.userStore = new UserStore();
            this.conversationStore = new ConversationStore();
            this.messageStore = new MessageStore();
            this.connectionManager = new ConnectionManager();
            this.presenceService = new PresenceService(userStore);
        }
        
        public User registerUser(long userId, String username) {
            return userStore.createUser(userId, username);
        }
        
        public Conversation createDirectConversation(long user1Id, long user2Id) {
            return conversationStore.createDirectConversation(user1Id, user2Id);
        }
        
        public Conversation createGroupConversation(String name, Set<Long> participants) {
            return conversationStore.createGroupConversation(name, participants);
        }
        
        public Message sendMessage(long senderId, long conversationId, String content, MessageType type) {
            Message message = messageStore.createMessage(senderId, conversationId, content, type);
            connectionManager.broadcastToConversation(conversationId, message, userStore, conversationStore);
            return message;
        }
        
        public List<Message> getConversationHistory(long conversationId, int limit, long beforeMessageId) {
            return messageStore.getConversationMessages(conversationId, limit, beforeMessageId);
        }
        
        public List<Message> getMessagesSince(long conversationId, long sinceTimestamp) {
            return messageStore.getMessagesSince(conversationId, sinceTimestamp);
        }
        
        public void userConnected(long userId, WebSocketConnection connection) {
            presenceService.userConnected(userId);
            connectionManager.addConnection(userId, connection);
        }
        
        public void userDisconnected(long userId, WebSocketConnection connection) {
            presenceService.userDisconnected(userId);
            connectionManager.removeConnection(userId, connection);
        }
        
        public Set<Long> getUserConversations(long userId) {
            return conversationStore.getUserConversations(userId);
        }
        
        public List<User> getOnlineUsers() { return userStore.getOnlineUsers(); }
        
        public void shutdown() { presenceService.shutdown(); }
    }

    public static void main(String[] args) throws InterruptedException {
        ChatService chatService = new ChatService();
        
        // Register users
        User alice = chatService.registerUser(1, "alice");
        User bob = chatService.registerUser(2, "bob");
        User charlie = chatService.registerUser(3, "charlie");
        
        // Create conversations
        Conversation directChat = chatService.createDirectConversation(1, 2);
        Conversation groupChat = chatService.createGroupConversation("Team", new HashSet<>(Arrays.asList(1L, 2L, 3L)));
        
        // Simulate connections
        WebSocketConnection aliceConn = new WebSocketConnection(1, 1);
        WebSocketConnection bobConn = new WebSocketConnection(2, 2);
        WebSocketConnection charlieConn = new WebSocketConnection(3, 3);
        
        chatService.userConnected(1, aliceConn);
        chatService.userConnected(2, bobConn);
        chatService.userConnected(3, charlieConn);
        
        // Send messages
        Message msg1 = chatService.sendMessage(1, directChat.getConversationId(), "Hi Bob!", MessageType.TEXT);
        Message msg2 = chatService.sendMessage(2, directChat.getConversationId(), "Hello Alice!", MessageType.TEXT);
        Message msg3 = chatService.sendMessage(3, groupChat.getConversationId(), "Hey team!", MessageType.TEXT);
        
        // Get conversation history
        System.out.println("Direct chat history:");
        for (Message msg : chatService.getConversationHistory(directChat.getConversationId(), 10, 0)) {
            System.out.println("  User " + msg.getSenderId() + ": " + msg.getContent());
        }
        
        System.out.println("\nGroup chat history:");
        for (Message msg : chatService.getConversationHistory(groupChat.getConversationId(), 10, 0)) {
            System.out.println("  User " + msg.getSenderId() + ": " + msg.getContent());
        }
        
        // Get online users
        System.out.println("\nOnline users:");
        for (User user : chatService.getOnlineUsers()) {
            System.out.println("  " + user.getUsername());
        }
        
        chatService.shutdown();
    }
}
