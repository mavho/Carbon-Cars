package ucsc121.carboncars;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

class CustAdapter implements ListAdapter {
    ArrayList<String> car_name;
    Context context;

    public interface OnCarSelectedListener {
        void onCarSelected(int carID);
    }

    // Reference to the activity
    private OnCarSelectedListener mListener;

    public CustAdapter(Context context, ArrayList<String> data) {
        this.car_name = data;
        this.context = context;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
    }

    @Override
    public int getCount() {
        return car_name.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        String item = car_name.get(position);
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_contents, null);
            RadioButton title = convertView.findViewById(R.id.car_rbutton);
            title.setText(item);
            title.setOnClickListener(buttonClickListener);
        }
        return convertView;
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Notify activity of band selection
            String carId = (String) view.getTag();
            mListener.onCarSelected(Integer.parseInt(carId));
        }
    };
    @Override
    public int getItemViewType(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return car_name.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }
}
