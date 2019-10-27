package android.pvt.softtecotest.repository

import android.pvt.softtecotest.api.ApiUsers
import android.pvt.softtecotest.entity.User
import io.reactivex.Single

class UserRepositoryRemote(private val api: ApiUsers) : UserRepository {
    override fun getUser(id:Int): Single<User> {
        return api.getUser(id)
    }
}