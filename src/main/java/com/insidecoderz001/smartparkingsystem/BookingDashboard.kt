package com.insidecoderz001.smartparkingsystem

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.text.SimpleDateFormat
import java.util.*


class BookingDashboard : AppCompatActivity() {

    private lateinit var myRecyclerview:RecyclerView
    private lateinit var database :DatabaseReference
    private lateinit var myArrayList:ArrayList<BookingHistory>
    private lateinit var myAdapter: MyAdapter

    companion object{
        const val phoneNum = ""
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_dashboard)

        val myNum:String? =intent.getStringExtra(phoneNum)



        myRecyclerview =findViewById(R.id.rvBill)
        database =FirebaseDatabase.getInstance().getReference("mybooking")
        myRecyclerview.setHasFixedSize(true)
        myRecyclerview.layoutManager =LinearLayoutManager(this)

        val sdf :SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val curr =sdf.format(Date())


        myArrayList=ArrayList()
        myAdapter=MyAdapter(this,myArrayList)
        myRecyclerview.adapter = myAdapter

        database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot in snapshot.children) {
                    val book:BookingHistory?  = datasnapshot.getValue(BookingHistory::class.java)
                    myArrayList.add(book!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BookingDashboard,"Something went wrong",Toast.LENGTH_SHORT).show()
            }

        })
        database.orderByChild("newDate").startAt(curr)


    }

}