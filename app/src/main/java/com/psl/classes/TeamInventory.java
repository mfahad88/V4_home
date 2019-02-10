package com.psl.classes;

import java.util.HashMap;

/**
 * Created by aamir.shehzad on 1/18/2018.
 */

public class TeamInventory {




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    String id="";
    public  String PurpleCap ="0";
    public  String OrangeCap ="0";
    public  String GoldenGloves ="0";
    public String SafetyCap ="0";
    public String Iconic ="0";
    public String MOM ="0";
    public String teamSafety ="0";
    public String playerSafety ="0";
    public String amount ="0";
    public String discount ="0";
    public HashMap<String,String> allItems=new HashMap<>();
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPlayerSafety() {
        return playerSafety;
    }

    public void setPlayerSafety(String playerSafety) {
        this.playerSafety = playerSafety;
    }


    public HashMap<String,String> getAllItems() {
        return allItems;
    }

    public void addItem(String item,String count) {
        this.allItems.put(item,count);
    }

    public String getTeamSafety() {
        return teamSafety;
    }

    public void setTeamSafety(String teamSafety) {
        this.teamSafety = teamSafety;
    }

    public String getPurpleCap() {
        return PurpleCap;
    }

    public void setPurpleCap(String purpleCap) {
        PurpleCap = purpleCap;
    }

    public String getOrangeCap() {
        return OrangeCap;
    }

    public void setOrangeCap(String orangeCap) {
        OrangeCap = orangeCap;
    }

    public String getGoldenGloves() {
        return GoldenGloves;
    }

    public void setGoldenGloves(String goldenGloves) {
        GoldenGloves = goldenGloves;
    }

    public String getSafetyCap() {
        return SafetyCap;
    }

    public void setSafetyCap(String safetyCap) {
        SafetyCap = safetyCap;
    }

    public String getIconic() {
        return Iconic;
    }

    public void setIconic(String iconic) {
        Iconic = iconic;
    }

    public String getMOM() {
        return MOM;
    }

    public void setMOM(String MOM) {
        this.MOM = MOM;
    }
}
