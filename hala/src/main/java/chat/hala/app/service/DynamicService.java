package chat.hala.app.service;

import chat.hala.app.entity.Dynamic;
import chat.hala.app.entity.DynamicReport;
import chat.hala.app.entity.Member;
import chat.hala.app.restful.exception.ConflictException;
import chat.hala.app.restful.exception.PreconditionFailedException;
import chat.hala.app.restful.wrapper.CommentWithLocate;
import chat.hala.app.restful.wrapper.DynamicWithLocate;
import chat.hala.app.service.exception.ObjectAlreadyUpdateException;
import chat.hala.app.service.exception.ObjectNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface DynamicService {
    Object star(Long from, Long dynamicId, String language) throws ObjectNotFoundException;

    Object cancelStar(Long id, Long dynamicId) throws ObjectNotFoundException, ObjectAlreadyUpdateException, ConflictException;

    Object addComment(Member m, CommentWithLocate comment, String language) throws ObjectNotFoundException;

    Object findPageComments(Member member, Long dynamicId, Integer page, Integer size, Double lat, Double lng) throws ObjectNotFoundException;

    Object findDynamicById(Long dynamicId, Member memberId);

    Object findDynamicByFollow(Long id, Pageable pageable);

    Object findNearbyDynamic(Long id, Double lng, Double lat, Pageable pageable);

    Object addDynamic(Member member, DynamicWithLocate dynamicWithLocate, String language) throws  PreconditionFailedException;

    Object addDynamicReport(Member member, DynamicReport dynamicReport) throws ObjectNotFoundException;

    Object updateDynamicPrivilege(Long dynamicId, Member member, Dynamic.Type type) throws Exception;

    //Object hideDynamic(Member member, Long dynamicId) throws Exception;
}

