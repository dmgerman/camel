begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.integration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|integration
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
name|CamelSpringTestSupport
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|integration
operator|.
name|core
operator|.
name|MessageChannel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|integration
operator|.
name|message
operator|.
name|StringMessage
import|;
end_import

begin_class
DECL|class|SpringIntegrationOneWayConsumerTest
specifier|public
class|class
name|SpringIntegrationOneWayConsumerTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|MESSAGE_BODY
specifier|private
specifier|static
specifier|final
name|String
name|MESSAGE_BODY
init|=
literal|"hello world"
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendingOneWayMessage ()
specifier|public
name|void
name|testSendingOneWayMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|MESSAGE_BODY
argument_list|)
expr_stmt|;
name|MessageChannel
name|outputChannel
init|=
operator|(
name|MessageChannel
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"outputChannel"
argument_list|)
decl_stmt|;
name|outputChannel
operator|.
name|send
argument_list|(
operator|new
name|StringMessage
argument_list|(
name|MESSAGE_BODY
argument_list|)
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|createApplicationContext ()
specifier|public
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/spring/integration/oneWayConsumer.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

