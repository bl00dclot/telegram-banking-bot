package com.discryptment.bot.conversation;

import java.util.HashMap;
import java.util.Map;

public class Conversation {
    private long telegramId;
    private ConversationState state;
    private long startedAt; // epoch millis
    private int attemptsLeft;
    private Map<String, Object> data = new HashMap<>(); // arbitrary metadata for the flow

    public Conversation(){};

    public Conversation(long telegramId, ConversationState state, long startedAt, int attemptsLeft){
        this.telegramId = telegramId;
        this.state = state;
        this.startedAt = startedAt;
        this. attemptsLeft = attemptsLeft;
    }

    // ----- getters -----

    public long getTelegramId() { return telegramId; }

    public ConversationState getState(){
        return state;
    }
    public long getStartedAt(){
        return startedAt;
    }
    public int getAttemptsLeft(){
        return attemptsLeft;
    }
    public Map<String, Object> getData(){
        return data;
    }

    // ----- setters -----
    public void setTelegramId(long telegramId){
        this.telegramId = telegramId;
    }
    public void setState(ConversationState state){
        this.state = state;
    }
    public void setStartedAt(long startedAt){
        this.startedAt = startedAt;
    }
    public void setAttemptsLeft(int attemptsLeft){
        this.attemptsLeft = attemptsLeft;
    }
    public void setData(Map<String, Object> data) {
        if (data == null) {
            this.data = new HashMap<>();
        } else {
            this.data = data;
        }
    }

    // ----- convenience helpers -----

    /**
     * Decrements the attempts counter by 1 and returns the remaining attempts.
     */
    public int decrementAttempts(){
        if(attemptsLeft > 0) attemptsLeft--;
        return attemptsLeft;
    }
    /**
     * Put arbitrary metadata into the conversation data map.
     */
    public void putData(String key, Object value){
        data.put(key, value);
    }
    /**
     * Convenience getter for conversation metadata.
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(String key, Class<T> clazz) {
        Object val = data.get(key);
        if (val == null) return null;
        return (T) val;
    }
    @Override
    public String toString() {
        return "Conversation{" +
                "telegramId=" + telegramId +
                ", state=" + state +
                ", startedAt=" + startedAt +
                ", attemptsLeft=" + attemptsLeft +
                ", dataKeys=" + (data == null ? 0 : data.size()) +
                '}';
    }

}

