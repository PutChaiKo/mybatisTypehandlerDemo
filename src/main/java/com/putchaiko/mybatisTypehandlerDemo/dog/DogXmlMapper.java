package com.putchaiko.mybatisTypehandlerDemo.dog;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DogXmlMapper {

    /**
     * 狗狗的名字和毛色
     *
     * @param id
     * @return 狗狗的名字和毛色
     */
    NameAndColor selectDogNameAndColor(Integer id);

    /**
     * 获取狗狗的信息
     *
     * @param id id
     * @return 狗狗的信息
     */
    Dog selectDog(int id);

    /**
     * 更新狗狗的信息
     *
     * @param dog 狗狗的信息
     * @return 布尔
     */
    Boolean updateDog(Dog dog);

    /**
     * 获取多个狗狗的名字
     *
     * @param ids ids
     * @return 名字列表
     */
    List<String> selectDogNames(@Param("ids") List<Integer> ids);
}
