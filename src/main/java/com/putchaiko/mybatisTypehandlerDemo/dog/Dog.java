package com.putchaiko.mybatisTypehandlerDemo.dog;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author kao
 */
public class Dog {
    private Integer id;
    private String name;
    private List<String> colors;
    private List<BigDecimal> heightLength;
    private List<Date> vaccinationDate;
    private List<Integer> vaccinationAge;
    private List<Boolean> isEarPrickedUp;

    public List<Integer> getVaccinationAge() {
        return vaccinationAge;
    }

    public void setVaccinationAge(List<Integer> vaccinationAge) {
        this.vaccinationAge = vaccinationAge;
    }

    public List<Boolean> getIsEarPrickedUp() {
        return isEarPrickedUp;
    }

    public void setIsEarPrickedUp(List<Boolean> isEarPrickedUp) {
        this.isEarPrickedUp = isEarPrickedUp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<BigDecimal> getHeightLength() {
        return heightLength;
    }

    public void setHeightLength(List<BigDecimal> heightLength) {
        this.heightLength = heightLength;
    }

    public List<Date> getVaccinationDate() {
        return vaccinationDate;
    }

    public void setVaccinationDate(List<Date> vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }


}
