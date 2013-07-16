package sernet.verinice.nativeapp;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class NetworkTask extends AsyncTask<String, Void, HttpResponse> {
	String ip = null;
	String port = null;
	String username = null;
	String password = null;
	String address = null;

	public MainActivity activity;
	public Iso27000Tasks Iso27000Tasks_activity;
	
	public NetworkTask(MainActivity a){
		activity = a;
	}
	public NetworkTask(Iso27000Tasks a){
		Iso27000Tasks_activity = a;
	}
	
    @Override
    protected HttpResponse doInBackground(String... params) {
    	ip = params[0];
    	port = params[1];
    	username = params[2];
    	password = params[3];
    	address = params[4];
    	int port_as_int = Integer.parseInt(port);
    	
    	String link = "http://" + ip + ":" + port + "/veriniceserver/rest/json/" + address + "/get";
    	DefaultHttpClient httpclient = new DefaultHttpClient();
        
    	
    	try{  
    		httpclient.getCredentialsProvider().setCredentials(
                    new AuthScope(ip, port_as_int),
                    new UsernamePasswordCredentials(username, password));
    		
    		HttpGet httpget = new HttpGet(link);
    		System.out.println("executing request" + httpget.getRequestLine());
    		
    		HttpResponse response = httpclient.execute(httpget);
    		HttpEntity entity = response.getEntity();

             System.out.println("----------------------------------------");
             System.out.println(response.getStatusLine());
             if (entity != null) {
                 System.out.println("Response content length: " + entity.getContentLength());
             }

        
        
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
        	
        	// httpclient.getConnectionManager().shutdown();
    }
    }

    @Override
    protected void onPostExecute(HttpResponse result) {
        //Do something with result
    	String answer = null;
    	
        if (result != null){
        	try{	
        	//Read the result

        	InputStream test = result.getEntity().getContent();

        	BufferedReader r = new BufferedReader(new InputStreamReader(test));
        	StringBuilder total = new StringBuilder();
        	String line = null;
        	while ((line = r.readLine()) != null) {
        	   total.append(line);
        	}
        	answer = total.toString();
        	
        	}
        	catch (Exception e) {
        		e.printStackTrace();
        	} 
        	finally {
        		
        	}
        }
        
        //Callback with JSON String
        if (activity != null){
        	System.out.println("Main call");
        	activity.networkReady(answer);
        } else if (Iso27000Tasks_activity != null){
        	System.out.println("iso call");
        	Iso27000Tasks_activity.networkReady(answer);
        }
    } 
}
