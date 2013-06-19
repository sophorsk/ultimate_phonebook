package edu.macalester.UltimatePhonebook;

import edu.macalester.database.DbHelper;
import java.util.List;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Provides four tabs for the user to enter a contact's information.
 * Each tab is categorized based on the information to be entered.
 * 
 * @author The Other Guys
 *
 */
public class ContactAdder extends TabActivity {

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

	/** Social networking information */
	EditText facebook;
	EditText twitter;

	/** Group information */ 
	Spinner group;
	String group_st;

	/** Resources */
	Resources res;
	TabHost tabHost;
	
	Button save_main;
	Button save_email;
	Button save_address;
	Button save_social;
	
	DbHelper helper;
	Long id;
	Cursor contact_cursor;
	List<PhoneBook> aListPhoBook;
	ArrayAdapter<CharSequence> spinner_adapter;

	/**
	 * Called when the activity is first created
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_adder);

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
		save_main = (Button) findViewById(R.id.contactSaveButton);
		save_email = (Button) findViewById(R.id.contactSaveButton1);
		save_address = (Button) findViewById(R.id.contactSaveButton2);
		save_social = (Button) findViewById(R.id.contactSaveButton3);
		
		save_main.setOnClickListener(onSave);
		save_email.setOnClickListener(onSave);
		save_address.setOnClickListener(onSave);
		save_social.setOnClickListener(onSave);

		spinner_adapter = ArrayAdapter.createFromResource(this,
				R.array.group_name_array, android.R.layout.simple_spinner_item);
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
	 * When the save button is clicked this method saves the contact's information in the
	 * database. It will not allow a save unless the name is entered 
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

			if (name_st.equals("")) {
				Toast.makeText(getBaseContext(), "You haven't entered a name.",
						Toast.LENGTH_SHORT).show();
			} else {
				id = helper.createContact(name_st, group_st, number_array,
						email_array, address_array, facebook_st, twitter_st);

				helper.newFBContact(id, facebook_st);
				helper.newTwitterContact(id, twitter_st);

				Intent addStatusIntent = new Intent();
				addStatusIntent.putExtra(DbHelper.KEY_ROWID, id);
				if (getParent() == null) {
					setResult(Activity.RESULT_OK, addStatusIntent);
				} else {
					getParent().setResult(Activity.RESULT_OK, addStatusIntent);
				}
				finish();
			}
		}
	};
}
