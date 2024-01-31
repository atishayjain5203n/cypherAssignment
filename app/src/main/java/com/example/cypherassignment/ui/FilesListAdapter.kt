package com.example.cypherassignment.ui

import com.example.cypherassignment.R
import com.example.cypherassignment.model.FilesModel
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cypherassignment.databinding.ItemRvFileBinding
import com.example.cypherassignment.model.FileClickListener
import com.example.cypherassignment.model.FileType

class FilesListAdapter(val context: Context, val clickListener: FileClickListener) :
    ListAdapter<FilesModel, FilesListAdapter.FileListViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRvFileBinding.inflate(inflater)
        return FileListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileListViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    inner class FileListViewHolder(private val binding: ItemRvFileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(filesModel: FilesModel) {
            binding.root.setOnClickListener {
                clickListener.onFileClick(filesModel)
            }
            binding.tvTitle.text = filesModel.title
            binding.tvSize.text = "${(filesModel.size).toLong() / (1024 * 1024)} MB"
            if (filesModel.type == FileType.PDF) Glide.with(context)
                .load(R.drawable.baseline_picture_as_pdf_24).into(binding.ivIcon)
            else if (filesModel.type == FileType.ZIP) Glide.with(context)
                .load(R.drawable.baseline_folder_zip_24).into(binding.ivIcon)
            else Glide.with(context).load(R.drawable.baseline_text_snippet_24).into(binding.ivIcon)
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<FilesModel>() {
        override fun areItemsTheSame(oldItem: FilesModel, newItem: FilesModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FilesModel, newItem: FilesModel): Boolean {
            return oldItem.id == newItem.id
        }
    }
}