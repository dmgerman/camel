begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jbi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jbi
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
name|servicemix
operator|.
name|jbi
operator|.
name|container
operator|.
name|ActivationSpec
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|tck
operator|.
name|MessageList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|tck
operator|.
name|ReceiverComponent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jbi
operator|.
name|messaging
operator|.
name|NormalizedMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|SendFromCamelToJbiTest
specifier|public
class|class
name|SendFromCamelToJbiTest
extends|extends
name|JbiTestSupport
block|{
DECL|field|receiverComponent
specifier|private
name|ReceiverComponent
name|receiverComponent
init|=
operator|new
name|ReceiverComponent
argument_list|()
decl_stmt|;
DECL|method|testCamelInvokingJbi ()
specifier|public
name|void
name|testCamelInvokingJbi
parameter_list|()
throws|throws
name|Exception
block|{
name|sendExchange
argument_list|(
literal|"<foo bar='123'/>"
argument_list|)
expr_stmt|;
name|MessageList
name|list
init|=
name|receiverComponent
operator|.
name|getMessageList
argument_list|()
decl_stmt|;
name|list
operator|.
name|assertMessagesReceived
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|List
name|messages
init|=
name|list
operator|.
name|getMessages
argument_list|()
decl_stmt|;
name|NormalizedMessage
name|message
init|=
operator|(
name|NormalizedMessage
operator|)
name|messages
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"null message!"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Received: "
operator|+
name|message
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese header"
argument_list|,
literal|123
argument_list|,
name|message
operator|.
name|getProperty
argument_list|(
literal|"cheese"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createRoutes ()
specifier|protected
name|RouteBuilder
name|createRoutes
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
comment|// no routes required
block|}
block|}
return|;
block|}
DECL|method|appendJbiActivationSpecs (List<ActivationSpec> activationSpecList)
specifier|protected
name|void
name|appendJbiActivationSpecs
parameter_list|(
name|List
argument_list|<
name|ActivationSpec
argument_list|>
name|activationSpecList
parameter_list|)
block|{
name|ActivationSpec
name|activationSpec
init|=
operator|new
name|ActivationSpec
argument_list|()
decl_stmt|;
name|activationSpec
operator|.
name|setId
argument_list|(
literal|"jbiReceiver"
argument_list|)
expr_stmt|;
name|activationSpec
operator|.
name|setService
argument_list|(
operator|new
name|QName
argument_list|(
literal|"serviceNamespace"
argument_list|,
literal|"serviceA"
argument_list|)
argument_list|)
expr_stmt|;
name|activationSpec
operator|.
name|setEndpoint
argument_list|(
literal|"endpointA"
argument_list|)
expr_stmt|;
name|activationSpec
operator|.
name|setComponent
argument_list|(
name|receiverComponent
argument_list|)
expr_stmt|;
name|activationSpecList
operator|.
name|add
argument_list|(
name|activationSpec
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

