package org.one.tracking.app

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat

data class GeoPoint(val lat:Double, val lon:Double, val acc:Float)

class OTALocationListener:LocationListener {

    private val TAG = "LocationListener"
    private var mGeoPoint:GeoPoint? = null

    private var GPS_enabled:Boolean = false
    private var network_enabled:Boolean = false
    private var permissionGranted:Boolean = false

    private var manager: LocationManager? = null
    private var mContext:Context? = null

    var label:TextView? = null

    fun getGeoPoint():GeoPoint?{
        return mGeoPoint
    }

    fun isGPSEnabled():Boolean{
        return GPS_enabled
    }

    fun setUp(context: Context, textView:TextView? = null){
        this.mContext   = context
        this.manager    = mContext!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        this.label      = textView
        GPS_enabled     = manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        network_enabled = manager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        startListening()

    }

    fun startListening(){
        if(ContextCompat.checkSelfPermission(mContext!!,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED){

            this.manager?.requestLocationUpdates(LocationManager.GPS_PROVIDER,1.toLong(),1f,this)

            Log.d(TAG,"STARTED LISTENING")
            permissionGranted = true
        }
        else{
            Log.d(TAG,"PERMISSION DENIED")
            permissionGranted = false
        }
    }

    fun getPermissionGranted():Boolean {
        return this.permissionGranted
    }

    override fun onLocationChanged(location: Location?) {

        val latitude  = location?.latitude
        val longitude = location?.longitude
        val accuracy  = location?.accuracy

        val string = "latitude: " + latitude.toString() + "\nlongitude: " + longitude.toString() +  "\naccuracy: " + accuracy.toString()
        Log.v(TAG,string)
        this.label?.text = string

        if(latitude == null || longitude == null || accuracy == null){
            return
        }

        this.mGeoPoint = GeoPoint(latitude,longitude,accuracy)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.v(TAG,"status changed: " + provider + "\nstatus: " + status.toString())
    }

    override fun onProviderDisabled(provider: String?) {
        Log.v(TAG,"provider diabled: " +  provider)
    }

    override fun onProviderEnabled(provider: String?) {
        Log.v(TAG,"provider enabled: " +  provider)
    }
}