package com.kuaidaoresume.account.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.account.dto.WechatAccountDto;
import com.kuaidaoresume.account.dto.ResumeDto;
import com.kuaidaoresume.account.model.Resume;
import com.kuaidaoresume.account.repo.ResumeRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.kuaidaoresume.account.AccountConstant;
import com.kuaidaoresume.account.dto.AccountDto;
import com.kuaidaoresume.account.model.Account;
import com.kuaidaoresume.account.model.AccountSecret;
import com.kuaidaoresume.account.props.AppProps;
import com.kuaidaoresume.account.dto.AccountList;
import com.kuaidaoresume.account.repo.AccountRepo;
import com.kuaidaoresume.account.repo.AccountSecretRepo;
import com.kuaidaoresume.account.service.helper.ServiceHelper;
import com.kuaidaoresume.common.api.BaseResponse;
import com.kuaidaoresume.common.api.ResultCode;
import com.kuaidaoresume.common.auditlog.LogEntry;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.AuthContext;
import com.kuaidaoresume.common.crypto.Sign;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.common.error.ServiceException;
import com.kuaidaoresume.mail.client.MailClient;
import com.kuaidaoresume.mail.dto.EmailRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static java.util.stream.Collectors.toList;

/**
 * Account services impls
 * @author Aaron Liu
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    static ILogger logger = SLoggerFactory.getLogger(AccountService.class);

    private final AccountRepo accountRepo;

    private final AccountSecretRepo accountSecretRepo;

    private final ResumeRepo resumeRepo;

    private final AppProps appProps;

    private final EnvConfig envConfig;

    private final MailClient mailClient;

    private final ServiceHelper serviceHelper;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public AccountDto get(String userId) {
        Account account = accountRepo.findAccountById(userId);
        if (account == null) {
            throw new ServiceException(String.format("User with id %s not found", userId));
        }
        return this.convertToDto(account);
    }

    public AccountDto getByOpenId(final String openId) {
        Account account = accountRepo.findAccountByOpenid(openId);
        if (account == null) {
            throw new ServiceException(String.format(("Wechat user with open id %s not found"), openId));
        }
        return this.convertToDto(account);
    }

    //public AccountDto create(String name, String email, String phoneNumber) {
    public AccountDto create(String name, String email) {
        if (StringUtils.hasText(email)) {
            // Check to see if account exists
            Account foundAccount = accountRepo.findAccountByEmail(email);
            if (foundAccount != null) {
                String errMsg = "A user with that email already exists. Try a password reset.";
                serviceHelper.handleException(logger, new ServiceException(errMsg), errMsg);
                throw new ServiceException(errMsg);
            }
        }
        /* not for phase I TODO:Woody
        if (StringUtils.hasText(phoneNumber)) {
            Account foundAccount = accountRepo.findAccountByPhoneNumber(phoneNumber);
            if (foundAccount != null) {
                throw new ServiceException("A user with that phonenumber already exists. Try a password reset");
            }
        }
        */

        // Column name/email/phone_number cannot be null
        if (name == null) { name = ""; }
        if (email == null) { email = ""; }
        // if (phoneNumber == null) { phoneNumber = ""; } not for phase I TODO:Woody

        Account account = Account.builder()
                .email(email).name(name).loginType("normal") // not for phase I TODO:Woody .phoneNumber(phoneNumber)
                .build();
        //account.setPhotoUrl(Helper.generateGravatarUrl(account.getEmail())); //TODO: Aaron Liu add wechat/fb avator sync
        account.setMemberSince(Instant.now());

        storeAccountAndSyncUser(account);

        if (StringUtils.hasText(email)) {
            // Email confirmation

            String emailName = name;
            if (StringUtils.isEmpty(emailName)) {
                emailName = AccountConstant.GREETING_WORD_CN_ZH;
            }

            String subject = AccountConstant.ACTIVATE_ACCOUNT_EMAIL_TITLE_CN_ZH;
            //this.sendEmail(account.getId(), email, emailName, subject, AccountConstant.ACTIVATE_ACCOUNT_TMPL, true); // for english
            this.sendEmail(account.getId(), email, emailName, subject, AccountConstant.ACTIVATE_ACCOUNT_TMPL_CN_ZH, true); // for chinese
        }

        LogEntry auditLog = LogEntry.builder()
                .authorization(AuthContext.getAuthz())
                .currentUserId(AuthContext.getUserId())
                .targetType("account")
                .targetId(account.getId())
                .updatedContents(account.toString())
                .build();

        logger.info("created account", auditLog);

        AccountDto accountDto = this.convertToDto(account);
        return accountDto;
    }

    public AccountDto createWechatTypeAccount(WechatAccountDto weChatAccountDto) {
        if (StringUtils.hasText(weChatAccountDto.getEmail())) {
            // Check to see if account exists
            Account foundAccount = accountRepo.findAccountByEmail(weChatAccountDto.getEmail());
            if (foundAccount != null) {
                throw new ServiceException("A wechat user with that email already exists. Try to not use wechat login.");
            }
        }

        final String openId = weChatAccountDto.getOpenid();
        Account account = Account.builder()
                .email(openId + "@kuaidaoemail.com")
                .name("未命名")
                .openid(openId)
//                .photoUrl(weChatAccountDto.getHeadimgurl()) // TODO: Add these back when we add in avatar
                .loginType("wechat")
                .build();

        account.setMemberSince(Instant.now());
        storeAccountAndSyncUser(account);
        logCreateAccountInfo(account);
        AccountDto accountDto = this.convertToDto(account);
        return accountDto;
    }

    // GetOrCreate is for internal use by other APIs to match a user based on their phonenumber or email.
    // public AccountDto getOrCreate(String name, String email, String phoneNumber) {  not for phase I TODO:Woody
    public AccountDto getOrCreate(String name, String email) {
        // rely on downstream permissions
        // check for existing user
        Account existingAccount = null;
        if (StringUtils.hasText(email)) {
            existingAccount = accountRepo.findAccountByEmail(email);
        }
        /* not for phase I TODO:Woody
        if (existingAccount == null && StringUtils.hasText(phoneNumber)) {
            existingAccount = accountRepo.findAccountByPhoneNumber(phoneNumber);
        }
        */

        if (existingAccount != null) {
            return this.convertToDto(existingAccount);
        }
        //return this.create(name, email, phoneNumber);  not for phase I TODO:Woody
        return this.create(name, email);
    }

    /* not for phase I TODO:Woody
    public AccountDto getAccountByPhoneNumber(String phoneNumber) {
        Account account = accountRepo.findAccountByPhoneNumber(phoneNumber);
        if (account == null) {
            throw new ServiceException(ResultCode.NOT_FOUND, "User with specified phonenumber not found");
        }
        return this.convertToDto(account);
    }
    */
    public AccountList list(int offset, int limit) {
        if (limit <= 0) {
            limit = 10;
        }

        Pageable pageRequest = PageRequest.of(offset, limit);
        Page<Account> accountPage = accountRepo.findAll(pageRequest);
        List<AccountDto> accountDtoList = accountPage.getContent()
                .stream()
                .map(account -> convertToDto(account))
                .collect(toList());

        return AccountList.builder()
                .limit(limit)
                .offset(offset)
                .accounts(accountDtoList)
                .build();
    }

    public AccountDto update(AccountDto newAccountDto) {
        Account newAccount = this.convertToModel(newAccountDto);

        Account existingAccount = accountRepo.findAccountById(newAccount.getId());
        if (existingAccount == null) {
            throw new ServiceException(ResultCode.NOT_FOUND,
                    String.format("User with id %s not found", newAccount.getId()));
        }
        entityManager.detach(existingAccount);

        if (!serviceHelper.isAlmostSameInstant(newAccount.getMemberSince(), existingAccount.getMemberSince())) {
            throw new ServiceException(ResultCode.REQ_REJECT,
                    "You cannot modify the member_since date");
        }

        if (StringUtils.hasText(newAccount.getEmail()) && !newAccount.getEmail().equals(existingAccount.getEmail())) {
            Account foundAccount = accountRepo.findAccountByEmail(newAccount.getEmail());
            if (foundAccount != null) {
                String errMsg = "A user with that email already exists. Try a password reset";
                serviceHelper.handleException(logger, new ServiceException(errMsg), errMsg);
                throw new ServiceException(ResultCode.REQ_REJECT, errMsg);
            }
        }
/* not for phase I TODO:Woody
        if (StringUtils.hasText(newAccount.getPhoneNumber()) && !newAccount.getPhoneNumber().equals(existingAccount.getPhoneNumber())) {
            Account foundAccount = accountRepo.findAccountByPhoneNumber(newAccount.getPhoneNumber());
            if (foundAccount != null) {
                throw new ServiceException(ResultCode.REQ_REJECT,
                        "A user with that phonenumber already exists. Try a password reset");
            }
        }
*/
        if (AuthConstant.AUTHORIZATION_AUTHENTICATED_USER.equals(AuthContext.getAuthz())) {
            if (!existingAccount.isConfirmedAndActive() && newAccount.isConfirmedAndActive()) {
                throw new ServiceException(ResultCode.REQ_REJECT,
                        "You cannot activate this account");
            }
            if (existingAccount.isSupport() != newAccount.isSupport()) {
                throw new ServiceException(ResultCode.REQ_REJECT,
                        "You cannot change the support parameter");
            }
            /*
            if (!existingAccount.getPhotoUrl().equals(newAccount.getPhotoUrl())) {
                throw new ServiceException(ResultCode.REQ_REJECT,
                        "You cannot change the photo through this endpoint (see docs)");
            }
            */
            // User can request email change - not do it :-)
            if (!existingAccount.getEmail().equals(newAccount.getEmail())) {
                this.requestEmailChange(newAccount.getId(), newAccount.getEmail());
                // revert
                newAccount.setEmail(existingAccount.getEmail());
            }
        }
        // newAccount.setPhotoUrl(Helper.generateGravatarUrl(newAccount.getEmail()));
        try {
            if(existingAccount.getResumes() != null) {
                for(Resume resume : existingAccount.getResumes()) {
                    /**
                     * 1. we just need to set here because the saveAccountNonEssentialInfo later is transactional and we have cascade
                     * 2. new account already has the resumes because it was foundByid System.out.println(newAccount.getResumes());
                     * 3. because we're only updating account, resumes should point to the "new" account,
                     *    new in the sense that it is updated and needs to be cascaded, whereas account still points to the same resumes.
                     *    In a sense we're only updating one side of the relationship.
                     * TODO: decouple resume account as much as possible to a transit/process services or consumer side.
                     */
                    resume.setAccount(newAccount);
                }
            }
            accountRepo.saveAccountNonEssentialInfo(newAccount.getOpenid(), newAccount.getName(),
                    newAccount.getPhotoUrl(), newAccount.getLoginType(), newAccount.getId());
        } catch (Exception ex) {
            String errMsg = "Could not update the user account";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }

        serviceHelper.syncUserAsync(newAccount.getId());

        LogEntry auditLog = LogEntry.builder()
                .authorization(AuthContext.getAuthz())
                .currentUserId(AuthContext.getUserId())
                .targetType("account")
                .targetId(newAccount.getId())
                .originalContents(existingAccount.toString())
                .updatedContents(newAccount.toString())
                .build();

        logger.info("updated account", auditLog);

        // If account is being activated, or if phone number is changed by current user - send text
        /*if (newAccount.isConfirmedAndActive() &&
                StringUtils.hasText(newAccount.getPhoneNumber()) &&
                !newAccount.getPhoneNumber().equals(existingAccount.getPhoneNumber())) {
            serviceHelper.sendSmsGreeting(newAccount.getId());
        }*/

        this.trackEventWithAuthCheck("account_updated");
        AccountDto accountDto = this.convertToDto(newAccount);
        return accountDto;
    }

    // RequestPasswordReset sends an email to a user with a password reset link
    public void requestPasswordReset(String email) {
        String newEmail = email.toLowerCase().trim();

        Account account = accountRepo.findAccountByEmail(email);
        if(account == null) {
            throw new ServiceException(ResultCode.NOT_FOUND, "No user with that email exists");
        }

        //String subject = "Reset your Smartresume password";
        String subject = AccountConstant.RESET_PASSWORD_EMAIL_TITLE_CN_ZH;


        boolean activate = false; // reset
        //String tmpl = AccountConstant.RESET_PASSWORD_TMPL; // for english
        String tmpl = AccountConstant.RESET_PASSWORD_TMPL_CN_ZH; // for chinese
        if (!account.isConfirmedAndActive()) {
            // Not actually active - make some tweaks for activate instead of password reset
            activate = true; // activate
            subject = AccountConstant.ACTIVATE_ACCOUNT_EMAIL_TITLE_CN_ZH;
            //tmpl = AccountConstant.ACTIVATE_ACCOUNT_TMPL; // for english
            tmpl = AccountConstant.ACTIVATE_ACCOUNT_TMPL_CN_ZH; // for chinese
        }

        // Send verification email
        this.sendEmail(account.getId(), email, account.getName(), subject, tmpl, activate);
    }

    // requestEmailChange sends an email to a user with a confirm email link
    public void requestEmailChange(String userId, String email) {
        Account account = accountRepo.findAccountById(userId);
        if (account == null) {
            throw new ServiceException(ResultCode.NOT_FOUND, String.format("User with id %s not found", userId));
        }

        String subject = "Confirm Your New Email Address";
        //this.sendEmail(account.getId(), email, account.getName(), subject, AccountConstant.CONFIRM_EMAIL_TMPL, true); //for english
        this.sendEmail(account.getId(), email, account.getName(), subject, AccountConstant.CONFIRM_EMAIL_TMPL_CN_ZH, true); // for chinese.
    }

    // ChangeEmail sets an account to active and updates its email. It is
    // used after a user clicks a confirmation link in their email.
    public void changeEmailAndActivateAccount(String userId, String email) {

        int affected = accountRepo.updateEmailAndActivateById(email, userId);
        if (affected != 1) {
            throw new ServiceException(ResultCode.NOT_FOUND, "user with specified id not found");
        }

        serviceHelper.syncUserAsync(userId);

        LogEntry auditLog = LogEntry.builder()
                .authorization(AuthContext.getAuthz())
                .currentUserId(AuthContext.getUserId())
                .targetType("account")
                .targetId(userId)
                .updatedContents(email)
                .build();

        logger.info("changed email", auditLog);

        this.trackEventWithAuthCheck("email_updated");
    }

    public void updatePassword(String userId, String password) {
        String pwHash = passwordEncoder.encode(password);

        int affected = accountSecretRepo.updatePasswordHashById(pwHash, userId);
        if (affected != 1) {
            throw new ServiceException(ResultCode.NOT_FOUND, "user with specified id not found");
        }

        LogEntry auditLog = LogEntry.builder()
                .authorization(AuthContext.getAuthz())
                .currentUserId(AuthContext.getUserId())
                .targetType("account")
                .targetId(userId)
                .build();

        logger.info("updated password", auditLog);

        this.trackEventWithAuthCheck("password_updated");
    }

    public AccountDto verifyPassword(String email, String password) {
        AccountSecret accountSecret = accountSecretRepo.findAccountSecretByEmail(email);
        if (accountSecret == null) {
            throw new ServiceException(ResultCode.NOT_FOUND,
                    "account with specified email not found");
        }

        if (!accountSecret.isConfirmedAndActive()) {
            throw new ServiceException(ResultCode.REQ_REJECT,
                    "This user has not confirmed their account");
        }

        if (StringUtils.isEmpty(accountSecret.getPasswordHash())) {
            throw new ServiceException(ResultCode.REQ_REJECT,
                    "This user has not set up their password");
        }

        if (!passwordEncoder.matches(password, accountSecret.getPasswordHash())) {
            throw new ServiceException(ResultCode.UN_AUTHORIZED,
                    "Incorrect password");
        }

        Account account = accountRepo.findAccountById(accountSecret.getId());
        if (account == null) {
            throw new ServiceException(String.format(
                    "User with id %s not found", accountSecret.getId()));
        }

        // You shall pass
        AccountDto accountDto = this.convertToDto(account);
        return accountDto;
    }

    void sendEmail(String userId, String email, String name, String subject, String template, boolean activateOrConfirm) {
        String token = null;
        try {
            token = Sign.generateEmailConfirmationToken(userId, email, appProps.getSigningSecret());
        } catch(Exception ex) {
            String errMsg = "Could not create token";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }

        String pathFormat = "/activate/%s";
        if (!activateOrConfirm) {
            pathFormat = "/reset/%s";
        }
        String path = String.format(pathFormat, token);
        URI link = null;
        try {
            link = new URI("http", "www." + envConfig.getExternalApex(), path, null);
        } catch (URISyntaxException ex) {
            String errMsg = "Could not create activation url";
            if (!activateOrConfirm) {
                errMsg = "Could not create reset url";
            }
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }
        Map<String, Object> emailModel = new HashMap();

        String htmlBody = null;
        if (activateOrConfirm) { // active or confirm
            htmlBody = String.format(template, name, link.toString(), link.toString(), link.toString());
            emailModel.put("type", "activateOrConfirm");
        } else { // reset
            htmlBody = String.format(template, link.toString(), link.toString());
            emailModel.put("type", "resetPassword");
        }

        emailModel.put("name", name);
        emailModel.put("link", link);

        EmailRequest emailRequest = EmailRequest.builder()
                .to(email)
                .from("info@eyeshightc.com")
                .name(name)
                .subject(subject)
                .htmlBody(htmlBody)
                .model(emailModel)
                .build();

        BaseResponse baseResponse = null;
        try {
            baseResponse = mailClient.send(emailRequest);
        } catch (Exception ex) {
            String errMsg = "Unable to send email to " + name + ", Email address: " + email + ".";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }
        if (!baseResponse.isSuccess()) {
            serviceHelper.handleError(logger, baseResponse.getMessage());
            throw new ServiceException(baseResponse.getMessage());
        }
    }

    public void trackEvent(String userId, String eventName) {
        // serviceHelper.trackEventAsync(userId, eventName);
        // Don't need CRM tracking in phase I.
    }

    public void syncUser(String userId) {
        serviceHelper.syncUserAsync(userId);
    }

    public void addResumeToAccount(String userId, ResumeDto resumeDto) {
        Resume resume = modelMapper.map(resumeDto, Resume.class);
        Account account = accountRepo.findAccountById(userId);
        account.getResumes().add(resume);
        resume.setAccount(account);
        accountRepo.save(account);
    }

    public void removeResumeFromAccount(String userId, ResumeDto resumeDto) {
        Resume resume = modelMapper.map(resumeDto, Resume.class);
        Account account = accountRepo.findAccountById(userId);
        resume.setAccount(account);
        account.getResumes().remove(resume);
        accountRepo.save(account);
        resumeRepo.delete(resume);
    }

    private AccountDto convertToDto(Account account) {
        return modelMapper.map(account, AccountDto.class);
    }

    private Account convertToModel(AccountDto accountDto) {
        return modelMapper.map(accountDto, Account.class);
    }

    private void trackEventWithAuthCheck(String eventName) {
        String userId = AuthContext.getUserId();
        if (StringUtils.isEmpty(userId)) {
            // Not an action performed by a normal user
            // (noop - not an view)
            return;
        }

        this.trackEvent(userId, eventName);
    }

    private void storeAccountAndSyncUser(Account account) {
        try {
            accountRepo.save(account);
        } catch (Exception ex) {
            String errMsg = "Could not create user account";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }

        serviceHelper.syncUserAsync(account.getId());
    }

    private void logCreateAccountInfo(Account account) {
        LogEntry auditLog = LogEntry.builder()
                .authorization(AuthContext.getAuthz())
                .currentUserId(AuthContext.getUserId())
                .targetType("account")
                .targetId(account.getId())
                .updatedContents(account.toString())
                .build();
        logger.info("created account", auditLog);
    }
}
