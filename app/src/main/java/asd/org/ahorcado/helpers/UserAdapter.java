package asd.org.ahorcado.helpers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import asd.org.ahorcado.R;

public class UserAdapter extends BaseAdapter {

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";

    public List<Map<String, String>> list;
    private Activity activity;
    private TextView id;
    private TextView name;

    public UserAdapter(Activity activity, List<Map<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.indexOf(list.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.user_row, null);
            id = (TextView) convertView.findViewById(R.id.userId);
            name = (TextView) convertView.findViewById(R.id.userName);
        }
        Map<String, String> map = list.get(position);
        id.setText(map.get(COLUMN_ID));
        name.setText(map.get(COLUMN_NAME));
        return convertView;
    }

}