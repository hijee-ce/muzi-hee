package com.hee.muzihee.musical.controller;


import com.hee.muzihee.common.ResponseDto;
import com.hee.muzihee.common.paging.Pagenation;
import com.hee.muzihee.common.paging.ResponseDtoWithPaging;
import com.hee.muzihee.common.paging.SelectCriteria;
import com.hee.muzihee.musical.dto.MusicalDto;
import com.hee.muzihee.musical.service.MusicalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class MusicalController {

    private final MusicalService musicalService;

    public MusicalController(MusicalService musicalService) {
        this.musicalService = musicalService;
    }

    @GetMapping("/main")
    public ResponseEntity<ResponseDto> selectMusicalList(){

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "메인페이지 조회 성공", musicalService.selectMusicalList()));
    }

    @GetMapping("/musicals/origin")
    public ResponseEntity<ResponseDto> selectOriginMusicalListWithPaging(@RequestParam(name="offset", defaultValue = "1") String offset){

        int totalCount = musicalService.selectOriginMusicalTotal();
        int limit = 5;
        int buttonAmount = 5;
        SelectCriteria selectCriteria = Pagenation.getSelectCriteria(Integer.parseInt(offset), totalCount, limit, buttonAmount);;

        log.info("[MusicalController] selectCriteria : " + selectCriteria);

        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
        responseDtoWithPaging.setPageInfo(selectCriteria);
        responseDtoWithPaging.setData(musicalService.selectOriginMusicalListWithPaging(selectCriteria));

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "오리지널 뮤지컬 조회 성공", responseDtoWithPaging));
    }

    @GetMapping("/musicals/creative")
    public ResponseEntity<ResponseDto> selectCreativeMusicalListWithPaging(@RequestParam(name="offset", defaultValue = "1") String offset){

        int totalCount = musicalService.selectCreativeMusicalTotal();
        int limit = 5;
        int buttonAmount = 5;
        SelectCriteria selectCriteria = Pagenation.getSelectCriteria(Integer.parseInt(offset), totalCount, limit, buttonAmount);;

        log.info("[MusicalController] selectCriteria : " + selectCriteria);

        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
        responseDtoWithPaging.setPageInfo(selectCriteria);
        responseDtoWithPaging.setData(musicalService.selectCreativeMusicalListWithPaging(selectCriteria));

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "창작 뮤지컬 조회 성공", responseDtoWithPaging));
    }

    @GetMapping("/musicals/family")
    public ResponseEntity<ResponseDto> selectFamilyMusicalListWithPaging(@RequestParam(name="offset", defaultValue = "1") String offset){

        int totalCount = musicalService.selectFamilyMusicalTotal();
        int limit = 5;
        int buttonAmount = 5;
        SelectCriteria selectCriteria = Pagenation.getSelectCriteria(Integer.parseInt(offset), totalCount, limit, buttonAmount);;

        log.info("[MusicalController] selectCriteria : " + selectCriteria);

        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
        responseDtoWithPaging.setPageInfo(selectCriteria);
        responseDtoWithPaging.setData(musicalService.selectFamilyMusicalListWithPaging(selectCriteria));

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "아동/가족 뮤지컬 조회 성공", responseDtoWithPaging));
    }

    @GetMapping("/musicals/{musicalCode}")
    public ResponseEntity<ResponseDto> selectMusicalDetail(@PathVariable String musicalCode){

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "뮤지컬 상세정보 조회 성공", musicalService.selectMusicalDetail(musicalCode)));
    }

    /* 관리자 영역 */
    @GetMapping("/musicals-management")
    public ResponseEntity<ResponseDto> selectMusicalListWithPagingForAdmin(@RequestParam(name="offset", defaultValue = "1") String offset){

        log.info("[MusicalController] selectMusicalListWithPaging : " + offset);

        int totalCount = musicalService.selectMusicalTotalForAdmin();
        int limit = 8;
        int buttonAmount = 5;
        SelectCriteria selectCriteria = Pagenation.getSelectCriteria(Integer.parseInt(offset), totalCount, limit, buttonAmount);;

        log.info("[MusicalController] selectCriteria : " + selectCriteria);

        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
        responseDtoWithPaging.setPageInfo(selectCriteria);
        responseDtoWithPaging.setData(musicalService.selectMusicalListWithPagingForAdmin(selectCriteria));

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", responseDtoWithPaging));
    }

    @GetMapping("/musicals-management/{musicalCode}")
    public ResponseEntity<ResponseDto> selectMusicalDetailForAdmin(@PathVariable String musicalCode){
        log.info("[MusicalController] musicalCode : " + musicalCode);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "뮤지컬 상세정보 조회 성공", musicalService.selectMusicalDetailForAdmin(musicalCode)));
    }

    @PostMapping("/musicals")
    public ResponseEntity<ResponseDto> insertMusical(@ModelAttribute MusicalDto musicalDto){

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "뮤지컬 등록 성공", musicalService.insertMusical(musicalDto)));
    }

    @PutMapping(value="/musicals-management")
    public ResponseEntity<ResponseDto> updateMusical(@ModelAttribute MusicalDto musicalDto){
//        musicalDto.setMusicalCode(musicalCode);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "뮤지컬 수정 성공", musicalService.updateMusical(musicalDto)));
    }

    @DeleteMapping(value = "/musicals-management/{musicalCode}")
    public ResponseEntity<ResponseDto> deleteMusical(@PathVariable String musicalCode){

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "뮤지컬 삭제 성공", musicalService.deleteMusical(musicalCode)));
    }

}
