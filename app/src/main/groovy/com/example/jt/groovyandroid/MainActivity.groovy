package com.example.jt.groovyandroid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBarActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnBackground
import com.arasthel.swissknife.annotations.OnClick
import com.arasthel.swissknife.annotations.OnUIThread
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic
import org.json.JSONObject

@CompileStatic class MainActivity extends ActionBarActivity {

    @InjectView(R.id.HelloTextView) TextView mHello
    @InjectView(R.id.textView) TextView mTextView
    @InjectView(R.id.textView2) TextView mTextView2
    @InjectView(R.id.button) Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SwissKnife.inject(this)
        loadWeather()

    }

    @OnClick(R.id.button)
    public void onClick(){
        Intent intent = new Intent(this, SideActivity.class)
        startActivities(intent)
    }

    @OnBackground void loadWeather() {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=London,uk"
        InputStream inputStream = new URL(url).openStream()
        def groovyString = new JsonSlurper().parse([:], new URL(url));
        def jsonObject = (JSONObject) groovyString;
        def weatherArray = jsonObject.getJSONArray("weather")
        def weather = weatherArray.getJSONObject(0)

        putString(weather)

    }

    @OnUIThread def putString(JSONObject weather){
        def weatherId = weather.getString("id")
        def weatherMain = weather.getString("main")
        def weatherDescription = weather.getString("description")
        def weathericon = weather.getString("icon")
        mHello.setText(weatherId)
        mTextView.setText(weatherMain)
        mTextView2.setText(weatherDescription)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu)
        true
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId()

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


}
