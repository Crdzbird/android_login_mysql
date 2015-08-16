package com.example.androidmysql1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	ListView lvMovie;
	Button btnRefresh, btnInsertMovie;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lvMovie = (ListView)findViewById(R.id.listViewMovie);
		
		btnRefresh = (Button)findViewById(R.id.btnRefresh);
				
		MovieTask mTask = new MovieTask();
		mTask.execute(new String[]{"http://10.0.2.2/movie_ticket/index.php?format=json"});
		
	}
	
	MovieArrayAdapter adapter;
	
	private class MovieTask extends AsyncTask<String, Void, Void>{

		private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
		
		private String error;
		@Override
		protected void onPreExecute() {
			dialog.setMessage("Reading Data...");
			dialog.show();
		}
		
		InputStream is1;
		String text = "";		

		ArrayList<Movie> mList;
		
		@Override
		protected Void doInBackground(String... urls) {
			for(String url: urls){				
				try {
					HttpClient client = new DefaultHttpClient();
					HttpPost post = new HttpPost(url);
					HttpResponse response = client.execute(post);
					is1 = response.getEntity().getContent();
					
				} catch (ClientProtocolException e) {
					error += "\nClientProtocolException: " + e.getMessage();
				} catch (IOException e) {
					error += "\nClientProtocolException: " + e.getMessage();
				}
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
			
			mList = new ArrayList<Movie>();
			
			JSONArray jArray;
			try {
				jArray = new JSONArray(text);
				for(int i=0;i<jArray.length();i++){
					JSONObject jsonData = jArray.getJSONObject(i);
					
					Movie m1 = new Movie();
					m1.setId(jsonData.getInt("m_id"));
					m1.setName(jsonData.getString("m_name"));
					m1.setDim(jsonData.getString("m_dim"));
					m1.setRelDate(jsonData.getString("m_reldate"));
					m1.setRate(jsonData.getString("m_rate"));					
					
					mList.add(m1);
				}
			} catch (JSONException e) {
				error = "Error Convert to JSON or Error JSON Format: " + e.getMessage();
			}
			
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if(dialog.isShowing()){
				dialog.dismiss();
			}
			
			
			adapter = new MovieArrayAdapter(
					MainActivity.this, 
					R.layout.movie_layout,
					mList
					);
			
			lvMovie.setAdapter(adapter);
			
		}
		
	}

	
	private class MovieArrayAdapter extends ArrayAdapter<Movie>{

		Context context;
		int resource;
		List<Movie> objects;
		
		public MovieArrayAdapter(Context context, int resource,
				List<Movie> objects) {
			super(context, resource, objects);
			this.context = context;
			this.resource = resource;
			this.objects = objects;
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View listItem = convertView;
			
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		    listItem = inflater.inflate(resource, parent, false);
		    TextView tvTitle = (TextView)listItem.findViewById(R.id.tvTitle); 
		    TextView tvDimension = (TextView)listItem.findViewById(R.id.tvDimension);
		    
		    Movie movie = objects.get(position);
		    tvTitle.setText(movie.getName());
		    tvDimension.setText(movie.getDim());
		      			
			return listItem;
		}		
	}

	
}
