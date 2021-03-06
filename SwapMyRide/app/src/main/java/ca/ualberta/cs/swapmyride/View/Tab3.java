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
package ca.ualberta.cs.swapmyride.View;

/**
 * This is the tab where a user can access their friends, their trades, and their
 * profile.
 * <p/>
 * Has toolbar at the top with search, add friend, and settings menu functionality!
 * <p/>
 * Created by Daniel on 2015-10-24.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.ualberta.cs.swapmyride.R;

/**
 * The view that displays the user tab
 */
public class Tab3 extends Fragment {

    TextView viewFriends;
    TextView pastTrades;
    TextView activeTrades;
    TextView editProfile;


    /**
     * OnCreate creates the list view items that can be clicked
     * to take a user to a friends list, trade lists, or to edit
     * their profile.
     *
     * Each click takes the user to a new activity
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab3, container, false);

        viewFriends = (TextView) v.findViewById(R.id.viewFriends);
        pastTrades = (TextView) v.findViewById(R.id.pastTrades);
        activeTrades = (TextView) v.findViewById(R.id.activeTrades);
        editProfile = (TextView) v.findViewById(R.id.editProfile);


        viewFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewFriendsActivity.class);
                startActivity(intent);
            }
        });

        pastTrades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewPastTradesActivity.class);
                startActivity(intent);
            }
        });

        activeTrades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewPendingTradesActivity.class);
                startActivity(intent);
            }
        });

        // TODO: Carson -> editProfile needs to actually edit profile.
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }
}