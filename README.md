# puppyfinder
Get notified when there are new puppies available for adoption at the Boulder Humane Society

## Get setup
You'll need to fill out all the properties under src/main/resources/puppy.properties.

Most importantly your Twilio API creds, your Twilio registered phone number, and the registered phone numbers to message from your Twilio account.

For example

```
twilio.account.sid=<Your Twilio SID>
twilio.account.auth_token=<Your Twilio SID>

# Phone number has the country code prefixed
# Multiple to numbers can be provided with comma separators
twilio.to.numbers=1123456789,12234568998
twilio.from.number=11231231234

# Interval in minutes to check for new pups
puppy.check.interval=5
```
To package the app in a fat jar for deploying on a server you can run
```mvn package```

Run it with
```java -jar puppyfinder-1.0-SNAPSHOT-jar-with-dependencies.jar```
