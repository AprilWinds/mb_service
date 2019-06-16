package chat.hala.app.service.implementation;

import chat.hala.app.entity.Fonding;
import chat.hala.app.library.util.Constant;
import chat.hala.app.repository.FondingRepository;
import chat.hala.app.service.FondingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by astropete on 2018/1/7.
 */

@Service
public class FondingServiceImplementation implements FondingService {

    @Autowired
    private FondingRepository fondingRepo;

    @Override
    public Fonding createFonding(Fonding fonding){
        fonding.setSystem(false);
        fonding.setUsedCount(0);
        fonding.setCreatedAt(new Date());
        fonding.setUpdatedAt(new Date());
        return fondingRepo.saveAndFlush(fonding);
    }

    @Override
    public Page<Fonding> findFondingByParentId(Long parentId, Pageable pageable, String language){
        Page<Fonding> pfs = fondingRepo.findByParentId(parentId, pageable);
        for(Fonding f : pfs.getContent()){
            if(language.equals(Constant.LAN_ARAB)){
                f.setName(f.getArabicName());
            }
            f.setArabicName(null);
        }
        return pfs;
    }
}
