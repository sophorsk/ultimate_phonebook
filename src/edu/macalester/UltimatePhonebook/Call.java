package edu.macalester.UltimatePhonebook;

import edu.macalester.database.DbHelper;
import java.util.LinkedList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Makes a phonecall to the contact whose contact pane the user was in. 
 * The user is given options on the number to call in cases where the
 * contact has more than one number saved in the database. Otherwise the 
 * user is prompted to enter a number
 * 
 * @author The Other Guys
 *
 */
public class Call extends Activity{
	
	Cursor contact_cursor;
	Long rowId;
	DbHelper helper;
	Bundle bundle;
	ContactPane pane;
	TextView name;
	String fname;
	String number;
	CharSequence[] numbers = new CharSequence[3];
	LinkedList<CharSequence> all_numbers = new LinkedList<CharSequence>();
	
	@Override
	public void onCreate(Bundle bundle) {
		this.bundle = bundle;
		super.onCreate(bundle);
		
		helper = new DbHelper(this);
		pane = new ContactPane();
		
		Bundle extras = getIntent().getExtras();
		rowId = null;
		
		rowId = (bundle == null) ? null : (Long) bundle
				.getSerializable(DbHelper.KEY_ROWID);
		
		if (extras != null) {
			rowId = extras.getLong(DbHelper.KEY_ROWID);			
		}

		all_numbers = getNumbers();
		
		numberStatus();
		
	}
	
	/**
	 * Checks how many numbers the contact has:
	 * 		Asks the user for a number if there are no saved numbers
	 * 		Calls the number given if there is only one number
	 * 		Allows the user to choose a number if there are two or more numbers
	 */
	private void numberStatus() {
		switch(all_numbers.size()){
		case 0: 
			askForNumbers(); break;
		case 1: 
			number = (String) all_numbers.get(0); 
			phonecall(number);
			break;
		case 2:
			showOptions(); break;
		default :
			showOptions(); break;
		}
	}
	
	/**
	 * Displays an alert dialogue asking the user if they would like to add a 
	 * number for a given contact
	 * 		Goes to the contact editing screen if the user chooses to add a number
	 * 		Cancels and goes back to the contact pane otherwise
	 */
	private void askForNumbers() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("This contact has no number. " +
    			"Would you like to add one?");
    	
    	builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
           @Override
		public void onClick(DialogInterface dialog, int id) {
        	   Intent add_number = new Intent();
        	   add_number.setClass(Call.this, ContactEditor.class);
        	   add_number.putExtra(DbHelper.KEY_ROWID, rowId);
        	   add_number.putExtra(Code.REQUEST_CODE, Code.ADD_NUMBER);
        	   startActivityForResult(add_number, Code.ADD_NUMBER);
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
	
	/**
	 * Searches the database for number/s saved for the contact with id = rowId
	 * 
	 * @return LinkedList<CharSequence> with all the numbers saved for the contact
	 */
	private LinkedList<CharSequence> getNumbers() {
		LinkedList<CharSequence> nums = new LinkedList<CharSequence>();
		Cursor num_cursor = helper.getAllNumbers(rowId);
		startManagingCursor(num_cursor);
		if(num_cursor.moveToFirst()){
    		do{
    			String mobile_0 = num_cursor.getString(num_cursor.getColumnIndexOrThrow(DbHelper.KEY_HOMENUMBER));
    			String mobile_1 = num_cursor.getString(num_cursor.getColumnIndexOrThrow(DbHelper.KEY_MOBILE_1));
    			String mobile_2 = num_cursor.getString(num_cursor.getColumnIndexOrThrow(DbHelper.KEY_MOBILE_2));
    			String[] mobile = {mobile_0, mobile_1, mobile_2};
    			for(int i = 0; i < mobile.length; i++){
    				if(mobile[i].length() != 0){
    					nums.add(mobile[i]);
    				}
    			}
    			
    		}while(num_cursor.moveToNext());
    	}
		return nums;
	}
	
	/**
	 * Shows an alert dialogue with the number/s saved for the contact. It allows the 
	 * user to choose the number they would like to call and calls the method phonecall 
	 * passing it the number as a parameter.
	 */
	private void showOptions() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Choose Number");
    	
    	final CharSequence[] mobile_nums = toCharSequence();  	
    	builder.setSingleChoiceItems(mobile_nums, -1, new DialogInterface.OnClickListener() {
    	    @Override
			public void onClick(DialogInterface dialog, int num) {
    	        number = (String) mobile_nums[num];
    	    }
    	});
    	builder.setCancelable(false);
    	builder.setPositiveButton("Choose", new DialogInterface.OnClickListener() {
           @Override
		public void onClick(DialogInterface dialog, int id) {
        	   phonecall(number);
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
	
	/**
	 * Converts the strings of numbers into character sequences 
	 *  
	 * @return an array of character sequences of the number associated with the contact 
	 */
	private CharSequence[] toCharSequence() {
		CharSequence[] seq = new CharSequence[all_numbers.size()];
		int i = 0;
		for(CharSequence val : all_numbers){
			seq[i] = val;
			i++;
		}
		return seq;
	}

	/**
	 * Makes a phone call to a number
	 * 
	 * @param num - the number being called
	 */
	private void phonecall(String num) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + num));
            startActivity(callIntent);
        } catch (ActivityNotFoundException e) {
            Log.e("", "Call failed", e);
        }
    }
	
	/**
	 * Recreates the activity after numbers have been changed from the database. 
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if((requestCode == Code.ADD_NUMBER) && (resultCode == RESULT_OK)){
			onCreate(bundle);
		}
	}
}
