import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * System Design — Notification Service (High-Level Design)
 * Implements a multi-channel notification system like Firebase Cloud Messaging
 * Features: Push notifications, email, SMS, in-app, rate limiting, templates
 */
public class DesignNotificationService {

    // Notification data structure
    static class Notification {
        private final String notificationId; private final long userId; private final String title;
        private final String body; private final Map<String, String> data; private final NotificationType type;
        private final Set<Channel> channels; private final long createdTime; private final long scheduledTime;
        private NotificationStatus status;
        
        public Notification(String notificationId, long userId, String title, String body, 
                           Map<String, String> data, NotificationType type, Set<Channel> channels, long scheduledTime) {
            this.notificationId = notificationId; this.userId = userId; this.title = title; this.body = body;
            this.data = data; this.type = type; this.channels = channels; this.scheduledTime = scheduledTime;
            this.createdTime = System.currentTimeMillis(); this.status = NotificationStatus.PENDING;
        }
        
        public String getNotificationId() { return notificationId; }
        public long getUserId() { return userId; }
        public String getTitle() { return title; }
        public String getBody() { return body; }
        public Map<String, String> getData() { return data; }
        public NotificationType getType() { return type; }
        public Set<Channel> getChannels() { return channels; }
        public long getCreatedTime() { return createdTime; }
        public long getScheduledTime() { return scheduledTime; }
        public NotificationStatus getStatus() { return status; }
        public void setStatus(NotificationStatus status) { this.status = status; }
    }
    
    enum NotificationType { ALERT, PROMOTION, TRANSACTIONAL, SYSTEM }
    enum Channel { PUSH, EMAIL, SMS, IN_APP }
    enum NotificationStatus { PENDING, SENT, DELIVERED, FAILED, CANCELLED }

    // User preferences for notifications
    static class UserPreferences {
        private final long userId; private final Map<NotificationType, Set<Channel>> enabledChannels;
        private final Map<Channel, String> contactInfo; private boolean doNotDisturb;
        private final int doNotDisturbStart; private final int doNotDisturbEnd;
        
        public UserPreferences(long userId) {
            this.userId = userId; this.enabledChannels = new ConcurrentHashMap<>();
            this.contactInfo = new ConcurrentHashMap<>(); this.doNotDisturb = false;
            this.doNotDisturbStart = 22; this.doNotDisturbEnd = 8; // 10 PM to 8 AM
            // Default preferences
            enabledChannels.put(NotificationType.ALERT, new HashSet<>(Arrays.asList(Channel.PUSH,Channel.EMAIL)));
            enabledChannels.put(NotificationType.TRANSACTIONAL, new HashSet<>(Arrays.asList(Channel.PUSH, Channel.EMAIL, Channel.SMS)));
        }
        
        public long getUserId() { return userId; }
        public Set<Channel> getEnabledChannels(NotificationType type) { return enabledChannels.getOrDefault(type, Collections.emptySet()); }
        public void setEnabledChannels(NotificationType type, Set<Channel> channels) { enabledChannels.put(type, channels); }
        public String getContactInfo(Channel channel) { return contactInfo.get(channel); }
        public void setContactInfo(Channel channel, String info) { contactInfo.put(channel, info); }
        public boolean isDoNotDisturb() { return doNotDisturb; }
        public void setDoNotDisturb(boolean enabled) { this.doNotDisturb = enabled; }
        public boolean isInDoNotDisturbPeriod() {
            if (!doNotDisturb) return false;
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if (doNotDisturbStart < doNotDisturbEnd) {
                return hour >= doNotDisturbStart && hour < doNotDisturbEnd;
            } else {
                return hour >= doNotDisturbStart || hour < doNotDisturbEnd;
            }
        }
    }

    // Notification template
    static class NotificationTemplate {
        private final String templateId; private final String titleTemplate; private final String bodyTemplate;
        private final Map<String, String> defaultData;
        
        public NotificationTemplate(String templateId, String titleTemplate, String bodyTemplate, Map<String, String> defaultData) {
            this.templateId = templateId; this.titleTemplate = titleTemplate; this.bodyTemplate = bodyTemplate;
            this.defaultData = defaultData;
        }
        
        public String getTemplateId() { return templateId; }
        public String renderTitle(Map<String, String> variables) { return render(titleTemplate, variables); }
        public String renderBody(Map<String, String> variables) { return render(bodyTemplate, variables); }
        private String render(String template, Map<String, String> variables) {
            String result = template;
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                result = result.replace("{" + entry.getKey() + "}", entry.getValue());
            }
            return result;
        }
    }

    // Notification storage
    static class NotificationStore {
        private final ConcurrentHashMap<String, Notification> notifications = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<Long, List<String>> userNotifications = new ConcurrentHashMap<>();
        private final AtomicLong notificationIdCounter = new AtomicLong(0);
        
        public Notification createNotification(long userId, String title, String body, Map<String, String> data, 
                                              NotificationType type, Set<Channel> channels, long scheduledTime) {
            String notificationId = "notif_" + notificationIdCounter.incrementAndGet();
            Notification notification = new Notification(notificationId, userId, title, body, data, type, channels, scheduledTime);
            notifications.put(notificationId, notification);
            userNotifications.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(notificationId);
            return notification;
        }
        
        public Notification getNotification(String notificationId) { return notifications.get(notificationId); }
        
        public List<Notification> getUserNotifications(long userId) {
            List<Notification> result = new ArrayList<>();
            for (String notifId : userNotifications.getOrDefault(userId, Collections.emptyList())) {
                result.add(notifications.get(notifId));
            }
            return result;
        }
        
        public void updateNotificationStatus(String notificationId, NotificationStatus status) {
            Notification notification = notifications.get(notificationId);
            if (notification != null) notification.setStatus(status);
        }
    }

    // Template storage
    static class TemplateStore {
        private final ConcurrentHashMap<String, NotificationTemplate> templates = new ConcurrentHashMap<>();
        
        public void addTemplate(NotificationTemplate template) { templates.put(template.getTemplateId(), template); }
        public NotificationTemplate getTemplate(String templateId) { return templates.get(templateId); }
    }

    // User preferences storage
    static class UserPreferencesStore {
        private final ConcurrentHashMap<Long, UserPreferences> preferences = new ConcurrentHashMap<>();
        
        public UserPreferences getPreferences(long userId) {
            return preferences.computeIfAbsent(userId, UserPreferences::new);
        }
        
        public void updatePreferences(long userId, UserPreferences prefs) {
            preferences.put(userId, prefs);
        }
    }

    // Rate limiter for notification sending
    static class NotificationRateLimiter {
        private final ConcurrentHashMap<Long, Map<Channel, TokenBucket>> userBuckets = new ConcurrentHashMap<>();
        private final int capacity; private final long refillRateMillis;
        
        public NotificationRateLimiter(int capacity, long refillRateMillis) {
            this.capacity = capacity; this.refillRateMillis = refillRateMillis;
        }
        
        public boolean allowSend(long userId, Channel channel) {
            Map<Channel, TokenBucket> buckets = userBuckets.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
            TokenBucket bucket = buckets.computeIfAbsent(channel, k -> new TokenBucket(capacity, refillRateMillis));
            return bucket.tryConsume();
        }
    }
    
    static class TokenBucket {
        private int tokens; private final long refillRateMillis; private long lastRefillTime;
        public TokenBucket(int capacity, long refillRateMillis) { this.tokens = capacity; this.refillRateMillis = refillRateMillis; this.lastRefillTime = System.currentTimeMillis(); }
        public synchronized boolean tryConsume() {
            refill();
            if (tokens > 0) { tokens--; return true; }
            return false;
        }
        private void refill() {
            long now = System.currentTimeMillis(); long elapsed = now - lastRefillTime;
            if (elapsed >= refillRateMillis) { tokens = Math.min(tokens + (int)(elapsed / refillRateMillis), 10); lastRefillTime = now; }
        }
    }

    // Push notification sender
    static class PushNotificationSender {
        public boolean sendPush(long userId, String title, String body, Map<String, String> data) {
            System.out.println("Sending push notification to user " + userId + ": " + title);
            // In real system, integrate with FCM/APNs
            return true;
        }
    }

    // Email sender
    static class EmailSender {
        public boolean sendEmail(String to, String subject, String body) {
            System.out.println("Sending email to " + to + ": " + subject);
            // In real system, integrate with SES/SendGrid
            return true;
        }
    }

    // SMS sender
    static class SMSSender {
        public boolean sendSMS(String to, String message) {
            System.out.println("Sending SMS to " + to + ": " + message);
            // In real system, integrate with Twilio
            return true;
        }
    }

    // In-app notification sender
    static class InAppNotificationSender {
        private final ConcurrentHashMap<Long, Queue<Notification>> userQueues = new ConcurrentHashMap<>();
        
        public void sendInApp(long userId, Notification notification) {
            userQueues.computeIfAbsent(userId, k -> new ConcurrentLinkedQueue<>()).offer(notification);
            System.out.println("Queued in-app notification for user " + userId);
        }
        
        public List<Notification> getPendingNotifications(long userId) {
            Queue<Notification> queue = userQueues.get(userId);
            List<Notification> result = new ArrayList<>();
            if (queue != null) {
                while (!queue.isEmpty() && result.size() < 10) result.add(queue.poll());
            }
            return result;
        }
    }

    // Notification dispatcher
    static class NotificationDispatcher {
        private final PushNotificationSender pushSender;
        private final EmailSender emailSender;
        private final SMSSender smsSender;
        private final InAppNotificationSender inAppSender;
        private final UserPreferencesStore prefsStore;
        private final NotificationRateLimiter rateLimiter;
        private final ExecutorService executor;
        
        public NotificationDispatcher(UserPreferencesStore prefsStore, NotificationRateLimiter rateLimiter) {
            this.pushSender = new PushNotificationSender(); this.emailSender = new EmailSender();
            this.smsSender = new SMSSender(); this.inAppSender = new InAppNotificationSender();
            this.prefsStore = prefsStore; this.rateLimiter = rateLimiter;
            this.executor = Executors.newFixedThreadPool(10);
        }
        
        public void dispatch(Notification notification) {
            UserPreferences prefs = prefsStore.getPreferences(notification.getUserId());
            
            // Check do not disturb
            if (prefs.isInDoNotDisturbPeriod() && notification.getType() != NotificationType.ALERT) {
                notification.setStatus(NotificationStatus.CANCELLED);
                return;
            }
            
            for (Channel channel : notification.getChannels()) {
                if (!prefs.getEnabledChannels(notification.getType()).contains(channel)) continue;
                if (!rateLimiter.allowSend(notification.getUserId(), channel)) continue;
                
                executor.submit(() -> sendViaChannel(notification, channel, prefs));
            }
        }
        
        private void sendViaChannel(Notification notification, Channel channel, UserPreferences prefs) {
            boolean success = false;
            switch (channel) {
                case PUSH:
                    success = pushSender.sendPush(notification.getUserId(), notification.getTitle(), notification.getBody(), notification.getData());
                    break;
                case EMAIL:
                    String email = prefs.getContactInfo(Channel.EMAIL);
                    if (email != null) success = emailSender.sendEmail(email, notification.getTitle(), notification.getBody());
                    break;
                case SMS:
                    String phone = prefs.getContactInfo(Channel.SMS);
                    if (phone != null) success = smsSender.sendSMS(phone, notification.getBody());
                    break;
                case IN_APP:
                    inAppSender.sendInApp(notification.getUserId(), notification);
                    success = true;
                    break;
            }
            
            notification.setStatus(success ? NotificationStatus.SENT : NotificationStatus.FAILED);
        }
        
        public void shutdown() { executor.shutdown(); }
    }

    // Scheduled notification processor
    static class ScheduledNotificationProcessor {
        private final NotificationStore notificationStore;
        private final NotificationDispatcher dispatcher;
        private final ScheduledExecutorService scheduler;
        
        public ScheduledNotificationProcessor(NotificationStore notificationStore, NotificationDispatcher dispatcher) {
            this.notificationStore = notificationStore; this.dispatcher = dispatcher;
            this.scheduler = Executors.newSingleThreadScheduledExecutor();
            startProcessing();
        }
        
        private void startProcessing() {
            scheduler.scheduleAtFixedRate(() -> {
                long now = System.currentTimeMillis();
                for (Notification notif : notificationStore.getUserNotifications(0)) { // In real system, iterate all users
                    if (notif.getStatus() == NotificationStatus.PENDING && notif.getScheduledTime() <= now) {
                        dispatcher.dispatch(notif);
                    }
                }
            }, 1, 1, TimeUnit.SECONDS);
        }
        
        public void shutdown() { scheduler.shutdown(); }
    }

    // Main Notification service
    static class NotificationService {
        private final NotificationStore notificationStore;
        private final TemplateStore templateStore;
        private final UserPreferencesStore prefsStore;
        private final NotificationRateLimiter rateLimiter;
        private final NotificationDispatcher dispatcher;
        private final ScheduledNotificationProcessor scheduledProcessor;
        
        public NotificationService() {
            this.notificationStore = new NotificationStore();
            this.templateStore = new TemplateStore();
            this.prefsStore = new UserPreferencesStore();
            this.rateLimiter = new NotificationRateLimiter(10, 60000); // 10 per minute per channel
            this.dispatcher = new NotificationDispatcher(prefsStore, rateLimiter);
            this.scheduledProcessor = new ScheduledNotificationProcessor(notificationStore, dispatcher);
        }
        
        public void addTemplate(NotificationTemplate template) { templateStore.addTemplate(template); }
        
        public Notification sendNotification(long userId, String templateId, Map<String, String> variables, 
                                           NotificationType type, Set<Channel> channels, long delayMillis) {
            String title; String body;
            if (templateId != null) {
                NotificationTemplate template = templateStore.getTemplate(templateId);
                if (template == null) throw new RuntimeException("Template not found");
                title = template.renderTitle(variables);
                body = template.renderBody(variables);
            } else {
                title = (String) variables.getOrDefault("title", "");
                body = (String) variables.getOrDefault("body", "");
            }
            long scheduledTime = System.currentTimeMillis() + delayMillis;
            
            Notification notification = notificationStore.createNotification(userId, title, body, variables, type, channels, scheduledTime);
            
            if (delayMillis == 0) {
                dispatcher.dispatch(notification);
            }
            
            return notification;
        }
        
        public Notification sendDirectNotification(long userId, String title, String body, Map<String, String> data,
                                                  NotificationType type, Set<Channel> channels) {
            return sendNotification(userId, null, data, type, channels, 0);
        }
        
        public List<Notification> getUserNotifications(long userId) {
            return notificationStore.getUserNotifications(userId);
        }
        
        public void updateUserPreferences(long userId, UserPreferences prefs) {
            prefsStore.updatePreferences(userId, prefs);
        }
        
        public UserPreferences getUserPreferences(long userId) { return prefsStore.getPreferences(userId); }
        
        public void shutdown() { dispatcher.shutdown(); scheduledProcessor.shutdown(); }
    }

    public static void main(String[] args) throws InterruptedException {
        NotificationService notificationService = new NotificationService();
        
        // Add templates
        Map<String, String> welcomeData = new HashMap<>();
        welcomeData.put("username", "John");
        NotificationTemplate welcomeTemplate = new NotificationTemplate("welcome", "Welcome {username}!", 
            "Hi {username}, welcome to our platform!", welcomeData);
        notificationService.addTemplate(welcomeTemplate);
        
        // Update user preferences
        UserPreferences prefs = notificationService.getUserPreferences(1);
        prefs.setContactInfo(Channel.EMAIL, "user@example.com");
        prefs.setContactInfo(Channel.SMS, "+1234567890");
        notificationService.updateUserPreferences(1, prefs);
        
        // Send notification using template
        Map<String, String> variables = new HashMap<>();
        variables.put("username", "Alice");
        Notification notif1 = notificationService.sendNotification(1, "welcome", variables, 
            NotificationType.ALERT, new HashSet<>(Arrays.asList(Channel.PUSH, Channel.EMAIL)), 0);
        System.out.println("Sent notification: " + notif1.getNotificationId());
        
        // Send direct notification
        Notification notif2 = notificationService.sendDirectNotification(1, "Order Confirmed", 
            "Your order #12345 has been confirmed", new HashMap<>(), 
            NotificationType.TRANSACTIONAL, new HashSet<>(Arrays.asList(Channel.PUSH, Channel.SMS)));
        System.out.println("Sent notification: " + notif2.getNotificationId());
        
        // Schedule notification
        Notification notif3 = notificationService.sendNotification(1, "welcome", variables, 
            NotificationType.PROMOTION, new HashSet<>(Arrays.asList(Channel.PUSH)), 5000);
        System.out.println("Scheduled notification: " + notif3.getNotificationId());
        
        // Get user notifications
        Thread.sleep(100);
        System.out.println("\nUser notifications:");
        for (Notification notif : notificationService.getUserNotifications(1)) {
            System.out.println("  " + notif.getTitle() + " - " + notif.getStatus());
        }
        
        Thread.sleep(6000);
        System.out.println("\nUser notifications after scheduled:");
        for (Notification notif : notificationService.getUserNotifications(1)) {
            System.out.println("  " + notif.getTitle() + " - " + notif.getStatus());
        }
        
        notificationService.shutdown();
    }
}