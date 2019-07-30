require 'rest-client'

module TokenGenerator
  def TokenGenerator.generate_token(assertion)
      RestClient.post('https://api.einstein.ai/v1/oauth2/token', {
        grant_type: 'urn:ietf:params:oauth:grant-type:jwt-bearer',
        assertion: assertion
    })
  end
end
