package hu.artklikk.android.deloitte.util;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.model.Animal;

/**
 * Created by gyozofule on 15. 11. 16..
 */
public class AnimalDescriptionDialog extends AlertDialog {

    private Animal animal;

    public static void getDescriptionDialog(Context context, Animal animal, int x, int y) {
        AnimalDescriptionDialog dialog = new AnimalDescriptionDialog(context,android.R.style.Theme_Panel,animal);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        dialog.setCancelable(true);
        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
        wmlp.x = x;
        wmlp.y = y;

        dialog.show();
    }

    public AnimalDescriptionDialog(Context context, int theme, Animal animal) {
        super(context, theme);
        this.animal = animal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_dialog);

        ((TextView) findViewById(R.id.name)).setText(animal.getAnimalTypeEnum().toString());
        ((TextView) findViewById(R.id.desc)).setText(animal.getDescription());
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}