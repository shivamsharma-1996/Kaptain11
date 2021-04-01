package com.shivam.kaptain11.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shivam.kaptain11.R
import com.shivam.kaptain11.models.Output

class RecentEarningsAdapter(private val context: Context)
    : RecyclerView.Adapter<RecentEarningsAdapter.RecentEarningHolder>() {
    private val allWinnings = ArrayList<Output>();

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentEarningHolder {
        val view  = LayoutInflater.from(parent.context).inflate(
            R.layout.item_recent_earning,
            parent,
            false
        )
        val viewHolder = RecentEarningHolder(view);
        return viewHolder;
    }

    override fun onBindViewHolder(holder: RecentEarningHolder, position: Int) {
        val winningObject = allWinnings[position]
        holder.tvAmountWon.text = winningObject.amount.toString()
        /* val date: Date? = AppUtil.convertIsoDateTime(recentWinning.creation_date)
        val matchDate = SimpleDateFormat("MMM dd, yyyy").format(date)
        val matchTime = SimpleDateFormat("hh:mm a").format(date)

        holder.tvContestTiming.text  = "$matchDate|$matchTime"*/
    }


    override fun getItemCount(): Int {
        return allWinnings.size
    }

    fun updateData(winningList: List<Output>){
        allWinnings.clear()
        allWinnings.addAll(winningList)
        notifyDataSetChanged()
    }

    inner class RecentEarningHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvContestTiming: TextView = itemView.findViewById(R.id.tvContestTiming)
        val tvAmountWon : TextView = itemView.findViewById(R.id.tvAmountWon)
    }
}

