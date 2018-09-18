package com.alexandr4.loftmoney;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

    public static final String TYPE_EXPENSE = "expense";
    public static final String TYPE_INCOME = "income";

    public int id;
    public String name;
    public int price;
    public String type;

    public Item(int id, String name, int price, String type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeString(type);
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    protected Item(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readInt();
        type = in.readString();
    }

}
