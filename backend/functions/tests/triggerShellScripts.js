// doc URL: https://firebase.google.com/docs/functions/local-shell

// can run these using CLI 'firebase functions:shell' 
// to test locally before deploying

shopStatusChangeTrigger( 
    {before: "active", after: "inactive"}, 
    {params: { "shopId": "6ZdGIYY30eceq196h7rZjft9Br13" }} 
);

newShopSignupTrigger(
    {
        "shopName": "deleteShop", 
        "shopType": "deleteShop", 
        "shopPhoneNumber": "deleteShop", 

        "shopAddress": "deleteShop", 
        "shopLatitude": 1.0,
        "shopLongitude": 1.1,

        "perimeterRadius": 100,

        "status": "active"
    },
    {params: { "shopId": "1234" }}
);

shopRemovedTrigger(
    {
        "shopName": "deleteShop", 
        "shopType": "deleteShop", 
        "shopPhoneNumber": "deleteShop", 

        "shopAddress": "deleteShop", 
        "shopLatitude": 1.0,
        "shopLongitude": 1.1,

        "perimeterRadius": 100,

        "status": "active"
    },
    {params: { "shopId": "1234" }}
);


newCustomerSignInTrigger(
    {
        "customerLatitude": 1.2,
        "customerLongitude": 2.5
    }
);
