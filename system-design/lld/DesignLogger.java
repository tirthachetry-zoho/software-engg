import java.util.*;
import java.util.concurrent.*;
import java.io.*;

/**
 * System Design — Logger System (Low-Level Design)
 * Implements a multi-level logging system with different appenders
 * Features: Log levels, file rotation, async logging, filtering
 */
public class DesignLogger {

    // Log levels
    enum LogLevel { TRACE, DEBUG, INFO, WARN, ERROR, FATAL }

    // Log message
    static class LogMessage {
        private final LogLevel level; private final String message; private final long timestamp;
        private final String threadName; private final String loggerName;
        
        public LogMessage(LogLevel level, String message, String loggerName) {
            this.level = level; this.message = message; this.loggerName = loggerName;
            this.timestamp = System.currentTimeMillis(); this.threadName = Thread.currentThread().getName();
        }
        
        public LogLevel getLevel() { return level; }
        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }
        public String getThreadName() { return threadName; }
        public String getLoggerName() { return loggerName; }
        
        @Override
        public String toString() {
            return String.format("[%s] [%s] [%s] [%s] %s", 
                new Date(timestamp), level, threadName, loggerName, message);
        }
    }

    // Log appender interface
    interface LogAppender {
        void append(LogMessage message);
        void flush();
        void close();
    }

    // Console appender
    static class ConsoleAppender implements LogAppender {
        private final LogLevel minLevel;
        
        public ConsoleAppender(LogLevel minLevel) { this.minLevel = minLevel; }
        
        @Override
        public void append(LogMessage message) {
            if (message.getLevel().ordinal() >= minLevel.ordinal()) {
                System.out.println(message);
            }
        }
        
        @Override
        public void flush() { System.out.flush(); }
        
        @Override
        public void close() { flush(); }
    }

    // File appender with rotation
    static class FileAppender implements LogAppender {
        private final String filePath; private final LogLevel minLevel;
        private final long maxFileSize; private int fileIndex;
        private BufferedWriter writer; private long currentSize;
        
        public FileAppender(String filePath, LogLevel minLevel, long maxFileSize) {
            this.filePath = filePath; this.minLevel = minLevel; this.maxFileSize = maxFileSize;
            this.fileIndex = 0; openFile();
        }
        
        private void openFile() {
            try {
                String currentFile = filePath + (fileIndex > 0 ? "." + fileIndex : "");
                writer = new BufferedWriter(new FileWriter(currentFile, true));
                currentSize = new File(currentFile).length();
            } catch (IOException e) {
                throw new RuntimeException("Failed to open log file", e);
            }
        }
        
        @Override
        public void append(LogMessage message) {
            if (message.getLevel().ordinal() < minLevel.ordinal()) return;
            
            try {
                String logLine = message.toString() + "\n";
                byte[] bytes = logLine.getBytes();
                
                if (currentSize + bytes.length > maxFileSize) {
                    rotateFile();
                }
                
                writer.write(logLine);
                writer.flush();
                currentSize += bytes.length;
            } catch (IOException e) {
                System.err.println("Failed to write log: " + e.getMessage());
            }
        }
        
        private void rotateFile() {
            try {
                writer.close();
                fileIndex++;
                openFile();
            } catch (IOException e) {
                System.err.println("Failed to rotate log file: " + e.getMessage());
            }
        }
        
        @Override
        public void flush() {
            try { writer.flush(); } catch (IOException e) { /* ignore */ }
        }
        
        @Override
        public void close() {
            try { writer.close(); } catch (IOException e) { /* ignore */ }
        }
    }

    // Async appender wrapper
    static class AsyncAppender implements LogAppender {
        private final LogAppender delegate;
        private final BlockingQueue<LogMessage> queue;
        private final Thread workerThread;
        private volatile boolean running = true;
        
        public AsyncAppender(LogAppender delegate, int queueSize) {
            this.delegate = delegate;
            this.queue = new LinkedBlockingQueue<>(queueSize);
            this.workerThread = new Thread(this::processLogs);
            this.workerThread.setDaemon(true);
            this.workerThread.start();
        }
        
        private void processLogs() {
            while (running || !queue.isEmpty()) {
                try {
                    LogMessage message = queue.poll(100, TimeUnit.MILLISECONDS);
                    if (message != null) {
                        delegate.append(message);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        
        @Override
        public void append(LogMessage message) {
            if (!queue.offer(message)) {
                System.err.println("Log queue full, dropping message: " + message);
            }
        }
        
        @Override
        public void flush() {
            delegate.flush();
        }
        
        @Override
        public void close() {
            running = false;
            try {
                workerThread.join(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            delegate.close();
        }
    }

    // Log filter
    interface LogFilter {
        boolean shouldLog(LogMessage message);
    }
    
    static class LevelFilter implements LogFilter {
        private final LogLevel minLevel;
        public LevelFilter(LogLevel minLevel) { this.minLevel = minLevel; }
        @Override
        public boolean shouldLog(LogMessage message) {
            return message.getLevel().ordinal() >= minLevel.ordinal();
        }
    }
    
    static class ThreadFilter implements LogFilter {
        private final Set<String> allowedThreads;
        public ThreadFilter(Set<String> allowedThreads) { this.allowedThreads = allowedThreads; }
        @Override
        public boolean shouldLog(LogMessage message) {
            return allowedThreads.contains(message.getThreadName());
        }
    }

    // Logger class
    static class Logger {
        private final String name; private final LogLevel level;
        private final List<LogAppender> appenders;
        private final List<LogFilter> filters;
        
        public Logger(String name, LogLevel level) {
            this.name = name; this.level = level;
            this.appenders = new CopyOnWriteArrayList<>();
            this.filters = new CopyOnWriteArrayList<>();
        }
        
        public void addAppender(LogAppender appender) { appenders.add(appender); }
        public void addFilter(LogFilter filter) { filters.add(filter); }
        
        public void setLevel(LogLevel level) { /* In real implementation, update level */ }
        
        public void trace(String message) { log(LogLevel.TRACE, message); }
        public void debug(String message) { log(LogLevel.DEBUG, message); }
        public void info(String message) { log(LogLevel.INFO, message); }
        public void warn(String message) { log(LogLevel.WARN, message); }
        public void error(String message) { log(LogLevel.ERROR, message); }
        public void fatal(String message) { log(LogLevel.FATAL, message); }
        
        public void log(LogLevel level, String message) {
            if (level.ordinal() < this.level.ordinal()) return;
            
            LogMessage logMessage = new LogMessage(level, message, name);
            
            for (LogFilter filter : filters) {
                if (!filter.shouldLog(logMessage)) return;
            }
            
            for (LogAppender appender : appenders) {
                appender.append(logMessage);
            }
        }
    }

    // Logger factory
    static class LoggerFactory {
        private static final Map<String, Logger> loggers = new ConcurrentHashMap<>();
        private static final List<LogAppender> globalAppenders = new CopyOnWriteArrayList<>();
        private static LogLevel globalLevel = LogLevel.INFO;
        
        public static void addGlobalAppender(LogAppender appender) { globalAppenders.add(appender); }
        public static void setGlobalLevel(LogLevel level) { globalLevel = level; }
        
        public static Logger getLogger(String name) {
            return loggers.computeIfAbsent(name, k -> {
                Logger logger = new Logger(name, globalLevel);
                for (LogAppender appender : globalAppenders) {
                    logger.addAppender(appender);
                }
                return logger;
            });
        }
        
        public static void shutdown() {
            for (LogAppender appender : globalAppenders) {
                appender.close();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Configure global appenders
        LoggerFactory.addGlobalAppender(new ConsoleAppender(LogLevel.DEBUG));
        LoggerFactory.addGlobalAppender(new AsyncAppender(new FileAppender("app.log", LogLevel.INFO, 1024 * 1024), 1000));
        LoggerFactory.setGlobalLevel(LogLevel.DEBUG);
        
        // Get loggers
        Logger mainLogger = LoggerFactory.getLogger("Main");
        Logger serviceLogger = LoggerFactory.getLogger("Service");
        
        // Add specific filter to service logger
        serviceLogger.addFilter(new LevelFilter(LogLevel.WARN));
        
        // Log messages
        mainLogger.trace("This is a trace message");
        mainLogger.debug("Debugging information");
        mainLogger.info("Application started");
        mainLogger.warn("Warning: low memory");
        mainLogger.error("Error occurred");
        mainLogger.fatal("Fatal error!");
        
        serviceLogger.info("Service message (should be filtered)");
        serviceLogger.warn("Service warning");
        serviceLogger.error("Service error");
        
        Thread.sleep(1000); // Wait for async appender
        
        LoggerFactory.shutdown();
    }
}
