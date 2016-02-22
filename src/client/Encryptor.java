package client;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class Encryptor 
{
	public Encryptor() throws Exception
	{
		FileOutputStream fos = null;
		String keyfile = "key.key";
        String algorithm = "DESede";       	
        KeyGenerator kg = KeyGenerator.getInstance(algorithm);
        SecretKey key = kg.generateKey();
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        
       ObjectOutputStream oos = new ObjectOutputStream(new CipherOutputStream(new FileOutputStream("Secret.file"), cipher));
        oos.writeObject("YOUR PASS IS MINE");
        fos = new FileOutputStream(keyfile);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
        DESedeKeySpec keyspec = (DESedeKeySpec) skf.getKeySpec(key, DESedeKeySpec.class);
        fos.write(keyspec.getKey());
        fos.close();
        oos.close();
	}
}
