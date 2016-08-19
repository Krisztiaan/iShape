package hu.artklikk.android.deloitte.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Random;

import hu.artklikk.android.deloitte.R;

/**
 * Created by gyozofule on 15. 11. 16..
 */
public class AnimalProgressDialog extends Dialog {
    private AnimationDrawable animation;

    public static Dialog getProgressDialog(Context context) {
        AnimalProgressDialog dialog = new AnimalProgressDialog(context,R.style.ProgressDialogTheme);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    public AnimalProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog);

        ImageView la = (ImageView) findViewById(R.id.animation);
        Random r = new Random();

        switch (r.nextInt(2)){
            case 0:
                la.setBackgroundResource(R.drawable.custom_progress_dialog_chicken);
                break;
            case 1:
                la.setBackgroundResource(R.drawable.custom_progress_dialog_snail);
                break;
            default:
                la.setBackgroundResource(R.drawable.custom_progress_dialog_chicken);
                break;
        }

        animation = (AnimationDrawable) la.getBackground();
    }

    @Override
    public void show() {
        super.show();
        animation.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        animation.stop();
    }
}