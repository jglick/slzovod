package com.myopenid.typrase.slzovod;
import android.app.Activity;
import android.os.Bundle;
public class MainActivity extends Activity {
    Universe universe;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MainView(this));
    }
}
