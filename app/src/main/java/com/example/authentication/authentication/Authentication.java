package com.example.authentication.authentication;


import android.app.Activity;
import android.content.Context;

import com.facebook.Session;

import java.util.HashMap;

public class Authentication  {

    public static final String TOKEN = "token";

    public static final String OLDPASS = "oldpassword";

    public static final String NEWPASS = "newpassword";

    public static final String FACEID = "facebook_id";

    public static final String FACETOKEN = "facebook_access_token";

    public static final String DEVICEID = "device_id";

    public static final String DEVICETYPE = "device_type";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String PASSLENGHT = "password_length";

    private static Authentication share;

    private Session mSession;

    public  AuthenticationCallback callBack;

    public Activity activity;

    public  Context context;

    private String host;

    public interface AuthenticationCallback
    {
        public void didSuccessAuthenticated(String string);

        public void didFailAuthenticated(String string);
    }

    private Authentication(Activity act)
    {
        this.activity = act;
        this.context = act.getApplicationContext();
    }

    public void initWitHost(String host)
    {
        this.host = host;
    }

    public static Authentication getInstance(Activity act)
    {
        if (share == null)
        {
            share = new Authentication(act);
        }
        return share;
    }

    public  void didSignIn(HashMap <String, String> hash)
    {
        isValidate();
        hash.put("url",String.format("%s/%s",host,"login"));
        HttpRequestAsync request =  new HttpRequestAsync(activity);
        request.execute(hash);
        request.callBack =  new HttpRequestAsync.AuthenticationCallback() {
            @Override
            public void didSuccessAuthenticated(String string) {
                callBack.didSuccessAuthenticated(string);
            }
            @Override
            public void didFailAuthenticated(String string) {
                callBack.didFailAuthenticated(string);
            }
        };
    }

    public  void didSignInFacebook(HashMap <String, String> hash)
    {
        isValidate();
        hash.put("url",String.format("%s/%s",host,"login"));
        HttpRequestAsync request =  new HttpRequestAsync(activity);
        request.execute(hash);
        request.callBack =  new HttpRequestAsync.AuthenticationCallback() {
            @Override
            public void didSuccessAuthenticated(String string) {
                callBack.didSuccessAuthenticated(string);
            }
            @Override
            public void didFailAuthenticated(String string) {
                callBack.didFailAuthenticated(string);
            }
        };
    }


    public  void didSignUp(HashMap <String, String> hash)
    {
        isValidate();
        hash.put("url",String.format("%s/%s",host,"register"));
        HttpRequestAsync request =  new HttpRequestAsync(activity);
        request.execute(hash);
        request.callBack =  new HttpRequestAsync.AuthenticationCallback() {
            @Override
            public void didSuccessAuthenticated(String string) {
                callBack.didSuccessAuthenticated(string);
            }
            @Override
            public void didFailAuthenticated(String string) {
                callBack.didFailAuthenticated(string);
            }
        };
    }

    public  void didRecoverPassword(HashMap <String, String> hash)
    {
        isValidate();
        hash.put("url",String.format("%s/%s",host,"forgot_password_generate_code"));
        HttpRequestAsync request =  new HttpRequestAsync(activity);
        request.execute(hash);
        request.callBack =  new HttpRequestAsync.AuthenticationCallback() {
            @Override
            public void didSuccessAuthenticated(String string) {
                callBack.didSuccessAuthenticated(string);
            }
            @Override
            public void didFailAuthenticated(String string) {
                callBack.didFailAuthenticated(string);
            }
        };
    }

    public void didChangePassword(HashMap <String, String> hash)
    {
        isValidate();
        hash.put("url",String.format("%s/%s",host,"change_password"));
        HttpRequestAsync request =  new HttpRequestAsync(activity);
        request.execute(hash);
        request.callBack =  new HttpRequestAsync.AuthenticationCallback() {
            @Override
            public void didSuccessAuthenticated(String string) {
                callBack.didSuccessAuthenticated(string);
            }
            @Override
            public void didFailAuthenticated(String string) {
                callBack.didFailAuthenticated(string);
            }
        };
    }

    public void pullUserInformation(HashMap <String, String> hash)
    {
        isValidate();
        hash.put("url",String.format("%s/%s",host,"get_user_information"));
        HttpRequestAsync request =  new HttpRequestAsync(activity);
        request.execute(hash);
        request.callBack =  new HttpRequestAsync.AuthenticationCallback() {
            @Override
            public void didSuccessAuthenticated(String string) {
                callBack.didSuccessAuthenticated(string);
            }
            @Override
            public void didFailAuthenticated(String string) {
                callBack.didFailAuthenticated(string);
            }
        };
    }

    private void isValidate()
    {
        if(!System.connectionStatus(context)) {
            System.showAlert(activity,"Please check your network connection.");
            return;
        }
    }
}
