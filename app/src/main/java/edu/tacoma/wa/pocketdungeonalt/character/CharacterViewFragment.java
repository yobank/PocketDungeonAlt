package edu.tacoma.wa.pocketdungeonalt.character;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import edu.tacoma.wa.pocketdungeonalt.R;
import edu.tacoma.wa.pocketdungeonalt.model.Character;

public class CharacterViewFragment extends Fragment {

    private TextView character_name;
    private TextView character_class;
    private TextView character_race;
    private TextView character_level;
    private Button button_background;
    private String background = "no background";
    private TextView alignment;
    private Button button_info;
    private String info = "no info";
    private TextView experience;
    private TextView inspiration;
    private TextView proficiency;
    private TextView armor_class;
    private TextView initiative;
    private TextView speed;
    private TextView maxHP;
    private TextView currentHP;
    private TextView hit_dice;
    private Button button_skills;
    private String skills = "no skills";
    private TextView strength;
    private TextView dexterity;
    private TextView constitution;
    private TextView intelligence;
    private TextView wisdom;
    private TextView charisma;
    private TextView perception;
    private Button button_attacks;
    private String attacks = "no attacks";
    private Button button_equipment;
    private String equipment = "no equipment";
    private Button button_other_proficiencies;
    private String other_proficiencies = "no other proficiencies";
    private Button add_button;
    private Button cancel_button;

    private SharedPreferences mSharedPreferences;
    private JSONObject mCharacterJSON;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_character_view, container, false);

        Character character = (Character) getArguments().getSerializable("CHARACTER");

        character_name = view.findViewById(R.id.character_name_input);
        character_name.setText(character.getCharacterName());
        character_class = view.findViewById(R.id.character_class_input);
        character_class.setText(character.getCharacterClass());
        character_race = view.findViewById(R.id.character_race_input);
        character_race.setText(character.getCharacterRace());
        character_level = view.findViewById(R.id.character_level_input);
        character_level.setText(String.valueOf(character.getCharacterLevel()));
        button_background = view.findViewById(R.id.background_button);
        background = character.getCharacterBackground();
        alignment  = view.findViewById(R.id.character_alignment_input);
        alignment.setText(character.getCharacterAlignment());
        button_info = view.findViewById(R.id.info_button);
        info = character.getCharacterInfo();
        experience = view.findViewById(R.id.character_experience_input);
        experience.setText(String.valueOf(character.getExperience()));
        inspiration = view.findViewById(R.id.character_inspiration_input);
        inspiration.setText(String.valueOf(character.getInspiration()));
        proficiency = view.findViewById(R.id.proficiency_input);
        proficiency.setText(String.valueOf(character.getProficiency()));
        armor_class = view.findViewById(R.id.ac_input);
        armor_class.setText(String.valueOf(character.getArmorClass()));
        initiative = view.findViewById(R.id.character_initiative_input);
        initiative.setText(String.valueOf(character.getInitiative()));
        speed = view.findViewById(R.id.speed_input);
        speed.setText(character.getSpeed());
        maxHP = view.findViewById(R.id.max_hp_input);
        maxHP.setText(String.valueOf(character.getMaxHP()));
        currentHP = view.findViewById(R.id.current_hp_input);
        currentHP.setText(String.valueOf(character.getCurrentHP()));
        hit_dice = view.findViewById(R.id.hit_dice_input);
        hit_dice.setText(character.getHitDice());
        button_skills = view.findViewById(R.id.skills_button);
        skills = character.getSkills();
        strength = view.findViewById(R.id.str_input);
        strength.setText(String.valueOf(character.getStrength()));
        dexterity = view.findViewById(R.id.dex_input);
        dexterity.setText(String.valueOf(character.getDexterity()));
        constitution  = view.findViewById(R.id.con_input);
        constitution.setText(String.valueOf(character.getConstitution()));
        intelligence = view.findViewById(R.id.int_input);
        intelligence.setText(String.valueOf(character.getIntelligence()));
        wisdom = view.findViewById(R.id.wis_input);
        wisdom.setText(String.valueOf(character.getWisdom()));
        charisma = view.findViewById(R.id.cha_input);
        charisma.setText(String.valueOf(character.getCharisma()));
        perception = view.findViewById(R.id.character_perception_input);
        perception.setText(String.valueOf(character.getPerception()));
        button_attacks = view.findViewById(R.id.attacks_button);
        attacks = character.getAttacks();
        button_equipment = view.findViewById(R.id.equipment_button);
        equipment = character.getEquipment();
        button_other_proficiencies = view.findViewById(R.id.otherProf_button);
        other_proficiencies = character.getOtherProficiencies();

        TextView strMod = view.findViewById(R.id.str_mod);
        TextView dexMod = view.findViewById(R.id.dex_mod);
        TextView conMod = view.findViewById(R.id.con_mod);
        TextView intMod = view.findViewById(R.id.int_mod);
        TextView wisMod = view.findViewById(R.id.wis_mod);
        TextView chaMod = view.findViewById(R.id.cha_mod);

        strMod.setText(calcMod(Integer.valueOf(strength.getText().toString())));
        dexMod.setText(calcMod(Integer.valueOf(dexterity.getText().toString())));
        conMod.setText(calcMod(Integer.valueOf(constitution.getText().toString())));
        intMod.setText(calcMod(Integer.valueOf(intelligence.getText().toString())));
        wisMod.setText(calcMod(Integer.valueOf(wisdom.getText().toString())));
        chaMod.setText(calcMod(Integer.valueOf(charisma.getText().toString())));

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

    private String calcMod(double val) {
        double vall = (val - 10) / 2;
        if (vall < 0) {
            System.out.println("1: " + (val - 10) / 2);
            val = Math.floor(vall);
            System.out.println("2: " + val);
        }
        else {val = Math.ceil(val);}
        return String.valueOf((int)val);
    }

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

    // can improve this later by changing from text entry to list of avalible skills
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