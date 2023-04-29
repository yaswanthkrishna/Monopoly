 package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "card")
public class Card {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
	private Long id;
    @Column(name = "card_Name")
    private String name;
    @Column(name = "card_type")
    private String type;
    @Column(name = "card_Description")
    private String description;
    @Column(name = "Money")
    private int taxAmount;

    public Card(String type, String description, int taxAmount) {
    	this.type = type;
        this.description = description;
        this.taxAmount = taxAmount;
    }
    
    public Card() {
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // getters and setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(int taxAmount) {
        this.taxAmount = taxAmount;
    }

}