package chat.hala.app.service;

import chat.hala.app.entity.Member;
import chat.hala.app.service.exception.*;

/**
 * Created by astropete on 2017/12/25.
 */

public interface AccountService {
    public String issueToken(Member member, String language);
    public Member verifyToken(String token) throws InvalidCredentialException;
    public void sendSmsCode(String mobileNumber) throws PreconditionNotQualifiedException, ThirdPartyServiceException;
    public Member signup(Member member, String code) throws RongCloudException, PreconditionNotQualifiedException, ObjectAlreadyExistedException, ObjectIllegalException;
    public Member signin(String reference, String password) throws ObjectNotFoundException;
   /* public Member thirdPartySignIn(String from, String outId) throws Exception;
    public Member thirdPartySignUp(String from, Member newedMember) throws Exception;*/
    public Member getMemberByReference(String reference);


    Member thirdSigIn(String from, String thirdId);

    Member thirdSignUp(String from, String m, String thirdToken) throws ThirdPartyServiceException, RongCloudException;
}
