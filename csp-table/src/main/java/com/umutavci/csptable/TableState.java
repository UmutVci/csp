package com.umutavci.csptable;

import java.util.ArrayList;
import java.util.List;

public class TableState {

    private final List<Integer> ingredients = new ArrayList<>();

    public synchronized void putIngredient(int ingredient) {
        if (ingredients.size() >= 2) {
            throw new IllegalStateException("Table already has two ingredients");
        }
        ingredients.add(ingredient);
    }

    public synchronized List<Integer> getCurrentIngredients() {
        return new ArrayList<>(ingredients); // defensive copy
    }

    public synchronized boolean takeIngredient(int ingredient) {
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i) == ingredient) {
                ingredients.remove(i);
                return true;
            }
        }
        return false;
    }

    public synchronized void clear() {
        ingredients.clear();
    }
}
