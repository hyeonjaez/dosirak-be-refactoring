package com.example.dosirakbe.domain.store.entity;

import com.example.dosirakbe.domain.menu.entity.Menu;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * packageName    : com.example.dosirakbe.domain.store.entity<br>
 * fileName       : Store<br>
 * author         : yyujin1231<br>
 * date           : 10/25/24<br>
 * description    : 가게 관련 정보를 처리하는 엔티티 클래스 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/25/24        yyujin1231                최초 생성<br>
 */


@Table(name = "stores")
@Entity
@Getter
@Setter
@AllArgsConstructor
public class Store {

    /**
     * 가게의 고유 식별자입니다.
     *
     */

    @Id
    @Column(name = "store_id",unique = true,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    /**
     * 가게의 이름입니다.
     *
     */

    @Column(name = "store_name", nullable = false)
    private String storeName;

    /**
     * 가게의 전화번호입니다.
     *
     */

    @Column(name = "tel_number", nullable = false)
    private String telNumber;

    /**
     * 가게의 주소입니다.
     *
     */

    @Column(name = "address", nullable = false)
    private String address;

    /**
     * 가게의 운영시간입니다.
     *
     */

    @Column(name = "operation_time", nullable = false)
    private String operationTime;

    /**
     * 가게의 카테고리입니다.
     *
     */

    @Column(name = "store_category")
    private String storeCategory;

    /**
     * 가게의 이미지입니다.
     *
     * <p>
     * 이 필드는 가게의 이미지 URL을 나타냅니다.
     * </p>
     */


    @Column(name = "store_img", nullable = false)
    private String storeImg;

    /**
     * 가게의 경도입니다.
     *
     * <p>
     * 이 필드는 가게의 X좌표(경도)를 나타냅니다.
     * </p>
     */

    @Column(name = "map_x", nullable = false)
    private double mapX;

    /**
     * 가게의 위도입니다.
     *
     * <p>
     * 이 필드는 가게의 Y좌표(위도)를 나타냅니다.
     * </p>
     */



    @Column(name = "map_y", nullable = false)
    private double mapY;

    /**
     * 가게의 다회용기 혜택여부 입니다.
     *
     */

    @Column(name = "if_valid", nullable = false)
    private String ifValid;

    /**
     * 가게의 리워드 혜택여부 입니다.
     *
     */

    @Column(name = "if_reward", nullable = false)
    private String ifReward;

    /**
     * 가게에 속한 메뉴 리스트입니다.
     * <p>
     * 이 필드는 가게와 메뉴 간의 1:N 관계를 나타냅니다.
     * </p>
     */

    @OneToMany(mappedBy = "store")
    private List<Menu> menus;

    /**
     * 운영 시간을 변환하여 반환합니다.
     * <p>
     * 운영 시간 문자열을 JSON 형식으로 파싱하여 Map의 리스트로 변환합니다.
     * </p>
     * @return 변환된 운영 시간 데이터
     * @throws JsonProcessingException JSON 파싱 중 오류가 발생한 경우
     */

    public List<Map<String, String>> changeOperationTime() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(operationTime, new TypeReference<List<Map<String, String>>>() {});
    }

    public Store() {
    }





}
