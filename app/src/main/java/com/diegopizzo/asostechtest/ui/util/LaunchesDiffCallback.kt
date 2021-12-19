package com.diegopizzo.asostechtest.ui.util

import androidx.recyclerview.widget.DiffUtil
import com.diegopizzo.network.model.LaunchDataModel

class LaunchesDiffCallback : DiffUtil.ItemCallback<LaunchDataModel>() {
    override fun areItemsTheSame(oldItem: LaunchDataModel, newItem: LaunchDataModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LaunchDataModel, newItem: LaunchDataModel): Boolean {
        return oldItem == newItem
    }
}