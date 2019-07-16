/*
 * Copyright (c) 2019 Artistian and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package moe.yufan.pandora.test.log;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Pattern;

/**
 * Adds automatic test name logging. Every test which wants to log which test is currently
 * executed and why it failed. It was loaded by JUnit 5 SPI context.
 *
 * @author せいうはん
 * @version 1.0.0, 2019-07-16 21:00
 * @since 1.0.0, 2019-07-16 21:00
 */
public class TestLoggerListener implements TestExecutionListener {

    private static final Pattern REPEAT_PATTERN = Pattern.compile("repetition \\d+ of \\d+");

    private static String exceptionToString(Throwable t) {
        if (t == null) {
            return "(null)";
        }

        try {
            StringWriter stm = new StringWriter();
            PrintWriter wrt = new PrintWriter(stm);
            t.printStackTrace(wrt);
            wrt.close();
            return stm.toString();
        } catch (Throwable ignored) { // NOSONAR It's OK to catch this.
            return t.getClass().getName() + " (error while printing stack trace)";
        }
    }

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        if (testIdentifier.isTest() && !isRepeatTest(testIdentifier)) {
            log(testIdentifier)
                .info("\n================================================================================"
                        + "\nTest [{}] is running."
                        + "\n--------------------------------------------------------------------------------\n",
                    testIdentifier.getDisplayName());
        }
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (testIdentifier.isTest() && !isRepeatTest(testIdentifier)) {
            Status status = testExecutionResult.getStatus();
            if (status == Status.FAILED || status == Status.ABORTED) {
                String exceptionStr = exceptionToString(testExecutionResult.getThrowable().orElse(null));
                log(testIdentifier)
                    .error("\n--------------------------------------------------------------------------------"
                            + "\nTest [{}] failed with:\n{}"
                            + "\n================================================================================\n",
                        testIdentifier.getDisplayName(), exceptionStr);
            } else {
                log(testIdentifier)
                    .info("\n--------------------------------------------------------------------------------"
                            + "\nTest [{}] successfully run."
                            + "\n================================================================================\n",
                        testIdentifier.getDisplayName());
            }
        }
    }

    private boolean isRepeatTest(TestIdentifier testIdentifier) {
        return REPEAT_PATTERN.matcher(testIdentifier.getDisplayName()).matches();
    }

    private Logger log(TestIdentifier testIdentifier) {
        return LoggerFactory.getLogger(testIdentifier.getUniqueId());
    }
}
