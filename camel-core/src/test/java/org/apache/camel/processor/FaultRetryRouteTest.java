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
name|apache
operator|.
name|camel
operator|.
name|CamelException
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
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_class
DECL|class|FaultRetryRouteTest
specifier|public
class|class
name|FaultRetryRouteTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|a
specifier|protected
name|MockEndpoint
name|a
decl_stmt|;
DECL|field|b
specifier|protected
name|MockEndpoint
name|b
decl_stmt|;
DECL|field|error
specifier|protected
name|MockEndpoint
name|error
decl_stmt|;
DECL|field|successOnRetryProcessor
specifier|protected
specifier|final
name|Processor
name|successOnRetryProcessor
init|=
operator|new
name|Processor
argument_list|()
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CamelException
block|{
if|if
condition|(
name|count
operator|++
operator|==
literal|0
condition|)
block|{
name|Message
name|message
init|=
name|exchange
operator|.
name|getFault
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
operator|new
name|CamelException
argument_list|(
literal|"Failed the first time"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
DECL|method|testSuccessfulRetry ()
specifier|public
name|void
name|testSuccessfulRetry
parameter_list|()
throws|throws
name|Exception
block|{
name|a
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"in"
argument_list|)
expr_stmt|;
name|b
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"in"
argument_list|)
expr_stmt|;
name|error
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"in"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|a
argument_list|,
name|b
argument_list|,
name|error
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
name|a
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:a"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|b
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:b"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|error
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:error"
argument_list|,
name|MockEndpoint
operator|.
name|class
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|4
argument_list|)
operator|.
name|loggingLevel
argument_list|(
name|LoggingLevel
operator|.
name|DEBUG
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|handleFault
argument_list|()
operator|.
name|process
argument_list|(
name|successOnRetryProcessor
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

