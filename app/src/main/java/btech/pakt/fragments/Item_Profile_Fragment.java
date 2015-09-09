package btech.pakt.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import btech.pakt.Item_Description_Class;
import btech.pakt.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Item_Profile_Fragment extends Fragment {


    TextView itemTitle;
    TextView itemDescription;
    TextView itemPPD;
    TextView itemSD;
    ImageView itemImage;

    public Item_Profile_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_item__profile, container, false);

        Item_Description_Class item = (Item_Description_Class) getActivity().getIntent().getSerializableExtra("ITEM");

        itemTitle = (TextView) v.findViewById(R.id.itemTitle);
        itemDescription = (TextView) v.findViewById(R.id.itemDescription);
        itemPPD = (TextView) v.findViewById(R.id.itemPPD);
        itemSD = (TextView) v.findViewById(R.id.itemSD);
        itemImage = (ImageView) v.findViewById(R.id.itemImage1);

        itemTitle.setText(item.getTitle());
        itemDescription.setText(item.getDescription());
        itemPPD.setText("$"+item.getPricePerDay());
        itemSD.setText("$"+item.getSafeDeposit());
        itemImage.setImageResource(item.getImage());


        return v;
    }


}