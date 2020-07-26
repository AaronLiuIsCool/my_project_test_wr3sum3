package com.kuaidaoresume.common.dto;

/*
 * For UPDATE and DELETE cases in which the persisted
 * entity Id is required
 * */
public interface PersistedEntityDto<ID> {
    public ID getId();
}
