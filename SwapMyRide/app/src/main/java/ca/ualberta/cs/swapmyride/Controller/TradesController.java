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

import ca.ualberta.cs.swapmyride.Misc.UserSingleton;
import ca.ualberta.cs.swapmyride.Model.Trade;
import ca.ualberta.cs.swapmyride.Model.TradeList;
import ca.ualberta.cs.swapmyride.Model.User;

/**
 * Created by Garry on 2015-11-01.
 */
public class TradesController {

    Context context;
    DataManager dataManager;

    public TradesController(Context context) {
        this.context = context;
        dataManager = new DataManager(context);
    }

    public void removePendingTrades(Trade trade) {
        User borrower = dataManager.loadUser(trade.getBorrower());
        User owner = dataManager.loadUser(trade.getOwner());

        TradeList borrowerPendingTrades = borrower.getPendingTrades();
        borrowerPendingTrades.delete(trade.getUniqueID());
        borrower.setPendingTrades(borrowerPendingTrades);

        TradeList userPendingTrades = owner.getPendingTrades();
        userPendingTrades.delete(trade.getUniqueID());
        owner.setPendingTrades(userPendingTrades);

        dataManager.saveUser(borrower);
        dataManager.saveUser(owner);
    }

    public TradeList getActiveTrades(){
        return UserSingleton.getCurrentUser().getPendingTrades();
    }

    public TradeList getPastTrades(){
        return UserSingleton.getCurrentUser().getPastTrades();
    }


}
