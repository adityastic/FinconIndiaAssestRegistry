package recoveryportal.adityagupta.recoveryportal.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import recoveryportal.adityagupta.recoveryportal.Data.SearchHistoryData;
import recoveryportal.adityagupta.recoveryportal.R;

/**
 * Created by adityagupta on 10/12/17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    Context context;
    List<SearchHistoryData> list;

    public SearchAdapter(Context context, List<SearchHistoryData> list) {
        this.list = list;
        this.context = context;
    }

    public static void setRecyclerViewAnimation(Context context,
                                                View view,
                                                int animation_resource) {
        Animation animation = AnimationUtils.loadAnimation(context, animation_resource);
        view.startAnimation(animation);
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_recycler, parent, false);
        return new SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        SearchHistoryData data = list.get(position);

        String key = data.key;
        Log.e("Length = ", key.length() + "");
        if (key.length() == 10) {
            key = key.substring(0, 6) + "-" + key.substring(6);
            key = key.substring(0, 4) + "-" + key.substring(4);
            key = key.substring(0, 2) + "-" + key.substring(2);
        }
        holder.key.setText(key);

        Calendar cal = Calendar.getInstance();

        Date startDate, endDate;
        try {
            startDate = new SimpleDateFormat("MM/dd/yyyy").parse(data.date);
            endDate = cal.getTime();

            long different = endDate.getTime() - startDate.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            if (elapsedDays == 0)
                holder.date.setText("| Today");
            else if (elapsedDays == 1)
                holder.date.setText("| Yesterday");
            else
                holder.date.setText("| " + data.date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        setRecyclerViewAnimation(context, holder.itemView, R.anim.recyclerview_anim);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView key, date;

        public SearchViewHolder(View itemView) {
            super(itemView);
            key = itemView.findViewById(R.id.key);
            date = itemView.findViewById(R.id.date);
        }

    }
}
