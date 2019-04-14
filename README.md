# Documentation

## Purpose 
The purpose of this document is to give a detailed description of the requirements for the “CHATBOTS” software. It will illustrate the purpose and complete declaration for the development of system. It will also explain system constraints, interface and interactions with other external applications 
 
## Scope 
The “CHATBOTS” will allow user to select a chatbot to talk with based on different knowledge of the selected chatbot. By Integrating chatbot functionality into the existing server project, it produces real-time responses based on a variety of knowledge. The chatbot processes the user input and outputs a reply based on what the user has just sent. It could be a greeting, conversation topic, or question. 
The project will access the chat bots through the Bot Libre web API that allows sending and receiving of message data. In the web service, the request will be sent consisting of user input, and bot instance data, and returns XML data to display on the dialog box. 
Furthermore, the “CHATBOTS” software need Internet connection to fetch and display result. 
 
## Definitions and Acronyms 

Chatbot - an artificial person, animal or other creature which holds conversations with humans.  

Bot Libre - a free open source platform for artificial intelligence, chat bots, live chat, etc. 

Voice RSS - a free open source platform that provides free text-to-speech online service API without any software installation.

API - a set of clearly defined methods of communication among various components. 
 
## References 
Bot Libre: https://www.botlibre.com/ 

Voice RSS: http://www.voicerss.org 
  
  
## User characteristics 
Users should be able to communicate with the chatbots provided in the dropdown list. The user also should be able to do the following functions: 

* Login 
* Provide username 
* Logout 
* Choose a chatbot 
* Send a message 
* Retrieve a response 
* View a response 
* Listen to a response 


## User Interfaces and functional requirements 

A user of the application should see the log-in page when he/she opens the “CHATBOTS”. However, The Echo Server in the project needs to be online otherwise users will not be able to login. 
 
 
