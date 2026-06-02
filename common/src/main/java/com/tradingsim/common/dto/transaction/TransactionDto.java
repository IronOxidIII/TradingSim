package com.tradingsim.common.dto.transaction;

public class TransactionDto {

    private int user_id;
    private int asset_id;
    private String sum;
    private String asset_price;
    private String datetime;
    private String amount;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(int asset_id) {
        this.asset_id = asset_id;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getAsset_price() {
        return asset_price;
    }

    public void setAsset_price(String asset_price) {
        this.asset_price = asset_price;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}