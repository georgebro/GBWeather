package com.geobro.gbweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText user_field;
    private Button main_button;
    private TextView result_Info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_field.findViewById(R.id.user_field);
        main_button.findViewById(R.id.main_button);
        result_Info.findViewById(R.id.result_Info);

    // *event handler when the button is clicked*
    main_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (user_field.getText().toString().trim().equals(""))//if empty field
            {
                Toast.makeText(MainActivity.this,R.string.no_user_input,Toast.LENGTH_LONG).show();
            }
            else{
                String city = user_field.getText().toString();
                String key = "d8ec39a838bffb2aee789581572a04f8";
                String url = "http://api.openweathermap.org/data/2.5/weather?q="+ city +
                             "&appid="+key+"&units=metric";
                new GetURLData().execute(url);
            }
        }
    });
    }

    // Nested class
    private class GetURLData extends AsyncTask<String,String,String> {

        // *status line in the result_field*
        protected void OnPreExecute(){
            super.onPreExecute();
            result_Info.setText("waiting...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {

                // *Open Connection*
                // *first element from ArrayStrings = our url address*
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // *reading information from url address*
                InputStream stream = connection.getInputStream();
                // *renovation the url stream to simple string
                reader = new BufferedReader(new InputStreamReader(stream));

                // * writing the new information to our String Buffer*
                StringBuffer ourBuffer = new StringBuffer();
                String line = "";

                    while((reader.readLine()) != null){
                        ourBuffer.append(line).append("\n");
                    }
                        return ourBuffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch(IOException e){
                        e.printStackTrace();
                    }
                }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                result_Info.setText("Temperature is:" + jsonObject.getJSONObject("main").getDouble("temp")+"deg");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}