package com.example.homework1

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.recyclerview_item.view.*


class FirstActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "EXTRA"
        const val PERMISSION_REQUEST_CODE = 0
        const val REQUEST_CODE = 1
    }

    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        title = resources.getString(R.string.first_activity_title)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        checkPermission()
    }

    private fun checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_REQUEST_CODE)
        } else
            startSecondActivity()
    }

    private fun startSecondActivity(){
        val intent = Intent(this, SecondActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val content = data!!.getSerializableExtra(EXTRA_ID) as ArrayList<Contact>
            adapter = Adapter(this, content)
            recyclerView.adapter = adapter
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                    startSecondActivity()
                else
                    checkPermission()
            }
        }
    }

    private class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.name!!
        private val number = itemView.number!!
        private lateinit var contact : Contact

        fun bind(contact: Contact){
            this.contact = contact
            name.text = contact.name
            number.text = contact.number
        }
    }

    private class Adapter(private val context : Context, private val content : ArrayList<Contact>) : RecyclerView.Adapter<Holder>() {

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
