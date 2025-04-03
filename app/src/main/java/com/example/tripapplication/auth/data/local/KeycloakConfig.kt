package com.example.tripapplication.auth.data.local

import android.content.Context
import android.net.Uri
import com.example.tripapplication.BuildConfig
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.browser.BrowserAllowList
import net.openid.appauth.browser.VersionedBrowserMatcher

class KeycloakConfig(private val context: Context) {

    companion object {
        // Replace with your Keycloak configuration
        private const val KEYCLOAK_URL = "http://10.50.47.139:9080"
        private const val REALM = "trip"
        private const val CLIENT_ID = "trip-client"
        private const val CLIENT_SECRET = BuildConfig.CLIENT_SECRET
        private const val REDIRECT_URI = "com.example.tripapplication://oauth2redirect"
        private const val SCOPE = "openid profile email offline_access"
    }

    val serviceConfig = AuthorizationServiceConfiguration(
        Uri.parse("$KEYCLOAK_URL/realms/$REALM/protocol/openid-connect/auth"),
        Uri.parse("$KEYCLOAK_URL/realms/$REALM/protocol/openid-connect/token"),
        Uri.parse("$KEYCLOAK_URL/realms/$REALM/protocol/openid-connect/userinfo"),
        Uri.parse("$KEYCLOAK_URL/realms/$REALM/protocol/openid-connect/logout")
    )

    private val authRequestBuilder = AuthorizationRequest.Builder(
        serviceConfig,
        CLIENT_ID,
        ResponseTypeValues.CODE,
        Uri.parse(REDIRECT_URI)
    ).setScope(SCOPE)

    val clientAuth: ClientAuthentication = ClientSecretBasic(CLIENT_SECRET)
    val clientId: String = CLIENT_ID

    fun getAuthRequest() = authRequestBuilder.build()

    fun getAuthService() = AuthorizationService(
        context,
        AppAuthConfiguration.Builder()
            .setBrowserMatcher(
                BrowserAllowList(
                    VersionedBrowserMatcher.CHROME_CUSTOM_TAB,
                )
            )
            .setConnectionBuilder(ConnectionBuilderForTesting.INSTANCE) // Allow HTTP
            .build()
    )
}