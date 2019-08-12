begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|InputStream
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
name|component
operator|.
name|http
operator|.
name|HttpComponent
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|conn
operator|.
name|PoolingHttpClientConnectionManager
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

begin_class
DECL|class|MultiThreadedHttpGetTest
specifier|public
class|class
name|MultiThreadedHttpGetTest
extends|extends
name|BaseJettyTest
block|{
annotation|@
name|Test
DECL|method|testHttpGetWithConversion ()
specifier|public
name|void
name|testHttpGetWithConversion
parameter_list|()
throws|throws
name|Exception
block|{
comment|// In this scenario response stream is converted to String
comment|// so the stream has to be read to the end. When this happens
comment|// the associated connection is released automatically.
name|String
name|endpointName
init|=
literal|"seda:withConversion?concurrentConsumers=5"
decl_stmt|;
name|sendMessagesTo
argument_list|(
name|endpointName
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpGetWithoutConversion ()
specifier|public
name|void
name|testHttpGetWithoutConversion
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This is needed as by default there are 2 parallel
comment|// connections to some host and there is nothing that
comment|// closes the http connection here.
comment|// Need to set the httpConnectionManager
name|PoolingHttpClientConnectionManager
name|httpConnectionManager
init|=
operator|new
name|PoolingHttpClientConnectionManager
argument_list|()
decl_stmt|;
name|httpConnectionManager
operator|.
name|setDefaultMaxPerRoute
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"http"
argument_list|,
name|HttpComponent
operator|.
name|class
argument_list|)
operator|.
name|setClientConnectionManager
argument_list|(
name|httpConnectionManager
argument_list|)
expr_stmt|;
name|String
name|endpointName
init|=
literal|"seda:withoutConversion?concurrentConsumers=5"
decl_stmt|;
name|sendMessagesTo
argument_list|(
name|endpointName
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpGetWithExplicitStreamClose ()
specifier|public
name|void
name|testHttpGetWithExplicitStreamClose
parameter_list|()
throws|throws
name|Exception
block|{
comment|// We close connections explicitely at the very end of the flow
comment|// (camel doesn't know when the stream is not needed any more)
name|MockEndpoint
name|mockEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:results"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:withoutConversion?concurrentConsumers=5"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Object
name|response
init|=
name|mockEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|InputStream
name|responseStream
init|=
name|assertIsInstanceOf
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|response
argument_list|)
decl_stmt|;
name|responseStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|mockEndpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|sendMessagesTo (String endpointName, int count)
specifier|protected
name|void
name|sendMessagesTo
parameter_list|(
name|String
name|endpointName
parameter_list|,
name|int
name|count
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|mockEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:results"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
name|count
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|endpointName
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|mockEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|list
control|)
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
name|log
operator|.
name|debug
argument_list|(
literal|"Body: "
operator|+
name|body
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have a body!"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"body should contain:<html"
argument_list|,
name|body
operator|.
name|contains
argument_list|(
literal|"<html"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
block|{
name|from
argument_list|(
literal|"seda:withConversion?concurrentConsumers=5"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:{{port}}/search"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:results"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:withoutConversion?concurrentConsumers=5"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:{{port}}/search"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:results"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:{{port}}/search"
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<html>Bye World</html>"
argument_list|)
expr_stmt|;
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

