/*
 * Copyright 2015 Adriano Marini, Carson McLean, Conner Dunn, Daniel Haberstock, Garry Bullock
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.ualberta.cs.swapmyride.Controller;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import ca.ualberta.cs.swapmyride.Misc.InvalidTradeException;
import ca.ualberta.cs.swapmyride.Misc.UserSingleton;
import ca.ualberta.cs.swapmyride.Model.Trade;
import ca.ualberta.cs.swapmyride.Model.TradeList;
import ca.ualberta.cs.swapmyride.Model.User;
import ca.ualberta.cs.swapmyride.Model.Vehicle;
import ca.ualberta.cs.swapmyride.View.FeedTradeActivity;

/**
 * Created by Garry on 2015-11-01.
 */
public class TradesController {

    Context context;
    NetworkDataManager dataManager;
    LocalDataManager dataManagerLocal;

    public TradesController(Context context) {
        this.context = context;
        dataManager = new NetworkDataManager();
        dataManagerLocal = new LocalDataManager(context);
    }

    public void initiateBorrow() {
        initiateTrade(true);
    }
    public void initiateTrade() {
        initiateTrade(false);
    }

    public void initiateTrade(Boolean isBorrowing) {
        Trade pendingTrade = UserSingleton.getCurrentTrade().copy();
        pendingTrade.setBorrowing(isBorrowing);

        User friend = dataManager.retrieveUser(pendingTrade.getBorrower());

        friend.addPendingTrade(pendingTrade);
        UserSingleton.getCurrentUser().addPendingTrade(pendingTrade);

        friend.getNotificationManager().notifyTrade(pendingTrade);
        UserSingleton.getCurrentUser().getNotificationManager().notifyTrade(pendingTrade);

        dataManager.saveUser(friend);
        dataManager.saveUser(UserSingleton.getCurrentUser());

        dataManagerLocal.saveUser(friend);
        dataManagerLocal.saveUser(UserSingleton.getCurrentUser());
    }

    public void deletePendingTrade(Trade trade) {
        //TODO MODIFY THIS TO NOT LOAD/SAVE SO OFTEN
        User borrower = dataManager.retrieveUser(trade.getBorrower());
        User owner = dataManager.retrieveUser(trade.getOwner());

        TradeList borrowerPendingTrades = borrower.getPendingTrades();
        borrowerPendingTrades.delete(trade.getUniqueID());
        borrower.setPendingTrades(borrowerPendingTrades);

        TradeList userPendingTrades = owner.getPendingTrades();
        userPendingTrades.delete(trade.getUniqueID());
        owner.setPendingTrades(userPendingTrades);

        owner.getNotificationManager().removeTrade(trade);
        borrower.getNotificationManager().removeTrade(trade);

        // make sure to save the right user back to the userSingleton!
        if(borrower.getUserName().equals(UserSingleton.getCurrentUser().getUserName())) {
            UserSingleton.addCurrentUser(borrower);
        } else {
            UserSingleton.addCurrentUser(owner);
        }

        dataManager.saveUser(borrower);
        dataManager.saveUser(owner);

        dataManagerLocal.saveUser(borrower);
        dataManagerLocal.saveUser(owner);
    }

    public void returnedPendingTrade(Trade trade){
        User borrower = dataManager.retrieveUser(trade.getBorrower());
        User owner = dataManager.retrieveUser(trade.getOwner());

        // remove from pendingList
        owner.getPendingTrades().delete(trade.getUniqueID());
        borrower.getPendingTrades().delete(trade.getUniqueID());

        // add to pastTradesList
        owner.addPastTrade(trade);
        borrower.addPastTrade(trade);

        // save userSingleton
        UserSingleton.addCurrentUser(owner);  // Only an owner should be able to click 'returned'

        // save users
        dataManager.saveUser(borrower);
        dataManager.saveUser(owner);
    }

    public void confirmPendingTrade(Trade trade) throws InvalidTradeException {
        // TODO finish working with these functions
        if(trade.getIsBorrowing()) {
            confirmBorrowingPendingTrade(trade);
        } else {
            confirmNotBorrowingPendingTrade(trade);
        }
    }

    public void confirmBorrowingPendingTrade(Trade trade) throws InvalidTradeException {
        // check that items are in inventory for both parties
        if (!(validTrade(trade))) {
            throw new InvalidTradeException("Trade is no longer valid");
        }

        // Starting the borrowing-trade logic
        User borrower = dataManager.retrieveUser(trade.getBorrower());
        User owner = dataManager.retrieveUser(trade.getOwner());

        owner.getPendingTrades().setAccepted(trade.getUniqueID(), true);
        borrower.getPendingTrades().setAccepted(trade.getUniqueID(), true);

        // save userSingleton
        UserSingleton.addCurrentUser(borrower);  // Only a borrower should be able to click 'confirm' on a trade

        dataManager.saveUser(borrower);
        dataManager.saveUser(owner);
    }

    public void confirmNotBorrowingPendingTrade(Trade trade) throws InvalidTradeException {
        User borrower = dataManager.retrieveUser(trade.getBorrower());
        User owner = dataManager.retrieveUser(trade.getOwner());

        trade.swapBelongsTo();

        // swap items between users
        owner.getInventory().getList().addAll(trade.getBorrowerItems());
        borrower.getInventory().deleteAll(trade.getBorrowerItems());

        borrower.getInventory().getList().addAll(trade.getOwnerItems());
        owner.getInventory().deleteAll(trade.getOwnerItems());

        // remove from pendingList
        owner.getPendingTrades().delete(trade.getUniqueID());
        borrower.getPendingTrades().delete(trade.getUniqueID());

        // add to pastTradesList
        owner.addPastTrade(trade);
        borrower.addPastTrade(trade);

        // save users
        dataManager.saveUser(borrower);
        dataManager.saveUser(owner);

        dataManagerLocal.saveUser(borrower);
        dataManagerLocal.saveUser(owner);

        // save userSingleton
        UserSingleton.addCurrentUser(borrower); // Only a borrower should be able to click 'confirm' on a trade
    }

    public void counterPendingTrade(Context context, Trade trade){

        // Saving done in deleteTrade
        deletePendingTrade(trade);

        Intent intent = new Intent(context, FeedTradeActivity.class);
        intent.putExtra("Username", trade.getOwner());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public TradeList getActiveTrades(){
        return UserSingleton.getCurrentUser().getPendingTrades();
    }

    public TradeList getPastTrades(){
        return UserSingleton.getCurrentUser().getPastTrades();
    }

    public boolean validTrade(Trade trade) {
        ArrayList<Vehicle> borrower = dataManager.retrieveUser(trade.getBorrower()).getInventory().getList();
        ArrayList<Vehicle> owner = dataManager.retrieveUser(trade.getOwner()).getInventory().getList();

        Boolean ownerBool = validVehicles(trade.getOwnerItems(), owner);
        Boolean borrowerBool = validVehicles(trade.getBorrowerItems(), borrower);

        return (ownerBool && borrowerBool);
    }

    public Boolean validVehicles(ArrayList<Vehicle> tradeVehicles, ArrayList<Vehicle> inventoryVehicles) {

        for (Vehicle tradeVehicle: tradeVehicles) {
            Boolean vehicleInInventory = false;
            for (Vehicle inventoryVehicle: inventoryVehicles) {
                //TODO Compare vehicles
                if (tradeVehicle.getUniqueID().isEqualID(inventoryVehicle.getUniqueID())) {
                    vehicleInInventory = true;
                    break;
                }

            }
            if (!(vehicleInInventory)) {
                return false;
            }
        }
        return true;
    }


}
