package com.google.code.springcryptoutils.core.cipher.symmetric;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.InitializingBean;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

public class CiphererWithStaticKeyImpl implements CiphererWithStaticKey, InitializingBean {

    private String keyAlgorithm;
    private String cipherAlgorithm;
    private Mode mode;
    private IvParameterSpec initializationVectorSpec;
    private String key;
    private SecretKeySpec keySpec;

    public void setKeyAlgorithm(String keyAlgorithm) {
        this.keyAlgorithm = keyAlgorithm;
    }

    public void setCipherAlgorithm(String cipherAlgorithm) {
        this.cipherAlgorithm = cipherAlgorithm;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setInitializationVector(String initializationVector) {
        try {
            this.initializationVectorSpec = new IvParameterSpec(Base64.decodeBase64(initializationVector.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new SymmetricEncryptionException("UTF-8 is an unsupported encoding on this platform", e);
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    public byte[] encrypt(byte[] message) {
        try {
            final Cipher cipher = Cipher.getInstance(cipherAlgorithm);
            switch (mode) {
                case ENCRYPT:
                    cipher.init(Cipher.ENCRYPT_MODE, keySpec, initializationVectorSpec);
                    break;
                case DECRYPT:
                    cipher.init(Cipher.DECRYPT_MODE, keySpec, initializationVectorSpec);
                    break;
            }
            return cipher.doFinal(message);
        } catch (Exception e) {
            throw new SymmetricEncryptionException("error encrypting/decrypting message; mode=" + mode, e);
        }
    }

    public void afterPropertiesSet() throws Exception {
        this.keySpec = new SecretKeySpec(Base64.decodeBase64(key.getBytes("UTF-8")), keyAlgorithm);
    }

}