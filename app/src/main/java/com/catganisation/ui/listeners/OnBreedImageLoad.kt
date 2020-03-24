package com.catganisation.ui.listeners

import com.catganisation.data.models.Breed

interface OnBreedImageLoad {
    fun loadImage(breed: Breed)
}