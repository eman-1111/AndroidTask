package ides.link.androidtask.models;

import com.google.gson.annotations.Expose;

/**
 * Created by Eman on 9/28/2017.
 */

public class UserResult {
    @Expose
    private String success;

    @Expose
    private int Id;

    @Expose
    private String UserName;

    @Expose
    private String Image;

    @Expose
    private String CustomerId;

    public String getSuccess() {
        return success;
    }

    public int getId() {
        return Id;
    }

    public String getUserName() {
        return UserName;
    }

    public String getImage() {
        return Image;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }
}
