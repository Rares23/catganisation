package com.catganisation.data.datasource

import java.util.*

interface CountriesDataSource {
    fun provideCountries() : List<Locale>
}