package com.example.demo;

import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Player {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int row;
    private int col;
    private int money;
    private boolean hasJailCard;

    @OneToMany(mappedBy = "owner")
    private List<Property> ownedProperties;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
        this.row = 0;
        this.col = 10;
        this.money = 1500;
        this.hasJailCard = false;
        this.ownedProperties = new ArrayList<>();
    }
    
    public Player(String name, PlayerRepository playerRepository) {
        this.name = name;
        this.row = 0;
        this.col = 10;
        this.hasJailCard = false;
        Player player = playerRepository.findByName(name);
        if (player != null) {
            this.money = player.getMoney();
        } else {
            this.money = 1500;
        }
    }
    
    public Player(String name, int money, int row, int col) {
        this.name = name;
        this.money = money;
        this.row = row;
        this.col = col;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        col = col;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean hasJailCard() {
        return hasJailCard;
    }

    public void setJailCard(boolean hasJailCard) {
        this.hasJailCard = hasJailCard;
    }

    public List<Property> getOwnedProperties() {
        return ownedProperties;
    }

    public void setOwnedProperties(List<Property> ownedProperties) {
        this.ownedProperties = ownedProperties;
    }

    public void addProperty(Property property) {
        ownedProperties.add(property);
    }

    public boolean ownsAllPropertiesOfSameColor(Property property, List<Property> allProperties) {
        String targetColor = property.getColor();
        List<Property> propertiesOfSameColor = new ArrayList<>();

        for (Property p : allProperties) {
            if (p.getColor().equalsIgnoreCase(targetColor)) {
                propertiesOfSameColor.add(p);
            }
        }

        for (Property p : propertiesOfSameColor) {
            if (!p.getOwnerId().equals(this.id)) {
                return false;
            }
        }

        return true;
    }

	public void removeProperty(Property property) {
		if (ownedProperties != null) {
	        ownedProperties.remove(property);
	    }
		
	}

	public int getOwnedCompanies() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getOwnedRailroads() {
		// TODO Auto-generated method stub
		return 0;
	}
}
