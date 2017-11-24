package me.quelotic.leagueschool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //must be set final cause accessed of within inner class
        final EditText txtSumName = (EditText) findViewById(R.id.txtSumName);
        final Spinner spinnerServer = (Spinner) findViewById(R.id.spinnerServer);

        //Button brings up profile
        Button btnSumProfile = (Button)findViewById(R.id.btnSumProfile);
        btnSumProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create intent
                Intent intentSumProfile = new Intent(main.this, summonerProfile.class);
                //create bundle
                Bundle bundle = new Bundle();
                //get name and server from fields
                String sumName = txtSumName.getText().toString();
                //get server id from spinner selection
                String server = "";
                switch (spinnerServer.getSelectedItem().toString()) {
                    case "EU Nordic and East":  server = "eun1";
                        break;
                    case "North America":  server = "na1";
                        break;
                    case "Europe West":  server = "euw1";
                        break;
                    case "Latin America North":  server = "la1";
                        break;
                    case "Latin America South":  server = "la2";
                        break;
                    case "Brazil":  server = "br1";
                        break;
                    case "Turkey":  server = "tr1";
                        break;
                    case "Russia":  server = "ru";
                        break;
                    case "Oceania":  server = "oc1";
                        break;
                    case "Japan":  server = "jp1";
                        break;
                    case "Korea":  server = "kr";
                        break;
                }
                //place them into the bundle
                bundle.putString("sumName", sumName);
                bundle.putString("server", server);
                //place the bundle in the intent
                intentSumProfile.putExtras(bundle);
                //start the activity
                startActivity(intentSumProfile);
            }
        });
    }
}
