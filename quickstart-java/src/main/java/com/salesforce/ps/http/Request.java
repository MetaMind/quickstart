package com.salesforce.ps.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public abstract class Request {

  protected final String EINSTEIN_VISION_URL = "https://api.metamind.io";
  private final String token;
  protected final Client client = ClientBuilder
      .newBuilder()
      .register(MultiPartFeature.class)
      .build();

  protected Request(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public <T> T readResponseAs(Response response, Class<T> classType) throws IOException {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(response.readEntity(InputStream.class), classType);
    } finally {
      response.close();
    }
  }

  public boolean isSuccessful(Response response) {
    return response.getStatus() / 100 == 2;
  }
}
