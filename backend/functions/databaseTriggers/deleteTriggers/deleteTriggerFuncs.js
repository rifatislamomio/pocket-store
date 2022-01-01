
// shop deleted
exports.handleShopDeleted = (snapshot, context, db) => {


    const promises = [];

    const shopId = context.params.shopId;

    console.log("shop removed -> "+snapshot.child('shopName').val());

    db.database().ref('customers')
    .once('value', (customersSnapshot) => {

        customersSnapshot.forEach( (eachCustomerSnapshot) => {
            
            if(eachCustomerSnapshot.hasChild('nearbyShops/'+shopId)){
                
                const promise = eachCustomerSnapshot.ref.child('nearbyShops/'+shopId).set(null);
             
                promises.push(promise);
            
            }

        } );

    } );

    return Promise.all(promises);

}
