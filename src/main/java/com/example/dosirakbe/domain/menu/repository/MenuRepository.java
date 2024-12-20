package com.example.dosirakbe.domain.menu.repository;

import com.example.dosirakbe.domain.menu.entity.Menu;
import com.example.dosirakbe.domain.track.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.menu.repository<br>
 * fileName       : MenuRepository<br>
 * author         : yyujin1231<br>
 * date           : 10/25/24<br>
 * description    : 메뉴 엔티티에 대한 데이터 접근을 담당하는 리포지토리 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/25/24        yyujin1231                최초 생성<br>
 */

@Repository
public interface MenuRepository  extends JpaRepository<Menu, Long> {

    /**
     * 특정 가게에 속한 메뉴 목록을 조회합니다.
     *
     * @param storeId   검색할 가게 이름 키워드
     * @return          해당 가게에 속한 {@link Menu} 메뉴 리스트
     */

    List<Menu> findByStore_StoreId(Long storeId);
}