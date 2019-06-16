package chat.hala.app.repository;

import chat.hala.app.entity.MemberSetting;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by astropete on 2018/2/24.
 */
public interface MemberSettingRepository extends JpaRepository<MemberSetting, Long> {
    public MemberSetting findByMemberId(Long memberId);
}
