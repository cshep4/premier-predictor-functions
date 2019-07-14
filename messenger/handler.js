'use strict';

const axios = require('axios');
const AWS = require('aws-sdk');

module.exports.messenger = (event, context, callback) => {
  if (event.method === 'GET') {
    // facebook app verification
    if (event.query['hub.verify_token'] === process.env.VERIFY_TOKEN && event.query['hub.challenge']) {
      return callback(null, parseInt(event.query['hub.challenge']));
    } else {
      return callback('Invalid token');
    }
  }

  if (event.method === 'POST') {
    event.body.entry.map((entry) => {
      entry.messaging.map((messagingItem) => {
        if (messagingItem.message && messagingItem.message.text) {
          const accessToken = process.env.ACCESS_TOKEN;

          const quotes = [
            'Don\'t cry because it\'s over, smile because it happened. - Dr. Seuss',
            'Be yourself; everyone else is already taken. - Oscar Wilde',
            'Two things are infinite: the universe and human stupidity; and I\'m not sure about the universe. - Albert Einstein',
            'Be who you are and say what you feel, because those who mind don\'t matter, and those who matter don\'t mind. - Bernard M. Baruch',
            'So many books, so little time. - Frank Zappa',
            'A room without books is like a body without a soul. - Marcus Tullius Cicero'
          ];

          // you shouldn't hardcode your keys in production! See http://docs.aws.amazon.com/AWSJavaScriptSDK/guide/snode-configuring.html
          AWS.config.update({accessKeyId: process.env.DYNAMO_DB_ACCESS_KEY, secretAccessKey: process.env.DYNAMO_DB_SECRET_KEY });

          var lambda = new AWS.Lambda();
          var params = {
            FunctionName: process.env.FUNCTION_NAME, /* required */
            Payload: '{"test" : "test"}'
          };
          lambda.invoke(params, function(err, data) {
            if (err) console.log(err, err.stack); // an error occurred
            else     console.log(data);           // successful response
          });

          const randomQuote = quotes[Math.floor(Math.random() * quotes.length)];

          const url = `https://graph.facebook.com/v2.6/me/messages?access_token=${accessToken}`;

          console.log("Sender PSID: " + messagingItem.sender.id);

          const payload = {
            recipient: {
              id: messagingItem.sender.id
            },
            message: {
              text: "Sender PSID: " + messagingItem.sender.id + ": " + randomQuote
            }
          };

          axios.post(url, payload).then((response) => callback(null, response));
        }
      });
    });
  }
};
