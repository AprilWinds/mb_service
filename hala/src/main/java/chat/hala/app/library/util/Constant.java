package chat.hala.app.library.util;

import chat.hala.app.entity.Room;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by astropete on 2018/3/19.
 */
public class Constant {

    public static final String ApplePaySandboxUrl = "https://sandbox.itunes.apple.com/verifyReceipt";

    public static final String ApplePayBuyUrl = "https://buy.itunes.apple.com/verifyReceipt";

    public static String getTestCode(){
        return "012321";
    }

    public static String getPrivateKey(){
        return "ha1a2ol$cha+!";
    }

    public static Integer[] getCheckinReward(){
        Integer[] re = {5, 10, 15, 20, 30, 40, 60};
        return re;
    }

    public static final String HALA_NOTICE_CHARACTERID="03477715";

    public static final String LAN_ARAB = "arabic";

    public static final String LAN_ARAB_ROOM_INTRO = "انا منتظرلك لتاتي ونتحدث";

    public static final String LAN_ENG_ROOM_INTRO = "I'm waiting for you,come and chat with me!";

    public static String getDefaultParamString(){
        return "default";
    }

    public static int getDefaultInsiderPrice(){
        return 20;
    }

    public static int getDefaultMicrophonePrice(){
        return 0;
    }

    public static int getDefaultMicrophoneCount(){
        return 5;
    }

    public static Room.MicFacing getDefaultMicrophoneFacing(){
        return Room.MicFacing.everyone;
    }

    /*public static Map getRoomStyle(){
        Map<Room.RoomStyle, Map<String, Integer>> styles = new HashMap<>();
        Map<String, Integer> standard = new HashMap<>();
        standard.put("coin", 0);
        standard.put("admin", 3);
        standard.put("attender", 20);
        styles.put(Room.RoomStyle.standard, standard);
        Map<String, Integer> garden = new HashMap<>();
        garden.put("coin", 500);
        garden.put("admin", 7);
        garden.put("attender", 60);
        styles.put(Room.RoomStyle.garden, garden);
        Map<String, Integer> island = new HashMap<>();
        island.put("coin", 1500);
        island.put("admin", 7);
        island.put("attender", 150);
        styles.put(Room.RoomStyle.island, island);
        Map<String, Integer> castle = new HashMap<>();
        castle.put("coin", 10000);
        castle.put("admin", 17);
        castle.put("attender", 1500);
        styles.put(Room.RoomStyle.castle, castle);
        return styles;
    }*/

    public static Integer getRechargeCoin(double amount){
        Map<Double, Integer> rmc = new HashMap<>();
        rmc.put(0.99, 100);
        rmc.put(8.99, 1000);
        rmc.put(47.99, 5500);
        rmc.put(99.99, 12000);
        rmc.put(399.99, 50000);
        return rmc.get(amount);
    }


}
