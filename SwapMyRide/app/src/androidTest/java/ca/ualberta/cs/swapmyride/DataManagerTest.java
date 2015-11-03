package ca.ualberta.cs.swapmyride;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

/**
 * Created by Garry on 2015-11-02.
 */
public class DataManagerTest extends ActivityInstrumentationTestCase2 {
    public DataManagerTest(){super(MainMenu.class);}

    public void testSaveUser(){
        DataManager dataManager = new DataManager(getActivity());
        User user = new User();
        user.setName("Garry");
        user.setUserAddress("4465");
        user.setUserName("gbullock");
        user.setUserEmail("gbullock@ualbert.ca");
        Log.i("FilePath", getActivity().getBaseContext().getFileStreamPath(user.getUserName()).toString());
        UserSingleton.addUser(user);
        dataManager.saveUser(user);

        User loadTo = dataManager.loadUser("gbullock");

        assertTrue(loadTo.getName().equals(user.getName()));
        assertTrue(loadTo.getUserEmail().equals(user.getUserEmail()));
        assertTrue(loadTo.getUserAddress().equals(user.getUserAddress()));
        assertTrue(loadTo.getUserName().equals(user.getUserName()));

        dataManager.deleteUser("gbullock");
    }

    public void testDeleteUser(){
        Log.i("WERE Here","RIGHT HERE");
        DataManager dm = new DataManager(getActivity());
        User user = new User();
        user.setName("Garry");
        user.setUserAddress("4465");
        user.setUserName("gbullock");
        user.setUserEmail("gbullock@ualbert.ca");

        dm.saveUser(user);

        //check that the file exists
        assertTrue(getActivity().getBaseContext().getFileStreamPath(user.getUserName()).exists());

        dm.deleteUser("gbullock");

        //check that the file no longer exists
        assertFalse(getActivity().getBaseContext().getFileStreamPath(user.getUserName()).exists());
    }

    public void testSaveUserWithVehicle(){
        DataManager dataManager = new DataManager(getActivity());

        User user = new User();
        user.setName("Garry");
        user.setUserAddress("4465");
        user.setUserName("gbullock");
        user.setUserEmail("gbullock@ualbert.ca");
        Log.i("FilePath", getActivity().getBaseContext().getFileStreamPath(user.getUserName()).toString());

        //ensure the file does not previously exist
        assertFalse(getActivity().getBaseContext().getFileStreamPath(user.getUserName()).exists());

        dataManager.saveUser(user);

        //check the file exists
        assertTrue(getActivity().getBaseContext().getFileStreamPath(user.getUserName()).exists());

        User loadTo = dataManager.loadUser("gbullock");

        assertTrue(loadTo.getName().equals(user.getName()));
        assertTrue(loadTo.getUserEmail().equals(user.getUserEmail()));
        assertTrue(loadTo.getUserAddress().equals(user.getUserAddress()));
        assertTrue(loadTo.getUserName().equals(user.getUserName()));

        Vehicle car = new Vehicle();
        car.setName("2008 Cadillac");
        car.setCategory(VehicleCategory.SEDAN);
        car.setQuality(VehicleQuality.OKAY);
        car.setQuantity(1);
        car.setPublic(true);
        car.setComments("These are some comments yep yep");
        user.addItem(car);

        dataManager.saveUser(user);

        loadTo = dataManager.loadUser("gbullock");

        //check that the list exists
        assertTrue(loadTo.getInventory().size() == 1);
        //assert no friends have magically appeared
        assertTrue(loadTo.getFriends().size() == 0);
        //assert no random tradelist appeared
        assertTrue(loadTo.getPastTrades().getSize() == 0);
        assertTrue(loadTo.getPendingTrades().getSize() == 0);
        //check the given car is the same as the car we gave it
        Vehicle newCar = loadTo.getInventory().getList().get(0);

        assertTrue(newCar.getName().equals(car.getName()));
        assertTrue(newCar.getCategory().equals(car.getCategory()));
        assertTrue(newCar.getQuality().equals(car.getQuality()));
        assertTrue(newCar.getQuantity().equals(car.getQuantity()));
        assertTrue(newCar.getPublic() == car.getPublic());
        assertTrue(newCar.getComments().equals(car.getComments()));

        dataManager.deleteUser("gbullock");

        assertFalse(getActivity().getBaseContext().getFileStreamPath(user.getUserName()).exists());

    }

    public void testSearchUser(){
        DataManager dataManager = new DataManager(getActivity());

        User user = new User();
        user.setName("Garry");
        user.setUserAddress("4465");
        user.setUserName("gbullock");
        user.setUserEmail("gbullock@ualbert.ca");
        Log.i("FilePath", getActivity().getBaseContext().getFileStreamPath(user.getUserName()).toString());

        dataManager.deleteUser(user.getUserName());
        //ensure the file does not previously exist
        assertFalse(getActivity().getBaseContext().getFileStreamPath(user.getUserName()).exists());

        //assert that it does not find any users
        assertFalse(dataManager.searchUser(user.getUserName()));

        dataManager.saveUser(user);

        //ensure the file now exists
        assertTrue(getActivity().getBaseContext().getFileStreamPath(user.getUserName()).exists());

        assertTrue(dataManager.searchUser(user.getUserName()));

        dataManager.deleteUser(user.getUserName());

        //ensure the file no longer exists
        assertFalse(getActivity().getBaseContext().getFileStreamPath(user.getUserName()).exists());;
    }
}