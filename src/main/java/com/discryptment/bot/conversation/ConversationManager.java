package com.discryptment.bot.conversation;

import java.util.concurrent.*;

public class ConversationManager {
    private final ConcurrentMap<Long, Conversation> map = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final long timeoutSeconds;

    public ConversationManager(long timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public void startConversation(long telegramId, Conversation conv) {
        map.put(telegramId, conv);
        //schedule remove
        scheduler.schedule(() -> {
            Conversation c = map.get(telegramId);

            if (c != null && c == conv) {
                map.remove(telegramId, conv);
                //optionally notify user about timeout via bot client
            }
        }, timeoutSeconds, TimeUnit.SECONDS);
    }
    public Conversation getConversation(long telegramId){
        return map.get(telegramId);
    }
    public void endConversation(long telegramId){
        map.remove(telegramId);
    }
    public void shutdown(){
        scheduler.shutdownNow();
    }
}
