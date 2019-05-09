package wings.app.microblog.service;

import com.google.common.base.Strings;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wings.app.microblog.Exception.BlockedException;
import wings.app.microblog.Exception.RepetitionException;
import wings.app.microblog.entity.Member;
import wings.app.microblog.repository.MemberRepository;
import wings.app.microblog.util.ErrorCode;
import wings.app.microblog.util.General;
import wings.app.microblog.util.Identifier;
import java.util.Date;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private MemberRepository memberRepo;



    public Member verifyToken(String token) {
        if (Strings.isNullOrEmpty(token)){
            return null;
        }
        Long characterId=null;
        try {
         characterId  = Long.parseLong(Jwts.parser().setSigningKey(General.privateKey).parseClaimsJws(token).getBody().getSubject());
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String characterIdInStr = String.format("%08d", characterId);
        return memberRepo.findByCharacterId(characterIdInStr);
    }

    public void  createToken(Member member) {
        assert member != null;
        String compact = Jwts.builder().setSubject(String.valueOf(member.getCharacterId())).setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256, General.privateKey).compact();
        member.setAppToken(compact);
    }

    public Member createMember(Member member) throws RepetitionException {
        Member m = memberRepo.findByUsername(member.getUsername());
        if (m!=null){
            throw new RepetitionException(ErrorCode.USERNAME_REPETITION.msg);
        }
        Identifier idg = new Identifier(8, "", true);
        member.setIsActive(1);
        member.setCreatedAt(new Date());
        member.setUpdatedAt(new Date());
        member.setNickname(String.valueOf(new Date()).substring(0,7));
        member.setCharacterId(idg.generate());
        String introduction = member.getIntroduction();
        member.setIntroduction(introduction==null?"....":introduction);
        return memberRepo.saveAndFlush(member);
    }

    public Member checkMember(Member member) {
        return memberRepo.findByUsernameAndPassword(member.getUsername(),member.getPassword());
    }


}
