package com.authentication.android;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

public class Authentication implements RegisterBroadCast.BroadCastCallBack {

    public static final String TOKEN = "token";

    public static final String EXTRAS = "extra_fields";

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

    public AuthenticationCallback callBack;

    public Activity activity;

    public Context context;

    public String device_token;

    public String facebook_id;

    public String facebook_token;

    public String host;

    private String sender_id;

    AsyncTask<Void, Void, Void> registerTask;

    public Facebook facebook;

    @Override
    public void didReicive(Intent result) {
        SystemHelper.hideDialog();
        if (result.getStringExtra("device_token") != null) {
            device_token = result.getStringExtra("device_token");
        }
    }

    public interface AuthenticationCallback {
        public void didSuccessAuthenticated(String string);

        public void didFailAuthenticated(String string);
    }

    public Authentication() {



    }

    private Authentication(Activity act) {
        //if(act != null)
        {
            this.activity = act;
            this.context = act.getApplicationContext();
            RegisterBroadCast broadCast = new RegisterBroadCast();
            broadCast.setOnEventListener(this);
        }
    }

        public void initWitHost(String host, String senderId, String appId) {

        this.host = host;

        facebook = new Facebook(appId);

        if (senderId != null) {

            GCMRegistrar.checkDevice(activity);

            GCMRegistrar.checkManifest(activity);

            this.sender_id = senderId;

            final String regId = GCMRegistrar.getRegistrationId(activity);

            if (regId.equalsIgnoreCase("")) {
                didCheckForDeviceId(regId, activity);
            } else {
                device_token = regId;
            }
        }
    }

    public static Authentication getInstance(Activity act) {
        if (share == null) {
            share = new Authentication(act);
        }
        return share;
    }

    public void didSignOut() {
        if (facebook.isSessionValid() && facebook != null) {
            registerTask = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        facebook.logout(context);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    registerTask = null;
                }
            };

            registerTask.execute(null, null, null);
        }
//        GCMRegistrar.setRegisteredOnServer(context, false);
//        GCMRegistrar.unregister(context);
        facebook_token = null;
        facebook_id = null;
    }

    public void didSignInFacebook() {
        if (!facebook.isSessionValid()) {
            facebook.authorize(activity, new String[]{"publish_stream"}, new LoginDialogListener());
        } else {
            didRequestInfor();
        }
    }

    private void didRequestInfor() {
        registerTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                getProfileInformation();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                registerTask = null;
            }
        };

        registerTask.execute(null, null, null);
    }

    class LoginDialogListener implements Facebook.DialogListener {

        public void onComplete(Bundle values) {
            try {
                didRequestInfor();
            } catch (Exception error) {
                SystemHelper.testLog(error.toString());
            }
        }

        public void onFacebookError(FacebookError error) {
            SystemHelper.testLog("FacebookError");
        }

        public void onError(DialogError error) {
            SystemHelper.testLog("DialogError");
        }

        public void onCancel() {
            SystemHelper.testLog("CancelError");
        }
    }

    public void getProfileInformation() {
        try {
            JSONObject profile = Util.parseJson(facebook.request("me"));
            final String mUserId = profile.getString("id");
            final String mUserToken = facebook.getAccessToken();
            final String mUserName = profile.getString("name");
            final String mUserEmail = profile.getString("email");

            activity.runOnUiThread(new Runnable() {

                public void run() {
                    Log.e("FaceBook_Profile", "" + mUserId + "\n" + mUserToken + "\n" + mUserName + "\n" + mUserEmail);

                    facebook_id = mUserId;

                    facebook_token = mUserToken;

                    didSignInFacebook(SystemHelper.build(
                            FACEID, facebook_id,
                            FACETOKEN, facebook_token,
                            DEVICEID, device_token,
                            DEVICETYPE, "android"
                    ));
                }
            });

        } catch (FacebookError e) {

            e.printStackTrace();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (JSONException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void didSignInFacebook(HashMap<String, String> hash) {
        isValidate();
        hash.put("url", String.format("%s/%s", host, "login"));
        HttpRequestAsync request = new HttpRequestAsync(activity);
        request.execute(hash);
        request.callBack = new HttpRequestAsync.AuthenticationCallback() {
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

    private void didCheckForDeviceId(String deviceId, Activity act) {
        if (deviceId.equals("")) {
            SystemHelper.showDialog(act, "", "Registering Device");
            GCMRegistrar.register(act, this.sender_id);
        } else {
            if (GCMRegistrar.isRegisteredOnServer(act)) {
                SystemHelper.testLog("Already Registerd Device");
            } else {
                registerTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        GCMRegistrar.setRegisteredOnServer(context, true);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        registerTask = null;
                    }
                };

                registerTask.execute(null, null, null);
            }
        }
    }


    public void didSignIn(HashMap<String, String> hash) {
        isValidate();
        hash.put("url", String.format("%s/%s", host, "login"));
        HttpRequestAsync request = new HttpRequestAsync(activity);
        request.execute(hash);
        request.callBack = new HttpRequestAsync.AuthenticationCallback() {
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

    public void didSignUp(HashMap<String, String> hash) {
        isValidate();
        hash.put("url", String.format("%s/%s", host, "register"));
        HttpRequestAsync request = new HttpRequestAsync(activity);
        request.execute(hash);
        request.callBack = new HttpRequestAsync.AuthenticationCallback() {
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

    public void didRecoverPassword(HashMap<String, String> hash) {
        isValidate();
        hash.put("url", String.format("%s/%s", host, "forgot_password_generate_code"));
        HttpRequestAsync request = new HttpRequestAsync(activity);
        request.execute(hash);
        request.callBack = new HttpRequestAsync.AuthenticationCallback() {
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

    public void didChangePassword(HashMap<String, String> hash) {
        isValidate();
        hash.put("url", String.format("%s/%s", host, "change_password"));
        HttpRequestAsync request = new HttpRequestAsync(activity);
        request.execute(hash);
        request.callBack = new HttpRequestAsync.AuthenticationCallback() {
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

    public void pullUserInformation(HashMap<String, String> hash) {
        isValidate();
        hash.put("url", String.format("%s/%s", host, "get_user_information"));
        HttpRequestAsync request = new HttpRequestAsync(activity);
        request.execute(hash);
        request.callBack = new HttpRequestAsync.AuthenticationCallback() {
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

    private void isValidate() {
        if (!SystemHelper.connectionStatus(context)) {
            SystemHelper.showAlert(activity, "Please check your network connection.");
            return;
        }
    }
}