package com.kuaidaoresume.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * List Account DTO
 *
 * @author Aaron Liu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountList {
    private List<AccountDto> accounts;
    private int limit;
    private int offset;
}
