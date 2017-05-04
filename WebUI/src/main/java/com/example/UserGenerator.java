package com.example;

import java.util.Random;

public final class UserGenerator {

    private int min = 1;
    private int max = 10;

    public String generateUser() {
        Random rn = new Random();
        return "User " + (rn.nextInt(max - min + 1) + min);
    }
}
