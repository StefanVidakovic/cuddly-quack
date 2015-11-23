package ca.ualberta.cs.swapmyride;

import android.content.Context;
import android.graphics.Point;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.Display;
import android.view.WindowManager;

import org.junit.Before;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;


import ca.ualberta.cs.swapmyride.Controller.DataManager;
import ca.ualberta.cs.swapmyride.Misc.VehicleCategory;
import ca.ualberta.cs.swapmyride.Misc.VehicleQuality;
import ca.ualberta.cs.swapmyride.Model.User;
import ca.ualberta.cs.swapmyride.Model.Vehicle;
import ca.ualberta.cs.swapmyride.View.MainMenu;
/**
 * Created by adrianomarini on 2015-11-18.
 *
 * In this test, we will ensure that the main menu functions
 * correctly and that all of the intended functions are present
 * and operational
 *
 *
 */
public class MainMenuTest extends ActivityInstrumentationTestCase2{

    private MainMenu mainMenu;

    public MainMenuTest() { super(MainMenu.class); }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mainMenu = (MainMenu) getActivity();
    }

    /**
     * This test tests the UI functionality of clicking on an item in the
     * friends' inventory feed to verify that the proper view activity
     * opens and that the information that is opened is correct
     */

    public void testClickItem(){
        populateTestData();
        Context context = getInstrumentation().getContext();
        //Feed of items should be active
        //Tap on an item in the feed,

        //ViewVehicleActivity should be active

        //Assert information matches the prescribed info


        cleanUp();
    }

    public void testClickInventory(){
        populateTestData();
        Context context = getInstrumentation().getContext();

        //Feed of inventory should be active

        //tap on an item

        //Assert the view activity is active

        //Assert information matches

        //Try changing an item's attribute + save

        //Feed inventory should be active

        // tap same item

        //assert information matches changes


        cleanUp();
    }

    public void testViewFriends(){
        populateTestData();
        Context context = getInstrumentation().getContext();

        //Tab3 should be active, tap View Friends

        //ViewFriendsActivity should be active

        //Verify a friend exists

        //Tap that friend -- ViewFriendProfileActivity should be active

        //Assert matching information

        cleanUp();
    }

    public void testEditProfile(){
        populateTestData();
        Context context = getInstrumentation().getContext();

        //Tab3 should be active, tap Edit Profile

        //Verify information is active

        //change name + click save

        //re-enter -- verify information matches
        cleanUp();
    }

    public void testAddFriends(){
        populateTestData();
        //Click search button

        //Verify search activity starts

        //search for string

        //tap first result and verify information matches

        //tap the add button

        //verify activity returned

        cleanUp();
    }

    public void testAddItem(){
        populateTestData();

        cleanUp();
    }

    public void testSearch(){
        populateTestData();

        cleanUp();
    }

    /**
     * This method populates known data into hte current user area
     * to ensure that it can be tested and verified.
     */

    public void populateTestData(){
        Context context = getInstrumentation().getContext();
        DataManager dm = new DataManager(context);
        //Create 2 users and make them friends
        User main = new User();
        User friend = new User();

        main.setUserName("bob");
        main.setName("Bob");
        main.setUserAddress("123 Fake Street");
        main.setUserEmail("bob@bob.bob");

        friend.setUserName("jane");
        friend.setName("Jane");
        friend.setUserAddress("234 Fake Street");
        friend.setUserEmail("jane@jane.jane");

        main.addFriend("jane");

        //Give them 2 inventory items each
        Vehicle v1 = new Vehicle();
        Vehicle v2 = new Vehicle();
        Vehicle v3 = new Vehicle();
        Vehicle v4 = new Vehicle();

        v1.setName("Jeep");
        v1.setCategory(VehicleCategory.SUV);
        v1.setQuantity(1);
        v1.setQuality(VehicleQuality.SHOWROOM);

        v2.setName("Honda");
        v2.setCategory(VehicleCategory.SEDAN);
        v2.setQuality(VehicleQuality.RUSTBUCKET);
        v2.setQuantity(1);

        main.addItem(v1);
        main.addItem(v2);

        v3.setName("Toyota");
        v3.setCategory(VehicleCategory.SUV);
        v3.setQuantity(1);
        v3.setQuality(VehicleQuality.SHOWROOM);

        v4.setName("Mitsubishi");
        v4.setCategory(VehicleCategory.SEDAN);
        v4.setQuality(VehicleQuality.RUSTBUCKET);
        v4.setQuantity(1);

        friend.addItem(v3);
        friend.addItem(v4);

        dm.saveUser(main);
        dm.saveUser(friend);
    }

    /**
     * This method removes the test data that exists from the system
     * to ensure that it will not cause other issues by sticking around
     */
    public void cleanUp(){
        Context context = getInstrumentation().getContext();
        DataManager dm = new DataManager(context);
        //delete the two created users
        dm.deleteUser("bob");
        dm.deleteUser("jane");
    }

}