package edu.tacoma.wa.pocketdungeonalt.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// fat class

/** A class for Character object, a character has name, class, race, level, strength, dexterity,
 * constitution, intelligence, wisdom and charisma. */
public class Character implements Serializable {

    private int characterID;
    private String characterName;
    private String characterClass;
    private String characterRace;
    private int characterLevel;
    private String characterBackground;
    private String characterAlignment;
    private String characterInfo;
    private int experience;
    private int inspiration;
    private int proficiency;
    private int armorClass;
    private int initiative;
    private String speed;
    private int maxHP;
    private int currentHP;
    private String hitDice;
    private String skills;
    private int strength;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;
    private int perception;
    private String attacks;
    private String equipment;
    private String otherProficiencies;

    // fields for query database
    public static final String CHARACTERID = "characterid";
    public static final String CHARACTERNAME = "charactername";
    public static final String CHARACTERCLASS = "characterclass";
    public static final String CHARACTERRACE = "characterrace";
    public static final String CHARACTERLEVEL = "characterlevel";
    public static final String CHARACTERBACKGROUND = "characterbackground";
    public static final String CHARACTERALIGNMENT = "characteralignment";
    public static final String CHARACTERINFO = "characterinfo";
    public static final String EXPERIENCE = "experience";
    public static final String INSPIRATION = "inspiration";
    public static final String PROFICIENCY = "proficiency";
    public static final String ARMORCLASS = "armorclass";
    public static final String INITIATIVE = "initiative";
    public static final String SPEED = "speed";
    public static final String MAXHP = "maxhp";
    public static final String CURRENTHP = "currenthp";
    public static final String HITDICE = "hitdice";
    public static final String SKILLS = "skills";
    public static final String STRENGTH = "strength";
    public static final String DEXTERITY = "dexterity";
    public static final String CONSTITUTION = "constitution";
    public static final String INTELLIGENCE = "intelligence";
    public static final String WISDOM = "wisdom";
    public static final String CHARISMA = "charisma";
    public static final String PERCEPTION = "perception";
    public static final String ATTACKS = "attacks";
    public static final String EQUIPMENT = "equipment";
    public static final String OTHERPROFICIENCIES = "otherproficiencies";

    public Character(int id, String name, String charClass, String race, int level, String backGround, String align, String info,
                     int exp, int insp, int prof, int ac, int init, String spd, int mxHP, int curHP, String hd, String skls,
                     int str, int dex, int constI, int intl, int wis, int cha, int prcp, String atk, String equip, String otherProf) {
        characterID = id;
        characterName = name;
        characterClass = charClass;
        characterRace = race;
        characterLevel = level;
        characterBackground = backGround;
        characterAlignment = align;
        characterInfo = info;
        experience = exp;
        inspiration = insp;
        proficiency = prof;
        armorClass = ac;
        initiative = init;
        speed = spd;
        maxHP = mxHP;
        currentHP = curHP;
        hitDice = hd;
        skills = skls;
        strength = str;
        dexterity = dex;
        constitution = constI;
        intelligence = intl;
        wisdom = wis;
        charisma = cha;
        perception = prcp;
        attacks = atk;
        equipment = equip;
        otherProficiencies = otherProf;
    }




/** Should this be removed? */
//    public String toString() {
//        String temp = "Name: " + this.mCharacterName + "Level: " + this.mCharacterLevel;
//        return temp;
//    }

    /** method to construct a character list by parsing JsonObject. */
    public static List<Character> parseCharacterJSON(String characterJson) throws JSONException {
        List<Character> characterList = new ArrayList<>();
        if (characterJson != null) {
            System.out.println("helter");
            JSONArray arr = new JSONArray(characterJson);

            for (int i = 0; i < arr.length(); i++) {

                JSONObject obj = arr.getJSONObject(i);
                Character character = new Character(obj.getInt(Character.CHARACTERID),
                        obj.getString(Character.CHARACTERNAME),
                        obj.getString(Character.CHARACTERCLASS),
                        obj.getString(Character.CHARACTERRACE),
                        obj.getInt(Character.CHARACTERLEVEL),
                        obj.getString(Character.CHARACTERBACKGROUND),
                        obj.getString(Character.CHARACTERALIGNMENT),
                        obj.getString(Character.CHARACTERINFO),
                        obj.getInt(Character.EXPERIENCE),
                        obj.getInt(Character.INSPIRATION),
                        obj.getInt(Character.PROFICIENCY),
                        obj.getInt(Character.ARMORCLASS),
                        obj.getInt(Character.INITIATIVE),
                        obj.getString(Character.SPEED),
                        obj.getInt(Character.MAXHP),
                        obj.getInt(Character.CURRENTHP),
                        obj.getString(Character.HITDICE),
                        obj.getString(Character.SKILLS),
                        obj.getInt(Character.STRENGTH),
                        obj.getInt(Character.DEXTERITY),
                        obj.getInt(Character.CONSTITUTION),
                        obj.getInt(Character.INTELLIGENCE),
                        obj.getInt(Character.WISDOM),
                        obj.getInt(Character.CHARISMA),
                        obj.getInt(Character.PERCEPTION),
                        obj.getString(Character.ATTACKS),
                        obj.getString(Character.EQUIPMENT),
                        obj.getString(Character.OTHERPROFICIENCIES));

                characterList.add(character);
            }
        }
        else {
            System.out.println("skelter");
        }
        return characterList;
    }

    public static List<String> parseCharacterList(String characterJson) throws JSONException {
        List<String> characterList = new ArrayList<>();
        if (characterJson != null) {
            JSONArray arr = new JSONArray(characterJson);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                characterList.add(obj.getString(Character.CHARACTERNAME));
            }
        }
        return characterList;
    }

    public String getAttacks() { return attacks; }

    public void setAttacks(String attacks) { this.attacks = attacks; }

    public int getCharacterID() { return characterID; }

    public void setCharacterID(int characterID) { this.characterID = characterID; }

    public String getCharacterName() { return characterName; }

    public void setCharacterName(String characterName) { this.characterName = characterName; }

    public String getCharacterClass() { return characterClass; }

    public void setCharacterClass(String characterClass) { this.characterClass = characterClass; }

    public String getCharacterRace() { return characterRace; }

    public void setCharacterRace(String characterRace) { this.characterRace = characterRace; }

    public int getCharacterLevel() { return characterLevel; }

    public void setCharacterLevel(int characterLevel) { this.characterLevel = characterLevel; }

    public String getCharacterBackground() { return characterBackground; }

    public void setCharacterBackground(String characterBackground) { this.characterBackground = characterBackground; }

    public String getCharacterAlignment() { return characterAlignment; }

    public void setCharacterAlignment(String characterAlignment) { this.characterAlignment = characterAlignment; }

    public String getCharacterInfo() { return characterInfo; }

    public void setCharacterInfo(String characterInfo) { this.characterInfo = characterInfo; }

    public int getExperience() { return experience; }

    public void setExperience(int experience) { this.experience = experience; }

    public int getInspiration() { return inspiration; }

    public void setInspiration(int inspiration) { this.inspiration = inspiration; }

    public int getProficiency() { return proficiency; }

    public void setProficiency(int proficiency) { this.proficiency = proficiency; }

    public int getArmorClass() { return armorClass; }

    public void setArmorClass(int armorClass) { this.armorClass = armorClass; }

    public int getInitiative() { return initiative; }

    public void setInitiative(int initiative) { this.initiative = initiative; }

    public String getSpeed() { return speed; }

    public void setSpeed(String speed) { this.speed = speed; }

    public int getMaxHP() { return maxHP; }

    public void setMaxHP(int maxHP) { this.maxHP = maxHP; }

    public int getCurrentHP() { return currentHP; }

    public void setCurrentHP(int currentHP) { this.currentHP = currentHP; }

    public int getConstitution() { return constitution; }

    public void setConstitution(int constitution) { this.constitution = constitution; }

    public String getHitDice() { return hitDice; }

    public void setHitDice(String hitDice) { this.hitDice = hitDice; }

    public String getSkills() { return skills; }

    public void setSkills(String skills) { this.skills = skills; }

    public int getStrength() { return strength; }

    public void setStrength(int strength) { this.strength = strength; }

    public int getDexterity() { return dexterity; }

    public void setDexterity(int dexterity) { this.dexterity = dexterity; }

    public int getIntelligence() { return intelligence; }

    public void setIntelligence(int intelligence) { this.intelligence = intelligence; }

    public int getWisdom() { return wisdom; }

    public void setWisdom(int wisdom) { this.wisdom = wisdom; }

    public int getCharisma() { return charisma; }

    public void setCharisma(int charisma) { this.charisma = charisma; }

    public int getPerception() { return perception; }

    public void setPerception(int perception) { this.perception = perception; }

    public String getEquipment() { return equipment; }

    public void setEquipment(String equipment) { this.equipment = equipment; }

    public String getOtherProficiencies() { return otherProficiencies; }

    public void setOtherProficiencies(String otherProficiencies) { this.otherProficiencies = otherProficiencies; }
}










