package com.yunusemrenalbant.springjwtredis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JWTResponse {

    private String token;

    @Builder.Default
    private String type = "Bearer";
    private String email;

}