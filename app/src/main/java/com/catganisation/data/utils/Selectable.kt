package com.catganisation.data.utils

class Selectable<T>(val value: T, var selected: Boolean) {
    override fun toString(): String {
        return value.toString()
    }
}