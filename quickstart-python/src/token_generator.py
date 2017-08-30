import json
import requests
from constants import EINSTEIN_VISION_URL

def generate_token(assertion):
    """Generate a token using assertion.

    Args:
        assertion: assertion generated using the private key

    Returns:
        An oauth token generated using the assertion.
    """
    url = EINSTEIN_VISION_URL+"/v1/oauth2/token"
    data = {
        "grant_type" : "urn:ietf:params:oauth:grant-type:jwt-bearer", 
        "assertion" : assertion
    }

    try:
        response = requests.post(url, data=data)
        return response
    except requests.exceptions.RequestException as exp:
        raise exp("Token generation failed: \"{}\"".format(response))
