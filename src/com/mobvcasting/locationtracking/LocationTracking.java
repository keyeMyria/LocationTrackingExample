package com.mobvcasting.locationtracking;

import android.app.Activity; 
import android.content.Context; 
import android.location.Location; 
import android.location.LocationListener; 
import android.location.LocationManager; 
import android.location.LocationProvider; 
import android.os.Bundle; 
import android.util.Log; 
import android.widget.TextView;

/*
To receive location updates from the LocationManager, we'll have our activity implement LocationListener.
*/

public class LocationTracking extends Activity implements LocationListener {

	LocationManager lm; 
	TextView tv;

	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.main);
		tv = (TextView) this.findViewById(R.id.location); 
		
		/*
		We get an instance of LocationManager by using the getSystemService method available
		in Context, which Activity is a subclass of therefore it is available to us.
		*/
		lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		/*
		LocationManager offers us the ability to specify that we want our LocationListener, in this case, our activity, to be notified of location-related changes. We register our activity as the LocationListener by passing it in as the last argument to the requestLocationUpdates method.
		The first argument in the method is the location provider that we would like to use. The two location providers available are specified as constants in the LocationManager class. The one we are using here, NETWORK_PROVIDER, utilizes network services such as cell tower location or WiFi access point location to determine location. The other one available is GPS_PROVIDER, which provides location information utilizing GPS (Global Positioning Satellites). NETWORK_PROVIDER is generally a much faster but potentially less accurate location lookup than GPS. GPS may take a significant amount of time to acquire signals from satellites and may not work at all indoors or in areas where the sky is not clearly visible (midtown Manhattan, for instance).

		The second argument is the minimum amount of time the system will wait between "location changed" notifications. It is specified as a long representing milliseconds. Here we are using 60,000 milliseconds or 1 minute.
		The third argument is the amount of distance that the location needs to have changed before a "location changed" notification is given. This is specified as a float representing meters. Here we are using 5 meters.
		 */

		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000l, 5.0f, this);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000l, 5.0f, this);
	}

	/*
	When using the LocationManager, particularly when using GPS as the provider, it may be prudent to stop the location updates when the application is no longer in the foreground. This will conserve battery power. To do so, we can override the normal onPause or onStop method in our activity and call the removeUpdates method on the LocationManager object.
	 */
	public void onPause() {
		super.onPause(); 
		lm.removeUpdates(this);
	}

	public void onResume() {
		super.onResume();
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000l, 5.0f, this);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000l, 5.0f, this);
	}
	
	/*
	The onLocationChanged method will be called on the registered LocationListener and passed a Location object whenever the location has changed and the change is greater than the distance and time parameters specified in the requestLocationUpdates method.
	The Location object that is passed in has methods available for getting latitude (getLatitude), longitude (getLongitude), altitude (getAltitude), and many more, detailed in the documentation: http://developer.android.com/reference/android/location/Location.html.
	 */

	public void onLocationChanged(Location location) {
		Log.v("LOCATION", "Altitude " + location.getAltitude() + "Supported: " + location.hasAltitude());
		Log.v("LOCATION", "Bearing" + location.getBearing() + "Supported: " + location.hasBearing());
		tv.setText(location.getLatitude() + " " + location.getLongitude()); 
		Log.v("LOCATION", "onLocationChanged: lat=" + location.getLatitude() + ", lon=" + location.getLongitude());
	}

	/*
	The onProviderDisabled method within the registered LocationListener will get called should the provider that is being monitored be disabled by the user.
	*/
	public void onProviderDisabled(String provider) { 
		Log.v("LOCATION", "onProviderDisabled: " + provider);
	}

	/*
	The onProviderEnabled method within the registered LocationListener will get called
	should the provider that is being monitored be enabled by the user.
	*/
	public void onProviderEnabled(String provider) { 
		Log.v("LOCATION", "onProviderEnabled: " + provider);
	}

	/*
	Finally, the onStatusChanged method in the registered LocationListener will be called if the location provider's status changes. There are three constants in LocationProvider that can be tested against the status variable which can be usedto determine what the change that happened is. They are AVAILABLE, which will get called should the provider become available after a period of time being unavailable, TEMPORARILY_UNAVAILABLE, which is just as its name implies, the provider is temporarily unable to be used as it was
	unable to fetch the current location and lastly, OUT_OF_SERVICE, which means that the provider is unable to be used probably due to losing connectivity or signal.
	*/
	public void onStatusChanged(String provider, int status, Bundle extras) { 
		Log.v("LOCATION", "onStatusChanged: " + provider + " status:" + status); 
	
		if (status == LocationProvider.AVAILABLE) {
			Log.v("LOCATION","Provider Available"); 
		} else if (status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
			Log.v("LOCATION","Provider Temporarily Unavailable"); 
		} else if (status == LocationProvider.OUT_OF_SERVICE) {
			Log.v("LOCATION","Provider Out of Service");
		}
	}
}       
