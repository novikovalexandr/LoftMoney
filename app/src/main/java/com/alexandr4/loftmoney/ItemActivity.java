package com.alexandr4.loftmoney;

public class ItemActivity {

    public static final String TYPE_EXPENSE = "expense";
    public static final String TYPE_INCOME = "income";
    public static final String TYPE_BALANCE = "balance";

    private int id;
    private String name;
    private int price;

    public ItemActivity(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
