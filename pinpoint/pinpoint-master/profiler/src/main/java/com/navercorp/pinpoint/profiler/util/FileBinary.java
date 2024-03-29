/*
 * Copyright 2016 NAVER Corp.
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
 *
 */

package com.navercorp.pinpoint.profiler.util;

import com.navercorp.pinpoint.common.util.Assert;

/**
 * @author Woonduk Kang(emeroad)
 */
public class FileBinary {
    private final String className;

    private final byte[] fileBinary;

    FileBinary(String fileName, byte[] fileBinary) {
        this.className = Assert.requireNonNull(fileName, "fileName");
        this.fileBinary = Assert.requireNonNull(fileBinary, "fileBinary");
    }

    public String getFileName() {
        return className;
    }

    public byte[] getFileBinary() {
        return fileBinary;
    }

    @Override
    public String toString() {
        return "FileBinary{" +
                "className='" + className + '\'' +
                ", fileBinarySize=" + fileBinary.length +
                '}';
    }
}
