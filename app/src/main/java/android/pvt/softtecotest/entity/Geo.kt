package android.pvt.softtecotest.entity

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class Geo: RealmObject() {
    @SerializedName("lat")
    open var latitude: String = ""
    @SerializedName("lng")
    open var longitude: String = ""
}