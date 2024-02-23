package Model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.hotelnebula.LoginActivity;
import com.example.hotelnebula.R;
import com.example.hotelnebula.SearchActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReservationAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Reservations> reservationList;
    private Reservations oneReservation;

    private String photoFromDB;

    public ReservationAdapter(Context context, ArrayList<Reservations> reservationList) {
        this.context = context;
        this.reservationList = reservationList;
    }


    @Override
    public int getCount() {
        return reservationList.size();
    }

    @Override
    public Object getItem(int position) {
        return reservationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        //1- Required declaration

        View oneItem = null;
        ImageView imPhoto;
        TextView tvRoomName, tvEvendate;

        //2- Convert the layout one_item.xml ---> java view object
        //Layout Inflation

        LayoutInflater inflater = LayoutInflater.from(context);
        oneItem = inflater.inflate(R.layout.one_item, viewGroup,false);//false is not root //true this item is the root

        //3-Reference the widgets of one_item.xml
        tvRoomName = oneItem.findViewById(R.id.tvRoomName);
        tvEvendate = oneItem.findViewById(R.id.tvEvendate);
        imPhoto = oneItem.findViewById(R.id.imPhoto);


        //4-Populate the widgets of one_item.xml

        oneReservation = (Reservations) getItem(position);
        //tvRoomName.setText(oneReservation.getUser());
        tvEvendate.setText(oneReservation.getReservationDate());
        //this.checkPhotoRoom(oneReservation.getRoomdId());
        //String photoName = photoFromDB;

        /*int imPhotoRes = context.getResources().getIdentifier("drawable/"+photoName,null,context.getPackageName());
        imPhoto.setImageResource(imPhotoRes);*/
        checkPhotoRoom(oneReservation.getRoomdId(), imPhoto, new RoomLoadedCallback() {
            @Override
            public void onPhotoLoaded(String photoName, ImageView imageView, String roomName) {

                tvRoomName.setText(roomName);
                int imPhotoRes = context.getResources().getIdentifier("drawable/" + photoName, null, context.getPackageName());
                imPhoto.setImageResource(imPhotoRes);
            }


        });

        return oneItem;
    }
    public void reloadData(ArrayList<Reservations> newReservations) {

        this.reservationList.clear();
        this.reservationList.addAll(newReservations);
        notifyDataSetChanged();

    }

    public void checkPhotoRoom(int room, ImageView imageView, RoomLoadedCallback callback) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Rooms");
        Query checkRoomdata = reference.orderByChild("id").equalTo(room);
        this.photoFromDB="";

        checkRoomdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        int roomFromDB = userSnapshot.child("id").getValue(int.class);

                        if (roomFromDB == room) {

                            photoFromDB = userSnapshot.child("photo").getValue(String.class);
                            String nameRoom = userSnapshot.child("type").getValue(String.class);

                            //callback.onPhotoLoaded(photoFromDB, imageView,nameRoom);
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onPhotoLoaded(photoFromDB, imageView,nameRoom);
                                }
                            });
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("ReservationAdapter", "Database Error: " + error.getMessage());
            }
        });
    }
}
