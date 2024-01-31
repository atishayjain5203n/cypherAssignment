package com.example.cypherassignment.model

data class FilesModel(
    val id: Long,
    val lastModified: Long,
    val title: String,
    val size: String,
    val type: FileType,
    val path: String
)
enum class FileType{
    PDF, DOC, ZIP
}
interface FileClickListener{
    fun onFileClick(filesModel: FilesModel)
}