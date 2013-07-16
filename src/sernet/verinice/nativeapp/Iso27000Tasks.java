package sernet.verinice.nativeapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Iso27000Tasks extends Activity{
	
	public Iso27000Tasks activity = this;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iso_27000_tasks);
       
        Button backButton = (Button) findViewById(R.id.backButton);
 
        Intent i = getIntent();
        // Receiving the Data
        String vname = i.getStringExtra("Vorname");
        String nname = i.getStringExtra("Nachname");


        //Initialize NetworkTask to pass data back later
    	NetworkTask myTask = new NetworkTask(activity);
    	myTask.execute(i.getStringExtra("ip_address"), i.getStringExtra("port"), i.getStringExtra("username"), i.getStringExtra("password"), "iso_27000");

        // Binding Click event to Button
        backButton.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
                //Closing SecondScreen Activity
                finish();
            }
        });
    }
	
	
	public void networkReady(String data){
		
		//If data is null - due to an exception in the network task
		if (data == null){
			Toast.makeText(getApplicationContext(), "Error! - Bitte Verbindungseinstellungen prüfen!", Toast.LENGTH_LONG).show();
			//Return to login screen
			finish();
		}
		else {
			try {
				JSONArray json = new JSONArray(data);
				JSONObject iso_27000_item;
				
				TableLayout tbl = (TableLayout) findViewById(R.id.iso_27000_table);
				
				
				TableRow row = new TableRow(this); 
				TextView text_view = new TextView(this);
				
				for(int i = 0; i < json.length(); i++) {
					iso_27000_item = json.getJSONObject(i);
			        System.out.println(iso_27000_item);
					row = new TableRow(this);
					
					text_view = new TextView(this);
					text_view.setText(iso_27000_item.get("name").toString());
					row.addView(text_view);
					
					text_view = new TextView(this);
					text_view.setText(iso_27000_item.get("controlTitle").toString());
					row.addView(text_view);
					
					text_view = new TextView(this);
					text_view.setText(iso_27000_item.get("dueDate").toString());
					row.addView(text_view);
					
					tbl.addView(row);
				 
				}

				 
				
			        

				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Toast.makeText(getApplicationContext(), "Laden abgeschlossen!", Toast.LENGTH_LONG).show();
		}
		
		
	}
}
