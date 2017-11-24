package me.quelotic.leagueschool;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import me.quelotic.leagueschool.models.matchHistoryModel;

public class matchHistory extends Activity {

    private TextView textView;
    private ListView lvMatchHist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_match_history);

        lvMatchHist = (ListView) findViewById(R.id.matchHistoryListView);

        Bundle bundle = getIntent().getExtras();
        String sumID = bundle.getString("sumID");
        String accID = bundle.getString("accID");
        String server = bundle.getString("server");

        new JSONTask().execute(sumID, accID, server);

    }
    public class JSONTask extends AsyncTask<String, String, List<matchHistoryModel>> {

        @Override
        protected List<matchHistoryModel> doInBackground(String... params) {
            String matchlists = "https://" + params[2] + ".api.riotgames.com/lol/match/v3/matchlists/by-account/" + params[1] + "/recent?api_key=RGAPI-5f588f65-8e43-4503-8bdc-a29de1561ff6";
            String matchinfo = "https://" + params[2] + ".api.riotgames.com/lol/match/v3/matchlists/by-account/" + params[0] + "/recent?api_key=RGAPI-5f588f65-8e43-4503-8bdc-a29de1561ff6";
            HttpsURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(matchlists);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJSON = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJSON);
                JSONArray parentArray = parentObject.getJSONArray("matches");

                List<matchHistoryModel> matchHistModelList = new ArrayList<>();

                for (int i=0; i<parentArray.length(); i++){
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    matchHistoryModel matchHistModel = new matchHistoryModel();
                    matchHistModel.setPlatformId(finalObject.getString("platformId"));
                    matchHistModel.setGameId(finalObject.getInt("gameId"));
                    matchHistModel.setChampion(finalObject.getInt("champion"));
                    matchHistModel.setQueue(finalObject.getInt("queue"));
                    matchHistModel.setSeason(finalObject.getInt("season"));
                    matchHistModel.setRole(finalObject.getString("role"));
                    matchHistModel.setLane(finalObject.getString("lane"));
                    matchHistModelList.add(matchHistModel);
                }
                return matchHistModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null){
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
        protected void onPostExecute(List<matchHistoryModel> result) {
            super.onPostExecute(result);
            matchHistoryAdapter adapter = new matchHistoryAdapter(getApplicationContext(),R.layout.match_history_list_row, result);
            lvMatchHist.setAdapter(adapter);
        }
    }

    public class matchHistoryAdapter extends ArrayAdapter{

        private List<matchHistoryModel> matchHistoryModelList;
        private int resource;
        private LayoutInflater inflater;

        public matchHistoryAdapter(Context context, int resource, List<matchHistoryModel> objects){
            super(context, resource, objects);
            matchHistoryModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        private String getChampionName(int x) {
            switch (x) {
                case 1: return "Annie";
                case 2: return "Olaf";
                case 3: return "Galio";
                case 4: return "Twisted Fate";
                case 5: return "Xin Zhao";
                case 6: return "Urgot";
                case 7: return "LeBlanc";
                case 8: return "Vladimir";
                case 9: return "Fiddlesticks";
                case 10: return "Kayle";
                case 11: return "Master Yi";
                case 12: return "Alistar";
                case 13: return "Ryze";
                case 14: return "Sion";
                case 15: return "Sivir";
                case 16: return "Soraka";
                case 17: return "Teemo";
                case 18: return "Tristana";
                case 19: return "Warwick";
                case 20: return "Nunu";
                case 21: return "Miss Fortune";
                case 22: return "Ashe";
                case 23: return "Tryndamere";
                case 24: return "Jax";
                case 25: return "Morgana";
                case 26: return "Zilean";
                case 27: return "Singed";
                case 28: return "Evelynn";
                case 29: return "Twitch";
                case 30: return "Karthus";
                case 31: return "Cho'Gath";
                case 32: return "Amumu";
                case 33: return "Rammus";
                case 34: return "Anivia";
                case 35: return "Shaco";
                case 36: return "Dr. Mundo";
                case 37: return "Sona";
                case 38: return "Kassadin";
                case 39: return "Irelia";
                case 40: return "Janna";
                case 41: return "Gangplank";
                case 42: return "Corki";
                case 43: return "Karma";
                case 44: return "Taric";
                case 45: return "Veigar";
                case 48: return "Trundle";
                case 50: return "Swain";
                case 51: return "Caitlyn";
                case 53: return "Blitzcrank";
                case 54: return "Malphite";
                case 55: return "Katarina";
                case 56: return "Nocturne";
                case 57: return "Maokai";
                case 58: return "Renekton";
                case 59: return "Jarvan IV";
                case 61: return "Orianna";
                case 62: return "Wukong";
                case 63: return "Brand";
                case 64: return "Lee Sin";
                case 67: return "Vayne";
                case 68: return "Rumble";
                case 69: return "Cassiopeia";
                case 72: return "Skarner";
                case 74: return "Heimerdinger";
                case 75: return "Nasus";
                case 76: return "Nidalee";
                case 77: return "Udyr";
                case 78: return "Poppy";
                case 79: return "Gragas";
                case 80: return "Pantheon";
                case 81: return "Ezreal";
                case 82: return "Mordekaiser";
                case 83: return "Yorick";
                case 84: return "Akali";
                case 85: return "Kennen";
                case 86: return "Garen";
                case 89: return "Leona";
                case 90: return "Malzahar";
                case 91: return "Talon";
                case 92: return "Riven";
                case 96: return "Kog'Maw";
                case 98: return "Shen";
                case 99: return "Lux";
                case 101: return "Xerath";
                case 102: return "Shyvana";
                case 103: return "Ahri";
                case 104: return "Graves";
                case 105: return "Fizz";
                case 106: return "Volibear";
                case 107: return "Rengar";
                case 110: return "Varus";
                case 111: return "Nautilus";
                case 112: return "Viktor";
                case 113: return "Sejuani";
                case 114: return "Fiora";
                case 115: return "Ziggs";
                case 117: return "Lulu";
                case 119: return "Draven";
                case 120: return "Hecarim";
                case 122: return "Darius";
                case 126: return "Jayce";
                case 131: return "Diana";
                case 143: return "Zyra";
            }
            return "";
        }

        ImageView championIcon;
        TextView championName;
        String strChampionName;

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if (convertView == null){
                convertView = inflater.inflate(resource, null);
            }

            championIcon = (ImageView) convertView.findViewById(R.id.championIcon);
            championName = (TextView) convertView.findViewById(R.id.championName);
            strChampionName = getChampionName(matchHistoryModelList.get(position).getChampion());

            Picasso.with(getBaseContext()).load("https://ddragon.leagueoflegends.com/cdn/7.23.1/img/champion/" + strChampionName + ".png").into(championIcon);
            championName.setText(strChampionName);

            return convertView;
        }
    }
}

