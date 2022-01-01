package com.example.customerqueue_module;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomerQueuePresenter implements CustomerQueueDatabaseCallback{
// class to handle all the business logic of a customer queue

    private static final String TAG = "CQP-debug";

    CustomerQueueView customerQueueView;
    ArrayList<CustomerWaitingInQueue> customers;
    CustomerQueueDatabaseHandler customerQueueDatabaseHandler;


    public CustomerQueuePresenter(CustomerQueueView customerQueueView,
                                  ArrayList<CustomerWaitingInQueue> customers,
                                  CustomerQueueDatabaseHandler customerQueueDatabaseHandler)
    {
        this.customerQueueView = customerQueueView;
        this.customers = customers;
        this.customerQueueDatabaseHandler = customerQueueDatabaseHandler;
    }

    public void fetchCustomerQueue(){
        // simply register listener to databaseHandler
        customerQueueDatabaseHandler.setCallback(this);
        customerQueueDatabaseHandler.registerDBChangeListener();

        // notifies when there is no customer in queue
        customerQueueDatabaseHandler.checkIfCustomerQueueEmpty();
    }

    public void onDestroy(){
        // MUST be called (how to strictly enforce this on Activity?)
        customerQueueDatabaseHandler.removeDBChangeListener();
    }


    @Override
    public void onCustomerAddedToQueueInDB(DataSnapshot dataSnapshot) {

        try {
            CustomerWaitingInQueue newCustomer = dataSnapshot.getValue(CustomerWaitingInQueue.class);

            Log.d(TAG, "onCustomerAddedToQueueInDB: customer added -> "+newCustomer.toString());

            customers.add(newCustomer);

            // always add new customer at the end of queue
            int newCustomerPosition = customers.size()-1;

            // update UI
            customerQueueView.onCustomerAddedToQueueUI(newCustomer, newCustomerPosition);

        }catch (Exception e){
            // thrown when there is a type mismatch in databases nodes and CustomerWaitingInQueue object's variables

            Log.d(TAG, "onCustomerAddedToQueueInDB: new customer not added! error = "+e.getMessage());
            return;
        }

    }

    @Override
    public void onCustomerRemovedFromQueueInDB(DataSnapshot dataSnapshot) {

        CustomerWaitingInQueue customerToBeRemoved = dataSnapshot.getValue(CustomerWaitingInQueue.class);

        int customerRemovedIndex = -1;

        // DON'T DO list.remove(), this matches object's addresses not variables

        for (int i=0;i<customers.size();i++){
            if(customers.get(i).getCustomerId() == customerToBeRemoved.getCustomerId()){

                customers.remove(i);

                customerRemovedIndex = i;

                break;
            }
        }

        if(customerRemovedIndex!=-1){

            Log.d(TAG, "onCustomerRemovedFromQueueInDB: customer removed -> "+customerToBeRemoved.toString());

            // update UI
            customerQueueView.onCustomerRemovedFromQueueUI(customerToBeRemoved, customerRemovedIndex);
        }

    }

    @Override
    public void onCustomerQueueEmptyInDB() {
        customerQueueView.onCustomerQueueEmptyUI();
    }

    @Override
    public void onDBFailure(String errorMessage) {
        customerQueueView.onCustomerQueueFetchFailedUI("something went wrong! please make sure you have active internet connection");
        Log.d(TAG, "onDBFailure: error = "+errorMessage);
    }

}
