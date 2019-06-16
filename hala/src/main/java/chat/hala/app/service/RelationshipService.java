package chat.hala.app.service;

import chat.hala.app.entity.Member;
import chat.hala.app.restful.exception.NotFoundException;
import chat.hala.app.service.exception.PreconditionNotQualifiedException;
import org.springframework.data.domain.Pageable;

/**
 * Created by astropete on 2018/1/25.
 */
public interface RelationshipService {

    public Object getFollowingByMember(Long memberId, Pageable pageable);
    public Object getFanByMember(Long memberId, Pageable pageable);
    public Object getBlockingByMember(Member member);
    public void followMember(Member member, Long relatedId, String language) throws PreconditionNotQualifiedException;
    public void unfollowMember(Member member, Long relatedId);
    public void blockMember(Member member, Long relatedId) throws NotFoundException;
    public void unblockMember(Member member, Long relatedId);
    Object getFriendsByMember(Long memberId, Pageable pageable);
}