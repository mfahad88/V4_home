package com.psl.fantasy.league.season2;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import com.psl.fantasy.league.season2.R;;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.psl.classes.Config;
import com.psl.classes.DatabaseHandler;
import com.psl.classes.DirectionsJSONParser;
import com.psl.classes.GPSTracker;
import com.psl.classes.JsLocationsVO;
import com.psl.classes.XMLParser;
import com.psl.transport.Connection;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;


/*import com.google.android.gms.location.LocationListener;*/

/**
 * Created by YAQOOB on 16/01/8.
 */

public class AgentLocator extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener {

    Polyline polyline = null;
    private GoogleMap mMap;
    private String provider;

    LatLng latLng;
    SupportMapFragment mMapView;
    private GoogleMap googleMap;
    DatabaseHandler dbHandler;
    double mLatitude = 0;
    double mLongitude = 0;
    View view;
    List<JsLocationsVO> distanceList;
    LatLng latlng1;
    Activity activity;
    boolean IS_ANIMATED = false;
    SharedPreferences sharedPreferences;
    String username;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    ProgressBar pBar;
    //FollowMeLocationSource followMeLocationSource;

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;

    Location location;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            view = inflater.inflate(R.layout.fragment_js_locations, container, false);
            RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.top_layout);
            CardView map_parent_layout = (CardView) view.findViewById(R.id.roun_corners_layout);
            pBar = (ProgressBar) view.findViewById(R.id.locator_progressbar);
            pBar.bringToFront();
            pBar.setVisibility(View.GONE);
            layout.bringToFront();
            layout.invalidate();
            map_parent_layout.invalidate();
            sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
            TextView tv_username = (TextView) view.findViewById(R.id.tv_username);
            username = sharedPreferences.getString(Config.FIRST_NAME, "");
            if (username.equals(""))
                username = sharedPreferences.getString(Config.NAME, "");

            tv_username.setText(username);

            dbHandler = new DatabaseHandler(getActivity());
            distanceList = new ArrayList<JsLocationsVO>();
            checkLocationPermission();
            locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

            Criteria criteria = new Criteria();

            ((ImageView) view.findViewById(R.id.spinner_city)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        GPSTracker gps = new GPSTracker(getActivity(), getActivity());
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        if (latitude == 0.0) {
                            latitude = 33.6844;
                            longitude = 73.0479;
                            Toast.makeText(getActivity(), "Current location coordinates", Toast.LENGTH_LONG).show();
                        }
                        new GetLocatorsAsync().execute(String.valueOf(latitude), String.valueOf(longitude));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            if (!isLocationEnabled())
                showAlert();

            ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);



            //mMapView.onResume(); // needed to get the map to display immediately

            //mMapView.getMapAsync(this);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mGoogleMap = googleMap;
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            boolean is_done = false;
            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker marker) {
                   String snip= marker.getSnippet();
                   final String mobile=snip.substring(snip.indexOf("("),snip.indexOf(")")).replace("-","").replace("(","").replace(")","").trim();

                   AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
                   adb.setMessage("Do you want to call JS Representative ("+mobile+") ?");
                   adb.setCancelable(false);
                   adb.setPositiveButton("Call", new DialogInterface.OnClickListener() {

                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           if (ContextCompat.checkSelfPermission(getActivity(),
                                   Manifest.permission.CALL_PHONE)
                                   == PackageManager.PERMISSION_GRANTED) {
                               Intent intent = new Intent(Intent.ACTION_DIAL);
                               intent.setData(Uri.parse("tel:" + mobile));
                               startActivity(intent);
                           }
                           else
                           {

                               try {
                                   //Prompt the user once explanation has been shown
                                   ActivityCompat.requestPermissions(getActivity(),
                                           new String[]{Manifest.permission.CALL_PHONE},
                                           100);
                               } catch (Exception e) {
                                   e.printStackTrace();
                               }
                           }
                       }
                   });
                   adb.setNegativeButton("Cancel",null);
                   adb.show();
                   //Toast.makeText(getActivity(),mobile,1000).show();
                }
            });

            //Initialize Google Play Services
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    //Location Permission already granted
                    buildGoogleApiClient();
                    mGoogleMap.setMyLocationEnabled(true);
                    moveCameraToCurrentLocation();
                    //getCurrentLocationAndDrawMarkers();
                } else {
                    //Request Location Permission
                    checkLocationPermission();
                }
            } else {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
                moveCameraToCurrentLocation();
                //getCurrentLocationAndDrawMarkers();
            }

            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    LinearLayout info = new LinearLayout(getActivity());
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(getActivity());
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(getActivity());
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(500);
            mLocationRequest.setFastestInterval(500);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //getCurrentLocationAndDrawMarkers();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            //Place current location marker
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            latlng1 = latLng;
            // mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

            //move map camera
            // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));

            if (!IS_ANIMATED) {
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title(username));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latLng.latitude, latLng.longitude)));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                IS_ANIMATED = true;
            }
            /*List<JsLocationsVO> list = dbHandler.getJsLocations();

            if (pBar != null)
                pBar.setVisibility(View.GONE);

            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    double distance = getDistance(latLng.latitude, latLng.longitude,
                            Double.parseDouble(list.get(i).getLatitude()), Double.parseDouble(list.get(i).getLongitude()));
                    if (distance < 5) {
                        createMarker(Double.parseDouble(list.get(i).getLatitude()), Double.parseDouble(list.get(i).getLongitude()), list.get(i).getLoc_name(), list.get(i).getAddress());
                    }
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        try {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

                Criteria criteria = new Criteria();
                provider = locationManager.getBestProvider(criteria, false);

                Location location = locationManager.getLastKnownLocation(provider);
                // location.getLatitude();
                //location.getLongitude();
                String aa = "";
                //Toast.makeText(getActivity(), "" + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    protected Marker createMarker(double latitude, double longitude, String title, String snippet) {
        Marker marker = null;
        try {
            try {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(latitude, longitude));
                markerOptions.title(title);
                markerOptions.snippet("JS Representative\n"+snippet);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));
                marker = mGoogleMap.addMarker(markerOptions);
                mGoogleMap.setOnMarkerClickListener(this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return marker;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Location Permission Needed")
                            .setMessage("This app needs the Location permission, please accept to use location functionality")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        //Prompt the user once explanation has been shown
                                        ActivityCompat.requestPermissions(getActivity(),
                                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                MY_PERMISSIONS_REQUEST_LOCATION);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .create()
                            .show();


                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                        moveCameraToCurrentLocation();
                        // getCurrentLocationAndDrawMarkers();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        try {
            Double latitude = marker.getPosition().latitude;
            Double longitude = marker.getPosition().longitude;

            LatLng lan2 = new LatLng(latitude, longitude);

            String url = getDirectionsUrl(latlng1, lan2);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            //downloadTask.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        //googleMap.clear();

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            //Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (pDialog != null)
                pDialog.dismiss();

            if (polyline != null)
                polyline.remove();

            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(getActivity(), "", "Please wait...");
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            try {

                ArrayList<LatLng> points = null;
                PolylineOptions lineOptions = null;
                MarkerOptions markerOptions = new MarkerOptions();

                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();

                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(12);
                    lineOptions.color(getResources().getColor(R.color.ufone_color));

                }
                // Drawing polyline in the Google Map for the i-th route
                if (lineOptions != null)
                    polyline = googleMap.addPolyline(lineOptions);
                //googleMap.addPolyline(lineOptions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //|| locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            double longitudeNetwork = location.getLongitude();
            double latitudeNetwork = location.getLatitude();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //longitudeValueNetwork.setText(longitudeNetwork + "");
                    //latitudeValueNetwork.setText(latitudeNetwork + "");
                    Toast.makeText(getActivity(), "Network Provider update", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {

            // getCurrentLocationAndDrawMarkers();

        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    private void showAlert() {
        try {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("Enable Location")
                    .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location " +
                            "for Better Location Accuracy")
                    .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            try {
                                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                getActivity().startActivity(myIntent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        }
                    });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
        /* We query for the best Location Provider everytime this fragment is displayed
         * just in case a better provider might have become available since we last displayed it */

            // Get a reference to the map/GoogleMap object

        /* Enable the my-location layer (this causes our LocationSource to be automatically activated.)
         * While enabled, the my-location layer continuously draws an indication of a user's
         * current location and bearing, and displays UI controls that allow a user to interact
         * with their location (for example, to enable or disable camera tracking of their location and bearing).*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mGoogleMap.setMyLocationEnabled(true);
                    // getCurrentLocationAndDrawMarkers();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getCurrentLocationAndDrawMarkers(List<JsLocationsVO> list) {
        try {

            GPSTracker gps = new GPSTracker(getActivity(), getActivity());
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            if (latitude == 0.0) {
                latitude = 33.6844;
                longitude = 73.0479;

            }

            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            latlng1 = latLng;
            // mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

            //move map camera
            // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
            if (latitude > 0.0) {

                if (!IS_ANIMATED) {
                    mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title(username));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latLng.latitude, latLng.longitude)));
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                    IS_ANIMATED = true;
                }

                if (pBar != null)
                    pBar.setVisibility(View.GONE);

                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        //double distance = getDistance(latLng.latitude, latLng.longitude,
                        //Double.parseDouble(list.get(i).getLatitude()), Double.parseDouble(list.get(i).getLongitude()));
                        //if (distance < 5) {
                        createMarker(Double.parseDouble(list.get(i).getLatitude()), Double.parseDouble(list.get(i).getLongitude()), list.get(i).getLoc_name(), list.get(i).getAddress());
                        //}
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class GetLocatorsAsync extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;
        String mResult;
        List<JsLocationsVO> list_locations;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(getActivity(), "", "Please wait..");

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection connection = new Connection(getActivity());
                mResult = connection.getJsLocators(params[0], params[1]);

                if (mResult != null && !mResult.equals("")) {
                    XMLParser xmp = new XMLParser();
                    xmp.parse(mResult);
                    list_locations = xmp.getLocationsData();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                if (pDialog != null)
                    pDialog.dismiss();

                getCurrentLocationAndDrawMarkers(list_locations);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    void moveCameraToCurrentLocation() {
        try {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location == null) {

                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.6844, 73.0479), 8));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                        .zoom(10)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
            } else if (location != null) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 8));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                        .zoom(10)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
