package com.catganisation.ui.activities

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.catganisation.R
import com.catganisation.app.CatganisationApplication
import com.catganisation.ui.viewmodels.ProfileViewModel
import kotlinx.android.synthetic.main.content_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class ProfileActivity : AppCompatActivity() {

    @Inject
    lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initializeUi()
        initializeViewModel()
    }

    private fun initializeUi() {
        initToolbar()

        button_logout.setOnClickListener {
            profileViewModel.logout()
        }
    }

    private fun initToolbar() {
        toolbar.title = getString(R.string.profile_page_title)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
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

        profileViewModel.user.observe(this, Observer{
            textView_userEmailLabel.text = it.email
        })

        profileViewModel.actionLogout.observe(this, Observer {
            if(it == true) {
                profileViewModel.actionLogout.removeObservers(this)

                setResult(Activity.RESULT_OK)
                finish()
            }
        })

        profileViewModel.loadUser()
    }
}