package com.salesforce.ps.http;

import com.salesforce.ps.representations.PredictResponse;
import java.io.IOException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.Boundary;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

public class PredictRequest extends Request {

  private final String modelId;
  private final String sampleLocation;

  public PredictRequest(String token, String modelId, String sampleLocation) {
    super(token);
    this.modelId = modelId;
    this.sampleLocation = sampleLocation;
  }

  public PredictResponse submit() throws IOException {
    FormDataMultiPart formPart = new FormDataMultiPart();
    formPart.field("modelId", modelId);
    formPart.field("sampleLocation", sampleLocation);

    MediaType contentType = formPart.getMediaType();
    contentType = Boundary.addBoundary(contentType);
    Entity<FormDataMultiPart> entity = Entity.entity(formPart, contentType);

    Response response = client.target(EINSTEIN_VISION_URL + "/v1/vision/predict")
        .request()
        .header("Authorization", "Bearer " + getToken())
        .post(entity);

    if (!isSuccessful(response)) {
      throw new IOException("Error occurred while making prediction call " + response);
    }

    return readResponseAs(response, PredictResponse.class);
  }
}
