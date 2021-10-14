# 一个好用的 mybatis Typehandler 小工具
首先来建一个 postgresql 数据库的表  
```postgresql
-- 创建表
create table dog
(
        id serial constraint dog_pk primary key,
        name varchar,
        colors varchar[],
        height_length numeric[],
        is_ear_pricked_up bool[],
        vaccination_date date[],
        vaccination_age integer[]
);
-- 插入点数据
INSERT INTO dog (id, name, colors, height_length, vaccination_date, vaccination_age, is_ear_pricked_up)
VALUES (DEFAULT, '旺财', '{黄,黑}', '{22.5, 33.2}', '{20210119,20210120}', '{1,1}', '{true, true}'),
       (DEFAULT, '来福', '{白,灰}', '{26.5, 43.0}', '{20200119}', '{2}', '{false, false}'),
       (DEFAULT, '大黄', '{黄,白}', '{40.5, 50.1}', '{20190101,20210101}', '{6,8}', '{false, true}'),
       (DEFAULT, '四眼', '{白,黑}', '{43.2, 46.7}', '{}', '{}', '{false, true}');
```

可以看到在 PostgreSql 中类似 varchar[] 的数组形式是以 '{黄,黑}' 这种形式进行储存的，看着像是字符串，在 java 中通过 mybatis 将数据取出来一般是这样子写：  

先声明一个包含狗名字和颜色的类 NameAndColor.java  
```java
// 省略 getter setter
public class NameAndColor {
    private String name;
    // 狗有多种颜色，用 List 表示
    private List<String> colors;
}
```

Controller    
```java
// 引入Mapper
@Resource
private DogMapper dogMapper;

@GetMapping("/name_and_color/{id}")
public NameAndColor selectDogNameAndColor(@PathVariable(value = "id") Integer id) {
    return dogMapper.selectDogNameAndColor(id);
}
```

Mapper
```java
// 用注解的方式写sql语句
@Select("SELECT name, colors FROM dog WHERE id = #{id}")
NameAndColor selectDogNameAndColor(Integer id);
```

或者是用xml方式写的 Mapper.xml 
```xml
<select id="selectDogNameAndColor" resultType="com.xxx.NameAndColor">
    SELECT name, colors FROM dog WHERE id = #{id}
</select>
```

拼接链接访问：http://127.0.0.1:8081/name_and_color/1  
然而并不能获取到数据库里的 colors 内容
```json
// 访问 http://127.0.0.1:8081/name_and_color/1
{
    "name": "旺财",
    "colors": null
}
```
原因其实很简单，mybatis 不能处理 varchar[] 和 List<String> 之间的转换。  
但其实数据库的 varchar 和 java 的 String 也不是一个东西，mybatis 维护了一套默认的类型处理器（typeHandlers）：  
[点击查看 mybatis 官方 typeHandlers 列表](https://mybatis.org/mybatis-3/configuration.html)  
里面提供了诸如 boolean、byte、int、BigDecimal和对应数据库类型的转换处理器，简单的增删改查基本可以满足。  
mybatis 不支持的类型之间的之间的转换，官方建设是自己继承 BaseTypeHandler 实现一套，但类似 List&lt;String&gt; 这种常见的类型就没必要造轮子了。

github 项目 MybatisTypeHandler 就是一个已经实现的 typeHandler   
链接：[https://github.com/gaojizhou/MybatisTypeHandler](https://github.com/gaojizhou/MybatisTypeHandler)  
使用也非常简单：  
pom.xml 引入依赖
```xml
<!-- 引入 MybatisTypeHandler -->
<dependency>
    <groupId>io.github.gaojizhou</groupId>
    <artifactId>MybatisTypeHandler</artifactId>
    <!-- 自行选择最新版本的 -->
    <version>0.0.3</version>
</dependency>
```

在 Mapper 里加入对应的 @Results 注解  
```java
import io.github.gaojizhou.list.ListStringHandler;

// @Results 注解加入对应的 typeHandler
@Select("SELECT name, colors FROM dog WHERE id = #{id}")
@Results({
        @Result(column = "name", property = "name"),
        @Result(column = "colors", property = "colors", typeHandler = ListStringHandler.class),
})
NameAndColor selectDogNameAndColor(Integer id);
```

或者是在用xml方式写的 Mapper.xml 里新建 resultMap 并把对应的 typeHandler 引入到对应的列  
```xml
<!-- 新建一个 resultMap -->
<resultMap id="NameAndColorResultMap" type="com.xxx.NameAndColor">
    <result column="name" jdbcType="VARCHAR" property="name"/>
    <!-- 加入对应的 typeHandler -->
    <result column="colors" jdbcType="ARRAY" property="colors"
            typeHandler="io.github.gaojizhou.list.ListStringHandler"/>
</resultMap>

<!-- 引用 resultMap -->
<select id="selectDogNameAndColor" resultMap="NameAndColorResultMap">
    SELECT name, colors FROM dog WHERE id = #{id}
</select>
```
这样出来请求链接出来的结果就是正常的了  
```json
// 访问 http://127.0.0.1:8081/name_and_color/1
{
    "name": "旺财",
    "colors": [ "黄",  "黑" ]
}
```

除了 List&lt;String&gt; 这个项目还提供了很多其他数据类型的 typeHandler：

|  typeHandler   | java数据类型  | 数据库数据类型  |
|  ----  | ----  | ----  |
| ListStringHandler  | List&lt;String&gt;  | varchar[]  |
| ListBigDecimalHandler  | List&lt;BigDecimal&gt;  | numeric[]  |
| ListBooleanHandler  | List&lt;Boolean&gt;  | bool[]  |
| ListDateHandler  | List&lt;Date&gt;  | date[]  |
| ListIntegerHandler  | List&lt;Integer&gt;  | integer[]  |

把这些 typeHandler 用来写一个完整的 dog 类是这样的：

Dog.java
```java
public class Dog {
    private Integer id;
    private String name;
    private List<String> colors;
    private List<BigDecimal> heightLength;
    private List<Date> vaccinationDate;
    private List<Integer> vaccinationAge;
    private List<Boolean> isEarPrickedUp;
}
```

Controller
```java
// 引入 mapper
@Resource
private DogMapper dogMapper;

// 获取 dog 信息
@GetMapping("/dog/{id}")
public Dog selectDog(@PathVariable(value = "id") Integer id) {
    return dogMapper.selectDog(id);
}

// 更新 dog 信息
@PutMapping("/dog")
Boolean updateDog(@RequestBody Dog dog) {
    return dogMapper.updateDog(dog);
    
// 批量获取 dog 的名字
@GetMapping("/dog_names/{ids}")
public List<String> selectDogName(@PathVariable(value = "ids") List<Integer> ids) {
        return dogMapper.selectDogNames(ids);
        }
```

Mapper
```java
import io.github.gaojizhou.list.*;

// 获取狗狗的信息
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

// 更新狗狗的信息
@Update("update dog set " +
        "name = #{name,jdbcType=VARCHAR}," +
        "colors = #{colors,typeHandler=io.github.gaojizhou.list.ListStringHandler}," +
        "height_length = #{heightLength,typeHandler=io.github.gaojizhou.list.ListBigDecimalHandler}," +
        "vaccination_date = #{vaccinationDate,typeHandler=io.github.gaojizhou.list.ListDateHandler}," +
        "vaccination_age = #{vaccinationAge,typeHandler=io.github.gaojizhou.list.ListIntegerHandler}," +
        "is_ear_pricked_up = #{isEarPrickedUp,typeHandler=io.github.gaojizhou.list.ListBooleanHandler}" +
        "where id = #{id,jdbcType=INTEGER}")
Boolean updateDog(Dog dog);

// 一次获取多个狗狗的名字
@Select("SELECT name FROM dog WHERE id = any(#{ids,jdbcType=ARRAY,typeHandler=io.github.gaojizhou.list.ListIntegerHandler});")
List<String> selectDogNames(@Param("ids") List<Integer> ids);
```

Mapper 的另一种写法  
Mapper.xml
```xml
<resultMap id="selectDogResultMap" type="com.putchaiko.mybatisTypehandlerDemo.dog.Dog">
    <id column="id" jdbcType="INTEGER" property="id"/>
    <result column="name" jdbcType="VARCHAR" property="name"/>
    <result column="colors" jdbcType="ARRAY" property="colors"  typeHandler="io.github.gaojizhou.list.ListStringHandler"/>
    <result column="height_length" jdbcType="ARRAY" property="heightLength"  typeHandler="io.github.gaojizhou.list.ListBigDecimalHandler"/>
    <result column="vaccination_date" jdbcType="ARRAY" property="vaccinationDate"  typeHandler="io.github.gaojizhou.list.ListDateHandler"/>
    <result column="vaccination_age" jdbcType="ARRAY" property="vaccinationAge"  typeHandler="io.github.gaojizhou.list.ListIntegerHandler"/>
    <result column="is_ear_pricked_up" jdbcType="ARRAY" property="isEarPrickedUp"  typeHandler="io.github.gaojizhou.list.ListBooleanHandler"/>
</resultMap>
<select id="selectDog" resultMap="selectDogResultMap">
    SELECT id, name, colors, height_length, vaccination_date, vaccination_age, is_ear_pricked_up
    FROM dog
    WHERE id = #{id}
</select>

<update id="updateDog">
    update dog set
        name = #{name,jdbcType=VARCHAR},
        colors = #{colors,typeHandler=io.github.gaojizhou.list.ListStringHandler},
        height_length = #{heightLength,typeHandler=io.github.gaojizhou.list.ListBigDecimalHandler},
        vaccination_date = #{vaccinationDate,typeHandler=io.github.gaojizhou.list.ListDateHandler},
        vaccination_age = #{vaccinationAge,typeHandler=io.github.gaojizhou.list.ListIntegerHandler},
        is_ear_pricked_up = #{isEarPrickedUp,typeHandler=io.github.gaojizhou.list.ListBooleanHandler}
        where id = #{id,jdbcType=INTEGER}
</update>

<select id="selectDogNames" resultType="java.lang.String">
    SELECT name
    FROM dog
    WHERE id = any(#{ids,jdbcType=ARRAY,typeHandler=io.github.gaojizhou.list.ListIntegerHandler});
</select>
```

请求链接的效果  
```json
// 获取狗狗 1 的信息 http://127.0.0.1:8081/xml/dog/1
{
  "id": 1,
  "name": "旺财",
  "colors": [ "黄", "黑" ],
  "heightLength": [ 22.5, 33.2 ],
  "vaccinationDate": [ "2021-01-19",  "2021-01-20" ],
  "vaccinationAge": [ 1, 1 ],
  "isEarPrickedUp": [ true,  true ]
}


// 一次获取 1,2,3,4 多个狗狗的名字 http://127.0.0.1:8081/dog_names/1,2,3,4
[ "旺财", "大黄",  "四眼",  "来福" ]
```
使用 put 方法更新 dog 数据库，数据以 body 的形式传入
```shell 
curl --location --request PUT 'http://127.0.0.1:8081/xml/dog' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": 2,
    "name": "来福",
    "colors": [ "白", "灰" ],
    "heightLength": [ 26.5, 43.0 ],
    "vaccinationDate": [ "2020-01-19" ],
    "vaccinationAge": [ 2 ],
    "isEarPrickedUp": [ false, false ]
}'
```

示例完整的代码放在github上 https://github.com/PutChaiKo/mybatisTypehandlerDemo