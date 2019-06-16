package chat.hala.app.restful.wrapper;

import chat.hala.app.entity.Room;

/**
 * Created by astropete on 2018/7/15.
 */
public class RoomAmount {

    private Integer amount;
    private Room room;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
