begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|JmsComponent
operator|.
name|jmsComponentAutoAcknowledge
import|;
end_import

begin_class
DECL|class|BrowsableQueueTest
specifier|public
class|class
name|BrowsableQueueTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BrowsableQueueTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|componentName
specifier|protected
name|String
name|componentName
init|=
literal|"activemq"
decl_stmt|;
DECL|field|expectedBodies
specifier|protected
name|Object
index|[]
name|expectedBodies
init|=
block|{
literal|"body1"
block|,
literal|"body2"
block|,
literal|"body3"
block|,
literal|"body4"
block|,
literal|"body5"
block|,
literal|"body6"
block|,
literal|"body7"
block|,
literal|"body8"
block|}
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendMessagesThenBrowseQueue ()
specifier|public
name|void
name|testSendMessagesThenBrowseQueue
parameter_list|()
throws|throws
name|Exception
block|{
comment|// send some messages
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expectedBodies
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|expectedBody
init|=
name|expectedBodies
index|[
name|i
index|]
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:test.b"
argument_list|,
name|expectedBody
argument_list|,
literal|"counter"
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
comment|// now lets browse the queue
name|JmsQueueEndpoint
name|endpoint
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"activemq:test.b?maximumBrowseSize=6"
argument_list|,
name|JmsQueueEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|endpoint
operator|.
name|getMaximumBrowseSize
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|endpoint
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received: "
operator|+
name|list
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Size of list"
argument_list|,
literal|6
argument_list|,
name|endpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|index
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|list
control|)
block|{
name|String
name|actual
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received body: "
operator|+
name|actual
argument_list|)
expr_stmt|;
name|Object
name|expected
init|=
name|expectedBodies
index|[
operator|++
name|index
index|]
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Body: "
operator|+
name|index
argument_list|,
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testSendMessagesThenBrowseQueueLimitNotHit ()
specifier|public
name|void
name|testSendMessagesThenBrowseQueueLimitNotHit
parameter_list|()
throws|throws
name|Exception
block|{
comment|// send some messages
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expectedBodies
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|expectedBody
init|=
name|expectedBodies
index|[
name|i
index|]
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:test.b"
argument_list|,
name|expectedBody
argument_list|,
literal|"counter"
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
comment|// now lets browse the queue
name|JmsQueueEndpoint
name|endpoint
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"activemq:test.b?maximumBrowseSize=10"
argument_list|,
name|JmsQueueEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|endpoint
operator|.
name|getMaximumBrowseSize
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|endpoint
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received: "
operator|+
name|list
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Size of list"
argument_list|,
literal|8
argument_list|,
name|endpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|index
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|list
control|)
block|{
name|String
name|actual
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received body: "
operator|+
name|actual
argument_list|)
expr_stmt|;
name|Object
name|expected
init|=
name|expectedBodies
index|[
operator|++
name|index
index|]
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Body: "
operator|+
name|index
argument_list|,
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testSendMessagesThenBrowseQueueNoMax ()
specifier|public
name|void
name|testSendMessagesThenBrowseQueueNoMax
parameter_list|()
throws|throws
name|Exception
block|{
comment|// send some messages
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expectedBodies
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|expectedBody
init|=
name|expectedBodies
index|[
name|i
index|]
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:test.b"
argument_list|,
name|expectedBody
argument_list|,
literal|"counter"
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
comment|// now lets browse the queue
name|JmsQueueEndpoint
name|endpoint
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"activemq:test.b"
argument_list|,
name|JmsQueueEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|endpoint
operator|.
name|getMaximumBrowseSize
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|endpoint
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received: "
operator|+
name|list
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Size of list"
argument_list|,
literal|8
argument_list|,
name|endpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|index
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|list
control|)
block|{
name|String
name|actual
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received body: "
operator|+
name|actual
argument_list|)
expr_stmt|;
name|Object
name|expected
init|=
name|expectedBodies
index|[
operator|++
name|index
index|]
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Body: "
operator|+
name|index
argument_list|,
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|ConnectionFactory
name|connectionFactory
init|=
name|CamelJmsTestHelper
operator|.
name|createConnectionFactory
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
name|componentName
argument_list|,
name|jmsComponentAutoAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"activemq:test.a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:test.b"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

