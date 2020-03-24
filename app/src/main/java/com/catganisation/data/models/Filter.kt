package com.catganisation.data.models

abstract class Filter<T>(val value: T) : Cloneable {
    abstract fun getTag(): String
}