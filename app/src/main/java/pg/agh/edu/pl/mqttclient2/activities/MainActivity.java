package pg.agh.edu.pl.mqttclient2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import pg.agh.edu.pl.mqttclient2.R;
import pg.agh.edu.pl.mqttclient2.gui.SensorsListAdapter;
import pg.agh.edu.pl.mqttclient2.model.MQTTSensor;
import pg.agh.edu.pl.mqttclient2.model.sensors.SonicSensor;
import pg.agh.edu.pl.mqttclient2.model.sensors.TemperatureSensor;

public class MainActivity extends AppCompatActivity {
    ListView sensorsList = null;
    SensorsListAdapter sensorsListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    
    private void init() {
        initSensorsList();
    }

    private void initSensorsList() {
        ArrayList<MQTTSensor> list =  new ArrayList<MQTTSensor>() {{
            add(new SonicSensor());
            add(new TemperatureSensor());
        }};
        this.sensorsListAdapter = new SensorsListAdapter(getBaseContext(), R.layout.list_row_sensor, list);
        this.sensorsList = (ListView) findViewById(R.id.sensors_list_view);
        sensorsList.setAdapter(sensorsListAdapter);
        sensorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MQTTSensor sensor = (MQTTSensor) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), SensorActivity.class);
                intent.putExtra(SensorActivity.EXTRA_SENSOR, sensor);
                startActivity(intent);
            }
        });
    }
}
