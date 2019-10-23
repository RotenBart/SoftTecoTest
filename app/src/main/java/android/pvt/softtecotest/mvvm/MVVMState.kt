package android.pvt.softtecotest.mvvm

import android.pvt.softtecotest.entity.Post

sealed class MVVMState {
    class Data(val postList: List<Post>) : MVVMState()
    class Error(throwable: Throwable) : MVVMState()
}