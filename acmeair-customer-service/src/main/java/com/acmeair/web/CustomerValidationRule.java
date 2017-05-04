package com.acmeair.web;

import javax.servlet.http.HttpServletRequest;

interface CustomerValidationRule {
    CustomerValidationRule alwaysPassValidationRule = ((customerId, request) -> true);

    boolean validate(String customerId, HttpServletRequest request);
}
