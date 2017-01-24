package pg.agh.edu.pl.mqttclient2.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import pg.agh.edu.pl.mqttclient2.R;
import pg.agh.edu.pl.mqttclient2.model.MQTTSensor;

/**
 * Created by Piotr on 2017-01-17.
 */

public class SensorActivity extends AppCompatActivity {
    public static final String EXTRA_SENSOR = "sensor";
    MQTTSensor sensor = null;
    TextView sensorNameTextView = null;
    TextView valueTextView = null;
    TextView unitTextView = null;
    TextView statusTextView = null;
    Button updateButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.err.println("On create");
        sensor = getIntent().getParcelableExtra(EXTRA_SENSOR);
        setContentView(R.layout.sensor_home);
        sensorNameTextView = (TextView) findViewById(R.id.sensor_home_sensor_name_textview);
        valueTextView = (TextView) findViewById(R.id.sensor_home_value_textview);
        unitTextView = (TextView) findViewById(R.id.sensor_home_unit_textview);
        statusTextView = (TextView) findViewById(R.id.sensor_home_status_textview);
        updateButton = (Button) findViewById(R.id.sensor_home_connect_button);
        init();
        try {
            sensor.connect(new IMqttActionListener() {
                               @Override
                               public void onSuccess(IMqttToken asyncActionToken) {
                                   statusTextView.setText("Connected");
                                   try {
                                       sensor.subscribe(new IMqttActionListener() {
                                           @Override
                                           public void onSuccess(IMqttToken asyncActionToken) {
                                               statusTextView.setText("Subscribed");
                                               updateButton.setEnabled(true);
                                           }

                                           @Override
                                           public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                               showDialogAlert("Subscribe fail", exception.toString());
                                           }
                                       });
                                   } catch (MqttException e) {
                                       showDialogAlert("Subscribe fail", e.toString());
                                   }
                               }

                               @Override
                               public void onFailure(IMqttToken asyncActionToken, Throwable exception) {}
                           },
                    new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {
                            updateButton.setEnabled(false);
                            statusTextView.setText("Connection lost");
                            showToast("Connection lost");
                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
                            updateButton.setEnabled(true);
//                            showToast("Received update");
                            valueTextView.setText(new String(message.getPayload()));
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {
//                            showToast("Request sent successfully");
                        }
                    },
            getApplicationContext());
        } catch (Exception e) {
            showDialogAlert("Exception", e.toString());
        }
    }

    private void init() {
        sensorNameTextView.setText(sensor.getTitle());
        valueTextView.setText("no value");
        unitTextView.setText(sensor.getValueUnitString());
        statusTextView.setText("Connecting...");
        updateButton.setEnabled(false);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButton.setEnabled(false);
                sensor.triggerDataRequest();
            }
        });
    }

    @Override
    public void onBackPressed() {
        try {
            System.err.println("On back pressed");
            sensor.disconnect();
        } catch (MqttException e) {
            showDialogAlert("Failed to disconnect", e.toString());
        }
        super.onBackPressed();
    }

    private void showDialogAlert(String title, String text) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(text)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .show();
    }

    private void showToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
