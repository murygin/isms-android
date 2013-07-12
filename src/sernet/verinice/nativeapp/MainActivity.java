package sernet.verinice.nativeapp;

import java.io.*;
import java.net.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	String ip_address = "";
	String port = "";
	String username = "";
	String password = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		final Button login_button = (Button) findViewById(R.id.btnLogin);
		final EditText ip_address_field = (EditText) findViewById(R.id.ip_address_textfield);
		final EditText port_field = (EditText) findViewById(R.id.port_textfield);
		final EditText username_field = (EditText) findViewById(R.id.username_textfield);
		final EditText password_field = (EditText) findViewById(R.id.password_textfield);
		ip_address_field.setText("192.168.2.104");
		port_field.setText("8080");
		
		login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	ip_address = ip_address_field.getText().toString();
            	port = port_field.getText().toString();
            	username = username_field.getText().toString();
            	password = password_field.getText().toString();
            			
            	System.out.println(ip_address + port + username + password);
            	new NetworkTask().execute(ip_address, port, username, password, "auth");
            }
        });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

}
