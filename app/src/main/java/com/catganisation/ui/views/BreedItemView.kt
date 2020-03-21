package com.catganisation.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.catganisation.R
import com.catganisation.data.models.Breed
import kotlinx.android.synthetic.main.item_view_breed.view.*

class BreedItemView : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.item_view_breed, this)
    }

    fun setContent(breed: Breed) {
        textView_breedName.text = breed.name
        textView_breedDescription.text = breed.description

        if(breed.imageUrl.isNullOrEmpty()) {
            imageView_breed.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_cat_image_placeholder, null))
        } else {
            Glide.with(context)
                .load(breed.imageUrl)
                .into(imageView_breed)
        }
    }
}