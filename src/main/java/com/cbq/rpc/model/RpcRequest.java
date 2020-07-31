package com.cbq.rpc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-04-15
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class RpcRequest {
    private String serviceName; //调用服务全路径
    private String methodName; //调用方法名称
    private Class<?>[] paramTypeList; //方法参数类型数组
    private Object[] paramValueList; //参数值数组
}
