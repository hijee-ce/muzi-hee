package com.hee.muzihee.musical.service;


import com.hee.muzihee.common.paging.SelectCriteria;
import com.hee.muzihee.musical.dao.MusicalMapper;
import com.hee.muzihee.musical.dto.MusicalDto;
import com.hee.muzihee.util.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MusicalService {

    @Value("${image.image-dir}")
    private String IMAGE_DIR;
    @Value("${image.image-url}")
    private String IMAGE_URL;

    private final MusicalMapper musicalMapper;

    public MusicalService(MusicalMapper musicalMapper) {
        this.musicalMapper = musicalMapper;
    }

    public List<MusicalDto> selectMusicalList( ){
        List<MusicalDto> musicalList = musicalMapper.selectMusicalList();

        return musicalList;
    }

    public int selectMusicalTotalForAdmin(){
        int result = musicalMapper.selectMusicalTotalForAdmin();

        return result;
    }

    public List<MusicalDto> selectMusicalListWithPagingForAdmin(SelectCriteria selectCriteria){
        List<MusicalDto> musicalList = musicalMapper.selectMusicalListWithPagingForAdmin(selectCriteria);

        return musicalList;
    }

    public MusicalDto selectMusicalDetailForAdmin(String musicalCode){

        return musicalMapper.selectMusicalDetailForAdmin(musicalCode);
    }

    public List<MusicalDto> selectOriginMusicalListWithPaging(SelectCriteria selectCriteria) {

        List<MusicalDto> musicalList = musicalMapper.selectOriginMusicalListWithPaging(selectCriteria);

        return musicalList;
    }


    public int selectOriginMusicalTotal() {

        int result = musicalMapper.selectOriginMusicalTotal();

        return  result;
    }

    public List<MusicalDto> selectCreativeMusicalListWithPaging(SelectCriteria selectCriteria) {
        List<MusicalDto> musicalList = musicalMapper.selectCreativeMusicalListWithPaging(selectCriteria);

        return musicalList;
    }

    public int selectCreativeMusicalTotal() {

        int result = musicalMapper.selectCreativeMusicalTotal();

        return  result;
    }

    public List<MusicalDto> selectFamilyMusicalListWithPaging(SelectCriteria selectCriteria) {
        List<MusicalDto> musicalList = musicalMapper.selectFamilyMusicalListWithPaging(selectCriteria);

        return musicalList;
    }

    public int selectFamilyMusicalTotal() {

        int result = musicalMapper.selectFamilyMusicalTotal();

        return  result;
    }

    public MusicalDto selectMusicalDetail(String musicalCode) {
        MusicalDto musicalDto = musicalMapper.selectMusicalDetail(musicalCode);

        return musicalDto;
    }

    @Transactional
    public String insertMusical(MusicalDto musicalDto) {
        log.info("[ProductService] insertProduct Start ===================================");
        log.info("[ProductService] musicalDto : " + musicalDto);

        String imageName = UUID.randomUUID().toString().replace("-", "");
        String replaceFileName = null;
        int result = 0;

        log.info("[ProductService] IMAGE_DIR : " + IMAGE_DIR);
        log.info("[ProductService] imageName : " + imageName);

        try {
            replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, musicalDto.getMusicalPosterImg());
            log.info("[ProductService] replaceFileName : " + replaceFileName);

            musicalDto.setMusicalPoster(replaceFileName);

            log.info("[ProductService] insert Image Name : "+ replaceFileName);

            result = musicalMapper.insertMusical(musicalDto);

        } catch (IOException e) {
            log.info("[ProductService] IOException IMAGE_DIR : "+ IMAGE_DIR);

            log.info("[ProductService] IOException deleteFile : "+ replaceFileName);

            FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
            throw new RuntimeException(e);
        }

        log.info("[ProductService] result > 0 성공: "+ result);
        return (result > 0) ? "뮤지컬 입력 성공" : "뮤지컬 입력 실패";
    }

    @Transactional
    public Object updateMusical(MusicalDto musicalDto) {
        log.info("[ProductService] updateProduct Start ===================================");
        log.info("[ProductService] productDto : " + musicalDto);
        String replaceFileName = null;
        int result = 0;

        try {
            String oriImage = musicalMapper.selectMusicalDetail(String.valueOf(musicalDto.getMusicalCode())).getMusicalPoster();
            log.info("[updateProduct] oriImage : " + oriImage);

            if(musicalDto.getMusicalPoster() != null){
                // 이미지 변경 진행
                String imageName = UUID.randomUUID().toString().replace("-", "");
                replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, musicalDto.getMusicalPosterImg());

                log.info("[updateProduct] IMAGE_DIR!!"+ IMAGE_DIR);
                log.info("[updateProduct] imageName!!"+ imageName);

                log.info("[updateProduct] InsertFileName : " + replaceFileName);
                musicalDto.setMusicalPoster(replaceFileName);

                log.info("[updateProduct] deleteImage : " + oriImage);
                boolean isDelete = FileUploadUtils.deleteFile(IMAGE_DIR, oriImage);
                log.info("[update] isDelete : " + isDelete);
            } else {
                // 이미지 변경 없을 시
                musicalDto.setMusicalPoster(oriImage);
            }

            result = musicalMapper.updateMusical(musicalDto);

        } catch (IOException e) {
            log.info("[updateProduct] Exception!!");
            FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
            throw new RuntimeException(e);
        }
        log.info("[ProductService] updateProduct End ===================================");
        log.info("[ProductService] result > 0 성공: "+ result);

        return (result > 0) ? "뮤지컬 업데이트 성공" : "뮤지컬 업데이트 실패";
    }

    @Transactional
    public Object deleteMusical(String musicalCode) {
        int result = musicalMapper.deleteMusical(musicalCode);

        return (result > 0) ? "뮤지컬 삭제 성공" : "뮤지컬 삭제 실패";

    }

}
