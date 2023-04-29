package com.example.demo;

import java.util.HashMap;
import java.util.Map;

public class Auction {
    private Property property;
    private Map<Player, Integer> bids;

    public Auction(Property property) {
        this.property = property;
        this.bids = new HashMap<>();
    }

    public Property getProperty() {
        return property;
    }

    public void addBid(Player player, int bidAmount) {
    	bids.putIfAbsent(player, 0);
        int currentBid = bids.get(player);
        if (bidAmount > currentBid) {
            bids.put(player, bidAmount);
        }
    }

    public Player getHighestBidder() {
        return bids.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
    }

    public int getHighestBid() {
        return bids.values().stream().max(Integer::compare).orElse(0);
    }
    
    
}

