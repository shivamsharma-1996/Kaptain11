package com.shivam.kaptain11.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shivam.kaptain11.R
import com.shivam.kaptain11.models.UserWiseWinningInfo

class UserWiseEarningsAdapter(private val context: Context) :
    RecyclerView.Adapter<UserWiseEarningsAdapter.UserWiseEarningHolder>() {
    private val userWiseEarningsList = ArrayList<UserWiseWinningInfo>();

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserWiseEarningHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_userwise_earnings,
            parent, false
        )
        val viewHolder = UserWiseEarningHolder(view);
        return viewHolder;
    }

    override fun onBindViewHolder(holder: UserWiseEarningHolder, position: Int) {
        val winning = userWiseEarningsList[position]
        holder.tvSerialNo.text = "#" + position+1
        holder.tvUserName.text = winning.follower_username
        holder.tvAmountGenerated.text = winning.earnings.toString()
    }


    override fun getItemCount(): Int {
        return userWiseEarningsList.size
    }

    fun updateData(userWiseEarningData: List<UserWiseWinningInfo>) {
        userWiseEarningsList.clear()
        userWiseEarningsList.addAll(userWiseEarningData)
        notifyDataSetChanged()
    }

    inner class UserWiseEarningHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSerialNo: TextView = itemView.findViewById(R.id.tvSerialNo);
        val tvUserName: TextView = itemView.findViewById(R.id.tvUserName);
        val tvAmountGenerated: TextView = itemView.findViewById(R.id.tvAmountGenerated);
    }
}

