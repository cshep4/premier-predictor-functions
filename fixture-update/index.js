const functions = require('firebase-functions');

exports.handler = functions.https.onRequest((request, response) => {
  var request = require('request');

  request({
    url: 'https://premierpredictor.herokuapp.com/fixtures/update',
    method: 'PUT',
    headers: {
      Accept: 'application/json',
      'Content-Type': 'application/json'
    },
  }, function (error, response, body) {
    if (!error && response.statusCode == 200) {
      console.log('BODY: ', body);
    }
  });
});
