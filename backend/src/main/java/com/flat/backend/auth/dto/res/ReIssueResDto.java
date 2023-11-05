package com.flat.backend.auth.dto.res;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flat.backend.common.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.simple.parser.JSONParser;

@AllArgsConstructor
@Getter
public class ReIssueResDto {
    private String accessToken;

    public ObjectNode toJson(){
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("accessToken", String.valueOf(accessToken));
        return json;
    }
}
