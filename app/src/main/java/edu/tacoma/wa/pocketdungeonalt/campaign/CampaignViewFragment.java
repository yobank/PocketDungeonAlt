/**
 * Fragment class to handle viewing campaigns
 *
 * @author: James McHugh & Meng Yang
 */

package edu.tacoma.wa.pocketdungeonalt.campaign;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

// Class for the campaign view
public class CampaignViewFragment extends Fragment {

    private List<Character> mCharacterList;
    private RecyclerView mRecyclerView;
    private String campaign_notes;
    private Campaign campaign;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_campaign_view, container, false);
        campaign = (Campaign) getArguments().getSerializable("CAMPAIGN");
        SharedPreferences mSharedPreferences = getContext()
                .getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        final int userID = mSharedPreferences.getInt(getString(R.string.USERID), 0);

        TextView campaign_name = view.findViewById(R.id.campaign_name_txt);
        TextView campaign_code = view.findViewById(R.id.code_label);
        TextView campaign_description = view.findViewById(R.id.campaign_description_txt);
        Button campaign_notes_button = view.findViewById(R.id.notes_button);
        Button edit_button = view.findViewById(R.id.edit_button);
        Button share_button = view.findViewById(R.id.share_button);

        String temp = "Campaign Code: " + campaign.getCampaignID();
        campaign_name.setText(campaign.getCampaignName());
        campaign_code.setText(temp);
        campaign_description.setText(campaign.getGetCampaignDescription());
        campaign_notes = campaign.getGetCampaignNotes();

        campaign_notes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotesDialog(getContext());
            }
        });

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (campaign.getCreatorID() == userID) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("CAMPAIGN", (Serializable) campaign);
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_nav_campaign_view_to_campaignEditFragment, bundle);
                }
                else {
                    Toast.makeText(getContext().getApplicationContext(), "Only the owner of this campaign may edit it.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Join me in " + campaign.getCampaignName() + " on Pocket Dungeon with code " + campaign.getCampaignID() + "!");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        StringBuilder url = new StringBuilder(getString(R.string.search_characters));
        url.append(campaign.getCampaignID());
        new CampaignViewFragment.CharactersTask().execute(url.toString());

        mRecyclerView = view.findViewById(R.id.player_list);
        assert mRecyclerView != null;
        setupRecyclerView(mRecyclerView);

        return view;
    }

    // displays the popup window for campaign notes
    private void showNotesDialog(Context c) {
        final TextView notes = new TextView(c);
        notes.setText(campaign_notes);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Campaign Notes")
                .setView(notes)
                .setNegativeButton("Close", null)
                .create();
        dialog.show();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mCharacterList != null) {
            recyclerView.setAdapter(new CampaignViewFragment.SimpleItemRecyclerViewAdapter(this, mCharacterList));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<CampaignViewFragment.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CampaignViewFragment mParentActivity;
        private final List<Character> mValues;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Character item = (Character) view.getTag();
                Bundle bundle = new Bundle();
                bundle.putSerializable("CHARACTER", (Serializable) item);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_campaign_view_to_nav_character_view, bundle);
            }
        };

                SimpleItemRecyclerViewAdapter(CampaignViewFragment parent,
                                      List<Character> items) {
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.campaign_character_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mNameView.setText(mValues.get(position).getCharacterName());
            holder.mClassView.setText("Class: " + mValues.get(position).getCharacterClass());
            holder.mLevelView.setText("Level: " + mValues.get(position).getCharacterLevel());
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mNameView;
            final TextView mClassView;
            final TextView mLevelView;

            ViewHolder(View view) {
                super(view);
                mNameView = view.findViewById(R.id.character_name_txt);
                mClassView = view.findViewById(R.id.character_class_txt);
                mLevelView = view.findViewById(R.id.character_level_txt);
            }
        }
    }


    private class CharactersTask extends AsyncTask<String, Void, String> {
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
                    mCharacterList = Character.parseCharacterJSON(
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