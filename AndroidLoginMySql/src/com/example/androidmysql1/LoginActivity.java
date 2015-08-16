package com.example.androidmysql1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	EditText etUsername, etPassword;
	Button btnLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		etUsername = (EditText)findViewById(R.id.etUsername);
		etPassword = (EditText)findViewById(R.id.etPassword);
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
	}

	
	@Override
	public void onClick(View sender) {
		if(sender.getId() == R.id.btnLogin){
			LoginTask loginTask = new LoginTask();
			loginTask.execute("http://10.0.2.2/movie_ticket/login.php");	
		}
		
	}
	
	private class LoginTask extends AsyncTask<String, Void, Void>{

		ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
		
		@Override
		protected void onPreExecute() {
			dialog.setMessage("Sending Data...");
			dialog.show();
		}
		
		Boolean result = false;
		
		InputStream is1;
		String text = "";	
		private String error = "";
		
		@Override
		protected Void doInBackground(String... urls) {
			for(String url1 : urls){
				
				try {
					ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
					pairs.add(new BasicNameValuePair("txtUsername", etUsername.getText().toString()));
					pairs.add(new BasicNameValuePair("txtPassword", etPassword.getText().toString()));
					pairs.add(new BasicNameValuePair("mobile", "android"));
					HttpClient client = new DefaultHttpClient();
					HttpPost post = new HttpPost(url1);
					post.setEntity(new UrlEncodedFormEntity(pairs));					
					HttpResponse response = client.execute(post);
					is1 = response.getEntity().getContent();
					
					result = true;
					
				} catch (ClientProtocolException e) {
					error += "\nClientProtocolException: " + e.getMessage();
				} catch (IOException e) {
					error += "\nClientProtocolException: " + e.getMessage();
				}
				
				BufferedReader reader;
				
				try {
					reader = new BufferedReader(new InputStreamReader(is1 ,"iso-8859-1"), 8);
					String line = null;
					
					while ((line = reader.readLine()) != null) {
						text += line + "\n";
					}
				} catch (UnsupportedEncodingException e) {
					error += "\nClientProtocolException: " + e.getMessage();
				} catch (IOException e) {
					error += "\nClientProtocolException: " + e.getMessage();
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void arg0) {
			if(dialog.isShowing()){
				dialog.dismiss();
			}
			
			text = text.trim();
			
			Toast.makeText(LoginActivity.this, text, Toast.LENGTH_LONG).show();
						
			if(text.equals("login_success")){
				Intent in = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(in);
			}
			else{
				Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();			
			}
						
		}
		
	}

}
