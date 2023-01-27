package com.hee.muzihee.musical.dao;


import com.hee.muzihee.common.paging.SelectCriteria;
import com.hee.muzihee.musical.dto.MusicalDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MusicalMapper {

    List<MusicalDto> selectMusicalList(); // 메인페이지에 띄울 리스트

    /* Admin */
    int selectMusicalTotalForAdmin(); // 전체 갯수 조회 ( 끝난 공연 포함 )

    List<MusicalDto> selectMusicalListWithPagingForAdmin(SelectCriteria selectCriteria); // 페이징처리 된 리스트 출력

    MusicalDto selectMusicalDetailForAdmin(String musicalCode);

    /* All */
    int selectMusicalTotal(); // 전체 갯수 조회

    List<MusicalDto> selectOriginMusicalListWithPaging(SelectCriteria selectCriteria);
    int selectOriginMusicalTotal();

    List<MusicalDto> selectCreativeMusicalListWithPaging(SelectCriteria selectCriteria);
    int selectCreativeMusicalTotal();

    List<MusicalDto> selectFamilyMusicalListWithPaging(SelectCriteria selectCriteria);
    int selectFamilyMusicalTotal();

    MusicalDto selectMusicalDetail(String musicalCode);

    int insertMusical(MusicalDto musicalDto);

    int updateMusical(MusicalDto musicalDto);

    int deleteMusical(String musicalCode);

}
