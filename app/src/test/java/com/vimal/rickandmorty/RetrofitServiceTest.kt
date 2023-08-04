package com.vimal.rickandmorty

import com.google.gson.Gson
import com.vimal.rickandmorty.api.RetrofitService
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitServiceTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var apiService: RetrofitService
    lateinit var gson: Gson

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        gson = Gson()
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(RetrofitService::class.java)
    }

    @Test
    fun `get all character api test`() {
        runBlocking {
             val mockResponse = MockResponse()
            mockWebServer.enqueue(mockResponse.setBody("[]"))
            val response = apiService.getAllCharacter()
            val request = mockWebServer.takeRequest()
            assertEquals("/character",request.path)
            assertEquals(true, response?.isSuccessful == true)
        }
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }
}