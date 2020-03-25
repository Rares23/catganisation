package com.catganisation.data.models

import com.catganisation.data.utils.FiltersTags

class CountriesFilter(countries: HashSet<Country>) : Filter<HashSet<Country>>(countries) {
    override fun getTag(): String {
        return FiltersTags.COUNTRIES_FILTER_TAG
    }
}