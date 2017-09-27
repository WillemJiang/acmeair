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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlowTransactionProblem extends ProblemInterceptorSupport implements ProblemInterceptor {
    public static final String SLOW_TRANSACTION_ISSUE_HEADER = "slow-transaction-issue";
    public static final String SLEEP_TIME = "sleep-time";
    
    
    private static Logger log = LoggerFactory.getLogger(SlowTransactionProblem.class);

    @Override
    protected String getProblemHeader() {
        return SLOW_TRANSACTION_ISSUE_HEADER;
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // we just ignore the exception here
        }
    }

    @Override
    public void handleRequest(Request request) {

        if (isInjectProblem(request)) {
            String sleepTimeReq = request.getHeader(SLEEP_TIME);
            if(sleepTimeReq == null || "".equals(sleepTimeReq)){
                sleepTimeReq = "1";
            }
            log.info(" injectIssue: "+SLOW_TRANSACTION_ISSUE_HEADER + " sleepTime:" + sleepTimeReq);
            int sleepTime = Integer.parseInt(sleepTimeReq);
            sleep(sleepTime * 1000);
        }
    }
    
}
