package com.catganisation.data.repositories

import com.catganisation.data.models.Country
import com.catganisation.data.datasource.CountriesDataSource
import io.reactivex.Observable
import javax.inject.Inject

class ConcreteCountryRepository @Inject constructor(
    private val countriesProvider: CountriesDataSource
) : CountryRepository {

    private var cacheCountries: List<Country> = listOf()

    override fun getAllCountries(): Observable<List<Country>> {
        return if(cacheCountries.isEmpty()) {
            Observable.just(countriesProvider.provideCountries())
                .map {locales ->
                    val countries: ArrayList<Country> = ArrayList()
                    locales.forEach {locale ->
                        val country = Country(locale.country, locale.displayName)
                        countries.add(country)
                    }

                    cacheCountries = countries.toList()
                    return@map cacheCountries
                }
        } else {
            Observable.just(cacheCountries)
        }
    }

    override fun getCountryByCode(code: String): Observable<Country?> {
        return getAllCountries().map {countries ->
            countries.forEach {country ->
                if(country.code == code) {
                    return@map country
                }
            }
            return@map null
        }
    }
}