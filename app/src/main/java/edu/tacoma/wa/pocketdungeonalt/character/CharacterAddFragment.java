package edu.tacoma.wa.pocketdungeonalt.character;

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
import edu.tacoma.wa.pocketdungeonalt.model.Character;
import edu.tacoma.wa.pocketdungeonalt.model.User;

public class CharacterAddFragment extends Fragment {

    private EditText character_name;
    private EditText character_class;
    private EditText character_race;
    private EditText character_level;
    private String background = "no background";
    private EditText alignment;
    private String info = "no info";
    private EditText experience;
    private EditText inspiration;
    private EditText proficiency;
    private EditText armor_class;
    private EditText initiative;
    private EditText speed;
    private EditText maxHP;
    private EditText currentHP;
    private EditText hit_dice;
    private String skills = "no skills";
    private EditText strength;
    private EditText dexterity;
    private EditText constitution;
    private EditText intelligence;
    private EditText wisdom;
    private EditText charisma;
    private EditText perception;
    private String attacks = "no attacks";
    private String equipment = "no equipment";
    private String other_proficiencies = "no other proficiencies";

    private SharedPreferences mSharedPreferences;
    private JSONObject mCharacterJSON;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_character_add, container, false);

        character_name = view.findViewById(R.id.character_name_input);
        character_class = view.findViewById(R.id.character_class_input);
        character_race = view.findViewById(R.id.character_race_input);
        character_level = view.findViewById(R.id.character_level_input);
        Button button_background = view.findViewById(R.id.background_button);
        alignment  = view.findViewById(R.id.character_alignment_input);
        Button button_info = view.findViewById(R.id.info_button);
        experience = view.findViewById(R.id.character_experience_input);
        inspiration = view.findViewById(R.id.character_inspiration_input);
        proficiency = view.findViewById(R.id.proficiency_input);
        armor_class = view.findViewById(R.id.ac_input);
        initiative = view.findViewById(R.id.character_initiative_input);
        speed = view.findViewById(R.id.speed_input);
        maxHP = view.findViewById(R.id.max_hp_input);
        currentHP = view.findViewById(R.id.current_hp_input);
        hit_dice = view.findViewById(R.id.hit_dice_input);
        Button button_skills = view.findViewById(R.id.skills_button);
        strength = view.findViewById(R.id.str_input);
        dexterity = view.findViewById(R.id.dex_input);
        constitution  = view.findViewById(R.id.con_input);
        intelligence = view.findViewById(R.id.int_input);
        wisdom = view.findViewById(R.id.wis_input);
        charisma = view.findViewById(R.id.cha_input);
        perception = view.findViewById(R.id.character_perception_input);
        Button button_attacks = view.findViewById(R.id.attacks_button);
        Button button_equipment = view.findViewById(R.id.equipment_button);
        Button button_other_proficiencies = view.findViewById(R.id.otherProf_button);
        Button add_button = view.findViewById(R.id.add_button);
        Button cancel_button = view.findViewById(R.id.cancell_button);


        /** Set up add button listener.
         * Get campaign name and notes from user entry. */
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // make default values or prevent creation unless values are filled in

                String characterName = character_name.getText().toString();
                String characterClass = character_class.getText().toString();
                String characterRace = character_race.getText().toString();
                int characterLevel = 0;
                if (!character_level.getText().toString().matches("")) { characterLevel = Integer.parseInt(character_level.getText().toString()); }
                String characterAlignment = alignment.getText().toString();
                int characterExperience = 0;
                if (!experience.getText().toString().matches("")) { characterExperience = Integer.parseInt(experience.getText().toString()); }
                int characterInspiration = 0;
                if (!inspiration.getText().toString().matches("")) { characterInspiration = Integer.parseInt(inspiration.getText().toString()); }
                int characterProficiency = 0;
                if (!proficiency.getText().toString().matches("")) { characterProficiency = Integer.parseInt(proficiency.getText().toString()); }
                int armorClass = 0;
                if (!armor_class.getText().toString().matches("")) { armorClass = Integer.parseInt(armor_class.getText().toString()); }
                int init = 0;
                if (!initiative.getText().toString().matches("")) { init = Integer.parseInt(initiative.getText().toString()); }
                String spd = speed.getText().toString();
                int max_HP = 0;
                if (!maxHP.getText().toString().matches("")) { max_HP = Integer.parseInt(maxHP.getText().toString()); }
                int current_HP = 0;
                if (!currentHP.getText().toString().matches("")) { current_HP = Integer.parseInt(currentHP.getText().toString()); }
                String hitDice = hit_dice.getText().toString();
                int str = 0;
                if (!strength.getText().toString().matches("")) { str = Integer.parseInt(strength.getText().toString()); }
                int dex = 0;
                if (!dexterity.getText().toString().matches("")) { dex = Integer.parseInt(dexterity.getText().toString()); }
                int con = 0;
                if (!constitution.getText().toString().matches("")) { con = Integer.parseInt(constitution.getText().toString()); }
                int inti = 0;
                if (!intelligence.getText().toString().matches("")) { inti = Integer.parseInt(intelligence.getText().toString()); }
                int wis = 0;
                if (!wisdom.getText().toString().matches("")) { wis = Integer.parseInt(wisdom.getText().toString()); }
                int cha = 0;
                if (!charisma.getText().toString().matches("")) { cha = Integer.parseInt(charisma.getText().toString()); }
                int prcp = 0;
                if (!perception.getText().toString().matches("")) { prcp = Integer.parseInt(perception.getText().toString()); }
                mSharedPreferences = getContext().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                int userID = mSharedPreferences.getInt(getString(R.string.USERID), 0);

                StringBuilder url = new StringBuilder(getString(R.string.add_character));
                mCharacterJSON = new JSONObject();
                try {
                    mCharacterJSON.put(Character.CHARACTERNAME, characterName);
                    mCharacterJSON.put(Character.CHARACTERCLASS, characterClass);
                    mCharacterJSON.put(Character.CHARACTERRACE, characterRace);
                    mCharacterJSON.put(Character.CHARACTERLEVEL, characterLevel);
                    mCharacterJSON.put(Character.CHARACTERBACKGROUND, background);
                    mCharacterJSON.put(Character.CHARACTERALIGNMENT, characterAlignment);
                    mCharacterJSON.put(Character.CHARACTERINFO, info);
                    mCharacterJSON.put(Character.EXPERIENCE, characterExperience);
                    mCharacterJSON.put(Character.INSPIRATION, characterInspiration);
                    mCharacterJSON.put(Character.PROFICIENCY, characterProficiency);
                    mCharacterJSON.put(Character.ARMORCLASS, armorClass);
                    mCharacterJSON.put(Character.INITIATIVE, init);
                    mCharacterJSON.put(Character.SPEED, spd);
                    mCharacterJSON.put(Character.MAXHP, max_HP);
                    mCharacterJSON.put(Character.CURRENTHP, current_HP);
                    mCharacterJSON.put(Character.HITDICE, hitDice);
                    mCharacterJSON.put(Character.SKILLS, skills);
                    mCharacterJSON.put(Character.STRENGTH, str);
                    mCharacterJSON.put(Character.DEXTERITY, dex);
                    mCharacterJSON.put(Character.CONSTITUTION, con);
                    mCharacterJSON.put(Character.INTELLIGENCE, inti);
                    mCharacterJSON.put(Character.WISDOM, wis);
                    mCharacterJSON.put(Character.CHARISMA, cha);
                    mCharacterJSON.put(Character.PERCEPTION, prcp);
                    mCharacterJSON.put(Character.ATTACKS, attacks);
                    mCharacterJSON.put(Character.EQUIPMENT, equipment);
                    mCharacterJSON.put(Character.OTHERPROFICIENCIES, other_proficiencies);
                    mCharacterJSON.put(User.ID, userID);
                    new CharacterAddFragment.AddCharacterTask().execute(url.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        button_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBackgroundDialog(getContext());
            }
        });

        button_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoDialog(getContext());
            }
        });

        button_skills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSkillsDialog(getContext());
            }
        });

        button_attacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAttacksDialog(getContext());
            }
        });

        button_equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEquipmentDialog(getContext());
            }
        });

        button_other_proficiencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOtherProfDialog(getContext());
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_characters);
            }
        });
        return view;
    }


    private void showBackgroundDialog(Context c) {
        final EditText editText = new EditText(c);
        editText.setText(background);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Edit Background")
                .setView(editText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userText = String.valueOf(editText.getText());
                        background = userText;
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void showInfoDialog(Context c) {
        final EditText editText = new EditText(c);
        editText.setText(info);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Edit Info")
                .setView(editText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userText = String.valueOf(editText.getText());
                        info = userText;
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    // can improve this later by changing from text entry to list of avalible skills
    private void showSkillsDialog(Context c) {
        final EditText editText = new EditText(c);
        editText.setText(skills);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Edit Skills")
                .setView(editText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userText = String.valueOf(editText.getText());
                        skills = userText;
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void showAttacksDialog(Context c) {
        final EditText editText = new EditText(c);
        editText.setText(attacks);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Edit Attacks")
                .setView(editText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userText = String.valueOf(editText.getText());
                        attacks = userText;
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void showEquipmentDialog(Context c) {
        final EditText editText = new EditText(c);
        editText.setText(equipment);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Edit Equipment")
                .setView(editText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userText = String.valueOf(editText.getText());
                        equipment = userText;
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void showOtherProfDialog(Context c) {
        final EditText editText = new EditText(c);
        editText.setText(other_proficiencies);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Edit Other Proficiencies")
                .setView(editText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userText = String.valueOf(editText.getText());
                        other_proficiencies = userText;
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }


    /** Send post request to server, adding campaign details into server. */
    private class AddCharacterTask extends AsyncTask<String, Void, String> {

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
                    // For Debugging
                    Log.i("Add_Character", mCharacterJSON.toString());

                    wr.write(mCharacterJSON.toString());
                    wr.flush();
                    wr.close();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to add the new character, Reason: "
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
            if (s.startsWith("Unable to add the new character")) {
                Toast.makeText(getContext().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                // For Debugging
                Log.i("Add_character", jsonObject.toString());

                if (jsonObject.getBoolean("success")) {
                    Toast.makeText(getContext().getApplicationContext(), "Character Added successfully"
                            , Toast.LENGTH_SHORT).show();

                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_characters);

                }
                else {
                    Toast.makeText(getContext().getApplicationContext(), "Character couldn't be added: "
                                    + jsonObject.getString("error")
                            , Toast.LENGTH_LONG).show();
                    Log.e("Add_Campaign", jsonObject.getString("error"));
                }
            } catch (JSONException e) {
                Toast.makeText(getContext().getApplicationContext(), "JSON Parsing error on Adding character"
                                + e.getMessage()
                        , Toast.LENGTH_LONG).show();
                Log.e("Add_Character", e.getMessage());
            }
        }
    }

    /** Search character by character code. If successful, go to join character screen and display result. */
    private class SearchCharacterTask extends AsyncTask<String, Void, String> {
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
                    response = "Unable to find the character, Reason: "
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

//                    Character character = Character.parseJoinCharacter(
//                            jsonObject.getString("names"));
//
//                    //Intent intent = new Intent(CampaignAddActivity.this, CampaignJoinActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("CAMPAIGN", (Serializable) campaign);
                    //intent.putExtras(bundle);
                    //startActivity(intent);
                }

            } catch (JSONException e) {
                Toast.makeText(getContext().getApplicationContext(), "Unable to find campaign: Invalid Code",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}