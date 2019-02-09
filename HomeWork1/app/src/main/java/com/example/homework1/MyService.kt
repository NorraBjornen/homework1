package com.example.homework1

import android.app.IntentService
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.support.v4.content.LocalBroadcastManager

class MyService : IntentService("MyService") {

    //Константа, которая служит ключем для ParcelableArrayExtra в интенте
    companion object {
        const val SERVICE_EXTRA_ID = "SERVICE_EXTRA_ID"
    }

    override fun onHandleIntent(intent: Intent?) {
        val res = getContacts()

        //Отправляем сообщение LocalBroadcastManager
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)

        val returnIntent = Intent()
        returnIntent.action = SecondActivity.CUSTOM_ACTION
        returnIntent.putExtra(SERVICE_EXTRA_ID, res)

        localBroadcastManager.sendBroadcast(returnIntent)
    }

    //Читаем контакты, загружаем их в массив
    private fun getContacts() : Array<Contact>{
        val contacts : Array<Contact>
        val uri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)

        if(cursor!!.moveToFirst()){
            contacts = Array(cursor.count) { Contact("name", "number") }

            val idName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val idNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            var i = 0

            do {
                contacts[i] = Contact(cursor.getString(idName), cursor.getString(idNumber))
                i++
            } while (cursor.moveToNext())

        } else
            contacts = emptyArray()

        cursor.close()

        return contacts
    }

}