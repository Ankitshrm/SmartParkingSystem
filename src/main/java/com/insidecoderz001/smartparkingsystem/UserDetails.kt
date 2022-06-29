package com.insidecoderz001.smartparkingsystem

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class UserDetails : AppCompatActivity() {

    lateinit var details_checkinDate:TextView
    lateinit var amountToBePay:TextView
    lateinit var details_checkoutDate:TextView
    lateinit var details_checkinTime:TextView
    lateinit var details_checkoutTime:TextView
    lateinit var details_wheels_no:EditText
    var checkInTime:String=""
    var checkOutTime:String=""
    var checkInDate:String=""
    var checkOutDate:String=""
    lateinit var Pay:TextView
    lateinit var details_name:EditText
    lateinit var details_phone_no:EditText
    lateinit var details_choose_wheelers:TextView
    lateinit var wheelersStatus:String
    var  ans =0


    private lateinit var database :DatabaseReference

    companion object {
        const val W2 = "two wheel"
        const val W4 = "four wheel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        ids()
        date_time()

        details_choose_wheelers.setOnClickListener {
            val popupMenu = PopupMenu(this,details_choose_wheelers)
            popupMenu.menuInflater.inflate(R.menu.wheelers_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.twoW -> {
                        details_choose_wheelers.text = item.title.toString()
                    }
                    R.id.fourW ->{
                        details_choose_wheelers.text=item.title.toString()
                        }
                }
                true
            })
            popupMenu.show()
        }
        wheelersStatus = details_choose_wheelers.text.toString()

        val temp =amountToBePay.text.toString()


        amountToBePay.setOnClickListener {

            val username = details_name.text.toString()
            val carNum = details_wheels_no.text.toString()
            val phoneNum = details_phone_no.text.toString()
            val checkOT =checkOutTime
            val checkIT =checkInTime
            val checkOD =checkOutDate
            val checkID =checkInDate
            wheelersStatus = details_choose_wheelers.text.toString()
            val statusWheeler =wheelersStatus

            if (username == "" || carNum == "" || phoneNum == ""||checkOD==""||checkID==""||checkOT==""||checkIT==""||statusWheeler=="") {
                Toast.makeText(this,"All entries are required: ",Toast.LENGTH_SHORT).show()

            } else {
                val aya :String = "$checkInDate $checkInTime:00"
                val gya :String = "$checkOutDate $checkOutTime:00"
                val sdf = SimpleDateFormat(
                    "dd:MM:yyyy HH:mm:ss")

                try {
                    val d1 = sdf.parse(aya)
                    val d2 = sdf.parse(gya)
                    val difference_In_Time = d2!!.time - d1!!.time
                    val difference_In_Hours = ((difference_In_Time / (1000 * 60 * 60)) % 24)
                    val difference_In_Days = ((difference_In_Time / (1000 * 60 * 60 * 24)) % 365)

                    var w2 =intent.getStringExtra(W2)
                    val w4 =intent.getStringExtra(W4)

                    var wlr2 = w2!!.toInt()
                    var wlr4 = w4!!.toInt()


                    if(statusWheeler.equals("2-Wheelers")){
                        wlr2=(difference_In_Hours.times(wlr2).toInt() + difference_In_Days.times(wlr2*24).toInt())
                        ans=wlr2
                        amountToBePay.text ="Amount to be pay is Rs. $wlr2"
                    }else if(statusWheeler.equals("4-Wheelers")){
                        wlr4=(difference_In_Hours.times(wlr4).toInt() + difference_In_Days.times(wlr4*24).toInt())
                        ans=wlr4
                        amountToBePay.text ="Amount to be pay is Rs. $wlr4"
                    }else{
                        Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
                    }

                }
                catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
        }

        Pay.setOnClickListener {

            if(amountToBePay.length()==35){
                Toast.makeText(this,"All entries are required: ",Toast.LENGTH_SHORT).show()
            }else {

                var rand:Random = Random()
                var bid:String = (rand.nextInt(999999-100000)+100000).toString()

                val bidId = bid
                val checkOT =checkOutTime
                val checkIT =checkInTime
                val checkOD =checkOutDate
                val checkID =checkInDate
                val amount =ans.toString()

                database = FirebaseDatabase.getInstance().getReference("BookingDetails")
                val Booking =BookingHistory(bidId,checkOT,checkIT,checkOD,checkID,amount)
                database.child(bidId).setValue(Booking).addOnSuccessListener {
                        Toast.makeText(this,"Your booking Id is $bidId",Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(this,"Something went wrong xd",Toast.LENGTH_SHORT).show()
                }


                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun ids() {
        details_checkinDate=findViewById(R.id.details_checkinDate)
        details_checkoutDate=findViewById(R.id.details_checkoutDate)
        details_checkinTime=findViewById(R.id.details_checkinTime)
        details_checkoutTime=findViewById(R.id.details_checkoutTime)
        details_wheels_no=findViewById(R.id.details_wheels_no)
        details_name=findViewById(R.id.details_name)
        details_phone_no=findViewById(R.id.details_phone_no)
        details_choose_wheelers=findViewById(R.id.details_choose_wheelers)
        Pay=findViewById(R.id.Pay)
        amountToBePay=findViewById(R.id.amountToBePay)
    }

    private fun date_time() {
        details_checkoutDate.setOnClickListener {
            checkoutDatePick()
        }
        details_checkinTime.setOnClickListener {
            checkinTimePick()
        }
        details_checkoutTime.setOnClickListener {
            checkoutTimePick()
        }
        details_checkinDate.setOnClickListener {
            checkingDatePick()
        }
    }

    private fun checkoutTimePick() {
        val c=Calendar.getInstance()
            val ts =TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                c.set(Calendar.HOUR_OF_DAY,hour)
                c.set(Calendar.MINUTE,minute)
                details_checkoutTime.text =SimpleDateFormat("HH:mm").format(c.time)
                checkOutTime=details_checkoutTime.text.toString()
            }
            TimePickerDialog(this,ts,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show()
    }
    private fun checkinTimePick() {
        val c=Calendar.getInstance()
            val ts =TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                c.set(Calendar.HOUR_OF_DAY,hour)
                c.set(Calendar.MINUTE,minute)
                details_checkinTime.text =SimpleDateFormat("HH:mm").format(c.time)
                checkInTime=details_checkinTime.text.toString()
            }
            TimePickerDialog(this,ts,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show()
    }
    private fun checkoutDatePick() {

        val c =Calendar.getInstance()
        val year =c.get(Calendar.YEAR)
        val month =c.get(Calendar.MONTH)
        val day =c.get(Calendar.DAY_OF_MONTH)
            val dt =DatePickerDialog(this,DatePickerDialog.OnDateSetListener { view, year, month, day ->
                details_checkoutDate.text= "$day:$month:$year"
                checkOutDate=details_checkoutDate.text.toString()
            },year,month,day)
            dt.show()
    }
    private fun checkingDatePick() {
        val c =Calendar.getInstance()
        val year =c.get(Calendar.YEAR)
        val month =c.get(Calendar.MONTH)
        val day =c.get(Calendar.DAY_OF_MONTH)
            val dt =DatePickerDialog(this,DatePickerDialog.OnDateSetListener { view, year, month, day ->
                details_checkinDate.text = "$day:$month:$year"
                checkInDate=details_checkinDate.text.toString()
            },year,month,day)
            dt.show()
    }
}