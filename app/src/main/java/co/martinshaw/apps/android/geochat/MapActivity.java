package co.martinshaw.apps.android.geochat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;

    public float lowestZoom = 14f;
//    public float radiusIncrement = 0.001f; // for polygon
//    public float radiusIncrement = 900; // for circle, when at 14 zoom
    public float radiusIncrement = 950/lowestZoom; // 950 (ideal size at lowest zoom) / 14f (lowest zoom level)
    Circle circle;

    Location initialLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set provided (current) location as the initial location for the map
        this.initialLocation = new Location("");
        this.initialLocation.setLatitude(getIntent().getDoubleExtra("CURRENT_LOCATION_LAT", new Double(0)));
        this.initialLocation.setLongitude(getIntent().getDoubleExtra("CURRENT_LOCATION_LONG", new Double(0)));

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add custom styling ...
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.style_map_json));

            if (!success) {
                Log.e("GeoChat Map ", "Style parsing failed.");
            }

        } catch (Resources.NotFoundException e) {
            Log.e("GeoChat Map ", "Can't find style. Error: ", e);
        }


        // Change Map default camera location ...
        LatLng googInitialLocation = new LatLng(this.initialLocation.getLatitude(), this.initialLocation.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(googInitialLocation).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googInitialLocation, 14f));
//        mMap.moveCamera(CameraUpdateFactory.zoomTo(16.75f));


        // Set Max. / Min. zoom bounds
        mMap.setMinZoomPreference(lowestZoom);
        mMap.setMaxZoomPreference(25);


        // Set UI Config for unique map operation
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);



        // Create circle polygon on map
        circle = mMap.addCircle(new CircleOptions()
            .center(googInitialLocation)
            .radius(radiusIncrement * lowestZoom)
            .strokeColor(Color.argb(190, 200, 200, 200))
            .fillColor(Color.argb(40, 111, 111, 111)));


        mMap.setOnCameraChangeListener(getCameraChangeListener());



//        PolygonOptions rectOptions = new PolygonOptions()
//                .add(
//                    addVarianceToLocation(initialLocation, -radiusIncrement, -radiusIncrement),
//                    addVarianceToLocation(initialLocation, -radiusIncrement, radiusIncrement),
//                    addVarianceToLocation(initialLocation, radiusIncrement, radiusIncrement),
//                    addVarianceToLocation(initialLocation, radiusIncrement, -radiusIncrement),
//                    addVarianceToLocation(initialLocation, -radiusIncrement, -radiusIncrement)
//                );
//
//        Polygon polygon = mMap.addPolygon(
//            rectOptions
//            .strokeColor(Color.argb(190, 200, 200, 200))
//            .fillColor(Color.argb(40, 111, 111, 111))
//        );




    }




    public LatLng addVarianceToLocation(Location location, float x, float y){
        return new LatLng(
                location.getLatitude() + x,
                location.getLongitude() + y
        );
    }



    public LatLng addVarianceToLocation(LatLng latlong, float x, float y){
        return new LatLng(
                latlong.latitude + x,
                latlong.longitude + y
        );
    }




    public GoogleMap.OnCameraChangeListener getCameraChangeListener()
    {
        return new GoogleMap.OnCameraChangeListener()
        {
            @Override
            public void onCameraChange(CameraPosition position)
            {

                float radiusAtFullSize = radiusIncrement * lowestZoom;
                float zoomDelta = position.zoom - lowestZoom;
                float radiusDeltaFromFullSize = zoomDelta * radiusIncrement;

                Log.i("MAP ZOOM", Float.toString(radiusAtFullSize) + " " + Float.toString(radiusDeltaFromFullSize) + " " + Float.toString(position.zoom));

                circle.setRadius(radiusAtFullSize - radiusDeltaFromFullSize);

            }
        };
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
