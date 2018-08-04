/*
 * Reference List
 *
 * 1. CodingWithMitch Android Beginner Tutorial #8 - Custom ListView Adapter]
 *    https://www.youtube.com/watch?annotation_id=annotation_3104328239&feature=iv&src_vid=8K-6gdTlGEA&v=E6vE8fqQPTE
 *
 *
 *
 * */


package oregonstate.chews.myagilitytracker;

//[ref: 1]

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class QualEventListAdapter extends ArrayAdapter<QualEvent> {

    private static final String TAG = "QualEventListAdapter";

    private Context mContext;
    int mResource;

    /**
     * Default constructor for the QualEventListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public QualEventListAdapter(@NonNull Context context, int resource, @NonNull List<QualEvent> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //get the event information
        String name = getItem(position).getDogname();
        String game = getItem(position).getGame();
        String date = getItem(position).getDate();
        String pts = getItem(position).getPoints();

        //Create a temp QualEvent
        QualEvent qtmp = new QualEvent(name, game, date, pts);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.textView1);
        TextView tvGame = (TextView) convertView.findViewById(R.id.textView2);
        TextView tvDate = (TextView) convertView.findViewById(R.id.textView3);
        TextView tvPts = (TextView) convertView.findViewById(R.id.textView4);

        tvName.setText(name);
        tvGame.setText(game);
        tvDate.setText(date);
        tvPts.setText(pts);

        return convertView;
    }
}
