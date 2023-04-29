package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {
    
    private PropertyRepository propertyRepository;
    private PlayerRepository playerRepository;
    
    @Autowired
    public PropertyService(PropertyRepository propertyRepository, PlayerRepository playerRepository) {
        this.propertyRepository = propertyRepository;
        this.playerRepository = playerRepository;
    }
    
    public boolean buyProperty(Long playerId, Long propertyId) {
        Player player = playerRepository.findById(playerId).orElse(null);
        Property property = propertyRepository.findById(propertyId).orElse(null);
        
        if (player == null || property == null) {
            return false;
        }
        
        if (player.getMoney() >= property.getCost() && !property.isOwned()) {
            player.setMoney(player.getMoney() - property.getCost());
            player.addProperty(property);
            property.setOwned(true);
            property.setOwnerId(playerId);
            propertyRepository.save(property);
            playerRepository.save(player);
            return true;
        }
        
        return false;
    }
    
    public boolean sellProperty(Long playerId, Long propertyId) {
        Player player = playerRepository.findById(playerId).orElse(null);
        Property property = propertyRepository.findById(propertyId).orElse(null);
        
        if (player == null || property == null || !property.isOwned() || property.getOwnerId() != playerId) {
            return false;
        }
        
        player.setMoney(player.getMoney() + property.getMortgageValue());
        player.removeProperty(property);
        property.setOwned(false);
        property.setOwnerId(null);
        propertyRepository.save(property);
        playerRepository.save(player);
        
        return true;
    }
}
