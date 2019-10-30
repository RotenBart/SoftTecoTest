package android.pvt.softtecotest.mvvm


import android.pvt.softtecotest.entity.User
import android.pvt.softtecotest.repository.provideUserRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.kotlin.createObject

class ViewModelUser : ViewModel() {

    private var disposable: Disposable? = null
    private val repository = provideUserRepository()
    var realm: Realm = Realm.getDefaultInstance()
    val state: MutableLiveData<MVVMState> by lazy(LazyThreadSafetyMode.NONE) {
        MutableLiveData<MVVMState>()
    }

    fun load(id: Int) {
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
        realm.close()
    }

    fun saveUser(user: User) {
        realm.executeTransaction { realm ->
            if (checkRealmObj(user) == null) {
                val newUser = realm.createObject<User>(user.id)
                newUser.name = user.name
                newUser.username = user.username
                newUser.email = user.email
                newUser.phone = user.phone
                newUser.address = realm.copyToRealm(user.address)
                newUser.website = user.website
            }
        }
    }

    private fun checkRealmObj(user: User): User? {
        return realm.where(User::class.java).equalTo("id", user.id).findFirst()
    }
}