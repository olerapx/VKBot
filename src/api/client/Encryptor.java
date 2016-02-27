package api.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class Encryptor extends Cryptor
{    
	private FileOutputStream fos;
	
	public Encryptor() throws Exception
	{
		fos = null;
		fis = null;
		key = null;
	    cipher = Cipher.getInstance(algorithm);
	}
	
	/**
	 * Encrypts the data to outputFile with an existing key from keyFile.
	 * @param data
	 * @param outputFile
	 * @param keyFile
	 * @throws Exception
	 */
	public void encrypt (String data, File outputFile, File keyFile) throws Exception
	{
		getKeyFromSpec (keyFile);
        
        encrypt(data, outputFile);
	}
		
	/**
	 * Encrypts the data to outputFile with a new key and writes it to newKeyFilePath.
	 * @param data
	 * @param outputFile
	 * @param newKeyFilePath
	 * @throws Exception
	 */
	public void encrypt (String data, File outputFile, String newKeyFilePath) throws Exception
	{
		KeyGenerator kg = KeyGenerator.getInstance(algorithm);
        key = kg.generateKey();
        
        encrypt (data, outputFile);
        
        fos = new FileOutputStream(newKeyFilePath);
        
        SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
        DESedeKeySpec keyspec = (DESedeKeySpec) skf.getKeySpec(key, DESedeKeySpec.class);
        fos.write(keyspec.getKey());
        fos.close();
	}
	
	private void encrypt (String data, File outputFile) throws Exception
	{
        cipher.init(Cipher.ENCRYPT_MODE, key);
        
        ObjectOutputStream oos = new ObjectOutputStream(new CipherOutputStream(new FileOutputStream(outputFile), cipher));
        oos.writeObject(data);

        oos.close();
	}
}
