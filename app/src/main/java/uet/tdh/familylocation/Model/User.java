package uet.tdh.familylocation.Model;

/**
 * Created by NGocDuc on 15/08/2016.
 */
public class User {
    private String email;
    private String name;

    // vi tri
    private float latitude;
    private float longitude;

    public User() {
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
        latitude = 0;
        longitude = 0;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
