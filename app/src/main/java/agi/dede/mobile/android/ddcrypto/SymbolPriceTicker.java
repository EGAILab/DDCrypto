package agi.dede.mobile.android.ddcrypto;

/**
 * Created by Eric on 9/01/2018.
 */

public class SymbolPriceTicker {

    // Coin pair symbol
    private String symbol;

    // Price
    private double price;

    public SymbolPriceTicker (String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public double getPrice() {
        return this.price;
    }
}
