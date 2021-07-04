package com.example.contadordepasos;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.UpdateItemOperationConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.example.contadordepasos.awsiot.Config;

import java.util.List;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;
import static com.example.contadordepasos.awsiot.Config.COGNITO_POOL_ID;

public class HistorialActivity extends AppCompatActivity  {
    private TextView thedate;
    private Button btngocalendar;
    // Create a new credentials provider
    CognitoCachingCredentialsProvider credentialsProvider;

// Create a connection to DynamoDB
    AmazonDynamoDBClient dbClient ;

// Create a table reference
    Table dbTable ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        thedate = findViewById(R.id.date);
        btngocalendar = findViewById(R.id.btngocalendar);
        /*credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(), COGNITO_POOL_ID, Config.MY_REGION);
        dbClient = new AmazonDynamoDBClient(credentialsProvider);
        new GetAllItemsAsyncTask().execute();*/
        Intent incoming = getIntent();
        String date = incoming.getStringExtra("date");
        thedate.setText(date);

        btngocalendar.setOnClickListener(v -> {
            Intent intent = new Intent(HistorialActivity.this,CalendarActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Retrieve all the memos from the database
     * @return the list of memos
     */
    public List<Document> getAllMemos() {
        return dbTable.query(new Primitive(credentialsProvider.getCachedIdentityId())).getAllResults();
    }
    public void readTable(List<Document> das){
        for(int i=0; i<das.size(); i++){
            System.out.println(das.get(i));
        }

    }

    public void connect(){
        try {
        } catch (final Exception e) {
            Log.e("LOG_TAG", "Connection error.", e);
        }
    }
    /**
     * Async Task for handling the network retrieval of all the memos in DynamoDB
     */
    private class GetAllItemsAsyncTask extends AsyncTask<Void, Void, List<Document>> {
        @Override
        protected List<Document> doInBackground(Void... params) {
            dbTable = Table.loadTable(dbClient, "wx_data");
            return getAllMemos();
        }
    }
}




