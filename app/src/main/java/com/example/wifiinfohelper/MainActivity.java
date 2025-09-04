package com.example.wifiinfohelper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        setContentView(textView);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_CODE
            );
            textView.setText("Grant location permission and reopen app!");
            return;
        }

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();

        String localIp = intToIp(dhcpInfo.ipAddress);
        String gateway = intToIp(dhcpInfo.gateway);
        String netmask = intToIp(dhcpInfo.netmask);
        String dns1 = intToIp(dhcpInfo.dns1);

        String result = "Local IP: " + localIp + "\n"
                + "Gateway: " + gateway + "\n"
                + "Netmask: " + netmask + "\n"
                + "DNS1: " + dns1 + "\n";

        textView.setText(result);

        try {
            File file = new File(Environment.getExternalStorageDirectory(), "wifi_info.txt");
            FileWriter writer = new FileWriter(file);
            writer.write(result);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            textView.setText("Failed to write file: " + e.getMessage());
        }
    }

    private String intToIp(int ip) {
        try {
            return InetAddress.getByAddress(new byte[]{
                    (byte) (ip & 0xff),
                    (byte) (ip >> 8 & 0xff),
                    (byte) (ip >> 16 & 0xff),
                    (byte) (ip >> 24 & 0xff)}).getHostAddress();
        } catch (UnknownHostException e) {
            return "0.0.0.0";
        }
    }
}
