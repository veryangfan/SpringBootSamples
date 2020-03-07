# lombok
使用lombok可以省略写getter and setter

## 1. 产生getter和setter的注解
- @Data             //自动为字段添加get/set方法-----等价于@Getter + @Setter + @ToString + @EqualsAndHashCode
- @Getter @Setter //这俩兄弟可以用在字段上 也可以用在类上

## 2. 重写toString和equals的注解
- @ToString(exclude = "userid")  //自动添加一个toString方法(默认是 "包名.类名(字段1=xxx,字段2=xxx....)")
- @EqualsAndHashCode   //自动添加一个合适的equals方法 和 hashcode方法 默认是每个字段分别运行这个方法，而非用对象的存储地址

## 3. 产生构造方法的注解
- @AllArgsConstructor  //添加全参数的构造方法
- @NoArgsConstructor   //添加空参数的构造方法