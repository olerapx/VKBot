package client;

import java.io.File;
import java.io.FileInputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public abstract class Cryptor 
{
    protected String algorithm = "DESede";  
    protected FileInputStream fis;
	
    protected SecretKey key;
	
    protected Cipher cipher;
    
	protected void getKeyFromSpec (File keyFile) throws Exception
	{
        fis = new FileInputStream(keyFile);
        
        byte[] keySpecBytes = new byte[fis.available()];
        fis.read(keySpecBytes);
        
        SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
        DESedeKeySpec keyspec = new DESedeKeySpec(keySpecBytes);
        key = skf.generateSecret(keyspec);
        
        fis.close();
	}
}
