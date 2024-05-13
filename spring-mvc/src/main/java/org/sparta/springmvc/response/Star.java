package org.sparta.springmvc.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
@Getter

public class Star {
    @JsonProperty(value = "aaa")
    private String name;

    private int age;

    public Star(String name, int age) {
        this.name = name;
        this.age = age;
    }

   // @JsonIgnore
    public String getNameAge() {
        return name + age;
    }

    public Star() {}
}
//리플렉션 get 메서드 기준으로