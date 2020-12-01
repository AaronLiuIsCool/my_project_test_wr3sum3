package com.kuaidaoresume.account.repo;

import com.kuaidaoresume.account.model.Resume;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.kuaidaoresume.account.model.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import com.kuaidaoresume.account.model.AccountSecret;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class AccountRepoTest {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private AccountSecretRepo accountSecretRepo;

    private Account newAccount;

    @Before
    public void setUp() {
        newAccount = Account.builder()
                .name("testAccount")
                .email("test@kuaidaoresume.net")
                .memberSince(LocalDateTime.of(2020, 1, 20, 12, 50).atZone(ZoneId.systemDefault()).toInstant())
                .confirmedAndActive(false)
                .photoUrl("https://kuaidaoresume.com/photo/test.png")
                //.phoneNumber("6041234567")
                .resumes(new ArrayList<>())
                .support(false)
                .build();
        // sanity check
        accountRepo.deleteAll();
    }

    @Test//(expected = DuplicateKeyException.class)
    public void createSampleAccount() {
        accountRepo.save(newAccount);
        assertTrue(accountRepo.existsById(newAccount.getId()));
    }

    @Test
    public void saveAccountWithResume() {
        Resume resume = Resume.builder().id("aUuid").alias("a").createdAt(Instant.now()).build();
        resume.setAccount(newAccount);
        newAccount.getResumes().add(resume);
        Account saved = accountRepo.save(newAccount);
        assertNotNull(saved);
    }

    @Test
    public void getAccountById() {
        accountRepo.save(newAccount);
        assertEquals(1, accountRepo.count());
        Account foundAccount = accountRepo.findById(newAccount.getId()).get();
        validateAccount(newAccount, foundAccount);
    }

    @Test
    public void findAccountByEmail() {
        // not existing
        Account foundAccount = accountRepo.findAccountByEmail("notexisting@kuaidaoresume.net");
        assertNull(foundAccount);

        accountRepo.save(newAccount);
        assertEquals(1, accountRepo.count());
        foundAccount = accountRepo.findAccountByEmail(newAccount.getEmail());
        assertNotNull(foundAccount);
        assertEquals(newAccount.getId(), foundAccount.getId());
    }

    /*@Test
    public void findAccountByPhoneNumber() {
        // not existing
        Account foundAccount = accountRepo.findAccountByPhoneNumber("6041234567");
        assertNull(foundAccount);

        // create new
        accountRepo.save(newAccount);
        assertEquals(1, accountRepo.count());
        foundAccount = accountRepo.findAccountByPhoneNumber(newAccount.getPhoneNumber());
        assertEquals(newAccount.getId(), foundAccount.getId());
    }*/

    @Test
    public void listAccount() {
        Pageable pageRequest = PageRequest.of(0, 2);
        // test empty
        Page<Account> accounts = accountRepo.findAll(pageRequest);
        assertEquals(0, accounts.getTotalElements());

        // create 1 new
        accountRepo.save(newAccount);
        assertEquals(1, accountRepo.count());

        // create 2 more
        newAccount.setId(null);
        accountRepo.save(newAccount);
        assertEquals(2, accountRepo.count());
        newAccount.setId(null);
        accountRepo.save(newAccount);
        assertEquals(3, accountRepo.count());
        accounts = accountRepo.findAll(pageRequest);
        assertEquals(2, accounts.getNumberOfElements());
        pageRequest = pageRequest.next();
        accounts = accountRepo.findAll(pageRequest);
        assertEquals(1, accounts.getNumberOfElements());
        assertEquals(2, accounts.getTotalPages());
        assertEquals(3, accounts.getTotalElements());
    }

    @Test
    public void updateAccount() {
        // create new
        accountRepo.save(newAccount);
        assertEquals(1, accountRepo.count());

        Account toUpdateAccount = newAccount;
        toUpdateAccount.setName("update");
        toUpdateAccount.setEmail("update@kuaidaoresume.com");
        accountRepo.save(toUpdateAccount);
        Account updatedAccount = accountRepo.save(toUpdateAccount);
        Account foundAccount = accountRepo.findById(updatedAccount.getId()).get();
        validateAccount(updatedAccount, foundAccount);

        toUpdateAccount.setConfirmedAndActive(true);
        toUpdateAccount.setSupport(true);
        //toUpdateAccount.setPhoneNumber("19001900190");
        toUpdateAccount.setPhotoUrl("http://kuaidaoresume.net/photo/update.png");
        updatedAccount = accountRepo.save(toUpdateAccount);
        foundAccount = accountRepo.findById(updatedAccount.getId()).get();
        validateAccount(updatedAccount, foundAccount);
    }

    @Test
    public void updateAccountWithoutResumesSnapshot() {
        // setup
        accountRepo.save(newAccount);
        assertEquals(1, accountRepo.count());
        Account toUpdateAccount = newAccount;
        toUpdateAccount.setName("update");
        toUpdateAccount.setEmail("update@kuaidaoresume.com");

        Resume newResume = Resume.builder()
                .id("SHA123")
                .alias("a resume alias string")
                .createdAt(LocalDateTime.of(2010, 1, 20, 12, 50)
                        .atZone(ZoneId.systemDefault()).toInstant())
                .thumbnailUri("https://a_storage_url")
                .build();
        List<Resume> updateResumes = new ArrayList<>();
        updateResumes.add(newResume);
        toUpdateAccount.setResumes(updateResumes);

        // execution
        Account foundAccount = null;
        int isSuccess = -1;
        try {
            isSuccess = accountRepo.saveAccountNonEssentialInfo(toUpdateAccount.getOpenid(),
                    toUpdateAccount.getName(), toUpdateAccount.getPhotoUrl(), toUpdateAccount.getLoginType(), toUpdateAccount.getId());
            foundAccount = accountRepo.findById(toUpdateAccount.getId()).get();
        }
        catch (Exception exception) {
            fail("Unexpected exception at updateAccountWithNewResumesSnapshot test case exception:" + exception.getMessage());
        }

        // check result
        assertEquals(1, isSuccess);
        assertEquals(toUpdateAccount.getOpenid(), foundAccount.getOpenid());
        assertEquals(toUpdateAccount.getName(), foundAccount.getName());
        assertEquals(toUpdateAccount.getPhotoUrl(), foundAccount.getPhotoUrl());
        assertEquals(toUpdateAccount.getLoginType(), foundAccount.getLoginType());
    }

    @Test
    public void updateEmailAndActivateById() {
        // create new
        Account account = accountRepo.save(newAccount);
        assertEquals(1, accountRepo.count());
        assertFalse(account.isConfirmedAndActive());

        String toUpdateEmail = "update@kuaidaoresume.com";
        int result = accountRepo.updateEmailAndActivateById(toUpdateEmail, newAccount.getId());
        assertEquals(1, result);

        Account updatedAccount = accountRepo.findAccountByEmail(toUpdateEmail);
        assertEquals(toUpdateEmail, updatedAccount.getEmail());
        assertTrue(updatedAccount.isConfirmedAndActive());
    }

    @Test
    public void updatePasswordById() {
        // create new
        accountRepo.save(newAccount);
        assertEquals(1, accountRepo.count());

        String passwordHash = "testhash";
        int result = accountSecretRepo.updatePasswordHashById(passwordHash, newAccount.getId());
        assertEquals(1, result);

        AccountSecret foundAccountSecret = accountSecretRepo.findAccountSecretByEmail(newAccount.getEmail());
        assertNotNull(foundAccountSecret);
        assertEquals(newAccount.getId(), foundAccountSecret.getId());
        assertEquals(newAccount.isConfirmedAndActive(), foundAccountSecret.isConfirmedAndActive());
        assertEquals(passwordHash, foundAccountSecret.getPasswordHash());
    }

    @After
    public void destroy() {
        accountRepo.deleteAll();
    }

    private void validateAccount(Account newAccount, Account account) {
        assertEquals(account.getId(), newAccount.getId());
        assertEquals(account.getName(), newAccount.getName());
        assertEquals(account.getEmail(), newAccount.getEmail());
        assertEquals(account.getMemberSince(), newAccount.getMemberSince());
        assertEquals(account.isConfirmedAndActive(), newAccount.isConfirmedAndActive());
        assertEquals(account.getPhotoUrl(), newAccount.getPhotoUrl());
        assertEquals(account.isSupport(), newAccount.isSupport());
        assertEquals(account.getResumes().size(), newAccount.getResumes().size()); //Need more tests for resumes.
    }
}
