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
comment|/**  * @version $Revision: 817619 $  */
end_comment

begin_class
DECL|class|XmppMultiUserChatTest
specifier|public
class|class
name|XmppMultiUserChatTest
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
name|XmppRouteChatTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|consumerEndpoint
specifier|protected
name|MockEndpoint
name|consumerEndpoint
decl_stmt|;
DECL|field|producerEndpoint
specifier|protected
name|MockEndpoint
name|producerEndpoint
decl_stmt|;
DECL|field|body1
specifier|protected
name|String
name|body1
init|=
literal|"the first message"
decl_stmt|;
DECL|field|body2
specifier|protected
name|String
name|body2
init|=
literal|"the second message"
decl_stmt|;
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testXmppChat ()
specifier|public
name|void
name|testXmppChat
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO: requires online against jabber. Test this manually
name|consumerEndpoint
operator|=
operator|(
name|MockEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:out"
argument_list|)
expr_stmt|;
name|consumerEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body1
argument_list|,
name|body2
argument_list|)
expr_stmt|;
comment|//will send chat messages to the room
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:toProducer"
argument_list|,
name|body1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:toProducer"
argument_list|,
name|body2
argument_list|)
expr_stmt|;
name|consumerEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
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
literal|"direct:toProducer"
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
literal|"xmpp://jabber.org:5222?room=camel-test&user=camel_producer&password=secret&serviceName=jabber.org"
return|;
block|}
DECL|method|getConsumerUri ()
specifier|protected
name|String
name|getConsumerUri
parameter_list|()
block|{
return|return
literal|"xmpp://jabber.org:5222?room=camel-test&user=camel_consumer&password=secret&serviceName=jabber.org"
return|;
block|}
block|}
end_class

end_unit

