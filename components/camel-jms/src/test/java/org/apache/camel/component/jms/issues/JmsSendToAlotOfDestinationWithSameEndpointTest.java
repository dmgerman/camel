begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.issues
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
operator|.
name|issues
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ExchangePattern
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
name|JmsConstants
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
name|spring
operator|.
name|CamelSpringTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xbean
operator|.
name|spring
operator|.
name|context
operator|.
name|ClassPathXmlApplicationContext
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
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_class
DECL|class|JmsSendToAlotOfDestinationWithSameEndpointTest
specifier|public
class|class
name|JmsSendToAlotOfDestinationWithSameEndpointTest
extends|extends
name|CamelSpringTestSupport
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
name|JmsSendToAlotOfDestinationWithSameEndpointTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|URI
specifier|private
specifier|static
specifier|final
name|String
name|URI
init|=
literal|"activemq:queue:foo?autoStartup=false"
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendToAlotOfMessageToQueues ()
specifier|public
name|void
name|testSendToAlotOfMessageToQueues
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|size
init|=
literal|100
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"About to send "
operator|+
name|size
operator|+
literal|" messages"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
comment|// use the same endpoint but provide a header with the dynamic queue we send to
comment|// this allows us to reuse endpoints and not create a new endpoint for each and every jms queue
comment|// we send to
if|if
condition|(
name|i
operator|>
literal|0
operator|&&
name|i
operator|%
literal|50
operator|==
literal|0
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Send "
operator|+
name|i
operator|+
literal|" messages so far"
argument_list|)
expr_stmt|;
block|}
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|URI
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
literal|"Hello "
operator|+
name|i
argument_list|,
name|JmsConstants
operator|.
name|JMS_DESTINATION_NAME
argument_list|,
literal|"foo"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Send complete use jconsole to view"
argument_list|)
expr_stmt|;
comment|// now we should be able to poll a message from each queue
comment|// Thread.sleep(99999999);
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"classpath:org/apache/camel/component/jms/issues/broker.xml"
block|,
literal|"classpath:org/apache/camel/component/jms/issues/camelBrokerClient.xml"
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

