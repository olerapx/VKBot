package client;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class Decryptor
{
	public Decryptor() throws Exception
	{
        FileInputStream fis = null;
        String keyfile = "key.key";
        String algorithm = "DESede";
        fis = new FileInputStream(keyfile);
        byte[] keyspecbytes = new byte[fis.available()];
        fis.read(keyspecbytes);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
        DESedeKeySpec keyspec = new DESedeKeySpec(keyspecbytes);
        SecretKey key = skf.generateSecret(keyspec);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        ObjectInputStream ois = new ObjectInputStream(new CipherInputStream(new FileInputStream("Secret.file"), cipher));
        String secret = (String) ois.readObject();
        System.out.println(secret);
        fis.close();
        ois.close();
	}
}
