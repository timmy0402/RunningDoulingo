package com.example.stepuptest;

public class RankItem {
    private String name;
    private int steps;

    public RankItem(String name, int steps) {
        this.name = name;
        this.steps = steps;
    }

    public int compareTo(RankItem other) {
        return Integer.compare(other.steps, this.steps);
    }

    public String getDate() {
        return name;
    }

    public int getSteps() {
        return steps;
    }
}
