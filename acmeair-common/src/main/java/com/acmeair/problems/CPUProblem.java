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

/**
 * Here we just use the for loop the eat up the CPU
 */
public class CPUProblem extends ProblemInterceptorSupport implements ProblemInterceptor {
    public static final String CPU_ISSUE_HEADER = "cpu-issue";

    private static Logger log = LoggerFactory.getLogger(CPUProblem.class);
    protected String getProblemHeader() {
        return CPU_ISSUE_HEADER;
    }

    private void eatUpCPU(int mathCount) {
        for(int i = 1 ; i < 1000000; i++) {
            for(int j=1; j<mathCount;j++){
                Math.sqrt(i);
            }
        }
    }

    @Override
    public void handleRequest(Request request) {
         if (isInjectProblem(request)) {
             String size = request.getHeader("size");
             if(size == null || "".equals(size)){
                 size = "1";
             }
             log.info("injectIssue: " + CPU_ISSUE_HEADER +" mathCount: "+size);
             int mathCount = Integer.parseInt(size);

             eatUpCPU(mathCount);
         }
    }

}
