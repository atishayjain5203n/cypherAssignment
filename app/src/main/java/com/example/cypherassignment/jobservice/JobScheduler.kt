package com.example.cypherassignment.jobservice

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class JobScheduler(private val context: Context) {
    fun scheduleJob(JOB_ID: Int) {
        val jobScheduler = context.getSystemService(
            AppCompatActivity.JOB_SCHEDULER_SERVICE
        ) as JobScheduler
        val name = ComponentName(context, ApiCallService::class.java)
        val result = jobScheduler.schedule(getJobInfo(JOB_ID, name))
        if (result == JobScheduler.RESULT_SUCCESS) {
            Log.d("ApiCallService", "Scheduled job successfully!")
        } else {
            Log.d("ApiCallService", "Scheduled job failed!")
        }
    }

    private fun getJobInfo(id: Int, name: ComponentName): JobInfo {
        val isPersistent = true
        val networkType = JobInfo.NETWORK_TYPE_ANY
        return JobInfo.Builder(id, name)
            .setPeriodic(5 * 60 * 1000)
            .setRequiredNetworkType(networkType)
            .setPersisted(isPersistent)
            .build()
    }
}