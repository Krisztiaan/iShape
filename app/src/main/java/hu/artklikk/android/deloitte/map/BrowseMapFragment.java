package hu.artklikk.android.deloitte.map;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.ls.widgets.map.MapWidget;
import com.ls.widgets.map.config.OfflineMapConfig;
import com.ls.widgets.map.events.MapScrolledEvent;
import com.ls.widgets.map.events.MapTouchedEvent;
import com.ls.widgets.map.events.ObjectTouchEvent;
import com.ls.widgets.map.interfaces.Layer;
import com.ls.widgets.map.interfaces.MapEventsListener;
import com.ls.widgets.map.interfaces.OnLocationChangedListener;
import com.ls.widgets.map.interfaces.OnMapScrollListener;
import com.ls.widgets.map.interfaces.OnMapTouchListener;
import com.ls.widgets.map.model.MapObject;
import com.ls.widgets.map.utils.PivotFactory;
import hu.artklikk.android.deloitte.MainActivity;
import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.fragment.ProfileFragment;
import hu.artklikk.android.deloitte.map.model.MapObjectContainer;
import hu.artklikk.android.deloitte.map.model.MapObjectModel;
import hu.artklikk.android.deloitte.map.popup.TextPopup;
import hu.artklikk.android.deloitte.model.User;
import hu.artklikk.android.deloitte.util.Util;
import java.util.ArrayList;

public class BrowseMapFragment extends Fragment
        implements MapEventsListener, OnMapTouchListener {
    private static final String TAG = "BrowseMapActivity";

    public static final String USER_BROADCAST = "userBroadcast";

    private static final Integer LAYER1_ID = 0;

    private static final int MAX_ZOOM = 13;

    private int nextObjectId;

    private MapObjectContainer model;
    private MapWidget map;
    private TextPopup mapObjectInfoPopup;

    private Handler delayHandler;

    private RelativeLayout rootLayout;

    private static int DELAY_TIME = 600;

    private MapObjectModel selfObject;

    public static BrowseMapFragment newInstance() {
        BrowseMapFragment f = new BrowseMapFragment();

        Bundle args = new Bundle();
        f.setArguments(args);

        return f;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(USER_BROADCAST)) {
                initModel();
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(USER_BROADCAST);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();

        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.main, container, false);

        nextObjectId = 0;

        delayHandler = new Handler();

        model = new MapObjectContainer();

        view.findViewById(R.id.locate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != selfObject) {
                    if (null != mapObjectInfoPopup) {
                        mapObjectInfoPopup.hide();
                        mapObjectInfoPopup = null;
                    }
                    map.scrollMapTo(selfObject.getLocation());
                    map.zoomIn();
                }
            }
        });

        initMap(savedInstanceState, view);
        if (null != ((MainActivity) getActivity()).userList){
            delayHandler.postDelayed(new Runnable() {
                public void run() {
                    initModel();
                }
            }, DELAY_TIME);
        }
        rootLayout = (RelativeLayout) view.findViewById(R.id.rootLayout);

        initMapListeners();

        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.saveState(outState);
    }

    private void initMap(Bundle savedInstanceState, View view) {
        // In order to display the map on the screen you will need
        // to initialize widget and place it into layout.
        map = new MapWidget(savedInstanceState, view.getContext(),
                "map", // root name of the map under assets folder.
                MAX_ZOOM); // initial zoom level

        //map.setScale(1.0f);

        OfflineMapConfig config = map.getConfig();

        config.setPinchZoomEnabled(true); // Sets pinch gesture to zoom
        config.setFlingEnabled(false);    // Sets inertial scrolling of the map
        config.setSoftwareZoomEnabled(true);
        config.setMinZoomLevelLimit(8);
        config.setMaxZoomLevelLimit(MAX_ZOOM);
        config.setZoomBtnsVisible(true); // Sets embedded zoom buttons visible

        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.rootLayout);
        // Adding the map to the layout
        layout.addView(map, 0);
        layout.setBackgroundColor(getResources().getColor(R.color.see_blue));

        // Adding layers in order to put there some map objects
        map.createLayer(LAYER1_ID);
        //map.centerMap();
    }


    private void initModel() {
        Layer layer1 = map.getLayerById(LAYER1_ID);
        selfObject = null;
        if (null == getActivity()) return;
        for (User user : ((MainActivity) getActivity()).userList) {
            MapObjectModel objectModel;
            if (user.getId() == Util.getSharedPrefenceId(getActivity(), R.string.preference_key_id)){
                user.setRole(MapObjectModel.ROLE.SELF);
                objectModel = new MapObjectModel(user.getId(), user.getName(), user);
                selfObject = objectModel;
            } else {
                objectModel = new MapObjectModel(user.getId(), user.getName(), user);
            }

            model.addObject(objectModel);

            addNotScalableMapObject(objectModel, layer1);
        }

        if (null != selfObject){
            map.scrollMapTo(selfObject.getLocation());

        }
    }

    private void addNotScalableMapObject(MapObjectModel objectModel, Layer layer) {

        Drawable pin = Util.generatePinToMap(objectModel.getUser(), getResources());

        // Creating the map object
        MapObject object1 = new MapObject(nextObjectId, // id, will be passed to the listener when user clicks on it
                pin,
                new Point(0, 0), // coordinates in original map coordinate system.
                // Pivot point of center of the drawable in the drawable's coordinate system.
                PivotFactory.createPivotPoint(pin, PivotFactory.PivotPosition.PIVOT_CENTER),
                true, // This object will be passed to the listener
                true); // is not scalable. It will have the same size on each zoom level
        layer.addMapObject(object1);

        // Will crash if you try to move before adding to the layer.
        object1.moveTo(objectModel.getLocation());

        nextObjectId += 1;
    }


    private void initMapListeners() {
        // In order to receive MapObject touch events we need to set listener
        map.setOnMapTouchListener(this);

        // In order to receive pre and post zoom events we need to set MapEventsListener
        map.addMapEventsListener(this);

        // In order to receive map scroll events we set OnMapScrollListener
        map.setOnMapScrolledListener(new OnMapScrollListener() {
            public void onScrolledEvent(MapWidget v, MapScrolledEvent event) {
                handleOnMapScroll(v, event);
            }
        });


        map.setOnLocationChangedListener(new OnLocationChangedListener() {
            @Override
            public void onLocationChanged(MapWidget v, Location location) {
                // You can handle location change here.
                // For example you can scroll to new location by using v.scrollMapTo(location)
            }
        });
    }


    private void handleOnMapScroll(MapWidget v, MapScrolledEvent event) {
        // When user scrolls the map we receive scroll events
        // This is useful when need to move some object together with the map

        int dx = event.getDX(); // Number of pixels that user has scrolled horizontally
        int dy = event.getDY(); // Number of pixels that user has scrolled vertically

        if (null != mapObjectInfoPopup && mapObjectInfoPopup.isVisible()) {
            mapObjectInfoPopup.hide();
        }
    }


    @Override
    public void onPostZoomIn() {
        Log.i(TAG, "onPostZoomIn()");
    }

    @Override
    public void onPostZoomOut() {
        Log.i(TAG, "onPostZoomOut()");
    }

    @Override
    public void onPreZoomIn() {
        Log.i(TAG, "onPreZoomIn()");

        if (mapObjectInfoPopup != null) {
            mapObjectInfoPopup.hide();
        }
    }

    @Override
    public void onPreZoomOut() {
        Log.i(TAG, "onPreZoomOut()");

        if (mapObjectInfoPopup != null) {
            mapObjectInfoPopup.hide();
        }
    }


    //* On map touch listener implemetnation *//
    @Override
    public void onTouch(MapWidget v, MapTouchedEvent event) {
        // Get touched object events from the MapTouchEvent
        ArrayList<ObjectTouchEvent> touchedObjs = event.getTouchedObjectEvents();

        if (touchedObjs.size() > 0) {

            int xInMapCoords = event.getMapX();
            int yInMapCoords = event.getMapY();
            int xInScreenCoords = event.getScreenX();
            int yInScreenCoords = event.getScreenY();

            ObjectTouchEvent objectTouchEvent = event.getTouchedObjectEvents().get(0);

            // Due to a bug this is not actually the layer id, but index of the layer in layers array.
            // Will be fixed in the next release.
            long layerId = objectTouchEvent.getLayerId();
            Integer objectId = (Integer) objectTouchEvent.getObjectId();
            // User has touched one or more map object
            // We will take the first one to show in the toast message.
            String message = "You touched the object with id: " + objectId + " on layer: " + layerId +
                    " mapX: " + xInMapCoords + " mapY: " + yInMapCoords + " screenX: " + xInScreenCoords + " screenY: " +
                    yInScreenCoords;

            Log.d(TAG, message);

            MapObjectModel objectModel = model.getObject(objectId);

            if (objectModel != null) {
                // This is a case when we want to show popup info exactly above the pin image
                map.scrollMapTo(objectModel.getLocation());
                map.zoomIn();

                // Show it
                showLocationsPopup(objectModel.getUser());
            } else {
                // This is a case when we want to show popup where the user has touched.
                //showLocationsPopup(null);
            }

            // Hint: If user touched more than one object you can show the dialog in which ask
            // the user to select concrete object
        } else {
            if (mapObjectInfoPopup != null) {
                mapObjectInfoPopup.hide();
            }
        }
    }


    private void showLocationsPopup(final User user) {
        final RelativeLayout mapLayout = (RelativeLayout) getActivity().findViewById(R.id.rootLayout);
        if (mapObjectInfoPopup != null) {
            mapObjectInfoPopup.hide();
        }

        mapObjectInfoPopup = new TextPopup(rootLayout.getContext(), rootLayout);

        mapObjectInfoPopup.setUser(user);

        mapObjectInfoPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapObjectInfoPopup != null) {
                    mapObjectInfoPopup.hide();
                    getActivity().getFragmentManager().beginTransaction().
                            replace(R.id.container, ProfileFragment.newInstance(user.getId())).addToBackStack("3").commit();
                    ((MainActivity) getActivity()).onSectionAttached(3);
                }
            }
        });


        delayHandler.postDelayed(new Runnable() {
            public void run() {
                if (null != mapObjectInfoPopup){
                    mapObjectInfoPopup.show(mapLayout);
                }
            }
        }, DELAY_TIME);

    }

    /***
     * Transforms coordinate in map coordinate system to screen coordinate system
     * @param mapCoord - X in map coordinate in pixels.
     * @return X coordinate in screen coordinates. You can use this value to display any object on the screen.
     */
    private int xToScreenCoords(double mapCoord) {
        return (int) (mapCoord * map.getScale() - map.getScrollX());
    }

    private int yToScreenCoords(double mapCoord) {
        return (int) (mapCoord * map.getScale() - map.getScrollY());
    }
}