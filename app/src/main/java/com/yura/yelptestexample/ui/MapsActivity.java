package com.yura.yelptestexample.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yura.yelptestexample.R;
import com.yura.yelptestexample.api.response.answer.SearchAnswer;
import com.yura.yelptestexample.core.BaseActivity;
import com.yura.yelptestexample.databinding.ActivityMapsBinding;
import com.yura.yelptestexample.event.httpEvent.BaseHttpEvent;
import com.yura.yelptestexample.mvp.presenter.MapPresenter;
import com.yura.yelptestexample.mvp.view.MapView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback,
        ClusterManager.OnClusterItemInfoWindowClickListener<ClusterItem>,
        LocationListener,
        MapView{

    ActivityMapsBinding binder;

    @InjectPresenter
    MapPresenter mapPresenter;

    private ClusterManager<ClusterItem> mClusterManager;

    private ItemsRenderer renderer;

    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    private GoogleMap mMap;

    private SearchAnswer.Businesses clickedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binder = DataBindingUtil.setContentView(this, R.layout.activity_maps);
        binder.mapView.onCreate(savedInstanceState);
        binder.mapView.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mapPresenter.getToken();
    }

    @Override
    public void onResume() {
        super.onResume();
        binder.mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binder.mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binder.mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binder.mapView.onLowMemory();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BaseHttpEvent event) {
        mapPresenter.messageEvent(event);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mClusterManager = new ClusterManager<>(this, mMap);

        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        renderer = new ItemsRenderer(getApplicationContext(), mMap, mClusterManager);
        mClusterManager.setRenderer(renderer);
        mClusterManager.setAlgorithm(new NonHierarchicalDistanceBasedAlgorithm<>());

        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnInfoWindowClickListener(mClusterManager);

        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        mClusterManager
                .setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ClusterItem>() {
                    @Override
                    public boolean onClusterItemClick(ClusterItem item) {
                        clickedItem = (SearchAnswer.Businesses)item;
                        return false;
                    }
                });

        MapsInitializer.initialize(this);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
    }


    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);

        mapPresenter.searchRestaurants(location.getLatitude(), location.getLongitude());

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onClusterItemInfoWindowClick(ClusterItem myItem) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("id", clickedItem.id);
        hashMap.put("image", clickedItem.image_url);
        hashMap.put("name", clickedItem.name);
        hashMap.put("phone", clickedItem.phone);
        hashMap.put("categories", clickedItem.getCategories());
        hashMap.put("rating", String.valueOf(clickedItem.rating));

        Intent intent = new Intent(this, RestaurantActivity.class);
        intent.putExtra("map", hashMap);
        startActivity(intent);
    }

    @Override
    public void showBusinesses(List<SearchAnswer.Businesses> list) {
        mClusterManager.clearItems();
        mClusterManager.cluster();

        for (SearchAnswer.Businesses businesses : list) {
            mClusterManager.addItem(businesses);

            mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(
                    new MapsActivity.MyCustomAdapterForItems());
        }

        mClusterManager.cluster();
    }

    public class MyCustomAdapterForItems implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyCustomAdapterForItems() {
            myContentsView = getLayoutInflater().inflate(
                    R.layout.info_window, null);


        }
        @Override
        public View getInfoWindow(Marker marker) {
            ((TextView)myContentsView.findViewById(R.id.tvName)).setText(clickedItem.name);
            ((TextView)myContentsView.findViewById(R.id.tvRating)).setText("Rating: " + clickedItem.rating + "/5");
            ((TextView)myContentsView.findViewById(R.id.tvAddress)).setText("" + clickedItem.location.get("address1") + clickedItem.location.get("address2") + clickedItem.location.get("address3"));

            return myContentsView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    /**
     * Draws photos inside markers.
     */
    private class ItemsRenderer extends DefaultClusterRenderer<ClusterItem> {

        public ItemsRenderer(Context context, GoogleMap map, ClusterManager<ClusterItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(ClusterItem customClusterItem, final MarkerOptions markerOptions) {
            final SearchAnswer.Businesses entity = (SearchAnswer.Businesses)customClusterItem;

            if(entity.image_url == null || entity.image_url.equals("")) {
                markerOptions.title(entity.name);
                return;
            }



            Picasso.with(getApplicationContext())
                    .load(entity.image_url)
                    .resize(128, 128)
                    .centerCrop()
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap)).title(entity.name);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<ClusterItem> cluster, MarkerOptions markerOptions) {
            super.onBeforeClusterRendered(cluster, markerOptions);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            return cluster.getSize() > 1;
        }
    }

}
