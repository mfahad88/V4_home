package com.psl.classes;

import java.io.Serializable;

/**
 * Created by aamir.shehzad on 1/18/2018.
 */

public class InventoryClass implements Serializable{


    String id,name,price,power,dealOfDay="0",description="";
    int image,itemCount=0;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDealOfDay() {
        return dealOfDay;
    }

    public void setDealOfDay(String dealOfDay) {
        this.dealOfDay = dealOfDay;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount += itemCount;
    }
    public void clearItemCount() {
        this.itemCount = 0;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }





    public static final String PurpleCap =      "purple cap";
    public static final String OrangeCap =      "orange cap";
    public static final String GoldenGloves =   "golden gloves";
    public static final String GoldenGolves =   "golden golves";
    public static final String SafetyCap =      "safety cap";
    public static final String Iconic =         "iconic player";
    public static final String IconPlayer =         "icon player";
    public static final String Swaps =          "transfers";
    public static final String Swaps10 =          "10 transfers";
    public static final String TeamSafety =      "team safety";
    public static final String PlayerSafety =      "player safety";


}
