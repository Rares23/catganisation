package com.catganisation.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.catganisation.R
import com.catganisation.app.CatganisationApplication
import com.catganisation.di.components.DaggerAppComponent
import com.catganisation.ui.adapters.BreedAdapter
import com.catganisation.ui.viewmodels.BreedsListViewModel
import kotlinx.android.synthetic.main.content_breeds_list.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class BreedsListActivity : AppCompatActivity() {

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
        breedListAdapter = BreedAdapter(this)
        val layoutManager: LinearLayoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView_breedsList.layoutManager = layoutManager
        recyclerView_breedsList.adapter = breedListAdapter
    }

    private fun initToolbar() {
        toolbar.title = getString(R.string.app_name)
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

        breedsListViewModel.loading.observe(this, Observer {loading ->
            if(loading) {
                progressBar_loading.visibility = View.VISIBLE
                recyclerView_breedsList.visibility = View.GONE
            } else {
                progressBar_loading.visibility = View.GONE
                recyclerView_breedsList.visibility = View.VISIBLE
                textView_noBreeds.visibility = View.GONE
            }
        })

        breedsListViewModel.getBreeds()
    }
}