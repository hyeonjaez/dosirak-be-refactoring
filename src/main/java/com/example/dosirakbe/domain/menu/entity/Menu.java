package com.example.dosirakbe.domain.menu.entity;

import com.example.dosirakbe.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;

/**
 * packageName    : com.example.dosirakbe.domain.menu.entity<br>
 * fileName       : Menu<br>
 * author         : yyujin1231<br>
 * date           : 10/25/24<br>
 * description    : 메뉴 정보를 저장하는 엔티티 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/25/24        yyujin1231                최초 생성<br>
 */


@Table(name = "menus")
@Entity
@Getter
public class Menu {

    /**
     * 메뉴의 고유 식별자입니다.
     *
     * <p>
     * 이 필드는 메뉴를 유일하게 식별하기 위한 ID입니다.
     * </p>
     */

    @Id
    @Column(name = "menu_id",unique = true,updatable = false)
    private Long menuId;

    /**
     * 메뉴의 이름입니다.
     *
     * <p>
     * 이 필드는 메뉴의 이름을 나타냅니다.
     * </p>
     */

    @Column(name = "menu_name")
    private String menuName;

    /**
     * 메뉴의 이미지를 나타냅니다.
     *
     * <p>
     * 이 필드는 메뉴의 이미지를 저장하는 URL 경로를 포함합니다.
     * </p>
     */

    @Column(name = "menu_img")
    private String menuImg;

    /**
     * 메뉴의 가격입니다.
     *
     * <p>
     * 이 필드는 메뉴의 가격 정보를 포함합니다.
     * </p>
     */

    @Column(name = "menu_price")
    private Integer menuPrice;

    /**
     * 다회용기 추천용량입니다.
     *
     * <p>
     * 이 필드는 GPT API를 활용하여 반환된 추천용량값을 나타냅니다.
     * </p>
     */

    @Column(name = "menu_pack_size")
    private String menuPackSize;

    /**
     * 메뉴가 속한 가게 정보입니다.
     *
     * <p>
     * 이 필드는 {@link Store} 엔티티와의 다대일(N:1) 관계를 나타내며, 메뉴가 특정 가게에 소속되어 있음을 나타냅니다.
     * </p>
     */

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;


}
