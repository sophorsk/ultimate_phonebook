package edu.macalester.twitter;

import java.util.Date;

import oauth.signpost.OAuth;
import edu.macalester.database.DbHelper;
import edu.macalester.UltimatePhonebook.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Opens a window for user to post a tweet with the contact tagged in it.  If the user has already authenticated Twitter for this app,
 * the tweet will be automatically posted when they click "Tweet".  If not, they will be asked to authenticate when they hit the "Tweet"
 * button.  The user can also clear their Twitter credentials from this screen. 
 * 
 * @author Daniel
 *
 */
public class TwitterStatus extends Activity{

	Bundle bundle;
	
	String screenName = "UltPhonebook";
	String twitterStatus;
	EditText tweetEditText;
	DbHelper helper;
	Long rowId;
	Cursor contact_cursor;
	
	private SharedPreferences prefs;
	private final Handler mTwitterHandler = new Handler();
	private TextView loginStatus;
	
    final Runnable mUpdateTwitterNotification = new Runnable() {
        @Override
		public void run() {
        	//Toast.makeText(getBaseContext(), "Tweet sent !", Toast.LENGTH_LONG).show();
        }
    };
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		this.bundle = savedInstanceState;
		super.onCreate(bundle);
        setContentView(R.layout.twitterstatus);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

        loginStatus = (TextView)findViewById(R.id.login_status);
        Button tweet = (Button) findViewById(R.id.btn_tweet);
        Button clearCredentials = (Button) findViewById(R.id.btn_clear_credentials);
        Button cancel = (Button) findViewById(R.id.btn_cancel_twitter_status);
        
        
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
		
        TextView t = (TextView)findViewById(R.id.tweet_at);
        t.setText(screenName +" will be automatically tagged in your tweet:");
        
        tweetEditText = (EditText) findViewById(R.id.TwitterMessageText);
        
        tweet.setOnClickListener(new View.OnClickListener() {
        	/*
        	 * Send a tweet. If the user hasn't authenticated to Tweeter yet, they'll be redirected via a browser
        	 * to the twitter login page. Once the user authenticated, they'll authorize the Android application to send
        	 * tweets on their behalf.
        	 */
            @Override
			public void onClick(View v) {
            	String status = tweetEditText.getText().toString();
                twitterStatus = ("@" + screenName + " " + status);
            	if (TwitterUtils.isAuthenticated(prefs)) {
            		sendTweet();
            		tweetEditText.setText("");
            		Toast.makeText(TwitterStatus.this, 
                            "You tweeted "+twitterStatus, 
                            Toast.LENGTH_LONG).show();
            	} else {
    				Intent i = new Intent(getApplicationContext(), PrepareRequestTokenActivity.class);
    				i.putExtra("tweet_msg",twitterStatus);
    				startActivity(i);
    				
            	}
            }
        });

        clearCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	clearCredentials();
            	updateLoginStatus();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	finish();
            }
        });    
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updateLoginStatus();
	}
	
	public void updateLoginStatus() {
		loginStatus.setText("Logged into Twitter : " + TwitterUtils.isAuthenticated(prefs));
	}
	

	private String getTweetMsg() {
		return "Tweeting from Android App at " + new Date().toLocaleString();
	}	
	
	public void sendTweet() {
		Thread t = new Thread() {
	        @Override
			public void run() {
	        	
	        	try {
	        		TwitterUtils.sendTweet(prefs,twitterStatus);
	        		mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
	        }

	    };
	    t.start();
	}

	private void clearCredentials() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final Editor edit = prefs.edit();
		edit.remove(OAuth.OAUTH_TOKEN);
		edit.remove(OAuth.OAUTH_TOKEN_SECRET);
		edit.commit();
	}
}