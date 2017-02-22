package com.salesforce.ps;

import com.salesforce.ps.http.PredictRequest;
import com.salesforce.ps.representations.AccessToken;
import com.salesforce.ps.representations.PredictResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

public class AppMain {

  public static void main(String[] args) throws Exception {

    // For Heroku users
    Optional<String> accountIdOpt = Optional
        .ofNullable(System.getenv("EINSTEIN_VISION_ACCOUNT_ID"));
    Optional<String> privateKeyContentOpt = Optional
        .ofNullable(System.getenv("EINSTEIN_VISION_PRIVATE_KEY"));

    String email;
    String privateKey;

    if (Objects.isNull(args) || args.length == 0) {
      if (!(accountIdOpt.isPresent() && privateKeyContentOpt.isPresent())) {
        System.out.println("Usage: mvn test \"-Dexec.args=<email/accountId>"
            + " <path to private key>\"");
      }
      email = accountIdOpt.get();
      privateKey = privateKeyContentOpt.get();
    } else {
      email = args[0];
      privateKey = new String(Files.readAllBytes(Paths.get(args[1])));
    }

    long durationInSeconds = 60 * 15;

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
