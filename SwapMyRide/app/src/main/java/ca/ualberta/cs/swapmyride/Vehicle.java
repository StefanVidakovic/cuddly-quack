package ca.ualberta.cs.swapmyride;

import android.graphics.Picture;
import android.provider.ContactsContract;

public class Vehicle {
    private Photo photo;
    private String name;
    private Integer quantity;
    private String comments;
    private VehicleCategory category;
    private VehicleQuality quality;
    private boolean isPublic;

    public Vehicle(){
        //add default constructor for photo
        name = "";
        quantity = 0;
        comments = "";
        category = VehicleCategory.NONE;
        quality = VehicleQuality.NONE;
        isPublic = true;
    }


    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public VehicleCategory getCategory() {
        return category;
    }

    public void setCategory(VehicleCategory category) {
        this.category = category;
    }

    public VehicleQuality getQuality() {
        return quality;
    }

    public void setQuality(VehicleQuality quality) {
        this.quality = quality;
    }


    public boolean getPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void setPhoto(Photo photo){
        this.photo = photo;
    }

    public Photo getPhoto(){
        return this.photo;
    }

    public void deletePhoto(){
        this.photo = null;
    }

}
