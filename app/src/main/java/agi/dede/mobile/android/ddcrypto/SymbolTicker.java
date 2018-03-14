package agi.dede.mobile.android.ddcrypto;

/**
 * Created by Eric on 13/01/2018.
 */

public class SymbolTicker {
    private final String id; // in request URL
    private final String name;
    private final String symbol;
    private final long rank;
    private final double priceBtc;
    private final double priceEth;
    private final double priceUsd;
    private final double priceLocal;

    private final double priceChange1h;
    private final double priceChange24h;
    private final double priceChange7d;
    private final double priceChangePercent1h;
    private final double priceChangePercent24h;
    private final double priceChangePercent7d;
    private final double volumeUsd24h;

    private final double marketCapUsd;
    private final double availableSupplyUsd;
    private final double totalSupplyUsd;
    private final double maxSupplyUsd;

    private final long lastUpdated;

    public SymbolTicker(SymbolTickerBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.symbol = builder.symbol;
        this.rank = builder.rank;
        this.priceBtc = builder.priceBtc;
        this.priceEth = builder.priceEth;
        this.priceUsd = builder.priceUsd;
        this.priceLocal = builder.priceLocal;

        this.priceChange1h = builder.priceChange1h;
        this.priceChange24h = builder.priceChange24h;
        this.priceChange7d = builder.priceChange7d;
        this.priceChangePercent1h = builder.priceChangePercent1h;
        this.priceChangePercent24h = builder.priceChangePercent24h;
        this.priceChangePercent7d = builder.priceChangePercent7d;
        this.volumeUsd24h = builder.volumeUsd24h;

        this.marketCapUsd = builder.marketCapUsd;
        this.availableSupplyUsd = builder.availableSupplyUsd;
        this.totalSupplyUsd = builder.totalSupplyUsd;
        this.maxSupplyUsd = builder.maxSupplyUsd;

        this.lastUpdated = builder.lastUpdated;
    }

    public static class SymbolTickerBuilder {
        private String id;
        private String name;
        private String symbol;
        private long rank;
        private double priceBtc;
        private double priceEth;
        private double priceUsd;
        private double priceLocal;

        private double priceChange1h;
        private double priceChange24h;
        private double priceChange7d;
        private double priceChangePercent1h;
        private double priceChangePercent24h;
        private double priceChangePercent7d;
        private double volumeUsd24h;

        private double marketCapUsd;
        private double availableSupplyUsd;
        private double totalSupplyUsd;
        private double maxSupplyUsd;

        private long lastUpdated;

        public SymbolTickerBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public SymbolTickerBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public SymbolTickerBuilder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public SymbolTickerBuilder setRank(long rank) {
            this.rank = rank;
            return this;
        }

        public SymbolTickerBuilder setPriceBtc(double priceBtc) {
            this.priceBtc = priceBtc;
            return this;
        }

        public SymbolTickerBuilder setPriceEth(double priceEth) {
            this.priceEth = priceEth;
            return this;
        }

        public SymbolTickerBuilder setPriceUsd(double priceUsd) {
            this.priceUsd = priceUsd;
            return this;
        }

        public SymbolTickerBuilder setPriceLocal(double priceLocal) {
            this.priceLocal = priceLocal;
            return this;
        }

        public SymbolTickerBuilder setPriceChange1h(double priceChange1h) {
            this.priceChange1h = priceChange1h;
            return this;
        }

        public SymbolTickerBuilder setPriceChange24h(double priceChange24h) {
            this.priceChange24h = priceChange24h;
            return this;
        }

        public SymbolTickerBuilder setPriceChange7d(double priceChange7d) {
            this.priceChange7d = priceChange7d;
            return this;
        }

        public SymbolTickerBuilder setPriceChangePercent1h(double priceChangePercent1h) {
            this.priceChangePercent1h = priceChangePercent1h;
            return this;
        }

        public SymbolTickerBuilder setPriceChangePercent24h(double priceChangePercent24h) {
            this.priceChangePercent24h = priceChangePercent24h;
            return this;
        }

        public SymbolTickerBuilder setPriceChangePercent7d(double priceChangePercent7d) {
            this.priceChangePercent7d = priceChangePercent7d;
            return this;
        }

        public SymbolTickerBuilder setVolumeUsd24h(double volumeUsd24h) {
            this.volumeUsd24h = volumeUsd24h;
            return this;
        }

        public SymbolTickerBuilder setMarketCapUsd(double marketCapUsd) {
            this.marketCapUsd = marketCapUsd;
            return this;
        }

        public SymbolTickerBuilder setAvailableSupplyUsd(double availableSupplyUsd) {
            this.availableSupplyUsd = availableSupplyUsd;
            return this;
        }

        public SymbolTickerBuilder setTotalSupplyUsd(double totalSupplyUsd) {
            this.totalSupplyUsd = totalSupplyUsd;
            return this;
        }

        public SymbolTickerBuilder setMaxSupplyUsd(double maxSupplyUsd) {
            this.maxSupplyUsd = maxSupplyUsd;
            return this;
        }

        public SymbolTickerBuilder setLastupdated(long lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public SymbolTicker build() {
            return new SymbolTicker(this);
        }
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public long getRank() {
        return this.rank;
    }

    public double getPriceBtc() {
        return this.priceBtc;
    }

    public double getPriceEth() {
        return this.priceEth;
    }

    public double getPriceUsd() {
        return this.priceUsd;
    }

    public double getPriceLocal() {
        return this.priceLocal;
    }

    public double getPriceChange1h() {
        return this.priceChange1h;
    }

    public double getPriceChange24h() {
        return this.priceChange24h;
    }

    public double getPriceChange7d() {
        return this.priceChange7d;
    }

    public double getPriceChangePercent1h() {
        return this.priceChangePercent1h;
    }

    public double getPriceChangePercent24h() {
        return this.priceChangePercent24h;
    }

    public double getPriceChangePercent7d() {
        return this.priceChangePercent7d;
    }

    public double getVolumeUsd24h() {
        return this.volumeUsd24h;
    }

    public double getMarketCapUsd() {
        return this.marketCapUsd;
    }

    public double getAvailableSupplyUsd() {
        return this.availableSupplyUsd;
    }

    public double getTotalSupplyUsd() {
        return this.totalSupplyUsd;
    }

    public double getMaxSupplyUsd() {
        return this.maxSupplyUsd;
    }

    public long getLastUpdated() {
        return this.lastUpdated;
    }
}