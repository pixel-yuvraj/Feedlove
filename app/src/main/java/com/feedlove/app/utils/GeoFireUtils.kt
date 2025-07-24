package com.feedlove.app.utils

import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation

object GeoFireUtils {

    fun getGeoHash(latitude: Double, longitude: Double): String {
        val location = GeoLocation(latitude, longitude)
        return GeoFireUtils.getGeoHashForLocation(location)
    }
}
