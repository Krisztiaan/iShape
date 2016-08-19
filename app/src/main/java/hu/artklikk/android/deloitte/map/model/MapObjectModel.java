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

package hu.artklikk.android.deloitte.map.model;

import android.location.Location;

import hu.artklikk.android.deloitte.model.User;

public class MapObjectModel 
{
	public enum ROLE {
		SELF,
		OTHER,
		MENTOR,
		LEADER
	};

	private int id;
	private String caption;
	private Location location;
    private User user;
	
	public MapObjectModel(int id, String caption, User user) {
		this.id = id;
        Location location = new Location(user.getName());
        location.setLatitude(user.getPoi()[1]);
        location.setLongitude(user.getPoi()[0]);
        this.location = location;
		this.caption = caption;
		this.user = user;
	}

	public int getId() 
	{
		return id;
	}

    public User getUser() {
        return user;
    }


	public Location getLocation()
	{
		return location;
	}

	public String getCaption()
	{
		return caption;
	}

}
