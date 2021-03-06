package com.google.code.springcryptoutils.core.signature;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class Base64EncodedSignerAndVerifierImplTest {

    @Autowired
    private Base64EncodedSigner signer;

    @Autowired
    private Base64EncodedVerifier verifier;

    private String message;

    @Before
    public void setup() {
        message = "this is a top-secret message";
        assertNotNull(signer);
        assertNotNull(verifier);
    }

    @Test
    public void testSignAndVerify() {
        String signature = signer.sign(message);
        assertNotNull(signature);
        assertTrue(verifier.verify(message, signature));
    }

    @Test
    public void testSignAndVerifyInALoop() {
        for (int i = 0; i < 100; i++) {
            String message = UUID.randomUUID().toString();
            String signature = signer.sign(message);
            assertNotNull(signature);
            assertTrue(verifier.verify(message, signature));
        }
    }

    @Test
    public void testVerifyWithGarbageSignatureFails() throws UnsupportedEncodingException {
        assertFalse(verifier.verify(message, "garbage"));
    }

    @Test
    public void testVerifyWithTamperedMessageFails() throws UnsupportedEncodingException {
        String signature = signer.sign(message);
        assertNotNull(signature);
        assertFalse(verifier.verify(message + " no more", signature));
    }

}