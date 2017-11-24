package me.quelotic.leagueschool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class summonerProfile extends Activity {

    private TextView txtSumName;
    private TextView txtSumLevel;
    private ImageView imgSumIcon;
    String forBunSumID, forBunAccID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_summoner_profile);

        txtSumName = (TextView) findViewById(R.id.sumName);
        txtSumLevel = (TextView) findViewById(R.id.sumLevel);
        imgSumIcon = (ImageView) findViewById(R.id.sumIcon);

        Bundle bundle = getIntent().getExtras();
        String bunSumName = bundle.getString("sumName");
        final String bunServer = bundle.getString("server");

        new summonerProfile.JSONTask().execute("https://" + bunServer + ".api.riotgames.com/lol/summoner/v3/summoners/by-name/" + bunSumName + "?api_key=RGAPI-5f588f65-8e43-4503-8bdc-a29de1561ff6");

        //Button brings up match history
        Button btnMatchHistory = (Button)findViewById(R.id.btnMatchHistory);
        btnMatchHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create intent
                Intent intentMatchHist = new Intent(summonerProfile.this, matchHistory.class);
                //create bundle
                Bundle bundleMatchHist = new Bundle();
                //place the strings into the bundle
                bundleMatchHist.putString("server", bunServer);
                bundleMatchHist.putString("sumID", forBunSumID);
                bundleMatchHist.putString("accID", forBunAccID);
                //place the bundle in the intent
                intentMatchHist.putExtras(bundleMatchHist);
                //start the activity
                startActivity(intentMatchHist);
            }
        });
    }

    public class JSONTask extends AsyncTask<String, String, ArrayList> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(summonerProfile.this);
            dialog.setTitle("Fetching data");
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected ArrayList doInBackground(String... params) {

            HttpsURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                ArrayList result = new ArrayList();
                String finalJSON = buffer.toString();
                JSONObject sumInfo = null;
                try {
                    sumInfo = new JSONObject(finalJSON);
                    result.add(sumInfo.getString("name"));
                    result.add(Integer.toString(sumInfo.getInt("summonerLevel")));
                    result.add(Integer.toString(sumInfo.getInt("profileIconId")));
                    result.add(Integer.toString(sumInfo.getInt("id")));
                    result.add(Integer.toString(sumInfo.getInt("accountId")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList result) {
            super.onPostExecute(result);
            if (result != null) {
                txtSumName.setText((String) result.get(0));
                txtSumLevel.setText((String) result.get(1));
                Picasso.with(getBaseContext()).load("https://ddragon.leagueoflegends.com/cdn/7.23.1/img/profileicon/" + result.get(2) + ".png").into(imgSumIcon);
                forBunSumID = (String) result.get(3);
                forBunAccID = (String) result.get(4);
            }
            dialog.dismiss();
        }
    }
}