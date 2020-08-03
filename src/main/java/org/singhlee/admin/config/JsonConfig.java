package org.singhlee.admin.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;

/**
 * @program: application
 * @description:
 * @author: hbxj_lx
 * @create: 2019-11-28 15:55
 **/
@JsonComponent
public class JsonConfig {
    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        /**
         *将原生jackson的返回值为null转化为“”，方便前台输出
         */
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
                jsonGenerator.writeString("");
            }
        });

        /**
         * 可配置项：
         * 1.Include.Include.ALWAYS : 默认
         * 2.Include.NON_DEFAULT : 默认值不序列化
         * 3.Include.NON_EMPTY : 属性为 空（""） 或者为 NULL 都不序列化
         * 4.Include.NON_NULL : 属性为NULL 不序列化
         **/
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        /**
         * 序列化时,将所有的long变成string
         */
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        module.addSerializer(Double.class,ToStringSerializer.instance);
        module.addSerializer(Double.TYPE,ToStringSerializer.instance);
        objectMapper.registerModule(module);

        return objectMapper;
    }

}
