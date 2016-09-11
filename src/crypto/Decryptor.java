package crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;

public class Decryptor extends Cryptor
{
	private ObjectInputStream ois;
	
	/**
	 * Open a file to encrypt a data with the key from keyFile.
	 */
	public Decryptor (File file, File keyFile) throws Exception
	{	
		getKeyFromSpec(keyFile);
		
		cipher = Cipher.getInstance(cipherAlgorithm);
	    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(salt));
	    ois = new ObjectInputStream(new CipherInputStream(new FileInputStream(file), cipher));
	}
	
	/**
	 * Read the encrypted object from the opened file.
	 */
	@SuppressWarnings("unchecked")
	public <T> T read() throws IOException, ClassNotFoundException
	{
		return (T)ois.readObject();
	}
}
