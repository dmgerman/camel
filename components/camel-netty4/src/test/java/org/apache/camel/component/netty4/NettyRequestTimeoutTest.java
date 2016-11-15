begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
package|;
end_package

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|timeout
operator|.
name|ReadTimeoutException
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
name|CamelExecutionException
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|NettyRequestTimeoutTest
specifier|public
class|class
name|NettyRequestTimeoutTest
extends|extends
name|BaseNettyTest
block|{
annotation|@
name|Test
DECL|method|testRequestTimeoutOK ()
specifier|public
name|void
name|testRequestTimeoutOK
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty4:tcp://localhost:{{port}}?textline=true&sync=true&requestTimeout=500"
argument_list|,
literal|"Hello Camel"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRequestTimeout ()
specifier|public
name|void
name|testRequestTimeout
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty4:tcp://localhost:{{port}}?textline=true&sync=true&requestTimeout=100"
argument_list|,
literal|"Hello Camel"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|ReadTimeoutException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|ReadTimeoutException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testRequestTimeoutViaHeader ()
specifier|public
name|void
name|testRequestTimeoutViaHeader
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"netty4:tcp://localhost:{{port}}?textline=true&sync=true"
argument_list|,
literal|"Hello Camel"
argument_list|,
name|NettyConstants
operator|.
name|NETTY_REQUEST_TIMEOUT
argument_list|,
literal|100
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|ReadTimeoutException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|ReadTimeoutException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testRequestTimeoutAndOk ()
specifier|public
name|void
name|testRequestTimeoutAndOk
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty4:tcp://localhost:{{port}}?textline=true&sync=true&requestTimeout=100"
argument_list|,
literal|"Hello Camel"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|ReadTimeoutException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|ReadTimeoutException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
comment|// now we try again but this time the is no delay on server and thus faster
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty4:tcp://localhost:{{port}}?textline=true&sync=true&requestTimeout=100"
argument_list|,
literal|"Hello World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"netty4:tcp://localhost:{{port}}?textline=true&sync=true"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|String
name|body
init|=
name|exchange
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
decl_stmt|;
if|if
condition|(
name|body
operator|.
name|contains
argument_list|(
literal|"Camel"
argument_list|)
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

