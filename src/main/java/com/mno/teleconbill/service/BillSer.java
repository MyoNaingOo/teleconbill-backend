package com.mno.teleconbill.service;


import com.mno.teleconbill.entity.Bill;
import com.mno.teleconbill.entity.User;
import com.mno.teleconbill.reposity.BillRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillSer {

    @Autowired
    private BillRepo billRepo;


    public void saveBill(Bill bill){
        billRepo.save(bill);
    }

    public List<Bill> billList() {
        return billRepo.findAll(Sort.by("id").descending());
    }

    public List<Bill> FindByReceive(Long rece){
        return billRepo.findByRecenumber(rece);
    }

    public List<Bill> FindBySend(Long send){
        return billRepo.findBySendnumber(send);
    }





    public List<Bill> FindByTime(LocalDate before, LocalDate after){
        List<Bill>  billList= billRepo.findAll(Sort.by("id").descending());
        List<Bill> bills = new ArrayList<>();
        for (Bill bill:billList ) {

            boolean beforeBool = bill.getDate().isBefore(before);
            boolean afterBool = bill.getDate().isAfter(after);
            if (beforeBool && afterBool){
                bills.add(bill);
            }

        }

        return bills;
    }

    public void deleteBill(Long id){
        billRepo.deleteById(id);
    }

    public void update(Bill bill){
        billRepo.save(bill);
    }

    public List<Bill> findByUser(User user){
        return billRepo.findByUser(user);
    }

}
