package agi.dede.mobile.android.ddcrypto;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 10/01/2018.
 *
 * Helper methods to request ticker data from CoinMarketCap
 */
public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query CoinMarketCap and return a list of {@link SymbolTicker} objects.
     */
    public static List<SymbolTicker> fetchSymbolTickers (String requestUrl) {

        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields (site specific) from the JSON response
        List<SymbolTicker> symbolTickers = extractFeatureFromJson(jsonResponse);

        return symbolTickers;
    }

    /**
     * Returns URL object from url string
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Invalid URL form ", e);
        }
        return url;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Return a list of {@link SymbolTicker} objects that has been built up from
     * parsing the given JSON response (site specific).
     */
    private static List<SymbolTicker> extractFeatureFromJson(String symbolTickerJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(symbolTickerJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding tickers to
        List<SymbolTicker> symbolTickers = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONArray symbolTickerJSONArray = new JSONArray(symbolTickerJSON);

            for (int i = 0; i < symbolTickerJSONArray.length(); i++) {

                // Get a single ticker at position i within the list of tickers
                JSONObject symbolTickerJSONObject = symbolTickerJSONArray.getJSONObject(i);

                String strMaxSuppyUsd = symbolTickerJSONObject.getString("max_supply");
                String strPriceChangePercent1h = symbolTickerJSONObject.getString("percent_change_1h");
                String strPriceChangePercent24h = symbolTickerJSONObject.getString("percent_change_24h");
                String strPriceChangePercent7d = symbolTickerJSONObject.getString("percent_change_7d");
                String strVolumeUsd24h = symbolTickerJSONObject.getString("24h_volume_usd");

                double maxSuppyUsd = 0;
                double priceChange1h = 0, priceChange24h = 0, priceChange7d = 0;
                double priceChangePercent1h = 0, priceChangePercent24h = 0, priceChangePercent7d = 0;
                double volumeUsd24h = 0;

                if (strMaxSuppyUsd != "null") {
                    maxSuppyUsd = Double.parseDouble(strMaxSuppyUsd);
                }

                if (strPriceChangePercent1h != "null") {
                    priceChangePercent1h = Double.parseDouble(strPriceChangePercent1h);
                }

                if (strPriceChangePercent24h != "null") {
                    priceChangePercent24h = Double.parseDouble(strPriceChangePercent1h);
                }

                if (strPriceChangePercent7d != "null") {
                    priceChangePercent7d = Double.parseDouble(strPriceChangePercent1h);
                }

                if (strVolumeUsd24h != "null") {
                    volumeUsd24h = Double.parseDouble(strVolumeUsd24h);
                }

                SymbolTicker symbolTicker
                        = new SymbolTicker.SymbolTickerBuilder()
                        .setId(symbolTickerJSONObject.getString("id"))
                        .setName(symbolTickerJSONObject.getString("name"))
                        .setSymbol(symbolTickerJSONObject.getString("symbol"))
                        .setRank(symbolTickerJSONObject.getLong("rank"))
                        .setPriceBtc(symbolTickerJSONObject.getDouble("price_btc"))
                        .setPriceEth(0)
                        .setPriceUsd(symbolTickerJSONObject.getDouble("price_usd"))
                        .setPriceLocal(symbolTickerJSONObject.getDouble("price_aud"))
                        .setPriceChange1h(priceChange1h)
                        .setPriceChange24h(priceChange24h)
                        .setPriceChange7d(priceChange7d)
                        .setPriceChangePercent1h(priceChangePercent1h)
                        .setPriceChangePercent24h(priceChangePercent24h)
                        .setPriceChangePercent7d(priceChangePercent7d)
                        .setVolumeUsd24h(volumeUsd24h)
                        .setMarketCapUsd(symbolTickerJSONObject.getDouble("market_cap_usd"))
                        .setAvailableSupplyUsd(symbolTickerJSONObject.getDouble("available_supply"))
                        .setTotalSupplyUsd(symbolTickerJSONObject.getDouble("total_supply"))
                        .setMaxSupplyUsd(maxSuppyUsd)
                        .setLastupdated(symbolTickerJSONObject.getLong("last_updated"))
                        .build();

                symbolTickers.add(symbolTicker);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the CoinMarketCap JSON results", e);
        } catch (NumberFormatException e) {
            Log.e("QueryUtils", "Problem parsing the CoinMarketCap JSON results ", e);
        } catch (Exception e) {
            Log.e("QueryUtils", "Problem parsing the CoinMarketCap JSON results ", e);
        }


        // Return the list of ticker objects
        return symbolTickers;
    }
}
