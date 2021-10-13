package com.putchaiko.mybatisTypehandlerDemo.dog;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author kao
 */
public interface DogMapper {

    @Select("SELECT * FROM dog WHERE id = #{id}")
    String selectDogName(int id);

}
