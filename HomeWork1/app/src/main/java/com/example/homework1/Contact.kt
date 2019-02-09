package com.example.homework1

import android.os.Parcel
import android.os.Parcelable

//Класс контакта, который наследует Parcelable, таким обазом можно передать массив контактов через Intent
class Contact (val name : String, val number : String) : Parcelable {
    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel!!.writeString(name)
        parcel.writeString(number)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Contact> = MyCreator()
    }

    class MyCreator: Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel.readString()!!, parcel.readString()!!)
        }

        override fun newArray(size: Int): Array<Contact> {
            return Array(size){
                Contact("name", "number")
            }
        }
    }
}