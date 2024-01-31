package com.example.cypherassignment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cypherassignment.databinding.ActivityMainBinding
import com.example.cypherassignment.jobservice.JobScheduler
import com.example.cypherassignment.model.FileClickListener
import com.example.cypherassignment.model.FileType
import com.example.cypherassignment.model.FilesModel
import com.example.cypherassignment.ui.FilesListAdapter


private const val REQUEST_CODE = 123
private const val JOB_ID = 123

class MainActivity : AppCompatActivity(), FileClickListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvFilesList.adapter = FilesListAdapter(this, this)
        binding.rvFilesList.layoutManager = LinearLayoutManager(this)
        val jobScheduler = JobScheduler(this)
        jobScheduler.scheduleJob(JOB_ID)
        checkPermission()
    }

    private fun extractFiles() {
        var files: MutableList<FilesModel> = mutableListOf()
        val projections = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.SIZE,
        )
        val selection = "_data LIKE '%.pdf'  or _data LIKE '%.txt' or _data LIKE '%.zip'"
        applicationContext.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projections,
            selection,
            null,
            "date_added DESC"
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val idIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
                val titleIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE)
                val sizeIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE)
                val dateAddedIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)
                val displayIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val title = cursor.getString(titleIndex)
                val size = cursor.getString(sizeIndex)
                val id = cursor.getLong(idIndex)
                val date = cursor.getLong(dateAddedIndex)
                val displayName = cursor.getString(displayIndex)
                val path = cursor.getString(idIndex)
                var type = FileType.PDF
                if(displayName.endsWith(".txt"))
                    type = FileType.DOC
                else if(displayName.endsWith(".zip"))
                    type = FileType.ZIP
                files.add(FilesModel(id, date, title, size, type,path))

            }
        }
        files.sortByDescending { it.lastModified }
        (binding.rvFilesList.adapter as FilesListAdapter).submitList(files)
        binding.progressCircular.visibility= View.GONE

    }

    private fun checkPermission(
    ): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
                    alertBuilder.setCancelable(true)
                    alertBuilder.setTitle("Permission necessary")
                    alertBuilder.setMessage(" Read permission is necessary")
                    alertBuilder.setPositiveButton(android.R.string.yes,
                        DialogInterface.OnClickListener { dialog, which ->
                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf<String>(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                                REQUEST_CODE
                            )
                        })
                    val alert: AlertDialog = alertBuilder.create()
                    alert.show()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf<String>(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_CODE
                    )
                }
                false
            } else {
                extractFiles()
                true
            }
        } else {
            extractFiles()
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                extractFiles()
            } else {
                Toast.makeText(this, "Permission to view files denies", Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onFileClick(filesModel: FilesModel) {
        val fileUri = Uri.withAppendedPath(
            MediaStore.Files.getContentUri("external"),
            filesModel.path
        )
        var type = when(filesModel.type){
            FileType.ZIP -> "application/zip"
            FileType.DOC ->"text/plain"
            else -> "application/pdf"
        }
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(fileUri, type)
        try{
            startActivity(intent)
        }
        catch (e: Exception){
            Log.d("Error", e.toString())
            Toast.makeText(this, "File can't be opened", Toast.LENGTH_SHORT).show()
        }
    }
}