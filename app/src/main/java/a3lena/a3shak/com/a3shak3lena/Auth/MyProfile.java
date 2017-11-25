package a3lena.a3shak.com.a3shak3lena.Auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import a3lena.a3shak.com.a3shak3lena.Main;
import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.SplashScreen;
import a3lena.a3shak.com.a3shak3lena.slide_menu.BaseSlideMenuActivity;
import a3lena.a3shak.com.a3shak3lena.util.Constatns;
import a3lena.a3shak.com.a3shak3lena.util.Dialogs;
import a3lena.a3shak.com.a3shak3lena.util.MySingleton;
import a3lena.a3shak.com.a3shak3lena.util.util;
import a3lena.a3shak.com.slidemenu.SlideMenu;
import a3lena.a3shak.com.sweet_dialog.SweetAlertDialog;

public class MyProfile extends BaseSlideMenuActivity {

    EditText firstName,lastName,userMobile, userPass;
    String F_Name,L_Name,mobile, pass;
    Button btnUpdate;
    Activity context=this;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String Ucountry,Uid;
    boolean update = true;
    TextView txtEditTitle;

    // Slider
    private SlideMenu mSlideMenu;
    ImageView btnMenu;

    int type;
    Bundle bundle;

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        setSlideRole(R.layout.activity_my_profile);
        setSlideRole(R.layout.layout_secondary_menu);
        mSlideMenu = getSlideMenu();
        util.changeTitle(this,"ملفي الشخصي");
        // Slider Toggle
        click_menu();
        init();
        changeFont();



//        Toast.makeText(MyProfile.this, SplashScreen.image_link, Toast.LENGTH_LONG).show();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                F_Name = firstName.getText().toString();
                L_Name = lastName.getText().toString();
                mobile = userMobile.getText().toString();
                pass = userPass.getText().toString();

                if(update){
                    btnUpdate.setText("حفظ");
                    firstName.setEnabled(true);
                    lastName.setEnabled(true);
                    userPass.setEnabled(true);
                    update=false;

                }else{
                    if (F_Name.equals("") || L_Name.equals("")) {
                        final Dialogs dialogs = new Dialogs(MyProfile.this);
                        dialogs.error_dialog("عذرا !","يرجى تعبئة جميع المعلومات");
                    } else if (pass.equals("")) {
                        userPass.setError("يرجى إدخال كلمة المرور");
                    } else {
                        if(util.haveNetworkConnection(MyProfile.this)){
                            updateProfileRequest();
                            firstName.setEnabled(false);
                            lastName.setEnabled(false);
                            userPass.setEnabled(false);
                            btnUpdate.setText("تعديل البيانات");
                            update = true;
                        }else{
                            util.dialogErrorInternet(MyProfile.this);
                        }

                    }

                }
            }
        });

        setDataUser();

        Log.e("tytyty", String.valueOf(type));
       if(!sharedPreferences.getString("fname","0").equals("")) {
           firstName.setText(sharedPreferences.getString("fname", ""));
           btnUpdate.setEnabled(false);
       }

        if(!sharedPreferences.getString("lname","0").equals("")) {
            lastName.setText(sharedPreferences.getString("lname", ""));
            btnUpdate.setEnabled(false);
        }
    }

    public void init(){
        firstName = (EditText) findViewById(R.id.p_firstName);
        lastName = (EditText) findViewById(R.id.p_lastName);
        userMobile = (EditText) findViewById(R.id.p_mobileNumber);
        userPass = (EditText) findViewById(R.id.p_pass);
        btnUpdate = (Button) findViewById(R.id.p_btnUpdate);
        txtEditTitle = (TextView) findViewById(R.id.txtEditTitle);
        firstName.setEnabled(false);
        lastName.setEnabled(false);
        userMobile.setEnabled(false);
        userPass.setEnabled(false);
        bundle = getIntent().getExtras();
        sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        userMobile.setEnabled(false);
    }

    public void changeFont(){
        firstName.setTypeface(util.changeFont(context));
        lastName.setTypeface(util.changeFont(context));
        userPass.setTypeface(util.changeFont(context));
        userMobile.setTypeface(util.changeFont(context));
        btnUpdate.setTypeface(util.changeFont(context));
        txtEditTitle.setTypeface(util.changeFont(context));
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

    public void setDataUser(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constatns.MAIN_API + Constatns.LOGIN+"?Password="+sharedPreferences.getString("Password","0")+"&Mobile="+sharedPreferences.getString("Mobile","0"),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.e("propo", response.toString());
                            JSONObject aaData = response.getJSONObject("aaData");

                            String id = aaData.getString("Uid");
                            String getName = aaData.getString("FirstName");
                            String getLastName = aaData.getString("LastName");
                            String getMobile = aaData.getString("Mobile");
                            String getCountryId = aaData.getString("CountryId");

                                firstName.setText(getName);
                                lastName.setText(getLastName);
                                userMobile.setText(getMobile);
                                Uid = id;
                                Ucountry = getCountryId;



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

    public void updateProfileRequest() {
              StringRequest stringRequest = new StringRequest(Request.Method.POST, Constatns.MAIN_API + Constatns.UPDATE,
                      new Response.Listener<String>() {
                          @Override
                          public void onResponse(String response) {
                              try {

                                  Log.e("user_info", response);

                                  JSONObject obj1Json = new JSONObject(response);
                                  JSONObject aaData = obj1Json.getJSONObject("aaData");
                                  Log.e("aadodo", aaData.toString());
                                  Log.e("aadodo22", aaData.getString("flag"));
                                  String Flag = aaData.getString("f");
                                  if(Flag.equals("1")){
                                      SharedPreferences.Editor editor = sharedPreferences.edit();
                                      editor.putString("Password", userPass.getText().toString());
                                      editor.commit();
                                      SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MyProfile.this, SweetAlertDialog.SUCCESS_TYPE);
                                      sweetAlertDialog.setTitleText("تم تحديث البيانات بنجاح");
                                      sweetAlertDialog.setColorTitleText(Color.parseColor("#000000"));
                                      sweetAlertDialog.setConfirmText("موافق");
                                      sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                          @Override
                                          public void onClick(SweetAlertDialog sweetAlertDialog) {
                                              sweetAlertDialog.dismiss();
                                          }
                                      });
                                      sweetAlertDialog.show();
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

                      Log.e("vovo", F_Name+" - "+ Ucountry+" - "+ pass+" - "+mobile+" - "+Uid);
                      params.put("FirstName", F_Name);
                      params.put("LastName", L_Name);
                      params.put("Mobile", mobile);
                      params.put("CountryId", Ucountry);
                      params.put("Password", pass);
                      params.put("users_id", Uid);
                      Log.e("dgfgdf", Uid);
                      return params;
                  }
              };
              MySingleton.getInstance(context).addToRequestQueue(stringRequest);
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
