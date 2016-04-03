package crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    protected final static byte[] salt = {0xB,0xA,0x5,0x7,0xC,0xD,0x2,0x8,0x7,0xF,0x6,0x9,0x5,0x4,0x3,0x2};
	
    protected Cipher cipher;
    
	protected void getKeyFromSpec (File keyFile) throws IOException, ClassNotFoundException
	{
        fis = new FileInputStream(keyFile);
        key = (SecretKey)(new ObjectInputStream(fis)).readObject();
        fis.close();
	}
}
