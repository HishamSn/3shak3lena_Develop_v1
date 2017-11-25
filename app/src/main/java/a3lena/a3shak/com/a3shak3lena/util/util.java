package a3lena.a3shak.com.a3shak3lena.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import a3lena.a3shak.com.a3shak3lena.Dealers.Category.CategoryAdapter;
import a3lena.a3shak.com.a3shak3lena.Dealers.Category.CategoryModel;
import a3lena.a3shak.com.a3shak3lena.Dealers.Items.ItemsModel;
import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.slidemenu.SlideMenu;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aldaboubi on 10/9/2017.
 */

public class util {



    public static void setLocale(Context ctx, String lang) { //call this in onCreate()
        Locale myLocale = new Locale(lang);
        Resources res = ctx.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        //Intent refresh = new Intent(this, AndroidLocalize.class);
        //startActivity(refresh);
        //finish();
    }

    public static Typeface changeFont(Activity activity){
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "HacenTunisia.ttf");
        return typeface;
    }

    public static void changeTitle(Activity activity ,String title){
        TextView txtTitle = (TextView) activity.findViewById(R.id.title_coupon_details);
        txtTitle.setText(title);
        txtTitle.setTypeface(changeFont(activity));
    }

    public static void getDealer(final Activity activity, final String type, final ArrayList<CategoryModel> list, final CategoryAdapter categoryAdapter, final Handler handler, final Runnable runnable){

        final Dialogs dialogs = new Dialogs(activity);
        dialogs.ProgressDialog();

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, Constatns.MAIN_API+Constatns.DEALERSHIP+"?CategoryType="+type,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("daboubi99", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("aaData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Log.e("adadadad", jsonObject.toString());
                                CategoryModel categoryModel = new CategoryModel();
                                categoryModel.setCategoryID(jsonObject.getString("CategoryID"));
                                categoryModel.setCategoryName(jsonObject.getString("CategoryName"));
                                categoryModel.setCategoryType(type);
                                ArrayList<ItemsModel> itemsModels = new ArrayList<ItemsModel>();
                                JSONArray jsonArray1 = new JSONArray(jsonObject.getString("Dealership"));
                                for (int x = 0; x < jsonArray1.length(); x++) {
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(x);
                                    Log.e("sdddddddddd",jsonObject1.getString("Name"));
                                    ItemsModel itemsModel = new ItemsModel();
                                    itemsModel.setId(jsonObject1.getString("Id"));
                                    itemsModel.setName(jsonObject1.getString("Name"));
                                    itemsModel.setDescr(jsonObject1.getString("Descr"));
                                    itemsModel.setLogo(jsonObject1.getString("Logo"));
                                    itemsModel.setFacebookLink(jsonObject1.getString("FacebookLink"));
                                    itemsModel.setYoutubeLink(jsonObject1.getString("YoutubeLink"));
                                    itemsModel.setTwitterLink(jsonObject1.getString("TwitterLink"));
                                    itemsModel.setWebLink(jsonObject1.getString("WebLink"));
                                    itemsModel.setShowCommite(jsonObject1.getString("ShowCommite"));
                                    itemsModel.setPhone(jsonObject1.getString("Phone"));
                                    itemsModel.setCategoryId(jsonObject1.getString("CategoryId"));
                                    itemsModel.setLatitude(jsonObject1.getString("Latitude"));
                                    itemsModel.setLongitude(jsonObject1.getString("Longitude"));
                                    itemsModel.setWorkingHours(jsonObject1.getString("WorkingHours"));
                                    itemsModel.setPoints(jsonObject1.getString("Points"));
                                    ArrayList<String> arrImages = new ArrayList<>();
                                    JSONArray jsonArray2 = new JSONArray(jsonObject1.getString("DealershipImages"));
                                    for(int k=1; k < jsonArray2.length(); k++){
                                        arrImages.add(jsonArray2.get(k).toString());
                                    }
                                    itemsModel.setDealershipImages(arrImages);
                                    itemsModels.add(itemsModel);
                                }
                                categoryModel.setItems(itemsModels);
                                list.add(categoryModel);
                                Log.e("suprrrr", String.valueOf(list.size()));
                                categoryAdapter.notifyDataSetChanged();
                            }
                            handler.removeCallbacksAndMessages(null);
                            handler.removeCallbacks(runnable);
                            dialogs.cancelDialog();
                        } catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(activity).addToRequestQueue(jsonArrayRequest);
    }



    public static boolean haveNetworkConnection(Activity activity) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }

        final boolean value = haveConnectedWifi || haveConnectedMobile;

        return value;
    }

    public static void dialogErrorInternet(final Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.check_internet_connection, null);

        GifImageView gifImageView = v.findViewById(R.id.gif_internet);
        TextView txtInternet = (TextView) v.findViewById(R.id.txtinternet);
        Button btnRetry = (Button) v.findViewById(R.id.btnRetry);

        txtInternet.setTypeface(util.changeFont(activity));
        btnRetry.setTypeface(util.changeFont(activity));

        final Dialog dialog = new Dialog(activity, R.style.DialogThemeWhite);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(util.haveNetworkConnection(activity)){
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(v);
        dialog.show();
        Window window_register = dialog.getWindow();
        window_register.setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
    }

    private static final String LANGUAGE_APP = "LANGUAGE_APP";

    public static String getLanguageApplication() {
        return StartUp.getInstance().getPreferences().getString(LANGUAGE_APP, "ar");
    }


}
