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
package ca.ualberta.cs.swapmyride;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class ViewVehicleActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView title;
    TextView quantity;
    TextView category;
    TextView quality;
    TextView comments;
    ImageView picture;

    Button delete;
    Button editVehicle;

    int position;
    DataManager dm = new DataManager(ViewVehicleActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewvehicle);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        title = (TextView) findViewById(R.id.title);
        quantity = (TextView) findViewById(R.id.quality);
        category = (TextView) findViewById(R.id.category);
        quality = (TextView) findViewById(R.id.quantity);
        comments = (TextView) findViewById(R.id.commentsHead);
        picture = (ImageView) findViewById(R.id.picture);


        delete = (Button) findViewById(R.id.delete);
        editVehicle = (Button) findViewById(R.id.edit);

        initOnClickListeners();

        Vehicle vehicle;
        position = getIntent().getIntExtra("vehiclePosition", 0);

        vehicle = UserSingleton.getCurrentUser().getInventory().getList().get(position);

        title.setText(vehicle.getName());
        quantity.setText(vehicle.getQuantity().toString());
        category.setText(vehicle.getCategory().getCategory());
        quality.setText(vehicle.getQuality().getQuality());
        comments.setText(vehicle.getComments());
        picture.setImageBitmap(vehicle.getPhoto().getImage());

    }

    public void initOnClickListeners(){
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog();
            }
        });
        editVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewVehicleActivity.this, AddInventoryActivity.class);
                intent.putExtra("vehiclePosition", position);
                startActivity(intent);
                finish();
            }
        });

    }

    public void deleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewVehicleActivity.this);
        builder.setMessage("Are you SURE you want to delete this vehicle? It is a permanent Action!");
        builder.setCancelable(false);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                User user = UserSingleton.getCurrentUser();
                Vehicle toDelete = user.getInventory().getList().get(position);
                user.getInventory().delete(toDelete);
                dm.saveUser(user);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
