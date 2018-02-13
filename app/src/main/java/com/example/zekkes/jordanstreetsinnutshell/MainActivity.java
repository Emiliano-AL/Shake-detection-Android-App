package com.example.zekkes.jordanstreetsinnutshell;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SensorEventListener{
    TextView xText , yText , zText , counterTv; // DECIDED TO LEAVE THEM IN CASE I WANT TO MODIFY AND TEST THE VALUES OF THE AXIS
    Button startButton , stopButton , resetButton;
    Sensor mySensor;
    SensorManager mySensorManager;
    boolean start = false;
    int numberOfHolesAndBumbs = 0;
    float accel;
    float accelCurrent;
    float accelLast;
    int shakeReset = 2500;
    long timeStamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CREATE SENSOR MANAGER
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // CREATE ACCELERATION SENSOR
        mySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // REGISTERING OUR SENSOR
        mySensorManager.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_UI);

        // ASSIGNING OUR TEXT VIEWS (THIS IS USED IN TESTING AND I DECIDED TO LEAVE IT IN CASE ERRORS AND MODIFYING )
        //xText = (TextView) findViewById(R.id.xAxis); // X AXIS
        //yText = (TextView) findViewById(R.id.yAxis); // Y AXIS
        //zText = (TextView) findViewById(R.id.zAxis); // Z AXIS
        counterTv = (TextView) findViewById(R.id.counterText);

        // ASSIGNING OUR BUTTONS
        startButton = (Button) findViewById(R.id.startBtn);
        stopButton = (Button) findViewById(R.id.stopBtn);
        resetButton = (Button) findViewById(R.id.resetBtn);

        // SETTING ACCELERATION VALUES
        accel = 0.00f;
        accelCurrent = SensorManager.GRAVITY_EARTH;
        accelLast = SensorManager.GRAVITY_EARTH;



        // CREATING OUR CLICK LISTENER
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!start) {
                    Toast.makeText(getApplicationContext(), "Application running", Toast.LENGTH_SHORT).show();
                    start = true; // IF CLICKED THEN START SHOWING IT ON THE UI
                }
                else {
                    Toast.makeText(getApplicationContext(),"Application is already running.",Toast.LENGTH_SHORT).show();
                }


            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start) {
                    Toast.makeText(getApplicationContext(), "Application stopped running", Toast.LENGTH_SHORT).show();
                    start = false; // IF CLICKED THEN STOP SHOWING IT ON THE UI
                }
                else {
                    Toast.makeText(getApplicationContext(),"Application is not running , please make sure to run the application first before pressing the stop button.",Toast.LENGTH_LONG).show();
                }
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start) {
                    Toast.makeText(getApplicationContext(), "Application Reset", Toast.LENGTH_SHORT).show();
                    numberOfHolesAndBumbs = 0;
                    counterTv.setText("Counter");
                }
                else {
                    Toast.makeText(getApplicationContext(),"Application is not running , please make sure to run the application first before resetting it.",Toast.LENGTH_LONG).show();

                }
            }
        });


    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (start == true) {
                // STORING THE VALUES OF THE AXIS
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                // ACCELEROMETER LAST READ EQUAL TO THE CURRENT ONE
                accelLast = accelCurrent;
                // QUICK MAFS TO CALCULATE THE ACCELERATION
                accelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
                // DELTA BETWEEN THE CURRENT AND THE LAST READ OF THE ACCELEROMETER
                float delta = accelCurrent - accelLast;
                // QUICK MAFS TO CALCULATE THE ACCEL THAT WILL DECLARE IF IT SHAKED OR NOT
                accel = accel * 0.9f + delta;
                // DID IT SHAKE??
                if (accel > 5) {
                    final long timenow = System.currentTimeMillis();
                    if(timeStamp + shakeReset  > timenow){
                        return;
                    }
                    timeStamp = timenow;
                    numberOfHolesAndBumbs++;
                    counterTv.setText(numberOfHolesAndBumbs + " ");
                }
            }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mySensorManager.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mySensorManager.unregisterListener(this);
    }
}
