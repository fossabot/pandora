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

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A Appender for logback based test, would be easily appended to test class, and get the log content.
 *
 * @author せいうはん
 * @version 1.0.0, 2019-07-16 21:00
 * @since 1.0.0, 2019-07-16 21:00
 */
public class LogbackListAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private final List<ILoggingEvent> logs = Lists.newArrayList();

    public LogbackListAppender() {
        start();
    }

    public static LogbackListAppender create(Class<?> loggerClass) {
        LogbackListAppender appender = new LogbackListAppender();
        appender.addToLogger(loggerClass);
        return appender;
    }

    public static LogbackListAppender create(String loggerName) {
        LogbackListAppender appender = new LogbackListAppender();
        appender.addToLogger(loggerName);
        return appender;
    }

    @Override
    protected void append(ILoggingEvent e) {
        logs.add(e);
    }

    /**
     * 返回之前append的第一个log.
     */
    public ILoggingEvent getFirstLog() {
        if (logs.isEmpty()) {
            return null;
        }
        return logs.get(0);
    }

    /**
     * 返回之前append的第一个log的内容.
     */
    public String getFirstMessage() {
        if (logs.isEmpty()) {
            return null;
        }
        return getFirstLog().getFormattedMessage();
    }

    /**
     * 返回之前append的最后一个log.
     */
    public ILoggingEvent getLastLog() {
        if (logs.isEmpty()) {
            return null;
        }
        return Iterables.getLast(logs);
    }

    /**
     * 返回之前append的最后一个log的内容.
     */
    public String getLastMessage() {
        if (logs.isEmpty()) {
            return null;
        }
        return getLastLog().getFormattedMessage();
    }

    /**
     * 返回之前append的所有log.
     */
    public List<ILoggingEvent> getAllLogs() {
        return logs;
    }

    /**
     * 返回Log的数量。
     */
    public int getLogsCount() {
        return logs.size();
    }

    /**
     * 判断是否有log.
     */
    public boolean isEmpty() {
        return logs.isEmpty();
    }

    /**
     * 清除之前append的所有log.
     */
    public void clearLogs() {
        logs.clear();
    }

    /**
     * 将此appender添加到logger中.
     */
    public void addToLogger(String loggerName) {
        Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
        logger.addAppender(this);
    }

    /**
     * 将此appender添加到logger中.
     */
    public void addToLogger(Class<?> loggerClass) {
        Logger logger = (Logger) LoggerFactory.getLogger(loggerClass);
        logger.addAppender(this);
    }

    /**
     * 将此appender添加到root logger中.
     */
    public void addToRootLogger() {
        Logger logger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        logger.addAppender(this);
    }

    /**
     * 将此appender从logger中移除.
     */
    public void removeFromLogger(String loggerName) {
        Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
        logger.detachAppender(this);
    }

    /**
     * 将此appender从logger中移除.
     */
    public void removeFromLogger(Class<?> loggerClass) {
        Logger logger = (Logger) LoggerFactory.getLogger(loggerClass);
        logger.detachAppender(this);
    }

    /**
     * 将此appender从root logger中移除.
     */
    public void removeFromRootLogger() {
        Logger logger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(this);
    }
}
