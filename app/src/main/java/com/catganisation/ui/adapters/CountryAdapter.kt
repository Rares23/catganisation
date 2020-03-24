package com.catganisation.ui.adapters

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.catganisation.data.models.Country
import com.catganisation.data.utils.Selectable
import com.catganisation.ui.listeners.OnCountryItemSelect
import com.catganisation.ui.views.CountryItemView

class CountryAdapter(
    private val context: Context,
    private val onCountryItemSelect: OnCountryItemSelect
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    private var countries: ArrayList<Selectable<Country>> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view: CountryItemView = CountryItemView(context)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )

        view.layoutParams = layoutParams

        return CountryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    fun setCountries(countries: List<Selectable<Country>>) {
        this.countries.clear()
        this.countries.addAll(countries)
        notifyDataSetChanged()
    }

    fun updateCountrySelectStatus(countryCode: String) {
        for((i, country) in countries.withIndex()) {
            if(country.value.code == countryCode) {
                notifyItemChanged(i)
            }
        }
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.view.setContent(countries[position])
        holder.view.setOnClickListener {
            onCountryItemSelect.onCountrySelect(countries[holder.adapterPosition])
        }
    }

    class CountryViewHolder(val view: CountryItemView) : RecyclerView.ViewHolder(view)
}