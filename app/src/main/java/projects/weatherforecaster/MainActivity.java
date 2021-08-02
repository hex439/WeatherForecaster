package projects.weatherforecaster;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public String city = "cairns,au";
    public String api = "15c50ca47bb11c7c3e7f90582de67a37";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new getWeather().execute();

        ImageButton refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(v -> new getWeather().execute());
    }

    @SuppressLint("StaticFieldLeak")
    public class getWeather extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.location).setVisibility(View.GONE);
            findViewById(R.id.updated_location).setVisibility(View.GONE);
            findViewById(R.id.status).setVisibility(View.GONE);
            findViewById(R.id.temperature).setVisibility(View.GONE);
            findViewById(R.id.min_temp).setVisibility(View.GONE);
            findViewById(R.id.max_temp).setVisibility(View.GONE);
            findViewById(R.id.container_sunrise).setVisibility(View.GONE);
            findViewById(R.id.container_sunset).setVisibility(View.GONE);
            findViewById(R.id.container_wind).setVisibility(View.GONE);
            findViewById(R.id.container_pressure).setVisibility(View.GONE);
            findViewById(R.id.container_humidity).setVisibility(View.GONE);
            findViewById(R.id.container_info).setVisibility(View.GONE);
            findViewById(R.id.error).setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {
            return "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + api;
        }

        @Override
        protected void onPostExecute(String link) {
            super.onPostExecute(link);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, link, response -> {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject main = jsonObject.getJSONObject("main");
                    JSONObject sys = jsonObject.getJSONObject("sys");
                    JSONObject wind = jsonObject.getJSONObject("wind");
                    JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
                    long updatedAt = jsonObject.getLong("dt");
                    String pattern = "dd/MM/yyyy hh:mm a";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
                    String updatedAtText = "Updated at: " + simpleDateFormat.format(new Date(updatedAt * 1000));
                    String temp = Math.round(main.getDouble("temp") * 2) / 2 + "°C";
                    String tempMin = "Min Temp: " + Math.round(main.getDouble("temp_min") * 2) / 2 + "°C";
                    String tempMax = "Max Temp: " + Math.round(main.getDouble("temp_max") * 2) / 2 + "°C";
                    String pressure_val = main.getString("pressure") + " Pa";
                    String humidity_val = main.getString("humidity") + "%";
                    long sunrise_val = sys.getLong("sunrise");
                    long sunset_val = sys.getLong("sunset");
                    String windSpeed = wind.getString("speed") + " m/s";
                    String weatherDescription = weather.getString("description");
                    String address = jsonObject.getString("name") + ", " + sys.getString("country");

                    TextView location = findViewById(R.id.location);
                    TextView updated_at = findViewById(R.id.updated_location);
                    TextView status = findViewById(R.id.status);
                    TextView temperature = findViewById(R.id.temperature);
                    TextView temp_min = findViewById(R.id.min_temp);
                    TextView temp_max = findViewById(R.id.max_temp);
                    TextView sunrise = findViewById(R.id.sunrise);
                    TextView sunset = findViewById(R.id.sunset);
                    TextView windView = findViewById(R.id.wind);
                    TextView pressure = findViewById(R.id.pressure);
                    TextView humidity = findViewById(R.id.humidity);

                    location.setText(address);
                    updated_at.setText(updatedAtText);
                    weatherDescription = weatherDescription.substring(0, 1).toUpperCase() + weatherDescription.substring(1).toLowerCase();
                    status.setText(weatherDescription);
                    temperature.setText(temp);
                    temp_min.setText(tempMin);
                    temp_max.setText(tempMax);
                    sunrise.setText(simpleDateFormat.format(new Date(sunrise_val * 1000)));
                    sunset.setText(simpleDateFormat.format(new Date(sunset_val * 1000)));
                    pressure.setText(pressure_val);
                    humidity.setText(humidity_val);
                    windView.setText(windSpeed);

                    findViewById(R.id.loader).setVisibility(View.GONE);
                    findViewById(R.id.location).setVisibility(View.VISIBLE);
                    findViewById(R.id.updated_location).setVisibility(View.VISIBLE);
                    findViewById(R.id.status).setVisibility(View.VISIBLE);
                    findViewById(R.id.temperature).setVisibility(View.VISIBLE);
                    findViewById(R.id.min_temp).setVisibility(View.VISIBLE);
                    findViewById(R.id.max_temp).setVisibility(View.VISIBLE);
                    findViewById(R.id.container_sunrise).setVisibility(View.VISIBLE);
                    findViewById(R.id.container_sunset).setVisibility(View.VISIBLE);
                    findViewById(R.id.container_wind).setVisibility(View.VISIBLE);
                    findViewById(R.id.container_pressure).setVisibility(View.VISIBLE);
                    findViewById(R.id.container_humidity).setVisibility(View.VISIBLE);
                    findViewById(R.id.container_info).setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    findViewById(R.id.loader).setVisibility(View.GONE);
                    findViewById(R.id.error).setVisibility(View.VISIBLE);
                }
            }, error -> Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show());
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}