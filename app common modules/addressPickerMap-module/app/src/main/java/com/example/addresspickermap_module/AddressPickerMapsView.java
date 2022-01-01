package com.example.addresspickermap_module;

public interface AddressPickerMapsView {

    void fetchingAddressUI();
    void fetchAddressDoneUI(String address);
    void fetchAddressFailed(String error);

}
