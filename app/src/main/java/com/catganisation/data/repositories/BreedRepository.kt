package com.catganisation.data.repositories

import androidx.lifecycle.MutableLiveData
import com.catganisation.data.actions.NotifyAction
import com.catganisation.data.models.Breed
import com.catganisation.data.models.BreedImage
import com.catganisation.data.models.Filter
import io.reactivex.Observable

interface BreedRepository {
    fun getBreeds(filters: HashSet<Filter<*>>, breedItemNotify: MutableLiveData<Breed>) : Observable<List<Breed>>
    fun getBreedById(breedId: String) : Observable<Breed?>
}