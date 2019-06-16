package chat.hala.app.service.implementation;

import chat.hala.app.entity.Member;
import chat.hala.app.entity.MemberSetting;
import chat.hala.app.library.Identifier;
import chat.hala.app.library.RongCloud;
import chat.hala.app.library.VerificationCode;
import chat.hala.app.library.YunpianSMS;
import chat.hala.app.library.util.*;
import chat.hala.app.repository.MemberRepository;
import chat.hala.app.repository.MemberSettingRepository;
import chat.hala.app.repository.RelationshipRepository;
import chat.hala.app.service.AccountService;
import chat.hala.app.service.RelationshipService;
import chat.hala.app.service.exception.*;
import chat.hala.app.service.library.TaskHandler;
import com.google.common.base.Strings;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by astropete on 2017/12/25.
 */

@Service
public class AccountServiceImplementation implements AccountService {

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private RelationshipRepository relationRepo;

    @Autowired
    private MemberSettingRepository settingRepo;

    @Autowired
    private TaskHandler taskHandler;

    @Autowired
    private RelationshipService  relationshipService;

    @Override
    public String issueToken(Member member, String language){
        assert member != null;
        String compact = Jwts.builder().setSubject(String.valueOf(member.getCharacterId())).setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256, Constant.getPrivateKey()).compact();
        member.setApToken(compact);
        Member m = memberRepo.saveAndFlush(member);

        try {
            relationshipService.followMember(m, Long.valueOf(1),language);
        } catch (PreconditionNotQualifiedException e) {
            System.out.println("follower fails");
        }
        return compact;

    }

    @Override
    public Member verifyToken(String token) throws InvalidCredentialException {
        if (Strings.isNullOrEmpty(token)) {
            throw new InvalidCredentialException(ErrorMsg.TOKEN_ERR);
        }
        Member member = null;
        try {

            Long characterId = Long.parseLong(Jwts.parser().setSigningKey(Constant.getPrivateKey()).parseClaimsJws(token).getBody().getSubject());
            String characterIdInStr = String.format("%08d", characterId);
            member = memberRepo.findByCharacterIdAndIsActiveTrue(characterIdInStr);
        } catch (Exception e) {
            throw new InvalidCredentialException(ErrorMsg.TOKEN_ERR);
        }
        if (member == null||(Strings.isNullOrEmpty(member.getApToken()))||(!member.getApToken().equals(token))) throw new InvalidCredentialException(ErrorMsg.TOKEN_ERR);

        return member;
    }


    @Override
    public void sendSmsCode(String mobile) throws PreconditionNotQualifiedException, ThirdPartyServiceException{
        String mobilePattern = "^\\+[0-9]+$";
        Pattern mp = Pattern.compile(mobilePattern);
        Matcher m = mp.matcher(mobile);
        if(!m.find()) throw new PreconditionNotQualifiedException(ErrorMsg.MOBILE_FORMAT_ERR);
        YunpianSMS sender = new YunpianSMS();
        VerificationCode coder = new VerificationCode(mobile);
        String code = coder.getCurrentCode();
        sender.setMobile(mobile);
        sender.setContent("【Hala】"+ code +" is your verification code.");
        if(!sender.send()) throw new ThirdPartyServiceException(ErrorMsg.YUNPIAN_ERR);
        sender.destroy();
    }

    @Override
    public Member signup(Member member, String code) throws RongCloudException, PreconditionNotQualifiedException, ObjectAlreadyExistedException, ObjectIllegalException{
        VerificationCode coder = new VerificationCode(member.getMobileNumber());
        if(!coder.getCurrentCode().equals(code) && !coder.getLastPeriodCode().equals(code) && !code.equals(Constant.getTestCode())) throw new PreconditionNotQualifiedException(ErrorMsg.CODE_NOT_MATCH);
        if(member.getNickname().matches("(?i:.*HALACHAT.*)")) throw new ObjectIllegalException(ErrorMsg.NICKNAME_SENSITIVE);
        Member existed = memberRepo.findByMobileNumberAndIsActiveTrue(member.getMobileNumber());
        if(existed != null) throw new ObjectAlreadyExistedException(ErrorMsg.MEMBER_EXISTED);
        member.setPassword(Encryption.MD5ed(member.getPassword()));
        this.generalSignUp(member);
        String token = "";
        try {
            RongCloud r = new RongCloud();
            token = r.getToken(member).toString();
        } catch (Exception e){
            throw new RongCloudException();
        }
        member.setRongToken(token);
        Member re = memberRepo.saveAndFlush(member);
        this.addSetting(re);
        taskHandler.completeTask(re, "phone");
        return re;
    }

    @Override
    public Member signin(String reference, String password) throws ObjectNotFoundException{
        Member member = memberRepo.findMemberSignIn(reference, "%"+reference, Encryption.MD5ed(password));
        if(member != null){
            this.generalSignIn(member);
            memberRepo.saveAndFlush(member);
        }else{
            throw new ObjectNotFoundException(ErrorMsg.NO_MEMBER);
        }
        return member;
    }

   /*@Override
    public Member thirdPartySignIn(String from, String outToken) throws Exception{
        Member m = null;
        if(from.equals("facebook")){
            Map<String, String> fb = Http.getFacebookMember(outToken);
            m = memberRepo.findByFacebookIdAndIsActiveTrue(fb.get("fid"));
            if(m == null){
                m = new Member();
                m.setFacebookId(fb.get("fid"));
                m.setNickname(fb.get("name"));
            }
        }
        if(m.getId() != null){
            this.generalSignIn(m);
            memberRepo.saveAndFlush(m);
        }
        return m;
    }

    @Override
    public Member thirdPartySignUp(String from, Member newedMember) throws Exception{
        Member m = new Member();
        if(from.equals("facebook")) m.setFacebookId(newedMember.getFacebookId());
        this.generalSignUp(m);
        this.setDefaultEssentialParam(m);
        m.setNickname(newedMember.getNickname());
        Member re = memberRepo.saveAndFlush(m);
        this.addSetting(re);
        return re;
    }
    */

    @Override
    public Member getMemberByReference(String reference){
        return memberRepo.findByMemberReference(reference, "%"+reference);
    }

    private Member setDefaultEssentialParam(Member m){
       /* m.setNickname(m.getCharacterId());
        m.setAvatarUrl(Constant.getDefaultParamString());
        m.setDob(Constant.getDefaultParamString());
        m.setGender(Member.Gender.unknown);*/
        //m.setDob(Constant.getDefaultParamString());
        m.setDob("2018-01-01");
        m.setNickname(UUID.randomUUID().toString().substring(0,7));
        m.setAvatarUrl(Constant.getDefaultParamString());
        m.setGender(Member.Gender.male);
        m.setAffectiveState(Member.AffectiveState.Secrecy);
        m.setIntroduction("");
       return m;
    }

    public void generalSignUp(Member member){
        Identifier idg = new Identifier(8, "", true);
        member.setCreatedAt(new Date());
        member.setLastActive(new Date());
        member.setOnline(true);
        member.setCoins(0);
        member.setWealth(0);
        member.setSpent(0);
        member.setCharacterId(idg.generate());
        member.setActive(true);
    }

    private void addSetting(Member member){
        MemberSetting s = new MemberSetting();
        s.setMemberId(member.getId());
        s.setDistance(true);
        s.setLive(true);
        s.setPush(7);
        s.setCreatedAt(new Date());
        s.setUpdatedAt(new Date());
        settingRepo.saveAndFlush(s);
    }

    private void generalSignIn(Member member){
        member.setLastActive(new Date());
        member.setOnline(true);
        member.setFollowingCount(relationRepo.findCountOfFollowing(member.getId()));
        member.setFanCount(relationRepo.findCountOfFan(member.getId()));
    }

    @Override
    public Member thirdSigIn(String from, String thirdId) {
        Member m = memberRepo.findByFromAndThirdId(thirdId,from);
        if (m!=null){this.generalSignIn(m);}
        return  m;
    }

    @Override
    public Member thirdSignUp(String from, String id, String thirdToken) throws ThirdPartyServiceException, RongCloudException {

        Member member=new Member();
        this.generalSignUp(member);
        this.setDefaultEssentialParam(member);
        member.setSource(from);
        member.setThirdId(id);
        try{
        if (from.equals("google")){
            Map<String, String> googleMember= Http.getGoogleMember(thirdToken);
            member.setAvatarUrl(googleMember.get("picture"));
            member.setNickname(googleMember.get("name"));
        }else if(from.equals("facebook")) {
            Map<String, String> facebookMember = Http.getFacebookMember(thirdToken);
            member.setNickname(facebookMember.get("name"));
        }else if (from.equals("instagram")){

        }
        } catch (Exception e) {
            throw new ThirdPartyServiceException(ErrorCode.THIRD_PARTY_ERROR.getMsg());
        }
        RongCloud r = new RongCloud();
        String  token = null;
        try {
            token = r.getToken(member).toString();
        } catch (Exception e) {
            throw new RongCloudException();
        }
        member.setRongToken(token);
        return  member;
    }

    /*  @Transactional
    @Override
    public Map<String,Member> third(String from, String thirdId, Double lng, Double lat) throws RongCloudException {
        String locate="POINT("+lng+" "+lat+")";
        Member m = memberRepo.findByFromAndThirdId(thirdId,from);
        if(m!=null){
            this.generalSignIn(m);
            memberRepo.triggerUpdateLocate(locate,m.getId());
            return memberRepo.saveAndFlush(m);
        }else{
            Member member = new Member();
            //this.generalSignUp(member);
            //this.setDefaultEssentialParam(member);
            member.setSource(from);
            member.setThirdId(thirdId);
            String token = "";
            try {
                RongCloud r = new RongCloud();
                token = r.getToken(member).toString();
            } catch (Exception e){
                throw new RongCloudException();
            }
            member.setRongToken(token);
            Member m1 = memberRepo.saveAndFlush(member);
            this.addSetting(m1);

            memberRepo.triggerUpdateLocate(locate,m1.getId());
            return m1;
        }
    }*/

}
