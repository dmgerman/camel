begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

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
name|camel
operator|.
name|ContextTestSupport
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
name|InvalidPayloadException
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
name|util
operator|.
name|jndi
operator|.
name|JndiContext
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|SimulatorTest
specifier|public
class|class
name|SimulatorTest
extends|extends
name|ContextTestSupport
block|{
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
name|answer
operator|.
name|bind
argument_list|(
literal|"foo"
argument_list|,
operator|new
name|MyBean
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"bar"
argument_list|,
operator|new
name|MyBean
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Test
DECL|method|testReceivesFooResponse ()
specifier|public
name|void
name|testReceivesFooResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|assertRespondsWith
argument_list|(
literal|"foo"
argument_list|,
literal|"Bye said foo"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReceivesBarResponse ()
specifier|public
name|void
name|testReceivesBarResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|assertRespondsWith
argument_list|(
literal|"bar"
argument_list|,
literal|"Bye said bar"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertRespondsWith (final String value, String containedText)
specifier|protected
name|void
name|assertRespondsWith
parameter_list|(
specifier|final
name|String
name|value
parameter_list|,
name|String
name|containedText
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
name|Exchange
name|response
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:a"
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
throws|throws
name|Exception
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
literal|"answer"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"cheese"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should receive a response!"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|response
operator|.
name|getOut
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertStringContains
argument_list|(
name|text
argument_list|,
name|containedText
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
comment|// START SNIPPET: example
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|recipientList
argument_list|(
name|simple
argument_list|(
literal|"bean:${in.header.cheese}"
argument_list|)
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
block|}
block|}
return|;
block|}
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|field|value
specifier|private
name|String
name|value
decl_stmt|;
DECL|method|MyBean (String value)
specifier|public
name|MyBean
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
DECL|method|doSomething (String in)
specifier|public
name|String
name|doSomething
parameter_list|(
name|String
name|in
parameter_list|)
block|{
return|return
literal|"Bye said "
operator|+
name|value
return|;
block|}
block|}
block|}
end_class

end_unit

