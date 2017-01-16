package com.salesforce.ps;

import com.salesforce.ps.http.AccessTokenRequest;
import com.salesforce.ps.representations.AccessToken;
import java.io.IOException;

/**
 * Generates an OAuth token
 */
public class AccessTokenProvider {

  private AccessToken accessToken;
  private final String email;
  private final String privateKey;
  private final long durationInSeconds;

  public static AccessTokenProvider getProvider(String email, String privateKey,
      long durationInSeconds) {
    return new AccessTokenProvider(email, privateKey, durationInSeconds);
  }

  private AccessTokenProvider(String email, String privateKey, long durationInSeconds) {
    this.email = email;
    this.privateKey = privateKey;
    this.durationInSeconds = durationInSeconds;
    refreshToken();
  }

  public AccessToken getAccessToken() {
    return accessToken;
  }

  public void refreshToken() {
    String assertion = AssertionGenerator
        .generateJWTAssertion(email, privateKey, durationInSeconds);
    AccessTokenRequest tokenRequest = new AccessTokenRequest(assertion);
    try {
      accessToken = tokenRequest.submit();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
