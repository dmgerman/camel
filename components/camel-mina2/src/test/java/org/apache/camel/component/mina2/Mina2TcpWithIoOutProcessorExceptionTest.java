begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina2
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * To unit test CAMEL-364.  */
end_comment

begin_class
DECL|class|Mina2TcpWithIoOutProcessorExceptionTest
specifier|public
class|class
name|Mina2TcpWithIoOutProcessorExceptionTest
extends|extends
name|BaseMina2Test
block|{
annotation|@
name|Test
DECL|method|testExceptionThrownInProcessor ()
specifier|public
name|void
name|testExceptionThrownInProcessor
parameter_list|()
block|{
name|String
name|body
init|=
literal|"Hello World"
decl_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"mina2:tcp://localhost:%1$s?textline=true&sync=true"
argument_list|,
name|getPort
argument_list|()
argument_list|)
argument_list|,
name|body
argument_list|)
decl_stmt|;
comment|// The exception should be passed to the client
name|assertNotNull
argument_list|(
literal|"the result should not be null"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"result is IllegalArgumentException"
argument_list|,
name|result
argument_list|,
literal|"java.lang.IllegalArgumentException: Forced exception"
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
comment|// use no delay for fast unit testing
name|errorHandler
argument_list|(
name|defaultErrorHandler
argument_list|()
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"mina2:tcp://localhost:%1$s?textline=true&sync=true"
argument_list|,
name|getPort
argument_list|()
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|// simulate a problem processing the input to see if we can handle it properly
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Forced exception"
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

