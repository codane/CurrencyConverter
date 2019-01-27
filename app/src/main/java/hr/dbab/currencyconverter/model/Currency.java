package hr.dbab.currencyconverter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Currency {

    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("unit_value")
    @Expose
    private Integer unitValue;
    @SerializedName("selling_rate")
    @Expose
    private String sellingRate;
    @SerializedName("buying_rate")
    @Expose
    private String buyingRate;
    @SerializedName("median_rate")
    @Expose
    private String medianRate;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Integer getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(Integer unitValue) {
        this.unitValue = unitValue;
    }

    public String getSellingRate() {
        return sellingRate;
    }

    public void setSellingRate(String sellingRate) {
        this.sellingRate = sellingRate;
    }

    public String getBuyingRate() {
        return buyingRate;
    }

    public void setBuyingRate(String buyingRate) {
        this.buyingRate = buyingRate;
    }

    public String getMedianRate() {
        return medianRate;
    }

    public void setMedianRate(String medianRate) {
        this.medianRate = medianRate;
    }

}