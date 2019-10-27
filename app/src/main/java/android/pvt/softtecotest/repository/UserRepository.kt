package android.pvt.softtecotest.repository

import android.pvt.softtecotest.entity.User
import io.reactivex.Single

interface UserRepository {
    fun getUser(id:Int): Single<User>
}