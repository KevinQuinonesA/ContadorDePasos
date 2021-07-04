package com.example.contadordepasos;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler;
    private Runnable _timer1;
    private int stepCounter = 0;
    private int lastStep = 0;
    private boolean showedGoalReach = false;
    private Button start;
    private ImageView end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        start.setOnClickListener(view -> controlFunction());
        end.setOnClickListener(view -> {
            DebugActivity.start = false;
            // end.setVisibility(View.INVISIBLE);
            SharedPreferences preferences = getSharedPreferences("StepDay",Context.MODE_PRIVATE);
            DebugActivity.mStepCounter = 0;
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear().apply();
            updateView();

        });
        setSupportActionBar(toolbar);
        stepCounter = (int)DebugActivity.mStepCounter;
        mHandler = new Handler();
        startRepeatingTask();

    }

    private void controlFunction(){
        // end.setVisibility(View.VISIBLE);
        DebugActivity.start = !DebugActivity.start;
        if(DebugActivity.start){
           start.setText("PAUSAR ACTIVIDAD ");
        }else{
            start.setText("INICIAR ACTIVDAD DEL DIA");
        }
    }

    private void updateView(){
        if(DebugActivity.mStepCounter > stepCounter) {
            stepCounter = (int)DebugActivity.mStepCounter;
            if(stepCounter >= 500 && !showedGoalReach){
                showedGoalReach = true;
                Context context = getApplicationContext();
                CharSequence text = "Buen trabajo! Alcanzaste la meta!";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            TextView stepCountStr = (TextView) this.findViewById(R.id.maintv1);
            stepCountStr.setText(new String("Pasos Contados: " + stepCounter));
            TextView progressText = (TextView) this.findViewById(R.id.maintv2);
            progressText.setText(new String("Pasos Meta: " + 500 + ". Progreso: " + stepCounter + " / 500"));

            ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
            ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", lastStep, stepCounter); //animar solo desde elm ultimo paso al paso actual
            animation.setDuration(5000); // en milisegundos
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
            lastStep = stepCounter;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menú; esto agrega elementos a la barra de acción si está presente.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar los clics de elementos de la barra de acciones aquí
        // La barra de acción manejará automáticamente los clics en el botón Inicio / Arriba, siempre que
        // especifique una actividad principal en AndroidManifest.xml.
        final Context context = this;

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_debug) {
            // Intent intent = new Intent(context, DebugActivity.class);
            // startActivity(intent);
            return true;
        }

        else {
            if (item.getItemId() == R.id.historial_debug) {
                Intent intent = new Intent(context, HistorialActivity.class);
                String date = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
                intent.putExtra("date",date);
                startActivity(intent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateView();
            } finally {
                mHandler.postDelayed(mStatusChecker, 500);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
}
