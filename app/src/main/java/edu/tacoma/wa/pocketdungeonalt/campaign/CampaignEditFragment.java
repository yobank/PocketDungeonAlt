package edu.tacoma.wa.pocketdungeonalt.campaign;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.wa.pocketdungeonalt.R;
import edu.tacoma.wa.pocketdungeonalt.model.Campaign;
import edu.tacoma.wa.pocketdungeonalt.model.User;

public class CampaignEditFragment extends Fragment {

    private Campaign campaign;
    private EditText campaign_name;
    private EditText campaign_description;
    private String campaign_notes;
    private Button campaign_notes_button;
    public Button add_button;

    private SharedPreferences mSharedPreferences;
    private JSONObject mCampaignJSON;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_campaign_edit, container, false);

        campaign = (Campaign) getArguments().getSerializable("CAMPAIGN");

        campaign_name = view.findViewById(R.id.campaign_name_input);
        campaign_description = view.findViewById(R.id.campaign_description_input);
        add_button = view.findViewById(R.id.add_button);
        campaign_notes_button = view.findViewById(R.id.notes_button);



        campaign_name.setText(campaign.getCampaignName());
        campaign_description.setText(campaign.getGetCampaignDescription());
        campaign_notes = campaign.getGetCampaignNotes();

        campaign_notes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotesDialog(getContext());
            }
        });

        /** Set up add button listener.
         * Get campaign name and notes from user entry. */
        Button add_button = view.findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String campaignName = campaign_name.getText().toString();
                String campaignDescription = campaign_description.getText().toString();
                mSharedPreferences = getContext().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                int userID = mSharedPreferences.getInt(getString(R.string.USERID), 0);

                StringBuilder url = new StringBuilder(getString(R.string.update_campaign));
                mCampaignJSON = new JSONObject();
                try {
                    mCampaignJSON.put(Campaign.ID, campaign.getCampaignID());
                    mCampaignJSON.put(Campaign.NAME, campaignName);
                    mCampaignJSON.put(Campaign.DESCRIPTION, campaignDescription);
                    mCampaignJSON.put(Campaign.NOTES, campaign_notes);
                    mCampaignJSON.put(User.ID, userID);
                    new CampaignEditFragment.AddCampaignTask().execute(url.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



        return view;
    }

    private void showNotesDialog(Context c) {
        final EditText notes = new EditText(c);
        notes.setText(campaign_notes);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Campaign Notes")
                .setView(notes)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userText = String.valueOf(notes.getText());
                        campaign_notes = userText;
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }



    /** Send post request to server, adding campaign details into server. */
    private class AddCampaignTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("PUT");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr =
                            new OutputStreamWriter(urlConnection.getOutputStream());
                    // For Debugging
                    Log.i("Add_Campaign", mCampaignJSON.toString());

                    wr.write(mCampaignJSON.toString());
                    wr.flush();
                    wr.close();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to update the new campaign, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to update the new campaign")) {
                Toast.makeText(getContext().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                // For Debugging
                Log.i("Add_campaign", jsonObject.toString());

                if (jsonObject.getBoolean("success")) {
                    Toast.makeText(getContext().getApplicationContext(), "Campaign Updated successfully"
                            , Toast.LENGTH_SHORT).show();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("CAMPAIGN", (Serializable) campaign);
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_campaigns);

                }
                else {
                    Toast.makeText(getContext().getApplicationContext(), "Campaign couldn't be updated: "
                                    + jsonObject.getString("error")
                            , Toast.LENGTH_LONG).show();
                    Log.e("Add_Campaign", jsonObject.getString("error"));
                }
            } catch (JSONException e) {
                Toast.makeText(getContext().getApplicationContext(), "JSON Parsing error on updating campaign"
                                + e.getMessage()
                        , Toast.LENGTH_LONG).show();
                Log.e("Add_Campaign", e.getMessage());
            }
        }
    }



}