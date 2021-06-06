package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class tracking extends AppCompatActivity
{
    private GpsTracker gpsTracker;
    private TextView tvLatitude,tvLongitude;
    private static final String url = "https://redxsofts.com/raza/user.php";
    String lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        tvLatitude=findViewById(R.id.textView);
        tvLongitude=findViewById(R.id.textView2);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        sendRequestAfterOneSecond();


    }



    public void sendRequestAfterOneSecond()
    {
        gpsTracker = new GpsTracker(tracking.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
             lat=String.valueOf(latitude);
             lon=String.valueOf(longitude);
            tvLatitude.setText(lat);
            tvLongitude.setText(lon);
        }else{
            gpsTracker.showSettingsAlert();
        }

        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
//                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> param=new HashMap<String,String>();
                param.put("latitude",lat);
                param.put("longitude",lon);
                return param;
            }
        };

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

        refresh(1000);
    }

    private void refresh(int i)
    {
        final Handler handler= new Handler();
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                sendRequestAfterOneSecond();
            }
        };

        handler.postDelayed(runnable,i);

    }




}