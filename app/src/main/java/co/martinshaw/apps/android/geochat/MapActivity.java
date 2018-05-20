package co.martinshaw.apps.android.geochat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
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

    public SeekBar zoomSeekBar;

    private GoogleMap mMap;
    private LocationManager locationManager;

    public float lowestZoom = 15f;
    public float highestZoom = 19f;
    public float deltaZoom;

    public float lowestRadius = 600f;
    public float highestRadius = 30f;
    public float deltaRadius;

    public float radiusIncrement;

    public float currentRadius;

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
        mMap.setMaxZoomPreference(highestZoom);


        // Set UI Config for unique map operation
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(false);



        // Create circle polygon on map
        circle = mMap.addCircle(new CircleOptions()
            .center(googInitialLocation)
            .radius(lowestRadius)
            .strokeColor(Color.argb(190, 200, 200, 200))
            .fillColor(Color.argb(40, 111, 111, 111)));


        mMap.setOnCameraChangeListener(getCameraChangeListener());


        setupSeekBar();




    }




    public GoogleMap.OnCameraChangeListener getCameraChangeListener()
    {
        return new GoogleMap.OnCameraChangeListener()
        {
            @Override
            public void onCameraChange(CameraPosition position)
            {

                deltaZoom = highestZoom - position.zoom;
                deltaRadius = lowestRadius - highestRadius;

                radiusIncrement = (lowestRadius - highestRadius) / (highestZoom - lowestZoom);
                    // delta(radius) / delta(zoom) = ~77.727272727272..

                currentRadius = highestRadius + (deltaZoom * radiusIncrement);

                circle.setRadius(currentRadius);
//                circle.setRadius(currentRadius * ((currentRadius/currentRadius)*.5));


                Log.i("MAP ZOOM", "Current Zoom Level: "+position.zoom+"\nDzoom: "+deltaZoom+"\nDradius: "+deltaRadius+"\nRincrement: "+radiusIncrement+"\nRcurrent: "+currentRadius+"");

            }
        };
    }




    public void setupSeekBar () {
        try
        {
            zoomSeekBar = (SeekBar)findViewById(R.id.zoomSeekBar);
            zoomSeekBar.setMax(Math.round(highestZoom)-Math.round(lowestZoom));

            zoomSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(lowestZoom + progress));
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
