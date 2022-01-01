const functions = require('firebase-functions');
const admin = require('firebase-admin');
const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');

// admin level access to realtime-db 
admin.initializeApp();


const app = express();
const db = admin.database();


//TODO: set rest requests auth as middleware 
app.use( cors({origin: true}) );
app.use(bodyParser.json());
app.use( bodyParser.urlencoded({extended: false}) )



// login post request with QRCode
app.post('/login', async (req, res) => {

    let response = {
        uid: "",
        valid: false
    };

    const qrCode = req.body.qrCode;
    if( !qrCode ){
        console.log("body has no qrCode what is this behavior?")
        return res.status(400).send(response);
    }

    console.log("looking for qrCode = "+qrCode);

    let deleteQrCodePromise = null;

    // do 10 db searches for qrCode->uid each with 3s timeout (30s)
    for(let i=0;i<10;i++){

        console.log("waiting..."+((i+1)*3)+"s");

        // delay 3s
        await sleep(3000);

        await db.ref('loginCodes/'+qrCode).once('value', async snapshot => {

            if(snapshot.hasChild('uid')){
                response.uid = snapshot.child('uid').val();
                response.valid = true;

                // promise to delete the qr code if it was found
                deleteQrCodePromise = snapshot.ref.set(null);
            }

        });
        

        if(response.valid)
            break;
    }

    if(deleteQrCodePromise!==null)
            await deleteQrCodePromise
            .then( m => console.log("qrCode removed.") )
            .catch( error => console.log("error trying to remove qrCode. error="+error) );


    (response.valid) ? 
    console.log("uid found! "+response.uid) : console.log("qrCode was not created in database");

    
    return res.status(200).send(response);

});

function sleep(ms) {
// synchronous delay ms
// courtesy - https://stackoverflow.com/questions/14249506

    return new Promise((resolve) => {
      setTimeout(resolve, ms);
    });
} 


// CREATE
app.post('/write/review', async (req, res) => {

    const customerId = req.body.customerId;
    const shopId = req.body.shopId;

    const review = {
        reviewId: "",
        author: "",
        shopName: "",
        reviewBody: req.body.reviewBody,
        rating: req.body.rating
    }

    let response = { 
        success: false,
        msg: ""
    };

    if(!customerId || !shopId || !review.reviewBody || !review.rating 
        || typeof review.reviewBody !== 'string'  || review.reviewBody === "" 
        || typeof review.rating !== 'string' || review.rating === "" 
        //|| (review.rating !== "Good" && review.rating !== "Neutral" && review.rating !== "Bad" ) 
    ) {
        // bad request, body doesn't have necessary fields
        console.log("Bad request!");
        response.msg = "request body doesn't contain all necessary/appropriate fields.";
        return res.status(400).send(response);
    }

    let customerUidExists = false, shopUidExists = false;

    // see if customer uid exists
    await db.ref('customers/'+customerId).once('value', (snapshot) => {

        if(snapshot.exists()){
            
            customerUidExists = true;
            
            review.author = snapshot.child("username").val();

        }

    } );

    if(!customerUidExists){
        console.log("customer uid not found!");
        response.msg = "unauthorized request! customer uid doesn't exist.";
        return res.status(403).send(response);
    }

    // see if shop exists & post the review
    await db.ref('shops/'+shopId).once('value', (snapshot) => {

        if(snapshot.exists()){
            
            shopUidExists = true;
            
            review.shopName = snapshot.child("shopName").val();

            review.reviewId = customerId; //snapshot.ref.child('reviews').push().key;

            snapshot.ref.child("reviews/"+review.reviewId).set(review);

        }

    } );

    if(!shopUidExists){
        console.log("shop uid not found!");
        response.msg = "shop not found.";
        return res.status(400).send(response);
    }

    // review posted successfully
    console.log("review posted!");
    response.success = true;
    response.msg = "review posted successfully.";
    res.status(200).send(response);

});


// READ all nearbyShops of a customer
app.post('/read/nearbyShops', async (req, res) => {

    const customerUid  = req.body.uid;

    let response = [];  

    if(!customerUid){
        // bad request, body has no uid
        console.log("Bad request!");
        return res.status(400).send(response);
    }

    let customerUidExists = false;

    // see if customer uid exists
    await db.ref('customers/'+customerUid).once('value', (snapshot) => {

        if(snapshot.exists()){
            
            customerUidExists = true;

            nearbyShops = snapshot.child('nearbyShops');

            nearbyShops.forEach( (nearbyShop) => {

                const shop = {

                    shopId: nearbyShop.key,

                    shopName: nearbyShop.child('shopName').val(),
                    
                    shopType: nearbyShop.child('shopType').val(),

                    shopPhoneNumber: nearbyShop.child('shopPhoneNumber').val(),
                    
                    shopAddress: nearbyShop.child('shopAddress').val(),
                    shopLatitude: nearbyShop.child('shopLatitude').val(),
                    shopLongitude: nearbyShop.child('shopLongitude').val(),
                    
                    perimeterRadius: nearbyShop.child('perimeterRadius').val(),

                    status: nearbyShop.child('status').val()

                }

                response.push(shop);

            });

        }

    } );    

    if(!customerUidExists){
        console.log("customer uid doesn't exist, unauthorized request!");
        return res.status(403).send(response);
    }

    ( !Array.isArray(response) || !response.length ) ?  
        console.log("no shop found!") : console.log("shops fetched!");

    return res.status(200).send(response);

});

// READ all reviews of a particular shop
app.post('/read/reviews', async (req, res) => {

    let response = [];

    const customerId = req.body.customerId;
    const shopId = req.body.shopId;

    if(!customerId || !shopId){
        // bad request, body has no shop/customer id
        console.log("Bad request!");
        return res.status(400).send(response);
    }

    let customerUidExists = false, shopUidExists = false;

    // see if customer uid exists
    await db.ref('customers/'+customerId).once('value', (snapshot) => {

        if(snapshot.exists()){
            customerUidExists = true;
            
            nearbyShops = snapshot.child('nearbyShops');

            nearbyShops.forEach( (nearbyShop) => {
                if(nearbyShop.key === shopId)
                    shopUidExists = true;
            });

        }

    });

    if(!customerUidExists){
        console.log("customer uid doesn't exist, unauthorized request!");
        return res.status(403).send(response);
    }
    if(!shopUidExists){
        console.log("shop uid not found!");
        return res.status(400).send(response);
    }

    // see if shop exists & read all its reviews
    await db.ref('shops/'+shopId).once('value', (snapshot) => {

        if(snapshot.exists()){
            
            reviews = snapshot.child('reviews');

            reviews.forEach( reviewSnap => {
                
                const review = {
                    reviewId: reviewSnap.child('reviewId').val(),
                    author: reviewSnap.child('author').val(),
                    shopName: reviewSnap.child('shopName').val(),
                    reviewBody: reviewSnap.child('reviewBody').val(),
                    rating: reviewSnap.child('rating').val()
                };

                response.push(review);

            } );

        }

    } );

    // review posted successfully
    console.log("review posted!");
    res.status(200).send(response);

});


// UPDATE


// DELETE



// assign all https requests to express
exports.pocketStoreApi = functions.https.onRequest(app);






// triggers
const signUpTriggers = require('./databaseTriggers/signupTriggers/signupTriggerFuncs.js');
const statusTrackTriggers = require('./databaseTriggers/statusTrackTrigger/statusTrackTriggerFuncs.js');
const deleteTriggers = require('./databaseTriggers/deleteTriggers/deleteTriggerFuncs.js');


// new customer signup trigger for nearbyShop calculation
exports.newCustomerSignInTrigger = functions.database.ref('/customers/{customerId}')
.onCreate( (snapshot, context)=>{

    return signUpTriggers.handleNewCustomerSignUp(snapshot, context, admin);

});


// new shop signup nearbyShops trigger
exports.newShopSignupTrigger = functions.database.ref('shops/{shopId}')
.onCreate( (snapshot, context) => {

    return signUpTriggers.handleNewShopSignUp(snapshot, context, admin);

} );


// shop deleted trigger (feature not in app yet but currently needed for debugging)
exports.shopRemovedTrigger = functions.database.ref('shops/{shopId}')
.onDelete( (snapshot, context) => {

    return deleteTriggers.handleShopDeleted(snapshot, context, admin);

} );


// shop status(online/offline) change trigger
exports.shopStatusChangeTrigger = functions.database.ref('shops/{shopId}/status')
.onUpdate( (change, context) => {

    return statusTrackTriggers.handleShopStatusChange(change, context, admin);

} );