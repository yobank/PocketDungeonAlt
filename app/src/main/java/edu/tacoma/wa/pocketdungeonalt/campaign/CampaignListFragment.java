/**
 * Fragment class to handle the main campaign list screen
 *
 * @author: James McHugh & Meng Yang
 */

package edu.tacoma.wa.pocketdungeonalt.campaign;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.tacoma.wa.pocketdungeonalt.R;
import edu.tacoma.wa.pocketdungeonalt.model.Campaign;

// Class for the main campaign list
public class CampaignListFragment extends Fragment {

    private List<Campaign> mCampaignList;
    private RecyclerView mRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_campaign_list, container, false);
        final EditText campaign_code = view.findViewById(R.id.campaign_code_input);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        Button add_button = view.findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_campaign_add);
            }
        });

        /** Set up search button listener.
         * Get campaign code from user entry. */
        Button search_button = view.findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String campaignId = campaign_code.getText().toString();
                StringBuilder url = new StringBuilder(getString(R.string.search_campaign));
                url.append(campaignId);
                new SearchCampaignTask().execute(url.toString());
            }
        });

        assert mRecyclerView != null;
        setupRecyclerView(mRecyclerView);
        SharedPreferences mSharedPreferences = getContext().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        int userID = mSharedPreferences.getInt(getString(R.string.USERID), 0);
        StringBuilder url = new StringBuilder(getString(R.string.get_campaigns));
        url.append(userID);
        new CampaignListFragment.CampaignTask().execute(url.toString());

        return view;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mCampaignList != null) {
            recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter
                    (this, mCampaignList));
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    /** Class to build RecyclerView and View holders. */
    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CampaignListFragment mParent;
        private final List<Campaign> mValues;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int campaignId = view.getId();
                StringBuilder url = new StringBuilder(getString(R.string.search_campaign));
                url.append(campaignId);
                new viewCampaignTask().execute(url.toString());
            }
        };

        // constructor
        SimpleItemRecyclerViewAdapter(CampaignListFragment parent,
                                      List<Campaign> items) {
            mParent = parent;
            mValues = items;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.campaign_list, parent, false);
            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mNameView.setText(mValues.get(position).getCampaignName());
            holder.mNotesView.setText(mValues.get(position).getGetCampaignDescription());
            holder.itemView.setId(mValues.get(position).getCampaignID());
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        // Return the size of campaign list (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /** Provide a reference to the views for each data item */
        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mNameView;
            final TextView mNotesView;

            ViewHolder(View view) {
                super(view);
                mNameView = view.findViewById(R.id.campaign_name_txt);
                mNotesView = view.findViewById(R.id.campaign_description_txt);
            }
        }
    }

    /** Send get request to server, construct a campaign list for display. */
    private class CampaignTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to download the list of campaigns, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    mCampaignList = Campaign.parseCampaignJson(jsonObject.getString("names"));
                    if (!mCampaignList.isEmpty()) {
                        setupRecyclerView(mRecyclerView);
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /** Search campaign by campaign code. If successful, go to join campaign screen and display result. */
    private class SearchCampaignTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to find the campaign, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getContext().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getBoolean("success")) {

                    Campaign campaign = Campaign.parseJoinCampaign(
                            jsonObject.getString("names"));

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("CAMPAIGN", (Serializable) campaign);
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_nav_campaigns_to_nav_campaign_join2, bundle);
                }
            } catch (JSONException e) {
                Toast.makeText(getContext().getApplicationContext(), "Unable to find campaign: Invalid Code",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /** Search campaign by campaign code. If successful, go to join campaign screen and display result. */
    private class viewCampaignTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to find the campaign, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getContext().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    Campaign campaign = Campaign.parseJoinCampaign(
                            jsonObject.getString("names"));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("CAMPAIGN", (Serializable) campaign);
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_action_campaign_view, bundle);
                }
            } catch (JSONException e) {
                Toast.makeText(getContext().getApplicationContext(), "Unable to find campaign: Invalid Code",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


}
