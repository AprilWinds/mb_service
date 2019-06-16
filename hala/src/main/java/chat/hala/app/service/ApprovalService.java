package chat.hala.app.service;

public interface ApprovalService {

    String  checkApprovalState(Long id);

    void insertApproval(String type, Long id);

}
