package a3lena.a3shak.com.a3shak3lena;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import a3lena.a3shak.com.a3shak3lena.Auth.Login;
import a3lena.a3shak.com.a3shak3lena.Dealers.DealersMain;
import a3lena.a3shak.com.a3shak3lena.Map.MapDealers;
import a3lena.a3shak.com.a3shak3lena.util.Constatns;
import a3lena.a3shak.com.a3shak3lena.util.MySingleton;
import a3lena.a3shak.com.a3shak3lena.util.util;
import pl.droidsonroids.gif.GifImageView;

public class SplashScreen extends AppCompatActivity {

    Handler handler;
    Runnable runnable;
    public static String image_link;
    public static String LoginUseFacebook, LoginUseGoogle, LoginUseTwitter, about, terms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        handler = new Handler();
        if(util.haveNetworkConnection(this)){
            runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(runnable,2000);
                    getSettings();
                }
            };
            handler.postDelayed(runnable, 0);
        }else{

            dialogErrorInternet(this);
        }
    }

    public void getSettings(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constatns.MAIN_API + Constatns.SETTINGS,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("aa", response.toString());

                            Log.e("imagesssssss",response.getString("ImageLink"));
                            String image_link_in = response.getString("ImageLink")+"/";
                            image_link = image_link_in;

                            String fb = response.getString("LoginUseFacebook");
                            String google = response.getString("LoginUseGoogle");
                            String twitter = response.getString("LoginUseTwitter");

                            String a = response.getString("AboutApp");
                            String t = response.getString("GeneralTermsandConditions");

                            about = a;
                            terms = t;

                            LoginUseFacebook = fb;
                            LoginUseGoogle = google;
                            LoginUseTwitter = twitter;

                            String start_screen = response.getString("StartScreen");
                            if(start_screen.equals("1")){
                                handler.removeCallbacks(runnable);
                                handler.removeCallbacksAndMessages(null);
                                Intent i = new Intent(SplashScreen.this, MapDealers.class);
                                startActivity(i);
                                finish();
                            }else if(start_screen.equals("2")){
                                handler.removeCallbacks(runnable);
                                handler.removeCallbacksAndMessages(null);
                                Intent i = new Intent(SplashScreen.this, Main.class);
                                startActivity(i);
                                finish();
                            }else {
                                handler.removeCallbacks(runnable);
                                handler.removeCallbacksAndMessages(null);
                                Intent i = new Intent(SplashScreen.this, DealersMain.class);
                                startActivity(i);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void dialogErrorInternet(final Activity activity){
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
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            handler.postDelayed(runnable,2000);
                            getSettings();
                            dialog.dismiss();
                        }
                    };
                    handler.postDelayed(runnable, 0);
                }
            }
        });
        dialog.setContentView(v);
        dialog.show();
        Window window_register = dialog.getWindow();
        window_register.setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
    }
}
