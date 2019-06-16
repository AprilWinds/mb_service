package chat.hala.app.service;

import chat.hala.app.entity.Fonding;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by astropete on 2018/1/7.
 */
public interface FondingService {
    public Fonding createFonding(Fonding fonding);
    public Page<Fonding> findFondingByParentId(Long parentId, Pageable pageable, String language);
}
