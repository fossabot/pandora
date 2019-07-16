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

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.ReflectionSupport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@code @CaptureSystemOutput} is a JUnit JUpiter extension for capturing
 * output to {@code System.out} and {@code System.err} with expectations
 * supported via assertj-core matchers.
 *
 * @author せいうはん
 * @version 1.0.0, 2019-07-16 21:00
 * @since 1.0.0, 2019-07-16 21:00
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@ExtendWith(CaptureSystemOutput.Extension.class)
@SuppressWarnings("squid:S106")
public @interface CaptureSystemOutput {

    class Extension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

        @Override
        public void beforeEach(ExtensionContext context) {
            getOutputCapture(context).captureOutput();
        }

        @Override
        public void afterEach(ExtensionContext context) {
            OutputCapture outputCapture = getOutputCapture(context);
            try {
                if (outputCapture.assertSupplier != null) {
                    String output = outputCapture.toString();
                    outputCapture.assertSupplier.apply(output);
                }
            } finally {
                outputCapture.releaseOutput();
            }
        }

        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
            boolean isTestMethodLevel = extensionContext.getTestMethod().isPresent();
            boolean isOutputCapture = parameterContext.getParameter().getType() == OutputCapture.class;
            return isTestMethodLevel && isOutputCapture;
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
            return getOutputCapture(extensionContext);
        }

        private OutputCapture getOutputCapture(ExtensionContext context) {
            return getOrComputeIfAbsent(getStore(context), OutputCapture.class);
        }

        private <V> V getOrComputeIfAbsent(Store store, Class<V> type) {
            return store.getOrComputeIfAbsent(type, ReflectionSupport::newInstance, type);
        }

        private Store getStore(ExtensionContext context) {
            return context.getStore(Namespace.create(getClass(), context.getRequiredTestMethod()));
        }

    }

    /**
     * {@code OutputCapture} captures output to {@code System.out} and {@code System.err}.
     *
     * <p>To obtain an instance of {@code OutputCapture}, declare a parameter of type
     * {@code OutputCapture} in a JUnit Jupiter {@code @Test}, {@code @BeforeEach},
     * or {@code @AfterEach} method.
     *
     * <p>{@linkplain #expect Expectations} are supported via assertj-core matchers.
     *
     * <p>To obtain all output to {@code System.out} and {@code System.err}, simply
     * invoke {@link #toString()}.
     */
    class OutputCapture {

        private CaptureOutputStream captureOut;

        private CaptureOutputStream captureErr;

        private ByteArrayOutputStream copy;

        private OutputAssertion assertSupplier;

        void captureOutput() {
            this.copy = new ByteArrayOutputStream();
            this.captureOut = new CaptureOutputStream(System.out, this.copy);
            this.captureErr = new CaptureOutputStream(System.err, this.copy);
            System.setOut(new PrintStream(this.captureOut));
            System.setErr(new PrintStream(this.captureErr));
        }

        void releaseOutput() {
            System.setOut(this.captureOut.getOriginal());
            System.setErr(this.captureErr.getOriginal());
            this.copy = null;
        }

        private void flush() {
            try {
                this.captureOut.flush();
                this.captureErr.flush();
            } catch (IOException ex) {
                // ignore
            }
        }

        /**
         * Verify that the captured output is matched by the supplied {@code OutputAssertion}.
         *
         * <p>Verification is performed after the test method has executed.
         */
        public void expect(OutputAssertion assertSupplier) {
            this.assertSupplier = assertSupplier;
        }

        /**
         * Return all captured output to {@code System.out} and {@code System.err}
         * as a single string.
         */
        @Override
        public String toString() {
            flush();
            return this.copy.toString();
        }

        private static class CaptureOutputStream extends OutputStream {

            private final PrintStream original;

            private final OutputStream copy;

            CaptureOutputStream(PrintStream original, OutputStream copy) {
                this.original = original;
                this.copy = copy;
            }

            PrintStream getOriginal() {
                return this.original;
            }

            @Override
            public void write(int b) throws IOException {
                this.copy.write(b);
                this.original.write(b);
                this.original.flush();
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                this.copy.write(b, off, len);
                this.original.write(b, off, len);
            }

            @Override
            public void flush() throws IOException {
                this.copy.flush();
                this.original.flush();
            }
        }
    }

    @FunctionalInterface
    interface OutputAssertion {
        void apply(String str);
    }
}
