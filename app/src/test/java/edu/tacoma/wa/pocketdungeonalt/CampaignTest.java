package edu.tacoma.wa.pocketdungeonalt;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;


import edu.tacoma.wa.pocketdungeonalt.model.Campaign;

import static org.junit.Assert.*;

public class CampaignTest {

    Random random;

    @Before
    public void initialize() {
        random = new Random();
    }

    // Test constructor

    @Test
    public void testCampaignConstructor() {
        int randId = random.nextInt(100) + 1000;
        int randCreatordId = random.nextInt(100) + 1;
        assertNotNull(new Campaign(randId, "A Dying Race", "The party fights for their lives in a hostile land.", "Two characters have died so far", randCreatordId));
    }

    // Test getters
    @Test
    public void testCampaignGetCampaignID() {
        int randId = random.nextInt(100) + 1000;
        int randCreatordId = random.nextInt(100) + 1;
        Campaign campaign = new Campaign(randId, "A Dying Race", "The party fights for their lives in a hostile land.", "Two members have died. Low on food", randCreatordId);
        assertEquals(randId, campaign.getCampaignID());
    }

    @Test
    public void testCampaignGetCampaignName() {
        int randId = random.nextInt(100) + 1000;
        int randCreatordId = random.nextInt(100) + 1;
        String campaignName = "Tales of Lordran";
        Campaign campaign = new Campaign(randId, campaignName, "A fight against demons and the undead.", "Attacked by two gargoyles.", randCreatordId);
        assertEquals(campaignName, campaign.getCampaignName());
    }

    @Test
    public void testCampaignGetCampaignDescription() {
        int randId = random.nextInt(100) + 1000;
        int randCreatordId = random.nextInt(100) + 1;
        String campaignDescription = "Mephisto is out for revenge";
        Campaign campaign = new Campaign(randId, "A Dying Race", campaignDescription, "Two members have died. Low on food", randCreatordId);
        assertEquals(campaignDescription, campaign.getGetCampaignDescription());
    }

    @Test
    public void testCampaignGetCampaignNotes() {
        int randId = random.nextInt(100) + 1000;
        int randCreatordId = random.nextInt(100) + 1;
        String campaignNotes = "We assaulted Strahd's castle.";
        Campaign campaign = new Campaign(randId, "A Dying Race", "An army of demons descends upon the land", campaignNotes, randCreatordId);
        assertEquals(campaignNotes, campaign.getGetCampaignNotes());
    }

    @Test
    public void testCampaignGetCreatorID() {
        int randId = random.nextInt(100) + 1000;
        int randCreatordId = random.nextInt(100) + 1;
        Campaign campaign = new Campaign(randId, "A Dying Race", "The party fights for their lives in a hostile land.", "Two characters have died so far", randCreatordId);
        assertEquals(randCreatordId, campaign.getCreatorID());
    }


    // Test setters
    @Test
    public void testCampaignSetID() {
        int beforeId = 130;
        int newId = 240;
        int randCreatordId = random.nextInt(100) + 1000;
        Campaign campaign = new Campaign(beforeId, "A Dying Race", "The party fights for their lives in a hostile land.", "Two members have died. Low on food", randCreatordId);
        campaign.setCampaignID(newId);
        assertEquals(newId, campaign.getCampaignID());
    }

    @Test
    public void testCampaignSetCampaignName() {
        int randId = random.nextInt(100) + 1000;
        int randCreatordId = random.nextInt(100) + 1;
        String beforeCampaignName = "Romance of the Three Kingdoms";
        String newCampaignName = "Out of the Abyss";
        Campaign campaign = new Campaign(randId, beforeCampaignName, "A fight against demons and the undead.", "Attacked by two gargoyles.", randCreatordId);
        campaign.setCampaignName(newCampaignName);
        assertEquals(newCampaignName, campaign.getCampaignName());
    }

    @Test
    public void testCampaignSetCampaignDescription() {
        int randId = random.nextInt(100) + 1000;
        int randCreatordId = random.nextInt(100) + 1;
        String beforeCampaignDescription = "Mephisto is out for revenge";
        String newCampaignDescription = "An army of demons descends upon the land.";
        Campaign campaign = new Campaign(randId, "A Dying Race", beforeCampaignDescription, "Two members have died. Low on food", randCreatordId);
        campaign.setGetCampaignDescription(newCampaignDescription);
        assertEquals(newCampaignDescription, campaign.getGetCampaignDescription());
    }

    @Test
    public void testCampaignSetCampaignNotes() {
        int randId = random.nextInt(100) + 1000;
        int randCreatordId = random.nextInt(100) + 1;
        String beforeCampaignNotes = "We assaulted Strahd's castle.";
        String newCampaignNotes = "Our party has come to its end. There are too many of them";
        Campaign campaign = new Campaign(randId, "A Dying Race", "An army of demons descends upon the land", beforeCampaignNotes, randCreatordId);
        campaign.setGetCampaignNotes(newCampaignNotes);
        assertEquals(newCampaignNotes, campaign.getGetCampaignNotes());
    }
    /*
    @Test
    public void testCampaignParseJSONCampaign() {
        String campaignJson = "[{\"campaignname\":\"name1\",\"campaigndescription\":\"desc1\",\"campaignnotes\":\"sample notes1\",\"memberid\":10}," +
                "{\"campaignname\":\"name\",\"campaigndescription\":\"desc\",\"campaignnotes\":\"sample notes\",\"memberid\":12}," +
                "{\"campaignname\":\"name2\",\"campaigndescription\":\"desc2\",\"campaignnotes\":\"sample notes2\",\"memberid\":11}]";
        try {
            List<Campaign> campaignArray = parseCampaignJson(campaignJson);
            assertEquals(campaignArray.size(), 3);
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Created an JSON Error Exception");
        }

    }
    */

    @Test
    public void testParseJsonCampaign() {
        String json = "[{\"campaignid\":1000, \"campaignname\":\"name\",\"campaigndescription\":\"desc\",\"campaignnotes\":\"sample notes\",\"memberid\":12}]";
        try {
            Campaign campaign = Campaign.parseJoinCampaign(json);
            assertNotNull(campaign);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
