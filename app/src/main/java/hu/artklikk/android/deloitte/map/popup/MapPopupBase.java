/*************************************************************************
* Copyright (c) 2015 Lemberg Solutions
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
**************************************************************************/

package hu.artklikk.android.deloitte.map.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MapPopupBase extends RelativeLayout
{
	protected ViewGroup parentView;
	
	protected LinearLayout container;
	protected float dipScaleFactor;
	protected int lastX;
	protected int lastY;
	protected int screenHeight;
	protected int screenWidth;


	public MapPopupBase(Context context, ViewGroup parentView) {
		super(context);
	    screenHeight = context.getResources().getDisplayMetrics().heightPixels;
	    screenWidth = context.getResources().getDisplayMetrics().widthPixels;
	    
		this.parentView = parentView;
    	dipScaleFactor = context.getResources().getDisplayMetrics().density;
    	
		container = new LinearLayout(context);
		container.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		container.setGravity(Gravity.CENTER);
		lastX = lastY = -1;
	}		
	
	
	public void show(ViewGroup view) {
		hide();

		container.measure(view.getWidth(), view.getHeight());
		int x = getWidth() / 2;
		int y = getHeight() / 2;

		lastX = x;
		lastY = y;

		parentView.addView(container);
		container.setVisibility(View.VISIBLE);
	}



	public int getContainerHeight()
	{
		return container.getMeasuredHeight();
	}


	public int getContainerWidth()
	{
		return container.getMeasuredWidth();
	}

	
	public boolean isVisible()
	{
		return container.getVisibility() == View.VISIBLE;
	}
	
	
	public void hide()
	{
		container.setPadding(0, 0, 0, 0);
		container.setVisibility(View.INVISIBLE);
		parentView.removeView(container);
	}
	
	
	public void setOnClickListener(View.OnTouchListener listener)
	{
	    if(container != null){
	        container.setOnTouchListener(listener);
	    }
	}
}
