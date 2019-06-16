package chat.hala.app.service;

import chat.hala.app.entity.*;
import chat.hala.app.restful.exception.NotFoundException;
import chat.hala.app.restful.wrapper.MemberStatus;
import chat.hala.app.service.exception.ObjectAlreadyExistedException;
import chat.hala.app.service.exception.PreconditionNotQualifiedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by astropete on 2017/12/26.
 */
public interface MemberService {
    public Member updateAMember(Member member, Member updated, String language);
    public Member bindAMember(Member member, String code, Member bind) throws ObjectAlreadyExistedException, PreconditionNotQualifiedException;
    public Page<Member> searchMembersByKeyword(String keyword, Pageable pageable);
    public Member addMemberFondings(Member member, String fondingIds);
    public Member getMemberById(Member requestedMember, Long memberId, String language) throws NotFoundException;
    public Member getMemberByCharacterId(Member requestedMember, String characterId, String request) throws Exception;
    public void reportMember(Member member, Report report);
    public void updateMemberLocate(Member member, float longitude, float latitude);
    public Object updateMemberSetting(Member member, MemberSetting setting);
    public Object getCheckinStatus(Member member);
    public void checkin(Member member) throws ObjectAlreadyExistedException;
    public void proposeFeedback(Member member, Feedback feedback);
    public Object getMemberSetting(Member member);
    public void setMemberPushToken(Member member, Member tokenM);
    public Object getMemberRongToken(Member member) throws Exception;
    public void logout(Member member);

    Object findNearbyPeople(Long id, Double lng, Double lat, Integer page, Integer size, String flag);

    Object cloudUpdateStatus(List<MemberStatus> ls);

    Object findOnlinePeople(Long id, Double lng, Double lat, Integer page, Integer size);

    Object getTags(String language);

    Object addTag(Member member, String arg);

    Object deleteTag(Member member, String arg);

    Object findDynamics(Member member, Long memberId, Pageable pageable) throws NotFoundException;

    Object updateTag(Member member, String arg);

    Object getSystemNotice(Member member, Pageable pageable);

    Object createBottle(String bottle, Member member);

    Object getBottle(Member id);

    Object readNoticeList(Long noticeId, Long id);
}
