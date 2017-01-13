package com.salesforce.ps;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Refreshes the token with the given interval so that your application always has a valid token.
 * <p>
 * Continue to use the same <code>AccessTokenProvider</code> and the token gets refreshed before it
 * expires.
 * </p>
 */
public class AccessTokenRefresher {

  public static void schedule(AccessTokenProvider accessTokenProvider, long refreshAfterInSeconds) {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(accessTokenProvider::refreshToken, refreshAfterInSeconds - 2,
        refreshAfterInSeconds, TimeUnit.SECONDS);
  }
}
