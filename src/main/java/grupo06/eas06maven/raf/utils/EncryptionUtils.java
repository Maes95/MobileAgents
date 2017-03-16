/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupo06.eas06maven.raf.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Pablo
 */
public class EncryptionUtils {
    
    
    /**
   * String to hold name of the encryption algorithm.
   */
  public static final String ALGORITHM_RSA = "RSA";
  public static final String ALGORITHM_AES = "AES";
  public static final String TRANSFORMATION = "AES";
  /**
   * String to hold the name of the private key file.
   */
  public static final String PRIVATE_KEY_FILE =  "src\\main\\java\\grupo06\\eas06maven"
                                                + File.separator
                                                + "raf" 
                                                + File.separator
                                                + "config"
                                                + File.separator
                                                + "private.key";

  /**
   * String to hold name of the public key file.
   */
  public static final String PUBLIC_KEY_FILE = "src\\main\\java\\grupo06\\eas06maven"
                                                + File.separator
                                                + "raf" 
                                                + File.separator
                                                + "config"
                                                + File.separator
                                                + "public.key";
  
  public static void generateKey() {
    try {
      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM_RSA);
      keyGen.initialize(1024);
      final KeyPair key = keyGen.generateKeyPair();

      File privateKeyFile = new File(PRIVATE_KEY_FILE);
      File publicKeyFile = new File(PUBLIC_KEY_FILE);

      // Create files to store public and private key
      if (privateKeyFile.getParentFile() != null) {
        privateKeyFile.getParentFile().mkdirs();
      }
      privateKeyFile.createNewFile();

      if (publicKeyFile.getParentFile() != null) {
        publicKeyFile.getParentFile().mkdirs();
      }
      publicKeyFile.createNewFile();

      // Saving the Public key in a file
      ObjectOutputStream publicKeyOS = new ObjectOutputStream(
          new FileOutputStream(publicKeyFile));
      publicKeyOS.writeObject(key.getPublic());
      publicKeyOS.close();

      // Saving the Private key in a file
      ObjectOutputStream privateKeyOS = new ObjectOutputStream(
          new FileOutputStream(privateKeyFile));
      privateKeyOS.writeObject(key.getPrivate());
      privateKeyOS.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * The method checks if the pair of public and private key has been generated.
   * 
   * @return flag indicating if the pair of keys were generated.
   */
  public static boolean areKeysPresent() {

    File privateKey = new File(PRIVATE_KEY_FILE);
    File publicKey = new File(PUBLIC_KEY_FILE);

    if (privateKey.exists() && publicKey.exists()) {
      return true;
    }
    return false;
  }

  /**
   * Encrypt the plain text using public key.
   * 
   * @param text
   *          : original plain text
   * @param key
   *          :The public key
   * @return Encrypted text
   * @throws java.lang.Exception
   */
  public static byte[] encrypt(byte[] data, PublicKey key) {
    byte[] cipherText = null;
    try {
      // get an RSA cipher object and print the provider
      final Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
      // encrypt the plain text using the public key
      cipher.init(Cipher.ENCRYPT_MODE, key);
        System.out.println("TAMAÑO: " + data.length);
      cipherText = cipher.doFinal(data);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return cipherText;
  }

  /**
   * Decrypt text using private key.
   * 
   * @param text
   *          :encrypted text
   * @param key
   *          :The private key
   * @return plain text
   * @throws java.lang.Exception
   */
  public static byte[] decrypt(byte[] data, PrivateKey key) {
    byte[] dectyptedText = null;
    try {
      // get an RSA cipher object and print the provider
      final Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);

      // decrypt the text using the private key
      cipher.init(Cipher.DECRYPT_MODE, key);
      dectyptedText = cipher.doFinal(data);

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return dectyptedText;
  }
  
  public static PublicKey getPublicKey (){
      PublicKey publicKey = null;
      try {
          ObjectInputStream inputStream = null;
          // Encrypt the string using the public key
          inputStream = new ObjectInputStream(new FileInputStream(EncryptionUtils.PUBLIC_KEY_FILE));
          publicKey = (PublicKey) inputStream.readObject();
      } catch (IOException ex) {
          Logger.getLogger(EncryptionUtils.class.getName()).log(Level.SEVERE, null, ex);
      } catch (ClassNotFoundException ex) {
          Logger.getLogger(EncryptionUtils.class.getName()).log(Level.SEVERE, null, ex);
      }
      return publicKey;
  }
  
  public static PrivateKey getPrivateKey (){
      PrivateKey privateKey = null;
      try {
          ObjectInputStream inputStream = null;
          // Encrypt the string using the public key
          inputStream = new ObjectInputStream(new FileInputStream(EncryptionUtils.PRIVATE_KEY_FILE));
          privateKey = (PrivateKey) inputStream.readObject();
      } catch (IOException ex) {
          Logger.getLogger(EncryptionUtils.class.getName()).log(Level.SEVERE, null, ex);
      } catch (ClassNotFoundException ex) {
          Logger.getLogger(EncryptionUtils.class.getName()).log(Level.SEVERE, null, ex);
      }
      return privateKey;
      
  }
  
  public static byte[] doCrypto(int cipherMode, String key, byte[] inputBytes){
        byte[] outputBytes = null;
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM_AES);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);
             
            outputBytes = cipher.doFinal(inputBytes);   
   
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException ex) {System.err.println(ex.getMessage());}
        return outputBytes;
    }
  
    
}
