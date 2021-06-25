package vn.ghn.library.repository.storage.share_referance

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import vn.ghn.library.Library

private val app: Application get() = Library.app
private val APPLICATION_ID: String get() = Library.APPLICATION_ID

object Shared {
    var shared: SharedPreferences = app.getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE)

    private fun edit(block: SharedPreferences.Editor.() -> Unit) {
        shared.edit {
            block()
        }
    }

    fun clear() {
        shared.edit {
            clear()
        }
    }

    fun addListener(listener: (String) -> Unit) {
        shared.registerOnSharedPreferenceChangeListener { sharedPref, key ->
            if (sharedPref != null && key != null) {
                listener(key)
            }
        }
    }

    fun save(key: String, value: String?) {
        edit { putString(key, value) }
    }

    fun save(key: String, value: Int?) {
        edit { putInt(key, value ?: -1) }
    }

    fun save(key: String, value: Long?) {
        edit { putLong(key, value ?: -1) }
    }

    fun save(key: String, value: Boolean?) {
        edit { putBoolean(key, value ?: false) }
    }

    fun str(key: String): String? {
        return shared.getString(key, null)
    }

    fun int(key: String): Int {
        return shared.getInt(key, -1)
    }

    fun long(key: String): Long {
        return shared.getLong(key, -1)
    }

    fun bool(key: String): Boolean {
        return shared.getBoolean(key, false)
    }

}