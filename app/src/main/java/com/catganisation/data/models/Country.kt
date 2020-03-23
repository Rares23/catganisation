package com.catganisation.data.models

data class Country(val code: String, val name: String) : Cloneable {
    override fun toString(): String {
        return "$name, $code"
    }

    public override fun clone(): Any {
        return Country(code, name)
    }
}