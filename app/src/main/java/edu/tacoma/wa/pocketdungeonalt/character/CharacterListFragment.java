package edu.tacoma.wa.pocketdungeonalt.character;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.tacoma.wa.pocketdungeonalt.R;
import edu.tacoma.wa.pocketdungeonalt.character.CharacterListFragment;
import edu.tacoma.wa.pocketdungeonalt.model.Character;

public class CharacterListFragment extends Fragment {

    private List<Character> mCharacterList;
    private RecyclerView mRecyclerView;
    private SharedPreferences mSharedPreferences;
    private JSONObject mCharacterJSON;
    private StringBuilder url;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_character_list, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        Button add_button = view.findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_character_add);

            }
        });

        assert mRecyclerView != null;
        setupRecyclerView(mRecyclerView);

        /** Use SharedPreferences to retrieve userID for query. */
        mSharedPreferences = getContext().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        int userID = mSharedPreferences.getInt(getString(R.string.USERID), 0);

        /** Set up url and append userID in the url query field. */
        url = new StringBuilder(getString(R.string.get_characters));
        url.append(userID);

        mCharacterJSON = new JSONObject();
        new CharacterListFragment.CharacterTask().execute(url.toString());

        return view;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mCharacterList != null) {
            recyclerView.setAdapter(new CharacterListFragment.SimpleItemRecyclerViewAdapter
                    (this, mCharacterList));
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    /** Class to build RecyclerView and View holders. */
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CharacterListFragment mParent;
        private final List<Character> mValues;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Character item = (Character) view.getTag();

                Navigation.findNavController((Activity)view.getContext(), R.id.nav_host_fragment).navigate(R.id.nav_character_add);

                // open character view

//                Context context = view.getContext();
//                Intent intent = new Intent(context, CharacterDetailFragment.class);
//                intent.putExtra(CharacterDetailFragment.ARG_ITEM_ID, item);
//
//                context.startActivity(intent);
            }
        };

        SimpleItemRecyclerViewAdapter(CharacterListFragment parent,
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
            holder.mClassView.setText(mValues.get(position).getCharacterClass());
            holder.mLevelView.setText("" + mValues.get(position).getCharacterLevel());
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

                    // For Debugging
                    Log.i("characterJson", mCharacterJSON.toString());

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
