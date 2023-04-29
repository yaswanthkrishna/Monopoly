package com.example.demo;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Board {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Board properties and relationships here
    
    // Getters and setters
    
    public boolean isIncomeTaxPosition(int row, int col) {
        Position position = positions.stream()
                .filter(p -> p.getRow() == row && p.getCol() == col)
                .findFirst()
                .orElse(null);
        return position != null && position.getName().equals("Income Tax");
    }
    
    // Define the Position class and the positions array
    @OneToMany(mappedBy = "board")
    private List<Position> positions;

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }
    
    @Autowired
    private CardRepository cardRepository;

    public Card drawCard(String type) {
        List<Card> cards = cardRepository.findByType(type);
        Random rand = new Random();
        int index = rand.nextInt(cards.size());
        return cards.get(index);
    }

    public boolean isCommunityChestPosition(int row, int col) {
        Position position = positions.stream()
                .filter(p -> p.getRow() == row && p.getCol() == col)
                .findFirst()
                .orElse(null);
        return position != null && position.getName().equals("Community Chest");
    }

    public boolean isChancePosition(int row, int col) {
        Position position = positions.stream()
                .filter(p -> p.getRow() == row && p.getCol() == col)
                .findFirst()
                .orElse(null);
        return position != null && position.getName().equals("Chance");
    }
    
    public Property getPropertyAtPosition(int row, int col) {
        return positions.stream()
                .filter(p -> p.getRow() == row && p.getCol() == col)
                .map(Position::getProperty)
                .findFirst()
                .orElse(null);
    }

}
