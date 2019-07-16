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
package moe.yufan.pandora.test.extensions.internal;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

/**
 * A {@code NoExitSecurityManager} throws a {@link CheckExitCalled} exception
 * whenever {@link #checkExit(int)} is called. All other method calls are
 * delegated to the original security manager.
 *
 * @author せいうはん
 * @version 1.0.0, 2019-07-16 21:00
 * @since 1.0.0, 2019-07-16 21:00
 */
public class NoExitSecurityManager extends SecurityManager {

    private final SecurityManager originalSecurityManager;
    private Integer statusOfFirstExitCall = null;

    public NoExitSecurityManager(SecurityManager originalSecurityManager) {
        this.originalSecurityManager = originalSecurityManager;
    }

    @Override
    public void checkExit(int status) {
        if (statusOfFirstExitCall == null) {
            statusOfFirstExitCall = status;
        }
        throw new CheckExitCalled(status);
    }

    public boolean isCheckExitCalled() {
        return statusOfFirstExitCall != null;
    }

    public int getStatusOfFirstCheckExitCall() {
        if (isCheckExitCalled()) {
            return statusOfFirstExitCall;
        } else {
            throw new IllegalStateException("checkExit(int) has not been called.");
        }
    }

    @Override
    public Object getSecurityContext() {
        return (originalSecurityManager == null) ? super.getSecurityContext()
            : originalSecurityManager.getSecurityContext();
    }

    @Override
    public void checkPermission(Permission perm) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkPermission(perm);
        }
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkPermission(perm, context);
        }
    }

    @Override
    public void checkCreateClassLoader() {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkCreateClassLoader();
        }
    }

    @Override
    public void checkAccess(Thread t) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkAccess(t);
        }
    }

    @Override
    public void checkAccess(ThreadGroup g) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkAccess(g);
        }
    }

    @Override
    public void checkExec(String cmd) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkExec(cmd);
        }
    }

    @Override
    public void checkLink(String lib) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkLink(lib);
        }
    }

    @Override
    public void checkRead(FileDescriptor fd) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkRead(fd);
        }
    }

    @Override
    public void checkRead(String file) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkRead(file);
        }
    }

    @Override
    public void checkRead(String file, Object context) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkRead(file, context);
        }
    }

    @Override
    public void checkWrite(FileDescriptor fd) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkWrite(fd);
        }
    }

    @Override
    public void checkWrite(String file) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkWrite(file);
        }
    }

    @Override
    public void checkDelete(String file) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkDelete(file);
        }
    }

    @Override
    public void checkConnect(String host, int port) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkConnect(host, port);
        }
    }

    @Override
    public void checkConnect(String host, int port, Object context) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkConnect(host, port, context);
        }
    }

    @Override
    public void checkListen(int port) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkListen(port);
        }
    }

    @Override
    public void checkAccept(String host, int port) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkAccept(host, port);
        }
    }

    @Override
    public void checkMulticast(InetAddress maddr) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkMulticast(maddr);
        }
    }

    @Override
    public void checkMulticast(InetAddress maddr, byte ttl) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkMulticast(maddr, ttl);
        }
    }

    @Override
    public void checkPropertiesAccess() {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkPropertiesAccess();
        }
    }

    @Override
    public void checkPropertyAccess(String key) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkPropertyAccess(key);
        }
    }

    @Override
    public boolean checkTopLevelWindow(Object window) {
        if (originalSecurityManager == null) {
            return super.checkTopLevelWindow(window);
        } else {
            return originalSecurityManager.checkTopLevelWindow(window);
        }
    }

    @Override
    public void checkPrintJobAccess() {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkPrintJobAccess();
        }
    }

    @Override
    public void checkSystemClipboardAccess() {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkSystemClipboardAccess();
        }
    }

    @Override
    public void checkAwtEventQueueAccess() {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkAwtEventQueueAccess();
        }
    }

    @Override
    public void checkPackageAccess(String pkg) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkPackageAccess(pkg);
        }
    }

    @Override
    public void checkPackageDefinition(String pkg) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkPackageDefinition(pkg);
        }
    }

    @Override
    public void checkSetFactory() {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkSetFactory();
        }
    }

    @Override
    public void checkMemberAccess(Class<?> clazz, int which) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkMemberAccess(clazz, which);
        }
    }

    @Override
    public void checkSecurityAccess(String target) {
        if (originalSecurityManager != null) {
            originalSecurityManager.checkSecurityAccess(target);
        }
    }

    @Override
    public ThreadGroup getThreadGroup() {
        return (originalSecurityManager == null) ? super.getThreadGroup() : originalSecurityManager.getThreadGroup();
    }
}
