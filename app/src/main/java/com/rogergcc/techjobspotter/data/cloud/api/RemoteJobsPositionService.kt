package com.rogergcc.techjobspotter.data.cloud.api

import com.rogergcc.techjobspotter.data.model.RemoteJobsResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created on enero.
 * year 2025 .
 */
interface RemoteJobsPositionService {

    @GET("remote-jobs")
    suspend fun getRemoteJobsPositionGenericResponse(@Query("limit") limit: Int): Response<ResponseBody>

    @GET("remote-jobs")
    suspend fun getResponseJobsPosition(@Query("limit") limit: Int): RemoteJobsResponse

//    https://remotive.com/api/remote-jobs?search=front%20end
    @GET("remote-jobs")
    suspend fun searchJobsPosition(@Query("limit") limit: Int, @Query("search") query: String): RemoteJobsResponse

    @GET("users/{profile}")
    suspend fun getResponseJobsPositionPath(@Path("profile") profile: String): RemoteJobsResponse


}
