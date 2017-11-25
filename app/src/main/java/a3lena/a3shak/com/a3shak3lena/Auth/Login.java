package a3lena.a3shak.com.a3shak3lena.Auth;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import a3lena.a3shak.com.a3shak3lena.Coupons.MyCoupons;
import a3lena.a3shak.com.a3shak3lena.Dealers.DealerDetails;
import a3lena.a3shak.com.a3shak3lena.Dealers.DealersMain;
import a3lena.a3shak.com.a3shak3lena.Main;
import a3lena.a3shak.com.a3shak3lena.Map.MapDealers;
import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.SplashScreen;
import a3lena.a3shak.com.a3shak3lena.slide_menu.BaseSlideMenuActivity;
import a3lena.a3shak.com.a3shak3lena.util.Constatns;
import a3lena.a3shak.com.a3shak3lena.util.Dialogs;
import a3lena.a3shak.com.a3shak3lena.util.MySingleton;
import a3lena.a3shak.com.a3shak3lena.util.util;
import a3lena.a3shak.com.slidemenu.SlideMenu;
import a3lena.a3shak.com.sweet_dialog.SweetAlertDialog;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class Login extends BaseSlideMenuActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int TIME_HOLD = 2000;
    // Slider
    private SlideMenu mSlideMenu;
    ImageView btnMenu;

    int x = 0;

    EditText etMobile, etPassword;
    TextView txtForgetPass, txtNewUser, or;
    Button btnLogin;
    ImageView btnFb, btnTwitter, btnGoogle;
    String mobile, passsword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public LoginButton loginButton;
    public CallbackManager callbackManager;
    int loginType;

//    SignInButton btnGoogle;

    GoogleApiClient googleApiClient;
    TwitterLoginButton twitterLoginButton;

    Bundle bundle;
    public boolean loggedin = false;

    Handler handler;
    Runnable runnable;

    LinearLayout or_hidden;

    String fbUrl, googleUrl, twitterUrl;
    private Handler handlerDialog;

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        setSlideRole(R.layout.activity_auth);
        setSlideRole(R.layout.layout_secondary_menu);
        mSlideMenu = getSlideMenu();
        // Slider Toggle
        click_menu();
        init();
        getSettingsSocail();
        changeFont();
        fbLogin();
        FacebookSdk.sdkInitialize(this);
        Twitter.initialize(this);
        handler = new Handler();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginWithMobile();
            }
        });

        txtNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Register.class);
                i.putExtra("from", bundle.getString("from"));
                startActivity(i);
                finish();
            }
        });


        txtForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetDialog();
            }
        });

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginType = 1;
                googleLogin();

            }
        });

        twitterLogin();

        loggedin = sharedPreferences.getBoolean("loggedin", false);

        if (loggedin) {
            if (bundle.getString("from").equals("coupons")) {
                startActivity(new Intent(Login.this, MyCoupons.class));
                finish();
            } else if (bundle.getString("from").equals("slider")) {
                startActivity(new Intent(Login.this, Main.class));
                finish();
            } else if (bundle.getString("from").equals("profile")) {
                startActivity(new Intent(Login.this, MyProfile.class));
                finish();
            } else if (bundle.getString("from").equals("details")) {
                startActivity(new Intent(Login.this, DealerDetails.class));
                finish();
            } else {
                startActivity(new Intent(Login.this, Main.class));
                finish();
            }
        }


    }

    public void getSettingsSocail() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constatns.MAIN_API + Constatns.SETTINGS,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String fb = response.getString("LoginUseFacebook");
                            String google = response.getString("LoginUseGoogle");
                            String twitter = response.getString("LoginUseTwitter");

                            fbUrl = fb;
                            googleUrl = google;
                            twitterUrl = twitter;


                            if (fbUrl.equals("0")) {
                                btnFb.setVisibility(View.GONE);
                            } else {
                                btnFb.setVisibility(View.VISIBLE);
                            }

                            if (googleUrl.equals("0")) {
                                btnGoogle.setVisibility(View.GONE);
                            } else {
                                btnGoogle.setVisibility(View.VISIBLE);
                            }

                            if (twitterUrl.equals("0")) {
                                btnTwitter.setVisibility(View.GONE);
                            } else {
                                btnTwitter.setVisibility(View.VISIBLE);
                            }

                            if (fbUrl.equals("0") && googleUrl.equals("0") && twitterUrl.equals("0")) {
                                or_hidden.setVisibility(View.GONE);
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

    public void init() {
        etMobile = (EditText) findViewById(R.id.etMobile);
        etPassword = (EditText) findViewById(R.id.etPassword);
        txtForgetPass = (TextView) findViewById(R.id.txtForgetPass);
        txtNewUser = (TextView) findViewById(R.id.txtNewUser);
        or = (TextView) findViewById(R.id.or);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        or_hidden = (LinearLayout) findViewById(R.id.or_hidden);

        btnTwitter = (ImageView) findViewById(R.id.btnTwitter);
        btnGoogle = (ImageView) findViewById(R.id.btnGoogle);
        sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        bundle = getIntent().getExtras();

    }

    public void changeFont() {
        etMobile.setTypeface(util.changeFont(this));
        etPassword.setTypeface(util.changeFont(this));
        txtForgetPass.setTypeface(util.changeFont(this));
        txtNewUser.setTypeface(util.changeFont(this));
        btnLogin.setTypeface(util.changeFont(this));
        or.setTypeface(util.changeFont(this));
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

    public void LoginWithMobile() {
        mobile = etMobile.getText().toString();
        passsword = etPassword.getText().toString();

        final Dialogs dialogs = new Dialogs(this);

        if (mobile.equals("") || passsword.equals("")) {
            dialogs.error_dialog("عذرا !", "يرجى ادخال رقم الهاتف وكلمة المرور");
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constatns.MAIN_API + Constatns.LOGIN + "?logintype=1",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.e("user_info_normal", response);

                                JSONObject obj1Json = new JSONObject(response);
                                JSONObject aaData = obj1Json.getJSONObject("aaData");
                                String Flag = aaData.getString("flag");
                                String statusLogin = aaData.getString("f");
                                if (statusLogin.equals("1")) {
                                    String id = aaData.getString("Uid");
                                    String getName = aaData.getString("FirstName");
                                    String getLastName = aaData.getString("LastName");
                                    editor.putBoolean("loggedin", true);
                                    editor.putString("userid", id);
                                    editor.putString("Password", passsword);
                                    editor.putString("Mobile", mobile);
                                    editor.putString("fname", "");
                                    editor.putString("lname", "");
                                    editor.commit();
//                                    dialogs.login_redirect(getName);
                                    intentLogin();

                                } else {
                                    dialogs.error_dialog("عذرا !", Flag);
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
                    params.put("Password", passsword);
                    params.put("Mobile", mobile);
                    return params;
                }
            };
            MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }
    }


    // [ Login With Facebook ]
    public void fbLogin() {
        loginButton = (LoginButton) findViewById(R.id.login_button);
        btnFb = (ImageView) findViewById(R.id.btnFb);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                graphRequest(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });


        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginType = 2;
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile", "email", "user_friends"));

            }
        });
    }

    public void graphRequest(AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(final JSONObject object, GraphResponse response) {
//                Toast.makeText(getApplicationContext(),object.toString(),Toast.LENGTH_LONG).show();
                Log.d("fb_data", object.toString());

                try {
                    Log.d("fb_id", object.getString("id"));
                    Log.d("email", object.getString("email"));

                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            handler.postDelayed(runnable, 2000);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constatns.MAIN_API + Constatns.LOGIN + "?logintype=2",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d("fb_data_database", response);

                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                JSONObject jsonObject1 = jsonObject.getJSONObject("aaData");
                                                String uid = jsonObject1.getString("Uid");
                                                String fname = jsonObject1.getString("FirstName");
                                                String lname = jsonObject1.getString("LastName");
                                                Log.e("rere", uid);

                                                editor.putString("userid", uid);
                                                editor.putBoolean("loggedin", true);
                                                editor.putString("fname", fname);
                                                editor.putString("lname", lname);
                                                editor.commit();
                                                handler.removeCallbacks(runnable);
                                                handler.removeCallbacksAndMessages(null);
                                                intentLogin();
//                                        Toast.makeText(Login.this, "Success Facebook", Toast.LENGTH_LONG).show();
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
                                    try {
                                        params.put("facebookid", object.getString("id"));
                                        params.put("email", object.getString("email"));
                                        params.put("first_name", object.getString("first_name"));
                                        params.put("last_name", object.getString("last_name"));
                                        params.put("gender", "");
                                        params.put("user_birthday", "");
                                        params.put("name", "");
                                        params.put("gender", "");
                                        params.put("phone", "");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return params;
                                }
                            };
                            MySingleton.getInstance(Login.this).addToRequestQueue(stringRequest);
                        }
                    };
                    handler.postDelayed(runnable, 0);

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        });

        Bundle b = new Bundle();
        b.putString("fields", "id,email,first_name,last_name,picture.type(large)");
        request.setParameters(b);
        request.executeAsync();

    }
    // [ END Login With Facebook ]

    // [ Login With Google ]
    public void googleLogin() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, 1);
        handler.removeCallbacks(runnable);
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            final GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getId();
            String email = account.getEmail();

            runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(runnable, 2000);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constatns.MAIN_API + Constatns.LOGIN + "?logintype=3",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("LoginGoogle", response);

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);

                                        JSONObject jsonObject1 = jsonObject.getJSONObject("aaData");
                                        String uid = jsonObject1.getString("Uid");
                                        String fname = jsonObject1.getString("FirstName");
                                        String lname = jsonObject1.getString("LastName");
                                        Log.e("rere", uid);

                                        editor.putBoolean("loggedin", true);
                                        editor.putString("userid", uid);
                                        editor.putString("Mobile", "");
                                        editor.putString("Password", "");
                                        editor.putString("fname", fname);
                                        editor.putString("lname", lname);
                                        editor.commit();

                                        handler.removeCallbacks(runnable);
                                        handler.removeCallbacksAndMessages(null);
//                                        Toast.makeText(Login.this, response.toString(), Toast.LENGTH_LONG).show();
                                        intentLogin();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

//                            Toast.makeText(Login.this, "google ok", Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<String, String>();

                            params.put("googleid", account.getId());
                            params.put("email", account.getEmail());
                            params.put("first_name", account.getGivenName());
                            params.put("last_name", account.getFamilyName());
                            params.put("gender", "");
                            params.put("user_birthday", "");
                            params.put("name", "");
                            params.put("gender", "");
                            params.put("phone", "");

                            return params;
                        }
                    };
                    MySingleton.getInstance(Login.this).addToRequestQueue(stringRequest);
                }
            };
            handler.postDelayed(runnable, 0);

        }
    }
    // [ End Login With Google ]

    // [ Login With Twitter ]
    public void twitterLogin() {
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.login_button_twitter);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;

                loginTwtitter(session);

            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });

        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginType = 3;
                twitterLoginButton.performClick();
            }
        });
    }

    public void loginTwtitter(final TwitterSession session) {
        Log.e("twtwtw", "sdfsdf");
        String name = session.getUserName();
        long id = session.getUserId();

        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, 3000);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constatns.MAIN_API + Constatns.LOGIN + "?logintype=4",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("twitter_data_database", response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    JSONObject jsonObject1 = jsonObject.getJSONObject("aaData");
                                    String uid = jsonObject1.getString("Uid");
                                    String fname = jsonObject1.getString("FirstName");
                                    String lname = jsonObject1.getString("LastName");
                                    Log.e("rere", uid);

                                    editor.putBoolean("loggedin", true);
                                    editor.putString("userid", uid);
                                    editor.putString("fname", fname);
                                    editor.putString("lname", lname);
                                    editor.commit();

                                    handler.removeCallbacks(runnable);
                                    handler.removeCallbacksAndMessages(null);
                                    intentLogin();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


//                        Toast.makeText(Login.this, "twitter ok", Toast.LENGTH_LONG).show();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<String, String>();

                        params.put("twitterid", String.valueOf(session.getId()));
                        params.put("email", "");
                        params.put("first_name", session.getUserName());
                        params.put("last_name", "");
                        params.put("gender", "");
                        params.put("user_birthday", "");
                        params.put("name", "");
                        params.put("gender", "");
                        params.put("phone", "");

                        return params;
                    }
                };
                MySingleton.getInstance(Login.this).addToRequestQueue(stringRequest);
            }
        };
        handler.postDelayed(runnable, 0);

        Log.e("TwitterIdddd", name + " XXXXX " + String.valueOf(id));
    }
    // [ End Login With Twitter ]


    public void forgetDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.forget_pass_dialog, null);
        TextView txtforget = (TextView) view.findViewById(R.id.txtforget);
        final EditText etForgetMobile = (EditText) view.findViewById(R.id.etForgetMobile);
        final TextView txtCode = (TextView) view.findViewById(R.id.txtCode);
        final Button yes = (Button) view.findViewById(R.id.yes);
        final Button yescode = (Button) view.findViewById(R.id.yescode);
        Button no = (Button) view.findViewById(R.id.no);


        final EditText etActiveCode = (EditText) view.findViewById(R.id.etActiveCop);
        final Button btnBack = (Button) view.findViewById(R.id.btnBack);

        txtCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etForgetMobile.setVisibility(View.GONE);
                etActiveCode.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.VISIBLE);
                txtCode.setVisibility(View.GONE);
                yes.setVisibility(View.GONE);
                yescode.setVisibility(View.VISIBLE);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etForgetMobile.setVisibility(View.VISIBLE);
                etActiveCode.setVisibility(View.GONE);
                txtCode.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.GONE);
                yes.setVisibility(View.VISIBLE);
                yescode.setVisibility(View.GONE);

            }
        });
        final Dialog dialog = new Dialog(this);

        txtforget.setTypeface(util.changeFont(this));
        etForgetMobile.setTypeface(util.changeFont(this));
        txtCode.setTypeface(util.changeFont(this));
        txtCode.setTypeface(util.changeFont(this));
        yes.setTypeface(util.changeFont(this));
        yescode.setTypeface(util.changeFont(this));
        no.setTypeface(util.changeFont(this));
        etActiveCode.setTypeface(util.changeFont(this));
        btnBack.setTypeface(util.changeFont(this));

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etForgetMobile.getText().toString().equals("")) {
                    reset_password("mobile", etForgetMobile.getText().toString(), dialog);
                    etForgetMobile.setVisibility(View.GONE);
                    yes.setVisibility(View.GONE);
                    yescode.setVisibility(View.VISIBLE);
                    etActiveCode.setVisibility(View.VISIBLE);
                    btnBack.setVisibility(View.VISIBLE);
                    txtCode.setVisibility(View.GONE);
                } else {
                }

            }
        });

        yescode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etActiveCode.getText().toString().equals("")) {
                    reset_password("code", etActiveCode.getText().toString(), dialog);
                    etActiveCode.setText("");
                } else {

                }
            }
        });


        dialog.setContentView(view);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.8f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        Window window_register = dialog.getWindow();

        window_register.setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (loginType == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        } else if (loginType == 2) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else {
            twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void intentLogin() {
        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.SUCCESS_TYPE);

        sweetAlertDialog.setTitleText("تم تسجيل دخولك بنجاح");
        sweetAlertDialog.setColorTitleText(Color.parseColor("#babd16"));
        sweetAlertDialog.setContentText("سيتم تحويلك الى شاشة الرئيسية");
        handlerDialog=new Handler();
        handlerDialog.postDelayed(new Runnable() {
            @Override
            public void run() {
                sweetAlertDialog.dismiss();
                if (bundle.getString("from").equals("coupons")) {
                    startActivity(new Intent(Login.this, MyCoupons.class));
                    finish();
                } else if (bundle.getString("from").equals("slider")) {
                    startActivity(new Intent(Login.this, Main.class));
                    finish();
                } else if (bundle.getString("from").equals("details")) {
                    Intent intent = new Intent(Login.this, DealerDetails.class);
                    intent.putExtra("logo", bundle.getString("logo"));
                    intent.putStringArrayListExtra("images", bundle.getStringArrayList("images"));
                    finish();
                } else if (bundle.getString("from").equals("profile")) {
                    Intent i = new Intent(Login.this, MyProfile.class);
                    startActivity(i);
                    finish();
                } else {
                    startActivity(new Intent(Login.this, Main.class));
                    finish();
                }
            }
        }, TIME_HOLD);
        sweetAlertDialog.show();
        sweetAlertDialog.showConfirmButton(false);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSlideMenu.isOpen()) {
            mSlideMenu.close(true);
            mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT);
        }
    }

    public void reset_password(final String key, final String value, final Dialog dialog) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setCancelable(true);
        pDialog.setTitleText("تفعيل");
        pDialog.setColorTitleText(Color.parseColor("#ffffff"));
        pDialog.show();

//8197
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constatns.MAIN_API + Constatns.RESEND + "?" + key + "=" + value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("reset_pass", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("aaData");
                            String checkCode = jsonObject1.getString("f");

                            if (key.equals("code") && checkCode.equals("1")) {
                                dialog.dismiss();
                            }


                            final SweetAlertDialog dlg = new SweetAlertDialog(Login.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                            dlg.setTitleText("اعادة تعيين كلمة المرور");
                            dlg.setContentText(jsonObject1.getString("flag"));
                            dlg.setCustomImage(R.drawable.logo_menu);
                            dlg.setConfirmText("موافق");
                            dlg.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    dlg.dismiss();
                                }
                            });
                            dlg.show();
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//


//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}
