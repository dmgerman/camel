begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Endpoint
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

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|RecipientTest
specifier|public
class|class
name|RecipientTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|x
DECL|field|y
DECL|field|z
specifier|protected
name|MockEndpoint
name|x
decl_stmt|,
name|y
decl_stmt|,
name|z
decl_stmt|;
DECL|method|testSendingAMessageUsingMulticastReceivesItsOwnExchange ()
specifier|public
name|void
name|testSendingAMessageUsingMulticastReceivesItsOwnExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|x
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"answer"
argument_list|)
expr_stmt|;
name|y
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"answer"
argument_list|)
expr_stmt|;
name|z
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"answer"
argument_list|)
expr_stmt|;
name|client
operator|.
name|send
argument_list|(
literal|"direct:a"
argument_list|,
operator|new
name|Processor
argument_list|<
name|Exchange
argument_list|>
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
literal|"answer"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"recipientListHeader"
argument_list|,
literal|"mock:x,mock:y,mock:z"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|z
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
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
name|x
operator|=
operator|(
name|MockEndpoint
operator|)
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:x"
argument_list|)
expr_stmt|;
name|y
operator|=
operator|(
name|MockEndpoint
operator|)
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:y"
argument_list|)
expr_stmt|;
name|z
operator|=
operator|(
name|MockEndpoint
operator|)
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:z"
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
argument_list|<
name|Exchange
argument_list|>
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
name|header
argument_list|(
literal|"recipientListHeader"
argument_list|)
operator|.
name|tokenize
argument_list|(
literal|","
argument_list|)
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

