package com.example.data.dataSource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.util.Constants.TOKEN_KEY
import com.example.data.util.Constants.TOKEN_PREFERENCE_NAME
import com.example.domain.dataSource.MessageTokenDataSource
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class MessageTokenDataSourceImpl(
    private val context: Context,
    private val firebaseMessaging: FirebaseMessaging,
) : MessageTokenDataSource {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = TOKEN_PREFERENCE_NAME)

    private object PreferencesKey {
        val tokenKey = stringPreferencesKey(name = TOKEN_KEY)
    }

    override suspend fun saveMessageToken() {
        var token = ""
        firebaseMessaging.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }
            token = task.result
            Timber.e("TOKEN : $token")
        }
        context.dataStore.edit { mutablePreferences: MutablePreferences ->
            mutablePreferences[PreferencesKey.tokenKey] = token
        }
    }

    override suspend fun readMessageToken(): Flow<String> =
        context.dataStore.data.map { preference ->
            preference[PreferencesKey.tokenKey] ?: ""
        }
}
