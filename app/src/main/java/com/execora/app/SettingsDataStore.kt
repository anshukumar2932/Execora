package com.execora.app

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(
    name = "execora_settings"
)

val THEME_KEY = stringPreferencesKey("theme")
