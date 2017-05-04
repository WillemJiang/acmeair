package com.acmeair.web;

import javax.servlet.http.HttpServletRequest;

class CustomerValidationRuleImpl implements CustomerValidationRule {

    @Override
    public boolean validate(String customerId, HttpServletRequest request) {
        String loginUser = request.getHeader(CustomerREST.LOGIN_USER);
        return customerId.equals(loginUser);
    }
}
