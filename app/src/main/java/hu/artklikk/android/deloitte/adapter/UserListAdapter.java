package hu.artklikk.android.deloitte.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.model.User;
import hu.artklikk.android.deloitte.util.Util;

/**
 * Created by gyozofule on 15. 11. 08..
 */
public class UserListAdapter extends ArrayAdapter<User> {

    private List<User> userList;
    private UserFilter filter;
    private boolean isToplist;

    public UserListAdapter(List<User> userList, Context ctx, boolean isToplist) {
        super(ctx, R.layout.list_item_user, userList);
        this.userList = userList;
        this.isToplist = isToplist;
    }

    @Override
    public long getItemId(int position) {
        return userList.get(position).getId();
    }

    @Override
    public User getItem(int position) {
        return userList.get(position);
    }

    @Override
    public int getCount() {
        return null == userList ? 0 : userList.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = new ViewHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_user, null);
            // Now we can fill the layout with the right values
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.job = (TextView) view.findViewById(R.id.job);
            holder.point = (TextView) view.findViewById(R.id.point);
            holder.pointInCycle = (TextView) view.findViewById(R.id.pointInCycle);
            holder.profileImage = (ImageView) view.findViewById(R.id.profileImage);
            holder.pointLayout = (LinearLayout) view.findViewById(R.id.pointLayout);
            holder.rank = (TextView) view.findViewById(R.id.rank);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        User user = getItem(position);
        holder.name.setText(user.getName());
        holder.job.setText(user.getJob());
        holder.point.setText(String.valueOf(user.getPointInYear()));
        holder.pointInCycle.setText(String.valueOf(user.getPointInCycle()));
        holder.rank.setText(String.valueOf(user.getCycleRank()));


        if (isToplist) {
            holder.profileImage.setImageDrawable(Util.generatePinToLists(user, getContext().getResources(), R.drawable.poi1));
            holder.pointLayout.setBackgroundColor(getContext().getResources().getColor(R.color.green));
            holder.rank.setVisibility(View.VISIBLE);
        } else {
            holder.profileImage.setImageDrawable(Util.generatePinToLists(user, getContext().getResources(), R.drawable.search_poi));
            holder.pointLayout.setBackgroundColor(getContext().getResources().getColor(R.color.dark_blue));
            holder.rank.setVisibility(View.GONE);
        }

        return view;
    }

    private static class ViewHolder {
        public TextView rank;
        public TextView name;
        public TextView job;
        public TextView point;
        public TextView pointInCycle;
        public ImageView profileImage;
        public LinearLayout pointLayout;
    }

    @Override
    public Filter getFilter() {
        if (null == filter) {
            List<User> userCopy = new ArrayList<>(userList);
            filter = new UserFilter(userCopy);
        }
        return filter;
    }

    private class UserFilter extends Filter {
        List<User> originalList;

        public UserFilter(List<User> originalList) {
            this.originalList = originalList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = originalList;
                results.count = originalList.size();
            } else {
                // We perform filtering operation
                List<User> nUserList = new ArrayList<>();

                for (User user : originalList) {
                    if (user.getName().toUpperCase().contains(constraint.toString().toUpperCase()))
                        nUserList.add(user);
                }

                results.values = nUserList;
                results.count = nUserList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                for (User user : (List<User>) results.values) {
                    add(user);
                }
                notifyDataSetChanged();
            }
        }
    }
}
