package com.example.customerqueue_module;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.uicomponents_module.CustomProgressDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerQueueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerQueueFragment extends Fragment implements CustomerQueueView{

    private static final String TAG = "CQF-debug";

    private CustomProgressDialog customProgressDialog;

    private CustomerQueueAdapter customerQueueAdapter;
    private CustomerQueuePresenter customerQueuePresenter;

    // database references to customer queue node
    private static String dbNodePath="";
    public static void setDbNodePath(String dbNodePath) {
        //  set by the calling activity
        CustomerQueueFragment.dbNodePath = dbNodePath;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerQueueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerQueueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerQueueFragment newInstance(String param1, String param2) {
        CustomerQueueFragment fragment = new CustomerQueueFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if(dbNodePath.isEmpty() || dbNodePath=="" || dbNodePath==null){
            // dbNodePath needs to be set before creating the fragment

            Log.d(TAG, "onCreateView: no db path specified!");
            getActivity().finish();
            return null;
        }

        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.customer_queue_fragment_list, container, false);

        customProgressDialog = new CustomProgressDialog();

        //Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.customer_queue_recycler_view);
        customerQueueAdapter = new CustomerQueueAdapter(getContext(), new ArrayList<CustomerWaitingInQueue>());
        recyclerView.setAdapter(customerQueueAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        init();

        return view;
    }

    private void init() {

        customerQueuePresenter = new CustomerQueuePresenter(
                this,
                new ArrayList<CustomerWaitingInQueue>(),
                new CustomerQueueDatabaseHandler(FirebaseDatabase.getInstance().getReference(), dbNodePath)
        );

        // start the customer queue fetch from db
        customerQueuePresenter.fetchCustomerQueue();

        // show progress bar as data loads
        customProgressDialog.show(getFragmentManager(), "");

    }

    @Override
    public void onStop() {
        super.onStop();

        // MUST BE CALLED to stop listening for db updates
        customerQueuePresenter.onDestroy();
    }

    // edit these methods implemented from CustomerQueueView to handle the Adapter
    @Override
    public void onCustomerQueueFetchDoneUI(ArrayList<CustomerWaitingInQueue> customers) {

        customerQueueAdapter.setCustomersList(customers);
        customerQueueAdapter.notifyDataSetChanged();

    }

    @Override
    public void onCustomerQueueFetchFailedUI(String errorMessage) {
        Toasty.error(getContext(), errorMessage).show();
    }

    @Override
    public void onCustomerQueueEmptyUI() {
        Toasty.info(getContext(), "no customer in queue!").show();
        customProgressDialog.dismiss();
    }

    @Override
    public void onCustomerAddedToQueueUI(CustomerWaitingInQueue customer, int position) {

        // hide the progressbar
        customProgressDialog.dismiss();

        Log.d(TAG, "onCustomerAddedToQueueUI: customer added -> "+customer.toString());

        customerQueueAdapter.add(customer);
        customerQueueAdapter.notifyItemInserted(position);

    }

    @Override
    public void onCustomerRemovedFromQueueUI(CustomerWaitingInQueue customer, int position) {

        Log.d(TAG, "onCustomerRemovedFromQueueUI: customer removed -> "+customer.toString());

        customerQueueAdapter.remove(customer);
        customerQueueAdapter.notifyItemRemoved(position);

        Toasty.normal(getContext(), customer.getUsername()+" left").show();

    }
}