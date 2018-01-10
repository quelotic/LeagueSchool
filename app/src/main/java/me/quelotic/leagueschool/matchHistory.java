package me.quelotic.leagueschool;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Locale;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import me.quelotic.leagueschool.models.matchHistoryModel;

public class matchHistory extends Activity {

    private ListView lvMatchHist;
    String summonerID, accountID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_match_history);

        lvMatchHist = findViewById(R.id.matchHistoryListView);

        Bundle bundle = getIntent().getExtras();
        final String server = bundle.getString("keyServer");
        final String summonerName = bundle.getString("keySumName");
        final String apiKey = bundle.getString("keyApiKey");

        // ############################## FIRST JSON TASK ####################################

        //create the json task with the data process override for the first level of data fetching
        final jsonTasks asyncTask = new jsonTasks(new jsonTasks.AsyncResponse() {

            @Override
            public void processFinish(String output) {
                String TAG = matchHistory.class.getSimpleName();
                JSONObject sumInfo;
                if (output != null) {
                    try {
                        //create json object from the output
                        sumInfo = new JSONObject(output);
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
                            String TAG = matchHistory.class.getSimpleName();
                            if (output != null) {
                                try {
                                    JSONObject parentObject = new JSONObject(output);
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

                                    matchHistoryAdapter adapter = new matchHistoryAdapter(getApplicationContext(),R.layout.match_history_list_row, matchHistModelList);
                                    lvMatchHist.setAdapter(adapter);


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
                    asyncTask2.execute("https://" + server + ".api.riotgames.com/lol/match/v3/matchlists/by-account/" + accountID + "/recent?api_key=" + apiKey);
                }
            }
        });
        //execute the first asyncTask
        asyncTask.execute("https://" + server + ".api.riotgames.com/lol/summoner/v3/summoners/by-name/" + summonerName + "?api_key=" + apiKey);
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
                case 266: return "Aatrox";
                case 412: return "Thresh";
                case 23: return "Tryndamere";
                case 79: return "Gragas";
                case 69: return "Cassiopeia";
                case 136: return "Aurelion Sol";
                case 13: return "Ryze";
                case 78: return "Poppy";
                case 14: return "Sion";
                case 1: return "Annie";
                case 202: return "Jhin";
                case 43: return "Karma";
                case 111: return "Nautilus";
                case 240: return "Kled";
                case 99: return "Lux";
                case 103: return "Ahri";
                case 2: return "Olaf";
                case 112: return "Viktor";
                case 34: return "Anivia";
                case 27: return "Singed";
                case 86: return "Garen";
                case 127: return "Lissandra";
                case 57: return "Maokai";
                case 25: return "Morgana";
                case 28: return "Evelynn";
                case 105: return "Fizz";
                case 74: return "Heimerdinger";
                case 238: return "Zed";
                case 68: return "Rumble";
                case 82: return "Mordekaiser";
                case 37: return "Sona";
                case 96: return "Kog'Maw";
                case 55: return "Katarina";
                case 117: return "Lulu";
                case 22: return "Ashe";
                case 30: return "Karthus";
                case 12: return "Alistar";
                case 122: return "Darius";
                case 67: return "Vayne";
                case 110: return "Varus";
                case 77: return "Udyr";
                case 89: return "Leona";
                case 126: return "Jayce";
                case 134: return "Syndra";
                case 80: return "Pantheon";
                case 92: return "Riven";
                case 121: return "Kha'Zix";
                case 42: return "Corki";
                case 268: return "Azir";
                case 51: return "Caitlyn";
                case 76: return "Nidalee";
                case 85: return "Kennen";
                case 3: return "Galio";
                case 45: return "Veigar";
                case 432: return "Bard";
                case 150: return "Gnar";
                case 90: return "Malzahar";
                case 104: return "Graves";
                case 254: return "Vi";
                case 10: return "Kayle";
                case 39: return "Irelia";
                case 64: return "Lee Sin";
                case 420: return "Illaoi";
                case 60: return "Elise";
                case 106: return "Volibear";
                case 20: return "Nunu";
                case 4: return "Twisted Fate";
                case 24: return "Jax";
                case 102: return "Shyvana";
                case 429: return "Kalista";
                case 36: return "Dr. Mundo";
                case 427: return "Ivern";
                case 131: return "Diana";
                case 223: return "Tahm Kench";
                case 63: return "Brand";
                case 113: return "Sejuani";
                case 8: return "Vladimir";
                case 154: return "Zac";
                case 421: return "Rek'Sai";
                case 133: return "Quinn";
                case 84: return "Akali";
                case 163: return "Taliyah";
                case 18: return "Tristana";
                case 120: return "Hecarim";
                case 15: return "Sivir";
                case 236: return "Lucian";
                case 107: return "Rengar";
                case 19: return "Warwick";
                case 72: return "Skarner";
                case 54: return "Malphite";
                case 157: return "Yasuo";
                case 101: return "Xerath";
                case 17: return "Teemo";
                case 75: return "Nasus";
                case 58: return "Renekton";
                case 119: return "Draven";
                case 35: return "Shaco";
                case 50: return "Swain";
                case 91: return "Talon";
                case 40: return "Janna";
                case 115: return "Ziggs";
                case 245: return "Ekko";
                case 61: return "Orianna";
                case 114: return "Fiora";
                case 9: return "Fiddlesticks";
                case 31: return "Cho'Gath";
                case 33: return "Rammus";
                case 7: return "LeBlanc";
                case 16: return "Soraka";
                case 26: return "Zilean";
                case 56: return "Nocturne";
                case 222: return "Jinx";
                case 83: return "Yorick";
                case 6: return "Urgot";
                case 203: return "Kindred";
                case 21: return "Miss Fortune";
                case 62: return "Wukong";
                case 53: return "Blitzcrank";
                case 98: return "Shen";
                case 201: return "Braum";
                case 5: return "Xin Zhao";
                case 29: return "Twitch";
                case 11: return "Master Yi";
                case 44: return "Taric";
                case 32: return "Amumu";
                case 41: return "Gangplank";
                case 48: return "Trundle";
                case 38: return "Kassadin";
                case 161: return "Vel'Koz";
                case 143: return "Zyra";
                case 267: return "Nami";
                case 59: return "Jarvan IV";
                case 81: return "Ezreal";
                case 164: return "Camille";
                case 141: return "Kayn";
                case 516: return "Ornn";
                case 497: return "Rakan";
                case 498: return "Xayah";
                case 142: return "Zoe";
            }
            return "";
        }

        private String getChampionTitle(int x) {
            switch (x) {
                case 266: return "the Darkin Blade";
                case 412: return "the Chain Warden";
                case 23: return "the Barbarian King";
                case 79: return "the Rabble Rouser";
                case 69: return "the Serpent's Embrace";
                case 136: return "the Star Forger";
                case 13: return "the Rune Mage";
                case 78: return "Keeper of the Hammer";
                case 14: return "the Undead Juggernaut";
                case 1: return "the Dark Child";
                case 202: return "the Virtuoso";
                case 43: return "the Enlightened One";
                case 111: return "the Titan of the Depths";
                case 240: return "the Cantankerous Cavalier";
                case 99: return "the Lady of Luminosity";
                case 103: return "the Nine-Tailed Fox";
                case 2: return "the Berserker";
                case 112: return "the Machine Herald";
                case 34: return "the Cryophoenix";
                case 27: return "the Mad Chemist";
                case 86: return "the Might of Demacia";
                case 127: return "the Ice Witch";
                case 57: return "the Twisted Treant";
                case 25: return "Fallen Angel";
                case 28: return "Agony's Embrace";
                case 105: return "the Tidal Trickster";
                case 74: return "the Revered Inventor";
                case 238: return "the Master of Shadows";
                case 68: return "the Mechanized Menace";
                case 82: return "the Iron Revenant";
                case 37: return "Maven of the Strings";
                case 96: return "the Mouth of the Abyss";
                case 55: return "the Sinister Blade";
                case 117: return "the Fae Sorceress";
                case 22: return "the Frost Archer";
                case 30: return "the Deathsinger";
                case 12: return "the Minotaur";
                case 122: return "the Hand of Noxus";
                case 67: return "the Night Hunter";
                case 110: return "the Arrow of Retribution";
                case 77: return "the Spirit Walker";
                case 89: return "the Radiant Dawn";
                case 126: return "the Defender of Tomorrow";
                case 134: return "the Dark Sovereign";
                case 80: return "the Artisan of War";
                case 92: return "the Exile";
                case 121: return "the Voidreaver";
                case 42: return "the Daring Bombardier";
                case 268: return "the Emperor of the Sands";
                case 51: return "the Sheriff of Piltover";
                case 76: return "the Bestial Huntress";
                case 85: return "the Heart of the Tempest";
                case 3: return "the Colossus";
                case 45: return "the Tiny Master of Evil";
                case 432: return "the Wandering Caretaker";
                case 150: return "the Missing Link";
                case 90: return "the Prophet of the Void";
                case 104: return "the Outlaw";
                case 254: return "the Piltover Enforcer";
                case 10: return "the Judicator";
                case 39: return "the Will of the Blades";
                case 64: return "the Blind Monk";
                case 420: return "Illaoi";
                case 60: return "Elise";
                case 106: return "Volibear";
                case 20: return "Nunu";
                case 4: return "Twisted Fate";
                case 24: return "Jax";
                case 102: return "Shyvana";
                case 429: return "Kalista";
                case 36: return "Dr. Mundo";
                case 427: return "Ivern";
                case 131: return "Diana";
                case 223: return "Tahm Kench";
                case 63: return "Brand";
                case 113: return "Sejuani";
                case 8: return "Vladimir";
                case 154: return "Zac";
                case 421: return "Rek'Sai";
                case 133: return "Quinn";
                case 84: return "Akali";
                case 163: return "Taliyah";
                case 18: return "Tristana";
                case 120: return "Hecarim";
                case 15: return "Sivir";
                case 236: return "Lucian";
                case 107: return "Rengar";
                case 19: return "Warwick";
                case 72: return "Skarner";
                case 54: return "Malphite";
                case 157: return "Yasuo";
                case 101: return "Xerath";
                case 17: return "Teemo";
                case 75: return "Nasus";
                case 58: return "Renekton";
                case 119: return "Draven";
                case 35: return "Shaco";
                case 50: return "Swain";
                case 91: return "Talon";
                case 40: return "Janna";
                case 115: return "Ziggs";
                case 245: return "the Boy Who Shattered Time";
                case 61: return "Orianna";
                case 114: return "Fiora";
                case 9: return "Fiddlesticks";
                case 31: return "Cho'Gath";
                case 33: return "Rammus";
                case 7: return "LeBlanc";
                case 16: return "Soraka";
                case 26: return "Zilean";
                case 56: return "Nocturne";
                case 222: return "Jinx";
                case 83: return "Yorick";
                case 6: return "Urgot";
                case 203: return "Kindred";
                case 21: return "Miss Fortune";
                case 62: return "the Monkey King";
                case 53: return "Blitzcrank";
                case 98: return "Shen";
                case 201: return "Braum";
                case 5: return "Xin Zhao";
                case 29: return "Twitch";
                case 11: return "Master Yi";
                case 44: return "Taric";
                case 32: return "Amumu";
                case 41: return "Gangplank";
                case 48: return "Trundle";
                case 38: return "Kassadin";
                case 161: return "Vel'Koz";
                case 143: return "Zyra";
                case 267: return "Nami";
                case 59: return "Jarvan IV";
                case 81: return "the Prodigal Explorer";
                case 164: return "Camille";
                case 141: return "Kayn";
                case 516: return "Ornn";
                case 497: return "Rakan";
                case 498: return "Xayah";
                case 142: return "the Aspect of Twilight";
            }
            return "";
        }

        private String getQueue(int x) {
            switch (x) {
                case 1010: return "ARURF Summoner's Rift";
                case 450: return "ARAM Howling Abyss";
                case 430: return "Summoner's Rift [Blind pick]";
                case 920: return "Legend of the Poro King";
            }
            return "";
        }

        ImageView championIcon;
        TextView championName, championTitle, queue;
        String strChampionName, strChampionTitle, strQueue;

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if (convertView == null){
                convertView = inflater.inflate(resource, null);
            }

            championIcon = convertView.findViewById(R.id.championIcon);
            championName = convertView.findViewById(R.id.championName);
            championTitle = convertView.findViewById(R.id.championTitle);
            queue = convertView.findViewById(R.id.queue);
            strChampionName = getChampionName(matchHistoryModelList.get(position).getChampion());
            strChampionTitle = getChampionTitle(matchHistoryModelList.get(position).getChampion());
            strQueue = getQueue(matchHistoryModelList.get(position).getQueue());

            Picasso.with(getBaseContext()).load("https://ddragon.leagueoflegends.com/cdn/7.24.2/img/champion/" + strChampionName + ".png").into(championIcon);
            championName.setText(strChampionName);
            championTitle.setText(strChampionTitle);
            queue.setText(strQueue);

            return convertView;
        }
    }
}

