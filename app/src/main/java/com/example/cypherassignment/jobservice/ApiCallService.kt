package com.example.cypherassignment.jobservice

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.example.cypherassignment.model.RegisterDetails
import com.example.cypherassignment.network.Api
import com.example.cypherassignment.network.RetrofitBuilder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApiCallService : JobService() {
    private val TAG = "ApiCallService"
    override fun onStartJob(params: JobParameters?): Boolean {
        val handlerThread = HandlerThread("ApiThread")
        handlerThread.start()

        val handler = Handler(handlerThread.looper)
        handler.post {
            callApi()
            jobFinished(params, true)
        }

        return true
    }
    private fun callApi(){
        Log.d(TAG, "Api call started")
        val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
            throwable.printStackTrace()
        }
        CoroutineScope(Dispatchers.IO+coroutineExceptionHandler).launch {
            val response = RetrofitBuilder.retrofit!!
                .create(Api::class.java).register(
                    RegisterDetails(nodeId = "9837724914")
                )
            if (response.isSuccessful) {
                Log.d(TAG, response.toString())
                Log.d(TAG, "Api call successful")

            } else {
                Log.d(TAG, "Api call failed")
            }
        }
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "on stop called")
        return true
    }
}