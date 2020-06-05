package edu.tacoma.wa.pocketdungeonalt.campaign;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.wa.pocketdungeonalt.R;
import edu.tacoma.wa.pocketdungeonalt.model.Campaign;
import edu.tacoma.wa.pocketdungeonalt.model.User;

public class CampaignAddFragment extends Fragment {

    private EditText campaign_name;
    private EditText campaign_description;
    private SharedPreferences mSharedPreferences;
    private JSONObject mCampaignJSON;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_campaign_add, container, false);

        campaign_name = view.findViewById(R.id.campaign_name_input);
        campaign_description = view.findViewById(R.id.campaign_description_input);
        Button add_button = view.findViewById(R.id.add_button);
        Button cancel_button = view.findViewById(R.id.cancell_button);

        /** Set up add button listener.
         * Get campaign name and notes from user entry. */
        //Button add_button = view.findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String campaignName = campaign_name.getText().toString();
                String campaignNotes = campaign_description.getText().toString();
                mSharedPreferences = getContext().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                int userID = mSharedPreferences.getInt(getString(R.string.USERID), 0);

                StringBuilder url = new StringBuilder(getString(R.string.add_campaign));
                mCampaignJSON = new JSONObject();
                try {
                    mCampaignJSON.put(Campaign.NAME, campaignName);
                    mCampaignJSON.put(Campaign.DESCRIPTION, campaignNotes);
                    mCampaignJSON.put(Campaign.NOTES, "sample notes");
                    mCampaignJSON.put(User.ID, userID);
                    new CampaignAddFragment.AddCampaignTask().execute(url.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_campaigns);
            }
        });
        return view;
    }

    public void onActivityCreated (@NonNull Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr =
                            new OutputStreamWriter(urlConnection.getOutputStream());

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
                    response = "Unable to add the new campaign, Reason: "
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
            if (s.startsWith("Unable to add the new campaign")) {
                Toast.makeText(getContext().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getBoolean("success")) {
                    Toast.makeText(getContext().getApplicationContext(), "Campaign Added successfully"
                            , Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_campaigns);
                }
                else {
                    Toast.makeText(getContext().getApplicationContext(), "Campaign couldn't be added: "
                                    + jsonObject.getString("error")
                            , Toast.LENGTH_LONG).show();
                    Log.e("Add_Campaign", jsonObject.getString("error"));
                }
            } catch (JSONException e) {
                Toast.makeText(getContext().getApplicationContext(), "JSON Parsing error on Adding campaign"
                                + e.getMessage()
                        , Toast.LENGTH_LONG).show();
                Log.e("Add_Campaign", e.getMessage());
            }
        }
    }
}