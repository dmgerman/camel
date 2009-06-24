begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
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
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQConnectionFactory
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
name|Body
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
name|Message
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
name|Processor
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
name|component
operator|.
name|jms
operator|.
name|JmsComponent
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|model
operator|.
name|config
operator|.
name|BatchResequencerConfig
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|jndi
operator|.
name|JndiContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_class
DECL|class|JmsResequencerTest
specifier|public
class|class
name|JmsResequencerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|JmsResequencerTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|b1
specifier|private
name|ReusableBean
name|b1
init|=
operator|new
name|ReusableBean
argument_list|(
literal|"myBean1"
argument_list|)
decl_stmt|;
DECL|field|b2
specifier|private
name|ReusableBean
name|b2
init|=
operator|new
name|ReusableBean
argument_list|(
literal|"myBean2"
argument_list|)
decl_stmt|;
DECL|field|b3
specifier|private
name|ReusableBean
name|b3
init|=
operator|new
name|ReusableBean
argument_list|(
literal|"myBean3"
argument_list|)
decl_stmt|;
DECL|field|resultEndpoint
specifier|private
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
DECL|method|sendBodyAndHeader (String endpointUri, final Object body, final String headerName, final Object headerValue)
specifier|public
name|void
name|sendBodyAndHeader
parameter_list|(
name|String
name|endpointUri
parameter_list|,
specifier|final
name|Object
name|body
parameter_list|,
specifier|final
name|String
name|headerName
parameter_list|,
specifier|final
name|Object
name|headerValue
parameter_list|)
block|{
name|template
operator|.
name|send
argument_list|(
name|endpointUri
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|headerName
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
comment|//in.setHeader("testCase", getName());
name|in
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|BEAN_METHOD_NAME
argument_list|,
literal|"execute"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendMessagesInWrongOrderButReceiveThemInCorrectOrder ()
specifier|public
name|void
name|testSendMessagesInWrongOrderButReceiveThemInCorrectOrder
parameter_list|()
throws|throws
name|Exception
block|{
name|sendAndVerifyMessages
argument_list|(
literal|"activemq:queue:batch"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendMessageToStream ()
specifier|public
name|void
name|testSendMessageToStream
parameter_list|()
throws|throws
name|Exception
block|{
name|sendAndVerifyMessages
argument_list|(
literal|"activemq:queue:stream"
argument_list|)
expr_stmt|;
block|}
DECL|method|sendAndVerifyMessages (String endpointUri)
specifier|private
name|void
name|sendAndVerifyMessages
parameter_list|(
name|String
name|endpointUri
parameter_list|)
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"msg1"
argument_list|,
literal|"msg2"
argument_list|,
literal|"msg3"
argument_list|,
literal|"msg4"
argument_list|,
literal|"msg5"
argument_list|,
literal|"msg6"
argument_list|)
expr_stmt|;
name|sendBodyAndHeader
argument_list|(
name|endpointUri
argument_list|,
literal|"msg4"
argument_list|,
literal|"seqnum"
argument_list|,
literal|4L
argument_list|)
expr_stmt|;
name|sendBodyAndHeader
argument_list|(
name|endpointUri
argument_list|,
literal|"msg1"
argument_list|,
literal|"seqnum"
argument_list|,
literal|1L
argument_list|)
expr_stmt|;
name|sendBodyAndHeader
argument_list|(
name|endpointUri
argument_list|,
literal|"msg3"
argument_list|,
literal|"seqnum"
argument_list|,
literal|3L
argument_list|)
expr_stmt|;
name|sendBodyAndHeader
argument_list|(
name|endpointUri
argument_list|,
literal|"msg2"
argument_list|,
literal|"seqnum"
argument_list|,
literal|2L
argument_list|)
expr_stmt|;
name|sendBodyAndHeader
argument_list|(
name|endpointUri
argument_list|,
literal|"msg6"
argument_list|,
literal|"seqnum"
argument_list|,
literal|6L
argument_list|)
expr_stmt|;
name|sendBodyAndHeader
argument_list|(
name|endpointUri
argument_list|,
literal|"msg5"
argument_list|,
literal|"seqnum"
argument_list|,
literal|5L
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|resultEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|list
control|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Received: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|Object
name|lookedUpBean
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
literal|"myBean1"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"Lookup of 'myBean' should return same object!"
argument_list|,
name|b1
argument_list|,
name|lookedUpBean
argument_list|)
expr_stmt|;
name|lookedUpBean
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
literal|"myBean2"
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Lookup of 'myBean' should return same object!"
argument_list|,
name|b2
argument_list|,
name|lookedUpBean
argument_list|)
expr_stmt|;
name|lookedUpBean
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
literal|"myBean3"
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Lookup of 'myBean' should return same object!"
argument_list|,
name|b3
argument_list|,
name|lookedUpBean
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
block|{
name|from
argument_list|(
literal|"activemq:queue:batch"
argument_list|)
operator|.
name|to
argument_list|(
name|callExecuteOnBean
argument_list|(
literal|"myBean1"
argument_list|)
argument_list|)
operator|.
name|resequencer
argument_list|(
name|header
argument_list|(
literal|"seqnum"
argument_list|)
argument_list|)
operator|.
name|batch
argument_list|(
operator|new
name|BatchResequencerConfig
argument_list|(
literal|100
argument_list|,
literal|2000L
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|callExecuteOnBean
argument_list|(
literal|"myBean2"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:queue:stop"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:stream"
argument_list|)
operator|.
name|to
argument_list|(
name|callExecuteOnBean
argument_list|(
literal|"myBean1"
argument_list|)
argument_list|)
operator|.
name|resequencer
argument_list|(
name|header
argument_list|(
literal|"seqnum"
argument_list|)
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|to
argument_list|(
name|callExecuteOnBean
argument_list|(
literal|"myBean2"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:queue:stop"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:stop"
argument_list|)
operator|.
name|to
argument_list|(
name|callExecuteOnBean
argument_list|(
literal|"myBean3"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|callExecuteOnBean (String beanName)
specifier|private
specifier|static
name|String
name|callExecuteOnBean
parameter_list|(
name|String
name|beanName
parameter_list|)
block|{
return|return
literal|"bean:"
operator|+
name|beanName
operator|+
literal|"?method=execute"
return|;
block|}
DECL|class|ReusableBean
specifier|public
class|class
name|ReusableBean
block|{
DECL|field|body
specifier|public
name|String
name|body
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|method|ReusableBean (String name)
specifier|public
name|ReusableBean
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"MyBean:"
operator|+
name|name
return|;
block|}
DECL|method|read (@ody String body)
specifier|public
name|void
name|read
parameter_list|(
annotation|@
name|Body
name|String
name|body
parameter_list|)
block|{
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
name|name
operator|+
literal|" read() method on "
operator|+
name|this
operator|+
literal|" with body: "
operator|+
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
name|name
operator|+
literal|" started"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
name|name
operator|+
literal|" finished"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiContext
name|answer
init|=
operator|new
name|JndiContext
argument_list|()
decl_stmt|;
name|ConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://localhost?broker.persistent=true"
argument_list|)
decl_stmt|;
name|JmsComponent
name|component
init|=
name|JmsComponent
operator|.
name|jmsComponent
argument_list|(
name|connectionFactory
argument_list|)
decl_stmt|;
name|component
operator|.
name|setConcurrentConsumers
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"activemq"
argument_list|,
name|JmsComponent
operator|.
name|jmsComponentAutoAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"myBean1"
argument_list|,
name|b1
argument_list|)
expr_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"myBean2"
argument_list|,
name|b2
argument_list|)
expr_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"myBean3"
argument_list|,
name|b3
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

