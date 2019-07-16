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
package moe.yufan.pandora.test.extensions;

import moe.yufan.pandora.test.extensions.internal.CheckExitCalled;
import moe.yufan.pandora.test.extensions.internal.NoExitSecurityManager;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.platform.commons.support.ReflectionSupport;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.System.getSecurityManager;
import static java.lang.System.setSecurityManager;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This class is original inspired from project {@code system-rules}, rewrite it to adapt JUnit5.
 * It is a JUnit Jupiter extension allows in-test specification of expected {@code System.exit(...)} calls.
 *
 * @author せいうはん
 * @version 1.0.0, 2019-07-16 21:00
 * @link https://github.com/stefanbirkner/system-rules/blob/master/src/main/java/org/junit/contrib/java/lang/system/ExpectedSystemExit.java
 * @since 1.0.0, 2019-07-16 21:00
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@ExtendWith(ExpectedSystemExit.Extension.class)
public @interface ExpectedSystemExit {

    class Extension implements BeforeEachCallback, AfterEachCallback, BeforeAllCallback, ParameterResolver, TestExecutionExceptionHandler {

        private NoExitSecurityManager noExitSecurityManager;
        private SecurityManager originalManager;

        @Override
        public void beforeAll(ExtensionContext context) throws Exception {
            noExitSecurityManager = new NoExitSecurityManager(getSecurityManager());
        }

        @Override
        public void beforeEach(ExtensionContext context) {
            originalManager = getSecurityManager();
            setSecurityManager(noExitSecurityManager);
        }

        @Override
        public void afterEach(ExtensionContext context) {
            NoExitSecurityManager securityManager = (NoExitSecurityManager) getSecurityManager();
            ExitCapture exitCapture = getExitCapture(context);

            if (exitCapture.expectExit) {
                checkSystemExit(securityManager, exitCapture);
            }
            setSecurityManager(originalManager);
        }

        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
            boolean isTestMethodLevel = extensionContext.getTestMethod().isPresent();
            boolean isOutputCapture = parameterContext.getParameter().getType() == ExitCapture.class;
            return isTestMethodLevel && isOutputCapture;
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
            return getExitCapture(extensionContext);
        }

        @Override
        public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
            ExitCapture exitCapture = getExitCapture(context);
            if (!(throwable instanceof CheckExitCalled) || !exitCapture.expectExit) {
                throw throwable;
            }
        }

        private void checkSystemExit(NoExitSecurityManager securityManager, ExitCapture exitCapture) {
            if (securityManager.isCheckExitCalled()) {
                exitCapture.handleSystemExitWithStatus(securityManager.getStatusOfFirstCheckExitCall());
            } else {
                exitCapture.handleMissingSystemExit();
            }
        }

        private ExitCapture getExitCapture(ExtensionContext context) {
            Store store = context.getStore(Namespace.create(getClass(), context.getRequiredTestMethod()));
            return store.getOrComputeIfAbsent(ExitCapture.class, ReflectionSupport::newInstance, ExitCapture.class);
        }
    }

    class ExitCapture {

        private boolean expectExit = false;
        private Integer expectedStatus = null;

        public void expectSystemExit() {
            expectExit = true;
        }

        public void expectSystemExitWithStatus(int status) {
            expectSystemExit();
            expectedStatus = status;
        }

        private void handleMissingSystemExit() {
            if (expectExit) {
                fail("System.exit has not been called.");
            }
        }

        private void handleSystemExitWithStatus(int status) {
            if (!expectExit) {
                fail("Unexpected call of System.exit(" + status + ").");
            } else if (expectedStatus != null) {
                assertEquals(expectedStatus, Integer.valueOf(status), "Wrong exit status");
            }
        }
    }
}
