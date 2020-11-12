//package zippiesignatures;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class SignatureExample {
    private static final String SPEC = "secp256r1";
    private static final String ALGO = "SHA256withECDSA";
   
    // Run this once and generate a private key for future use for signing in your API 
    public static String createPrivateKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
        KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
        g.initialize(ecSpec, new SecureRandom());
        KeyPair keypair = g.generateKeyPair();
        PrivateKey privateKey = keypair.getPrivate();
        PublicKey publicKey = keypair.getPublic();
        System.out.println("// Publish this on your website");
        System.out.println("private static final String PUBLIC_KEY = \"" + Base64.getEncoder().encodeToString(publicKey.getEncoded()) + "\";");
        System.out.println("// Store this in your app or load it from a file");
        System.out.println("private static final String PRIVATE_KEY = \"" + Base64.getEncoder().encodeToString(privateKey.getEncoded()) + "\";");
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }
    
    // Sign JSON body with this and publish result as HTTP-header X-Signature-DER: <url for public key> <returned value>
    public static String signWithPrivateKey(String privateKey, String body) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
    	PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
    	KeyFactory keyFactory = KeyFactory.getInstance("EC");
    	PrivateKey privateKey2 = keyFactory.generatePrivate(spec);
    	
        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
        ecdsaSign.initSign(privateKey2);
        ecdsaSign.update(body.getBytes("UTF-8"));
        byte[] signature = ecdsaSign.sign();
        
        String sig = Base64.getEncoder().encodeToString(signature);
                
        return sig;
    }
  
    
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException, UnsupportedEncodingException, InvalidKeySpecException{
        String privateKey = createPrivateKey();
        System.out.println("");
        System.out.println("// Example HTTP header, replace URL with URL pointing to your public key on your website");
        System.out.println("X-Signature-DER: https://my.site/public-api.key " + signWithPrivateKey(privateKey, "{\"ok\":\"true\"}"));
    }
}
