package pg.agh.edu.pl.mqttclient2.model.sensors;

import android.os.Parcel;
import android.os.Parcelable;

import pg.agh.edu.pl.mqttclient2.model.MQTTSensor;

/**
 * Created by Piotr on 2017-01-19.
 */

public class TemperatureSensor extends MQTTSensor {
    @Override
    public String getTitle() {
        return "Temperature sensor";
    }

    @Override
    public String getDescription() {
        return "Measures temperature";
    }

    @Override
    public String getDataTopic() {
        return "/temp/data";
    }

    @Override
    public String getRequestTopic() {
        return "/temp/request";
    }

    @Override
    public String getValueUnitString() {
        return "*C";
    }

    public static final Parcelable.Creator<MQTTSensor> CREATOR
            = new Parcelable.Creator<MQTTSensor>() {
        public MQTTSensor createFromParcel(Parcel in) {
            return new TemperatureSensor();
        }

        public MQTTSensor[] newArray(int size) {
            return new MQTTSensor[size];
        }
    };
}
