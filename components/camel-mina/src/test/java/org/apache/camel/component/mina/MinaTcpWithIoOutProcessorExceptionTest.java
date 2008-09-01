begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
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
name|CamelContext
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
name|impl
operator|.
name|DefaultCamelContext
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
name|DefaultExchange
import|;
end_import

begin_comment
comment|/**  * To unit test CAMEL-364.  */
end_comment

begin_class
DECL|class|MinaTcpWithIoOutProcessorExceptionTest
specifier|public
class|class
name|MinaTcpWithIoOutProcessorExceptionTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|PORT
specifier|private
specifier|static
specifier|final
name|int
name|PORT
init|=
literal|6334
decl_stmt|;
DECL|field|container
specifier|protected
name|CamelContext
name|container
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
comment|// use parameter sync=true to force InOut pattern of the MinaExchange
DECL|field|uri
specifier|protected
name|String
name|uri
init|=
literal|"mina:tcp://localhost:"
operator|+
name|PORT
operator|+
literal|"?textline=true&sync=true"
decl_stmt|;
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
name|sendBody
argument_list|(
name|uri
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
name|from
argument_list|(
name|uri
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

