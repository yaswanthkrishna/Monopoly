package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "property")
public class Property {

    @Id
	protected static Long id;
    
    @Column(name = "p_name")
    private Long name;   
    @Column(name = "p_color")
    private String color;  
    @Column(name = "cost")
    private int cost;   
    @Column(name = "house_cost")
    private int houseCost;  
    @Column(name = "hotel_cost")
    private int hotelCost;
    @Column(name = "hotel_mortgage")
    private int mortgageValue;
    @Column(name = "houses")
    private int houses;
    @Column(name = "hotels")
    private int hotels;
    private boolean owned;
    @Column(name = "Enabled")
    private boolean isMortgaged;
    @Column(name = "house_rent")
    private int rent;
    @Column(name = "player_id")
    private Long ownerId; // Use ownerId instead of owner to avoid circular dependencies
    
    @ManyToOne
    private Player owner;

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
    
    @OneToOne
    private Position position;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    
    public Property() {
    }

    public Property(Long id2, String color, int cost, int rent, int houseCost, int hotelCost, int mortgageValue) {
    	this.name = id2;
        this.color = color;
        this.cost = cost;
        this.rent = rent;
        this.houseCost = houseCost;
        this.hotelCost = hotelCost;
        this.mortgageValue = mortgageValue;
        this.houses = 0;
        this.hotels = 0;
        this.owned = false;
        this.isMortgaged = false;
        this.ownerId = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getName() {
        return name;
    }

    public void setName(Long name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public int getMortgageValue() {
        return mortgageValue;
    }

    public void setMortgageValue(int mortgageValue) {
        this.mortgageValue = mortgageValue;
    }

    public boolean isOwned() {
        if(ownerId==0) {
        	return false;
        }
        else {
        	return true;
        }
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public Long getOwnerId() {
    	return owner != null ? owner.getId() : null;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    
    public boolean isMortgaged() {
        return isMortgaged;
    }

    public void setMortgaged(boolean isMortgaged) {
        this.isMortgaged = isMortgaged;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getHouses() {
        return houses;
    }

    public void setHouses(int houses) {
        this.houses = houses;
    }

    public int getHotels() {
        return hotels;
    }

    public void setHotels(int hotels) {
        this.hotels = hotels;
    }

    public int getHouseCost() {
        return houseCost;
    }

    public void setHouseCost(int houseCost) {
        this.houseCost = houseCost;
    }

    public int getHotelCost() {
        return hotelCost;
    }

    public void setHotelCost(int hotelCost) {
        this.hotelCost = hotelCost;
    }
	
}
