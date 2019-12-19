require 'jwt'

module JwtHelper
  def JwtHelper.sign(subject, privateKey, expiry)
    rsa_private = OpenSSL::PKey::RSA.new(privateKey)
    payload = {
        :sub => subject,
        :aud => "https://api.einstein.ai/v2/oauth2/token",
        :exp => expiry
    }
    assertion = JWT.encode payload, rsa_private, 'RS256'
    assertion
  end
end

