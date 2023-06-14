package com.mno.teleconbill.reposity;


import com.mno.teleconbill.entity.Bill;
import com.mno.teleconbill.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface BillRepo extends JpaRepository<Bill,Long> {


    List<Bill> findByRecenumber(Long recenumber);
    List<Bill> findBySendnumber(Long sendnumber);

    List<Bill> findByUser(User user);

    List<Bill> findByDate(LocalDate date);


}
