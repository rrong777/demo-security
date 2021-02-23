package com.rrong777.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 业务复杂的查询，一个个写参数麻烦，而且读起来也麻烦，判断起来也麻烦，写一个实体类接收参数
 */
public class UserQueryCondition {
    @ApiModelProperty("用户年龄起始值") // 封装在对象中的请求参数，可以用@ApiModelProperty进行描述，最后生成在swagger文档中
    private String username;
    private int age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(int ageTo) {
        this.ageTo = ageTo;
    }

    public String getXxx() {
        return xxx;
    }

    public void setXxx(String xxx) {
        this.xxx = xxx;
    }

    private int ageTo;
    private String xxx;// 按照其他的属性来查  xxx代表其他
}
