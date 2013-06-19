package edu.macalester.twitter;


import edu.macalester.database.DbHelper;
import edu.macalester.UltimatePhonebook.Code;
import edu.macalester.UltimatePhonebook.ContactEditor;
import edu.macalester.UltimatePhonebook.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/** 
 * This class opens a menu displaying options for interacting with Twitter.  User is given option to view tweets, tag the contact
 * in a tweet, or cancel and return to the contact pane.  If the contact has no twitter info entered, the user will be prompted to 
 * add it.
 * 
 * @author Daniel
 *
 */
public class Twitter extends Activity {
    
	
	Bundle bundle;
	DbHelper helper;
	Long rowId;
	Cursor contact_cursor;
	String twitterName;
	
	
	Button addAccount;
	Button viewTweets;
	Button tweetAt;
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
	   this.bundle = savedInstanceState;
	   super.onCreate(bundle);
	   setContentView(R.layout.twitter);
	   
	   helper = new DbHelper(this);
	   
	   
	   Bundle extras = getIntent().getExtras();
		rowId = null;
		
		rowId = (bundle == null) ? null : (Long) bundle
				.getSerializable(DbHelper.KEY_ROWID);
		if (extras != null) {
			rowId = extras.getLong(DbHelper.KEY_ROWID);
		}
		
		contact_cursor = helper.getTwitterInfo(rowId);
		twitterName = contact_cursor.getString(contact_cursor.getColumnIndex(DbHelper.KEY_TWITTER_NAME));
		
		
		
	   //checks for twitter account, prompts user to add one if none exists
		if (twitterName.equals("")) {
			askForTwitter();
		}
		
		
	   addAccount = (Button) findViewById(R.id.add_twitter);
	   tweetAt = (Button) findViewById(R.id.update_twitter);
	   viewTweets = (Button) findViewById(R.id.view_twitter);
	     
	     
	   addAccount.setOnClickListener(onClick);
	   tweetAt.setOnClickListener(onClick);
	   viewTweets.setOnClickListener(onClick);
   }
   private View.OnClickListener onClick = new View.OnClickListener() {
 		@Override
		public void onClick(View v) {
 						
 			switch(v.getId()){
 				
 				
 			case R.id.update_twitter:
 				Intent tweetAt = new Intent();
 				tweetAt.setClass(Twitter.this, TwitterStatus.class);
 				tweetAt.putExtra(DbHelper.KEY_ROWID, rowId);
 		        startActivity(tweetAt);
 		        break;
 			        
 			case R.id.view_twitter:
 				Intent viewTweets = new Intent();
 				viewTweets.setClass(Twitter.this, TweetReader.class);
 				viewTweets.putExtra(DbHelper.KEY_ROWID, rowId);
 		        startActivity(viewTweets);
 		        break;
 			      
 		        
 			case R.id.add_twitter:
 				finish();
 				break;    
 			}
 		}
    };
    /**
     * Opens a dialog for the user to decide whether to add a twitter account
     * If user selects "Add", the contact editor for the user to add a Twitter account
     */
	private void askForTwitter() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("This contact has no Twitter. " +
    			"Would you like to add one?");
    	
    	builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
           @Override
		public void onClick(DialogInterface dialog, int id) {
        	   Intent add_twitter = new Intent();
        	   add_twitter.setClass(Twitter.this, ContactEditor.class);
        	   add_twitter.putExtra(DbHelper.KEY_ROWID, rowId);
        	   add_twitter.putExtra(Code.REQUEST_CODE, Code.ADD_TWITTER);
        	   startActivityForResult(add_twitter, Code.ADD_TWITTER);
           }
       }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           @Override
		public void onClick(DialogInterface dialog, int id) {
               dialog.cancel();
               finish();
          }
      });
    	AlertDialog alert = builder.create();
    	alert.show();
	}
}
