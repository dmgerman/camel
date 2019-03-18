begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|stream
operator|.
name|CachedOutputStream
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
name|Synchronization
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
comment|//Modified from https://issues.apache.org/jira/secure/attachment/12730161/0001-CAMEL-8419-Camel-StreamCache-does-not-work-with-CXF-.patch
end_comment

begin_class
DECL|class|CxfConsumerStreamCacheTest
specifier|public
class|class
name|CxfConsumerStreamCacheTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|REQUEST_MESSAGE
specifier|protected
specifier|static
specifier|final
name|String
name|REQUEST_MESSAGE
init|=
literal|"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"test/service\">"
operator|+
literal|"<soapenv:Header/><soapenv:Body><ser:ping/></soapenv:Body></soapenv:Envelope>"
decl_stmt|;
DECL|field|RESPONSE_MESSAGE_BEGINE
specifier|protected
specifier|static
specifier|final
name|String
name|RESPONSE_MESSAGE_BEGINE
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<soap:Body><pong xmlns=\"test/service\""
decl_stmt|;
DECL|field|RESPONSE_MESSAGE_END
specifier|protected
specifier|static
specifier|final
name|String
name|RESPONSE_MESSAGE_END
init|=
literal|"/></soap:Body></soap:Envelope>"
decl_stmt|;
DECL|field|RESPONSE
specifier|protected
specifier|static
specifier|final
name|String
name|RESPONSE
init|=
literal|"<pong xmlns=\"test/service\"/>"
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
literal|"?synchronous="
operator|+
name|isSynchronous
argument_list|()
operator|+
literal|"&serviceClass=org.apache.camel.component.cxf.ServiceProvider&dataFormat=PAYLOAD"
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
name|getContext
argument_list|()
operator|.
name|setStreamCaching
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|setSpoolThreshold
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|getFromEndpointUri
argument_list|()
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
specifier|final
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
name|Node
name|node
init|=
name|in
operator|.
name|getBody
argument_list|(
name|Node
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|cos
operator|.
name|write
argument_list|(
name|RESPONSE
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|cos
operator|.
name|close
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|cos
operator|.
name|newStreamCache
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|Synchronization
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"mock:onComplete"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{                              }
block|}
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
annotation|@
name|Test
DECL|method|testInvokingServiceFromHttpCompnent ()
specifier|public
name|void
name|testInvokingServiceFromHttpCompnent
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:onComplete"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
comment|// call the service with right post message
name|String
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|simpleEndpointAddress
argument_list|,
name|REQUEST_MESSAGE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong response "
argument_list|,
name|response
operator|.
name|startsWith
argument_list|(
name|RESPONSE_MESSAGE_BEGINE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong response "
argument_list|,
name|response
operator|.
name|endsWith
argument_list|(
name|RESPONSE_MESSAGE_END
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
name|simpleEndpointAddress
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Excpetion to get exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// do nothing here
block|}
name|response
operator|=
name|template
operator|.
name|requestBody
argument_list|(
name|simpleEndpointAddress
argument_list|,
name|REQUEST_MESSAGE
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong response "
argument_list|,
name|response
operator|.
name|startsWith
argument_list|(
name|RESPONSE_MESSAGE_BEGINE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong response "
argument_list|,
name|response
operator|.
name|endsWith
argument_list|(
name|RESPONSE_MESSAGE_END
argument_list|)
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|getFromEndpointUri ()
specifier|protected
name|String
name|getFromEndpointUri
parameter_list|()
block|{
return|return
name|simpleEndpointURI
return|;
block|}
DECL|method|isSynchronous ()
specifier|protected
name|boolean
name|isSynchronous
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

