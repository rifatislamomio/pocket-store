//jshint esversion:6
const express = require("express");
const bodyParser = require("body-parser");
const qr = require("qr-image");
const { v4: uuid } = require("uuid"); //using version 4
const axios = require("axios"); // https req-res package
const session = require('express-session'); // session package
// template engine
const exphbs = require('express-handlebars');

// server running port
const PORT = process.env.PORT || 3230;

const app = express();

// middlewares
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:true}));
// reference static files by '/..' (and not '/static/...')
app.use(express.static(__dirname + '/static'));

// handlebar middle-ware
app.engine('handlebars', exphbs({ defaultLayout: 'main' }));
app.set('view engine', 'handlebars');

// session package setup
app.use(session({
    // TODO: auto generate(?) unique secret key
    secret: "secret-key",
    resave: false,
    saveUninitialized: false
}));





// landing page
app.get("/", function (req, res) {

    if(req.session.userId)
        res.redirect('/home');

    res.render('index');

    //res.sendFile(__dirname + "/index.html");
    //Generating QR Code
});





// generate unique qrcode and pass to image
app.get('/qr/:codeImage', function (req, response) {

    let key = uuid().toString();
    req.session.qrCode = key;

    console.log("qrcode = " + key);
    
    let code = qr.image(key, { type: 'png', ec_level: 'H', size: 10, margin: 0 });
    response.setHeader("Content-type", "image/png");
    code.pipe(response);

    //nijeke nije request, doesn't work

});





// login validator/checker
app.get('/login', (req, res) => {

    const key = req.session.qrCode;

    if(!key)
        // TODO: create a static error page to redirect to when any error occurs
        return res.status(400).send('<h1>Qr-code kothay?</h1>');

    axios.post("https://us-central1-pocket-store-6e931.cloudfunctions.net/pocketStoreApi/login", 
                { qrCode: key })
        .then((result) => {

            if(!result.data.valid)
                return res.status(400).send('<h1>App e scan kora hoy nai</h1>');
            
            req.session.userId = result.data.uid;
            
            console.log("userId = " + req.session.userId);
            
            return res.redirect('/home');
        })

        .catch((error) => {
            console.error("userId fetch failed, error = " + error);
            // TODO: create a static error page to redirect to when any error occurs
            return res.status(500).send('<h2>server e somossa hoise come back later</h2>');
        });

} );





// home page (first page after login)
app.get("/home",function(req, res){

    loggedInUserId = req.session.userId;

    if(!loggedInUserId)
        return res.redirect('/');
    
    axios.post("https://us-central1-pocket-store-6e931.cloudfunctions.net/pocketStoreApi/read/nearbyShops",
    { uid: loggedInUserId })
    .then( (result) => {

        let shops = result.data;

        console.log(shops);

        if(shops[0] === undefined){
            console.log("empty array received!");
            return res.send("<h2>invalid uid</h2>");
        }
        return res.render('home', {
            shops
        });
    } )
    .catch(error => {
        console.error("server error = " + error);
            // TODO: create a static error page to redirect to when any error occurs
            return res.status(500).send('<h2>server e somossa hoise come back later</h2>');
    });

    
});





// show reviews of a particular shop
app.get('/shops/:shopId', (req, res) => {

    if(!req.session.userId){
        console.log("user logged out");
        return res.redirect('/');
    }

    shopId = req.params.shopId;
    req.session.shopId = shopId;
    customerId = req.session.userId;

    axios.post("https://us-central1-pocket-store-6e931.cloudfunctions.net/pocketStoreApi/read/reviews",
    { "customerId": customerId, "shopId": shopId })
    .then( (result) => {

        let reviews = result.data;

        console.log(reviews);
         
        return res.render('reviews', {
            reviews
        });
    } )
    .catch(error => {
        console.error("server error = " + error);
            // TODO: create a static error page to redirect to when any error occurs
            return res.status(500).send('<h2>server e somossa hoise come back later</h2>');
    });

});





// post a review
app.post('/postReview', function (req, res) {
  
    if(!req.session.userId)
        return res.redirect('/');
    
    shopId = req.session.shopId;
    
    delete req.session.shopId;

    customerId = req.session.userId;
    reviewBody = req.body.reviewBody;
    reviewBody = reviewBody.trim();
    rating = req.body.rating;

    console.log(customerId+", "+shopId+", "+rating+", "+reviewBody);

    if(reviewBody===null || reviewBody==="" || reviewBody===undefined
    || rating===null || rating==="" || rating===undefined)
        return res.redirect('/shops/'+shopId);

    axios.post("https://us-central1-pocket-store-6e931.cloudfunctions.net/pocketStoreApi/write/review",
    { "customerId": customerId, "shopId": shopId, "reviewBody": reviewBody, "rating": rating })
    .then( (result) => {
         console.log(result.data);
        return res.redirect('/shops/'+shopId);
    } )
    .catch(error => {
        console.error("server error = " + error);
            // TODO: create a static error page to redirect to when any error occurs
            return res.status(500).send('<h2>server e somossa hoise come back later</h2>');
    });

})





// logout
app.post('/logout', function (req, res) {
    logOutByDestroyingSession(req);
    res.redirect('/');
})













// start up the server
app.listen(PORT, function(){
    console.log("Server started successfully!");
});


// helper functions
function logOutByDestroyingSession(req){
    req.session.destroy();
}

function getFullUrl(req){
// courtesy: https://stackoverflow.com/questions/10183291/how-to-get-the-full-url-in-express
    return req.protocol + '://' + req.get('host') + req.originalUrl;
}


// async function makeRequest() {
//     try {
//         console.log("working");
//         if (userId != null) {
//             response.redirect("/home");
//         }
//     } catch (err) { console.log(err) };
// };
