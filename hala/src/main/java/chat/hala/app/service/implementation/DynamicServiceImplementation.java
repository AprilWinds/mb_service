package chat.hala.app.service.implementation;

        import chat.hala.app.entity.*;
        import chat.hala.app.library.util.*;
        import chat.hala.app.repository.*;
        import chat.hala.app.entity.Dynamic;
        import chat.hala.app.restful.exception.ConflictException;
        import chat.hala.app.restful.exception.NotFoundException;
        import chat.hala.app.restful.exception.PreconditionFailedException;
        import chat.hala.app.restful.wrapper.CommentWithLocate;
        import chat.hala.app.restful.wrapper.DynamicWithLocate;
        import chat.hala.app.service.DynamicService;
        import chat.hala.app.service.exception.ObjectAlreadyUpdateException;
        import chat.hala.app.service.exception.ObjectNotFoundException;
        import com.vividsolutions.jts.geom.Coordinate;
        import com.vividsolutions.jts.geom.GeometryFactory;
        import com.vividsolutions.jts.geom.Point;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.data.domain.Pageable;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;

        import java.text.ParseException;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

        import static chat.hala.app.library.util.General.getAgeByBirth;

@Service
public class DynamicServiceImplementation  implements DynamicService {



    @Autowired
    private DynamicRepository dRepo;

    @Autowired
    private MemberRepository mRepo;

    @Autowired
    private StarRepository sRepo;

    @Autowired
    private CommentRepository cRepo;

    @Autowired
    private DynamicReportRepository drRepo;

    @Autowired
    private CloudUtils cu;

    @Autowired
    private RelationshipRepository rsRepo;

    @Autowired
    private SystemNoticeRepository snRepo;

    @Autowired
    private MemberServiceImplementation msImpl;

    @Transactional
    @Override
    public Object addDynamic(Member member, DynamicWithLocate dynamicWithLocate, String language) throws PreconditionFailedException {
        if ((dynamicWithLocate.getDynamic().getImgUrl()==null)&&(dynamicWithLocate.getDynamic().getContent()==null)){
            throw  new PreconditionFailedException(ErrorMsg.NO_PICTURE_OR_TEXT);
        }

        Dynamic dynamic=dynamicWithLocate.getDynamic();
        Coordinate locate = dynamicWithLocate.getLocate();
        GeometryFactory geoFactory = new GeometryFactory();
        Point point = geoFactory.createPoint(locate);
        member.setLocate(point);
        mRepo.saveAndFlush(member);
        dynamic.setLocate(point);dynamic.setMemberId(member.getId());
        dynamic.setTime(new Date());dynamic.setReadNumber(0);
        dynamic.setStar(0);dynamic.setIsPraise(0);
        dynamic.setCommentNumber(0);
        Dynamic newDynamic = dRepo.saveAndFlush(dynamic);
        memberInfo(newDynamic,member);
        List<String> ids = rsRepo.findFanCharacterIds(newDynamic.getMemberId());
        String[] ss= new String[ids.size()];
        String[] target = ids.toArray(ss);
        String notice = null;
        try {
            notice = this.swtichLangAndSendMsg(member.getNickname(), language, " post moment:", ":نشر منشور ",newDynamic.getContent(),target,"{\"type\":1}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String finalNotice = notice;
        ids.forEach(x->{
            Member receive= mRepo.findByCharacterId(x);
            SystemNotice systemNotice = insertNotice(finalNotice, member.getId(), newDynamic.getId(), receive.getId());
            snRepo.saveAndFlush(systemNotice);
        });

        return Http.standardResponse(newDynamic);
    }

    @Override
    public Object findDynamicByFollow(Long id, Pageable pageable) {
        List<Dynamic> ls = dRepo.findAllByFollow(id,pageable);
        ls.forEach(x->{
            x.setIsPraise(sRepo.findMemberStarState(x.getId(),id));
            try {
                x.setAge(getAgeByBirth(x.getAge()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        return Http.standardResponse(ls,200,"success");
    }


    @Transactional
    @Override
    public Object findNearbyDynamic(Long id, Double lng, Double lat, Pageable pageable) {
        StringBuilder sb=new StringBuilder("POINT(");
        StringBuilder locate = sb.append(lng.toString()).append(" ").append(lat.toString()).append(")");
        mRepo.triggerUpdateLocate(locate.toString(), id);
        List<Dynamic> ls=dRepo.findNearbyDynamic(id,lng,lat,pageable.getOffset(),pageable.getPageSize());
        ls.forEach(x->{
            Member one = mRepo.findOne(x.getMemberId());
            memberInfo(x,one);
            Point point= x.getLocate();
            x.setDistance(distance(point.getX(),point.getY(),lng,lat));
            x.setIsPraise(sRepo.findMemberStarState(x.getId(),id));
        });

        return  Http.standardResponse(ls,200, "success");
    }

    @Override
    public Object addDynamicReport(Member member, DynamicReport dynamicReport) throws ObjectNotFoundException {
        Long dynamicId = dynamicReport.getDynamicId();
        Dynamic one = dRepo.findOne(dynamicId);
        if (one == null) {throw  new ObjectNotFoundException(ErrorMsg.NO_DYNAMIC);}
        dynamicReport.setCreatedAt(new Date());
        dynamicReport.setUpdatedAt(new Date());
        dynamicReport.setReportId(member.getId());
        dynamicReport.setContent(one.getContent());
        dynamicReport.setImgUrl(one.getImgUrl());
        dynamicReport.setReportMemberId(one.getMemberId());

        drRepo.saveAndFlush(dynamicReport);
        return Http.standardResponse();
    }


    @Transactional
    @Override
    public Object star(Long from, Long dynamicId, String language) throws ObjectNotFoundException{
        Dynamic dynamic = dRepo.findOne(dynamicId);
        if (dynamic == null){
            throw new ObjectNotFoundException(ErrorMsg.NO_DYNAMIC);
        }
        Star star=sRepo.findStarByDynamicId(dynamicId,from);
        if (star==null){
            Star s=new Star();
            s.setDynamicId(dynamicId);
            s.setFromId(from);
            s.setToId(dynamic.getMemberId());
            s.setTime(new Date());
            s.setState(1);
            sRepo.saveAndFlush(s);
            dynamic.setStar(dynamic.getStar()+1);
            dRepo.saveAndFlush(dynamic);
            Member fromMember=mRepo.findOne(from);
            Member toMember = mRepo.findOne(dynamic.getMemberId());
            if (from.longValue()!=toMember.getId()){
                String[] target = {String.valueOf(toMember.getCharacterId())};
                String notice= null;
                try {
                    notice = swtichLangAndSendMsg(fromMember.getNickname(),language," like your moment"," معجب بلحظاتك","",target,"{\"type\":0}");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SystemNotice systemNotice = insertNotice(notice, from, dynamicId, dynamic.getMemberId());
                snRepo.saveAndFlush(systemNotice);
            }
            return  Http.standardResponse(1);//点赞成功
        }else{
            if (star.getState()==1){
                dynamic.setStar((dynamic.getStar()-1<0)?0:(dynamic.getStar()-1));
                dRepo.saveAndFlush(dynamic);
                star.setState(0); star.setTime(new Date());
                sRepo.saveAndFlush(star);
                return Http.standardResponse(0);//取消点赞
            } else {
                star.setState(1);star.setTime(new Date());
                sRepo.saveAndFlush(star);
                dynamic.setStar(dynamic.getStar()+1);
                dRepo.saveAndFlush(dynamic);
                return Http.standardResponse(1);//点赞成功
            }
        }
    }


    @Transactional
    @Override
    public Object cancelStar(Long fromId, Long dynamicId) throws ObjectNotFoundException, ObjectAlreadyUpdateException, ConflictException {
        if (dRepo.findOne(dynamicId)==null){
            throw new ObjectNotFoundException(ErrorMsg.NO_DYNAMIC);
        }
        Star s = sRepo.findStarByDynamicId(dynamicId,fromId);
        if (s==null){
            throw new  ConflictException(ErrorMsg.NO_STAR);
        }else if (s.getState()==0){
            throw  new ObjectAlreadyUpdateException(ErrorMsg.ALREADY_STAR_CANCEL);
        }
        sRepo.updateStarByStateReduce(fromId,dynamicId,new Date());
        dRepo.updateDynamicByStarReduce(dynamicId);
        return Http.standardResponse(200,"success");
    }

    @Transactional
    @Override
    public Object addComment(Member member, CommentWithLocate commentWithLocate, String language) throws ObjectNotFoundException {
        Comment comment = commentWithLocate.getComment();
        Dynamic dynamic = dRepo.findOne(comment.getDynamicId());
        if(dynamic==null) {
            throw new ObjectNotFoundException(ErrorMsg.NO_DYNAMIC);
        }
        Coordinate locate = commentWithLocate.getLocate();
        GeometryFactory factory=new GeometryFactory();
        Point point = factory.createPoint(locate);
        member.setLocate(point);
        mRepo.saveAndFlush(member);

        comment.setTime(new Date());
        comment.setFromId(member.getId());
        comment.setLocate(point);
        cRepo.saveAndFlush(comment);

        dynamic.setCommentNumber(dynamic.getCommentNumber()+1);
        dRepo.saveAndFlush(dynamic);
        Member toMember = mRepo.findOne(dynamic.getMemberId());
        if (member.getId().longValue()!=toMember.getId()){
        String[] target={toMember.getCharacterId()};
            String notice = null;
            try {
                notice = this.swtichLangAndSendMsg(member.getNickname(), language, " comment: ", ": علق", dynamic.getContent(),target,"{\"type\":0}");
            } catch (Exception e) {
                e.printStackTrace();
            }
            SystemNotice systemNotice = insertNotice(notice,member.getId(), dynamic.getId(),dynamic.getMemberId());
        snRepo.saveAndFlush(systemNotice);
        }
        return Http.standardResponse(200, "success");
    }

    @Override
    public Object findPageComments(Member member, Long dynamicId, Integer page, Integer pageSize, Double lat, Double lng) throws ObjectNotFoundException {
        Dynamic dynamic=dRepo.findOne(dynamicId);
        if (dynamic==null){
            throw new ObjectNotFoundException(ErrorMsg.NO_DYNAMIC);
        }
        List<Comment> ls = cRepo.findAllByDynamicId(dynamicId, page *pageSize, pageSize,  lat, lng);
        for (Comment x:ls){
            Member from = mRepo.getOne(x.getFromId());
            x.setFromName(from.getNickname());
            x.setFromAvatar(from.getAvatarUrl());
            x.setFromGender(String.valueOf(from.getGender()));
            x.setFromCharctetrId(from.getCharacterId());
            try {
                x.setFromAge(getAgeByBirth(from.getDob()));
            } catch (ParseException e) {
                x.setFromAge("");
            }
            List<Comment> byParentId = cRepo.findByParentId(x.getId(),lat,lng);
            if (byParentId.size()!=0) {
                byParentId.forEach(s -> {
                    Member sfrom = mRepo.getOne(s.getFromId());
                    Member sto = mRepo.getOne(s.getToId());
                    s.setFromName(sfrom.getNickname());
                    s.setFromGender(String.valueOf(s.getFromGender()));
                    s.setFromAvatar(sfrom.getAvatarUrl());
                    s.setFromCharctetrId(sfrom.getCharacterId());
                    String sfromDob = sfrom.getDob();
                    s.setToName(sto.getNickname());
                    s.setToAvatar(sto.getAvatarUrl());
                    s.setToCharacterId(sto.getCharacterId());
                    if (sfromDob==null) s.setFromAge("");
                    else {
                        try {
                            s.setFromAge(getAgeByBirth(sfromDob));
                        } catch (ParseException e) {
                            s.setFromAge("");
                        }
                    }

                });
                x.setComments(byParentId);
            }
        }
        return  Http.standardResponse(ls,200, "success");
    }


    @Override
    public Object findDynamicById(Long dynamicId, Member member) {
        Dynamic dynamic = dRepo.findOne(dynamicId);
        if (dynamic!=null){
            Member owner = mRepo.findOne(dynamic.getMemberId());
            memberInfo(dynamic,owner);
            dynamic.setRelation((member.getId().longValue()==owner.getId())?null:msImpl.getMemberRelation(member,owner));
            dynamic.setIsPraise(sRepo.findMemberStarState(dynamicId,member.getId()));
            if(dynamic.getMemberId().longValue()!=member.getId()) {
                dynamic.setReadNumber(dynamic.getReadNumber()+1);
                dRepo.saveAndFlush(dynamic);
            }
        }
        return Http.standardResponse(dynamic,200, "success");
    }




    @Transactional
    @Override
    public Object updateDynamicPrivilege(Long dynamicId, Member member, Dynamic.Type type) throws NotFoundException {
        Dynamic one = dRepo.findOne(dynamicId);
        if (one == null){ throw new NotFoundException(ErrorCode.NO_DYNAMIC.getMsg());}
        if (one.getMemberId().longValue()!=member.getId()){return  Http.standardResponse(ErrorCode.No_DYNAMIC_UPDATE_PRIVILEGE.getStatus(),ErrorCode.No_DYNAMIC_UPDATE_PRIVILEGE.getMsg());}
        one.setType(type);
        dRepo.saveAndFlush(one);
        return Http.standardResponse();
    }


    static void  memberInfo(Dynamic one, Member m){
        try {
            one.setAge(General.getAgeByBirth(m.getDob()));
        } catch (ParseException e) {
            one.setAge("");
        }
        one.setGender(m.getGender());
        one.setAvatarUrl(m.getAvatarUrl());
        one.setNickname(m.getNickname());
        one.setCharacterId(m.getCharacterId());
    }

    private Double distance(Double x,Double y ,Double lng, Double lat) {
     //   String[] s = locate.substring(6, locate.length()-1).split(" ");
        Double x1=x*Math.PI/180.0;
        Double y1=y*Math.PI/180.0;
        Double x2=lng*Math.PI/180.0;
        Double y2=lat*Math.PI/180.0;

        Double a=y1-y2;
        Double b=x1-x2;

        Double rs = 2 * Math.asin(Math.sqrt(Math.abs(Math.pow(Math.sin(a/2),2) +
                Math.cos(y1)*Math.cos(y2)*Math.pow(Math.sin(b/2),2))));

        rs*=6378.137;
        rs=Math.round(rs*10000d)/10000d;

        return rs;
    }

    static SystemNotice insertNotice(String sb, Long memberId , Long dynamicId ,Long receiveId){
        SystemNotice sn=new SystemNotice();
        sn.setIsRead(0);
        sn.setTime(new Date());
        sn.setMsg(sb);
        sn.setDynamicId(dynamicId);
        sn.setMemberId(memberId);
        sn.setReceiveId(receiveId);
        return sn;
    }

    String  swtichLangAndSendMsg(String nickname,String language,String en ,String arabic,String end,String[] target,String extra) throws Exception {

        Map<String ,String> map=new HashMap<>();
        StringBuilder sb=new StringBuilder("");
        if (language.equals(Constant.LAN_ARAB)){
            sb.append(arabic);
        }else{
            sb.append(en);
        }
        sb.append(end);
        StringBuilder s1=new StringBuilder(nickname);
        s1.append(sb.toString());
        cu.systemNotice(s1.toString(),target,extra);
        return sb.toString();
    }
}
