package edu.macalester.twitter;

import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import edu.macalester.database.DbHelper;
import edu.macalester.UltimatePhonebook.R;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

/**
 * Displays a list of all tweets posted by the contact or containing the contact's screen name.
 * 
 * @author Daniel
 *
 */

public class TweetReader extends Activity {
    
	
	Bundle bundle;
	DbHelper helper;
	Long rowId;
	Cursor contact_cursor;
	String screenName;
    @Override
    

    public void onCreate(Bundle savedInstanceState) {

    	this.bundle = savedInstanceState;
		super.onCreate(bundle);

		setContentView(R.layout.tweetreader);

		
		helper = new DbHelper(this);
		   
		   
		Bundle extras = getIntent().getExtras();
		rowId = null;
			
		rowId = (bundle == null) ? null : (Long) bundle
					.getSerializable(DbHelper.KEY_ROWID);
		if (extras != null) {
			rowId = extras.getLong(DbHelper.KEY_ROWID);
		}
		
		//contact_cursor = helper.getContact(rowId);
		
		contact_cursor = helper.getTwitterInfo(rowId);
		String s = contact_cursor.getString(contact_cursor.getColumnIndex(DbHelper.KEY_TWITTER_NAME));
		screenName = s.trim();
		
      
		ArrayList<Tweet> tweets = getTweets(screenName, 1);


		ListView listView = (ListView) findViewById(R.id.ListViewId);

		listView.setAdapter(new TweetItemAdapter(this, R.layout.listitem, tweets));

    }
    public ArrayList<Tweet> getTweets(String searchTerm, int page) {
  	  String searchUrl =
  	        "http://search.twitter.com/search.json?q="
  	        + searchTerm + "&rpp=100&page=" + page;
  	   
  	  ArrayList<Tweet> tweets =
  	        new ArrayList<Tweet>();
  	   
  	  HttpClient client = new  DefaultHttpClient();
  	  HttpGet get = new HttpGet(searchUrl);
  	       
  	  ResponseHandler<String> responseHandler =
  	        new BasicResponseHandler();

  	  String responseBody = null;
  	  try {
  	    responseBody = client.execute(get, responseHandler);
  	  } catch(Exception ex) {
  	    ex.printStackTrace();
  	  }

  	  JSONObject jsonObject = null;
  	  JSONParser parser=new JSONParser();
  	   
  	  try {
  	    Object obj = parser.parse(responseBody);
  	    jsonObject=(JSONObject)obj;
  	  }catch(Exception ex){
  	    Log.v("TEST","Exception: " + ex.getMessage());
  	  }
  	   
  	  JSONArray arr = null;
  	   
  	  try {
  	    Object j = jsonObject.get("results");
  	    arr = (JSONArray)j;
  	  } catch(Exception ex){
  	    Log.v("TEST","Exception: " + ex.getMessage());
  	  }

  	  for(Object t : arr) {
  	    Tweet tweet = new Tweet(
  	      ((JSONObject)t).get("from_user").toString(),
  	      ((JSONObject)t).get("text").toString(),
  	      ((JSONObject)t).get("profile_image_url").toString()
  	    );
  	    tweets.add(tweet);
  	  }
  	   
  	  return tweets;
  	}
  

}