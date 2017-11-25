package a3lena.a3shak.com.a3shak3lena.Map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import a3lena.a3shak.com.a3shak3lena.Dealers.DealerDetails;
import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.SplashScreen;
import a3lena.a3shak.com.a3shak3lena.slide_menu.BaseSlideMenuActivity;
import a3lena.a3shak.com.a3shak3lena.util.Constatns;
import a3lena.a3shak.com.a3shak3lena.util.MySingleton;
import a3lena.a3shak.com.a3shak3lena.util.util;
import a3lena.a3shak.com.slidemenu.SlideMenu;
import a3lena.a3shak.com.sweet_dialog.SweetAlertDialog;

public class MapDealers extends BaseSlideMenuActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener  {


    List<ItemsModelMap> listDistribution;
    List<ItemsModelMap> listExchange;
    GoogleMap mMap;
    Button btn1, btn2;
    String mapType = "1";
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    Activity context = this;
    private SlideMenu mSlideMenu;
    ImageView btnMenu;
    MapView mMapView;
    boolean locationEnable;

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        setSlideRole(R.layout.activity_map_dealers);
        setSlideRole(R.layout.layout_secondary_menu);
        mSlideMenu = getSlideMenu();
        util.changeTitle(this, "الخريطة");
        // Slider Toggle
        click_menu();
        init();
        changeFont();

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        mMapView = (MapView) findViewById(R.id.mapView);

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }


    }


    public void init() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);

        btn1.setTypeface(util.changeFont(this));
        btn2.setTypeface(util.changeFont(this));

        if (util.getLanguageApplication().equals("en")) {
            btn1.setBackgroundResource(R.drawable.shape_selected);
            btn2.setBackgroundResource(R.drawable.shape_unselected);
            btn1.setTextColor(getApplication().getResources().getColor(R.color.white));
            btn2.setTextColor(getApplication().getResources().getColor(R.color.black));
        } else if (util.getLanguageApplication().equals("ar")) {
            btn1.setBackgroundResource(R.drawable.shape_selected_ar);
            btn2.setBackgroundResource(R.drawable.shape_unselected_ar);
            btn1.setTextColor(getApplication().getResources().getColor(R.color.white));
            btn2.setTextColor(getApplication().getResources().getColor(R.color.black));
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapType = "1";
                if (util.getLanguageApplication().equals("en")) {
                    btn1.setBackgroundResource(R.drawable.shape_selected);
                    btn2.setBackgroundResource(R.drawable.shape_unselected);
                    btn1.setTextColor(getApplication().getResources().getColor(R.color.white));
                    btn2.setTextColor(getApplication().getResources().getColor(R.color.black));
                } else if (util.getLanguageApplication().equals("ar")) {
                    btn1.setBackgroundResource(R.drawable.shape_selected_ar);
                    btn2.setBackgroundResource(R.drawable.shape_unselected_ar);
                    btn1.setTextColor(getApplication().getResources().getColor(R.color.white));
                    btn2.setTextColor(getApplication().getResources().getColor(R.color.black));
                }
                mMap.clear();
                getDealer(mapType);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapType = "2";
                if (util.getLanguageApplication().equals("en")) {
                    btn2.setBackgroundResource(R.drawable.shape_selected_ar);
                    btn1.setBackgroundResource(R.drawable.shape_unselected_ar);
                    btn1.setTextColor(getApplication().getResources().getColor(R.color.black));
                    btn2.setTextColor(getApplication().getResources().getColor(R.color.white));
                } else if (util.getLanguageApplication().equals("ar")) {
                    btn2.setBackgroundResource(R.drawable.shape_selected);
                    btn1.setBackgroundResource(R.drawable.shape_unselected);
                    btn1.setTextColor(getApplication().getResources().getColor(R.color.black));
                    btn2.setTextColor(getApplication().getResources().getColor(R.color.white));
                }
                mMap.clear();
                getDealer(mapType);
            }
        });

    }

    public void changeFont() {

    }

    public void click_menu() {
        btnMenu = (ImageView) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSlideMenu.isOpen()) {
                    mSlideMenu.close(true);
                    mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT);
                } else {
                    mSlideMenu.open(true, true);
                    mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT);
                }
            }
        });
    }



    @Override
    public boolean onMarkerClick(Marker marker) {
        String id = marker.getSnippet();
        switch (mapType) {
            case "1": {
                for (ItemsModelMap location : listExchange) {
                    if (location.getId().equals(id)) {
                        intentToActivity(location);
                    }
                }
                break;
            }
            case "2": {
                for (ItemsModelMap location : listDistribution) {
                    if (location.getId().equals(id)) {
                        intentToActivity(location);
                    }
                }
                break;
            }
            default: {
                break;
            }
        }

        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setOnMarkerClickListener(this);
        getDealer(mapType);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            if(locationEnable)
            {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        getLocation(), 11), 3000, null);
            }
            else
            {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        getLocation(), 6), 3000, null);
            }


        }



    }


    private void distributionMap() {
        mMap.clear();
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$ list distribution size = " + listDistribution.size());
        for (int i = 0; i < listDistribution.size(); i++) {
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$ Draw pin distribution No = " + i);
            ItemsModelMap model = listDistribution.get(i);
            try {
                new DownloadImageTask(model).execute("Distribution load image = " + i);
            } catch (Exception e) {
                mMap.addMarker(new MarkerOptions().position(
                        new LatLng(pars(model.getLatitude()), pars(model.getLongitude()))).icon(BitmapDescriptorFactory.defaultMarker())).setSnippet(model.getId());
            }
        }
    }

    private void exchangeMap() {
        mMap.clear();
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$ list Exchange size = " + listExchange.size());
        for (int i = 0; i < listExchange.size(); i++) {
            ItemsModelMap model = listExchange.get(i);
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$ Draw pin Exchange No = " + i);
            try {
                new DownloadImageTask(model).execute("Exchange load image = " + i);
            } catch (Exception e) {
                mMap.addMarker(new MarkerOptions().position(
                        new LatLng(pars(model.getLatitude()), pars(model.getLongitude()))).icon(BitmapDescriptorFactory.defaultMarker())).setSnippet(model.getId());
            }
        }
    }

    public void getDealer(final String type) {

        if (!util.haveNetworkConnection(context)) {
            util.dialogErrorInternet(context);

        } else {


            final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#babd16"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(true);
            pDialog.show();


            mMap.clear();
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$ list typr = " + type);
            switch (type) {
                case "1": {
                    listExchange = new ArrayList<ItemsModelMap>();
                    listDistribution = null;
                    break;
                }
                case "2": {
                    listDistribution = new ArrayList<ItemsModelMap>();
                    listExchange = null;
                    break;
                }
            }

            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, Constatns.MAIN_API + Constatns.DEALERSHIP + "?CategoryType=" + type,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
//                        Log.e("responseData", response.toString() + " 99999 " + type);

                            try {
                                JSONArray jsonArray = response.getJSONArray("aaData");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$ list type = " + type + " : category  No = " + i);
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    JSONArray jsonArray1 = new JSONArray(jsonObject.getString("Dealership"));
                                    for (int x = 0; x < jsonArray1.length(); x++) {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(x);
                                        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$ list type = " + type + " : category  No = " + i + " : location No = " + x);
                                        switch (type) {
                                            case "1": {
                                                listExchange.add(new ItemsModelMap(jsonObject1));
                                                break;
                                            }
                                            case "2": {
                                                listDistribution.add(new ItemsModelMap(jsonObject1));
                                                break;
                                            }
                                        }
                                    }
                                }
                                switch (type) {
                                    case "1": {
                                        exchangeMap();
                                        break;
                                    }
                                    case "2": {
                                        distributionMap();
                                        break;
                                    }
                                }
                            } catch (Exception ignored) {
                                System.err.println("%%%%%%%%%%%%%%%%%%%%%%%    " + ignored.getMessage());
                            }
                            pDialog.dismiss();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
        }
    }


    public void intentToActivity(ItemsModelMap model) {
        Intent i = new Intent(this, DealerDetails.class);
        i.putExtra("id", model.getId());
        i.putExtra("name", model.getName());
        i.putExtra("mobile", model.getPhone());
        i.putExtra("lat", model.getLatitude());
        i.putExtra("longitude", model.getLongitude());
        i.putExtra("desc", model.getDescr());
        i.putExtra("hours", model.getWorkingHours());
        i.putExtra("fb", model.getFacebookLink());
        i.putExtra("twitter", model.getTwitterLink());
        i.putExtra("youtube", model.getYoutubeLink());
        i.putExtra("web", model.getWebLink());
        i.putExtra("Logo", model.getLogo());
        i.putExtra("show_comment", model.getShowCommite());
        i.putExtra("points", model.getPoints());
        i.putStringArrayListExtra("images", model.getDealershipImages());
        startActivity(i);
    }


    private Bitmap getWhiteBitMap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        bitmap.recycle();

        return output;
    }


    public Bitmap getIconAsBitmapWithSize(Bitmap resultPhoto, int width, int height) {
        return Bitmap.createScaledBitmap(resultPhoto, width, height, false);
    }

    public Bitmap getCircleBitmap(Bitmap resultPhoto, int width, int height) {
        Bitmap bitmap = getIconAsBitmapWithSize(getWhiteBitMap(resultPhoto), width, height);
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // paint.setColor(color);
        canvas.drawOval(rectF, paint);


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }



    private static double pars(String number) {
        double x = 0;
        try {
            x = Double.parseDouble(number);
        } catch (Exception e) {
            x = Double.parseDouble(arabicToDecimal(number));
        } finally {
            return x;
        }
    }

    private static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    private LatLng getLocation() {
        LatLng currentLocation = null;

        if (ActivityCompat.checkSelfPermission(MapDealers.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MapDealers.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapDealers.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                locationEnable=true;
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                currentLocation = new LatLng(latti, longi);
                return currentLocation;

            } else {
                locationEnable=false;
                Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();
                buildAlertMessageNoGps();
                return new LatLng(31.768633, 36.056334);
            }

        }
        return currentLocation;
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("يرجى تفعيل الـ GPS")
                .setCancelable(false)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        //  finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private final int PERMISSION_REQUEST_CODE = 1;


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }

                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    if(locationEnable)
                    {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                getLocation(), 11), 3000, null);
                    }
                    else
                    {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                getLocation(), 6), 3000, null);
                    }



                } else {

                }
                break;
        }
    }


    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private Bitmap image;
        ItemsModelMap location;

        public DownloadImageTask(ItemsModelMap location) {
            this.location = location;
        }

        protected Bitmap doInBackground(String... urls) {
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$ AsyncTask " + urls[0]);
            try {
                InputStream in = new java.net.URL(Constatns.imageLink+location.getLogo()).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                image = null;
            }
            return image;
        }

        @SuppressLint("NewApi")
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                Bitmap smallMarker = Bitmap.createScaledBitmap(result, 150, 150, false);

                BitmapDescriptor icon = BitmapDescriptorFactory
                        .fromBitmap(getCircleBitmap(smallMarker, 150, 150));

                mMap.addMarker(new MarkerOptions().position(
                        new LatLng(pars(location.getLatitude()), pars(location.getLongitude()))).icon(icon)).setSnippet(location.getId());
            }
        }
    }

}
