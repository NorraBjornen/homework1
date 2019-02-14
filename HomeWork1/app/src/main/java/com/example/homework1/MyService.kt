package com.example.homework1

import android.app.IntentService
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Parcelable
import android.provider.ContactsContract
import android.support.v4.content.LocalBroadcastManager

class MyService : IntentService("MyService") {

    companion object {
        const val SERVICE_EXTRA_ID = "SERVICE_EXTRA_ID"
    }

    override fun onHandleIntent(intent: Intent?) {
        val res = getContacts()

        val localBroadcastManager = LocalBroadcastManager.getInstance(this)

        val returnIntent = Intent()
        returnIntent.action = SecondActivity.CUSTOM_ACTION
        returnIntent.putExtra(SERVICE_EXTRA_ID, res)

        localBroadcastManager.sendBroadcast(returnIntent)
    }

    private fun getContacts() : ArrayList<Contact>{
        val contacts : ArrayList<Contact> = ArrayList()
        val uri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)

        cursor?.use {
            if(cursor.moveToFirst()){

                val idName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val idNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                do {
                    contacts.add(Contact(cursor.getString(idName), cursor.getString(idNumber)))
                } while (cursor.moveToNext())
            }
        }

        return contacts
    }

}