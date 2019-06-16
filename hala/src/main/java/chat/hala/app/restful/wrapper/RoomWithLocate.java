package chat.hala.app.restful.wrapper;

import chat.hala.app.entity.Room;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * Created by astropete on 2018/1/28.
 */
public class RoomWithLocate {

    private Room room;
    private Coordinate position;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "RoomWithLocate{" +
                "room=" + room +
                ", position=" + position +
                '}';
    }
}
