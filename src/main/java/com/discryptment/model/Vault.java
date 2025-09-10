package com.discryptment.model;

public class Vault {
    private int id;
    private long goldG;
    private int pricePerG;
    private long version;

    private String action;
    private long changeGoldG;
    private long prevGoldG;
    private long newGoldG;
    private long prevPricePerG;
    private long newPricePerG;
    private long prevValueOfUSD; // prevPricePerG * prevGoldG
    private long newValueOfUSD; // newPricePerG * newGoldG
    private long actorTelegramId;
    private String reason;
    private String externalId;

    // Constructors
    public Vault() {}

    public Vault(int id, long goldG, int pricePerG, long version) {
        this.id = id;
        this.goldG = goldG;
        this.pricePerG = pricePerG;
        this.version = version;
    }

    // Getters
    public int getId() {
        return id;
    }

    public long getGoldG() {
        return goldG;
    }

    public int getPricePerG() {
        return pricePerG;
    }

    public long getVersion() {
        return version;
    }

    public String getAction() {
        return action;
    }

    public long getChangeGoldG() {
        return changeGoldG;
    }

    public long getPrevGoldG() {
        return prevGoldG;
    }

    public long getNewGoldG() {
        return newGoldG;
    }

    public long getPrevPricePerG() {
        return prevPricePerG;
    }

    public long getNewPricePerG() {
        return newPricePerG;
    }

    public long getPrevValueOfUSD() {
        return prevValueOfUSD;
    }

    public long getNewValueOfUSD() {
        return newValueOfUSD;
    }

    public long getActorTelegramId() {
        return actorTelegramId;
    }

    public String getReason() {
        return reason;
    }

    public String getExternalId() {
        return externalId;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setGoldG(long goldG) {
        this.goldG = goldG;
    }

    public void setPricePerG(int pricePerG) {
        this.pricePerG = pricePerG;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setChangeGoldG(long changeGoldG) {
        this.changeGoldG = changeGoldG;
    }

    public void setPrevGoldG(long prevGoldG) {
        this.prevGoldG = prevGoldG;
    }

    public void setNewGoldG(long newGoldG) {
        this.newGoldG = newGoldG;
    }

    public void setPrevPricePerG(long prevPricePerG) {
        this.prevPricePerG = prevPricePerG;
    }

    public void setNewPricePerG(long newPricePerG) {
        this.newPricePerG = newPricePerG;
    }

    public void setPrevValueOfUSD(long prevValueOfUSD) {
        this.prevValueOfUSD = prevValueOfUSD;
    }

    public void setNewValueOfUSD(long newValueOfUSD) {
        this.newValueOfUSD = newValueOfUSD;
    }

    public void setActorTelegramId(long actorTelegramId) {
        this.actorTelegramId = actorTelegramId;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    // Utility methods for calculated values
    public void calculatePrevValueOfUSD() {
        this.prevValueOfUSD = prevPricePerG * prevGoldG;
    }

    public void calculateNewValueOfUSD() {
        this.newValueOfUSD = newPricePerG * newGoldG;
    }

    public long getCurrentValue() {
        return pricePerG * goldG;
    }

    @Override
    public String toString() {
        return "Vault{" +
                "id=" + id +
                ", goldG=" + goldG +
                ", pricePerG=" + pricePerG +
                ", version=" + version +
                ", action='" + action + '\'' +
                ", changeGoldG=" + changeGoldG +
                ", prevGoldG=" + prevGoldG +
                ", newGoldG=" + newGoldG +
                ", prevPricePerG=" + prevPricePerG +
                ", newPricePerG=" + newPricePerG +
                ", prevValueOfUSD=" + prevValueOfUSD +
                ", newValueOfUSD=" + newValueOfUSD +
                ", actorTelegramId=" + actorTelegramId +
                ", reason='" + reason + '\'' +
                ", externalId='" + externalId + '\'' +
                '}';
    }
}

