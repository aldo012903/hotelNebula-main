package Model;

public class Rooms {
    public Rooms(String roomId, String desription, String photo, String type) {
        this.roomId = roomId;
        this.desription = desription;
        this.photo = photo;
        this.type = type;
    }

    private String roomId;
    private String desription;
    private String photo;
    private String type;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getDesription() {
        return desription;
    }

    public void setDesription(String desription) {
        this.desription = desription;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
