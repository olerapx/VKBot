package crypto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;

public class Encryptor extends Cryptor
{    
	private FileOutputStream fos;
	private ObjectOutputStream oos;
	
	/**
	 * Open an outputFile to encrypt a data with the key in keyFile.
	 */
	public Encryptor(File outputFile, File keyFile) throws Exception
	{
		getKeyFromSpec (keyFile);		
		construct (outputFile);
	}
	
	/**
	 * Open an outputFile to encrypt a data with a new key and write the key to newKeyFilePath.
	 */
	public Encryptor (File outputFile, String newKeyFilePath) throws Exception
	{
		KeyGenerator kg = KeyGenerator.getInstance(keyAlgorithm);
        key = kg.generateKey();
        
        fos = new FileOutputStream(newKeyFilePath);

        (new ObjectOutputStream(fos)).writeObject(key);
        fos.close();
        
        construct (outputFile);
	}
	
	private void construct (File outputFile) throws Exception
	{
	    cipher = Cipher.getInstance(cipherAlgorithm);	    
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(salt));
        
        oos = new ObjectOutputStream(new CipherOutputStream(new FileOutputStream(outputFile), cipher));
	}
	
	/**
	 * Write an encrypted object to opened file.
	 */
	public void write (Object data)
	{
        try
        {
			oos.writeObject(data);
		} catch (IOException e) 
        {
			
		}
	}
}
