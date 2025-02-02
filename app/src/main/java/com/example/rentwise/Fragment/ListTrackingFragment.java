package com.example.rentwise.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.rentwise.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import android.os.AsyncTask;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListTrackingFragment extends Fragment implements ListVehicleTrackFragment.OnVehicleItemClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap googleMap;
    private boolean isMapReady = false; // Flag to indicate if the map is ready

    // Callback to handle map readiness
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap map) {
            googleMap = map;
            isMapReady = true; // Set the flag to true when the map is ready
            enableMyLocation(); // Check permissions and enable location features
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_tracking, container, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity()); // Initialize FusedLocationProviderClient
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check for location permissions each time the fragment is created or displayed
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions(); // Request location permissions
        } else {
            initializeMap(); // If permissions are granted, initialize the map
        }

        // Button to open the bottom sheet dialog for vehicle tracking
        ImageButton showBottomSheetButton = view.findViewById(R.id.showBottomSheetButton);
        showBottomSheetButton.setOnClickListener(v -> {
            ListVehicleTrackFragment bottomSheet = ListVehicleTrackFragment.newInstance("param1", "param2");
            bottomSheet.setOnVehicleItemClickListener(this);
            bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());
        });

        // Button to show the user's current location on the map
        ImageButton btnDirection = view.findViewById(R.id.btnDirection);
        btnDirection.setOnClickListener(v -> {
            Log.d("ListTrackingFragment", "My Location button clicked.");
            if (isMapReady) {
                showDeviceLocation(); // Fetch and show the user's current location
            } else {
                Log.e("ListTrackingFragment", "GoogleMap is not ready yet.");
            }
        });
    }

    // Request location permissions
    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    // Initialize the map
    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback); // Set up the map asynchronously
        }
    }

    // Enable My Location layer if permissions are granted
    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                Log.d("ListTrackingFragment", "Permissions granted, enabling MyLocation layer.");
                if (isMapReady) {
                    googleMap.setMyLocationEnabled(true); // Enable location layer
                    showDeviceLocation(); // Show the current device location
                }
            } catch (SecurityException e) {
                Log.e("ListTrackingFragment", "SecurityException when enabling MyLocation: " + e.getMessage());
            }
        } else {
            // Request location permissions if they are not granted
            requestLocationPermissions();
        }
    }

    // Show the device's last known location on the map
    private void showDeviceLocation() {
        if (!isMapReady) {
            Log.e("ListTrackingFragment", "GoogleMap is not ready, cannot show device location.");
            return;
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("ListTrackingFragment", "Fetching last known location.");
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    LatLng initialLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.d("ListTrackingFragment", "Device location found: " + initialLocation);
                    googleMap.addMarker(new MarkerOptions().position(initialLocation).title("Your Location"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 15)); // Set zoom level
                } else {
                    Log.d("ListTrackingFragment", "No last known location available, setting default location.");
                    // Set a default location if location data is not available
                    LatLng defaultLocation = new LatLng(-34, 151); // Example: Sydney
                    googleMap.addMarker(new MarkerOptions().position(defaultLocation).title("Default Location"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
                }
            }).addOnFailureListener(e -> Log.e("ListTrackingFragment", "Failed to fetch location: " + e.getMessage()));
        } else {
            Log.e("ListTrackingFragment", "Location permission not granted for fetching device location.");
            requestLocationPermissions(); // Request permissions if not granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, initialize map features
                initializeMap();
            } else {
                Log.e("ListTrackingFragment", "Location permission denied");
            }
        }
    }

    @Override
    public void onVehicleItemClick(double latitude, double longitude) {
        if (isMapReady) {
            LatLng vehicleLocation = new LatLng(latitude, longitude);
            googleMap.clear(); // Clear previous markers

            // Add a marker for the vehicle location
            googleMap.addMarker(new MarkerOptions().position(vehicleLocation).title("Vehicle Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vehicleLocation, 15)); // Set zoom level

            // Get current location and draw the route
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        drawRoute(userLocation, vehicleLocation); // Call drawRoute to draw the route on the map
                    } else {
                        Log.e("ListTrackingFragment", "Unable to retrieve current location.");
                    }
                }).addOnFailureListener(e -> Log.e("ListTrackingFragment", "Failed to fetch location: " + e.getMessage()));
            } else {
                Log.e("ListTrackingFragment", "Location permission not granted for fetching device location.");
            }
        } else {
            Log.e("ListTrackingFragment", "GoogleMap is not initialized.");
        }
    }


    private void drawRoute(LatLng origin, LatLng destination) {
        String url = getDirectionsUrl(origin, destination);
        new FetchURL().execute(url);
    }

    // Method to generate Google Maps Directions API URL
    private String getDirectionsUrl(LatLng origin, LatLng destination) {
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        String strDest = "destination=" + destination.latitude + "," + destination.longitude;
        String mode = "mode=driving";
        String parameters = strOrigin + "&" + strDest + "&" + mode + "&key=YOUR_API_KEY";
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    // AsyncTask to fetch data from the Google Directions API
    private class FetchURL extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.e("ListTrackingFragment", "Background Task Error: " + e.getMessage());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    // A method to download the JSON data from the Google Directions URL
    private String downloadUrl(String strUrl) throws Exception {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(iStream);
            int dataRead;
            char[] buffer = new char[1024];
            while ((dataRead = isr.read(buffer)) > 0) {
                data += String.copyValueOf(buffer, 0, dataRead);
            }
        } finally {
            if (iStream != null) iStream.close();
            if (urlConnection != null) urlConnection.disconnect();
        }
        return data;
    }

    // AsyncTask to parse the directions JSON data and draw a polyline on the map
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                Log.e("ListTrackingFragment", "Parsing Error: " + e.getMessage());
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = new PolylineOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    points.add(new LatLng(lat, lng));
                }
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE); // Use desired color for the route
            }

            if (googleMap != null) {
                googleMap.addPolyline(lineOptions);
            }
        }
    }

    // JSON Parser class to parse directions JSON data
    public class DirectionsJSONParser {
        public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
            List<List<HashMap<String, String>>> routes = new ArrayList<>();
            JSONArray jRoutes;
            JSONArray jLegs;
            JSONArray jSteps;

            try {
                jRoutes = jObject.getJSONArray("routes");

                for (int i = 0; i < jRoutes.length(); i++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<HashMap<String, String>>();

                    for (int j = 0; j < jLegs.length(); j++) {
                        jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                        for (int k = 0; k < jSteps.length(); k++) {
                            String polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);

                            for (LatLng latLng : list) {
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("lat", Double.toString(latLng.latitude));
                                hm.put("lng", Double.toString(latLng.longitude));
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }
                }

            } catch (Exception e) {
                Log.e("DirectionsJSONParser", "Error: " + e.getMessage());
            }
            return routes;
        }

        private List<LatLng> decodePoly(String encoded) {
            List<LatLng> poly = new ArrayList<>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
                poly.add(p);
            }
            return poly;
        }
    }

}
