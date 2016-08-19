package hu.artklikk.android.deloitte.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.model.Endorsement;
import hu.artklikk.android.deloitte.model.User;
import hu.artklikk.android.deloitte.util.Util;

/**
 * Created by gyozofule on 15. 11. 08..
 */
public class NominationListAdapter extends ArrayAdapter<Endorsement> {

    private List<Endorsement> endorsementList;
    private WeakReference<Context> context;
    private int resource;
    private Map<Integer, User>  userMap;
    private boolean isIncoming;

    public NominationListAdapter(List<Endorsement> endorsementList, Context ctx, int resource, Map<Integer, User> userMap, boolean isIncoming) {
        super(ctx, resource, endorsementList);
        this.resource = resource;
        this.endorsementList = endorsementList;
        this.context = new WeakReference<>(ctx);
        this.userMap = userMap;
        this.isIncoming = isIncoming;
    }

    @Override
    public int getCount() {
        return endorsementList.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = new ViewHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, null);
            // Now we can fill the layout with the right values
            holder.point = (TextView) view.findViewById(R.id.point);
            holder.time = (TextView) view.findViewById(R.id.time);
            holder.date = (TextView) view.findViewById(R.id.date);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.job = (TextView) view.findViewById(R.id.job);
            holder.category = (TextView) view.findViewById(R.id.category);
            holder.description = (TextView) view.findViewById(R.id.description);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Endorsement endorsement = getItem(position);
        long userId = isIncoming ? endorsement.getConsignorId() : endorsement.getConsigneeId();

        holder.point.setText(Html.fromHtml(String.format(context.get().getResources().getString(R.string.points_activity), endorsement.getPoint())));
        holder.date.setText(Util.convertTimestampToDate(endorsement.getTime(), false));
        holder.time.setText(Util.convertTimestampToHour(endorsement.getTime()));

        try{
            holder.category.setText(context.get().getResources().getTextArray(R.array.category_array)
                    [Endorsement.Category.getCategoryOfCode(endorsement.getCategory()).getId()]);

            holder.name.setText(userMap.get((int) userId).getName());
            holder.job.setText(userMap.get((int) userId).getJob());

            holder.point.setCompoundDrawablesWithIntrinsicBounds(null,
                    Util.generatePinToLists(userMap.get((int) userId), getContext().getResources(),
                            R.drawable.poi1), null, null);

        } catch (NullPointerException e){
            holder.name.setText("Admin");
            holder.job.setText("admin");
            holder.point.setCompoundDrawablesWithIntrinsicBounds(null,
                    Util.generatePinToLists(null, getContext().getResources(),
                            R.drawable.poi1), null, null);
        }

        holder.description.setText(endorsement.getDescription());

        return view;
    }

    private static class ViewHolder {
        public TextView point;
        public TextView date;
        public TextView time;
        public TextView name;
        public TextView job;
        public TextView category;
        public TextView description;
    }
}
