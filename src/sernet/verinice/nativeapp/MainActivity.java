package sernet.verinice.nativeapp;

import java.io.*;
import java.net.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	
	String ip_address = "";
	String port = "";
	String username = "";
	String password = "";
	public MainActivity activity = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		
		//Button and textarea definitions
		final Button login_button = (Button) findViewById(R.id.btnLogin);
		final EditText ip_address_field = (EditText) findViewById(R.id.ip_address_textfield);
		final EditText port_field = (EditText) findViewById(R.id.port_textfield);
		final EditText username_field = (EditText) findViewById(R.id.username_textfield);
		final EditText password_field = (EditText) findViewById(R.id.password_textfield);
		
		//Default values
		ip_address_field.setText("192.168.2.104");
		port_field.setText("8080");
		
		login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	ip_address = ip_address_field.getText().toString();
            	port = port_field.getText().toString();
            	username = username_field.getText().toString();
            	password = password_field.getText().toString();
            			
            	System.out.println(ip_address + port + username + password);
            	
            	//Initialize NetworkTask to pass data back later
            	NetworkTask myTask = new NetworkTask(activity);
            	myTask.execute(ip_address, port, username, password, "auth", "get", "");
            	  	
            }
        });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	public void networkReady(String data){
		
		//If data is null - due to an exception in the network task
		if (data == null){
			Toast.makeText(getApplicationContext(), "Error! - Bitte Verbindungseinstellungen prüfen!", Toast.LENGTH_LONG).show();
		}
		// If Authentication was successful
		else if (data.equals("{\"Access\":\"granted\"}")){
    		Toast.makeText(getApplicationContext(), "Authentifizierung erfolgreich!", Toast.LENGTH_LONG).show();
    		
    		callNextScreen();
    	} 
		//If 401 Code is returned
		else if (data.contains("This request requires HTTP authentication")){
			Toast.makeText(getApplicationContext(), "Bitte die Login-Daten überprüfen!", Toast.LENGTH_LONG).show();
    	}
		else {
			
			Toast.makeText(getApplicationContext(), "Different", Toast.LENGTH_LONG).show();
		}
		
		
	}

	private void callNextScreen() {
		//New intent
        Intent Iso27000Screen = new Intent(getApplicationContext(), Iso27000Tasks.class);

        //Fill with user credentials for next screen
        Iso27000Screen.putExtra("ip_address", ip_address);
        Iso27000Screen.putExtra("port", port);
        Iso27000Screen.putExtra("username", username);
        Iso27000Screen.putExtra("password", password);

        // Start intent and change screen
        startActivity(Iso27000Screen);
		
	}
}
