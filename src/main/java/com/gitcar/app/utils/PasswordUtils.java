package com.gitcar.app.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    /**
     * Hashes a password using BCrypt.
     *
     * @param plainPassword The password to hash.
     * @return The BCrypt hash string.
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        // gensalt() uses default rounds (currently 10)
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /**
     * Checks if a plain password matches a stored BCrypt hash.
     *
     * @param plainPassword The password provided by the user.
     * @param hashedPassword The hash stored in the database.
     * @return true if the password matches the hash, false otherwise.
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || plainPassword.isEmpty() || hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // Handle cases where the stored hash might not be a valid BCrypt hash
            System.err.println("Error checking password: Invalid hash format? " + e.getMessage());
            return false;
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        String password = "admin";
        String hash = hashPassword(password);
        System.out.println("Password: " + password);
        System.out.println("Hashed: " + hash);
        System.out.println("Check 'admin': " + checkPassword("admin", hash));
        System.out.println("Check 'wrong': " + checkPassword("wrong", hash));

        // Example hash from schema (replace with real one)
        String placeholderHash = "$2a$10$abcdefghijklmnopqrstuv"; // This is NOT a valid hash
        System.out.println("Check 'admin' against placeholder: " + checkPassword("admin", placeholderHash));
    }
}

