package edu.macalester.UltimatePhonebook;

import edu.macalester.database.DbHelper;
import edu.macalester.map.Directions;
import edu.macalester.twitter.Twitter;
import edu.macalester.facebook.*;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ContactPane extends Activity {

	DbHelper helper;
	ImageButton call;
	ImageButton text;
	ImageButton delete;
	ImageButton edit;
	ImageButton mail;
	ImageButton twitter;
	ImageButton map;
	ImageButton facebook;
	ImageButton exit;
	
	Call makeCall;
	Message message;

	TextView name;
	Long rowId;
	SQLiteDatabase database;
	Bundle bundle;
	Cursor contact_cursor;
	String fname;
	String number;
	String email;

	@Override
	public void onCreate(Bundle bundle) {
		this.bundle = bundle;
		super.onCreate(bundle);
		setContentView(R.layout.pane);

		helper = new DbHelper(this);

		call = (ImageButton) findViewById(R.id.call);
		text = (ImageButton) findViewById(R.id.text);
		delete = (ImageButton) findViewById(R.id.delete);
		edit = (ImageButton) findViewById(R.id.edit);
		name = (TextView) findViewById(R.id.name);
		mail = (ImageButton) findViewById(R.id.email);
		map = (ImageButton) findViewById(R.id.map);
		facebook = (ImageButton) findViewById(R.id.facebook);
		twitter = (ImageButton) findViewById(R.id.twitter);
		exit = (ImageButton) findViewById(R.id.exit);

		setId();
		contact_cursor = helper.getContact(rowId);
		setName();
		setNumber();
		setEmail();
		
		call.setOnClickListener(onClick);
		text.setOnClickListener(onClick);
		edit.setOnClickListener(onClick);
		delete.setOnClickListener(onClick);
		mail.setOnClickListener(onClick);
		map.setOnClickListener(onClick);
		facebook.setOnClickListener(onClick);
		twitter.setOnClickListener(onClick);
		exit.setOnClickListener(onClick);

	}

	private void setEmail() {
		email = contact_cursor.getString(contact_cursor
				.getColumnIndexOrThrow(DbHelper.KEY_EMAIL_1));
	}

	private void setNumber() {
		number = contact_cursor.getString(contact_cursor
				.getColumnIndexOrThrow(DbHelper.KEY_MOBILE_1));
	}

	public void setId() {
		Bundle extras = getIntent().getExtras();
		rowId = null;
		rowId = (bundle == null) ? null : (Long) bundle
				.getSerializable(DbHelper.KEY_ROWID);
		if (extras != null) {
			rowId = extras.getLong(DbHelper.KEY_ROWID);
		}
	}

	public void setName() {
		fname = contact_cursor.getString(contact_cursor
				.getColumnIndexOrThrow(DbHelper.KEY_NAME));
		StringBuffer name_buffer = new StringBuffer(fname);
		CharSequence char_seq = name_buffer.subSequence(0, fname.length());
		name.setText(char_seq);
	}

	private View.OnClickListener onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.call:

				Intent call_intent = new Intent();
				call_intent.setClass(ContactPane.this, Call.class);
				call_intent.putExtra(DbHelper.KEY_ROWID, rowId);
				startActivity(call_intent);
				break;

			case R.id.text:

				Intent text_intent = new Intent(Intent.ACTION_VIEW);
				text_intent.setClass(ContactPane.this, TextMessage.class);
				text_intent.putExtra("sms_body", "smsBody");
				text_intent.putExtra(DbHelper.KEY_ROWID, rowId);
				text_intent.setType("vnd.android-dir/mms-sms");
				startActivityForResult(text_intent, Code.SEND_TEXT);
				break;

			case R.id.delete:

				Intent delete_intent = new Intent();
				delete_intent.setClass(ContactPane.this, Deletion.class);
				delete_intent.putExtra(DbHelper.KEY_ROWID, rowId);
				startActivityForResult(delete_intent, Code.MARK_FOR_DELETION);
				break;

			case R.id.edit:
				
				Intent edit_intent = new Intent();
				edit_intent.setClass(ContactPane.this, ContactEditor.class);
				edit_intent.putExtra(DbHelper.KEY_ROWID, rowId);
				edit_intent.putExtra(Code.REQUEST_CODE, Code.CONTACT_EDIT);
				startActivityForResult(edit_intent, Code.CONTACT_EDIT);

				break;

			case R.id.email:

				Intent mail_intent = new Intent();
				mail_intent.setClass(ContactPane.this, EmailSender.class);
				mail_intent.putExtra(DbHelper.KEY_ROWID, rowId);
				startActivity(mail_intent);

				break;

			case R.id.twitter:

				Intent twitter_intent = new Intent();
				twitter_intent.setClass(ContactPane.this, Twitter.class);
				twitter_intent.putExtra(DbHelper.KEY_ROWID, rowId);
				startActivity(twitter_intent);
				break;

			case R.id.map:
				Intent map_intent = new Intent(Intent.ACTION_VIEW);
				map_intent.setClass(ContactPane.this, Directions.class);
				map_intent.putExtra(DbHelper.KEY_ROWID, rowId);
				startActivity(map_intent);
				break;

			case R.id.facebook:

				Intent facebook = new Intent(Intent.ACTION_VIEW);
				facebook.setClass(ContactPane.this, FacebookHelper.class);
				facebook.putExtra(DbHelper.KEY_ROWID, rowId);
				startActivity(facebook);
				break;	
			
			case R.id.exit:
				finish();
				System.exit(0);
				break;
			}	
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent backIntent = new Intent();
			backIntent.setClass(ContactPane.this, UltimatePhonebook.class);
			backIntent.putExtra(DbHelper.KEY_ROWID, rowId);
			backIntent.putExtra(Code.REQUEST_CODE, Code.CONTACT_EDIT);
			if (getParent() == null) {
				setResult(Activity.RESULT_OK, backIntent);
			} else {
				getParent().setResult(Activity.RESULT_OK, backIntent);
			}
			startActivity(backIntent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if ((requestCode == Code.MARK_FOR_DELETION)
				&& (resultCode == RESULT_OK)) {

			Bundle extras = data.getExtras();
			rowId = null;
			rowId = (bundle == null) ? null : (Long) bundle
					.getSerializable(DbHelper.KEY_ROWID);
			if (extras != null) {
				rowId = extras.getLong(DbHelper.KEY_ROWID);
			}
			Intent backIntent = new Intent();
			backIntent.setClass(ContactPane.this, UltimatePhonebook.class);
			backIntent.putExtra(DbHelper.KEY_ROWID, rowId);
			if (getParent() == null) {
				setResult(Code.MARK_FOR_DELETION, backIntent);
			} else {
				getParent().setResult(Code.MARK_FOR_DELETION, backIntent);
			}
			finish();
		} else if ((requestCode == Code.CONTACT_EDIT)
				&& (resultCode == RESULT_OK)) {
			onCreate(bundle);
		} else if ((requestCode == Code.SEND_TEXT)){
			switch (resultCode) {
			case Activity.RESULT_OK:
				Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
				break;
			case Activity.RESULT_CANCELED:
				Toast.makeText(getBaseContext(), "SMS failed", Toast.LENGTH_SHORT).show();
				break;
			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
				break;
			case SmsManager.RESULT_ERROR_NO_SERVICE:
				Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
				break;
			case SmsManager.RESULT_ERROR_NULL_PDU:
				Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
				break;
			case SmsManager.RESULT_ERROR_RADIO_OFF:
				Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
				break;
		}
		}
	}
}
