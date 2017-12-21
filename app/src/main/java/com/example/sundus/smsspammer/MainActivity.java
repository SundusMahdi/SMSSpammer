package com.example.sundus.smsspammer;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final int RESULT_PICK_CONTACT = 85500;
    public static final String EXTRA_MESSAGE = "com.example.applol.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void pickContact(View v)
    {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }
    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     * @param data
     */
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        EditText editTextNum = (EditText) findViewById(R.id.editTextNumber);
        try {
            String phoneNo = null ;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNo = cursor.getString(phoneIndex);
            // Set the value to the textviews
            editTextNum.setText(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);

        String phoneNum = ((EditText) findViewById(R.id.editTextNumber)).getText().toString();
        String message = ((EditText) findViewById(R.id.editTextMessage)).getText().toString();
        int times = Integer.valueOf(((EditText) findViewById(R.id.editTextTimes)).getText().toString()).intValue();

        for (int i = 0; i < times; i++) {
            try {
                SmsManager.getDefault().sendTextMessage(phoneNum, null, message, null, null);
            } catch (Exception e) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                AlertDialog dialog = alertDialogBuilder.create();
                dialog.setMessage(e.getMessage());
                dialog.show();
            }
            intent.putExtra(EXTRA_MESSAGE, phoneNum);
            startActivity(intent);
        }
    }
}

