package com.catganisation.ui.listeners

import com.catganisation.data.models.Country
import com.catganisation.data.utils.Selectable

interface OnCountryItemSelect {
    fun onCountrySelect(country: Selectable<Country>)
}