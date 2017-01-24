package pg.agh.edu.pl.mqttclient2.model.sensors;

import android.os.Parcel;
import android.os.Parcelable;

import pg.agh.edu.pl.mqttclient2.model.MQTTSensor;

/**
 * Created by Piotr on 2017-01-17.
 */

public class SonicSensor extends MQTTSensor {
    @Override
    public String getTitle() {
        return "Sonic sensor";
    }

    @Override
    public String getDescription() {
        return "Measures distance";
    }

    @Override
    public String getDataTopic() {
        return "/sonic/data";
    }

    @Override
    public String getRequestTopic() {
        return "/sonic/request";
    }

    @Override
    public String getValueUnitString() {
        return "cm";
    }

    public static final Parcelable.Creator<MQTTSensor> CREATOR
            = new Parcelable.Creator<MQTTSensor>() {
        public MQTTSensor createFromParcel(Parcel in) {
            return new SonicSensor();
        }

        public MQTTSensor[] newArray(int size) {
            return new MQTTSensor[size];
        }
    };
}
