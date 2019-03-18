begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.activemq.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|activemq
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TextMessage
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

begin_comment
comment|/**  *   */
end_comment

begin_class
DECL|class|InvokeJmsMessageListenerTest
specifier|public
class|class
name|InvokeJmsMessageListenerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|messageListener
specifier|protected
name|MyMessageListener
name|messageListener
init|=
operator|new
name|MyMessageListener
argument_list|()
decl_stmt|;
DECL|field|expectedBody
specifier|private
name|String
name|expectedBody
init|=
literal|"<hello>world!</hello>"
decl_stmt|;
annotation|@
name|Test
DECL|method|testCamelInvokesMessageListener ()
specifier|public
name|void
name|testCamelInvokesMessageListener
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|expectedBody
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|messageListener
operator|.
name|message
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have invoked the message listener!"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|TextMessage
name|textMessage
init|=
name|assertIsInstanceOf
argument_list|(
name|TextMessage
operator|.
name|class
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"body"
argument_list|,
name|expectedBody
argument_list|,
name|textMessage
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
literal|"direct:start"
argument_list|)
operator|.
name|bean
argument_list|(
name|messageListener
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyMessageListener
specifier|protected
specifier|static
class|class
name|MyMessageListener
implements|implements
name|MessageListener
block|{
DECL|field|message
specifier|public
name|Message
name|message
decl_stmt|;
DECL|method|onMessage (Message message)
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

