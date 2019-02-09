package com.example.homework1

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

//Тут конструстор с необязательным полем activity, потому что Manifest ругался на то, что нет стандартного конструктора
class MyBroadcastReceiver(private val activity: SecondActivity = SecondActivity()) : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        //Устанавливаем резальтат для экземляра SecondActivity и добавляем массив контактов, который мы получили из сервиса
        when(intent!!.action){
            SecondActivity.CUSTOM_ACTION -> {
                val data = Intent()
                data.putExtra(FirstActivity.EXTRA_ID, intent.getParcelableArrayExtra(MyService.SERVICE_EXTRA_ID))
                activity.setResult(Activity.RESULT_OK, data)
                activity.finish()
            }
        }
    }
}