package com.catganisation.data.repositories

import com.catganisation.data.models.Filter
import io.reactivex.Observable

interface FilterRepository {
    fun getActiveFilters() : Observable<HashSet<Filter<*>>>
    fun updateFilters(filters: HashSet<Filter<*>>) : Observable<HashSet<Filter<*>>>
}