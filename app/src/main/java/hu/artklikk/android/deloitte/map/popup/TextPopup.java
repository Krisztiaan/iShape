/*************************************************************************
 * Copyright (c) 2015 Lemberg Solutions
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **************************************************************************/

package hu.artklikk.android.deloitte.map.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.model.User;

public class TextPopup
        extends MapPopupBase {

    private TextView textName;
    private TextView textJob;
    private TextView textPointInCycle;
    private TextView textPointInYear;
    private TextView textViewRank;
    private RelativeLayout go;


    public TextPopup(Context context, ViewGroup parentView) {
        super(context, parentView);

        LayoutInflater inflater = LayoutInflater.from(context);
        View inflatedView = inflater.inflate(R.layout.map_popup, this, true);

        textName = (TextView) inflatedView.findViewById(R.id.textViewName);
        textJob = (TextView) inflatedView.findViewById(R.id.textViewJob);
        textPointInCycle = (TextView) inflatedView.findViewById(R.id.textViewPointInCycle);
        textPointInYear = (TextView) inflatedView.findViewById(R.id.textViewPointInYear);
        textViewRank = (TextView) inflatedView.findViewById(R.id.textViewRank);
        go = (RelativeLayout) inflatedView.findViewById(R.id.mapPopup);

        container.addView(inflatedView);
    }


    public void setUser(User user) {
        if (null == user){
            textName.setText("not point click!");
        } else {
            textName.setText(user.getName());
            textJob.setText(user.getJob());
            String pointCycle = String.format(getResources().getString(R.string.cycle), user.getPointInCycle());
            String pointYear = String.format(getResources().getString(R.string.year), user.getPointInYear());
            textPointInCycle.setText(pointCycle);
            textPointInYear.setText(pointYear);
            if(user.getCycleRank() < 10){
                textViewRank.setText("0" + user.getCycleRank());
            } else {
                textViewRank.setText(String.valueOf(user.getCycleRank()));
            }
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        if (textName != null) {
            go.setOnClickListener(listener);
        }
    }
}
