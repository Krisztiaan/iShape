package hu.artklikk.android.deloitte.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikhaellopez.circularimageview.CircularImageView;

import hu.artklikk.android.deloitte.client.IShapeClient;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import hu.artklikk.android.deloitte.MainActivity;
import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.adapter.AnimalListAdapter;
import hu.artklikk.android.deloitte.model.Animal;
import hu.artklikk.android.deloitte.model.Endorsement;
import hu.artklikk.android.deloitte.model.communication.ProfileResponse;
import hu.artklikk.android.deloitte.util.AnimalDescriptionDialog;
import hu.artklikk.android.deloitte.util.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    public static String USER_ID = "userId";

    static final int REQUEST_IMAGE_CAPTURE = 1;

    static final int REQUEST_IMAGE_SELECT = 2;

    private CircularImageView imageView;

    private View sloth;

    private ListView animalList;
    private LinearLayout specAnimalList;
    private boolean isSelfFarm;
    private int userId;

    private TextView pointInCycle;
    private TextView pointInYear;
    private TextView rank;

    private Dialog endorsementDialog;

    @SuppressWarnings("HandlerLeak")
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
            Endorsement endorsement = (Endorsement) inputMessage.getData().get("data");
            new SendEndorsementTask().execute(endorsement);
        }
    };

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        new ProfileTask().execute(String.valueOf(getArguments().getInt(USER_ID, 0)));
    }

    public static ProfileFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(USER_ID, id);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_SELECT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap imageBitmap = null;

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
        } else if(requestCode == REQUEST_IMAGE_SELECT && resultCode == getActivity().RESULT_OK) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                imageBitmap = imageBitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 10,bitmap.getHeight() / 10, false);

            } catch (IOException e) {
                Toast.makeText(getActivity(), "Problem occured in image load", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        if (null != imageBitmap){

            new ProfileImageTask().execute(imageBitmap);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        sloth = view.findViewById(R.id.sloth);

        imageView = (CircularImageView) view.findViewById(R.id.profileImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Select image")
                        .setMessage("Please select from two possibility to profile image change")
                        .setPositiveButton("Take photo", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dispatchTakePictureIntent();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Select from gallery", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                pickImage();
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_camera)
                        .show();
            }
        });
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView job = (TextView) view.findViewById(R.id.job);

        pointInCycle = (TextView) view.findViewById(R.id.pointInCycle);
        pointInYear = (TextView) view.findViewById(R.id.pointInYear);
        rank = (TextView) view.findViewById(R.id.rank);

        animalList = (ListView) view.findViewById(R.id.listViewAnimals);
        specAnimalList = (LinearLayout) view.findViewById(R.id.scrollSpecAnimals);
        Button buttonNominations = (Button) view.findViewById(R.id.buttonNominations);
        TextView edit = (TextView) view.findViewById(R.id.textEdit);


        userId = getArguments().getInt(USER_ID, 0);
        isSelfFarm = false;

        if (Util.getSharedPrefenceId(getActivity(), R.string.preference_key_id) == userId){
            isSelfFarm = true;

            buttonNominations.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getFragmentManager().beginTransaction()
                            .replace(R.id.container, ActionsFragment.newInstance()).addToBackStack("4").commit();

                    ((MainActivity) getActivity()).onSectionAttached(4);
                }
            });

            edit.setVisibility(View.VISIBLE);
            ((MainActivity) getActivity()).title.setText("my farm");
        } else {
            imageView.setEnabled(false);

            buttonNominations.setText(R.string.give_endorsement_button);
            buttonNominations.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endorsementDialog = Util.buildEndorsementDialog(getActivity(),
                            ((MainActivity) getActivity()).userMap.get(userId), handler);
                    endorsementDialog.show();
                }
            });

            buttonNominations.setBackground(getResources().getDrawable(R.drawable.green_button_selector));
            edit.setVisibility(View.INVISIBLE);
            ((MainActivity) getActivity()).title.setText("farm");
        }

        name.setText(((MainActivity)getActivity()).userMap.get(userId).getName());
        job.setText(((MainActivity)getActivity()).userMap.get(userId).getJob());

        pointInCycle.setText(String.valueOf(((MainActivity) getActivity()).userMap.get(userId).getPointInCycle()));
        pointInYear.setText(String.valueOf(((MainActivity) getActivity()).userMap.get(userId).getPointInYear()));
        rank.setText(String.valueOf(((MainActivity) getActivity()).userMap.get(userId).getCycleRank()));

        if (((MainActivity)getActivity()).userMap.get(userId).isMenthor()) {
            view.findViewById(R.id.crown).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.crown).setVisibility(View.GONE);
        }

        imageView.setImageBitmap(Util.decodeBase64(((MainActivity)getActivity()).userMap.get(userId).getProfileImage()));

        return view;
    }

    private class ProfileTask extends AsyncTask<String, Void, ProfileResponse> {
        @Override
        protected ProfileResponse doInBackground(String... params) {
            try {
                String uri = Uri.parse(IShapeClient.BASE_URL + "profile")
                        .buildUpon()
                        .appendQueryParameter("userID", params[0])
                        .build().toString();
                try {
                    URL url = new URL(uri);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.addRequestProperty("Cookie", "ASP.NET_SessionId=" + Util.getSharedPrefence(getActivity(), R.string.preference_key_token));
                    ObjectMapper mapper = new ObjectMapper();
                    ProfileResponse profileResponse = mapper.readValue(urlConnection.getInputStream(), ProfileResponse.class);

                    Log.d(getClass().toString(), profileResponse.toString());

                    return profileResponse;

                } catch (Exception e ) {
                    Log.d(getClass().toString(), "error: " + e.getLocalizedMessage());

                    return null;
                }
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(ProfileResponse profileResponse) {
            List<Animal> notSpecAnimals = new ArrayList<>();
            if (null != profileResponse) {
                specAnimalList.removeAllViews();
                for (final Animal animal : profileResponse.getAnimalList()){
                    if (Endorsement.Category.Special.equals(animal.getAnimalTypeEnum().getCategory())){
                        if (animal.getAnimalTypeEnum().equals(Animal.AnimalType.Sloth)){
                            sloth.setVisibility(View.VISIBLE);
                        } else {
                            final ImageView imageView = Util.resolveAnimalDrawable(animal.getAnimalTypeEnum(), getActivity(), false);
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int[] location = new int[2];
                                    imageView.getLocationOnScreen(location);
                                    AnimalDescriptionDialog.getDescriptionDialog(getActivity(), animal, location[0], location[1] - imageView.getHeight() - 100);
                                }
                            });
                            specAnimalList.addView(imageView);
                        }
                    } else {
                        notSpecAnimals.add(animal);
                    }
                }

                Bitmap bitmapOriginal = Util.decodeBase64(profileResponse.getProfileImage());
                Bitmap bitmap = ThumbnailUtils.extractThumbnail(bitmapOriginal, getSquareCropDimensionForBitmap(bitmapOriginal),
                        getSquareCropDimensionForBitmap(bitmapOriginal));

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                        ((MainActivity) getActivity()).userMap.get(userId).setProfileImage(Base64.encodeToString(byteArray, Base64.DEFAULT));

                ((MainActivity) getActivity()).userMap.get(userId).setCycleRank(profileResponse.getCycleRank());
                ((MainActivity) getActivity()).userMap.get(userId).setYearRank(profileResponse.getYearRank());
                ((MainActivity) getActivity()).userMap.get(userId).setPointInCycle(profileResponse.getPointInCycle());
                ((MainActivity) getActivity()).userMap.get(userId).setPointInYear(profileResponse.getPointInYear());

                pointInCycle.setText(String.valueOf(profileResponse.getPointInCycle()));
                pointInYear.setText(String.valueOf(profileResponse.getPointInYear()));
                rank.setText(String.valueOf(profileResponse.getCycleRank()));

                imageView.setImageBitmap(bitmapOriginal);
            } else {
                try {
                    Toast.makeText(getActivity(), "Some problem in download, please try again later", Toast.LENGTH_LONG).show();
                } catch (NullPointerException e){

                }
            }

            animalList.setAdapter(new AnimalListAdapter(notSpecAnimals, getActivity(),  isSelfFarm));

        }
    }

    public int getSquareCropDimensionForBitmap(Bitmap bitmap) {
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            return bitmap.getHeight();
        } else {
            return bitmap.getWidth();
        }
    }

    private class ProfileImageTask extends AsyncTask<Bitmap, Void, Integer> {
        @Override
        protected Integer doInBackground(Bitmap... params) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                params[0].compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                String uri = Uri.parse(IShapeClient.BASE_URL + "ProfileImage")
                        .buildUpon()
                        .build().toString();
                try {
                    URL url = new URL(uri);

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.setUseCaches(false);
                    urlConnection.setChunkedStreamingMode(1024);

                    JSONObject image = new JSONObject();
                    image.put("profileImage", encoded);
                    image.put("mimetype", "image/png");

                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.addRequestProperty("Cookie", "ASP.NET_SessionId=" + Util.getSharedPrefence(getActivity(), R.string.preference_key_token));
                    urlConnection.setRequestMethod("POST");
                    urlConnection.connect();

                    //Write
                    OutputStream outputStream = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    writer.write(image.toString());
                    writer.close();
                    outputStream.close();

                    int responseCode = urlConnection.getResponseCode();

                    Log.d(getClass().toString(), "response code: " + responseCode);

                    return responseCode;

                } catch (Exception e ) {
                    Log.d(getClass().toString(), e.getLocalizedMessage());

                    return null;
                }
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer profileResponse) {
            if (profileResponse == HttpURLConnection.HTTP_OK){
                new ProfileTask().execute(String.valueOf(getArguments().getInt(USER_ID, 0)));
            } else {
                try {
                    Toast.makeText(getActivity(), "Error in image uploading... Please try again", Toast.LENGTH_LONG).show();
                } catch (NullPointerException e){

                }
            }
        }
    }

    private class SendEndorsementTask extends AsyncTask<Endorsement, Void, Integer> {
        @Override
        protected Integer doInBackground(Endorsement... params) {
            try {
                String uri = Uri.parse(IShapeClient.BASE_URL + "SendNomination")
                        .buildUpon()
                        .appendQueryParameter("consignorId", String.valueOf(params[0].getConsignorId()))
                        .appendQueryParameter("consigneeId", String.valueOf(params[0].getConsigneeId()))
                        .appendQueryParameter("time", String.valueOf(params[0].getTime()))
                        .appendQueryParameter("category", params[0].getCategory())
                        .appendQueryParameter("description", String.valueOf(params[0].getDescription()))
                        .appendQueryParameter("point", String.valueOf(params[0].getPoint()))
                        .appendQueryParameter("id", String.valueOf(params[0].getId()))
                        .appendQueryParameter("acceptedFlag", String.valueOf(params[0].isAcceptedFlag()))
                        .build().toString();
                try {
                    URL url = new URL(uri);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.addRequestProperty("Cookie", "ASP.NET_SessionId=" +
                            Util.getSharedPrefence(getActivity(), R.string.preference_key_token));
                    int responseCode = urlConnection.getResponseCode();

                    Log.d(getClass().toString(), "response code: " + responseCode);

                    return responseCode;

                } catch (Exception e ) {
                    Log.d(getClass().toString(), e.getLocalizedMessage());

                    return null;
                }
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer endorsementResponse) {
            if (endorsementResponse == HttpURLConnection.HTTP_OK){
                Toast.makeText(getActivity(), "Endorsement sent ;)", Toast.LENGTH_LONG).show();
                endorsementDialog.cancel();
            } else {
                try {
                    Toast.makeText(getActivity(), "Error in endorsement sending... Please try again", Toast.LENGTH_LONG).show();
                } catch (NullPointerException e){

                }
            }
        }
    }
}
