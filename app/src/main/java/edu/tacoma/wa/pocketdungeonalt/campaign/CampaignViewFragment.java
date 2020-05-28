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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.wa.pocketdungeonalt.R;
import edu.tacoma.wa.pocketdungeonalt.model.Campaign;
import edu.tacoma.wa.pocketdungeonalt.model.User;

public class CampaignViewFragment extends Fragment {

    private TextView campaign_name;
    private TextView campaign_description;
    private TextView campaign_notes;
    private SharedPreferences mSharedPreferences;
    private JSONObject mCampaignJSON;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_campaign_view, container, false);

        int campaignid = getArguments().getInt("campaignid");

        campaign_name = view.findViewById(R.id.campaign_name_txt);
        campaign_description = view.findViewById(R.id.campaign_description_txt);
        campaign_notes = view.findViewById(R.id.campaign_notes_txt);

        return view;
    }







}