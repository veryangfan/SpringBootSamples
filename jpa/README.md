# SpringBoot JPA


1. 创建实体类

- `@Entity` 标识这是一个实体类  
- `@Id` 用于标识主键  
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` 主键生成策略，
可选策略为
    - `AUTO` JPA自动选择合适的策略，是默认选项,可以省略掉
    - `IDENTITY` 主键递增策略，MySQL常用,Oracle不支持
    - `SEQUENCE`通过序列产生主键，通过@SequenceGenerator注解指定序列名，MySQL不支持这种方式
    - `TABLE` 这种方式较为通用，但效率不高，配合@TableGenerator需要计算获得
- @Table(name = "user") 对应数据库的表名
- @Column(name = "user_name")对应数据库的列名


2. 创建自定义接口继承JpaRepository接口
继承后即就拥有了系统默认帮我们实现的方法。并不需要提供任何实现类，Spring会根据 JPA接口规范帮我们完成

3. 注入即可使用

- 默认实现`findById()`  `save()`  
- 可以自定义`findByName()``findByNameAndEmail()`
- 可以`@Query(value = "select * from user",nativeQuery = true)`实现SQL查询    
`nativeQuery = true`设置使用SQL（JPA默认使用的查询语言是JPQL）

- JPA实际上还可以实现子查询、分页和排序等功能，但由于可以自定义SQL，就不太喜欢用了

