package rs.ac.uns.ftn.informatika.rest.utils;

import java.util.HashMap;
import java.util.Map;

public class RateLimiter {
    private final Map<String, UserRequestInfo> userRequests = new HashMap<>();
    private final int MAX_REQUESTS = 5;
    private final long TIME_FRAME = 60 * 1000; // 1 minute in milliseconds

    public RateLimiter() {}

    public boolean isRequestAllowed(String identifier) {
        long currentTime = System.currentTimeMillis();
        UserRequestInfo userInfo = userRequests.get(identifier);

        if (userInfo == null) {
            userRequests.put(identifier, new UserRequestInfo(currentTime, 1));
            return true;
        }

        // Clean up old requests
        if (currentTime - userInfo.getFirstRequestTime() > TIME_FRAME) {
            userRequests.put(identifier, new UserRequestInfo(currentTime, 1));
            return true;
        }

        // Check the request count
        if (userInfo.getRequestCount() < MAX_REQUESTS) {
            userInfo.incrementRequestCount();
            return true;
        }

        return false;
    }

    private static class UserRequestInfo {
        private long firstRequestTime;
        private int requestCount;

        public UserRequestInfo(long firstRequestTime, int requestCount) {
            this.firstRequestTime = firstRequestTime;
            this.requestCount = requestCount;
        }

        public long getFirstRequestTime() {
            return firstRequestTime;
        }

        public int getRequestCount() {
            return requestCount;
        }

        public void incrementRequestCount() {
            this.requestCount++;
        }
    }
}

