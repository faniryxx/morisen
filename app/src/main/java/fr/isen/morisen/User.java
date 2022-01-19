package fr.isen.morisen;

public class User {
    public String username;
    public String number;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String number) {
        this.username = username;
        this.number = number;
    }
}
