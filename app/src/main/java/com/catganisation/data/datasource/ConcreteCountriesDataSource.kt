package com.catganisation.data.datasource

import java.util.*
import kotlin.collections.ArrayList


class ConcreteCountriesDataSource : CountriesDataSource {
    override fun provideCountries(): List<Locale> {
        val locales: ArrayList<Locale> = ArrayList()
        Locale.getISOCountries().forEach {
            locales.add(Locale("", it))
        }

        return locales
    }
}