
// shop active/iactive status change trigger
exports.handleShopStatusChange = (change, context, db) => {

    const promises = [];

    const status = change.after.val();
    const shopId = context.params.shopId;

    console.log("status changed ->"+shopId+"("+status+")");

    // make changes in 'customers/nearbyShops' node
    db.database().ref('customers')
    .once( 'value', (customersSnapshot) => {

        customersSnapshot.forEach( (eachCustomerSnapshot) => {
            
            if(eachCustomerSnapshot.hasChild('nearbyShops/'+shopId)){
                

                const ref = eachCustomerSnapshot.ref.child('nearbyShops/'+shopId+'/status');
                
                // using transactions keep the status tracking synchronous
                const promise = ref.transaction( currStatus => currStatus = status );
             
                promises.push(promise);
            
            }

        } );

    } );

    // delete any 'customerShopInteraction' node if exists
    // db.database().ref('customerShopInteraction')
    // .once( 'value', (interactionsSnapShot) => {

    //     interactionsSnapShot.forEach( (interactionSnap) => {

    //         if(interactionSnap.hasChild('shop/'+shopId)){

    //             const promise = interactionSnap.ref.set(null);
                
    //             promises.push(promise);

    //         }

    //     } )

    // } );

    return Promise.all(promises);

}