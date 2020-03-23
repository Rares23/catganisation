package com.catganisation.data.actions

abstract class NotifyAction<T>(val action: Action, val value: T) {
    enum class Action {
        INSERT, UPDATE, REMOVE, UPDATE_ALL
    }
}