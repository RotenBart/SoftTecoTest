package android.pvt.softtecotest.entity

import com.google.gson.annotations.SerializedName

data class Geo(
    @SerializedName("lat")
    val latitude: String,
    @SerializedName("lng")
    val longitude: String
)