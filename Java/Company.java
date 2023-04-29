package com.example.demo;

public class Company extends Property {
    private static final int RENT_MULTIPLIER = 4;
    private static final int MORTGAGE_VALUE = 75;

    public Company(String name, int cost) {
        super(id, name, cost, MORTGAGE_VALUE, cost, cost, cost);
    }

    public int calculateRent(int rollTotal) {
        int ownedCompanies = getOwner().getOwnedCompanies();
        if (ownedCompanies == 1) {
            return RENT_MULTIPLIER * rollTotal;
        } else if (ownedCompanies == 2) {
            return 10 * rollTotal;
        } else {
            return 0;
        }
    }
}
