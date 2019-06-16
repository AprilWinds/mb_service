package chat.hala.app.library.util;

import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by astropete on 2018/1/30.
 */
public class General {

    public static Object pageImitation(Page<?> original, List<?> content){
        Map<String, Object> re = new HashMap<>();
        re.put("content", content);
        re.put("totalElements", original.getTotalElements());
        re.put("totalPages", original.getTotalPages());
        re.put("numberOfElements", original.getNumberOfElements());
        re.put("first", original.isFirst());
        re.put("last", original.isLast());
        re.put("number", original.getNumber());
        re.put("sort", original.getSort());
        re.put("size", original.getSize());
        return re;
    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    public static Date date(String type){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        if(type.equals("daily")){
            calendar.set(year, month, day, 0, 0, 0);
        }
        if(type.equals("monthly")){
            calendar.set(year, month, 1, 0, 0, 0);
        }
        if(type.equals("weekly")){
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.set(Calendar.HOUR, -12);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }
        return calendar.getTime();
    }

    public static Calendar midnight(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static String getAgeByBirth(String birthday) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = format.parse(birthday);
        int age = 0;
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            Calendar birth = Calendar.getInstance();
            birth.setTime(parse);

            if (birth.after(now)) {
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return String.valueOf(age);
        } catch (Exception e) {
            return "0";
        }
    }

}
