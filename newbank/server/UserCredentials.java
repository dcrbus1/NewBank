package newbank.server;

import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.ArrayList;
import java.util.List;

public class UserCredentials {
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String SECRET_KEY = "ThisIsASecretKey";
    private static final String CREDENTIALS_DIRECTORY = "credentials/";

    public static void storeCredentials(String userId, String password) {
        try {
            File directory = new File(CREDENTIALS_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdir(); // Create the "credentials" directory if it doesn't exist
            }

            String userFileName = CREDENTIALS_DIRECTORY + userId + ".dat";

            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ENCRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedPassword = cipher.doFinal(password.getBytes());
            //save credentals in the userId.dat file
            FileOutputStream fos = new FileOutputStream(userFileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userId);
            oos.writeObject(encryptedPassword);
            oos.close();
            fos.close();

            System.out.println("User credentials stored successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Customer validateCredentials(String userId, String password) {
        try {
            String userFileName = CREDENTIALS_DIRECTORY + userId + ".dat";
               
            //validate credentals with the userId.dat file

            FileInputStream fis = new FileInputStream(userFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            String storedUserId = (String) ois.readObject();
            byte[] encryptedPassword = (byte[]) ois.readObject();
            ois.close();
            fis.close();
    
            if (!userId.equals(storedUserId)) {
                System.out.println("Invalid user ID.");
                return null;
            }
    
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ENCRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedPassword = cipher.doFinal(encryptedPassword);
    
            if (password.equals(new String(decryptedPassword))) {
                System.out.println("Login successful.");
                return Customer.loadCustomerData(userId);
            } else {
                System.out.println("Incorrect password.");
                return null;
            }
        } catch (FileNotFoundException e) {
            System.out.println("User not found.");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<String> getAllUsers() {
        List<String> users = new ArrayList<>();

        try {
            File credentialsDir = new File(CREDENTIALS_DIRECTORY);
            File[] files = credentialsDir.listFiles();

            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".dat")) {
                        String userId = fileName.substring(0, fileName.length() - 4); // Remove the ".dat" extension
                        users.add(userId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }
}
