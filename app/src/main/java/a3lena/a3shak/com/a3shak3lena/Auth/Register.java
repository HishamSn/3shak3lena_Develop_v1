package a3lena.a3shak.com.a3shak3lena.Auth;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.FacebookSdk;
import com.twitter.sdk.android.core.Twitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import a3lena.a3shak.com.a3shak3lena.Auth.Country.ContryModel;
import a3lena.a3shak.com.a3shak3lena.Auth.Country.SpinnerAdapter;
import a3lena.a3shak.com.a3shak3lena.Dealers.DealerDetails;
import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.slide_menu.BaseSlideMenuActivity;
import a3lena.a3shak.com.a3shak3lena.util.Constatns;
import a3lena.a3shak.com.a3shak3lena.util.Dialogs;
import a3lena.a3shak.com.a3shak3lena.util.MySingleton;
import a3lena.a3shak.com.a3shak3lena.util.util;
import a3lena.a3shak.com.slidemenu.SlideMenu;

public class Register extends BaseSlideMenuActivity {

    // Slider
    private SlideMenu mSlideMenu;
    ImageView btnMenu;
    EditText etMobile, etPassword, etFname, etLname;
    Button btnRegister;
    String mobile, passsword, fname, lname, countryid;
    Bundle bundle;

    List<ContryModel> lstCountry = new ArrayList<>();
    Spinner spCountry;
    SpinnerAdapter spinnerAdapter;

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        setSlideRole(R.layout.activity_register);
        setSlideRole(R.layout.layout_secondary_menu);
        mSlideMenu = getSlideMenu();
        // Slider Toggle
        click_menu();
        init();
        changeFont();
        getCountry();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }

    public void init(){
        etMobile = (EditText) findViewById(R.id.etMobile);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etFname = (EditText) findViewById(R.id.etFname);
        etLname = (EditText) findViewById(R.id.etLname);
        btnRegister = (Button) findViewById(R.id.btnRegsiter);
        spCountry = (Spinner) findViewById(R.id.spcountry);
        spinnerAdapter = new SpinnerAdapter(lstCountry, Register.this);
        spCountry.setAdapter(spinnerAdapter);
        bundle = getIntent().getExtras();

        Log.e("vbvb", bundle.getString("from"));

//        sharedPreferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();

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

    public void Register() {

        final ProgressDialog progressDialog = new ProgressDialog(Register.this);
        progressDialog.setCancelable(false);
        progressDialog.show();

        fname = etFname.getText().toString();
        lname = etLname.getText().toString();
        countryid = lstCountry.get(spCountry.getSelectedItemPosition()).getCid();
        mobile = etMobile.getText().toString();
        passsword = etPassword.getText().toString();

        Log.e("countaaaa", String.valueOf(countryid));

        final Dialogs dialogs = new Dialogs(this);

        if (fname.equals("") || lname.equals("") || passsword.equals("") || mobile.equals("") ) {
            progressDialog.dismiss();
            dialogs.error_dialog("عذرا !","يرجى ادخال جميع البيانات المطلوبة");
        } else if(spCountry.getSelectedItemPosition() == 0) {
            progressDialog.dismiss();
            dialogs.error_dialog("عذرا !","يرجى اختيار الدولة");
        }else{

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constatns.MAIN_API + Constatns.REGISTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                Log.e("user_info", response);

                                JSONObject obj1Json = new JSONObject(response);
                                JSONObject aaData = obj1Json.getJSONObject("aaData");
                                String Flag = aaData.getString("flag");
                                String Status = aaData.getString("f");

                                if(Status.equals("1")){
                                    dialogs.register_success_dialog(Flag, bundle.getString("from"));
                                    progressDialog.dismiss();
                                }else{
                                    dialogs.error_dialog("عذرا !",Flag);
                                    progressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();

                    params.put("FirstName", fname);
                    params.put("LastName", lname);
                    params.put("Mobile", mobile);
                    params.put("CountryId", countryid);
                    params.put("Password", passsword);
                    return params;
                }
            };
            MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

    public void changeFont(){
        etMobile.setTypeface(util.changeFont(this));
        etPassword.setTypeface(util.changeFont(this));
        etFname.setTypeface(util.changeFont(this));
        etLname.setTypeface(util.changeFont(this));
        btnRegister.setTypeface(util.changeFont(this));
    }

    public void getCountry(){
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, Constatns.MAIN_API + Constatns.GET_CONTRIES,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("saaa", response.toString());

                        try {
                            JSONArray jsonArray = response.getJSONArray("aaData");
                            ContryModel contryModel2 = new ContryModel();
                            contryModel2.setCid("0");
                            contryModel2.setCname("يرجى اختيار الدولة");
                            lstCountry.add(contryModel2);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ContryModel contryModel = new ContryModel();
                                contryModel.setCid(jsonObject.getString("id"));
                                contryModel.setCname(jsonObject.getString("name"));
                                lstCountry.add(contryModel);
                                spinnerAdapter.notifyDataSetChanged();
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
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
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
