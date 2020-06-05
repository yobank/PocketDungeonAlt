/**
 * Fragment class to handle joining campaigns
 *
 * @author: James McHugh & Meng Yang
 */
package edu.tacoma.wa.pocketdungeonalt.campaign;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import edu.tacoma.wa.pocketdungeonalt.model.Character;

// Class to handle the screen for joining a campaign
public class CampaignJoinFragment extends Fragment {

    private List<String> mCharacterList;
    private RecyclerView mRecyclerView;

    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View view = inflater.inflate(R.layout.fragment_campaign_join, container, false);
        final Campaign campaign = (Campaign) getArguments().getSerializable("CAMPAIGN");

        SharedPreferences mSharedPreferences = getContext().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);
        mSharedPreferences.edit()
                .putInt(getString(R.string.CAMPAIGNID), campaign.getCampaignID())
                .apply();

        String campaignId = "Code: " + campaign.getCampaignID();
        String campaignName = "Name: " + campaign.getCampaignName();
        String campaignDesc = "Description: " + campaign.getGetCampaignDescription();

        TextView mIdView = view.findViewById(R.id.campaignId_txt);
        TextView mNameView = view.findViewById(R.id.campaignName_txt);
        TextView mDescView = view.findViewById(R.id.campaignDesc_txt);

        mIdView.setText(campaignId);
        mNameView.setText(campaignName);
        mDescView.setText(campaignDesc);

        Button charButton = view.findViewById(R.id.select_char_btn);
        charButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("CAMPAIGN", (Serializable) campaign);
                Navigation.findNavController((Activity)view.getContext(), R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_campaign_join_to_nav_character_selector, bundle);
            }
        });

        StringBuilder url = new StringBuilder(getString(R.string.search_characters));
        url.append(campaign.getCampaignID());
        new otherPlayersTask().execute(url.toString());

        mRecyclerView = view.findViewById(R.id.player_list);
        assert mRecyclerView != null;
        setupRecyclerView(mRecyclerView);

        return view;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mCharacterList != null) {
            recyclerView.setAdapter(new CampaignJoinFragment.SimpleItemRecyclerViewAdapter(this, mCharacterList));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<CampaignJoinFragment.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CampaignJoinFragment mParentActivity;
        private final List<String> mValues;

        SimpleItemRecyclerViewAdapter(CampaignJoinFragment parent,
                                      List<String> items) {
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public CampaignJoinFragment.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.campaign_character_list, parent, false);
            return new CampaignJoinFragment.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final CampaignJoinFragment.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mNameView.setText(mValues.get(position));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mNameView;

            ViewHolder(View view) {
                super(view);
                mNameView = view.findViewById(R.id.character_name_txt);
            }
        }
    }

    private class otherPlayersTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    /** Get response from server. */
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to download the list of characters, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /** If character is retrieved successfully, inform user.
         * Otherwise, send error message.
         * @param s response message
         */
        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getContext().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getBoolean("success")) {
                    mCharacterList = Character.parseCharacterList(
                            jsonObject.getString("names"));

                    if (!mCharacterList.isEmpty()) {
                        setupRecyclerView(mRecyclerView);
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getContext().getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
