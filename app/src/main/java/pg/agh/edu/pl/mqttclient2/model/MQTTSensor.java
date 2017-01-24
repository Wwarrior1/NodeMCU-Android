package pg.agh.edu.pl.mqttclient2.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;

/**
 * Created by Piotr on 2017-01-17.
 */

public abstract class MQTTSensor implements Parcelable {
    private static final String BROKER_URI = "tcp://192.168.0.13:1883";
    private static final int QOS = 2;
    private static final String CLIENT_NAME = "Android client";
    private MqttAndroidClient client = null;

    private final String requestTopic = getRequestTopic();
    private final String dataTopic = getDataTopic();

    private boolean subscribed = false;

    public void connect(final IMqttActionListener listener, final MqttCallback eventsCallback, Context context) throws MqttException {
        if (client == null) {
            client = new MqttAndroidClient(
                    context,
                    BROKER_URI,
                    CLIENT_NAME
            );
        }
        if (client.isConnected()) {
            return;
        }
        IMqttToken token = client.connect();
        token.setActionCallback(listener);
        client.setCallback(eventsCallback);
    }

    public void triggerDataRequest() throws IllegalStateException {
        if (client == null) {
            return;
        }
        if (!subscribed || !client.isConnected()) {
            throw new IllegalStateException("You must subscribe before requesting data");
        }
        publish(requestTopic, "request");
    }

    public void subscribe(final IMqttActionListener listener) throws IllegalStateException, MqttException {
        if (client == null) {
            return;
        }
        if (!client.isConnected()) {
            throw  new IllegalStateException("You must connect to mqtt broker first");
        }
        IMqttToken token = client.subscribe(dataTopic, QOS);
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                subscribed = true;
                listener.onSuccess(asyncActionToken);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                listener.onFailure(asyncActionToken, exception);
            }
        });
    }

    public void disconnect() throws MqttException {
        if (client == null) {
            return;
        }
        if (client.isConnected()) {
            client.disconnect(300);
        }
    }

    private void publish(String topic, String data) {
        if (client == null) {
            return;
        }
        try {
            byte[] encodedPayload = data.getBytes("UTF-8");
            client.publish(topic, encodedPayload, QOS, false);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {}

    public abstract String getTitle();
    public abstract String getDescription();
    public abstract String getDataTopic();
    public abstract String getRequestTopic();
    public abstract String getValueUnitString();
}
