import imghdr
import requests
from constants import EINSTEIN_VISION_URL

_FILE_TYPES = {
    'png': 'image/png',
    'jpg': 'image/jpg',
    'jpeg': 'image/jpeg'
}

_PREDICTION_URL = EINSTEIN_VISION_URL+"/v1/vision/predict"


def predict_with_url(token, model_id, image_url):
    """Run a prediction against a model using image url.

    Args:
        token: oauth token
        model_id: model id
        image_url: image url

    Returns:
        A json string containing classes and probabilities.
    """
    
    payload = {'sampleLocation': (None, image_url), 'modelId': (None, model_id)}
    headers = {'Authorization':'Bearer ' + token, 'Cache-Control': 'no-cache'}
    return _prediction_request(_PREDICTION_URL, payload, headers)


def predict_with_image_file(token, model_id, image_file_path):
    """Run a prediction against a model using image file.

    Args:
        token: oauth token
        model_id: model id
        image_file_path: absolute path to image file

    Returns:
        A json string containing classes and probabilities.
    """
    payload = {'sampleContent': ('image', open(image_file_path, 'rb'), _FILE_TYPES[_get_image_type(image_file_path)]),
    'modelId': (None, model_id)}
    headers = {'Authorization':'Bearer ' + token, 'Cache-Control': 'no-cache'}
    return _prediction_request(_PREDICTION_URL, payload, headers)
    

def _prediction_request(url, payload, headers):
    """Make a prediction request.
    Args:
        url: endpoint url
        data: multipart payload
        headers: request headers

    Returns:
        JSON response
    """    
    try:        
        response = requests.post(url, files=payload, headers=headers)
        return response
    except requests.exceptions.RequestException as exp:
        raise exp("Prediction failed: \"{}\"".format(response))


def _get_image_type(image_file):
    """Get the type of image file (PNG, JPG or JPEG).

    Args:
        image_file: absolute path to image file

    Returns:
        Type of the image file (PNG, JPG or JPEG)
    """
    try:
        return imghdr.what(image_file)
    except IOError as e:
        raise e

