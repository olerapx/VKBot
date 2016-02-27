package api.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

public class Decryptor extends Cryptor
{
	public Decryptor() throws Exception
	{
        fis = null;
        key = null;
        cipher = Cipher.getInstance(algorithm);
	}
	
	/**
	 * Decrypts the data from file with a key from keyFile.
	 * @param file
	 * @param keyFile
	 * @return Decrypted data.
	 * @throws Exception
	 */
	public String decrypt (File file, File keyFile) throws Exception
	{
		getKeyFromSpec(keyFile);
		
        cipher.init(Cipher.DECRYPT_MODE, key);
        ObjectInputStream ois = new ObjectInputStream(new CipherInputStream(new FileInputStream(file), cipher));
        String output = (String) ois.readObject();
        ois.close();
        
        return output;
	}
}
