package com.example.authentication.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity  implements View.OnClickListener {

    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = getApplicationContext();
        Authentication.getInstance(this).initWitHost(ctx.getString(R.string.host));
        //signUp();
        //signIn();
        //changPassword();
        //recoveryPassword();
       // signUpFacebook();
        ((Button)findViewById(R.id.button)).setOnClickListener(this);
    }

    public void recoveryPassword()
    {
        Authentication au =  Authentication.getInstance(this);
        au.didRecoverPassword(System.build(
                au.EMAIL, "tthufo@gmail.com"
        ));

        au.callBack = new Authentication.AuthenticationCallback() {
            @Override
            public void didSuccessAuthenticated(String string) {
                System.showAlert(MainActivity.this,"Done");
            }

            @Override
            public void didFailAuthenticated(String string) {
                System.showAlert(MainActivity.this, "Failed");
            }
        };
    }

    public void signUp()
    {
        Authentication au =  Authentication.getInstance(this);
        au.didSignUp(System.build(
                au.EMAIL, "tthufokkoo@gmail.com",
                au.USERNAME, "tthhuuffoou",
               // au.FACETOKEN, "sdfsdfdsfdsfdsfdsfs",    //optional
                //au.FACEID, "sdfsdfdsfdsfdsfdsfs",    //optional
                au.PASSWORD, System.md5("123456"),
                au.PASSLENGHT, "6",
                au.DEVICEID, "sfsfsfsdfsdfsdfsfs",    //optional
                au.DEVICETYPE, "android"
        ));

        au.callBack = new Authentication.AuthenticationCallback() {
            @Override
            public void didSuccessAuthenticated(String string) {
                System.showAlert(MainActivity.this,"Done");
            }

            @Override
            public void didFailAuthenticated(String string) {
                System.showAlert(MainActivity.this, string);
            }
        };
    }

    public void signIn()
    {
        Authentication au =  Authentication.getInstance(this);
        au.didSignIn(System.build(
                //au.EMAIL, "tthufo@gmail.com",               //optional
                au.USERNAME, "trangphuong",
                au.PASSWORD, System.md5("123456"),
                //au.FACEID, "sdfsdfdsfdsfdsfdsfs",    //optional
                au.DEVICEID, "sfsfsfsdfsdfsdfsfs",    //optional
                au.DEVICETYPE, "android"              //optional
        ));

        au.callBack = new Authentication.AuthenticationCallback() {
            @Override
            public void didSuccessAuthenticated(String string) {
                System.showAlert(MainActivity.this,string);

                try {
                    JSONObject obj = new JSONObject(string);
                    String token = obj.getString("code");
                    Authentication get =  Authentication.getInstance(MainActivity.this);
                    get.pullUserInformation(System.build(
                            get.USERNAME, token
                    ));
                    get.callBack = new Authentication.AuthenticationCallback() {
                        @Override
                        public void didSuccessAuthenticated(String string) {
                            System.showAlert(MainActivity.this, string);
                        }

                        @Override
                        public void didFailAuthenticated(String string) {
                            System.showAlert(MainActivity.this, string);
                        }
                    };
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void didFailAuthenticated(String string) {
                System.showAlert(MainActivity.this, string);
            }
        };
    }

    public void signUpFacebook()
    {
        Authentication au =  Authentication.getInstance(this);
        au.didSignInFacebook(System.build(
                au.FACEID, "sdfsdfdsfdsfdsfdsfs",
                au.FACETOKEN, "sdfdsfdsfdsfsdf",   //optional
                au.DEVICEID, "sfsfsfsdfsdfsdfsfs",    //optional
                au.DEVICETYPE, "android"
        ));

        au.callBack = new Authentication.AuthenticationCallback() {
            @Override
            public void didSuccessAuthenticated(String string) {
                System.showAlert(MainActivity.this,"Done");
            }

            @Override
            public void didFailAuthenticated(String string) {
                System.testLog(string);
                System.showAlert(MainActivity.this, string);
            }
        };
    }

    public void changPassword() {
        Authentication au = Authentication.getInstance(this);
        au.didChangePassword(System.build(

                au.TOKEN, "dsfdsfdsfdsfdsfdsf",
                au.OLDPASS, "oldpassword",
                au.NEWPASS, "newpassword"
        ));

        au.callBack = new Authentication.AuthenticationCallback() {
            @Override
            public void didSuccessAuthenticated(String string) {
                System.showAlert(MainActivity.this, "Done");
            }

            @Override
            public void didFailAuthenticated(String string) {
                System.showAlert(MainActivity.this, string);
            }
        };
    }


    @Override
    public void onClick(View v) {
        signUp();
    }
}
