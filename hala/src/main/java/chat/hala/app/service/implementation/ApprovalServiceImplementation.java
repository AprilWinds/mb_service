package chat.hala.app.service.implementation;

import chat.hala.app.entity.Approval;
import chat.hala.app.entity.Room;
import chat.hala.app.repository.ApprovalRepository;
import chat.hala.app.repository.RoomRepository;
import chat.hala.app.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ApprovalServiceImplementation implements ApprovalService {


    @Autowired
    private ApprovalRepository  aRepo;

    @Autowired
    private RoomRepository rRepo;

    @Override
    public String checkApprovalState(Long id) {

        String s="login";
        Approval  memberState= aRepo.findMemberByState(id);

        if (memberState!=null&&memberState.getState()==1){
            s="Person>"+memberState.getDescription()+"; ";
        }

        StringBuffer rs=new StringBuffer("Room>");
        List<Room> owner = rRepo.findByOwnerId(id);
        if (owner!=null&&owner.size()!=0){
            for (Room room:owner){
                Approval roomState = aRepo.findRoomByState(room.getId());
                if (roomState!=null&&roomState.getState()==1) {
                    rs.append (room.getName() + "-" + roomState.getDescription() + ",");
                }
            }
        }

        if (!rs.toString().equals("Room>")) {
            if (s.equals("login")){s=rs.substring(0,rs.length()-1);}
            else{s+=rs.substring(0,rs.length()-1);}
        }
        return  s;
    }




    @Override
    public void insertApproval(String type, Long id) {
        if (type.equals("member")){
            aRepo.insertApprovalByMember(id);
        }else{
            aRepo.insertApprovalByRoom(id);
        }
    }


}
