var admin = require('firebase-admin');

admin.initializeApp({
  credential: admin.credential.applicationDefault(),
  databaseURL: 'https://<hawkeye_harvest_food_bank>.firebaseio.com'
});