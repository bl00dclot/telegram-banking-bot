package com.discryptment.bot.conversation;

import java.util.concurrent.*;

public class ConversationManager {
    private final ConcurrentMap<Long, ConversationInterface> map = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final long timeoutSeconds;

    public ConversationManager(long timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
        //periodic clean-up
//                scheduler.scheduleAtFixedRate(this::cleanupExpired, timeoutSeconds, timeoutSeconds, TimeUnit.SECONDS);
    }

    public void startConversation(long telegramId, ConversationInterface conv) {
        map.put(telegramId, conv);
        //schedule remove
        scheduler.schedule(() -> {
            ConversationInterface c = map.get(telegramId);

            if (c != null && c == conv) {
                map.remove(telegramId, conv);
                //optionally notify user about timeout via bot client
            }
        }, timeoutSeconds, TimeUnit.SECONDS);
    }
    public ConversationInterface getConversation(long telegramId){
//    	if (c != null) {
//            // optional cleanup
//        }
        return map.get(telegramId);
    }
    public void endConversation(long telegramId){
        map.remove(telegramId);
    }
    public void touch(long tgId) {
        // update last-active timestamp if you store it for expiration
        // implement per-conversation metadata if needed
    }
    public void shutdown(){
        scheduler.shutdownNow();
    }
/*
 *     private void scheduleExpiry(long tgId) {
        scheduler.schedule(() -> {
            Conversation c = convs.get(tgId);
            if (c != null && c.getStatus() == Conversation.Status.ACTIVE) {
                // expire
                convs.remove(tgId);
                // call onCancel to notify user
                // You need a CommandContext factory or store ctx inside conv snapshot
            }
        }, timeoutSeconds, TimeUnit.SECONDS);
    }

    private void cleanupExpired() {
        // optional: iterate and remove stale by last-active timestamp
    }
 * */    
}
