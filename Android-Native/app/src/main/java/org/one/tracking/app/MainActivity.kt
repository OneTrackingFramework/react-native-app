package org.one.tracking.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

///This activity is used to send location data to the backend
class MainActivity : AppCompatActivity() {

    var geoListener:OTALocationListener = OTALocationListener()
    var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //start receiving geo locations
        geoListener.setUp(this,textview)
        geoListener.startListening()

        //in case permissions have not been granted ask user
        if(!geoListener.getPermissionGranted()){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
            geoListener.startListening()
        }

        // keep thread running to detect if signal is blocked
        thread {
            while (true){
                Thread.sleep(1000)
                 if(geoListener.isGPSEnabled() && geoListener.getGeoPoint() == null){
                     textview.text = "no gps signal received. Perhaps you are indoors"
                 }
            }
        }

    }
}
