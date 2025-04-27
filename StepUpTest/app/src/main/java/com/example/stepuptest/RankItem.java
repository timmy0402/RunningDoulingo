package com.example.stepuptest;

public class RankItem {
    private String name;
    private int steps;

    public RankItem(String name, int steps) {
        this.name = name;
        this.steps = steps;
    }

    public String getDate() {
        return name;
    }

    public int getSteps() {
        return steps;
    }
}
