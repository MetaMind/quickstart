from datetime import datetime
import time
import jwt
import requests



pem_file= "name of file.pem"

my_email = "your email"


#EAI
my_endpoint = "https://api.einstein.ai/v2/oauth2/token"

expiry_time = int(time.time() + 99999)

with open(pem_file, 'r') as f:
    key_contents = f.read()

headers = {'Content-type': 'application/x-www-form-urlencoded'}


my_payload = {
    "sub": my_email,
    "aud": "https://api.einstein.ai/v2/oauth2/token",
    "exp": expiry_time
}

my_assertion = jwt.encode(my_payload, key_contents, algorithm='RS256')

my_new_payload = {
    "grant_type": "urn:ietf:params:oauth:grant-type:jwt-bearer",
    "assertion": my_assertion,
    "scope": "offline"
}

response = requests.post(url=my_endpoint, data=my_new_payload, headers=headers)
print(response.status_code)
print(response.headers)

response_body = response.json()
my_token = response_body['access_token']
print(my_token)
