// // doc URL: https://firebase.google.com/docs/functions/unit-testing
// // for unit testing

// // At the top of test/index.test.js
// const test = require('firebase-functions-test')({
//     databaseURL: 'https://pocket-store-6e931.firebaseio.com',
//     storageBucket: 'pocket-store-6e931.appspot.com',
//     projectId: 'pocket-store-6e931',
//   }, '../pocket-store-287623-4de370ed10d7.json');


// // after firebase-functions-test has been initialized
// const allFunctions = require('../index.js'); // relative path to functions code

// // "Wrap" the shopStatusChangeTrigger from index.js
// // shopStatusChangeWrapped will invoke this functions
// const shopStatusChangeWrapped = test.wrap(allFunctions.shopStatusChangeTrigger);

// const beforeSnap ;