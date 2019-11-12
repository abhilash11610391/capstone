package com.example.capstone0.BottomNavigationThings.MenShoes;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.capstone0.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EthnicShoe extends Fragment {


    ArrayList<D_ShoesDataFromInternet> arrayListEthnic=new ArrayList<>();
    Context context;
    MyAdapterEthnicmen myAdapterEthnic;

    EthnicShoe(Context context)
    {
        this.context=context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=LayoutInflater.from(context).inflate(R.layout.fragment_ethnic_shoe,container,false);
        getArrayListData();
        setRecyclerView(view);
        return view;
    }
    private void setRecyclerView(View view) {
        RecyclerView recyclerView=view.findViewById(R.id.RecyclerViewInEthnicMen);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        myAdapterEthnic=new MyAdapterEthnicmen(arrayListEthnic);
        recyclerView.setAdapter(myAdapterEthnic);
    }
    public void getArrayListData()
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("MenFootWear").child("Ethnic");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListEthnic.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    if (dataSnapshot1.exists())
                    {
                        String productDescriptionOfShoe=dataSnapshot1.child("ProductDescriptionOfShoe").getValue(String.class);
                        String ProductPrice=dataSnapshot1.child("ProductPriceOfShoe").getValue(String.class);
                        String productTitle=dataSnapshot1.child("ProductTitleOfShoe").getValue(String.class);
                        String imageLocation=dataSnapshot1.child("ImageLocation").getValue(String.class);
                        D_ShoesDataFromInternet dShoesDataFromInternet=new D_ShoesDataFromInternet(productTitle,ProductPrice,productDescriptionOfShoe,imageLocation);

                        arrayListEthnic.add(dShoesDataFromInternet);
                    }
                }
                myAdapterEthnic.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    class AsyncTaskToFetchEthnic extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... strings) {
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("MenFootWear").child("Ethnic");
            //arrayListEthnic.clear();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                        D_ShoesDataFromInternet dShoesDataFromInternet = dataSnapshot1.getValue(D_ShoesDataFromInternet.class);
                        if (dShoesDataFromInternet != null) {
                            arrayListEthnic.add(dShoesDataFromInternet);
                            Log.e("MenFragment","Going inside");
                        }
                        else
                        {
                            Log.e("MenFragment","Unable To go inside");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return null;
        }
    }

    class MyAdapterEthnicmen extends RecyclerView.Adapter<MyAdapterEthnicmen.ViewHolderClass>
    {

        ArrayList<D_ShoesDataFromInternet> arrayList;

        public MyAdapterEthnicmen(ArrayList<D_ShoesDataFromInternet> arrayList1)
        {
            this.arrayList=arrayList1;
        }
        @NonNull
        @Override
        public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view=LayoutInflater.from(getContext()).inflate(R.layout.single_view_for_label_of_shoe,parent,false);
            return new ViewHolderClass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {

            holder.Name.setText(arrayList.get(position).ProductTitleOfShoe);
            holder.Price.setText(arrayList.get(position).ProductPriceOfShoe);
        }


        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        private class ViewHolderClass extends RecyclerView.ViewHolder
        {
            TextView Name,Price;
            ImageView ProductImage;
            public ViewHolderClass(@NonNull View itemView)
            {
                super(itemView);
                Name=itemView.findViewById(R.id.SingleViewForLabelOfShoe_Name);
                Price=itemView.findViewById(R.id.SingleViewForLabelOfShoe_Price);
                ProductImage=itemView.findViewById(R.id.SingleViewForLabelOfShoe_ImageView);
            }
        }
    }
}
