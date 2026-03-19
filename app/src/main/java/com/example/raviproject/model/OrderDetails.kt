package com.example.raviproject.model

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

class orderdetails():Parcelable {
    var userUid:String? = null
    var userName:String?=null
    var foodNames:String?= null
    var foodImages:String?= null
    var foodprices:String?= null
    var foodQuantities:String?= null
    var addresses:String?= null
    var totalprice:String?= null
    var phoneNumber:String?= null
    var itemPushkey:String?= null
    var currentTime:String?= null

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        foodNames = parcel.readString()
        foodImages = parcel.readString()
        foodprices = parcel.readString()
        foodQuantities = parcel.readString()
        addresses = parcel.readString()
        totalprice = parcel.readString()
        phoneNumber = parcel.readString()
        itemPushkey = parcel.readString()
        currentTime = parcel.readString()
    }

    constructor(
        userId: String,
        name: String,
        foodItemName: ArrayList<String>,
        foodItemPrice: ArrayList<String>,
        foodItemImage: ArrayList<String>,
        foodItemQuantitiy: ArrayList<Int>,
        address: String,
        phone: String,
        itemPushkey: String?,
        b: Boolean,
        b1: Boolean
    ):this()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeString(foodNames)
        parcel.writeString(foodImages)
        parcel.writeString(foodprices)
        parcel.writeString(foodQuantities)
        parcel.writeString(addresses)
        parcel.writeString(totalprice)
        parcel.writeString(phoneNumber)
        parcel.writeString(itemPushkey)
        parcel.writeString(currentTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<orderdetails> {
        override fun createFromParcel(parcel: Parcel): orderdetails {
            return orderdetails(parcel)
        }

        override fun newArray(size: Int): Array<orderdetails?> {
            return arrayOfNulls(size)
        }
    }


}