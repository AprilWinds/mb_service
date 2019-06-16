package chat.hala.app.service;

import org.springframework.data.domain.Pageable;

/**
 * Created by astropete on 2018/1/29.
 */
public interface RankService {
    public Object getGiftRanking(String type, Pageable pageable);
    public Object getWealthRanking(String type, Pageable pageable);
    public Object getRoomRanking(String type, Pageable pageable);
    public Object getRoomMemberRanking(String type, Long roomId, Pageable pageable);
}
