Pocket Dungeon

## CHANGES FROM SPRINT 1 ##
- The biggest piece of feedback we recieved was that our navigation felt very unintuitive so we revamped the whole app to use a navigation drawer layout, making navigating between screens much easier.

## IMPLEMENTED USER STORIES ##
- A new user can register for an account with an email and password
- A returning user can login using their email and password and have their account information saved
- Users can create new Dungeons & Dragons campaigns which other users can then join
- Players can search for campaigns by their unique campaign code and then join with one of their characters
- The creator of a campaign can send invites to other users to join thier campaign
- Players can create and save Dungeons & Dragons characters in the form of character sheets
- Players can then edit any information about their characters
- Members of a campaign can view the character sheets of any joined players
- The creator of a campaign can add notes pertaining to said campaign which all joined players can view
- Users can search for information pertaining to Dungeons & Dragons 5th edition
- Users can roll a variety of dice used in Dungeons & Dragons
- Users can log out of the app

## DEVICE STORAGE ##
- We store user login information in SharedPreferences for automatic sign in
- SharedPreferences also stores all campaignids, characterids, and userids


## TESTING TIPS ##

- The fastest and easiest way to test adding a character is to repeat typing a number and tab until all of the fields are filled. For example "9, tab, 9, tab..."

- Sample Testing accounts
	email: tester1@email.com
	password: password

- Sample Campaign Code to test joining a campaign: 1022

- During development, the app was tested on the Nexus 5X and the Pixel 2. All screens should display fine on devices of all sizes, but incase anyone runs in to elements being clipped off screen, those two devices are confirmed to work.
