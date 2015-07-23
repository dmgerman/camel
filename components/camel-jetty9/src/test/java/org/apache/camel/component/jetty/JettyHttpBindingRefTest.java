begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|http
operator|.
name|common
operator|.
name|HttpOperationFailedException
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
name|JndiRegistry
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
name|spi
operator|.
name|HeaderFilterStrategy
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
comment|/**  * Unit test for jetty http binding ref option.  */
end_comment

begin_class
DECL|class|JettyHttpBindingRefTest
specifier|public
class|class
name|JettyHttpBindingRefTest
extends|extends
name|BaseJettyTest
block|{
annotation|@
name|Test
DECL|method|testDefaultJettyHttpBinding ()
specifier|public
name|void
name|testDefaultJettyHttpBinding
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"jetty:http://localhost:{{port}}/myapp/myservice?jettyHttpBindingRef=default"
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|out
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"jetty:http://localhost:{{port}}/myapp/myotherservice"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|HttpOperationFailedException
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Not exactly the message the server returned."
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|HttpOperationFailedException
operator|)
name|e
operator|.
name|getCause
argument_list|()
operator|)
operator|.
name|getResponseBody
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testCustomJettyHttpBinding ()
specifier|public
name|void
name|testCustomJettyHttpBinding
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"jetty:http://localhost:{{port}}/myapp/myotherservice?jettyHttpBindingRef=myownbinder"
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Not exactly the message the server returned."
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"default"
argument_list|,
operator|new
name|DefaultJettyHttpBinding
argument_list|()
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myownbinder"
argument_list|,
operator|new
name|MyJettyHttpBinding
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
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
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:{{port}}/myapp/myservice"
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
name|from
argument_list|(
literal|"jetty:http://localhost:{{port}}/myapp/myotherservice"
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
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not implemented"
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
comment|// START SNIPPET: e1
DECL|class|MyJettyHttpBinding
specifier|public
class|class
name|MyJettyHttpBinding
extends|extends
name|DefaultJettyHttpBinding
block|{
annotation|@
name|Override
DECL|method|populateResponse (Exchange exchange, JettyContentExchange httpExchange, Message in, HeaderFilterStrategy strategy, int responseCode)
specifier|protected
name|void
name|populateResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|JettyContentExchange
name|httpExchange
parameter_list|,
name|Message
name|in
parameter_list|,
name|HeaderFilterStrategy
name|strategy
parameter_list|,
name|int
name|responseCode
parameter_list|)
throws|throws
name|IOException
block|{
name|Message
name|answer
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setHeaders
argument_list|(
name|in
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|responseCode
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setBody
argument_list|(
literal|"Not exactly the message the server returned."
argument_list|)
expr_stmt|;
block|}
block|}
comment|// END SNIPPET: e1
block|}
end_class

end_unit

