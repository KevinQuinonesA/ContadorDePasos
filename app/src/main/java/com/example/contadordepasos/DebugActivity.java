package com.example.contadordepasos;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.example.contadordepasos.awsiot.AWS;
import com.example.contadordepasos.awsiot.Config;
import com.example.contadordepasos.datainformation.PerfilInformation;
import com.example.contadordepasos.models.DataSteps;
import com.example.contadordepasos.models.SensorAcelerometer;
import com.example.contadordepasos.models.SensorStepCounter;
import com.example.contadordepasos.models.SensorUse;
import com.example.contadordepasos.utils.MapperUtils;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.example.contadordepasos.awsiot.Config.COGNITO_POOL_ID;


public class DebugActivity extends AppCompatActivity implements SensorEventListener {

    private static int SMOOTHING_WINDOW_SIZE = 20;

    public static SensorManager mSensorManager;
    public static Sensor mSensorCount, mSensorAcc;
    private float mRawAccelValues[] = new float[3];

    // suavizar las variables de la señal del acelerómetro
    private float mAccelValueHistory[][] = new float[3][SMOOTHING_WINDOW_SIZE];
    private float mRunningAccelTotal[] = new float[3];
    private float mCurAccelAvg[] = new float[3];
    private int mCurReadIndex = 0;

    public static float mStepCounter = 0;
    public static float mStepCounterAndroid = 0;
    public static float mInitialStepCount = 0;

    private double mGraph1LastXValue = 0d;
    private double mGraph2LastXValue = 0d;

    private LineGraphSeries<DataPoint> mSeries1;
    private LineGraphSeries<DataPoint> mSeries2;

    private double lastMag = 0d;
    private double avgMag = 0d;
    private double netMag = 0d;

    //variables de detección de picos
    private double lastXPoint = 1d;
    double stepThreshold = 1.0d;
    double noiseThreshold = 2d;
    private int windowSize = 10;

    CognitoCachingCredentialsProvider credentialsProvider;
    private AnimationDrawable animacion;
    private ImageView loading;
    AWS aws;
    public static boolean start = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        Objects.requireNonNull(getSupportActionBar()).hide();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        // mStepCounter = readPreferences();
        loading = findViewById(R.id.loading);
        loading.setBackgroundResource(R.drawable.cargando);
        animacion = (AnimationDrawable) loading.getBackground();
        animacion.start();
        animationSetup();
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                COGNITO_POOL_ID,
                Config.MY_REGION
        );
        aws = new AWS(credentialsProvider);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorCount = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mSensorAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensorCount, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mSensorAcc, SensorManager.SENSOR_DELAY_UI);
        mSeries1 = new LineGraphSeries<>();
        mSeries2 = new LineGraphSeries<>();
    }

    //Botón para enlazar el Home desde la vista de DEBUG
    public void onClickBtn(View v)
    {
        System.out.println("SALTA");
        Intent i = new Intent(this, MainActivity.class);
        this.startActivity(i);
    }
    //Botón para enlazar el Home desde la vista de Datos
    public void onClickBtn1(View v)
    {
        System.out.println("SALTA");
        Intent i = new Intent(DebugActivity.this, PerfilInformation.class);
        System.out.println("SALTA");
        startActivity(i);
    }
    @Override
    public void onSensorChanged (SensorEvent e)
    {
        if(!start){
            return;
        }
        switch (e.sensor.getType()) {
            case Sensor.TYPE_STEP_COUNTER:
                if (mInitialStepCount == 0.0) {
                    mInitialStepCount = e.values[0];
                }
                mStepCounterAndroid = e.values[0];
                break;
            case Sensor.TYPE_ACCELEROMETER:
                mRawAccelValues[0] = e.values[0];
                mRawAccelValues[1] = e.values[1];
                mRawAccelValues[2] = e.values[2];

                lastMag = Math.sqrt(Math.pow(mRawAccelValues[0], 2) + Math.pow(mRawAccelValues[1], 2) + Math.pow(mRawAccelValues[2], 2));

                //Source: https://github.com/jonfroehlich/CSE590Sp2018
                for (int i = 0; i < 3; i++) {
                    mRunningAccelTotal[i] = mRunningAccelTotal[i] - mAccelValueHistory[i][mCurReadIndex];
                    mAccelValueHistory[i][mCurReadIndex] = mRawAccelValues[i];
                    mRunningAccelTotal[i] = mRunningAccelTotal[i] + mAccelValueHistory[i][mCurReadIndex];
                    mCurAccelAvg[i] = mRunningAccelTotal[i] / SMOOTHING_WINDOW_SIZE;
                }
                mCurReadIndex++;
                if (mCurReadIndex >= SMOOTHING_WINDOW_SIZE) {
                    mCurReadIndex = 0;
                }

                avgMag = Math.sqrt(Math.pow(mCurAccelAvg[0], 2) + Math.pow(mCurAccelAvg[1], 2) + Math.pow(mCurAccelAvg[2], 2));

                netMag = lastMag - avgMag; //removes gravity effect

                //actualizar puntos del grafico
                mGraph1LastXValue += 1d;
                mSeries1.appendData(new DataPoint(mGraph1LastXValue, lastMag), true, 60);

                mGraph2LastXValue += 1d;
                mSeries2.appendData(new DataPoint(mGraph2LastXValue, netMag), true, 60);
        }

        //TextView calculatedStep = (TextView) this.findViewById(R.id.tv1);
        //TextView androidStep = (TextView) this.findViewById(R.id.tv2);

        peakDetection();

        //calculatedStep.setText(new String("Pasos contados: " + (int)mStepCounter));
        //Android siempre devuelve el total de pasos desde el reinicio, así que restamos todos los pasos registrados antes de que comenzara la aplicación
        //androidStep.setText(new String("Pasos Contados Android: " + (int)(mStepCounterAndroid - mInitialStepCount)));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void peakDetection(){

        /* Algoritmo de detección de picos derivado de: Un servicio de contador de pasos para dispositivos habilitados para Java que utilizan un acelerómetro incorporado, Mladenov et al.
         * Threshold, stepThreshold se obtuvo observando el gráfico de pasos de las personas
         * SUPUESTOS:
         * El teléfono se sostiene verticalmente en orientación vertical para obtener mejores resultados
         */

        double highestValX = mSeries2.getHighestValueX();

        if(highestValX - lastXPoint < windowSize){
            return;
        }

        Iterator<DataPoint> valuesInWindow = mSeries2.getValues(lastXPoint,highestValX);

        lastXPoint = highestValX;

        double forwardSlope = 0d;
        double downwardSlope = 0d;

        List<DataPoint> dataPointList = new ArrayList<DataPoint>();
        valuesInWindow.forEachRemaining(dataPointList::add); //This requires API 24 or higher

        for(int i = 0; i<dataPointList.size(); i++){
            if(i == 0) continue;
            else if(i < dataPointList.size() - 1){
                forwardSlope = dataPointList.get(i+1).getY() - dataPointList.get(i).getY();
                downwardSlope = dataPointList.get(i).getY() - dataPointList.get(i - 1).getY();

                if(forwardSlope < 0 && downwardSlope > 0 && dataPointList.get(i).getY() > stepThreshold && dataPointList.get(i).getY() < noiseThreshold){
                    mStepCounter+=1;
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    if(mSensorCount!=null){
                        SensorStepCounter sensorUse = new SensorStepCounter("stepcounter");
                        sensorUse.setStepcount(mStepCounterAndroid);
                        DataSteps dataSteps = new DataSteps("caminata",date,mStepCounter,sensorUse);
                        aws.publicar(MapperUtils.MapperObj2Json(dataSteps).toString(), Config.topic_step_sensor_publicar);
                    }else{
                        SensorAcelerometer sensorUse = new SensorAcelerometer("acelerometer");
                        sensorUse.setX(mRawAccelValues[0]);
                        sensorUse.setY(mRawAccelValues[1]);
                        sensorUse.setZ(mRawAccelValues[2]);
                        DataSteps dataSteps = new DataSteps("caminata",date,mStepCounter,sensorUse);
                        aws.publicar(MapperUtils.MapperObj2Json(dataSteps).toString(), Config.topic_acelerometer_publicar);
                    }
                    savePreferences(mStepCounter);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    private void animationSetup(){
        Animation transicion = AnimationUtils.loadAnimation(this, R.anim.mitransicion);
        loading.startAnimation(transicion);
        transicion.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                stopAnimation();
                //redirectUI();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void stopAnimation(){
        animacion.stop();
    }
    private void redirectUI() {
        Intent intent = new Intent(DebugActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
    private void savePreferences(float stepcounter){
        SharedPreferences preferences = getSharedPreferences("StepDay",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("stepcounter",stepcounter);
        editor.commit();
    }
    private float readPreferences(){
        SharedPreferences preferences = getSharedPreferences("StepDay",Context.MODE_PRIVATE);
        return preferences.getFloat("stepcounter",0);
    }
}
