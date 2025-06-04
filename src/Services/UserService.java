package Services;

import DAO.UserDAO;
import Entities.User;

import java.util.List;




    public class UserService {

        private final UserDAO userDAO;
        public UserService(UserDAO userDAO) {
            if (userDAO == null) {
                throw new IllegalArgumentException("UserDAO cannot be null");
            }
            this.userDAO = userDAO;
        }

        public boolean createUser(User user) {
            User existingUser = userDAO.findUserByUsername(user.getUsername());
            if (existingUser != null) {
                throw new IllegalArgumentException("Username '" + user.getUsername() + "' already exists.");
            }
            return userDAO.createUser(user);
        }

        public boolean updateUser(User user) {
            User existingUser = userDAO.findUserById(user.getId());
            if (existingUser == null) {
                throw new IllegalArgumentException("User with ID " + user.getId() + " not found for update.");
            }
            return userDAO.updateUser(user);
        }

        public boolean deleteUser(int userId) {

            User existingUser = userDAO.findUserById(userId);
            if (existingUser == null) {
                throw new IllegalArgumentException("User with ID " + userId + " not found for deletion.");
            }
            return userDAO.deleteUser(existingUser);
        }

        public User findUserByUsername(String username) {
            return userDAO.findUserByUsername(username);
        }

        public User findUserById(int userId) {
            return userDAO.findUserById(userId);
        }

        public List<User> getAllUsers(){
            return userDAO.getAllUsers();
        }

        public User loginUser(String username, String rawPassword) {
            User user = userDAO.findUserByUsername(username);
            if (user != null) {
                // Εδώ θα γίνει η σύγκριση του rawPassword με το αποθηκευμένο (hashed) password
                // boolean passwordsMatch = PasswordHasher.check(rawPassword, user.getPassword()); // Υποθετική μέθοδος
                // if (passwordsMatch) {
                //    return user;
                // }

                // ΠΡΟΣΩΡΙΝΑ, για το demo ΧΩΡΙΣ hashing, κάνουμε απλή σύγκριση (ΠΟΛΥ ΚΑΚΗ ΠΡΑΚΤΙΚΗ ΓΙΑ ΠΡΑΓΜΑΤΙΚΗ ΕΦΑΡΜΟΓΗ)
                if (user.getPassword().equals(rawPassword)) {
                    System.out.println("UserService: Login successful for user " + username + " (INSECURE PLAIN TEXT PASSWORD MATCH!)");
                    return user;
                } else {
                    System.out.println("UserService: Login failed for user " + username + " - password mismatch.");
                }
            } else {
                System.out.println("UserService: Login failed - user " + username + " not found.");
            }
            return null;
        }

    }


