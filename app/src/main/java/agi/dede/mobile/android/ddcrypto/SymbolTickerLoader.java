package agi.dede.mobile.android.ddcrypto;

import android.content.Context;
import android.content.AsyncTaskLoader;
import java.util.List;

/**
 * Created by Eric on 14/01/2018.
 */
public class SymbolTickerLoader extends AsyncTaskLoader<List<SymbolTicker>>{

    /** Tag for log messages */
    private static final String LOG_TAG = SymbolTickerLoader.class.getName();

    /** Query URL */
    private String url;

    /**
     * Constructs a new {@link SymbolTicker}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public SymbolTickerLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<SymbolTicker> loadInBackground() {
        if (url == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of SymbolTickers.
        List<SymbolTicker> symbolTickers = QueryUtils.fetchSymbolTickers(url);
        return symbolTickers;
    }
}
