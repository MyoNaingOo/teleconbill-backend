package com.mno.teleconbill.controller;


import com.mno.teleconbill.config.JwtService;
import com.mno.teleconbill.dto.BillDto;
import com.mno.teleconbill.entity.Bill;
import com.mno.teleconbill.entity.User;
import com.mno.teleconbill.service.BillSer;
import com.mno.teleconbill.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/bill")
public class BillController {

    private final BillSer billSer;

    private final UserService userService;

    private final JwtService jwtService;

    @GetMapping("all")
    private List<Bill> billList(){
        return billSer.billList();
    }

    private void billList(@RequestBody Bill bill){
        billSer.saveBill(bill);
    }

    @GetMapping("home")
    private LocalDateTime local(){
        LocalDate date = LocalDate.now();
        int timeHour = LocalTime.now().getHour();
        int timeMinute = LocalTime.now().getMinute();
        LocalTime time = LocalTime.of(timeHour,timeMinute);
        LocalDateTime out = LocalDateTime.of(date,time);
        System.out.println(out);
        return LocalDateTime.now();
    }

    @PostMapping("save")
    private void saveBill(@RequestBody BillDto billDto, HttpServletRequest request){
//        bill.setDate(LocalDate.now());
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        User user = userService.userfindByEmail(userEmail);
        Bill bill = Bill.builder()
                .recenumber(billDto.getRecenumber())
                .sendnumber(billDto.getSendnumber())
                .date(LocalDate.now())
                .user(user)
                .build();
        billSer.saveBill(bill);
    }

    @DeleteMapping("delete/{id}")
    private void delete(@PathVariable("id") Long id){
        billSer.deleteBill(id);
    }


    @PutMapping("update/{id}")
    private void updateBill(@RequestBody Bill bill, @PathVariable("id") Long id){
        bill.setId(id);
        billSer.update(bill);
    }

    @PostMapping("findByTime")
    private void findByTime(@RequestBody BillDto billDto){

        System.out.println(billDto.toString());

    }

    @GetMapping("findBySend/{num}")
    private List<Bill> findBySend(@PathVariable("num") Long number){
        return billSer.FindBySend(number);
    }


    @GetMapping("findByRece/{num}")
    private List<Bill> findByRece(@PathVariable("num") Long  number){
        return billSer.FindByReceive(number);
    }

    @GetMapping("findByNum/{num}")
    private List<Bill> findByNumber(@PathVariable("num") Long number){
        List<Bill> billList = new ArrayList<>();
        List<Bill> rece = billSer.FindByReceive(number);
        List<Bill> send = billSer.FindBySend(number);
        billList.addAll(rece);
        billList.addAll(send);
        return billList;
    }

    @GetMapping("getByUser")
    private List<Bill> getByUser(HttpServletRequest request){
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        User user = userService.userfindByEmail(userEmail);
        return billSer.findByUser(user);
    }




}
