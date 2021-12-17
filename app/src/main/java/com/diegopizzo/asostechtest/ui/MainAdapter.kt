package com.diegopizzo.asostechtest.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diegopizzo.asostechtest.R
import com.diegopizzo.asostechtest.databinding.ItemRecyclerViewBinding
import com.diegopizzo.asostechtest.util.Util
import com.diegopizzo.network.base.isFalse
import com.diegopizzo.network.base.isTrue
import com.diegopizzo.network.model.LaunchDataModel

class MainAdapter(private val context: Context, private val onItemClick: (text: String?) -> Unit) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val launches: MutableList<LaunchDataModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemRecyclerViewBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = launches[position]
        val notAvailableLabel = context.getString(R.string.not_available)
        launch.apply {
            holder.itemBinding.mainItem.setItemContentView(
                patchImage,
                context.getString(R.string.mission_label),
                missionName,
                context.getString(R.string.date_time_label),
                getDateAndTime(dateTimeUtc),
                context.getString(R.string.rocket_label),
                context.getString(
                    R.string.rocket_value,
                    rocketName ?: notAvailableLabel,
                    rocketType ?: notAvailableLabel
                ),
                getDaysLabel(isUpcoming),
                Util.getRemainingDays(endDateUtc = dateTimeUtc).toString(),
                getSmallIcon(isSuccess)
            )
        }
        holder.itemBinding.mainItem.setOnClickListener {
            onItemClick.invoke(launch.wikipediaLink)
        }
    }

    private fun getDateAndTime(dateUtc: String): String {
        val date = Util.convertUtcDateTimeToLocalDate(dateUtc, datePattern = DATE_PATTERN)
        val time = Util.convertUtcDateTimeToLocalTime(dateUtc, timePattern = TIME_PATTERN)
        return context.getString(R.string.date_time_value, date, time)
    }

    private fun getDaysLabel(isUpcoming: Boolean): String {
        return if (isUpcoming) {
            val from = context.getString(R.string.from)
            context.getString(R.string.days_label, from)
        } else {
            val since = context.getString(R.string.since)
            context.getString(R.string.days_label, since)
        }
    }

    private fun getSmallIcon(isSuccess: Boolean?): Int? {
        return when {
            isSuccess.isTrue() -> {
                R.drawable.ic_check_black
            }
            isSuccess.isFalse() -> {
                R.drawable.ic_close_black
            }
            else -> null
        }
    }

    override fun getItemCount(): Int {
        return launches.size
    }

    fun addLaunches(list: List<LaunchDataModel>) {
        launches.clear()
        launches.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(val itemBinding: ItemRecyclerViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    companion object {
        const val DATE_PATTERN = "dd/MM/yyyy"
        const val TIME_PATTERN = "HH:mm"
    }
}

