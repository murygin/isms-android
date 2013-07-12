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

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Base64;

public class NetworkTask extends AsyncTask<String, Void, HttpResponse> {
    @Override
    protected HttpResponse doInBackground(String... params) {
    	String ip = params[0];
    	String port = params[1];
    	String username = params[2];
    	String password = params[3];
    	String address = params[4];
    	int port_as_int = Integer.parseInt(port);
    	
    	String link = "http://" + ip + ":" + port + "/veriniceserver/rest/json/" + address + "/get";
    	StringBuilder builder = new StringBuilder();
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
        	httpclient.getConnectionManager().shutdown();
    }
    }

    @Override
    protected void onPostExecute(HttpResponse result) {
        //Do something with result
        if (result != null){
        	try{
        	BufferedReader r = new BufferedReader(new InputStreamReader(result.getEntity().getContent()));

        	StringBuilder total = new StringBuilder();

        	String line = null;

        	while ((line = r.readLine()) != null) {
        	   total.append(line);
        	}
   
        	System.out.println(total);
        	}
        	catch (Exception e) {
        		e.printStackTrace();
        	}
        }
           
    }
}
