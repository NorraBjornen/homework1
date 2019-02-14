package com.example.homework1

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    companion object {
        const val CUSTOM_ACTION = "CUSTOM_ACTION"
    }

    private lateinit var receiver : BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        receiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                when(intent!!.action){
                    SecondActivity.CUSTOM_ACTION -> {
                        val data = Intent()
                        data.putExtra(FirstActivity.EXTRA_ID, intent.getSerializableExtra(MyService.SERVICE_EXTRA_ID))
                        setResult(Activity.RESULT_OK, data)
                        finish()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(receiver, IntentFilter(CUSTOM_ACTION))

        val intent = Intent(this, MyService::class.java)
        startService(intent)
    }

    override fun onStop() {
        super.onStop()
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.unregisterReceiver(receiver)
    }
}