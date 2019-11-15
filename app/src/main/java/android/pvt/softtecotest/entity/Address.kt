package android.pvt.softtecotest.entity

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class Address : RealmObject() {
    @SerializedName("street")
    open var street: String = ""
    @SerializedName("suite")
    open var suite: String = ""
    @SerializedName("city")
    open var city: String = ""
    @SerializedName("zipcode")
    open var zipcode: String = ""
    @SerializedName("geo")
    open var geo: Geo? = null
}