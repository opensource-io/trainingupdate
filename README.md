# trainingupdate
Helper app to assist with training schedule tasks

App needs the following in the current working directory:

app.properties
client_secret.json


# app.properties

```
training.api.class-sessions= (url to fetch schedule from)
jenkinsemail.to= (recipient email address)
jenkinsemail.from= (senders email address)
jenkinsemail.recipientname= (recipient's name)
jenkinsemail.sendername= (sender's name)
```


# client_secret.json

(This file is specified by the Google API)

```
{
  "installed": {
    "client_id": "123.....googleusercontent.com",
    "client_secret":"000000000000000000",
    "redirect_uris": ["http://localhost", "urn:ietf:wg:oauth:2.0:oob"],
    "auth_uri": "https://accounts.google.com/o/oauth2/auth",
    "token_uri": "https://accounts.google.com/o/oauth2/token"
  }
}
```

# run

With everything in the current directory simply run io.opensource.trainingupdate.App

For more information and assistance please contact rob@opensource.io or visit https://opensource.io/
