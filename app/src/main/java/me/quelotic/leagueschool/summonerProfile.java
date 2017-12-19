package me.quelotic.leagueschool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Objects;

public class summonerProfile extends Activity {

    private TextView txtSumName;
    private TextView txtSumLevel;
    private ImageView imgSumIcon;
    private TextView txtFiveSoloLeagueName, txtFiveSoloRank, txtFiveSoloWins, txtFiveSoloLosses, txtFiveSoloLP;
    private TextView txtFlex3LeagueName, txtFlex3Rank, txtFlex3Wins, txtFlex3Losses, txtFlex3LP;
    private TextView txtFlex5LeagueName, txtFlex5Rank, txtFlex5Wins, txtFlex5Losses, txtFlex5LP;
    private ImageView imgFiveSoloHS, imgFiveSoloFB, imgFiveSoloVET;
    private ImageView imgFlex3HS, imgFlex3FB, imgFlex3VET;
    private ImageView imgFlex5HS, imgFlex5FB, imgFlex5VET;
    String summonerID, accountID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_summoner_profile);

        final String apiKey = "RGAPI-fe3d452c-117d-48c5-8d15-1ffba1b4d9ca";
        txtSumName = findViewById(R.id.sumName);
        txtSumLevel = findViewById(R.id.sumLevel);
        imgSumIcon = findViewById(R.id.sumIcon);

        txtFiveSoloLeagueName = findViewById(R.id.fiveSoloLeagueName);
        txtFiveSoloRank = findViewById(R.id.fiveSoloRank);
        txtFiveSoloWins = findViewById(R.id.fiveSoloWins);
        txtFiveSoloLosses = findViewById(R.id.fiveSoloLosses);
        txtFiveSoloLP = findViewById(R.id.fiveSoloLP);
        imgFiveSoloHS = findViewById(R.id.fiveSoloHS);
        imgFiveSoloFB = findViewById(R.id.fiveSoloFB);
        imgFiveSoloVET = findViewById(R.id.fiveSoloVET);

        txtFlex3LeagueName = findViewById(R.id.flex3LeagueName);
        txtFlex3Rank = findViewById(R.id.flex3Rank);
        txtFlex3Wins = findViewById(R.id.flex3Wins);
        txtFlex3Losses = findViewById(R.id.flex3Losses);
        txtFlex3LP = findViewById(R.id.flex3LP);
        imgFlex3HS = findViewById(R.id.flex3HS);
        imgFlex3FB = findViewById(R.id.flex3FB);
        imgFlex3VET = findViewById(R.id.flex3VET);

        txtFlex5LeagueName = findViewById(R.id.flex5LeagueName);
        txtFlex5Rank = findViewById(R.id.flex5Rank);
        txtFlex5Wins = findViewById(R.id.flex5Wins);
        txtFlex5Losses = findViewById(R.id.flex5Losses);
        txtFlex5LP = findViewById(R.id.flex5LP);
        imgFlex5HS = findViewById(R.id.flex5HS);
        imgFlex5FB = findViewById(R.id.flex5FB);
        imgFlex5VET = findViewById(R.id.flex5VET);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        final String summonerName = bundle.getString("keySumName");
        final String server = bundle.getString("keyServer");


        // ############################## FIRST JSON TASK ####################################

        //create the json task with the data process override for the first level of data fetching
        final jsonTasks asyncTask = new jsonTasks(new jsonTasks.AsyncResponse() {

            @Override
            public void processFinish(String output) {
                String TAG = summonerProfile.class.getSimpleName();
                JSONObject sumInfo;
                if (output != null) {
                    try {
                        //create json object from the output
                        sumInfo = new JSONObject(output);
                        //set the text and image fields
                        txtSumName.setText(sumInfo.getString("name"));
                        txtSumLevel.setText(String.format(Locale.US,"%d", sumInfo.getInt("summonerLevel")));
                        Picasso.with(getBaseContext()).load("https://ddragon.leagueoflegends.com/cdn/7.24.2/img/profileicon/" + Integer.toString(sumInfo.getInt("profileIconId")) + ".png").into(imgSumIcon);
                        //set the variables to use later
                        summonerID = Integer.toString(sumInfo.getInt("id"));
                        accountID = Integer.toString(sumInfo.getInt("accountId"));
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }

                    // ########################### SECOND JSON TASK ####################################

                    //create the json task with the data process override for the first level of data fetching
                    jsonTasks asyncTask2 = new jsonTasks(new jsonTasks.AsyncResponse() {

                        @Override
                        public void processFinish(String output) {
                            String TAG = summonerProfile.class.getSimpleName();
                            if (output != null) {
                                try {
                                    //create json objects from the output
                                    JSONArray sumPosArray = new JSONArray(output);
                                    for (int i = 0; i < sumPosArray.length(); i++) {
                                        JSONObject c = sumPosArray.getJSONObject(i);
                                        if (Objects.equals(c.getString("queueType"), "RANKED_SOLO_5x5")){
                                            txtFiveSoloLeagueName.setText(c.getString("leagueName"));
                                            txtFiveSoloRank.setText(c.getString("tier"));
                                            txtFiveSoloRank.append(" ");
                                            txtFiveSoloRank.append(c.getString("rank"));
                                            txtFiveSoloWins.setText(String.format(Locale.US,"%d", c.getInt("wins")));
                                            txtFiveSoloWins.append("W");
                                            txtFiveSoloLosses.setText(String.format(Locale.US,"%d", c.getInt("losses")));
                                            txtFiveSoloLosses.append("L");
                                            txtFiveSoloLP.setText(String.format(Locale.US,"%d", c.getInt("leaguePoints")));
                                            txtFiveSoloLP.append("LP");
                                            if (c.getBoolean("hotStreak")){ imgFiveSoloHS.setImageResource(R.drawable.thotstreak); }
                                            else { imgFiveSoloHS.setImageResource(R.drawable.fhotstreak); }
                                            if (c.getBoolean("freshBlood")){ imgFiveSoloFB.setImageResource(R.drawable.tfreshblood); }
                                            else { imgFiveSoloFB.setImageResource(R.drawable.ffreshblood); }
                                            if (c.getBoolean("veteran")){ imgFiveSoloVET.setImageResource(R.drawable.tveteran); }
                                            else { imgFiveSoloVET.setImageResource(R.drawable.fveteran); }
                                        } else if (Objects.equals(c.getString("queueType"), "RANKED_FLEX_TT")){
                                            txtFlex3LeagueName.setText(c.getString("leagueName"));
                                            txtFlex3Rank.setText(c.getString("tier"));
                                            txtFlex3Rank.append(" ");
                                            txtFlex3Rank.append(c.getString("rank"));
                                            txtFlex3Wins.setText(String.format(Locale.US,"%d", c.getInt("wins")));
                                            txtFlex3Wins.append("W");
                                            txtFlex3Losses.setText(String.format(Locale.US,"%d", c.getInt("losses")));
                                            txtFlex3Losses.append("L");
                                            txtFlex3LP.setText(String.format(Locale.US,"%d", c.getInt("leaguePoints")));
                                            txtFlex3LP.append("LP");
                                            if (c.getBoolean("hotStreak")){ imgFlex3HS.setImageResource(R.drawable.thotstreak); }
                                            else { imgFlex3HS.setImageResource(R.drawable.fhotstreak); }
                                            if (c.getBoolean("freshBlood")){ imgFlex3FB.setImageResource(R.drawable.tfreshblood); }
                                            else { imgFlex3FB.setImageResource(R.drawable.ffreshblood); }
                                            if (c.getBoolean("veteran")){ imgFlex3VET.setImageResource(R.drawable.tveteran); }
                                            else { imgFlex3VET.setImageResource(R.drawable.fveteran); }
                                        } else if (Objects.equals(c.getString("queueType"), "RANKED_FLEX_SR")){
                                            txtFlex5LeagueName.setText(c.getString("leagueName"));
                                            txtFlex5Rank.setText(c.getString("tier"));
                                            txtFlex5Rank.append(" ");
                                            txtFlex5Rank.append(c.getString("rank"));
                                            txtFlex5Wins.setText(String.format(Locale.US,"%d", c.getInt("wins")));
                                            txtFlex5Wins.append("W");
                                            txtFlex5Losses.setText(String.format(Locale.US,"%d", c.getInt("losses")));
                                            txtFlex5Losses.append("L");
                                            txtFlex5LP.setText(String.format(Locale.US,"%d", c.getInt("leaguePoints")));
                                            txtFlex5LP.append("LP");
                                            if (c.getBoolean("hotStreak")){ imgFlex5HS.setImageResource(R.drawable.thotstreak); }
                                            else { imgFlex5HS.setImageResource(R.drawable.fhotstreak); }
                                            if (c.getBoolean("freshBlood")){ imgFlex5FB.setImageResource(R.drawable.tfreshblood); }
                                            else { imgFlex5FB.setImageResource(R.drawable.ffreshblood); }
                                            if (c.getBoolean("veteran")){ imgFlex5VET.setImageResource(R.drawable.tveteran); }
                                            else { imgFlex5VET.setImageResource(R.drawable.fveteran); }
                                        }
                                    }
                                } catch (final JSONException e) {
                                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(),
                                                    "Json parsing error: " + e.getMessage(),
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    });
                                }
                            }
                        }
                    });
                    //execute the second asyncTask
                    asyncTask2.execute("https://" + server + ".api.riotgames.com/lol/league/v3/positions/by-summoner/" + summonerID + "?api_key=" + apiKey);
                }
            }
        });
        //execute the first asyncTask
        asyncTask.execute("https://" + server + ".api.riotgames.com/lol/summoner/v3/summoners/by-name/" + summonerName + "?api_key=" + apiKey);

        //button brings up match history
        Button btnMatchHistory = findViewById(R.id.btnMatchHistory);
        btnMatchHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create intent
                Intent intentMatchHist = new Intent(summonerProfile.this, matchHistory.class);
                //create bundle
                Bundle bundleMatchHist = new Bundle();
                //place the strings into the bundle
                bundleMatchHist.putString("keyServer", server);
                bundleMatchHist.putString("keyApiKey", apiKey);
                bundleMatchHist.putString("keySumName", summonerName);
                //place the bundle in the intent
                intentMatchHist.putExtras(bundleMatchHist);
                //start the activity
                startActivity(intentMatchHist);
            }
        });
    }
}