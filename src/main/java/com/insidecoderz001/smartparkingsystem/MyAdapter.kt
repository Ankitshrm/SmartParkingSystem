package com.insidecoderz001.smartparkingsystem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val c: Context, val billList:ArrayList<BookingHistory>):RecyclerView.Adapter<MyAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val booking =billList[position]
        holder.bookingID.text = booking.bookId
        holder.ciDate.text = booking.checkInDate
        holder.ciTime.text = booking.checkInTime
        holder.coDate.text = booking.checkOutDate
        holder.coTime.text = booking.checkOutTime
        holder.amount.text= booking.amount
    }

    override fun getItemCount(): Int {
        return billList.size
    }

    inner class ViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){
        var bookingID:TextView
        var ciDate:TextView
        var ciTime:TextView
        var coTime:TextView
        var coDate:TextView
        var amount:TextView

        init {
            bookingID =itemView.findViewById(R.id.bookingID)
            ciDate =itemView.findViewById(R.id.ciDate)
            ciTime =itemView.findViewById(R.id.ciTime)
            coTime =itemView.findViewById(R.id.coTime)
            coDate =itemView.findViewById(R.id.coDate)
            amount =itemView.findViewById(R.id.amount)
        }
    }
}