package android.pvt.softtecotest.repository

import android.pvt.softtecotest.api.Api
import android.pvt.softtecotest.entity.Post
import io.reactivex.Single

class PostRepositoryRemote(private val api: Api) : PostRepository {
    override fun getPosts(): Single<List<Post>> {
        return api.getPosts()
    }
}