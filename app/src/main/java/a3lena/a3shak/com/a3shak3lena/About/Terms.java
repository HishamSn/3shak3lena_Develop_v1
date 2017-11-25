package a3lena.a3shak.com.a3shak3lena.About;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.SplashScreen;
import a3lena.a3shak.com.a3shak3lena.slide_menu.BaseSlideMenuActivity;
import a3lena.a3shak.com.a3shak3lena.util.Constatns;
import a3lena.a3shak.com.a3shak3lena.util.MySingleton;
import a3lena.a3shak.com.a3shak3lena.util.util;
import a3lena.a3shak.com.slidemenu.SlideMenu;

public class Terms extends BaseSlideMenuActivity {

    // Slider
    private SlideMenu mSlideMenu;
    ImageView btnMenu;

    TextView txtTerms, title_about;

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        setSlideRole(R.layout.activity_terms);
        setSlideRole(R.layout.layout_secondary_menu);
        mSlideMenu = getSlideMenu();
        util.changeTitle(this,"الشروط والأحكام");
        // Slider Toggle
        click_menu();
        init();
        changeFont();
//        getTerms();
        getTerms();
    }


    public void init(){
        txtTerms = (TextView) findViewById(R.id.txtAbout);
        title_about = (TextView) findViewById(R.id.title_about);
    }

    public void changeFont(){
        txtTerms.setTypeface(util.changeFont(this));
        title_about.setTypeface(util.changeFont(this));
    }

    public void click_menu(){
        btnMenu = (ImageView) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSlideMenu.isOpen()){
                    mSlideMenu.close(true);
                    mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT);
                }else{
                    mSlideMenu.open(true, true);
                    mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT);
                }
            }
        });
    }

    public void getTerms(){

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Constatns.MAIN_API + Constatns.SETTINGS,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("terms", response.getString("GeneralTermsandConditions"));
                            txtTerms.setText(response.getString("GeneralTermsandConditions"));
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

    @Override
    protected void onStop() {
        super.onStop();
        if(mSlideMenu.isOpen()) {
            mSlideMenu.close(true);
            mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT);
        }
    }
}
