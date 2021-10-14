package com.putchaiko.mybatisTypehandlerDemo;

import com.putchaiko.mybatisTypehandlerDemo.dog.Dog;
import com.putchaiko.mybatisTypehandlerDemo.dog.DogMapper;
import com.putchaiko.mybatisTypehandlerDemo.dog.DogXmlMapper;
import com.putchaiko.mybatisTypehandlerDemo.dog.NameAndColor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
@RestController
@MapperScan("com.putchaiko.mybatisTypehandlerDemo.dog")
public class MybatisTypehandlerDemoApplication {

    /**
     * DogMapper 是以注解的方式写sql语句的 Mapper
     */
    @Resource
    private DogMapper dogMapper;

    /**
     * DogXmlMapper 是以 xml 的方式写sql语句的 Mapper
     * 两个 Mapper 功能一样的
     */
    @Resource
    private DogXmlMapper dogXmlMapper;

    public static void main(String[] args) {
        SpringApplication.run(MybatisTypehandlerDemoApplication.class, args);
    }

    @GetMapping("/name_and_color/{id}")
    public NameAndColor selectDogNameAndColor(@PathVariable(value = "id") Integer id) {
        return dogMapper.selectDogNameAndColor(id);
    }

    @GetMapping("/dog/{id}")
    public Dog selectDog(@PathVariable(value = "id") Integer id) {
        return dogMapper.selectDog(id);
    }

    @PutMapping("/dog")
    Boolean updateDog(@RequestBody Dog dog) {
        return dogMapper.updateDog(dog);
    }

    @GetMapping("/dog_names/{ids}")
    public List<String> selectDogName(@PathVariable(value = "ids") List<Integer> ids) {
        return dogMapper.selectDogNames(ids);
    }

    @GetMapping("/xml/name_and_color/{id}")
    public NameAndColor selectXmlDogNameAndColor(@PathVariable(value = "id") Integer id) {
        return dogXmlMapper.selectDogNameAndColor(id);
    }

    @GetMapping("/xml/dog/{id}")
    public Dog selectXmlDog(@PathVariable(value = "id") Integer id) {
        return dogXmlMapper.selectDog(id);
    }

    @PutMapping("/xml/dog")
    Boolean updateXmlDog(@RequestBody Dog dog) {
        return dogXmlMapper.updateDog(dog);
    }

    @GetMapping("/xml/dog_names/{ids}")
    public List<String> selectXmlDogName(@PathVariable(value = "ids") List<Integer> ids) {
        return dogXmlMapper.selectDogNames(ids);
    }
}
