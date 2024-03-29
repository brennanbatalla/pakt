package btech.pakt.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.firebase.client.Firebase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import btech.pakt.FireBaseAPI;
import btech.pakt.R;
import btech.pakt.SharedPrefs;
import rapid.decoder.BitmapDecoder;

/**
 * A simple {@link Fragment} subclass.
 */
public class Item_Profile_Edit_Fragment extends Fragment implements View.OnClickListener {

    //Layout Widgets
    MaterialEditText title;
    MaterialEditText descritpion;
    MaterialEditText safetyD;
    MaterialEditText rentRate;
    ImageButton mainImage;
    ImageButton addImage;
    ImageSwitcher iSwitcher;
    ImageButton deleteImage;
    Button save;
    MaterialBetterSpinner rateDropdown;
    Firebase baseRef;
    String rate;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();


    // -------------

    ArrayList<Bitmap> imageArray = new ArrayList<>();
    ArrayList<String> bmpStrings = new ArrayList<>();
    int currentImage = 0;

    //---------- Constants ------------------------------
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CAMERA = 2;
    private static final String TAG = "ITEM PROFILE EDIT";
    private static final int RESULT_OK = -1;

    public Item_Profile_Edit_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item__profile_edit, container, false);

        initialize(v);



        return v;
    }

    public void initialize(View v){
        //Layout widgets
        title = (MaterialEditText) v.findViewById(R.id.title);
        descritpion = (MaterialEditText) v.findViewById(R.id.itemDescription);
        rentRate = (MaterialEditText) v.findViewById(R.id.itemRate);
        safetyD = (MaterialEditText) v.findViewById(R.id.itemSD);
        mainImage = (ImageButton) v.findViewById(R.id.mainImage);
        mainImage.setOnClickListener(this);
        addImage = (ImageButton) v.findViewById(R.id.addImage);
        addImage.setOnClickListener(this);
        iSwitcher = (ImageSwitcher) v.findViewById(R.id.imageSwitch);
        iSwitcher.setOnClickListener(this);
        baseRef = new Firebase((new FireBaseAPI()).getBase());
        save = (Button) v.findViewById(R.id.saveButton);
        save.setOnClickListener(this);
        deleteImage = (ImageButton) v.findViewById(R.id.deleteImage);
        deleteImage.setOnClickListener(this);
        rateDropdown = (MaterialBetterSpinner) v.findViewById(R.id.rentRateDropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.rentRateOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        rateDropdown.setAdapter(adapter);

      rateDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, parent.getItemAtPosition(position).toString());
              rate = parent.getItemAtPosition(position).toString();
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
      });

        iSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getActivity().getApplicationContext());
                if (imageArray.size() > 0) {
                    // Create a new ImageView set it's properties

                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    imageView.setImageBitmap(imageArray.get(currentImage));

                }
                return imageView;
            }
        });

        // Declaring animations
        Animation in = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right);


        iSwitcher.setInAnimation(in);
        iSwitcher.setOutAnimation(out);

        iSwitcher.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int numImages = imageArray.size() - 1;
                currentImage++;
                if (currentImage > numImages)
                    currentImage = 0;

                if( currentImage == 0)
                    mainImage.setImageResource(R.mipmap.ic_star_grey600_24dp);
                else
                    mainImage.setImageResource(R.mipmap.ic_star_outline_grey600_24dp);

                if(imageArray.size() > 0) {
                    Drawable image = new BitmapDrawable(getResources(), imageArray.get(currentImage));
                    iSwitcher.setImageDrawable(image);
                }
                return false;
            }
        });

    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivtyResult");
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".png");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Bitmap newImage = BitmapDecoder.from(thumbnail).scale(thumbnail.getWidth()*10,thumbnail.getHeight()*10).decode();

                if(!newImage.isRecycled())
                 bmpStrings.add(bitmapToString(newImage));

                imageArray.add(newImage);

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(getContext(), selectedImageUri, projection, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 500;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                Log.i(TAG,"Bitmap: "+ bm.toString());

                bm = BitmapDecoder.from(bm).scale(bm.getWidth()*10,bm.getHeight()*10).decode();
                imageArray.add(bm);

            }

        }
    }

    public void cheers(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.addImage:

                selectImage();
                break;
            case R.id.mainImage:
                if(currentImage != 0) {
                    Bitmap temp = imageArray.get(0);
                    imageArray.add(0, imageArray.get(0));
                    imageArray.add(currentImage, temp);
                    cheers("Main Image has changed!");
                    mainImage.setImageResource(R.mipmap.ic_star_grey600_24dp);
                }
                break;
            case R.id.saveButton:
                if(!title.getText().toString().matches("")) {
                    if (!descritpion.getText().toString().matches("")) {
                        if (!rentRate.getText().toString().matches("")) {
                            if (!safetyD.getText().toString().matches("")) {
                                cheers("Item Added");
                                //@todo -- Create item in Database

                                String titleS = title.getText().toString();
                                String descriptionS = descritpion.getText().toString();
                                String rentPrice = rentRate.getText().toString();
                                String deposit = safetyD.getText().toString();



                                Map<String, Object> map = new HashMap<>();
                                map.put("title",titleS);
                                map.put("description",descriptionS);
                                map.put("rentPrice",rentPrice);
                                map.put("deposit", deposit);
                                map.put("rentRate", rate);
                                map.put("images",bmpStrings);
                                map.put("ownerAuth", (new SharedPrefs(getContext())).getAuthuid());
                                map.put("datePosted", dateFormat.format(date));
                                map.put("status", "0");

                                Firebase prodRef = new Firebase(baseRef+"products");//.push().setValue(map);
                                prodRef.push().setValue(map);
                                String name = prodRef.getKey();

                                baseRef.child("users").child((new SharedPrefs(getContext())).getAuthuid()).child("inventoryKeys").push().setValue(name);


                            } else {
                                safetyD.setError("Missing Safety Deposit");
                            }
                        } else {
                            rentRate.setError("Missing Rent Rate");
                        }
                    } else {
                        descritpion.setError("Missing Product Description");
                    }
                }else {
                    title.setError("Missing Product Title");
                }


                break;

        }
    }


    private String bitmapToString(Bitmap bm){
        Bitmap bmp = Bitmap.createBitmap(bm);
        bm.recycle();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        bmp.recycle();
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }






}
