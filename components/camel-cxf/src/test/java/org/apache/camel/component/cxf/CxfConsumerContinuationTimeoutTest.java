begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|AsyncCallback
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
name|AsyncProcessor
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|AsyncProcessorHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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
DECL|class|CxfConsumerContinuationTimeoutTest
specifier|public
class|class
name|CxfConsumerContinuationTimeoutTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|ECHO_METHOD
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_METHOD
init|=
literal|"ns1:echo xmlns:ns1=\"http://cxf.component.camel.apache.org/\""
decl_stmt|;
DECL|field|ECHO_RESPONSE
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_RESPONSE
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<soap:Body><ns1:echoResponse xmlns:ns1=\"http://cxf.component.camel.apache.org/\">"
operator|+
literal|"<return xmlns=\"http://cxf.component.camel.apache.org/\">echo Hello World!</return>"
operator|+
literal|"</ns1:echoResponse></soap:Body></soap:Envelope>"
decl_stmt|;
DECL|field|ECHO_BOOLEAN_RESPONSE
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_BOOLEAN_RESPONSE
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<soap:Body><ns1:echoBooleanResponse xmlns:ns1=\"http://cxf.component.camel.apache.org/\">"
operator|+
literal|"<return xmlns=\"http://cxf.component.camel.apache.org/\">true</return>"
operator|+
literal|"</ns1:echoBooleanResponse></soap:Body></soap:Envelope>"
decl_stmt|;
DECL|field|simpleEndpointAddress
specifier|protected
specifier|final
name|String
name|simpleEndpointAddress
init|=
literal|"http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
operator|+
literal|"/"
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"/test"
decl_stmt|;
DECL|field|simpleEndpointURI
specifier|protected
specifier|final
name|String
name|simpleEndpointURI
init|=
literal|"cxf://"
operator|+
name|simpleEndpointAddress
operator|+
literal|"?serviceClass=org.apache.camel.component.cxf.HelloService"
decl_stmt|;
DECL|field|pool
specifier|protected
name|ExecutorService
name|pool
decl_stmt|;
annotation|@
name|Override
DECL|method|isCreateCamelContextPerClass ()
specifier|public
name|boolean
name|isCreateCamelContextPerClass
parameter_list|()
block|{
return|return
literal|true
return|;
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
name|pool
operator|=
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
literal|"MyPool"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Sensitive Data"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|simpleEndpointURI
operator|+
literal|"&continuationTimeout=30000&dataFormat=MESSAGE"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|simpleEndpointURI
operator|+
literal|"&continuationTimeout=30000&dataFormat=MESSAGE"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|AsyncProcessor
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
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|asyncCallback
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
comment|// check the content-length header is filtered
name|Object
name|value
init|=
name|in
operator|.
name|getHeader
argument_list|(
literal|"Content-Length"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"The Content-Length header should be removed"
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|// Get the request message
name|String
name|request
init|=
name|in
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|priority
init|=
name|in
operator|.
name|getHeader
argument_list|(
literal|"priority"
argument_list|,
literal|"fast"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// need not to block this thread to simulate slow response so use a thread pool to wait
if|if
condition|(
literal|"slow"
operator|.
name|equalsIgnoreCase
argument_list|(
name|priority
argument_list|)
condition|)
block|{
name|pool
operator|.
name|submit
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Sleeping for 50 seconds to simulate slow response"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|35000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
finally|finally
block|{
name|asyncCallback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
else|else
block|{
comment|// Send the response message back
if|if
condition|(
name|request
operator|.
name|indexOf
argument_list|(
name|ECHO_METHOD
argument_list|)
operator|>
literal|0
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|ECHO_RESPONSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// echoBoolean call
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|ECHO_BOOLEAN_RESPONSE
argument_list|)
expr_stmt|;
block|}
block|}
name|asyncCallback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testNoTimeout ()
specifier|public
name|void
name|testNoTimeout
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
literal|"direct:start"
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
name|ECHO_BOOLEAN_RESPONSE
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"CAMEL-12104"
argument_list|)
DECL|method|testTimeout ()
specifier|public
name|void
name|testTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Bye World"
argument_list|,
literal|"priority"
argument_list|,
literal|"slow"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|contains
argument_list|(
literal|"The OUT message was not received within: 30000 millis."
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

