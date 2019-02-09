package com.example.homework1

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    //Обозначение кода действия, которое будет отслеживать BroadcastReceiver
    companion object {
        const val CUSTOM_ACTION = "CUSTOM_ACTION"
    }

    //Ссыслка на экземпляр класса-наследника BroadcastReceiver
    private lateinit var receiver : MyBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        //Инициализация. Туда подгружается ссылка на эту активити, чтобы можно было ее закрыть из MyBroadcastReceiver
        receiver = MyBroadcastReceiver(this)
    }

    override fun onStart() {
        super.onStart()
        //Резистрация LocalBroadcastManager
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(receiver, IntentFilter(CUSTOM_ACTION))

        //Запуск сервиса
        val intent = Intent(this, MyService::class.java)
        startService(intent)
    }

    override fun onStop() {
        super.onStop()
        //Разрегистрация (?) LocalBroadcastManager
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.unregisterReceiver(receiver)
    }
}