/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.scala
package dsl

import builder.RouteBuilder
import org.apache.camel.scala.test.Cat
import org.junit.Test
import org.junit.Assert.assertNull

/**
 * Test for setting the message header from the Scala DSL
 */
class RemoveHeaderTest extends ScalaTestSupport {

  @Test
  def testSetHeaderThenRemove() {
    test {
      "direct:start" ! ("ping")
    }

    assertMockEndpointsSatisfied()

    "mock:mid" expect { _.headerReceived("response", "pong?") }
    "mock:end" expect { endpoint =>
      val exchange = endpoint.getExchanges.get(0)

      assertNull(exchange.getIn.getHeader("response"))
    }
  }

  val builder = new RouteBuilder {
    "direct:start" ==> {
      setHeader("response", "pong?")
      to ("mock:mid")
      removeHeader("response")
      to ("mock:end")
    }
  }
}
