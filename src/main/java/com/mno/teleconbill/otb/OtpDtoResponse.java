package com.mno.teleconbill.otb;


import com.mno.teleconbill.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpDtoResponse {

    public String email;
    public boolean logined;
    public boolean checkotp;
    public String massage;
    public String token;
    private User user;

}
