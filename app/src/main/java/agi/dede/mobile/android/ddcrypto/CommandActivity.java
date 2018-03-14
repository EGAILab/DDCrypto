package agi.dede.mobile.android.ddcrypto;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.List;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.Context;
import android.content.Loader;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;

import android.speech.RecognizerIntent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Math.abs;


public class CommandActivity extends AppCompatActivity
        implements TextToSpeech.OnInitListener, LoaderCallbacks<List<SymbolTicker>> {

    private static final String LOG_TAG = CommandActivity.class.getName();

    private EditText etEditCommand;
    private TextView tvInteractiveHistory;
    private ImageButton ibVoice;

    private TextToSpeech tts;
    private Random random;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int SYMBOLTICKER_LOADER_ID = 1;
    private static final int ALLSYMBOLTICKER_LOADER_ID = 2;

    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);

        etEditCommand = (EditText) findViewById(R.id.textEditCommand);
        tvInteractiveHistory = (TextView) findViewById(R.id.textInteractionHistory);
        tvInteractiveHistory.setMovementMethod(new ScrollingMovementMethod());
        ibVoice = (ImageButton) findViewById(R.id.imageButtonVoice);

        tts = new TextToSpeech(this, this);
        random = new Random();
        // getActionBar().hide();

        ibVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        // Hide loading indicator
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // retrieve all coins
        // createTickerLoader("");
    }

    @Override
    public void onDestroy() {

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                // btnSpeak.setEnabled(true);
                // speakOut();
            }
        } else {
            Log.e("TTS", "Initialization Failed!");
        }
    }

    @Override
    public Loader<List<SymbolTicker>> onCreateLoader(int i, Bundle bundle) {
        return new SymbolTickerLoader(this, this.url);
    }

    private void pauseSpeech(int miniSeconds) {
        for(int i=0; i < miniSeconds*100; i++) {
        }
    }

    private void makeComments(SymbolTicker symbolTicker) {

        String comments = "";

        comments = comments + symbolTicker.getName() + " is trading at ";
        if(!symbolTicker.getName().equalsIgnoreCase("bitcoin")) {
            Double satoshi = symbolTicker.getPriceBtc() * 100000000;
            comments = comments + String.format("%.0f", satoshi) + " Satoshi and ";
        }

        comments = comments + symbolTicker.getPriceUsd() + " US dollar. ";

        //speakOut(comments);
        //pauseSpeech(10);
        //comments = "";

        double percentage = symbolTicker.getPriceChangePercent1h();
        if(percentage >= 0)
            comments = comments + "The price is up " + abs(percentage) + " percent last hour, ";
        else
            comments = comments + "The price is down " + abs(percentage) + " percent last hour, ";

        //speakOut(comments);
        //pauseSpeech(5);
        //comments = "";

        percentage = symbolTicker.getPriceChangePercent24h();
        if(percentage >= 0)
            comments = comments + "up " + abs(percentage) + " percent last 24 hours, ";
        else
            comments = comments + "down " + abs(percentage) + " percent last 24 hours, ";

        //speakOut(comments);
        //pauseSpeech(5);
        //comments = "";

        percentage = symbolTicker.getPriceChangePercent7d();
        if(percentage >= 0)
            comments = comments + "and up " + abs(percentage) + " percent last 7 days. ";
        else
            comments = comments + "and down " + abs(percentage) + " percent last 7 days. ";

        //speakOut(comments);
        //pauseSpeech(10);
        //comments = "";

        comments = comments + symbolTicker.getName() + " is going to the moon! ";
        //speakOut(comments);
        //pauseSpeech(10);
        //comments = "";

        comments = comments + "Trading advice: ";
        //speakOut(comments);
        //pauseSpeech(10);
        //comments = "";

        comments = comments + "Hoddle";
        speakOut(comments);
    }

    // Display tickers on screen
    @Override
    public void onLoadFinished(Loader<List<SymbolTicker>> loader, List<SymbolTicker> symbolTickers) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        //tvInteractiveHistory.setText("");

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (symbolTickers != null && !symbolTickers.isEmpty()) {

            if(symbolTickers.size() == 1) {
                SymbolTicker symbolTicker = symbolTickers.get(0);

                tvInteractiveHistory.append(DateFormat.getDateTimeInstance().format(new Date()) + "\n");
                tvInteractiveHistory.append("ID: " + symbolTicker.getId() + "\n");
                tvInteractiveHistory.append("Name: " + symbolTicker.getName() + "\n");
                tvInteractiveHistory.append("Symbol: " + symbolTicker.getSymbol() + "\n");
                tvInteractiveHistory.append("Rank: " + symbolTicker.getRank() + "\n");
                tvInteractiveHistory.append("Price(BTC): " + symbolTicker.getPriceBtc() + "\n");
                tvInteractiveHistory.append("Price(ETH): " + symbolTicker.getPriceEth() + "\n");
                tvInteractiveHistory.append("Price(USD): " + symbolTicker.getPriceUsd() + "\n");
                tvInteractiveHistory.append("Price(Local): " + symbolTicker.getPriceLocal() + "\n");
                tvInteractiveHistory.append("Price Changed 1h(USD): " + symbolTicker.getPriceChange1h() + "\n");
                tvInteractiveHistory.append("Price Changed 24h(USD): " + symbolTicker.getPriceChange24h() + "\n");
                tvInteractiveHistory.append("Price Changed 7d(USD): " + symbolTicker.getPriceChange7d() + "\n");
                tvInteractiveHistory.append("Price Changed 1h%: " + symbolTicker.getPriceChangePercent1h() + "\n");
                tvInteractiveHistory.append("Price Changed 24h%: " + symbolTicker.getPriceChangePercent24h() + "\n");
                tvInteractiveHistory.append("Price Changed 7d%: " + symbolTicker.getPriceChangePercent7d() + "\n");
                tvInteractiveHistory.append("Volume(USD): " + symbolTicker.getVolumeUsd24h() + "\n");
                tvInteractiveHistory.append("Market Cap(USD): " + symbolTicker.getMarketCapUsd() + "\n");
                tvInteractiveHistory.append("Available Supply(USD): " + symbolTicker.getAvailableSupplyUsd() + "\n");
                tvInteractiveHistory.append("Total Supply(USD): " + symbolTicker.getTotalSupplyUsd() + "\n");
                tvInteractiveHistory.append("Max Supply(USD): " + symbolTicker.getMaxSupplyUsd() + "\n");
                tvInteractiveHistory.append("Last Update: " + symbolTicker.getLastUpdated() + "\n");
                tvInteractiveHistory.append("\n");

                makeComments(symbolTicker);
            } else {
                // Retrieve all coins, update DB
            }
        } else {
            tvInteractiveHistory.append("Something is wrong, no result.");
        }
        etEditCommand.setText("");
    }

    @Override
    public void onLoaderReset(Loader<List<SymbolTicker>> loader) {
        // Loader reset, so we can clear out our existing data.
        // mAdapter.clear();
    }

    @SuppressWarnings("deprecation")
    private void speakOut(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String utteranceId = this.hashCode() + "";
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void answerCommand(String command) {
        if (random.nextBoolean()) {
            speakOut(getString(R.string.tts_command_answer_positive) + command);
        } else {
            speakOut(getString(R.string.tts_command_answer_negative));
        }
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {

        speakOut(getString(R.string.prompt_speech));
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.prompt_speech));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.prompt_speech_not_support),
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Initialise loader
    private void createTickerLoader(String coin) {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // ??? Pass the url with private class variable instead of args of initLoader
            // initLoader:args - Optional arguments to supply to the loader at construction.
            // If a loader already exists (a new one does not need to be created),
            // this parameter will be ignored and the last arguments continue to be used.
            // https://api.coinmarketcap.com/v1/ticker/?convert=AUD&limit=0
            // https://api.coinmarketcap.com/v1/ticker/bitcoin/?convert=AUD
            int loaderId;
            if(coin.isEmpty()) {
                this.url = "https://api.coinmarketcap.com/v1/ticker/?convert=AUD&limit=0";
                loaderId = ALLSYMBOLTICKER_LOADER_ID;
            } else {
                this.url = "https://api.coinmarketcap.com/v1/ticker/" + coin + "/?convert=AUD";
                loaderId = SYMBOLTICKER_LOADER_ID;
            }

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            if (loaderManager.getLoader(loaderId) == null) {
                // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                // because this activity implements the LoaderCallbacks interface).
                loaderManager.initLoader(loaderId, null, this);
            } else {
                loaderManager.restartLoader(loaderId, null, this);
            }

            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);
        }
    }

    // Parsing command string received
    private void handleCommand(String command) {

        tvInteractiveHistory.append("Command: " + command + "\n");

        String[] commandTokens = command.split(" ");
        String output = "";

        //if(commandTokens[0].equals(commandTokens[0].toUpperCase()))
        //    tvInteractiveHistory.append("Command is UPPERCASE\n");

        switch (commandTokens[0].toLowerCase()) {
            case "cp":
                createTickerLoader(commandTokens[1]);
                break;
            case "crypto":
                // if this is SYMBOL(all uppercase), convert to name
                // to be adding db mapping
                if(commandTokens[1].equals(commandTokens[1].toUpperCase())) {
                    switch (commandTokens[1]) {
                        case "BTC":
                            commandTokens[1]="bitcoin";
                            break;
                        case "ETH":
                            commandTokens[1]="ethereum";
                            break;
                        case "ADA":
                            commandTokens[1]="cardano";
                            break;
                    }
                }
                createTickerLoader(commandTokens[1]);
                break;
            default:
                answerCommand(command);
                output = "Command: " + command + "\n";
                etEditCommand.setText("");
                tvInteractiveHistory.append(output);
                break;
        }
    }

    /**
     * Called when receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String command = result.get(0);
                    handleCommand(command);
                }
                break;
            }
        }
    }

    /** Called when taps the Send button */
    public void sendCommand(View view) {

        String command = etEditCommand.getText().toString();
        handleCommand(command);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
