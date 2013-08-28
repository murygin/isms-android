package sernet.verinice.nativeapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Iso27000Tasks extends Activity{
	
	String ip_address = "";
	String port = "";
	String username = "";
	String password = "";
	
	public Iso27000Tasks activity = this;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iso_27000_tasks);
       
        Button backButton = (Button) findViewById(R.id.backButton);
 
        Intent i = getIntent();
        
        // Receiving the Data 
        ip_address = i.getStringExtra("ip_address");
        port = i.getStringExtra("port");
        username = i.getStringExtra("username");
        password = i.getStringExtra("password");

        //Initialize NetworkTask to pass data back later
    	NetworkTask myTask = new NetworkTask(activity);
    	myTask.execute(ip_address, port, username, password, "iso_27000", "get", "");

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

				final ArrayList<JSONObject> json_list = new ArrayList<JSONObject>();
				final ArrayList<String> list = new ArrayList<String>();

				for(int i = 0; i < json.length(); i++) {
					iso_27000_item = json.getJSONObject(i);
			        System.out.println(iso_27000_item);

			        list.add(iso_27000_item.get("name").toString());
			        json_list.add(iso_27000_item);
					//text_view.setText(iso_27000_item.get("controlTitle").toString());
					//text_view.setText(iso_27000_item.get("dueDate").toString());
				}
				 
				ListAdapter listenAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
				final ListView listview = (ListView) findViewById(R.id.listview);
				listview.setAdapter(listenAdapter);
				
				listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						 
					public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
					 
					         // We know the View is a TextView so we can cast it
					        TextView clickedView = (TextView) view;
					        JSONObject json_object = json_list.get((int) id);
					        callNextScreen(json_object);
					     }
					});

				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Toast.makeText(getApplicationContext(), "Laden abgeschlossen!", Toast.LENGTH_LONG).show();
		}
		
		
	}
	
	
	private void callNextScreen(JSONObject item) {
		//New intent
        Intent Iso27000EditorScreen = new Intent(getApplicationContext(), Iso27000TasksEditor.class);

        //Fill with user credentials for next screen
        Iso27000EditorScreen.putExtra("ip_address", ip_address);
        Iso27000EditorScreen.putExtra("port", port);
        Iso27000EditorScreen.putExtra("username", username);
        Iso27000EditorScreen.putExtra("password", password);
        Iso27000EditorScreen.putExtra("item", item.toString());

        // Start intent and change screen
        startActivity(Iso27000EditorScreen);
		
	}
	
}
