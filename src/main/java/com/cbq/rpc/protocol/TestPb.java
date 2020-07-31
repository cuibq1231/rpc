package com.cbq.rpc.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-04-30
 */


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class TestPb {
    private String person;

//    @Override
//    public String toString() {
//        Map map = new HashMap<>();
//        map.put("person", person);
//        return JacksonUtils.serialize(map);
//    }
}
