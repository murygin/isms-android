package sernet.verinice.nativeapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Iso27000TasksEditor extends Activity {
	
	String ip_address = "";
	String port = "";
	String username = "";
	String password = "";
	String json_string = "";
	JSONObject active_item;
	
	public Iso27000TasksEditor activity = this;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iso_27000_tasks_editor);
 
        // Receiving the Data
        Intent i = getIntent();
        ip_address = i.getStringExtra("ip_address");
        port = i.getStringExtra("port");
        username = i.getStringExtra("username");
        password = i.getStringExtra("password");
        json_string = i.getStringExtra("item");
        
        final EditText active_item_title = (EditText) findViewById(R.id.active_item_title);
        final EditText active_item_control_title = (EditText) findViewById(R.id.active_item_control_title);
        final EditText active_item_due_date = (EditText) findViewById(R.id.active_item_due_date);

        //Get JSON Object and fill the Edit-fields
        try {
        	active_item = new JSONObject(json_string);

	        active_item_title.setText(active_item.get("name").toString());
	        active_item_control_title.setText(active_item.get("controlTitle").toString());
	        active_item_due_date.setText(active_item.get("dueDate").toString());
  
        } catch (JSONException e) {
			e.printStackTrace();
		}
        
        //Get Buttons
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        // Binding Click event to cancelButton
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //Closing SecondScreen Activity
                finish();
            }
        });
        // Binding Click event to saveButton
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	//Write new data in JSON String
            	try {
            		System.out.println("SaveButton pressed");
					active_item.put("name", active_item_title.getText());
					active_item.put("controlTitle", active_item_control_title.getText());
					active_item.put("dueDate", active_item_due_date.getText());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
          	
            	createJsonString();
            }

			
        });
    }
	
	private void createJsonString() {
		//Initialize NetworkTask to pass data back later
		System.out.println("Network call");
    	NetworkTask myTask = new NetworkTask(activity);
    	myTask.execute(ip_address, port, username, password, "iso_27000", "post", active_item.toString());
    	
    	
    	
	}
	
	private void callNextScreen() {

	}
	
	public void networkReady(String data){
		
	}
}
