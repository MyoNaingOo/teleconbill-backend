package com.mno.teleconbill.dto;


import com.mno.teleconbill.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillDto {


    private Long recenumber;
    private Long sendnumber;
    private LocalDate before;
    private LocalDate after;
    private User user;

}
