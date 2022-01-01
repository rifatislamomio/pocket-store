
// new shop sign up trigger
exports.handleNewShopSignUp = (snapshot, context, db) => {

    if(!snapshot.hasChild('shopLatitude') || !snapshot.hasChild('shopLongitude') ){
        console.log("why doesn't shop have latLng?")
        return null;
    }

    const promises = [];

    const newShop = snapshot.val();
    const newShopId = context.params.shopId;

    const newNearbyShop = {

        shopName: newShop.shopName,

        shopType: newShop.shopType,

        shopPhoneNumber: newShop.shopPhoneNumber,

        shopAddress: newShop.shopAddress,
        shopLatitude: newShop.shopLatitude,
        shopLongitude: newShop.shopLongitude,

        primeterRadius: newShop.perimeterRadius,
        
        status: newShop.status
    };

    db.database().ref('customers')
    .once( 'value', (customerSnapshot) => {


        customerSnapshot.forEach( (eachCustomerSnap) => {

            const customerLatitude = eachCustomerSnap.child('customerLatitude').val();
            const customerLongitude = eachCustomerSnap.child('customerLongitude').val();
            
            const distance = caculateDistance(
                newShop.shopLatitude, newShop.shopLongitude, 
                customerLatitude, customerLongitude);
            
            console.log("calculated distance = "+distance);

            if( distance <= newShop.perimeterRadius ){
                    const promise = eachCustomerSnap.ref.child('nearbyShops/'+newShopId).set(newNearbyShop);
                    promises.push(promise);
                }

        } );

    } );


    return Promise.all(promises);

}



// new customer signup trigger
exports.handleNewCustomerSignUp = (snapshot, context, db) => {

    const promises = [];

    if(!snapshot.hasChild('customerLatitude') || !snapshot.hasChild('customerLongitude')){
        console.error("why doesn't new customer have latLng?");
        return null;
    }

    const newCustomer = snapshot.val();

    // check all shops to find nearbyShops
    db.database().ref('shops')
    .once('value', (shopSnapshot)=>{
        
        shopSnapshot.forEach( (childSnapshot) => {
                
                const shopId = childSnapshot.key;
                const potentialNearbyShop = {

                    shopName: childSnapshot.child('shopName').val(),
                    
                    shopType: childSnapshot.child('shopType').val(),

                    shopPhoneNumber: childSnapshot.child('shopPhoneNumber').val(),
                    
                    shopAddress: childSnapshot.child('shopAddress').val(),
                    shopLatitude: childSnapshot.child('shopLatitude').val(),
                    shopLongitude: childSnapshot.child('shopLongitude').val(),
                    
                    perimeterRadius: childSnapshot.child('perimeterRadius').val(),

                    status: childSnapshot.child('status').val()

                }
                
                const distance = caculateDistance(newCustomer.customerLatitude, newCustomer.customerLongitude, 
                    potentialNearbyShop.shopLatitude, potentialNearbyShop.shopLongitude);
                
                console.log("calculated distance = "+distance);

                if( distance <= potentialNearbyShop.perimeterRadius ){

                    const promise =  snapshot.ref.child("nearbyShops/"+shopId).set(potentialNearbyShop);
                    promises.push(promise);
                }

        } )

    } );

    // db triggers must always return promise/s
    return Promise.all(promises);

}


function caculateDistance(lat1, long1, lat2, long2){
    // distance from latitude, longitude
    // courtesy - https://www.geeksforgeeks.org/program-distance-two-points-earth/
    
    // trigonometric functions require radian value
    lat1 = lat1*Math.PI / 180;
    long1 = long1*Math.PI / 180;
    lat2 = lat2*Math.PI / 180;
    long2 = long2*Math.PI / 180;

    // Harvesine formula
    dlong = long2-long1;
    dlat = lat2-lat1;

    var res = Math.sin(dlat/2)*Math.sin(dlat/2) 
    + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dlong/2)*Math.sin(dlong/2);

    res = 2*Math.asin( Math.sqrt(res) );

    // return 0 for testing
    return 0;
    
}



