package com.putchaiko.mybatisTypehandlerDemo.dog;

import io.github.gaojizhou.list.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author kao
 */
@Mapper
public interface DogMapper {

    /**
     * 狗狗的名字和毛色
     *
     * @param id
     * @return 狗狗的名字和毛色
     */
    @Select("SELECT name, colors FROM dog WHERE id = #{id}")
//    @Results({
//            @Result(column = "name", property = "name"),
//            @Result(column = "colors", property = "colors", typeHandler = ListStringHandler.class),
//    })
    NameAndColor selectDogNameAndColor(Integer id);

    /**
     * 获取狗狗的信息
     *
     * @param id id
     * @return 狗狗的信息
     */
    @Select("SELECT id,name,colors,height_length,vaccination_date,vaccination_age,is_ear_pricked_up FROM dog WHERE id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "colors", property = "colors", typeHandler = ListStringHandler.class),
            @Result(column = "height_length", property = "heightLength", typeHandler = ListBigDecimalHandler.class),
            @Result(column = "vaccination_date", property = "vaccinationDate", typeHandler = ListDateHandler.class),
            @Result(column = "vaccination_age", property = "vaccinationAge", typeHandler = ListIntegerHandler.class),
            @Result(column = "is_ear_pricked_up", property = "isEarPrickedUp", typeHandler = ListBooleanHandler.class)
    })
    Dog selectDog(int id);

    /**
     * 更新狗狗的信息
     *
     * @param dog 狗狗的信息
     * @return 布尔
     */
    @Update("update dog set " +
            "name = #{name,jdbcType=VARCHAR}," +
            "colors = #{colors,typeHandler=io.github.gaojizhou.list.ListStringHandler}," +
            "height_length = #{heightLength,typeHandler=io.github.gaojizhou.list.ListBigDecimalHandler}," +
            "vaccination_date = #{vaccinationDate,typeHandler=io.github.gaojizhou.list.ListDateHandler}," +
            "vaccination_age = #{vaccinationAge,typeHandler=io.github.gaojizhou.list.ListIntegerHandler}," +
            "is_ear_pricked_up = #{isEarPrickedUp,typeHandler=io.github.gaojizhou.list.ListBooleanHandler}" +
            "where id = #{id,jdbcType=INTEGER}")
    Boolean updateDog(Dog dog);

    /**
     * 获取多个狗狗的名字
     *
     * @param ids ids
     * @return 名字列表
     */
    @Select("SELECT name FROM dog WHERE id = any(#{ids,jdbcType=ARRAY,typeHandler=io.github.gaojizhou.list.ListIntegerHandler});")
    List<String> selectDogNames(@Param("ids") List<Integer> ids);
}
