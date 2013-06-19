package edu.macalester.UltimatePhonebook;

import edu.macalester.database.DbHelper;
import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView.OnItemSelectedListener;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView;
import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;

/**
 * This activity allows a user to edit a contact that has already 
 * been saved to the database. 
 * 
 * @author The Other Guys
 *
 */
public class ContactEditor extends TabActivity {

	/** Personal Information */
	EditText name;

	/** Phone Number Information */
	String mobile = "true";
	String not_mobile = "false";
	EditText mobile_personal;
	EditText mobile_work;
	EditText other_home;

	/** Email Information */
	EditText email_1;
	EditText email_2;
	EditText email_3;
	EditText email_4;

	/** Address Information */
	EditText street_1;
	EditText street_2;
	EditText city;
	EditText state;
	EditText country;
	EditText zip;

	/** Group information */
	Spinner group;
	String group_st;

	/** Social networking information */
	EditText facebook;
	EditText twitter;

	/** Resources */
	Resources res;
	TabHost tabHost;
	
	Button save_main;
	Button save_email;
	Button save_address;
	Button save_social;
	
	DbHelper helper;
	List<PhoneBook> aListPhoBook;
	ArrayAdapter<CharSequence> spinner_adapter;
	Integer requestCode;

	Long rowId;
	Bundle bundle;
	Cursor contact_cursor;

	/**
	 * Called when the activity is first created
	 */
	@Override
	public void onCreate(Bundle bundle) {
		this.bundle = bundle;
		super.onCreate(bundle);
		setContentView(R.layout.contact_edit);

		helper = new DbHelper(this);

		tabHost = getTabHost();
		res = getResources();

		tabHost.addTab(tabHost
				.newTabSpec("personl_info")
				.setIndicator("Personal Info",
						res.getDrawable(R.drawable.ic_tab_phone))
				.setContent(R.id.layout_tab_one));
		tabHost.addTab(tabHost
				.newTabSpec("email")
				.setIndicator("Email", res.getDrawable(R.drawable.ic_tab_email))
				.setContent(R.id.layout_tab_two));
		tabHost.addTab(tabHost
				.newTabSpec("address")
				.setIndicator("Address",
						res.getDrawable(R.drawable.ic_tab_home))
				.setContent(R.id.layout_tab_three));
		tabHost.addTab(tabHost
				.newTabSpec("social_site")
				.setIndicator("Social",
						res.getDrawable(R.drawable.ic_tab_social))
				.setContent(R.id.layout_tab_four));

		tabHost.setCurrentTab(0);

		// Personal Information
		name = (EditText) findViewById(R.id.full_name_et);

		// Phone Number Information
		mobile_personal = (EditText) findViewById(R.id.mobile_personal_et);
		mobile_work = (EditText) findViewById(R.id.mobile_work_et);
		other_home = (EditText) findViewById(R.id.other_home_et);

		// Email Information
		email_1 = (EditText) findViewById(R.id.first_email_et);
		email_2 = (EditText) findViewById(R.id.second_email_et);
		email_3 = (EditText) findViewById(R.id.third_email_et);
		email_4 = (EditText) findViewById(R.id.fourth_email_et);

		// Address Information
		street_1 = (EditText) findViewById(R.id.first_street_et);
		street_2 = (EditText) findViewById(R.id.second_street_et);
		city = (EditText) findViewById(R.id.city_et);
		state = (EditText) findViewById(R.id.state_et);
		country = (EditText) findViewById(R.id.country_et);
		zip = (EditText) findViewById(R.id.zip_et);

		// Social Network Information
		facebook = (EditText) findViewById(R.id.facebookID);
		twitter = (EditText) findViewById(R.id.twitterUsername);

		group = (Spinner) findViewById(R.id.group_spinner);
		save_main = (Button) findViewById(R.id.contactEditButton);
		save_email = (Button) findViewById(R.id.contactEditButton1);
		save_address = (Button) findViewById(R.id.contactEditButton2);
		save_social = (Button) findViewById(R.id.contactEditButton3);

		// Get the contact id
		Bundle extras = getIntent().getExtras();
		rowId = null;
		requestCode = -1;

		rowId = (bundle == null) ? null : (Long) bundle
				.getSerializable(DbHelper.KEY_ROWID);
		requestCode = (bundle == null) ? null : (Integer) bundle
				.getSerializable(Code.REQUEST_CODE);

		if (extras != null) {
			rowId = extras.getLong(DbHelper.KEY_ROWID);
			requestCode = extras.getInt(Code.REQUEST_CODE);
		}

		// Adding current contact Information so it can be edited
		contact_cursor = helper.getContact(rowId);

		name.setText(contact_cursor.getString(contact_cursor
				.getColumnIndex(DbHelper.KEY_NAME)));

		// Adding Current Phone Number Information
		mobile_personal.setText(contact_cursor.getString(contact_cursor
				.getColumnIndex(DbHelper.KEY_MOBILE_1)));
		mobile_work.setText(contact_cursor.getString(contact_cursor
				.getColumnIndex(DbHelper.KEY_MOBILE_2)));
		other_home.setText(contact_cursor.getString(contact_cursor
				.getColumnIndex(DbHelper.KEY_HOMENUMBER)));

		// Adding Current Email Information
		email_1.setText(contact_cursor.getString(contact_cursor
				.getColumnIndex(DbHelper.KEY_EMAIL_1)));
		email_2.setText(contact_cursor.getString(contact_cursor
				.getColumnIndex(DbHelper.KEY_EMAIL_2)));
		email_3.setText(contact_cursor.getString(contact_cursor
				.getColumnIndex(DbHelper.KEY_EMAIL_3)));
		email_4.setText(contact_cursor.getString(contact_cursor
				.getColumnIndex(DbHelper.KEY_EMAIL_4)));

		// Adding Current Address Information
		street_1.setText(contact_cursor.getString(contact_cursor
				.getColumnIndex(DbHelper.KEY_STREET_1)));
		street_2.setText(contact_cursor.getString(contact_cursor
				.getColumnIndex(DbHelper.KEY_STREET_2)));
		city.setText(contact_cursor.getString(contact_cursor
				.getColumnIndex(DbHelper.KEY_CITY)));
		state.setText(contact_cursor.getString(contact_cursor
				.getColumnIndex(DbHelper.KEY_STATE)));
		country.setText(contact_cursor.getString(contact_cursor
				.getColumnIndex(DbHelper.KEY_COUNTRY)));
		zip.setText(contact_cursor.getString(contact_cursor
				.getColumnIndex(DbHelper.KEY_ZIP)));

		// Adding Current Group Information
		group_st = contact_cursor.getString(contact_cursor
				.getColumnIndex(DbHelper.KEY_GROUP));

		// Adding Current Current Social Network Information
		facebook.setText(contact_cursor.getString(contact_cursor
				.getColumnIndex(DbHelper.KEY_FACEBOOKID)));
		twitter.setText(contact_cursor.getString(contact_cursor
				.getColumnIndex(DbHelper.KEY_TWITTER_NAME)));

		displayGroup();

		group.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				group_st = (String) parentView.getItemAtPosition(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}

		});

		save_main.setOnClickListener(onSave);
		save_email.setOnClickListener(onSave);
		save_address.setOnClickListener(onSave);
		save_social.setOnClickListener(onSave);
	}

	/**
	 * When the save button is clicked this method saves the contact's information in the
	 * database. There are a number of conditions that have to be met when editing a contact:
	 * 		If the reason you are editing the contact is because you wanted to call a person, 
	 * 			then you will not be allowed to save changes if you still haven't entered any
	 * 			number. 
	 * 		The same applies with email address, postal address, facebook id and twitter username  
	 */
	private View.OnClickListener onSave = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			String name_st = name.getText().toString();

			String[] number_array = { mobile_personal.getText().toString(),
					mobile_work.getText().toString(),
					other_home.getText().toString() };

			String[] email_array = { email_1.getText().toString(),
					email_2.getText().toString(), email_3.getText().toString(),
					email_4.getText().toString() };

			String[] address_array = { street_1.getText().toString(),
					street_2.getText().toString(), city.getText().toString(),
					state.getText().toString(), country.getText().toString(),
					zip.getText().toString() };

			String facebook_st = facebook.getText().toString();
			String twitter_st = twitter.getText().toString();

			helper.updateContact(rowId, name_st, group_st, number_array,
					email_array, address_array, facebook_st, twitter_st);

			Intent editStatusIntent = new Intent();
			editStatusIntent.putExtra(DbHelper.KEY_ROWID, rowId);

			if (requestCode == Code.CONTACT_EDIT) {
				if (name_st.equals("")) {
					Toast.makeText(getBaseContext(), "You haven't entered a name.",
							Toast.LENGTH_SHORT).show();
				} else {
					
					if (getParent() == null) {
						setResult(Activity.RESULT_OK, editStatusIntent);
					} else {
						getParent().setResult(Activity.RESULT_OK, editStatusIntent);
					}
					finish();
				}
				
			} else if (requestCode == Code.ADD_NUMBER) {
				String mobile_1 = mobile_personal.getText().toString();
				String mobile_2 = mobile_work.getText().toString();

				if ((mobile_1.length() == 0) && (mobile_2.length() == 0)) {
					Toast.makeText(getBaseContext(),
							"You still haven't added a number :(",
							Toast.LENGTH_SHORT).show();
					onCreate(bundle);
				} else {
					if (getParent() == null) {
						setResult(Activity.RESULT_OK, editStatusIntent);
					} else {
						getParent().setResult(Activity.RESULT_OK,
								editStatusIntent);
					}
					finish();
				}

			} else if (requestCode == Code.ADD_EMAIL) {
				String email1 = email_1.getText().toString();
				String email2 = email_2.getText().toString();
				String email3 = email_3.getText().toString();
				String email4 = email_4.getText().toString();

				if ((email1.length() == 0) && (email2.length() == 0)
						&& (email3.length() == 0) && email4.length() == 0) {
					Toast.makeText(getBaseContext(),
							"You still haven't added an email account :(",
							Toast.LENGTH_SHORT).show();
					onCreate(bundle);
				} else {
					if (getParent() == null) {
						setResult(Activity.RESULT_OK, editStatusIntent);
					} else {
						getParent().setResult(Activity.RESULT_OK,
								editStatusIntent);
					}
					finish();
				}

			} else if (requestCode == Code.ADD_ADDRESS) {
				String address1 = street_1.getText().toString();
				String address2 = street_2.getText().toString();
				String address3 = city.getText().toString();
				String address4 = state.getText().toString();
				String address5 = country.getText().toString();
				String address6 = zip.getText().toString();

				if ((address1.length() == 0 && address2.length() == 0
						&& address3.length() == 0 && address4.length() == 0 && address5
						.length() == 0)) {
					Toast.makeText(getBaseContext(),
							"You still haven't added an address :(",
							Toast.LENGTH_SHORT).show();
					onCreate(bundle);
				} else {
					if (getParent() == null) {
						setResult(Activity.RESULT_OK, editStatusIntent);
					} else {
						getParent().setResult(Activity.RESULT_OK,
								editStatusIntent);
					}
					finish();
				}

			} else if (requestCode == Code.ADD_FACEBOOK) {
				String facebook_id = facebook.getText().toString();

				if ((facebook_id.length() == 0)) {
					Toast.makeText(getBaseContext(),
							"You still haven't added a facebook account :(",
							Toast.LENGTH_SHORT).show();
					onCreate(bundle);
				} else {
					if (getParent() == null) {
						setResult(Activity.RESULT_OK, editStatusIntent);
					} else {
						getParent().setResult(Activity.RESULT_OK,
								editStatusIntent);
					}
					finish();
				}

			}else if (requestCode == Code.ADD_TWITTER) {
				String facebook_id = twitter.getText().toString();

				if ((facebook_id.length() == 0)) {
					Toast.makeText(getBaseContext(),
							"You still haven't added a Twitter account :(",
							Toast.LENGTH_SHORT).show();
					onCreate(bundle);
				} else {
					if (getParent() == null) {
						setResult(Activity.RESULT_OK, editStatusIntent);
					} else {
						getParent().setResult(Activity.RESULT_OK,
								editStatusIntent);
					}
					finish();
				}

			} else {
				finish();
			}
		}
	};

	/**
	 * Populates the group spinner from an array adapter, displaying the contact's 
	 * current group
	 */
	private void displayGroup() {
		ArrayList<CharSequence> init_group_list = new ArrayList<CharSequence>();

		init_group_list.add("Acquaintance");
		init_group_list.add("Close Friends");
		init_group_list.add("Family");

		spinner_adapter = new ArrayAdapter<CharSequence>(this,
				android.R.layout.simple_spinner_item, init_group_list);
		Cursor group_cursor = helper.getGroups();
		startManagingCursor(group_cursor);

		if (group_cursor.moveToFirst()) {
			do {
				if (group_cursor.getString(
						group_cursor.getColumnIndex(DbHelper.KEY_GROUP))
						.equals(group_st)) {

				} else {
					spinner_adapter.add(group_cursor.getString(group_cursor
							.getColumnIndex(DbHelper.KEY_GROUP)));
				}
			} while (group_cursor.moveToNext());
		}
		spinner_adapter.sort(new StringComparator());
		remove();
		spinner_adapter.insert(group_st, 0);
		removeDuplicates();
		spinner_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		group.setAdapter(spinner_adapter);

		group.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				group_st = (String) parentView.getItemAtPosition(position);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}

		});
	}

	/**
	 * Removes a group name from the array adapter
	 */
	private void remove() {
		int j = 0;
		while (j < spinner_adapter.getCount()) {
			if(group_st.equals(spinner_adapter.getItem(j))){
				spinner_adapter.remove(spinner_adapter.getItem(j));
			} else {
				j++;
			}
		}
		spinner_adapter.notifyDataSetChanged();
	}

	/**
	 * Removes duplicates from the array adapter
	 */
	private void removeDuplicates() {
		int i = 0;
		int j = i + 1;
		while (j < spinner_adapter.getCount()) {
			if (spinner_adapter.getItem(i).equals(spinner_adapter.getItem(j))) {
				spinner_adapter.remove(spinner_adapter.getItem(j));
			} else {
				i++;
				j++;
			}
		}
		spinner_adapter.notifyDataSetChanged();
	}
}