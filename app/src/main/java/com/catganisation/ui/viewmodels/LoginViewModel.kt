package com.catganisation.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.catganisation.data.models.AuthResponse
import com.catganisation.data.repositories.AuthRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler
) : BaseViewModel() {

    private lateinit var loginDisposable: Disposable

    val authResponse: MutableLiveData<AuthResponse> = MutableLiveData()

    fun login(email: String, password: String) {
        loginDisposable = authRepository.login(email, password)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .doOnSubscribe {

            }
            .doOnTerminate {

            }
            .subscribe (
                {authResponse.value = it},
                {
                    val validations: HashMap<String, String> = HashMap()
                    validations["error"] = it.message.toString()
                    authResponse.value = AuthResponse(false, validations)
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        loginDisposable.dispose()
        authResponse.value = null
    }

    fun clearData() {
        authResponse.value = null
    }
}