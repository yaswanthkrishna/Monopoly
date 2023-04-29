package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.Column;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/game")
public class GameController {

    private PlayerRepository playerRepository;
    private PropertyRepository propertyRepository;
    private BoardRepository boardRepository;

    @Autowired
    public GameController(PlayerRepository playerRepository, PropertyRepository propertyRepository, BoardRepository boardRepository) {
        this.playerRepository = playerRepository;
        this.propertyRepository = propertyRepository;
        this.boardRepository = boardRepository;
    }

    private int currentPlayerIndex = 0;
    private List<Player> players = new ArrayList<>();
    @Column(name = "playerposition_row")
    int newRow = 0;
    @Column(name = "playerposition_column")
    int newCol = 0;

    public void initGame() {
        players = playerRepository.findAll();
        if (players.isEmpty()) {
            // Create players and save them to the repository
            for (int i = 1; i <= 4; i++) {
                Player player = new Player("Player " + i, 1500, 0, 0);
                playerRepository.save(player);
                players.add(player);
            }
        }
    }

    @GetMapping("/play")
    public ResponseEntity<Object> playGame() {
        if (players.isEmpty()) {
            initGame();
        }

        // Game logic here for the current player
        Player currentPlayer = players.get(currentPlayerIndex);
        String playerName = currentPlayer.getName(); // Get the player name from currentPlayer object
        int currentRow = currentPlayer.getRow();
        int currentCol = currentPlayer.getCol();

        // Call methods on playerRepository to interact with the game state
        Dice dice = new Dice();
        int steps = dice.getResult();

        Movement_monopoly movementMonopoly = new Movement_monopoly(playerName, playerRepository);
        movementMonopoly.move(steps);
        newRow = movementMonopoly.getRow();
        newCol = movementMonopoly.getCol();
        
        // Update the current player's position in the database
        
        currentPlayer.setRow(newRow);
        currentPlayer.setCol(newCol);
        playerRepository.save(currentPlayer);

        if (boardRepository.getOne(1L).isIncomeTaxPosition(newRow, newCol)) {
            currentPlayer.setMoney(currentPlayer.getMoney() - 200);
            playerRepository.save(currentPlayer);
        }
        
     // Check if the player lands on a property
        Property property = propertyRepository.findByRowAndCol(newRow, newCol);
        if (property != null) {
            // Check if the property is already owned
            if (property.isOwned()) {
                // Pay rent to the owner of the property
                Player owner = playerRepository.getOne(property.getOwnerId());
                int rent = property.getRent();
                currentPlayer.setMoney(currentPlayer.getMoney() - rent);
                owner.setMoney(owner.getMoney() + rent);
                playerRepository.save(currentPlayer);
                playerRepository.save(owner);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("playerName", playerName);
        result.put("steps", steps);
        result.put("currentRow", currentRow);
        result.put("currentCol", currentCol);
        result.put("newRow", newRow);
        result.put("newCol", newCol);
        result.put("newMoney", currentPlayer.getMoney());
        
     // Update the current player index to advance to the next player's turn
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        return ResponseEntity.ok(result);
    }

    // Other endpoints like /buy, /buildHouse, and /buildHotel remain unchanged


    @GetMapping("/buy")
    public ResponseEntity<Object> buyingProperty(@RequestParam Long playerId, @RequestParam Long propertyId) {
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            return ResponseEntity.badRequest().body("Invalid player ID");
        }

        Property property = propertyRepository.findById(propertyId).orElse(null);
        if (property == null) {
            return ResponseEntity.badRequest().body("Invalid property ID");
        }

        if (property.isOwned()) {
            return ResponseEntity.badRequest().body("Property is already owned");
        }

        int playerMoney = player.getMoney();
        int propertyCost = property.getCost();
        if (playerMoney < propertyCost) {
            return ResponseEntity.badRequest().body("Not enough money to buy property");
        }

        // Update player and property objects to reflect the transaction
        player.setMoney(playerMoney - propertyCost);
        player.addProperty(property);
        property.setOwned(true);
        property.setOwnerId(player.getId());

        // Save updated player and property objects to the database
        playerRepository.save(player);
        propertyRepository.save(property);

        return ResponseEntity.ok("Property bought successfully");
    }

    @GetMapping("/buildHouse")
    public ResponseEntity<Object> buildHouse(@RequestParam Long playerId, @RequestParam Long propertyId) {
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            return ResponseEntity.badRequest().body("Invalid player ID");
        }

        Property property = propertyRepository.findById(propertyId).orElse(null);
        if (property == null) {
            return ResponseEntity.badRequest().body("Invalid property ID");
        }

        // Check if the player owns the property
        if (!property.getOwnerId().equals(player.getId())) {
            return ResponseEntity.badRequest().body("You do not own this property");
        }

        // Check if the player can build a house on this property
        List<Property> allProperties = propertyRepository.findAll();
        if (!player.ownsAllPropertiesOfSameColor(property, allProperties)) {
            return ResponseEntity.badRequest().body("You do not own all properties of the same color");
        }

        // Check if the player has enough money to build a house
        int houseCost = property.getHouseCost();
        if (player.getMoney() < houseCost) {
            return ResponseEntity.badRequest().body("Not enough money to build a house");
        }

        // Update player's money and property's houses
        player.setMoney(player.getMoney() - houseCost);
        property.setHouses(property.getHouses() + 1);

        // Save updated player and property objects to the database
        playerRepository.save(player);
        propertyRepository.save(property);

        return ResponseEntity.ok("House built successfully");
    }

    @GetMapping("/buildHotel")
    public ResponseEntity<Object> buildHotel(@RequestParam Long playerId, @RequestParam Long propertyId) {
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            return ResponseEntity.badRequest().body("Invalid player ID");
        }

        Property property = propertyRepository.findById(propertyId).orElse(null);
        if (property == null) {
            return ResponseEntity.badRequest().body("Invalid property ID");
        }

        // Check if the player owns the property
        if (!property.getOwnerId().equals(player.getId())) {
            return ResponseEntity.badRequest().body("You do not own this property");
        }

        // Check if the player can build a hotel on this property
        List<Property> allProperties = propertyRepository.findAll();
        if (!player.ownsAllPropertiesOfSameColor(property, allProperties)) {
            return ResponseEntity.badRequest().body("You do not own all properties of the same color");
        }
        // Check if there are 4 houses on the property
        if (property.getHouses() != 4) {
            return ResponseEntity.badRequest().body("There must be 4 houses on the property to build a hotel");
        }

        // Check if the player has enough money to build a hotel
        int hotelCost = property.getHotelCost();
        if (player.getMoney() < hotelCost) {
            return ResponseEntity.badRequest().body("Not enough money to build a hotel");
        }

        // Update player's money and property's hotels and houses
        player.setMoney(player.getMoney() - hotelCost);
        property.setHouses(0); // Reset the number of houses on the property
        property.setHotels(property.getHotels() + 1);

        // Save updated player and property objects to the database
        playerRepository.save(player);
        propertyRepository.save(property);

        return ResponseEntity.ok("Hotel built successfully");
    }
    
    @GetMapping("/winner")
    public ResponseEntity<Object> declareWinner() {
        List<Player> players = playerRepository.findAll();
        Player winner = null;
        int maxMoney = 0;

        for (Player player : players) {
            int playerMoney = player.getMoney();
            if (playerMoney > maxMoney) {
                maxMoney = playerMoney;
                winner = player;
            }
        }

        if (winner == null) {
            return ResponseEntity.ok("No winner yet");
        } else {
            String message = "The winner is " + winner.getName() + " with a total of " + winner.getMoney() + " money!";
            return ResponseEntity.ok(message);
        }
    }
    
    @GetMapping("/reset")
    public ResponseEntity<Object> resetGame1() {
        playerRepository.deleteAll();
        propertyRepository.deleteAll();
        boardRepository.deleteAll();
        initGame();
        return ResponseEntity.ok("Game reset successfully");
    }


}
