/*
 * Copyright 2017 NAVER Corp.
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

package com.navercorp.pinpoint.bootstrap.instrument.matcher.operand;

import com.navercorp.pinpoint.common.annotations.InterfaceStability;
import com.navercorp.pinpoint.common.util.Assert;
import com.navercorp.pinpoint.common.util.ClassUtils;

/**
 * @author jaehong.kim
 */
@InterfaceStability.Unstable
public class PackageInternalNameMatcherOperand extends AbstractMatcherOperand {
    private final String packageInternalName;

    public PackageInternalNameMatcherOperand(final String packageName) {
        Assert.requireNonNull(packageName, "packageName");
        this.packageInternalName = ClassUtils.toInternalName(packageName);
    }

    public String getPackageInternalName() {
        return packageInternalName;
    }

    public boolean match(final String packageInternalName) {
        if (packageInternalName == null) {
            return false;
        }
        return packageInternalName.startsWith(this.packageInternalName);
    }

    @Override
    public int getExecutionCost() {
        return 3;
    }

    @Override
    public boolean isIndex() {
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("packageInternalName=").append(packageInternalName);
        sb.append('}');
        return sb.toString();
    }
}