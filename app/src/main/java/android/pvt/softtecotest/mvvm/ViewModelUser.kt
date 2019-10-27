package android.pvt.softtecotest.mvvm


import android.pvt.softtecotest.repository.providePostRepository
import android.pvt.softtecotest.repository.provideUserRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ViewModelUser : ViewModel() {

    private var disposable: Disposable? = null
    private val repository = provideUserRepository()
    val state: MutableLiveData<MVVMState> by lazy(LazyThreadSafetyMode.NONE) {
        MutableLiveData<MVVMState>()
    }

    fun load (id:Int) {
        disposable =
            repository.getUser(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    state.value = MVVMState.DataUser(data)
                }
    }
    override fun onCleared() {
        disposable?.dispose()
    }
}