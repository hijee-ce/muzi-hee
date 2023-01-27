package com.hee.muzihee.musical.dto;

import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

public class MusicalDto {
    private Long musicalCode;
    private String musicalName;
    private String musicalPoster;
    private MultipartFile musicalPosterImg;
    private String musicalPlace;
    private Date musicalStartDay;
    private Date musicalEndDay;
    private String musicalRuntime;
    private String musicalAge;
    private String musicalPrice;
    private String musicalWhen;
    private String musicalDiscount;
    private String musicalDetail;
    private String musicalCategory;
    private String categoryName;

    public MusicalDto(){ }

    public MusicalDto(Long musicalCode, String musicalName, String musicalPoster, MultipartFile musicalPosterImg,
                      String musicalPlace, Date musicalStartDay, Date musicalEndDay, String musicalRuntime,
                      String musicalAge, String musicalPrice, String musicalWhen,
                      String musicalDiscount, String musicalDetail, String musicalCategory,
                      String categoryName) {
        this.musicalCode = musicalCode;
        this.musicalName = musicalName;
        this.musicalPoster = musicalPoster;
        this.musicalPosterImg = musicalPosterImg;
        this.musicalPlace = musicalPlace;
        this.musicalStartDay = musicalStartDay;
        this.musicalEndDay = musicalEndDay;
        this.musicalRuntime = musicalRuntime;
        this.musicalAge = musicalAge;
        this.musicalPrice = musicalPrice;
        this.musicalWhen = musicalWhen;
        this.musicalDiscount = musicalDiscount;
        this.musicalDetail = musicalDetail;
        this.musicalCategory = musicalCategory;
        this.categoryName = categoryName;
    }

    public Long getMusicalCode() { return musicalCode; }

    public void setMusicalCode(Long musicalCode) {  this.musicalCode = musicalCode; }

    public String getMusicalName() { return musicalName; }

    public void setMusicalName(String musicalName) { this.musicalName = musicalName; }

    public String getMusicalPoster() { return musicalPoster; }

    public void setMusicalPoster(String musicalPoster) { this.musicalPoster = musicalPoster; }

    public MultipartFile getMusicalPosterImg() { return musicalPosterImg; }

    public void setMusicalPosterImg(MultipartFile musicalPosterImg) { this.musicalPosterImg = musicalPosterImg; }

    public String getMusicalPlace() { return musicalPlace; }

    public void setMusicalPlace(String musicalPlace) { this.musicalPlace = musicalPlace; }

    public Date getMusicalStartDay() { return musicalStartDay; }

    public void setMusicalStartDay(Date musicalStartDay) { this.musicalStartDay = musicalStartDay; }

    public Date getMusicalEndDay() { return musicalEndDay; }

    public void setMusicalEndDay(Date musicalEndDay) { this.musicalEndDay = musicalEndDay; }

    public String getMusicalRuntime() { return musicalRuntime; }

    public void setMusicalRuntime(String musicalRuntime) { this.musicalRuntime = musicalRuntime; }

    public String getMusicalAge() { return musicalAge; }

    public void setMusicalAge(String musicalAge) { this.musicalAge = musicalAge; }

    public String getMusicalPrice() { return musicalPrice; }

    public void setMusicalPrice(String musicalPrice) { this.musicalPrice = musicalPrice; }

    public String getMusicalWhen() { return musicalWhen; }

    public void setMusicalWhen(String musicalWhen) { this.musicalWhen = musicalWhen; }

    public String getMusicalDiscount() { return musicalDiscount; }

    public void setMusicalDiscount(String musicalDiscount) { this.musicalDiscount = musicalDiscount; }

    public String getMusicalDetail() { return musicalDetail; }

    public void setMusicalDetail(String musicalDetail) { this.musicalDetail = musicalDetail; }

    public String getMusicalCategory() { return musicalCategory; }

    public void setMusicalCategory(String musicalCategory) { this.musicalCategory = musicalCategory; }

    public String getCategoryName() { return categoryName; }

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    @Override
    public String toString() {
        return "MusicalDto{" +
                "musicalCode=" + musicalCode +
                ", musicalName='" + musicalName + '\'' +
                ", musicalPoster=" + musicalPoster +
                ", musicalPosterImg='" + musicalPosterImg + '\'' +
                ", musicalPlace='" + musicalPlace + '\'' +
                ", musicalStartDay=" + musicalStartDay +
                ", musicalEndDay=" + musicalEndDay +
                ", musicalRuntime='" + musicalRuntime + '\'' +
                ", musicalAge='" + musicalAge + '\'' +
                ", musicalPrice='" + musicalPrice + '\'' +
                ", musicalWhen='" + musicalWhen + '\'' +
                ", musicalDiscount=" + musicalDiscount +
                ", musicalDetail=" + musicalDetail +
                ", musicalCategory=" + musicalCategory +
                '}';
    }
}
