package Model;

import android.widget.ImageView;

public interface RoomLoadedCallback {
    void onPhotoLoaded(String photoName, ImageView imageView, String roomName);
}

