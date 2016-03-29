package crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * Base class for data cryptography.
 */
public abstract class Cryptor 
{ 
	protected String cipherAlgorithm = "AES/CTR/NoPadding";
    protected String keyAlgorithm = "AES";
    protected FileInputStream fis;
	
    protected SecretKey key;
    protected final static byte[] salt = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	
    protected Cipher cipher;
    
	protected void getKeyFromSpec (File keyFile) throws Exception
	{
        fis = new FileInputStream(keyFile);

        key = (SecretKey)(new ObjectInputStream(fis)).readObject();
        fis.close();
	}
}
