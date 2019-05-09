package wings.app.microblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import wings.app.microblog.Exception.BlockedException;
import wings.app.microblog.entity.BlackList;
import wings.app.microblog.entity.Member;
import wings.app.microblog.entity.RelationShip;
import wings.app.microblog.repository.BlackListRepository;
import wings.app.microblog.repository.RelationShipRepository;
import wings.app.microblog.util.ErrorCode;
import wings.app.microblog.util.RelationType;

import java.util.Date;
import java.util.List;

@Service
public class RelationShipService {

    @Autowired
    private RelationShipRepository relationShipRepo;

    @Autowired
    private BlackListRepository blackListRepo;

    public void  follow(Long from, Long to) throws BlockedException {
        RelationType relationType =relation(from, to);
        if(relationType.getId()>RelationType.blocking.getId()){
            throw new BlockedException();
        }else if(relationType.getId()==RelationType.none.getId()||relationType.getId()==RelationType.followed.getId()){
            RelationShip r=new RelationShip();
            r.setCreateAt(new Date());
            r.setUpdateAt(new Date());
            r.setFollowerId(from);
            r.setFollowingId(to);
            relationShipRepo.saveAndFlush(r);
        }

    }

    public void  unfollow(Long from, Long to) {
        RelationType relationType=relation(from,to);
        if (relationType.getId()==RelationType.friend.getId()||relationType.getId()==RelationType.following.getId()){
            RelationShip relationShip=relationShipRepo.findByFollowing(from,to);
            if (relationShip!=null) relationShipRepo.delete(relationShip);
        }
    }

    public List<Member> getFollowList(Long id, Pageable pageable) {

        return relationShipRepo.findFollowList(id,pageable);
    }

    public List<Member> getFansList(Member member, Pageable pageable) {
        List<Member> fansList = relationShipRepo.findFansList(member.getId(), pageable);
        fansList.forEach(x->{
            Integer relation = getRelation(member, x.getId());
            x.setRelation(relation);
        });
        return fansList;
    }

    public void unblock(Long from, Long to) {
        BlackList blackList=blackListRepo.findByBlockingId(from,to);
        if (blackList!=null)  blackListRepo.delete(blackList);
    }

    public void block(Long from, Long to) {
        RelationType relation = relation(from, to);
        if (relation==null||relation.getId()==RelationType.blocking.getId()|| relation.getId()!=RelationType.hater.getId()){
            List<RelationShip> ls = relationShipRepo.findByMemberId(from);
            ls.forEach(x->{
                relationShipRepo.delete(x);
            });
            BlackList blackList=new BlackList();
            blackList.setBlockerId(from);
            blackList.setBlockingId(to);
            blackList.setCreateAt(new Date());
            blackList.setUpdateAt(new Date());
            blackListRepo.saveAndFlush(blackList);
        }

    }

    public List<Member> getBlackList(Long id, Pageable pageable) {
        return blackListRepo.findBlackList(id,pageable);
    }

    public RelationType relation(Long a, Long b){
        List<BlackList> bl = blackListRepo.findByFromAndTo(a, b);
        switch (bl.size()){
            case 2:return  RelationType.hater;
            case 1:
                BlackList blackList = bl.get(0);
                if (blackList.getBlockerId()!=a.longValue()) return RelationType.blocked;
                else return RelationType.blocking;
            case 0:
                List<RelationShip> rl= relationShipRepo.findByFromAndTo(a,b);
                switch (rl.size()){
                    case 2: return RelationType.friend;
                    case 1:
                        RelationShip relationShip = rl.get(0);
                        if (relationShip.getFollowerId()!=a.longValue()) return RelationType.followed;
                        else return RelationType.following;
                    case 0: return RelationType.none;
                }
        }
        return null;
    }


    public Integer getRelation(Member member, Long mid) {
        RelationType relation = this.relation(member.getId(), mid);

        return relation.getId();
    }
}