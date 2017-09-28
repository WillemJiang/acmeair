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

import java.util.ArrayList;
import java.util.List;

/**
 * Just calling the problem injection handler to inject the problem for the manage console to find out
 */

public class ProblemInjector implements ProblemInterceptor {
    public static final String ISSUE_HEADER = "issue-header";

    private List<ProblemInterceptorSupport> interceptorChain = new ArrayList<>();

    public ProblemInjector() {
        // TODO we need find a way to find out the problem list, now we just use the hard coding
        interceptorChain.add(new CPUProblem());
        interceptorChain.add(new SlowTransactionProblem());
        interceptorChain.add(new ErrorProblem());
    }

    public ProblemInjector(List<ProblemInterceptorSupport> chain) {
        this.interceptorChain = chain;
    }

    public void handleRequest(final Request request) {
        for (ProblemInterceptorSupport interceptor : interceptorChain) {
            if (interceptor.isInjectProblem(request)) {
                interceptor.handleRequest(request);
            }
        }
    }


}
