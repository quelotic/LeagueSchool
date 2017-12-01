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

import java.util.Objects;

public class summonerProfile extends Activity {

    private TextView txtSumName;
    private TextView txtSumLevel;
    private ImageView imgSumIcon;
    private TextView testText, testText2;
    String summonerID, accountID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_summoner_profile);

        String apiKey = "RGAPI-b7910bbe-3d36-4414-8c4a-40a5b13dd535";
        txtSumName = findViewById(R.id.sumName);
        txtSumLevel = findViewById(R.id.sumLevel);
        imgSumIcon = findViewById(R.id.sumIcon);
        testText = findViewById(R.id.textView);
        testText2 = findViewById(R.id.textView2);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        final String summonerName = bundle.getString("keySumName");
        final String server = bundle.getString("keyServer");


        // ############################## FIRST JSON TASK ####################################

        //create the json task with the data process override for the first level of data fetching
        jsonTasks asyncTask = new jsonTasks(new jsonTasks.AsyncResponse() {

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
                        txtSumLevel.setText(Integer.toString(sumInfo.getInt("summonerLevel")));
                        Picasso.with(getBaseContext()).load("https://ddragon.leagueoflegends.com/cdn/7.23.1/img/profileicon/" + Integer.toString(sumInfo.getInt("profileIconId")) + ".png").into(imgSumIcon);
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
                }
            }
        });
        //execute the first asyncTask
        asyncTask.execute("https://" + server + ".api.riotgames.com/lol/summoner/v3/summoners/by-name/" + summonerName + "?api_key=" + apiKey);



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
                                testText.setText(c.getString("leagueName"));
                            } else if (Objects.equals(c.getString("queueType"), "RANKED_FLEX_TT")){
                                testText2.setText(c.getString("leagueName"));
                            }
                            String leagueName = c.getString("leagueName");
                            String tier = c.getString("tier");
                            String queueType = c.getString("queueType");
                            String rank = c.getString("rank");
//                            String leaguePoints = c.getInt(leaguePoints);
//                            String rank = c.getString("rank");
//                            String rank = c.getString("rank");



                        }


                        //set the text and image fields

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
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //execute the second asyncTask
        asyncTask2.execute("https://" + server + ".api.riotgames.com/lol/league/v3/positions/by-summoner/" + summonerID + "?api_key=" + apiKey);


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
                bundleMatchHist.putString("keySumID", summonerID);
                bundleMatchHist.putString("keyAccID", accountID);
                //place the bundle in the intent
                intentMatchHist.putExtras(bundleMatchHist);
                //start the activity
                startActivity(intentMatchHist);
            }
        });
    }
}