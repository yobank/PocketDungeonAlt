package edu.tacoma.wa.pocketdungeonalt.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** A class for Campaign object, a campaign has ID, name, notes and player role. */
public class Campaign implements Serializable {
    private int campaignID;
    private String campaignName;
    private String campaignDescription;
    private String campaignNotes;
    private String campaignRole;

    public static final String ID = "campaignid";
    public static final String NAME = "campaignname";
    public static final String DESCRIPTION = "campaigndescription";
    public static final String NOTES = "campaignnotes";
    public static final String ROLE = "role";

    public Campaign(int campaignID, String campaignName, String campaignDescription, String campaignNotes, String campaignRole) {
        this.campaignID = campaignID;
        this.campaignName = campaignName;
        this.campaignDescription = campaignDescription;
        this.campaignNotes = campaignNotes;
        this.campaignRole = campaignRole;
    }

    public int getCampaignID() { return campaignID; }

    public void setCampaignID(int campaignID) { this.campaignID = campaignID; }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getGetCampaignDescription() {
        return campaignDescription;
    }

    public void setGetCampaignDescription(String campaignDescription) {
        this.campaignDescription = campaignDescription;
    }

    public String getGetCampaignNotes() {
        return campaignNotes;
    }

    public void setGetCampaignNotes(String campaignNotes) {
        this.campaignNotes = campaignNotes;
    }

    public String getCampaignRole() {
        return campaignRole;
    }

    public void setCampaignRole(String campaignRole) {
        this.campaignRole = campaignRole;
    }

    /** Construct a campaign list by parsing JsonObject. */
    public static List<Campaign> parseCampaignJson(String campaignJson) throws JSONException {
        List<Campaign> campaignList = new ArrayList<>();
        if (campaignJson != null) {
            JSONArray arr = new JSONArray(campaignJson);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Campaign campaign = new Campaign(obj.getInt(Campaign.ID),
                        obj.getString(Campaign.NAME), obj.getString(Campaign.DESCRIPTION),
                        obj.getString(Campaign.NOTES), "Player");
                campaignList.add(campaign);
            }
        }
        return campaignList;
    }

    /** Construct a campaign object by parsing JsonObject. */
    public static Campaign parseJoinCampaign(String campaignJson) throws JSONException {
        JSONArray arr = new JSONArray(campaignJson);
        JSONObject obj = arr.getJSONObject(0);

        Campaign campaign = new Campaign(obj.getInt(Campaign.ID),
                obj.getString(Campaign.NAME), obj.getString(Campaign.DESCRIPTION),
                obj.getString(Campaign.NOTES), "Player");
        return campaign;
    }




}