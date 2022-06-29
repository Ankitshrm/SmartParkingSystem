package com.insidecoderz001.smartparkingsystem

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.*


class BookingDashboard : AppCompatActivity() {

    private lateinit var myRecyclerview:RecyclerView
    private lateinit var database :DatabaseReference
    private lateinit var myArrayList:ArrayList<BookingHistory>
    private lateinit var myAdapter: MyAdapter
    lateinit var close:ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_dashboard)

        myRecyclerview =findViewById(R.id.rvBill)
        close =findViewById(R.id.close)
        database =FirebaseDatabase.getInstance().getReference("BookingDetails")
        myRecyclerview.setHasFixedSize(true)
        myRecyclerview.layoutManager =LinearLayoutManager(this)

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


        close.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }





    }

}