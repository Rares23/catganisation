package com.catganisation.ui.activities

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.catganisation.R
import com.catganisation.app.CatganisationApplication
import com.catganisation.data.utils.AuthConstants
import com.catganisation.ui.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.content_login.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeUi()
        initializeViewModel()
    }

    private fun initializeUi() {
        initToolbar()
    }

    private fun initToolbar() {
        toolbar.title = getString(R.string.login_page_title)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
               true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initializeViewModel() {
        (application as CatganisationApplication).getAppComponent().inject(this)

        loginViewModel.authResponse.observe(this, Observer {

            textInputLayout_email.error = null
            textInputLayout_password.error = null

            if(it?.success == true) {
                loginViewModel.authResponse.value = null

                setResult(Activity.RESULT_OK)
                finish()
            } else {
                it?.validations?.keys?.forEach { key ->
                    when(key) {
                        AuthConstants.USER_EMAIL -> textInputLayout_email.error = it.validations[AuthConstants.USER_EMAIL]
                        AuthConstants.USER_PASSWORD -> textInputLayout_password.error = it.validations[AuthConstants.USER_PASSWORD]
                        else -> Toast.makeText(this, it.validations[key], Toast.LENGTH_SHORT).show()
                    }

                }
            }
        })

        button_login.setOnClickListener {
            loginViewModel.login(
                textInputEditText_email.text.toString(),
                textInputEditText_password.text.toString()
            )
        }
    }
}