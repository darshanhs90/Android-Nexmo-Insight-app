# Android-Nexmo-Insight-app
<br/>Link:http://devpost.com/software/number-share
##Inspiration
Every time you meet a new person,there is a problem of sharing the cell phone number.Assume You are an american and you meet an spanish friend.Due to the change in dialects and languages,there is a problem of sharing phone number. Also even in case of same dialects,there are chances that you might miss out on a single digit of a number and face the problem of contacting later. So this application along with the Nexmo Insight API helps in efficient sharing and validation of the phone numbers.

##How it works
The user can log in to the profile and create a QR code,provided he gives a valid formatted phone number. This QR code can be used by others to receive the phone number. 
<br/>The process works as follows 
<br/>1.The user Creates the QR code in the profile tab.While creating in case he enters an invalid number,The NEXMO Insight API recognizes its format and prompts the user to enter the number in proper format 
<br/>2.The user can scan others Profile QR code to get the phone number,once the number is retrieved after the scan is done,we pass the number to the Nexmo API to check its format,only on its success we save the phone number into our contacts.We also save the users country,international format number,etc 
<br/>3.Suppose a User is asked a favor by his friend to recharge/pay the bill for his number.the user should find the carrier name ,country of the friends,etc before proceeding with the request.Hence we use the Nexmo API to gather the carrier information prior and direct the user to the proper website easily 
<br/>4.Suppose the user wants to know the roaming information,we use the Nexmo API to get the country code and carrier name to get the corresponding roaming information. 
<br/>5.Suppose the user wants to call an friend staying abroad,at first the user has to check the timezone of his friend before calling.This application along with Nexmo API gets the location of the receivers phone number and suggests the user with the time at the receivers end before placing the call. Also before placing the call,the phone number is validated using Nexmo API. 
<br/>6.Suppose the user gets a missed call or wants to know the location of the received call.Using the Number info the user can get the location of the caller from the Nexmo Insight API which is mapped to google maps

##Challenges I ran into
##Integrating the Nexmo API into the android SDK and integrating the QR code scanner module.

##Accomplishments that I'm proud of
Developed an application using which it is now easier to share the phone numbers.

##What's next for Number Share
Validate the number for its reachability Provide more info to the user regarding his number.

##tl;dr This is a android application to share the phone numbers using qr code and validate the shared phone numbers using Nexmo API

##App Download Link https://s3-us-west-2.amazonaws.com/hackathonhacks/NumberShare.apk or https://s3-us-west-2.amazonaws.com/hackathonhacks/numbershare.apk

##Built With
android
nexmo
nexmo-insight-api
google-maps
