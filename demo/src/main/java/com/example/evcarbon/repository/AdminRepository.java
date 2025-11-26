package com.example.evcarbon.repository;

import com.example.evcarbon.model.AdminUser;
import com.example.evcarbon.model.Transaction;
import com.example.evcarbon.model.CarbonCredit;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AdminRepository {

    public final List<AdminUser> users = new ArrayList<>();
    public final List<Transaction> transactions = new ArrayList<>();
    public final List<CarbonCredit> credits = new ArrayList<>();
}
