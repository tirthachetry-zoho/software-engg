import java.util.*;
import java.util.concurrent.*;

/**
 * System Design — File Storage System (High-Level Design)
 * Implements a distributed file storage system like Dropbox/Google Drive
 * Features: Chunking, deduplication, versioning, sync, access control
 */
public class DesignFileStorage {

    // File metadata
    static class FileMetadata {
        private final String fileId; private final String fileName; private final long fileSize;
        private final String mimeType; private final long ownerId; private final long createdTime;
        private final long modifiedTime; private final int version; private final List<String> chunkIds;
        private final Set<String> sharedWith;
        
        public FileMetadata(String fileId, String fileName, long fileSize, String mimeType, long ownerId, List<String> chunkIds) {
            this.fileId = fileId; this.fileName = fileName; this.fileSize = fileSize; this.mimeType = mimeType;
            this.ownerId = ownerId; this.createdTime = System.currentTimeMillis(); this.modifiedTime = createdTime;
            this.version = 1; this.chunkIds = chunkIds; this.sharedWith = ConcurrentHashMap.newKeySet();
        }
        
        public String getFileId() { return fileId; }
        public String getFileName() { return fileName; }
        public long getFileSize() { return fileSize; }
        public String getMimeType() { return mimeType; }
        public long getOwnerId() { return ownerId; }
        public long getCreatedTime() { return createdTime; }
        public long getModifiedTime() { return modifiedTime; }
        public int getVersion() { return version; }
        public List<String> getChunkIds() { return chunkIds; }
        public Set<String> getSharedWith() { return sharedWith; }
    }

    // Chunk data structure
    static class Chunk {
        private final String chunkId; private final byte[] data; private final String checksum;
        private final int size; private final long createdTime;
        
        public Chunk(String chunkId, byte[] data, String checksum) {
            this.chunkId = chunkId; this.data = data; this.checksum = checksum;
            this.size = data.length; this.createdTime = System.currentTimeMillis();
        }
        
        public String getChunkId() { return chunkId; }
        public byte[] getData() { return data; }
        public String getChecksum() { return checksum; }
        public int getSize() { return size; }
    }

    // User data structure
    static class User {
        private final long userId; private final String username; private final String email;
        private final long storageQuota; private long usedStorage;
        
        public User(long userId, String username, String email, long storageQuota) {
            this.userId = userId; this.username = username; this.email = email;
            this.storageQuota = storageQuota; this.usedStorage = 0;
        }
        
        public long getUserId() { return userId; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public long getStorageQuota() { return storageQuota; }
        public long getUsedStorage() { return usedStorage; }
        public boolean hasStorage(long size) { return usedStorage + size <= storageQuota; }
        public void useStorage(long size) { usedStorage += size; }
        public void freeStorage(long size) { usedStorage = Math.max(0, usedStorage - size); }
    }

    // Chunk storage with deduplication
    static class ChunkStorage {
        private final ConcurrentHashMap<String, Chunk> chunks = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<String, String> checksumToChunkId = new ConcurrentHashMap<>();
        private final AtomicLong chunkIdCounter = new AtomicLong(0);
        
        public Chunk storeChunk(byte[] data) {
            String checksum = calculateChecksum(data);
            
            // Check for deduplication
            String existingChunkId = checksumToChunkId.get(checksum);
            if (existingChunkId != null) {
                return chunks.get(existingChunkId);
            }
            
            String chunkId = "chunk_" + chunkIdCounter.incrementAndGet();
            Chunk chunk = new Chunk(chunkId, data, checksum);
            chunks.put(chunkId, chunk);
            checksumToChunkId.put(checksum, chunkId);
            
            return chunk;
        }
        
        public Chunk getChunk(String chunkId) { return chunks.get(chunkId); }
        
        public void deleteChunk(String chunkId) {
            Chunk chunk = chunks.get(chunkId);
            if (chunk != null) {
                chunks.remove(chunkId);
                checksumToChunkId.remove(chunk.getChecksum());
            }
        }
        
        private String calculateChecksum(byte[] data) {
            // Simple checksum for demonstration (use SHA-256 in production)
            int sum = 0;
            for (byte b : data) sum += b;
            return String.valueOf(sum);
        }
    }

    // File metadata storage
    static class FileMetadataStore {
        private final ConcurrentHashMap<String, FileMetadata> files = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<Long, Set<String>> userFiles = new ConcurrentHashMap<>();
        private final AtomicLong fileIdCounter = new AtomicLong(0);
        
        public FileMetadata createFile(String fileName, long fileSize, String mimeType, long ownerId, List<String> chunkIds) {
            String fileId = "file_" + fileIdCounter.incrementAndGet();
            FileMetadata metadata = new FileMetadata(fileId, fileName, fileSize, mimeType, ownerId, chunkIds);
            files.put(fileId, metadata);
            userFiles.computeIfAbsent(ownerId, k -> ConcurrentHashMap.newKeySet()).add(fileId);
            return metadata;
        }
        
        public FileMetadata getFile(String fileId) { return files.get(fileId); }
        
        public Set<String> getUserFiles(long userId) {
            return userFiles.getOrDefault(userId, Collections.emptySet());
        }
        
        public void deleteFile(String fileId, long userId) {
            FileMetadata file = files.get(fileId);
            if (file != null && file.getOwnerId() == userId) {
                files.remove(fileId);
                Set<String> userFileSet = userFiles.get(userId);
                if (userFileSet != null) userFileSet.remove(fileId);
            }
        }
        
        public void shareFile(String fileId, long userId, String shareWithUsername) {
            FileMetadata file = files.get(fileId);
            if (file != null && file.getOwnerId() == userId) {
                file.getSharedWith().add(shareWithUsername);
            }
        }
    }

    // User storage
    static class UserStore {
        private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<String, Long> usernameToId = new ConcurrentHashMap<>();
        private final AtomicLong userIdCounter = new AtomicLong(0);
        
        public User createUser(String username, String email, long storageQuota) {
            long userId = userIdCounter.incrementAndGet();
            User user = new User(userId, username, email, storageQuota);
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

    // File upload service with chunking
    static class FileUploadService {
        private final ChunkStorage chunkStorage;
        private final FileMetadataStore metadataStore;
        private final UserStore userStore;
        private static final int CHUNK_SIZE = 4 * 1024 * 1024; // 4MB chunks
        
        public FileUploadService(ChunkStorage chunkStorage, FileMetadataStore metadataStore, UserStore userStore) {
            this.chunkStorage = chunkStorage; this.metadataStore = metadataStore; this.userStore = userStore;
        }
        
        public FileMetadata uploadFile(long userId, String fileName, byte[] fileData, String mimeType) {
            User user = userStore.getUser(userId);
            if (user == null) throw new RuntimeException("User not found");
            if (!user.hasStorage(fileData.length)) throw new RuntimeException("Insufficient storage");
            
            // Chunk the file
            List<String> chunkIds = new ArrayList<>();
            for (int i = 0; i < fileData.length; i += CHUNK_SIZE) {
                int chunkSize = Math.min(CHUNK_SIZE, fileData.length - i);
                byte[] chunkData = Arrays.copyOfRange(fileData, i, i + chunkSize);
                Chunk chunk = chunkStorage.storeChunk(chunkData);
                chunkIds.add(chunk.getChunkId());
            }
            
            // Create file metadata
            FileMetadata metadata = metadataStore.createFile(fileName, fileData.length, mimeType, userId, chunkIds);
            user.useStorage(fileData.length);
            
            return metadata;
        }
        
        public byte[] downloadFile(String fileId, long userId) {
            FileMetadata metadata = metadataStore.getFile(fileId);
            if (metadata == null) throw new RuntimeException("File not found");
            if (metadata.getOwnerId() != userId && !metadata.getSharedWith().contains(userStore.getUser(userId).getUsername())) {
                throw new RuntimeException("Access denied");
            }
            
            // Reassemble file from chunks
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            for (String chunkId : metadata.getChunkIds()) {
                Chunk chunk = chunkStorage.getChunk(chunkId);
                if (chunk != null) outputStream.write(chunk.getData(), 0, chunk.getSize());
            }
            
            return outputStream.toByteArray();
        }
        
        public void deleteFile(String fileId, long userId) {
            FileMetadata metadata = metadataStore.getFile(fileId);
            if (metadata == null) return;
            
            User user = userStore.getUser(userId);
            if (metadata.getOwnerId() == userId) {
                // Free storage
                user.freeStorage(metadata.getFileSize());
                
                // Delete chunks (in real system, check if chunks are used by other files)
                for (String chunkId : metadata.getChunkIds()) {
                    chunkStorage.deleteChunk(chunkId);
                }
                
                // Delete metadata
                metadataStore.deleteFile(fileId, userId);
            }
        }
    }

    // File sync service
    static class FileSyncService {
        private final FileMetadataStore metadataStore;
        private final UserStore userStore;
        
        public FileSyncService(FileMetadataStore metadataStore, UserStore userStore) {
            this.metadataStore = metadataStore; this.userStore = userStore;
        }
        
        public List<FileMetadata> getSyncFiles(long userId, long sinceTimestamp) {
            List<FileMetadata> syncFiles = new ArrayList<>();
            for (String fileId : metadataStore.getUserFiles(userId)) {
                FileMetadata metadata = metadataStore.getFile(fileId);
                if (metadata != null && metadata.getModifiedTime() >= sinceTimestamp) {
                    syncFiles.add(metadata);
                }
            }
            return syncFiles;
        }
    }

    // Access control service
    static class AccessControlService {
        private final FileMetadataStore metadataStore;
        private final UserStore userStore;
        
        public AccessControlService(FileMetadataStore metadataStore, UserStore userStore) {
            this.metadataStore = metadataStore; this.userStore = userStore;
        }
        
        public boolean hasAccess(String fileId, long userId) {
            FileMetadata metadata = metadataStore.getFile(fileId);
            if (metadata == null) return false;
            
            if (metadata.getOwnerId() == userId) return true;
            
            User user = userStore.getUser(userId);
            return metadata.getSharedWith().contains(user.getUsername());
        }
        
        public void grantAccess(String fileId, long ownerId, String shareWithUsername) {
            metadataStore.shareFile(fileId, ownerId, shareWithUsername);
        }
    }

    // Main File Storage service
    static class FileStorageService {
        private final UserStore userStore;
        private final ChunkStorage chunkStorage;
        private final FileMetadataStore metadataStore;
        private final FileUploadService uploadService;
        private final FileSyncService syncService;
        private final AccessControlService accessControlService;
        
        public FileStorageService() {
            this.userStore = new UserStore();
            this.chunkStorage = new ChunkStorage();
            this.metadataStore = new FileMetadataStore();
            this.uploadService = new FileUploadService(chunkStorage, metadataStore, userStore);
            this.syncService = new FileSyncService(metadataStore, userStore);
            this.accessControlService = new AccessControlService(metadataStore, userStore);
        }
        
        public User registerUser(String username, String email, long storageQuota) {
            return userStore.createUser(username, email, storageQuota);
        }
        
        public FileMetadata uploadFile(long userId, String fileName, byte[] fileData, String mimeType) {
            return uploadService.uploadFile(userId, fileName, fileData, mimeType);
        }
        
        public byte[] downloadFile(String fileId, long userId) {
            return uploadService.downloadFile(fileId, userId);
        }
        
        public void deleteFile(String fileId, long userId) {
            uploadService.deleteFile(fileId, userId);
        }
        
        public List<FileMetadata> listFiles(long userId) {
            List<FileMetadata> files = new ArrayList<>();
            for (String fileId : metadataStore.getUserFiles(userId)) {
                files.add(metadataStore.getFile(fileId));
            }
            return files;
        }
        
        public List<FileMetadata> getSyncFiles(long userId, long sinceTimestamp) {
            return syncService.getSyncFiles(userId, sinceTimestamp);
        }
        
        public void shareFile(String fileId, long ownerId, String shareWithUsername) {
            accessControlService.grantAccess(fileId, ownerId, shareWithUsername);
        }
        
        public boolean hasAccess(String fileId, long userId) {
            return accessControlService.hasAccess(fileId, userId);
        }
    }

    public static void main(String[] args) {
        FileStorageService storageService = new FileStorageService();
        
        // Register users
        User alice = storageService.registerUser("alice", "alice@example.com", 10L * 1024 * 1024 * 1024); // 10GB
        User bob = storageService.registerUser("bob", "bob@example.com", 5L * 1024 * 1024 * 1024); // 5GB
        
        // Upload files
        byte[] fileData = "This is a test file content".getBytes();
        FileMetadata file1 = storageService.uploadFile(alice.getUserId(), "test.txt", fileData, "text/plain");
        
        System.out.println("Uploaded file: " + file1.getFileName() + " (ID: " + file1.getFileId() + ")");
        System.out.println("File size: " + file1.getFileSize() + " bytes");
        System.out.println("Chunks: " + file1.getChunkIds().size());
        
        // List files
        System.out.println("\nAlice's files:");
        for (FileMetadata file : storageService.listFiles(alice.getUserId())) {
            System.out.println("  " + file.getFileName() + " - " + file.getFileSize() + " bytes");
        }
        
        // Download file
        byte[] downloaded = storageService.downloadFile(file1.getFileId(), alice.getUserId());
        System.out.println("\nDownloaded content: " + new String(downloaded));
        
        // Share file
        storageService.shareFile(file1.getFileId(), alice.getUserId(), "bob");
        System.out.println("\nFile shared with bob");
        
        // Check access
        System.out.println("Bob can access file: " + storageService.hasAccess(file1.getFileId(), bob.getUserId()));
        
        // Get sync files
        long syncTimestamp = System.currentTimeMillis() - 60000; // 1 minute ago
        List<FileMetadata> syncFiles = storageService.getSyncFiles(alice.getUserId(), syncTimestamp);
        System.out.println("\nFiles to sync since 1 minute ago: " + syncFiles.size());
        
        // Delete file
        storageService.deleteFile(file1.getFileId(), alice.getUserId());
        System.out.println("\nFile deleted");
        
        System.out.println("Alice's used storage: " + alice.getUsedStorage() + " bytes");
    }
}
