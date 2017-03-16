/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupo06.eas06maven.raf.utils;

import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import grupo06.eas06maven.raf.utils.EncryptionUtils.*;
import java.io.FileInputStream;
import static org.junit.Assert.*;

/**
 *
 * @author Pablo
 */
public class EncriptionableTest {
    
    public EncriptionableTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of generateKey method, of class Encriptionable.
     */
    @Test
    public void testGenerateKey() {
        System.out.println("generateKey");
        
        try {

            // Check if the pair of keys are present else generate those.
            if (!EncryptionUtils.areKeysPresent()) {
              // Method generates a pair of keys using the RSA algorithm and stores it
              // in their respective files
              EncryptionUtils.generateKey();
            }

            final String originalText = "Text to be encrypted ";
            ObjectInputStream inputStream = null;

            // Encrypt the string using the public key
            inputStream = new ObjectInputStream(new FileInputStream(EncryptionUtils.PUBLIC_KEY_FILE));
            final PublicKey publicKey = (PublicKey) inputStream.readObject();
            final byte[] cipherText = EncryptionUtils.encrypt(originalText.getBytes(), publicKey);

            // Decrypt the cipher text using the private key.
            inputStream = new ObjectInputStream(new FileInputStream(EncryptionUtils.PRIVATE_KEY_FILE));
            final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
            final String plainText = new String(EncryptionUtils.decrypt(cipherText, privateKey));

            // Printing the Original, Encrypted and Decrypted Text
            System.out.println("Original: " + originalText);
            System.out.println("Encrypted: " +cipherText.toString());
            System.out.println("Decrypted: " + plainText);
            
            assertEquals(originalText, plainText);

          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        
    }
