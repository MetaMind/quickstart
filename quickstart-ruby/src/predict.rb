require 'rest-client'

module PredictHelper
  def PredictHelper.predict(token, model_id, sample_location)
      RestClient.post('https://api.einstein.ai/v1/vision/predict',
                    {:sampleLocation => sample_location,
                     :modelId => model_id, :multipart => true},
                    headers = {:authorization=> "Bearer #{token}"})
  end
end
