package com.insidecoderz001.smartparkingsystem

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.insidecoderz001.smartparkingsystem.MainActivity.Companion.DEFAULT_ZOOM
import com.insidecoderz001.smartparkingsystem.MainActivity.Companion.REQUESTCODE_LOCATION
import java.io.*
import java.util.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    companion object{
        val REQUESTCODE_LOCATION = 110
        val REQUESTCODE_LOCATION_ALERT=999
        val  DEFAULT_ZOOM:Float =17.0f

        val ISBT_lat:Double = 30.28908097375941
        val ISBT_lng:Double = 77.99803071183182

        val SubhashNagar_lat:Double = 30.278035553365182
        val SubhashNagar_lng:Double =  77.99247081118064

        val DU_lat:Double = 30.26920090472671
        val DU_lng:Double = 78.04425516885182

        val RajpurRoad_lat:Double = 30.384807448256456
        val RajpurRoad_lng:Double = 78.0896581467784

        val PremNagar_lat:Double = 30.333742308176802
        val PremNagar_lng:Double = 77.95853860329211

        val IMA_lat:Double = 30.325068788503053
        val IMA_lng:Double = 77.97628998878854

        val Clement_lat:Double = 30.270563512218292
        val Clement_lng:Double = 78.00899504368768

    }

    lateinit var ETsearchLocations :SearchView
    lateinit var payCard_txt:TextView
    lateinit var txt_info:TextView
    lateinit var gudmrng:TextView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    lateinit var bottomSheet:ConstraintLayout
    lateinit var expand_collapse:RelativeLayout
    lateinit var searchIcon:ImageView
    lateinit var mapView :CardView
    lateinit var payCard2 :CardView
    lateinit var linearLayout:LinearLayout
    lateinit var backToPrev:ImageView
    lateinit var arrow:ImageView
    lateinit var bookCard :CardView
    lateinit var myLocation :CardView
    private var mLocationPermissionGranted :Boolean =false
     lateinit var mMap: GoogleMap
    lateinit var ETsearchLocation :EditText
    lateinit var locationManager :LocationManager
    lateinit var myLocationClient: FusedLocationProviderClient
    lateinit var distance : String
    lateinit var markerTitle:String
    lateinit var yourBooking:CardView

//    private lateinit var mMap :MapView

    lateinit var locName : List<String>
    lateinit var locLatLon : List<LatLng>

    private var targetLat : Double = 30.384807448256456
    private var targetLng :Double =78.0896581467784


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ids()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        initMap()
        bottomsheetPanel()
        maptypeChange()
        ETsearchLocations.setOnClickListener(this::geoLocate)
        findMyLocation()

        myLocationClient = FusedLocationProviderClient( this)


        yourBooking.setOnClickListener {
            startActivity(Intent(this,BookingDashboard::class.java))
        }

        bookCard.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

            backToPrev.visibility=View.VISIBLE
            arrow.visibility=View.GONE
            linearLayout.visibility=View.GONE
//            payCard_txt.text ="Check for availability"
            txt_info.text="Click on the location marker where you want to find the space and then click on Check for availability.\n\n"
            bookCard.visibility=View.GONE
            payCard2.visibility=View.VISIBLE
            gudmrng.visibility=View.INVISIBLE

            backToPrev.setOnClickListener {
                txt_info.text="What do you want to do today?"
                backToPrev.visibility=View.INVISIBLE
                arrow.visibility=View.VISIBLE
                linearLayout.visibility=View.VISIBLE
                bookCard.visibility=View.VISIBLE
                gudmrng.visibility=View.VISIBLE
                payCard2.visibility=View.INVISIBLE
            }

            payCard2.setOnClickListener {
                val intent  = Intent(this,AvailablilityCheck::class.java)
                intent.putExtra(AvailablilityCheck.STRING_EXTRA,markerTitle)
                startActivity(intent)
            }
            BookParking()
        }

//        mMap=findViewById(R.id.map)
//        mMap.onCreate(savedInstanceState)
//        mMap.getMapAsync(this)
//        mMap.mapType=GoogleMap.MAP_TYPE_SATELLITE

    }



    private fun gpsEnable():Boolean{
         locationManager  =getSystemService(LOCATION_SERVICE) as LocationManager
        val providerEnabled :Boolean =locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (providerEnabled) return true
        else{
            val alertDialog : AlertDialog? = AlertDialog.Builder(this)
                .setTitle("GPS permission")
                .setMessage("Please enable the gps")
                .setPositiveButton("Yes") { dialogInterface, i ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(intent, REQUESTCODE_LOCATION_ALERT)
                }.setCancelable(false)
                .show()

        }
        return false
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUESTCODE_LOCATION_ALERT)
        locationManager  =getSystemService(LOCATION_SERVICE) as LocationManager
        val providerEnabled :Boolean =locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (providerEnabled){
                Toast.makeText(this,"GPS is enabled successfully",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"GPS enabled is failed",Toast.LENGTH_SHORT).show()

        }
    }
    private fun geoLocate(view :View) {
        hideSoftKeyboard(view)
        val locationName =ETsearchLocations.query.toString()
        val geocoder :Geocoder= Geocoder(this, Locale.getDefault())
        try {
            val addressList :List<Address> =geocoder.getFromLocationName(locationName,1)
            if (addressList.size>0){
                val address  :Address = addressList[0]
                gotoLoaction(address.latitude,address.longitude)
                mMap.addMarker(MarkerOptions().position(LatLng(address.latitude,address.longitude)).title(address.toString()))
            }
        }catch(e:Exception){
            e.printStackTrace()
        }
    }
    private fun hideSoftKeyboard(view: View) {
        val imm :InputMethodManager =getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)
    }
    private fun ParkingAreasList(){
        locName = listOf<String>("ISBT","Subhash Nagar","Doon University","Rajpur Road","Prem Nagar","IMA", "Clement Town","Clock Tower")
        locLatLon = listOf<LatLng>(LatLng(ISBT_lat, ISBT_lng),LatLng(SubhashNagar_lat,
            SubhashNagar_lng), LatLng(DU_lat, DU_lng),
            LatLng(RajpurRoad_lat, RajpurRoad_lng), LatLng(PremNagar_lat, PremNagar_lng),
            LatLng(IMA_lat, IMA_lng), LatLng(Clement_lat, Clement_lng))
        showMarker(locLatLon,locName)
    }

    private fun showMarker(loc:List<LatLng>, locName: List<String>){
        for (i in 1 until loc.size) {
            mMap.addMarker(MarkerOptions().position(loc[i]).title(locName[i]))
        }
    }
    private fun removeMarker(loc:List<LatLng>, locName: List<String>){
        for (i in 1 until loc.size){
            removeMarker(loc,locName)
        }
    }
    private fun findMyLocation() {
        myLocation.setOnClickListener {

            if (checkLocationPermission()) {
                myLocationClient.lastLocation.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val location =task.result
                        gotoLoaction(location.latitude,location.longitude)
                                mMap.addMarker(MarkerOptions().position(LatLng(location.latitude,location.longitude)).title("My Location"))
                    } else {
//                        Toast.makeText(this,"Something went wrong \n Unable to find devices location",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
    private fun BookParking() {
        ParkingAreasList()

            if (checkLocationPermission()) {
                myLocationClient.lastLocation.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val location = task.result
                        gotoLoaction(location.latitude, location.longitude)
                        mMap.addMarker(MarkerOptions().position(LatLng(location.latitude,
                            location.longitude)).title("My Location"))

                        if (mMap!=null) {
                            val bottomBound:Double =location.latitude-0.102
                            val LeftBound:Double =location.longitude-0.102
                            val topBound:Double =location.latitude+0.102
                            val rightBound:Double = location.longitude+0.102

                            val bounda  =LatLngBounds(LatLng(bottomBound,LeftBound),LatLng(topBound,rightBound))
                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounda,1))
                            (bounda.center)
                        }
                        } else { }
                }
        }
    }
    override fun onMarkerClick(marker: Marker): Boolean {
        targetLat = marker.position.latitude
        targetLng = marker.position.longitude
        markerTitle = marker.title.toString()
        return false
    }

    private fun maptypeChange() {
        mapView.setOnClickListener {
            val popupMenu = PopupMenu(this,mapView)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.none_mapView ->{
                        mMap.mapType=GoogleMap.MAP_TYPE_NONE
                    }
                    R.id.normal_mapView ->{
                        mMap.mapType=GoogleMap.MAP_TYPE_NORMAL
                    }
                    R.id.hybrid_mapView ->{
                        mMap.mapType=GoogleMap.MAP_TYPE_HYBRID
                    }
                    R.id.satellite_mapView->{
                        mMap.mapType=GoogleMap.MAP_TYPE_SATELLITE
                    }
                    R.id.terrain_mapView ->{
                        mMap.mapType=GoogleMap.MAP_TYPE_TERRAIN
                    }   }
                true
            })
            popupMenu.show()
        }

    }
    private fun bottomsheetPanel() {
        expand_collapse.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            else
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
    private fun ids() {
        backToPrev=findViewById(R.id.backToPrev)
        linearLayout=findViewById(R.id.linearLayout)
        payCard2=findViewById(R.id.payCard2)
        bottomSheet=findViewById(R.id.bottomSheet)
        expand_collapse=findViewById(R.id.expand_collapse)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        mapView= findViewById(R.id.maptype)
        arrow=findViewById(R.id.arrow)
        gudmrng=findViewById(R.id.gudmrng)
        txt_info=findViewById(R.id.txt_info)
        myLocation=findViewById(R.id.myLocation)
        searchIcon=findViewById(R.id.searchIcon)
        ETsearchLocation=findViewById(R.id.ETsearchLocation)
        yourBooking=findViewById(R.id.yourBooking)
        bookCard=findViewById(R.id.BookCard)
        payCard_txt=findViewById(R.id.payCard_txt)
        ETsearchLocations=findViewById(R.id.ETsearchLocations)
    }
    private fun gotoLoaction(lat: Double, lng: Double){
        val targetLocation = LatLng(lat,lng)
        val cameraUpdate : CameraUpdate = CameraUpdateFactory.newLatLngZoom(targetLocation,DEFAULT_ZOOM)
        mMap.animateCamera(cameraUpdate,3000,object :GoogleMap.CancelableCallback{
            override fun onCancel() {
//                Toast.makeText(this@MainActivity,"Unable to locate the location",Toast.LENGTH_SHORT).show()
            }

            override fun onFinish() {

            }

        })
        mMap.uiSettings.isMapToolbarEnabled=true

    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap =googleMap
        mMap.setOnMarkerClickListener(this as OnMarkerClickListener)
    }
    private fun initMap() {
        if(okServiceOk()){
            if (gpsEnable()) {
                if (checkLocationPermission()) {

                } else {
                    requestLoactionPermission()
                }
            }
        }
    }

    private fun okServiceOk():Boolean {

        val gaa : GoogleApiAvailability = GoogleApiAvailability.getInstance()

        val result = gaa.isGooglePlayServicesAvailable(this)

        if(result== ConnectionResult.SUCCESS){
            return true
        }else if(gaa.isUserResolvableError(result)){
            Toast.makeText(this,"Resolvable error", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    private fun checkLocationPermission(): Boolean {
        return  ContextCompat.checkSelfPermission(
            this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==110 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted=true
            Toast.makeText(this,"Permission granted successfully", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"Permission granted failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestLoactionPermission() {
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@MainActivity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUESTCODE_LOCATION)
        }
    }

}
