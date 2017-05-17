begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xmpp
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
name|impl
operator|.
name|JndiRegistry
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
name|jivesoftware
operator|.
name|smack
operator|.
name|ReconnectionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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

begin_comment
comment|/**  * Test to verify that the XMPP consumer will reconnect when the connection is lost.  * Also verifies that the XMPP producer will lazily re-establish a lost connection.  */
end_comment

begin_class
DECL|class|XmppRobustConnectionTest
specifier|public
class|class
name|XmppRobustConnectionTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|EmbeddedXmppTestServer
operator|.
name|instance
argument_list|()
operator|.
name|bindSSLContextTo
argument_list|(
name|registry
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Ignore
argument_list|(
literal|"Since upgrade to smack 4.2.0 the robust connection handling doesn't seem to work, as consumerEndpoint below receives only 5 payloads instead of the expected 9"
argument_list|)
annotation|@
name|Test
DECL|method|testXmppChatWithRobustConnection ()
specifier|public
name|void
name|testXmppChatWithRobustConnection
parameter_list|()
throws|throws
name|Exception
block|{
comment|// does not work well on aix or solaris
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
operator|||
name|isPlatform
argument_list|(
literal|"sunos"
argument_list|)
condition|)
block|{
return|return;
block|}
name|MockEndpoint
name|consumerEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:out"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|MockEndpoint
name|errorEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:error"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// the sleep may not be sufficient so assume around 9 or so messages
name|consumerEndpoint
operator|.
name|setMinimumExpectedMessageCount
argument_list|(
literal|9
argument_list|)
expr_stmt|;
name|errorEndpoint
operator|.
name|setExpectedMessageCount
argument_list|(
literal|5
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
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Test message [ "
operator|+
name|i
operator|+
literal|" ]"
argument_list|)
expr_stmt|;
block|}
name|consumerEndpoint
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
name|errorEndpoint
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
name|EmbeddedXmppTestServer
operator|.
name|instance
argument_list|()
operator|.
name|stopXmppEndpoint
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
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
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Test message [ "
operator|+
name|i
operator|+
literal|" ]"
argument_list|)
expr_stmt|;
block|}
name|errorEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|consumerEndpoint
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
name|EmbeddedXmppTestServer
operator|.
name|instance
argument_list|()
operator|.
name|startXmppEndpoint
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
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
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Test message [ "
operator|+
name|i
operator|+
literal|" ]"
argument_list|)
expr_stmt|;
block|}
name|consumerEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
name|onException
argument_list|(
name|RuntimeException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:error"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|id
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
name|getProducerUri
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|getConsumerUri
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|getProducerUri ()
specifier|protected
name|String
name|getProducerUri
parameter_list|()
block|{
return|return
literal|"xmpp://localhost:"
operator|+
name|EmbeddedXmppTestServer
operator|.
name|instance
argument_list|()
operator|.
name|getXmppPort
argument_list|()
operator|+
literal|"/camel_producer@apache.camel?connectionConfig=#customConnectionConfig&room=camel-test@conference.apache.camel&user=camel_producer&password=secret&serviceName=apache.camel"
return|;
block|}
DECL|method|getConsumerUri ()
specifier|protected
name|String
name|getConsumerUri
parameter_list|()
block|{
return|return
literal|"xmpp://localhost:"
operator|+
name|EmbeddedXmppTestServer
operator|.
name|instance
argument_list|()
operator|.
name|getXmppPort
argument_list|()
operator|+
literal|"/camel_consumer@apache.camel?connectionConfig=#customConnectionConfig&room=camel-test@conference.apache.camel&user=camel_consumer&password=secret&serviceName=apache.camel"
operator|+
literal|"&connectionPollDelay=1"
return|;
block|}
block|}
end_class

end_unit

