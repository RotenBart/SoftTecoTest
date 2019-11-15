package android.pvt.softtecotest.api

import android.pvt.softtecotest.entity.Post
import android.pvt.softtecotest.entity.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    @GET("posts")
    fun getPosts(): Single<List<Post>>
}

interface ApiUsers {
    @GET("users/{id}")
    fun getUser(
        @Path("id") id: Int
    ): Single<User>
}