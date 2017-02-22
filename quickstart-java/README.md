## Sample Java App

<!--[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)-->
### Running on Heroku
When the add-on is installed on Heroku, `EINSTEIN_VISION_ACCOUNT_ID`, `EINSTEIN_VISION_PRIVATE_KEY` and `EINSTEIN_VISION_URL` variables are set in the environment.
####Run the app
`mvn test`

### Running outside Heroku
If you have private key outside heroku, use it as shown below
####Run the app
`mvn test "-Dexec.args=<email/accountId> </path/to/private key>"`
