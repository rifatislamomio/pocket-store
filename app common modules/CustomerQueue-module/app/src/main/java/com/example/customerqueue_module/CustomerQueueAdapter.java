package com.example.customerqueue_module;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomerQueueAdapter extends RecyclerView.Adapter<CustomerQueueAdapter.MyViewHolder>
{
    ArrayList<CustomerWaitingInQueue> clist;
    Context context;

    public CustomerQueueAdapter(Context c, ArrayList<CustomerWaitingInQueue> clist)
    {
        this.context = c;
        this.clist = clist;
    }

    public void setCustomersList(ArrayList<CustomerWaitingInQueue> clist) {

        for (CustomerWaitingInQueue customer: clist) {
            this.clist.add(customer);
        }

    }

    public void add(CustomerWaitingInQueue customerWaitingInQueue){

        clist.add(customerWaitingInQueue);

    }

    public void remove(CustomerWaitingInQueue customerWaitingInQueue){

        for (CustomerWaitingInQueue cust: clist) {
            if(cust.getCustomerId()==customerWaitingInQueue.getCustomerId()){

                // remove works with address of an object and passing just any object wont work
                clist.remove(cust);

                break;
            }
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_queue_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.userName.setText(clist.get(position).getUsername());
    }


    @Override
    public int getItemCount() {
        return clist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.customer_name);
        }
    }
}
