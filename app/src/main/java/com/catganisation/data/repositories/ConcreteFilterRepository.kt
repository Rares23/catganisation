package com.catganisation.data.repositories

import com.catganisation.data.models.Filter
import io.reactivex.Observable

class ConcreteFilterRepository : FilterRepository {

    private var activeFilters: HashSet<Filter<*>> = HashSet()

    override fun getActiveFilters(): Observable<HashSet<Filter<*>>> {
        return Observable.just(activeFilters)
    }

    override fun updateFilters(filters: HashSet<Filter<*>>) : Observable<HashSet<Filter<*>>> {
        return Observable.just {
            activeFilters = filters
            return@just activeFilters
        }.map { it.invoke() }
    }
}