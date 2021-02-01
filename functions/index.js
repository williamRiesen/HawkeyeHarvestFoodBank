const functions = require('firebase-functions');
const admin = require('firebase-admin');
const nodemailer = require('nodemailer');
const cors = require('cors')({origin: true});
admin.initializeApp(functions.config().firebase);


let transporter = nodemailer.createTransport({
    host: 'smtp.gmail.com',
    port: 465,
    secure: true, 
    auth: {
        user: 'williamriesen@gmail.com', 
        pass: 'Tanzend0g!' 
    }
});

exports.sendMail = functions.firestore.document('triggers/{trigger}').onWrite(async (event) => {

        const db = admin.firestore();
		const ref = db.collection('reports').doc('report');
		let mailOptions = {
			from: '',
			to: 'williamriesen@gmail.com',
			subject: '',
			text: ''
		}
		ref.get()
		.then((doc) => {
        	mailOptions = {
          	  from: 'William Riesen <williamriesen@gmail.com>', 
         	  to: 'williamriesen@gmail.com',
         	  subject: 'Hawkeye Harvest Report',
          	  text: doc.get('Month') + "\r\n" +
          	  	doc.get('Year') + "\r\n" +
          	  	doc.get('totalOrders')
        	};
       		return transporter.sendMail(mailOptions)
       	})
       	.then((error, info) => {
            if(error){
                return console.log(error.toString());
            }
            return console.log('Sended');
        })
        .catch(error => console.error(error))
});


// exports.sendOrderToPackNotification = functions.firestore.document('orders/{uid}').onWrite(async (event) => {
//     //let docID = event.after.id;
//     // let title = event.after.get('title');
//     // let content = event.after.get('content');
//     var message = {
//         "notification" : {
//             "title" : "NEW ORDER RECEIVED",
//             "body" : "A new order is ready to pack.",
//         },
//         "topic" : "volunteer",
//         "android": {
//         	"notification":{
//         		"sound": "default"
//         	}
//         }
//     };
//     let orderState = event.after.get('orderState');
//     if (orderState === "SUBMITTED") {
//     	let response = await admin.messaging().send(message);
//     	console.log(response);
// 	}
// });

// exports.sendNotificationToFCMToken = functions.firestore.document('orders/{turnip}').onWrite(async (event) => {
//     // const uid = event.after.get('userUid');
//     // const title = event.after.get('title');
//     // const content = event.after.get('content');
//     // let userDoc = await admin.firestore().doc(`users/${uid}`).get();
//     // let fcmToken = userDoc.get('fcm');

//     let deviceToken = event.after.get('deviceToken');
//         var message = {
//         "notification" : {
//             "title" : "ORDER PACKED",
//             "body" : "Your order is ready to pick up.",
//         },
//         "token": deviceToken,
//         "android": {
//         	"notification":{
//         		"sound": "default"
//         	}
//         }
//     };
//         let orderState = event.after.get('orderState');
//     if (orderState === "PACKED") {
//     	let response = await admin.messaging().send(message);
//     	console.log(response);
// 	}
// });


exports.decrementInventory = functions.firestore.document('orders/{orderId}').onWrite(async (event) => {
	let orderStateBefore = event.before.get('orderState');
	let orderStateAfter = event.after.get('orderState');
	if (orderStateBefore !== "SUBMITTED" && orderStateAfter === "SUBMITTED"){
		const db = admin.firestore();
		const ref = db.collection('catalogs').doc('objectCatalog');
		ref.get()
			.then((doc) => {
				let priorItemList = doc.get('foodItemList')
				let orderedItemList = event.after.get('itemList');
				orderedItemList.forEach(foodItem => {
					let numberAvailable = foodItem['numberAvailable'];
					let name = foodItem['name']
					let qtyOrdered = foodItem['qtyOrdered']
					if (numberAvailable !== 100) {
						const thisItem = priorItemList.find(element => element['name'] === name);
						let index = priorItemList.findIndex(element => element['name'] === name)
						let numberAvailableBefore = (thisItem['numberAvailable']);
						let numberAvailableAfter = numberAvailableBefore - qtyOrdered
						thisItem['numberAvailable'] = numberAvailableAfter
						priorItemList[index] = thisItem
					}
				});
				ref.update({foodItemList: priorItemList})
				return null
			})
			.catch(function(error){
					console.error("Error writing document: ", error);
			});
		}
	});


// exports.decrementInventory = functions.firestore.document('orders/{orderId}').onWrite(async (event) => {
// //TESTED WORKING FUNCTION -- KEEP FOR BACKUP!
// 	const db = admin.firestore();
// 	const ref = db.collection('catalogs').doc('objectCatalog');
// 	ref.get()
// 		.then((doc) => {
// 			let priorItemList = doc.get('foodItemList')
// 			let orderedItemList = event.after.get('itemList');
// 			orderedItemList.forEach(foodItem => {
// 				let numberAvailable = foodItem['numberAvailable'];
// 				let name = foodItem['name']
// 				let qtyOrdered = foodItem['qtyOrdered']
// 				if (numberAvailable !== 100) {
// 					const thisItem = priorItemList.find(element => element['name'] === name);
// 					let index = priorItemList.findIndex(element => element['name'] === name)
// 					let numberAvailableBefore = (thisItem['numberAvailable']);
// 					let numberAvailableAfter = numberAvailableBefore - qtyOrdered
// 					thisItem['numberAvailable'] = numberAvailableAfter
// 						priorItemList[index] = thisItem
// 				}
// 			});
// 			ref.update({foodItemList: priorItemList})
// 			return null
// 		})
// 		.catch(function(error){
// 				console.error("Error writing document: ", error);
// 		});
// 	});




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
	// console.log(accountID)
	// console.log(orderDate)
	if (orderStateBefore ==="SAVED" && orderStateAfter === "SUBMITTED"){
		const db = admin.firestore();
		const ref = db.collection('accounts').doc(accountID);
		// const ref = firebase.firestore().collection('accounts').doc(accountID);
		ref.get()
			.then((doc) => {
				// console.log("Promise fulfilled");
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
	if (orderStateAfter === "BEING_PACKED" || orderStateAfter === "PACKED" || orderStateAfter === "SUBMITTED" || orderStateAfter === "SAVED"|| orderStateAfter === "NO SHOW"){
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
				console.error("Error writing document: ", error);
		});
	}
	if (orderStateBefore !== "PACKED" && orderStateAfter === "PACKED") {
		//when order is packed, update cumulative report:
		incrementReport(accountID)
	}
});



function incrementReport(thisAccountID){
	const db = admin.firestore();
	const refAccount = db.collection('accounts').doc(thisAccountID);
	const refReport = db.collection('reports').doc('report');
	let city = '';
	let county = '';
	let familySize = 0;
	refAccount.get()
	.then(doc => {
		city = doc.get('city');
		county = doc.get('county');
		familySize = doc.get('familySize');
		return refReport.get()
	})
	.then(doc => {
		let totalOrders = doc.get('totalOrders');
		totalOrders += 1
		refReport.update({totalOrders: totalOrders})
		return null
	})
	.catch(error => console.error(error))
}





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


