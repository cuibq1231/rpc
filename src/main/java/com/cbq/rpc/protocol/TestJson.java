package com.cbq.rpc.protocol;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;

import java.io.IOException;

import com.cbq.rpc.java.GreeterImpl.HelloRequest;
import com.cbq.rpc.utils.JacksonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *  * @author libo <libo@kuaishou.com>
 *  * Created on 2018-11-19
 *  
 */
public class TestJson {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        //反序列化的时候如果多了其他属性,不抛出异常
        MAPPER.disable(FAIL_ON_UNKNOWN_PROPERTIES);
        //如果是空对象的时候,不抛异常
        MAPPER.disable(FAIL_ON_EMPTY_BEANS);
        //        MAPPER.enable(ALLOW_UNQUOTED_CONTROL_CHARS);
        //        MAPPER.enable(ALLOW_COMMENTS);
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        return MAPPER.writeValueAsString(obj);
    }

    public static <T> T fromJson(String str, Class<T> clazz) throws IOException {
        return MAPPER.readValue(str, clazz);
    }


    public static void main(String[] args) throws IOException {
        Person person = new Person(1, "bob", "kwai://gamecenter/pageId=2");
        String personJson = toJson(person);
        System.out.println(personJson);
        HelloRequest testPb = HelloRequest.newBuilder().setName(personJson).build();
        TestPb pb = new TestPb();
        pb.setPerson(personJson);
        System.out.println(JacksonUtils.serialize(pb));
        System.out.println(testPb.toString());

//        Map<String, Object> myMap = new HashMap<>();
//        myMap.put("scheme", "kwai://gamecenter/pageId=2");
//        System.out.println(toJson(myMap));

        //        String str = "{\"id\":1,\"name\":\"bob\",\"address\":null}";
        //        Person myPerson = objectMapper.readValue(str, Person.class);
        //        System.out.println(myPerson);
        //        String str = "{\"id\":1,\"name\":\"bob\"}";
        //        Person myPerson = fromJson(str, Person.class);
        //        System.out.println(myPerson);
    }

    static class Person {

        private int id;
        private String name;
        private String address;
        private boolean show;

        Person() {
        }

        public int getUserId() {
            return id;
        }

        public String getName() {
            return this.name;
        }

        public String getAddress() {
            return this.address;
        }

        Person(int id, String name, String address) {
            this.id = id;
            this.name = name;
            this.address = address;
        }
    }
}