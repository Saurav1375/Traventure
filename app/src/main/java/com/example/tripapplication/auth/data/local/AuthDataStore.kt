package com.example.tripapplication.auth.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.tripapplication.auth.domain.AuthResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
private val Context.dataStore : DataStore<Preferences> by preferencesDataStore("tokens")

class AuthDataStore(
    private val context: Context
) {
    private object PreferencesKeys {
        val IS_AUTHENTICATED = booleanPreferencesKey("is_authenticated")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val ID_TOKEN = stringPreferencesKey("id_token")
        val ACCESS_TOKEN_EXPIRATION = longPreferencesKey("access_token_expiration")
    }

    val authState: Flow<AuthResponse> = context.dataStore.data.map { preferences ->
        AuthResponse(
            accessToken = preferences[PreferencesKeys.ACCESS_TOKEN],
            refreshToken = preferences[PreferencesKeys.REFRESH_TOKEN],
            idToken = preferences[PreferencesKeys.ID_TOKEN],
            accessTokenExpirationTime = preferences[PreferencesKeys.ACCESS_TOKEN_EXPIRATION]
        )
    }

    suspend fun saveAuthState(authState: AuthResponse) {

        Log.d("LoginScreen", "Saving auth state: ${authState.accessToken}")
        context.dataStore.edit { preferences ->
            if (authState.accessToken != null) {
                preferences[PreferencesKeys.ACCESS_TOKEN] = authState.accessToken
            }
            if (authState.refreshToken != null) {
                preferences[PreferencesKeys.REFRESH_TOKEN] = authState.refreshToken
            }
            if (authState.idToken != null) {
                preferences[PreferencesKeys.ID_TOKEN] = authState.idToken
            }
            if (authState.accessTokenExpirationTime != null) {
                preferences[PreferencesKeys.ACCESS_TOKEN_EXPIRATION] = authState.accessTokenExpirationTime
            }
        }
    }

    suspend fun clearAuthState() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}