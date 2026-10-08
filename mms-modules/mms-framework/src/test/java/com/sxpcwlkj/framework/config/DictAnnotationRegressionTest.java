package com.sxpcwlkj.framework.config;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class DictAnnotationRegressionTest {
    public static class Response extends PageQuery {
        @JsonIgnore public String password = "sensitive";
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) public String secret = "hidden";
        public String title = "visible";
    }
    @Test void dictionaryIntrospectorPreservesStandardJacksonAnnotations() throws Exception {
        ObjectMapper mapper = new ObjectMapper().setAnnotationIntrospector(new DictSensitiveAnnotationIntrospector());
        var json = mapper.readTree(mapper.writeValueAsString(new Response()));
        assertEquals("visible", json.get("title").asText());
        assertFalse(json.has("password")); assertFalse(json.has("secret")); assertFalse(json.has("pageQuery"));
        assertEquals("input", mapper.readValue("{\"secret\":\"input\"}", Response.class).secret);
    }
}
