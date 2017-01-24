package pg.agh.edu.pl.mqttclient2.gui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pg.agh.edu.pl.mqttclient2.R;
import pg.agh.edu.pl.mqttclient2.model.MQTTSensor;

public class SensorsListAdapter extends ArrayAdapter<MQTTSensor> {
    private Context context;
    private LayoutInflater inflater = null;

    public SensorsListAdapter(Context context, int resource, List<MQTTSensor> objects) {
        super(context, resource, objects);
        this.context = context;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_row_sensor, null);
        }

        TextView title = (TextView) view.findViewById(R.id.list_row_sensor_title);
        TextView description = (TextView) view.findViewById(R.id.list_row_sensor_description);

        MQTTSensor sensor = getItem(position);
        if (sensor != null) {
            title.setText(sensor.getTitle());
            description.setText(sensor.getDescription());
        } else {
            title.setText("null");
            description.setText("null");
        }

        return view;
    }
}