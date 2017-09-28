/*
 *  Copyright 2017 Huawei Technologies Co., Ltd
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.acmeair.problems;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProblemInjectorTest {
    @Mock ProblemInterceptorSupport myProblemInterceptor;
    
    @Mock Request request;
    
    ProblemInjector injector;
    
    @Before
    public void setUp() {
        when(myProblemInterceptor.getProblemHeader()).thenReturn("myError");
        when(myProblemInterceptor.isInjectProblem(request)).thenCallRealMethod();
        injector = new ProblemInjector(Arrays.asList(myProblemInterceptor));
    }
    
    @Test
    public void handlerInvokedTest() throws Exception {
        when(request.getHeader(ProblemInjector.ISSUE_HEADER)).thenReturn("myError, otherError");
        injector.handleRequest(request);
        verify(myProblemInterceptor).handleRequest(request);
    }
    
    @Test
    public void handlerNotInvokedTest() throws Exception {
        when(request.getHeader(ProblemInjector.ISSUE_HEADER)).thenReturn("otherError");
        injector.handleRequest(request);
        verify(myProblemInterceptor, never()).handleRequest(request);
    }
}
    

