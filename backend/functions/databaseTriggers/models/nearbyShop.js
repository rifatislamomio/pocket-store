class NearbyShop {

    // not used yet

    constructor(shopName, shopType, shopPhoneNumber, 
        shopAddress, shopLatitude, shopLongitude, 
        perimeterRadius, status)
        
    {

        this.shopName = shopName;
        this.shopType = shopType;
        this.shopPhoneNumber = shopPhoneNumber;
        
        this.shopAddress = shopAddress;
        this.shopLatitude = shopLatitude;
        this.shopLongitude = shopLongitude;

        this.perimeterRadius = perimeterRadius;
        this.status = status;

    }

}

module.exports = NearbyShop; 