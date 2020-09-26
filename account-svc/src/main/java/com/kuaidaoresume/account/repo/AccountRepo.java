package com.kuaidaoresume.account.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.kuaidaoresume.account.model.Account;

@Repository
public interface AccountRepo extends JpaRepository<Account, String> {

    Account findAccountById(String id);

    Account findAccountByEmail(String email);

    // Account findAccountByPhoneNumber(String phoneNumber); not for phase I TODO:Woody

    @Modifying(clearAutomatically = true)
    @Query("update Account account set account.email = :email, account.confirmedAndActive = true where account.id = :id")
    @Transactional
    int updateEmailAndActivateById(@Param("email") String email, @Param("id") String id);

    @Modifying(clearAutomatically = true)
    @Query("update Account account set account.openid = :openid, account.name = :name, account.photoUrl = :photoUrl, account.loginType = :loginType, account.confirmedAndActive = true where account.id = :id")
    @Transactional
    int saveAccountNonEssentialInfo(@Param("openid") String openid, @Param("name") String name,
                                        @Param("photoUrl") String photoUrl, @Param("loginType") String loginType,
                                        @Param("id") String id);

}
