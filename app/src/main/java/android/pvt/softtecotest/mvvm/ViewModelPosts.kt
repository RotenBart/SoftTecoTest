package android.pvt.softtecotest.mvvm

import android.pvt.softtecotest.repository.providePostRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ViewModelPosts : ViewModel() {

    private var disposable: Disposable? = null
    private val repository = providePostRepository()
    val state: MutableLiveData<MVVMState> by lazy(LazyThreadSafetyMode.NONE) {
        MutableLiveData<MVVMState>()
    }

    init {
        load()
    }

    private fun load() {
        disposable =
            repository.getPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    state.value = MVVMState.Data(data)
                }
    }

    override fun onCleared() {
        disposable?.dispose()
    }
}