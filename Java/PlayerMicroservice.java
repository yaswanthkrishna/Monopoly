package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/player")
public class PlayerMicroservice {

    @Autowired
    private PlayerRepository playerRepository;
    
    @Autowired
    private PropertyRepository propertyRepository;

    private Map<Long, Auction> auctionMap = new HashMap<>();

    public Map<Long, Auction> getAuctionMap() {
        return auctionMap;
    }

    public void setAuctionMap(Map<Long, Auction> auctionMap) {
        this.auctionMap = auctionMap;
    }

    public void addToAuctionMap(Long propertyId, Auction auction) {
        auctionMap.put(propertyId, auction);
    }

    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable("id") Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Player createPlayer(@RequestBody Player player) {
        return playerRepository.save(player);
    }

    @PostMapping("/createWithName")
    public Player createPlayerWithName(@RequestParam("name") String name) {
        Player player = new Player(name);
        return playerRepository.save(player);
    }
    
    @PostMapping("/{playerId}/buy/{propertyId}")
    public ResponseEntity<String> buyProperty(@PathVariable("playerId") Long playerId, @PathVariable("propertyId") Long propertyId) {
        Player player = playerRepository.findById(playerId).orElse(null);
        Property property = propertyRepository.findById(propertyId).orElse(null);
        
        if (player == null || property == null) {
            return ResponseEntity.badRequest().body("Player or property not found");
        }
        
        if (property.isOwned()) {
            return ResponseEntity.badRequest().body("Property is already owned by someone else");
        }
        
        if (player.getMoney() < property.getCost()) {
            return ResponseEntity.badRequest().body("Player does not have enough money to buy this property");
        }

        // Deduct property cost from player's money
        player.setMoney(player.getMoney() - property.getCost());
        playerRepository.save(player);

        // Set property as owned and update the owner ID
        property.setOwned(true);
        property.setOwnerId(player.getId());
        propertyRepository.save(property);

        return ResponseEntity.ok("Property bought successfully");
    }

    @PostMapping("/{playerId}/sell/{propertyId}")
    public ResponseEntity<String> sellProperty(@PathVariable("playerId") Long playerId, @PathVariable("propertyId") Long propertyId) {
        Player player = playerRepository.findById(playerId).orElse(null);
        Property property = propertyRepository.findById(propertyId).orElse(null);
        
        if (player == null || property == null) {
            return ResponseEntity.badRequest().body("Player or property not found");
        }
        
        if (!property.isOwned() || !property.getOwnerId().equals(player.getId())) {
            return ResponseEntity.badRequest().body("Player does not own this property");
        }

        // Add property's mortgage value to player's money
        player.setMoney(player.getMoney() + property.getMortgageValue());
        playerRepository.save(player);

        // Set property as not owned and update the owner ID
        property.setOwned(false);
        property.setOwnerId(null);
        propertyRepository.save(property);

        return ResponseEntity.ok("Property sold successfully");
    }

    @PostMapping("/{playerId}/giveJailCard")
    public ResponseEntity<String> giveJailCard(@PathVariable("playerId") Long playerId) {
        Player player = playerRepository.findById(playerId).orElse(null);

        if (player == null) {
            return ResponseEntity.badRequest().body("Player not found");
        }

        player.setJailCard(true);
        playerRepository.save(player);

        return ResponseEntity.ok("Jail card given to the player");
    }

    @GetMapping("/{playerId}/hasJailCard")
    public ResponseEntity<Boolean> hasJailCard(@PathVariable("playerId") Long playerId) {
        Player player = playerRepository.findById(playerId).orElse(null);

        if (player == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(player.hasJailCard());
    }
    
    @PostMapping("/{playerId}/mortgage/{propertyId}")
    public ResponseEntity<String> mortgageProperty(@PathVariable("playerId") Long playerId, @PathVariable("propertyId") Long propertyId) {
        Player player = playerRepository.findById(playerId).orElse(null);
        Property property = propertyRepository.findById(propertyId).orElse(null);

        if (player == null || property == null) {
            return ResponseEntity.badRequest().body("Player or property not found");
        }

        if (!property.isOwned() || !property.getOwnerId().equals(player.getId())) {
            return ResponseEntity.badRequest().body("Player does not own this property");
        }

        if (property.isMortgaged()) {
            return ResponseEntity.badRequest().body("Property is already mortgaged");
        }

        // Add mortgage value to player's money
        player.setMoney(player.getMoney() + property.getMortgageValue());
        playerRepository.save(player);

        // Set property as mortgaged
        property.setMortgaged(true);
        propertyRepository.save(property);

        return ResponseEntity.ok("Property mortgaged successfully");
    }
    
    @PostMapping("/auction/{propertyId}")
    public ResponseEntity<String> startAuction(@PathVariable("propertyId") Long propertyId) {
        Property property = propertyRepository.findById(propertyId).orElse(null);

        if (property == null) {
            return ResponseEntity.badRequest().body("Property not found");
        }

        if (property.isOwned()) {
            return ResponseEntity.badRequest().body("Property is already owned");
        }

        Auction auction = new Auction(property);
        addToAuctionMap(propertyId, auction);

        return ResponseEntity.ok("Auction started for property " + property.getName());
    }

    
    @PostMapping("/{playerId}/auction/{propertyId}/bid")
    public ResponseEntity<String> placeBid(@PathVariable("playerId") Long playerId, @PathVariable("propertyId") Long propertyId, @RequestParam("bidAmount") int bidAmount) {
        Player player = playerRepository.findById(playerId).orElse(null);
        Property property = propertyRepository.findById(propertyId).orElse(null);

        if (player == null || property == null) {
            return ResponseEntity.badRequest().body("Player or property not found");
        }

        // Assuming you have stored the auctions in a map in the class, you can retrieve it like this:
        Auction auction = auctionMap.get(propertyId);

        if (auction == null) {
            return ResponseEntity.badRequest().body("No auction found for the given property");
        }

        if (player.getMoney() < bidAmount) {
            return ResponseEntity.badRequest().body("Player does not have enough money to place this bid");
        }

        auction.addBid(player, bidAmount);

        return ResponseEntity.ok("Bid placed successfully");
    }
    
    @PostMapping("/auction/{propertyId}/end")
    public ResponseEntity<String> endAuction(@PathVariable("propertyId") Long propertyId) {
        Property property = propertyRepository.findById(propertyId).orElse(null);

        if (property == null) {
            return ResponseEntity.badRequest().body("Property not found");
        }

        Auction auction = auctionMap.get(propertyId);

        if (auction == null) {
            return ResponseEntity.badRequest().body("No auction found for the given property");
        }

        Player highestBidder = auction.getHighestBidder();
        int highestBid = auction.getHighestBid();

        if (highestBidder == null) {
            return ResponseEntity.badRequest().body("No bids placed for the property");
        }

        // Deduct bid amount from highest bidder's money
        highestBidder.setMoney(highestBidder.getMoney() - highestBid);
        playerRepository.save(highestBidder);

        // Set property as owned and update the owner ID
        property.setOwned(true);
        property.setOwnerId(highestBidder.getId());
        propertyRepository.save(property);

        // Remove auction from the map
        auctionMap.remove(propertyId);

        return ResponseEntity.ok("Auction ended. Property awarded to " + highestBidder.getName() + " for " + highestBid);
    }

    @PostMapping("/{playerId}/passGo")
    public ResponseEntity<String> passGo(@PathVariable("playerId") Long playerId) {
        Player player = playerRepository.findById(playerId).orElse(null);

        if (player == null) {
            return ResponseEntity.badRequest().body("Player not found");
        }

        int moneyAfterPassingGo = player.getMoney() + 200;
        player.setMoney(moneyAfterPassingGo);
        playerRepository.save(player);

        return ResponseEntity.ok("Player " + player.getName() + " passed GO and received 200. New balance: " + moneyAfterPassingGo);
    }
    
    @PostMapping("/{playerId}/landOnTaxPosition")
    public ResponseEntity<String> landOnTaxPosition(@PathVariable("playerId") Long playerId, @RequestBody Position position) {
        Player player = playerRepository.findById(playerId).orElse(null);

        if (player == null) {
            return ResponseEntity.badRequest().body("Player not found");
        }

        int taxAmount = position.getTaxAmount();

        if (player.getMoney() < taxAmount) {
            return ResponseEntity.badRequest().body("Player does not have enough money to pay the tax");
        }

        player.setMoney(player.getMoney() - taxAmount);
        playerRepository.save(player);

        return ResponseEntity.ok("Player " + player.getName() + " paid a tax of " + taxAmount + ". New balance: " + player.getMoney());
    }
    
    @PostMapping("/{playerId}/drawTaxCard")
    public ResponseEntity<String> drawTaxCard(@PathVariable("playerId") Long playerId, @RequestBody Card card) {
        Player player = playerRepository.findById(playerId).orElse(null);

        if (player == null) {
            return ResponseEntity.badRequest().body("Player not found");
        }

        int taxAmount = card.getTaxAmount();

        if (player.getMoney() < taxAmount) {
            return ResponseEntity.badRequest().body("Player does not have enough money to pay the tax");
        }

        player.setMoney(player.getMoney() - taxAmount);
        playerRepository.save(player);

        return ResponseEntity.ok("Player " + player.getName() + " drew a card: " + card.getDescription() + ". Paid a tax of " + taxAmount + ". New balance: " + player.getMoney());
    }

    
    // Add this method to expose the player repository to other classes
    public PlayerRepository getPlayerRepository() {
        return playerRepository;
    }
}
