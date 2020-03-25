package com.catganisation.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.catganisation.data.models.User
import com.catganisation.data.repositories.AuthRepository
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler
) : BaseViewModel() {

    private lateinit var loadProfileDisposable: Disposable
    private lateinit var logoutDisposable: Disposable

    val user: MutableLiveData<User> = MutableLiveData()
    val actionLogout: MutableLiveData<Boolean> = MutableLiveData()

    fun loadUser() {
        loadProfileDisposable = authRepository.getLoggedUser()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe(
                { user.value = it },
                { it.printStackTrace() }
            )
    }

    fun logout() {
        logoutDisposable = authRepository.logout()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe(
                { actionLogout.postValue(true) },
                {it.printStackTrace()}
            )
    }

    override fun onCleared() {
        super.onCleared()

        if(!logoutDisposable.isDisposed) {
            logoutDisposable.dispose()
        }

        if(!loadProfileDisposable.isDisposed) {
            loadProfileDisposable.dispose()
        }
    }
}