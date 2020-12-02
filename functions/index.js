const functions = require('firebase-functions');
const admin = require('firebase-admin');
const nodemailer = require('nodemailer');
const cors = require('cors')({origin: true});
admin.initializeApp(functions.config().firebase);

// let transporter = nodemailer.createTransport({
//     service: 'gmail',
//     auth: {
//         user: 'william.riesen@gmail.com',
//         pass: 'Tanzend0g!'
//     }
// });

// exports.sendMailOrderSubmitted = functions.firestore.document('orders/{turnip}').onWrite(async (event) => {
//     // cors(req, res, () => {
//         const mailOptions = {
//             from: 'Your Account Name <yourgmailaccount@gmail.com>', // Something like: Jane Doe <janedoe@gmail.com>
//             to: 'william.riesen@gmail.com',
//             subject: 'Food Bank Order Recieved',
//             html: `<p style="font-size: 16px;">Pickle Riiiiiiiiiiiiiiiick!!</p>
//                 <br />
//                 <img src="https://images.prod.meredith.com/product/fc8754735c8a9b4aebb786278e7265a5/1538025388228/l/rick-and-morty-pickle-rick-sticker" />
//             ` // email content in HTML
//         };
//         // returning result
//         return transporter.sendMail(mailOptions, (erro, info) => {
//             if(erro){
//                 return res.send(erro.toString());
//             }
//             return res.send('email sent.');
//         });
//     // });    
// });

exports.sendOrderToPackNotification = functions.firestore.document('orders/{uid}').onWrite(async (event) => {
    //let docID = event.after.id;
    // let title = event.after.get('title');
    // let content = event.after.get('content');
    var message = {
        "notification" : {
            "title" : "NEW ORDER RECEIVED",
            "body" : "A new order is ready to pack.",
        },
        "topic" : "volunteer",
        "android": {
        	"notification":{
        		"sound": "default"
        	}
        }
    };
    let orderState = event.after.get('orderState');
    if (orderState === "SUBMITTED") {
    	let response = await admin.messaging().send(message);
    	console.log(response);
	}
});

exports.sendNotificationToFCMToken = functions.firestore.document('orders/{turnip}').onWrite(async (event) => {
    // const uid = event.after.get('userUid');
    // const title = event.after.get('title');
    // const content = event.after.get('content');
    // let userDoc = await admin.firestore().doc(`users/${uid}`).get();
    // let fcmToken = userDoc.get('fcm');

    let deviceToken = event.after.get('deviceToken');
        var message = {
        "notification" : {
            "title" : "ORDER PACKED",
            "body" : "Your order is ready to pick up.",
        },
        "token": deviceToken,
        "android": {
        	"notification":{
        		"sound": "default"
        	}
        }
    };
        let orderState = event.after.get('orderState');
    if (orderState === "PACKED") {
    	let response = await admin.messaging().send(message);
    	console.log(response);
	}
});

// exports.updateLastOrderDate = functions.firestore.document('orders/{turnip}').onWrite(async (event) => {
// 	let accountID = event.after.get('accountID');
// 	let orderDate = event.after.get('date');
// 	let orderStateBefore = event.before.get('orderState');
// 	let orderStateAfter = event.after.get('orderState');
// 	console.log(accountID)
// 	console.log(orderDate)
// 	if (orderStateBefore ==="SAVED" && orderStateAfter === "SUBMITTED"){
// 		const db = admin.firestore();
// 		const ref = db.collection('accounts').doc(accountID);
// 		// const ref = firebase.firestore().collection('accounts').doc(accountID);
// 		ref.get()
// 			.then((doc) => {
// 				console.log("Promise fulfilled");
//     			if (doc.exists){
//      	  			 ref.update({lastOrderDate: orderDate});
//      	  			}
//  				return null
//  				}
//  			)
 		 
// 			.catch(function(error){
// 				// console.log("Promise rejected")

// 				console.error("Error writing document: ", error);
// 		});
// 	}
// 	if (orderStateAfter === "PACKED" || orderStateAfter === "SUBMITTED" || orderStateAfter === "SAVED"|| orderStateAfter === "NO SHOW"){
// 		const db = admin.firestore();
// 		const ref = db.collection('accounts').doc(accountID);
// 		ref.get()
// 			.then((doc) => {
// 				console.log("Promise fulfilled");
//     			if (doc.exists){
//      	  			 ref.update({orderState: orderStateAfter});
//      	  			}
//  				return null
//  				}
//  			)
 		 
// 			.catch(function(error){
// 				// console.log("Promise rejected")

// 				console.error("Error writing document: ", error);
// 		});
// 	}
// });

exports.updateLastOrderDate = functions.firestore.document('orders/{turnip}').onWrite(async (event) => {
	let accountID = event.after.get('accountID');
	let orderDate = event.after.get('date');
	let orderStateBefore = event.before.get('orderState');
	let orderStateAfter = event.after.get('orderState');
	let pickUpHour24 = event.after.get('pickUpHour24');
	if (pickUpHour24 === null) {
		lastOrderType = "ON_SITE"
	} else{
		lastOrderType = "NEXT_DAY"
	}
	console.log(accountID)
	console.log(orderDate)
	if (orderStateBefore ==="SAVED" && orderStateAfter === "SUBMITTED"){
		const db = admin.firestore();
		const ref = db.collection('accounts').doc(accountID);
		// const ref = firebase.firestore().collection('accounts').doc(accountID);
		ref.get()
			.then((doc) => {
				console.log("Promise fulfilled");
    			if (doc.exists){
     	  			 ref.update({lastOrderDate: orderDate});
     	  			 ref.update({lastOrderType: lastOrderType});
     	  			 ref.update({pickUpHour24: pickUpHour24});
     	  			}
 				return null
 				}
 			)
 		 
			.catch(function(error){
				// console.log("Promise rejected")

				console.error("Error writing document: ", error);
		});
	}
	if (orderStateAfter === "PACKED" || orderStateAfter === "SUBMITTED" || orderStateAfter === "SAVED"|| orderStateAfter === "NO SHOW"){
		const db = admin.firestore();
		const ref = db.collection('accounts').doc(accountID);
		ref.get()
			.then((doc) => {
				console.log("Promise fulfilled");
    			if (doc.exists){
     	  			 ref.update({orderState: orderStateAfter});
     	  			}
 				return null
 				}
 			)
 		 
			.catch(function(error){
				// console.log("Promise rejected")

				console.error("Error writing document: ", error);
		});
	}
});



// // set a custom claim for a specific user
// exports.appointAsManager = functions.https.onRequest(async(req, res) => {
// 	//Grab the text parameter (email of appointee)
// 	const appointeeEmail = req.query.text;
// 	admin.auth().getUserByEmail(appointeeEmail).then((user) => {
// 		// const currentCustomClaims = user.customClaims;
// 		// currentCustomClaims['manager'] = true
// 		console.log(user.uid)
// 		console.log(user.displayName)
// 		const customClaims = {
// 			manager: true
// 		}
// 		return admin.auth().setCustomUserClaims(user.uid, customClaims)
// 	})
// 	.catch((error) => {
// 		console.log(error);
// 	})
// 	const writeResult = await admin.firestore().collection('messages').add({appointeeEmail, appointeeEmail});
//   // Send back a message that we've succesfully written the message
//   res.json({result: `Message with ID: ${writeResult.id} added.`});
//   });



// set a custom claim for a specific user


// USE THIS STRING TO CALL HTTTP REQUEST!  http://localhost:5001/hawkeye-harvest-food-bank/us-central1/appointAsVolunteer?text=hhfb50401@gmail.com
exports.appointAsVolunteer = functions.https.onRequest(async(req, res) => {
	//Grab the text parameter (email of appointee)
	const appointeeEmail = req.query.text;
	admin.auth().getUserByEmail(appointeeEmail).then((user) => {
		// const currentCustomClaims = user.customClaims;
		// currentCustomClaims['manager'] = true
		console.log(user.uid)
		console.log(user.displayName)
		const customClaims = {
			volunteer: true
		}
		return admin.auth().setCustomUserClaims(user.uid, customClaims)
	})
	.catch((error) => {
		console.log(error);
	})
	// const writeResult = await admin.firestore().collection('messages').add(appointeeEmail);
	const writeResult = await admin.firestore().collection('messages').add({appointeeEmail: appointeeEmail});
  // Send back a message that we've succesfully written the message
  res.json({result: `Message with ID: ${writeResult.id} added.`});
  });

// USE THIS STRING TO CALL HTTTP REQUEST!  http://localhost:5001/hawkeye-harvest-food-bank/us-central1/appointAsManager?text=williamriesen@gmail.com
exports.appointAsManager = functions.https.onRequest(async(req, res) => {
	//Grab the text parameter (email of appointee)
	const appointeeEmail = req.query.text;
	admin.auth().getUserByEmail(appointeeEmail).then((user) => {
		// const currentCustomClaims = user.customClaims;
		// currentCustomClaims['manager'] = true
		console.log(user.uid)
		console.log(user.displayName)
		const customClaims = {
			manager: true
		}
		return admin.auth().setCustomUserClaims(user.uid, customClaims)
	})
	.catch((error) => {
		console.log(error);
	})
	// const writeResult = await admin.firestore().collection('messages').add(appointeeEmail);
	const writeResult = await admin.firestore().collection('messages').add({appointeeEmail: appointeeEmail});
  // Send back a message that we've succesfully written the message
  res.json({result: `Message with ID: ${writeResult.id} added.`});
  });


