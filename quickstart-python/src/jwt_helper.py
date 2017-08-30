import jwt
from constants import EINSTEIN_VISION_URL

def generate_assertion(subject, private_key, expiry):
    """Generate an assertion using RSA private key.

    Args:
        subject: subject
        private_key: private key used to generate assertion
        expiry: time for the assertion/token to expire

    Returns:
        An assertion.
    """
    url = EINSTEIN_VISION_URL+"/v1/oauth2/token"
    payload = {'sub': subject, 'aud': url, 'exp': expiry}

    try:
        return jwt.encode(payload, private_key, algorithm='RS256')
    except Exception as exp:
        raise exp
  
