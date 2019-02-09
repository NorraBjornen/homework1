package com.example.homework1

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.recyclerview_item.view.*


class FirstActivity : AppCompatActivity() {

    //Константы для:
    //1)EXTRA_ID - получения ParcelableArrayExtra из Intent, возвращаемого SecondActivity
    //2)PERMISSION_REQUEST_CODE - обработки результата запроса разрешения на чтение списка контактов
    //3)REQUEST_CODE - обработки результата работы SecondActivity
    companion object {
        const val EXTRA_ID = "EXTRA"
        const val PERMISSION_REQUEST_CODE = 0
        const val REQUEST_CODE = 1
    }

    //RecyclerView и адаптер для него
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        //Устанавливаю название заголовка для первого экрана
        title = "Контакты"

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        check()
    }

    //Метод, который проверяет наличие разрешения на чтение контактов
    //Если такового нет - запрашивает
    //Если есть - вызывает метод запуска SecondActivity
    private fun check(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_REQUEST_CODE)
        } else
            start()
    }

    //Запускает SecondActivity с ожиданием результата
    private fun start(){
        val intent = Intent(this, SecondActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE)
    }

    //Обработка результата работы SecondActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Проверка того, что вернулся ответ от той активити, которую мы запускали для получения результата
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //Чтение массива контактов из вернувшегося интента
            val content = data!!.getParcelableArrayExtra(EXTRA_ID)
            //Загрузка контактов в RecyclerView
            adapter = Adapter(this, content)
            recyclerView.adapter = adapter
        }
    }

    //Обработка результата запроса разрешения на чтение контактов
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                //Если разрешение получено - вызывает метод запуска SecondActivity
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                    start()
                //Если нет - повторный запрос разрешения (да, это жёстко)
                else
                    check()
            }
        }
    }

    //Holder и Adapter - для RecyclerView
    private inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.name!!
        private val number = itemView.number!!
        private lateinit var contact : Contact

        fun bind(contact: Parcelable){
            this.contact = contact as Contact
            name.text = contact.name
            number.text = contact.number
        }
    }

    private inner class Adapter(private val context : Context, private val content : Array<Parcelable>) : RecyclerView.Adapter<Holder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return Holder(LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent,false))
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(content[position])
        }

        override fun getItemCount(): Int {
            return content.size
        }
    }
}
