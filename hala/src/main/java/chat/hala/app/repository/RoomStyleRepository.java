package chat.hala.app.repository;


import chat.hala.app.entity.RoomStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomStyleRepository  extends JpaRepository<RoomStyle,Long> {



}
