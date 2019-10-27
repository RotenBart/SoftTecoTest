package android.pvt.softtecotest.mvvm

import android.pvt.softtecotest.entity.Post
import android.pvt.softtecotest.entity.User

sealed class MVVMState {
    class Data(val postList: List<Post>) : MVVMState()
    class DataUser (val user: User) : MVVMState()
    class Error(throwable: Throwable) : MVVMState()
}