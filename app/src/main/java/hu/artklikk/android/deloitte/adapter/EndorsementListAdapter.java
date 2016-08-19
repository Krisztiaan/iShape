package hu.artklikk.android.deloitte.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.model.Animal;
import hu.artklikk.android.deloitte.model.Endorsement;
import hu.artklikk.android.deloitte.model.User;
import hu.artklikk.android.deloitte.util.Util;

/**
 * Created by gyozofule on 15. 11. 08..
 */
public class EndorsementListAdapter extends ArrayAdapter<Endorsement> {

    private List<Endorsement> endorsementList;
    private Map<Integer, User> userList;
    private WeakReference<Context> context;

    public EndorsementListAdapter(List<Endorsement> endorsementList,Map userList, Context ctx) {
        super(ctx, R.layout.list_item_user, endorsementList);
        this.endorsementList = endorsementList;
        this.userList = userList;
        this.context = new WeakReference<>(ctx);
    }

    @Override
    public int getCount() {
        return endorsementList.size();
    }

    //Do not use viewHolder, because FlowLayout show wrong
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item_endorsement, null);

        TextView point = (TextView) convertView.findViewById(R.id.point);
        TextView category = (TextView) convertView.findViewById(R.id.category);
        TextView endorser = (TextView) convertView.findViewById(R.id.endorser);
        TextView description = (TextView) convertView.findViewById(R.id.description);

        FlowLayout animalCage = (FlowLayout) convertView.findViewById(R.id.animalCage);

        final Endorsement endorsement = getItem(position);

        point.setText(Html.fromHtml(String.format(context.get().getResources().getString(R.string.pts), endorsement.getPoint())));
        category.setText(context.get().getResources().getTextArray(R.array.category_array)
                [Endorsement.Category.getCategoryOfCode(endorsement.getCategory()).getId()]);
        description.setText(endorsement.getDescription());
        try{
            endorser.setText(this.userList.get((int) endorsement.getConsignorId()).getName());
        } catch (NullPointerException e){
            endorser.setText("Admin");
        }


        for (int i = 0; i < endorsement.getPoint(); ++i){
            animalCage.addView(Util.resolveAnimalDrawable(Animal.AnimalType.getAnimalCategoryList(Endorsement.Category.getCategoryOfCode(endorsement.getCategory())).get(0), getContext(),true));
        }

        return convertView;
    }
}
