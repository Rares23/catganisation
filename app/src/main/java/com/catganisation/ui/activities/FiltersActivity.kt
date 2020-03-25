package com.catganisation.ui.activities

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.catganisation.R
import com.catganisation.app.CatganisationApplication
import com.catganisation.data.actions.NotifyAction
import com.catganisation.data.models.Country
import com.catganisation.data.utils.Selectable
import com.catganisation.ui.adapters.CountryAdapter
import com.catganisation.ui.listeners.OnCountryItemSelect
import com.catganisation.ui.viewmodels.FiltersViewModel
import kotlinx.android.synthetic.main.content_filters.*
import kotlinx.android.synthetic.main.toolbar.toolbar
import kotlinx.android.synthetic.main.toolbar_filters.*
import javax.inject.Inject

class FiltersActivity : AppCompatActivity(), OnCountryItemSelect {

    @Inject
    lateinit var filtersViewModel: FiltersViewModel

    private lateinit var countriesAdapter: CountryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filters)
        initializeUi()
        initializeViewModel()
    }

    private fun initializeUi() {
        initToolbar()
        initCountriesList()
        initButtons()
        initSearchCountryListener()
    }

    private fun initButtons() {
        fab_resetFilters.setOnClickListener {
            filtersViewModel.resetFilters()
        }

        fab_saveFilters.setOnClickListener {
            filtersViewModel.saveFilters()
        }
    }

    private fun initSearchCountryListener() {
        editText_searchCountry.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtersViewModel.loadFiltersData(s.toString())
            }
        })
    }

    private fun initCountriesList() {
        countriesAdapter = CountryAdapter(this, this)
        val layoutManager: LinearLayoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        recyclerView_countries.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView_countries.layoutManager = layoutManager
        recyclerView_countries.adapter = countriesAdapter
    }

    private fun initializeViewModel() {
        (application as CatganisationApplication).getAppComponent().inject(this)

        filtersViewModel.countries.observe(this, Observer { countries ->
            countriesAdapter.setCountries(countries)
        })

        filtersViewModel.countryNotifyAction.observe(this, Observer {
            it?.let {
                if(it.action == NotifyAction.Action.UPDATE) {
                    countriesAdapter.updateCountrySelectStatus(it.value)
                }

                if(it.action == NotifyAction.Action.UPDATE_ALL) {
                    countriesAdapter.notifyDataSetChanged()
                }
            }
        })

        filtersViewModel.submitNotify.observe(this, Observer {
            if(it == true) {
                filtersViewModel.submitNotify.value = null

                setResult(Activity.RESULT_OK)
                finish()
            }
        })

        filtersViewModel.loadFiltersData(editText_searchCountry.text.toString())
    }

    private fun updateActionButtons(selectedCountries: Int) {
        if(selectedCountries > 0) {
            fab_resetFilters.show()
            fab_saveFilters.show()
        } else {
            fab_resetFilters.hide()
            fab_saveFilters.hide()
        }
    }

    private fun initToolbar() {
        toolbar.title = getString(R.string.filters_page_title)
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

    override fun onBackPressed() {
        filtersViewModel.resetTmpSelectedCountries()
        super.onBackPressed()
    }

    override fun onCountrySelect(country: Selectable<Country>) {
        filtersViewModel.selectCountry(country)
    }
}