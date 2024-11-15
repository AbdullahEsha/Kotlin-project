package com.example.kotlinproject.service

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AuthService {
    private val client = OkHttpClient()

    // Change localhost to 10.0.2.2 for Android emulator
    private const val base_url = "http://10.0.2.2:5000/api/v1"

    suspend fun handleLogin(email: String, password: String): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val json = JSONObject()
                json.put("email", email)
                json.put("password", password)

                val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
                val request = Request.Builder()
                    .url("$base_url/auth/login")
                    .post(requestBody)
                    .build()
                try {
                    val response = client.newCall(request).execute()
                    Log.d("AuthService", "Response code: ${response.code}")
                    response.isSuccessful
                } catch (e: Exception) {
                    Log.e("AuthService", "Network error: ${e.message}")
                    false
                }
            }
        } catch (e: Exception) {
            Log.e("AuthService", "Login error: ${e.message}")
            false
        }
    }

    suspend fun handleRegister(name: String, email: String, password: String): Boolean {
        return try {
            withContext(Dispatchers.IO) {
            val json = JSONObject()
            json.put("name", name)
            json.put("email", email)
            json.put("password", password)
            json.put("role", "user")

            val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
            val request = Request.Builder()
                .url("$base_url/auth/register")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            response.isSuccessful
        } } catch (e: Exception) {
            Log.e("AuthService", "Register error: ${e.message}")
            return  e.message != null
        }
    }
}
