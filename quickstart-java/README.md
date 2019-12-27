## Sample Java App

<!--[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)-->
### Running on Heroku
When the add-on is installed on Heroku, the `EINSTEIN_VISION_ACCOUNT_ID`, `EINSTEIN_VISION_PRIVATE_KEY`, and `EINSTEIN_VISION_URL` environment variables are set.
#### Run the app
`mvn test`

### Running Outside of Heroku
If you signed up for a private key via Salesforce, use the key as shown below.
#### Run the app
`mvn test "-Dexec.args=<email/accountId> </path/to/private key>"`

**Note:** Avoid security vulnerabilities and CVEs by using the latest versions of code libraries and dependencies.
