package sernet.verinice.nativeapp;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
	String type = null;
	String post_params = null;
	int port_as_int;

	public MainActivity activity;
	public Iso27000Tasks Iso27000Tasks_activity;
	public Iso27000TasksEditor Iso27000TasksEditor_activity;
	
	public NetworkTask(MainActivity a){
		activity = a;
	}
	public NetworkTask(Iso27000Tasks a){
		Iso27000Tasks_activity = a;
	}
	public NetworkTask(Iso27000TasksEditor a){
		Iso27000TasksEditor_activity = a;
	}
	
    @Override
    protected HttpResponse doInBackground(String... params) {
    	
    	ip = params[0];
    	port = params[1];
    	username = params[2];
    	password = params[3];
    	address = params[4];
    	type = params[5];
    	post_params = params[6];
    	port_as_int = Integer.parseInt(port);
    	HttpResponse response = null;
    	
    	String link = "http://" + ip + ":" + port + "/veriniceserver/rest/json/" + address + "/" + type;
    	
    	if (type == "get"){

	    	
	    	DefaultHttpClient httpclient = new DefaultHttpClient();
	        
	    	
	    	try{  
	    		httpclient.getCredentialsProvider().setCredentials(
	                    new AuthScope(ip, port_as_int),
	                    new UsernamePasswordCredentials(username, password));
	    		
	    		HttpGet httpget = new HttpGet(link);
	    		System.out.println("executing request" + httpget.getRequestLine());
	    		
	    		response = httpclient.execute(httpget);
	    		HttpEntity entity = response.getEntity();
	
	             System.out.println("----------------------------------------");
	             System.out.println(response.getStatusLine());
	             if (entity != null) {
	                 System.out.println("Response content length: " + entity.getContentLength());
	             }
	
	        
	        
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        } finally {
	        	
	        	// httpclient.getConnectionManager().shutdown();
	    }
    	} else if (type == "post"){
    		
    		try {
    			 
    			System.out.println("HTTP Post call");
	    		DefaultHttpClient httpClient = new DefaultHttpClient();
	    		httpClient.getCredentialsProvider().setCredentials(
	                    new AuthScope(ip, port_as_int),
	                    new UsernamePasswordCredentials(username, password));
	            HttpPost httppost = new HttpPost(link);
	            
	            //Add data body to post request
	            httppost.addHeader("Content-Type", "application/json; charset=utf-8"); 
	            httppost.setEntity(new StringEntity(post_params,"utf-8"));

	            response = httpClient.execute(httppost);
            } catch (Exception e) {
				e.printStackTrace();
			}	
    	}
    	
    	return response;
    	
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
        } else if (Iso27000TasksEditor_activity != null){
        	System.out.println("iso editor call");
        	Iso27000TasksEditor_activity.networkReady(answer);
        }
    } 
}
