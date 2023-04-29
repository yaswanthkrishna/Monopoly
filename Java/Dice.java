package com.example.demo;

import java.util.Random;

public class Dice {
    private int result;

    public Dice() {
        Random random = new Random();
        result = random.nextInt(12) + 1;
    }
    
    public int getResult() {
        Random random = new Random();
        return random.nextInt(12) + 1;
    }
}
