package hu.artklikk.android.deloitte.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.model.Animal;
import hu.artklikk.android.deloitte.util.Util;

/**
 * Created by gyozofule on 15. 11. 08..
 */
public class AnimalListAdapter extends ArrayAdapter<Animal> {

    private List<Animal> animalList;
    private boolean isSelf;

    public AnimalListAdapter(List<Animal> animalList,Context context, boolean isSelf) {
        super(context, R.layout.list_item_user, animalList);
        this.isSelf = isSelf;
        this.animalList = animalList;
    }

    @Override
    public int getCount() {
        return animalList.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item_animal, null);

        TextView animalName = (TextView) convertView.findViewById(R.id.animal);
        Button trade = (Button) convertView.findViewById(R.id.trade);
        FlowLayout animalCage = (FlowLayout) convertView.findViewById(R.id.animalCage);

        final Animal animal = getItem(position);

        animalName.setText(getContext().getResources().getStringArray(R.array.category_array)[animal.getAnimalTypeEnum().getCategory().getId()]);
        //trade.setVisibility(isSelf ? View.VISIBLE : View.INVISIBLE);
        trade.setVisibility(View.INVISIBLE);

        trade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "animal clicked : " + animal.getAnimalType(), Toast.LENGTH_SHORT).show();

                //Dialog dialog = new Dialog(getContext(), R.style.DialogTheme);
                //dialog.setContentView(R.layout.dialog_fragment_trade);

                //dialog.show();
            }
        });

        for (int i = 0; i < animal.getQuantity(); ++i){
            animalCage.addView(Util.resolveAnimalDrawable(animal.getAnimalTypeEnum(), getContext(), false));
        }

        return convertView;
    }
}
