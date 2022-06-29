package com.insidecoderz001.smartparkingsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class AvailablilityCheck : AppCompatActivity() {

    companion object{
        const val STRING_EXTRA = "markerTitle"

        val IMA_ADDRESS ="19, Chakrata Rd, Phase 2, Indian Military Academy, Dehradun, Uttarakhand 248007"
        var IMA_AS=7
        var IMA_OS=2
        var IMA_W2 =25
        var IMA_W4=50

        val PN_ADD="Prem Nagar, Dehradun, Uttarakhand"
        var PN_AS=7
        var PN_OS=4
        var PN_W2 =20
        var PN_W4=40

        val SN_ADD="Subhash Nagar, Dehradun, Uttarakhand"
        var SN_AS=9
        var SN_OS=3
        var SN_W2=15
        var SN_W4=35

        val DU_ADD="Mothorowala Rd, Doon University Campus, Kedarpur, Uttarakhand 248001"
        var DU_AS=17
        var DU_OS=5
        var DU_W2=33
        var DU_W4=55

        val CT_ADD="Clement Town, Dehradun, Uttarakhand 248002"
        var CT_AS=9
        var CT_OS=4
        var CT_W2=25
        var CT_W4=45

        val RR_ADD="Rajpur Road, Dehradun, Uttarakhand"
        var RR_AS=18
        var RR_OS=12
        var RR_W2=35
        var RR_W4=60
    }

    lateinit var areaImg:ImageView
    lateinit var BookingDetails:TextView
    lateinit var LocName:TextView
    lateinit var LocAdd:TextView
    lateinit var availableValue:TextView
    lateinit var OccupiedValue:TextView
    lateinit var car2Value:TextView
    lateinit var car4Value:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_availablility_check)

        ids()

        val name =intent.getStringExtra(STRING_EXTRA)
        LocName.text =name
        when(LocName.text){
            "IMA" -> {
                LocAdd.text= IMA_ADDRESS
                availableValue.text= IMA_AS.toString()
                OccupiedValue.text= IMA_OS.toString()
                car2Value.text= IMA_W2.toString()
                car4Value.text= IMA_W4.toString()
                areaImg.setImageResource(R.drawable.ima_img)
            }
            "Prem Nagar" -> {
                LocAdd.text= PN_ADD
                availableValue.text= PN_AS.toString()
                OccupiedValue.text= PN_OS.toString()
                car2Value.text= PN_W2.toString()
                car4Value.text= PN_W4.toString()
                areaImg.setImageResource(R.drawable.pn)
            }
            "Subhash Nagar"->{
                LocAdd.text= SN_ADD
                availableValue.text= SN_AS.toString()
                OccupiedValue.text= SN_OS.toString()
                car2Value.text= SN_W2.toString()
                car4Value.text= SN_W4.toString()
                areaImg.setImageResource(R.drawable.sn)

            }
            "Clement Town" ->{
                LocAdd.text= CT_ADD
                availableValue.text= CT_AS.toString()
                OccupiedValue.text= CT_OS.toString()
                car2Value.text= CT_W2.toString()
                car4Value.text= CT_W4.toString()
                areaImg.setImageResource(R.drawable.ct_img)

            }
            "Doon University"->{
                LocAdd.text= DU_ADD
                availableValue.text= DU_AS.toString()
                OccupiedValue.text= DU_OS.toString()
                car2Value.text= DU_W2.toString()
                car4Value.text= DU_W4.toString()
                areaImg.setImageResource(R.drawable.du)

            }
            "Rajpur Road"->{
                LocAdd.text= RR_ADD
                availableValue.text= RR_AS.toString()
                OccupiedValue.text= RR_OS.toString()
                car2Value.text= RR_W2.toString()
                car4Value.text= RR_W4.toString()
                areaImg.setImageResource(R.drawable.rajpur)
            }
        }
        BookingDetails.setOnClickListener{
            val intent =Intent(this,UserDetails::class.java)
            intent.putExtra(UserDetails.W2,car2Value.text)
            intent.putExtra(UserDetails.W4,car4Value.text)
            startActivity(intent)
        }
    }

    private fun ids() {
        areaImg=findViewById(R.id.areaImg)
        BookingDetails=findViewById(R.id.BookingDetails)
        LocName=findViewById(R.id.LocName)
        LocAdd=findViewById(R.id.LocAdd)
        availableValue=findViewById(R.id.availableValue)
        OccupiedValue=findViewById(R.id.OccupiedValue)
        car2Value=findViewById(R.id.car2Value)
        car4Value=findViewById(R.id.car4Value)
    }
}