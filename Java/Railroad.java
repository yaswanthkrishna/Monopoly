package com.example.demo;

public class Railroad extends Property {
    private static final int RENT_BASE = 25;
    private static final int RENT_TWO = 50;
    private static final int RENT_THREE = 100;
    private static final int RENT_FOUR = 200;
    private static final int MORTGAGE_VALUE = 100;

    public Railroad(String name, int cost) {
        super(id, name, cost, MORTGAGE_VALUE, cost, cost, cost);
    }

    public int calculateRent() {
        int ownedRailroads = getOwner().getOwnedRailroads();
        switch (ownedRailroads) {
            case 1:
                return RENT_BASE;
            case 2:
                return RENT_TWO;
            case 3:
                return RENT_THREE;
            case 4:
                return RENT_FOUR;
            default:
                return 0;
        }
    }
}
