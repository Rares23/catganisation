package com.catganisation.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.catganisation.R
import com.catganisation.app.CatganisationApplication
import com.catganisation.data.models.Breed
import com.catganisation.ui.viewmodels.BreedDetailsViewModel
import kotlinx.android.synthetic.main.content_breed_details.*
import kotlinx.android.synthetic.main.toolbar_breed_details.*
import javax.inject.Inject


class BreedDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var breedDetailsViewModel: BreedDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breed_details)
        initializeUi()
        initializeViewModel()
    }

    private fun initializeUi() {
        initToolbar()
    }

    private fun initializeViewModel() {
        (application as CatganisationApplication).getAppComponent().inject(this)

        breedDetailsViewModel.breed.observe(this, Observer { breed ->
            if(breed != null) {
                setBreedContent(breed)
            } else {
                Toast.makeText(this, getString(R.string.breed_details_load_error), Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        })

        breedDetailsViewModel.loadBreed(intent.getStringExtra("breedId"))
    }

    private fun setBreedContent(breed: Breed) {
        textView_breedName.text = breed.name
        textView_breedDescription.text = breed.description
        textView_location.text = breed.countryCode
        textView_temper.text = breed.temperament

        val wikiClickListener = View.OnClickListener {
            openWikiInBrowser(breed.wikiLink)
        }

        imageView_wiki.setOnClickListener(wikiClickListener)
        textView_wiki.setOnClickListener(wikiClickListener)

        imageView_breed.post {
            if(breed.imageUrl.isNullOrEmpty()) {
                imageView_breed.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_cat_image_placeholder, null))
            } else {
                Glide.with(this)
                    .load(breed.imageUrl)
                    .into(imageView_breed)
            }
        }
    }

    private fun initToolbar() {
        collapsingToolbarLayout_breedDetails.title = getString(R.string.details_page_title)
        toolbar.title = ""
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

    private fun openWikiInBrowser(wikiLink: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(wikiLink))
        startActivity(intent)
    }
}