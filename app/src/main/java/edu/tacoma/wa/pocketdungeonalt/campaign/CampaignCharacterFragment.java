package edu.tacoma.wa.pocketdungeonalt.campaign;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.tacoma.wa.pocketdungeonalt.R;

public class CampaignCharacterFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<String> mCharacterList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_campaign_character, container, false);
        //Intent intent = getIntent();
//        int campaignId = intent.getIntExtra("campaignID", 0);
//        StringBuilder url = new StringBuilder(getString(R.string.search_characters));
//        url.append(campaignId);
//
//        new CampaignCharacterFragment.CharacterTask().execute(url.toString());

        mRecyclerView = view.findViewById(R.id.campaign_character_list);
        assert mRecyclerView != null;
        setupRecyclerView(mRecyclerView);

        return view;

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mCharacterList != null) {
            recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, mCharacterList));
        }
        //recyclerView.setLayoutManager(new LinearLayoutManager(CampaignCharacterFragment.this));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CampaignCharacterFragment mParentActivity;
        private final List<String> mValues;

        SimpleItemRecyclerViewAdapter(CampaignCharacterFragment parent,
                                      List<String> items) {
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
                mNameView = view.findViewById(R.id.character_name);
            }
        }
    }

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
//                    mCharacterList = Character.parseCharacterList(
//                            jsonObject.getString("names"));
//
//                    if (!mCharacterList.isEmpty()) {
//                        setupRecyclerView(mRecyclerView);
//                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getContext().getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
