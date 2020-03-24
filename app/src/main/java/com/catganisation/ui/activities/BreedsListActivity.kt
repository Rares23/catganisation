package com.catganisation.ui.activities

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.catganisation.R
import com.catganisation.app.CatganisationApplication
import com.catganisation.data.models.Breed
import com.catganisation.ui.adapters.BreedAdapter
import com.catganisation.ui.listeners.OnBreedImageLoad
import com.catganisation.ui.listeners.OnBreedItemSelect
import com.catganisation.ui.uitls.ActivityRequestCodes
import com.catganisation.ui.viewmodels.BreedsListViewModel
import kotlinx.android.synthetic.main.content_breeds_list.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class BreedsListActivity : AppCompatActivity(),
    OnBreedItemSelect, OnBreedImageLoad {

    @Inject
    lateinit var breedsListViewModel: BreedsListViewModel

    lateinit var breedListAdapter: BreedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breeds_list)
        initializeUi()
        initializeViewModel()
    }

    private fun initializeUi() {
        initToolbar()
        initBreedsList()
    }

    private fun initBreedsList() {
        breedListAdapter = BreedAdapter(this, this, this)
        val layoutManager: LinearLayoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView_breedsList.layoutManager = layoutManager
        recyclerView_breedsList.adapter = breedListAdapter
    }

    private fun initToolbar() {
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val profileIcon: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.ic_account_circle, null)
        profileIcon?.let {
            DrawableCompat.setTint(it, ResourcesCompat.getColor(resources, R.color.colorWhite,null))
        }
        supportActionBar?.setHomeAsUpIndicator(profileIcon)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.menu_breeds_list, menu)
        menu?.children?.forEach {
            if(it.itemId == R.id.menuItem_filters) {
                it.icon?.let {drawable ->
                    DrawableCompat.setTint(drawable, ResourcesCompat.getColor(resources, R.color.colorWhite, null));
                }

                it.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menuItem_filters -> {
                openFilters()
                true
            }

            android.R.id.home -> {
                onProfileMenuItemClick()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onProfileMenuItemClick() {
        if(breedsListViewModel.isLogged.value == true) {
            openProfile()
        } else {
            openLogin()
        }
    }

    private fun openLogin() {
        val intent: Intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, ActivityRequestCodes.LOGIN_REQUEST_CODE)
    }

    private fun openProfile() {
        val intent: Intent = Intent(this, ProfileActivity::class.java)
        startActivityForResult(intent, ActivityRequestCodes.PROFILE_REQUEST_CODE)
    }

    private fun openFilters() {
        val intent = Intent(this, FiltersActivity::class.java)
        startActivityForResult(intent, ActivityRequestCodes.FILTERS_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            ActivityRequestCodes.FILTERS_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK) {
                    breedsListViewModel.getBreeds()
                }
            }
            ActivityRequestCodes.PROFILE_REQUEST_CODE,
            ActivityRequestCodes.LOGIN_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK) {
                    breedsListViewModel.checkLoggedUser()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initializeViewModel() {
        (application as CatganisationApplication).getAppComponent().inject(this)

        breedsListViewModel.breedsList.observe(this, Observer {breeds ->
            breedListAdapter.setBreeds(breeds)

            if(breeds.isEmpty()) {
                textView_noBreeds.visibility = View.VISIBLE
            } else {
                textView_noBreeds.visibility = View.GONE
            }
        })

        breedsListViewModel.notifyBreedItemUpdate.observe(this, Observer {
            breedListAdapter.updateBreed(it)
        })

        breedsListViewModel.loading.observe(this, Observer {loading ->
            if(loading) {
                progressBar_loading.visibility = View.VISIBLE
                recyclerView_breedsList.visibility = View.GONE
                textView_noBreeds.visibility = View.GONE
            } else {
                progressBar_loading.visibility = View.GONE
                recyclerView_breedsList.visibility = View.VISIBLE
            }
        })

        breedsListViewModel.checkLoggedUser()
        breedsListViewModel.getBreeds()
    }

    override fun selectBreed(breedId: String) {
        val intent: Intent = Intent(this, BreedDetailsActivity::class.java)
        intent.putExtra("breedId", breedId)
        startActivity(intent)
    }

    override fun loadImage(breed: Breed) {
        breedsListViewModel.getBreedImage(breed)
    }
}