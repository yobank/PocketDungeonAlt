/**
 * Fragment class to handle viewing characters
 *
 * @author: James McHugh
 */
package edu.tacoma.wa.pocketdungeonalt.character;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import java.io.Serializable;

import edu.tacoma.wa.pocketdungeonalt.R;
import edu.tacoma.wa.pocketdungeonalt.model.Character;

// Class to handle viewing characters
public class CharacterViewFragment extends Fragment {

    // default values for string fields
    private String background = "no background";
    private String info = "no info";
    private String skills = "no skills";
    private String attacks = "no attacks";
    private String equipment = "no equipment";
    private String other_proficiencies = "no other proficiencies";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_character_view, container, false);

        final Character character = (Character) getArguments().getSerializable("CHARACTER");

        TextView character_name = view.findViewById(R.id.character_name_input);
        character_name.setText(character.getCharacterName());
        TextView character_class = view.findViewById(R.id.character_class_input);
        character_class.setText(character.getCharacterClass());
        TextView character_race = view.findViewById(R.id.character_race_input);
        character_race.setText(character.getCharacterRace());
        TextView character_level = view.findViewById(R.id.character_level_input);
        character_level.setText(String.valueOf(character.getCharacterLevel()));
        Button button_background = view.findViewById(R.id.background_button);
        background = character.getCharacterBackground();
        TextView alignment = view.findViewById(R.id.character_alignment_input);
        alignment.setText(character.getCharacterAlignment());
        Button button_info = view.findViewById(R.id.info_button);
        info = character.getCharacterInfo();
        TextView experience = view.findViewById(R.id.character_experience_input);
        experience.setText(String.valueOf(character.getExperience()));
        TextView inspiration = view.findViewById(R.id.character_inspiration_input);
        inspiration.setText(String.valueOf(character.getInspiration()));
        TextView proficiency = view.findViewById(R.id.proficiency_input);
        proficiency.setText(String.valueOf(character.getProficiency()));
        TextView armor_class = view.findViewById(R.id.ac_input);
        armor_class.setText(String.valueOf(character.getArmorClass()));
        TextView initiative = view.findViewById(R.id.character_initiative_input);
        initiative.setText(String.valueOf(character.getInitiative()));
        TextView speed = view.findViewById(R.id.speed_input);
        speed.setText(character.getSpeed());
        TextView maxHP = view.findViewById(R.id.max_hp_input);
        maxHP.setText(String.valueOf(character.getMaxHP()));
        TextView currentHP = view.findViewById(R.id.current_hp_input);
        currentHP.setText(String.valueOf(character.getCurrentHP()));
        TextView hit_dice = view.findViewById(R.id.hit_dice_input);
        hit_dice.setText(character.getHitDice());
        Button button_skills = view.findViewById(R.id.skills_button);
        skills = character.getSkills();
        TextView strength = view.findViewById(R.id.str_input);
        strength.setText(String.valueOf(character.getStrength()));
        TextView dexterity = view.findViewById(R.id.dex_input);
        dexterity.setText(String.valueOf(character.getDexterity()));
        TextView constitution = view.findViewById(R.id.con_input);
        constitution.setText(String.valueOf(character.getConstitution()));
        TextView intelligence = view.findViewById(R.id.int_input);
        intelligence.setText(String.valueOf(character.getIntelligence()));
        TextView wisdom = view.findViewById(R.id.wis_input);
        wisdom.setText(String.valueOf(character.getWisdom()));
        TextView charisma = view.findViewById(R.id.cha_input);
        charisma.setText(String.valueOf(character.getCharisma()));
        TextView perception = view.findViewById(R.id.character_perception_input);
        perception.setText(String.valueOf(character.getPerception()));
        Button button_attacks = view.findViewById(R.id.attacks_button);
        attacks = character.getAttacks();
        Button button_equipment = view.findViewById(R.id.equipment_button);
        equipment = character.getEquipment();
        Button button_other_proficiencies = view.findViewById(R.id.otherProf_button);
        other_proficiencies = character.getOtherProficiencies();

        Button add_button = view.findViewById(R.id.add_button);

        // calculate stat modifiers
        TextView strMod = view.findViewById(R.id.str_mod);
        TextView dexMod = view.findViewById(R.id.dex_mod);
        TextView conMod = view.findViewById(R.id.con_mod);
        TextView intMod = view.findViewById(R.id.int_mod);
        TextView wisMod = view.findViewById(R.id.wis_mod);
        TextView chaMod = view.findViewById(R.id.cha_mod);

        strMod.setText(calcMod(Integer.parseInt(strength.getText().toString())));
        dexMod.setText(calcMod(Integer.parseInt(dexterity.getText().toString())));
        conMod.setText(calcMod(Integer.parseInt(constitution.getText().toString())));
        intMod.setText(calcMod(Integer.parseInt(intelligence.getText().toString())));
        wisMod.setText(calcMod(Integer.parseInt(wisdom.getText().toString())));
        chaMod.setText(calcMod(Integer.parseInt(charisma.getText().toString())));

        SharedPreferences mSharedPreferences = getContext().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        final int userID = mSharedPreferences.getInt(getString(R.string.USERID), 0);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (character.getCreatorID() == userID) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("CHARACTER", (Serializable) character);
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_nav_character_view_to_nav_character_edit, bundle);
                }
                else {
                    Toast.makeText(getContext().getApplicationContext(), "Only the owner of this character may edit it.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // set up button listeners
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

        return view;
    }

    // function to calculate stat modifiers, returns a formatted string
    private String calcMod(double val) {
        double vall = (val - 10) / 2;
        if (vall < 0) {
            val = Math.floor(vall);
            return String.valueOf((int)val);
        }
        else {
            val = Math.ceil(vall);
            return "+" + String.valueOf((int)val);
        }
    }

    // functions to show dialog popups
    private void showBackgroundDialog(Context c) {
        final TextView editText = new TextView(c);
        editText.setText(background);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Background")
                .setView(editText)
                .setNegativeButton("Close", null)
                .create();
        dialog.show();
    }

    private void showInfoDialog(Context c) {
        final TextView editText = new TextView(c);
        editText.setText(info);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Info")
                .setView(editText)
                .setNegativeButton("Close", null)
                .create();
        dialog.show();
    }

    private void showSkillsDialog(Context c) {
        final TextView editText = new TextView(c);
        editText.setText(skills);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Skills")
                .setView(editText)
                .setNegativeButton("Close", null)
                .create();
        dialog.show();
    }

    private void showAttacksDialog(Context c) {
        final TextView editText = new TextView(c);
        editText.setText(attacks);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Attacks")
                .setView(editText)
                .setNegativeButton("Close", null)
                .create();
        dialog.show();
    }

    private void showEquipmentDialog(Context c) {
        final TextView editText = new TextView(c);
        editText.setText(equipment);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Equipment")
                .setView(editText)
                .setNegativeButton("Close", null)
                .create();
        dialog.show();
    }

    private void showOtherProfDialog(Context c) {
        final TextView editText = new TextView(c);
        editText.setText(other_proficiencies);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Other Proficiencies")
                .setView(editText)
                .setNegativeButton("Close", null)
                .create();
        dialog.show();
    }

}