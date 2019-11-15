package android.pvt.softtecotest.entity

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey

open class User : RealmObject() {
    @SerializedName("id")
    @PrimaryKey
    open var id: Int = 0
    @SerializedName("name")
    open var name: String = ""
    @SerializedName("username")
    open var username: String = ""
    @SerializedName("email")
    open var email: String = ""
    @SerializedName("address")
    open var address: Address? = null
    @SerializedName("phone")
    open var phone: String = ""
    @SerializedName("website")
    @Ignore
    open var website: String = ""
}
