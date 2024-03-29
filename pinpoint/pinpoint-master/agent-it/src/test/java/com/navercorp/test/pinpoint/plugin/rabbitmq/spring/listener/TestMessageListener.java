/*
 * Copyright 2018 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.test.pinpoint.plugin.rabbitmq.spring.listener;

import com.navercorp.pinpoint.plugin.jdk7.rabbitmq.util.RabbitMQTestConstants;
import com.navercorp.test.pinpoint.plugin.rabbitmq.PropagationMarker;
import com.navercorp.test.pinpoint.plugin.rabbitmq.spring.TestMessageHolder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author HyunGil Jeong
 */
@Component
public class TestMessageListener {

    private final PropagationMarker marker = new PropagationMarker();
    private final TestMessageHolder testMessageHolder;

    @Autowired
    public TestMessageListener(TestMessageHolder testMessageHolder) {
        if (testMessageHolder == null) {
            throw new NullPointerException("testMessageHolder");
        }
        this.testMessageHolder = testMessageHolder;
    }

    @RabbitListener(queues = RabbitMQTestConstants.QUEUE_PUSH)
    public void receiveMessage(String message) {
        marker.mark();
        testMessageHolder.addMessage(message);
    }
}
