package android.pvt.softtecotest.repository

import android.pvt.softtecotest.network.NetProvider

fun providePostRepository(): PostRepository {
    return PostRepositoryRemote(
        NetProvider.provideApi(
            NetProvider.provideRetrofit(
                "http://jsonplaceholder.typicode.com/",
                NetProvider.provideOkHttp(),
                NetProvider.provideGson()
            )
        )
    )
}