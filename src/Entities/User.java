package Entities;

public class User {
    private String username;
    private String password;
    private String role;
    private int id;
    private String email;
    private String name;

    public enum Role {
        ADMIN,
        MANAGER,
        USER;

        public static Role fromString(String text) {
            if (text != null) {
                for (Role b : Role.values()) {
                    if (text.equalsIgnoreCase(b.name())) {
                        return b;
                    }
                }
            }
            throw new IllegalArgumentException("No enum constant Role for text: " + text);
        }



    }



    public User(String username, String password, String role, int id, String email, String name) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public User(String username, String password, String role, String email, String name) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.name = name;
    }

    public User(){
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
