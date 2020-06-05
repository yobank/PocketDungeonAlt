/**
 * Fragment class to handle character selection for campaigns
 *
 * @author: James McHugh
 */
package edu.tacoma.wa.pocketdungeonalt.character;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.tacoma.wa.pocketdungeonalt.R;
import edu.tacoma.wa.pocketdungeonalt.model.Campaign;
import edu.tacoma.wa.pocketdungeonalt.model.Character;
import edu.tacoma.wa.pocketdungeonalt.model.User;

// Class for the character selector which appears during campaign join
public class CharacterSelectorFragment extends Fragment {

    private List<Character> mCharacterList;
    private RecyclerView mRecyclerView;
    private JSONObject mCharacterJSON;
    private static JSONObject mCampCharJSON;
    public Campaign campaign;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_character_selector, container, false);
        campaign = (Campaign) getArguments().getSerializable("CAMPAIGN");

        mRecyclerView = view.findViewById(R.id.recyclerView);
        assert mRecyclerView != null;
        setupRecyclerView(mRecyclerView);

        /** Use SharedPreferences to retrieve userID for query. */
        SharedPreferences mSharedPreferences = getContext().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        int userID = mSharedPreferences.getInt(getString(R.string.USERID), 0);

        /** Set up url and append userID in the url query field. */
        StringBuilder url = new StringBuilder(getString(R.string.get_characters));
        url.append(userID);
        mCampCharJSON = new JSONObject();
        mCharacterJSON = new JSONObject();
        try {
            mCampCharJSON.put(User.ID, userID);
            mCampCharJSON.put(Campaign.ID, campaign.getCampaignID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new CharacterSelectorFragment.CharacterTask().execute(url.toString());

        return view;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mCharacterList != null) {
            recyclerView.setAdapter(new CharacterSelectorFragment.SimpleItemRecyclerViewAdapter
                    (this, mCharacterList));
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    /** Class to build RecyclerView and View holders. */
    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CharacterSelectorFragment mParent;
        private final List<Character> mValues;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Character item = (Character) view.getTag();
                try {
                    mCampCharJSON.put("characterid", item.getCharacterID());

                    // does url have json?
                    StringBuilder url2 = new StringBuilder(getString(R.string.join_campaign));
                    new JoinCampaignTask().execute(url2.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        SimpleItemRecyclerViewAdapter(CharacterSelectorFragment parent,
                                      List<Character> items) {
            mParent = parent;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.character_list, parent, false);
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

        /** Return the size of character list (invoked by the layout manager) */
        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mNameView;
            final TextView mClassView;
            final TextView mLevelView;
            LinearLayout mainLayout;

            ViewHolder(View view) {
                super(view);
                mNameView = view.findViewById(R.id.character_name_txt);
                mClassView = view.findViewById(R.id.character_class_txt);
                mLevelView = view.findViewById(R.id.character_level_txt);
                mainLayout = view.findViewById(R.id.mainLayout);
            }
        }
    }

    /** Send get request to server, construct a character list for display. */
    private class CharacterTask extends AsyncTask<String, Void, String> {
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

    /** Send post request and add join campaign info into server. */
    private class JoinCampaignTask extends AsyncTask<String, Void, String> {

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
                    wr.write(mCampCharJSON.toString());
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
                    Toast.makeText(getContext().getApplicationContext(), "Campaign joined successfully"
                            , Toast.LENGTH_SHORT).show();

                    // navigate back to campaign list
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_campaigns);

                }
                else {
                    Toast.makeText(getContext().getApplicationContext(), "Character is already part of this campaign"
                            , Toast.LENGTH_LONG).show();
                    Log.e("Join_Campaign", jsonObject.getString("error"));
                }
            } catch (JSONException e) {
                Toast.makeText(getContext().getApplicationContext(), "JSON Parsing error on joining campaign"
                                + e.getMessage()
                        , Toast.LENGTH_LONG).show();
                Log.e("Join_Campaign", e.getMessage());
            }
        }
    }

}
