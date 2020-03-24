package com.catganisation.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import com.catganisation.R
import com.catganisation.data.models.Country
import com.catganisation.data.utils.Selectable
import kotlinx.android.synthetic.main.item_view_country.view.*

class CountryItemView : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.item_view_country, this)
    }

    fun setContent(country: Selectable<Country>) {
        textView_countryLabel.text = country.toString()
        if(country.selected) {
            imageView_countrySelected.visibility = View.VISIBLE
            textView_countryLabel.setTextColor(
                ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
            )
        } else {
            textView_countryLabel.setTextColor(
                ResourcesCompat.getColor(resources, R.color.colorDarkGray, null)
            )
            imageView_countrySelected.visibility = View.INVISIBLE
        }
    }
}