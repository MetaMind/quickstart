require './jwt'
require './token_generator'
require './predict'
require 'json'

if __FILE__ == $0
  accId = ENV['EINSTEIN_VISION_ACCOUNT_ID']

  # Remove all '\n' and add newline
  privKey = String.new(ENV['EINSTEIN_VISION_PRIVATE_KEY'])
  privKey.gsub!('\n', "\n")
  exp = Time.now.to_i + (60 * 15)

  # Generate an assertion using rsa private key
  assertion = JwtHelper.sign(accId, privKey, exp)

  # Obtain oauth token
  token = JSON.parse(TokenGenerator.generate_token(assertion))
  puts "\nGenerated access token:\n"
  puts JSON.pretty_generate(token)

  access_token = token["access_token"]

  # Make a prediction call
  prediction_response = JSON.parse(
      PredictHelper.predict(access_token,
                            "GeneralImageClassifier",
                            "http://dgicdplf3pvka.cloudfront.net/images/dogbreeds/large/Siberian-Husky.jpg"))

  puts "\nPrediction response:\n"
  puts JSON.pretty_generate(prediction_response)
end
