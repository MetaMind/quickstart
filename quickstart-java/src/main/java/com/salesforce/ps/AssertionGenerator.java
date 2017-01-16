package com.salesforce.ps;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.UUID;
import jersey.repackaged.com.google.common.base.Throwables;
import org.apache.commons.codec.binary.Base64;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

/**
 * Generate a JWT assertion signed using the rsa private key
 */
public class AssertionGenerator {

  public static String generateJWTAssertion(String email, String privateKeyBase64,
      float expiryInSeconds) {
    PrivateKey privateKey = getPrivateKey(privateKeyBase64);
    final JwtClaims claims = new JwtClaims();
    claims.setSubject(email);
    claims.setAudience("https://api.metamind.io/v1/oauth2/token");
    claims.setExpirationTimeMinutesInTheFuture(expiryInSeconds / 60);
    claims.setIssuedAtToNow();

    // Generate the payload
    final JsonWebSignature jws = new JsonWebSignature();
    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
    jws.setPayload(claims.toJson());
    jws.setKeyIdHeaderValue(UUID.randomUUID().toString());

    // Sign using the private key
    jws.setKey(privateKey);
    try {
      return jws.getCompactSerialization();
    } catch (JoseException e) {
      return null;
    }
  }

  /**
   * Extracts private key (predictive_services.pem) contents
   */
  private static PrivateKey getPrivateKey(String privateKeyBase64) {
    String privKeyPEM = privateKeyBase64.replace("-----BEGIN RSA PRIVATE KEY-----\n", "");
    privKeyPEM = privKeyPEM.replace("\n-----END RSA PRIVATE KEY-----", "");

    // Base64 decode the data
    byte[] encoded = Base64.decodeBase64(privKeyPEM);

    try {
      DerInputStream derReader = new DerInputStream(encoded);
      DerValue[] seq = derReader.getSequence(0);

      if (seq.length < 9) {
        throw new GeneralSecurityException("Could not read private key");
      }

      // skip version seq[0];
      BigInteger modulus = seq[1].getBigInteger();
      BigInteger publicExp = seq[2].getBigInteger();
      BigInteger privateExp = seq[3].getBigInteger();
      BigInteger primeP = seq[4].getBigInteger();
      BigInteger primeQ = seq[5].getBigInteger();
      BigInteger expP = seq[6].getBigInteger();
      BigInteger expQ = seq[7].getBigInteger();
      BigInteger crtCoeff = seq[8].getBigInteger();

      RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp,
          primeP, primeQ, expP, expQ, crtCoeff);

      KeyFactory factory = KeyFactory.getInstance("RSA");
      return factory.generatePrivate(keySpec);
    } catch (IOException | GeneralSecurityException e) {
      Throwables.propagate(e);
    }
    return null;
  }
}
