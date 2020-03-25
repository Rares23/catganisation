package com.catganisation.data.repositories

import com.catganisation.data.models.Country
import io.reactivex.Observable

interface CountryRepository {
    fun getAllCountries() : Observable<List<Country>>
    fun getCountryByCode(code: String) : Observable<Country?>
}