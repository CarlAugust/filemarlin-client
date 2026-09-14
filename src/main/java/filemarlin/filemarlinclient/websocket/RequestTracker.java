package filemarlin.filemarlinclient.websocket;

import javafx.util.Pair;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RequestTracker {
    private final ConcurrentHashMap<String, CompletableFuture<Object>> pendingRequests = new ConcurrentHashMap<>();

    public Pair<String, CompletableFuture<Object>> registerRequest() {
        String id = UUID.randomUUID().toString();
        CompletableFuture<Object> future = new CompletableFuture<>();

        future.orTimeout(5, TimeUnit.SECONDS)
                .whenComplete((res, ex) -> pendingRequests.remove(id));

        pendingRequests.put(id, future);
        return new Pair<>(id, future);
    }

    public void completeRequest(String requestId, Object result) {
        pendingRequests.get(requestId).complete(result);
        pendingRequests.remove(requestId);
    }

    public void failRequest(String requestId, Exception e) {
        pendingRequests.get(requestId).completeExceptionally(e);
        pendingRequests.remove(requestId);
    }
}
