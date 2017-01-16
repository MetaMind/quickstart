package com.salesforce.ps;

import com.salesforce.ps.http.PredictRequest;
import com.salesforce.ps.representations.AccessToken;
import com.salesforce.ps.representations.PredictResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppMain {

  public static void main(String[] args) throws IOException {
    String email = args[0];
    String privateKeyPath = args[1];
    long durationInSeconds = 60 * 15;

    String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));

    // Create a AccessToken provider
    AccessTokenProvider tokenProvider = AccessTokenProvider
        .getProvider(email, privateKey, durationInSeconds);

    // Use this if you want the refresh the token automatically
    // Schedule Token refresher to refresh
    // AccessTokenRefresher.schedule(tokenProvider, 60 * 14);

    AccessToken accessToken = tokenProvider.getAccessToken();

    PredictRequest predictRequest = new PredictRequest(accessToken.getToken(),
        "GeneralImageClassifier",
        "http://dgicdplf3pvka.cloudfront.net/images/dogbreeds/large/Siberian-Husky.jpg");

    PredictResponse response = predictRequest.submit();
    System.out.println(response.getProbabilities());
  }
}
