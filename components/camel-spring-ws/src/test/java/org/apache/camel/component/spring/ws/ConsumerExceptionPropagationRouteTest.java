begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|spring
operator|.
name|ws
operator|.
name|bean
operator|.
name|CamelEndpointMapping
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
name|Registry
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
name|support
operator|.
name|SimpleRegistry
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
name|Before
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringJUnit4ClassRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|test
operator|.
name|server
operator|.
name|MockWebServiceClient
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|test
operator|.
name|server
operator|.
name|RequestCreators
operator|.
name|withPayload
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|test
operator|.
name|server
operator|.
name|ResponseMatchers
operator|.
name|serverOrReceiverFault
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
annotation|@
name|RunWith
argument_list|(
name|SpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
DECL|class|ConsumerExceptionPropagationRouteTest
specifier|public
class|class
name|ConsumerExceptionPropagationRouteTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|xmlRequestForGoogleStockQuote
specifier|private
specifier|final
name|String
name|xmlRequestForGoogleStockQuote
init|=
literal|"<GetQuote xmlns=\"http://www.webserviceX.NET/\"><symbol>GOOG</symbol></GetQuote>"
decl_stmt|;
annotation|@
name|Autowired
DECL|field|endpointMapping
specifier|private
name|CamelEndpointMapping
name|endpointMapping
decl_stmt|;
annotation|@
name|Autowired
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|mockClient
specifier|private
name|MockWebServiceClient
name|mockClient
decl_stmt|;
annotation|@
name|Before
DECL|method|createClient ()
specifier|public
name|void
name|createClient
parameter_list|()
block|{
name|mockClient
operator|=
name|MockWebServiceClient
operator|.
name|createClient
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
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
name|context
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelRegistry ()
specifier|protected
name|Registry
name|createCamelRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|Registry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"endpointMapping"
argument_list|,
name|this
operator|.
name|endpointMapping
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Ignore
argument_list|(
literal|"For now getEndpointUri does not return the initial uri. Info like the endpoint scheme is lost"
argument_list|)
annotation|@
name|Test
DECL|method|testValidUri ()
specifier|public
name|void
name|testValidUri
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|deprecate
init|=
literal|"spring-ws:rootqname:{http://www.webserviceX.NET/}GetQuote?endpointMapping=#endpointMapping"
decl_stmt|;
name|String
name|sanitized
init|=
literal|"spring-ws:rootqname:(http://www.webserviceX.NET/)GetQuote?endpointMapping=#endpointMapping"
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"spring-ws"
argument_list|)
operator|.
name|createEndpoint
argument_list|(
name|deprecate
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|sanitized
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
operator|new
name|URI
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|consumeWebserviceAndTestForSoapFault ()
specifier|public
name|void
name|consumeWebserviceAndTestForSoapFault
parameter_list|()
throws|throws
name|Exception
block|{
name|StreamSource
name|source
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|xmlRequestForGoogleStockQuote
argument_list|)
argument_list|)
decl_stmt|;
name|mockClient
operator|.
name|sendRequest
argument_list|(
name|withPayload
argument_list|(
name|source
argument_list|)
argument_list|)
operator|.
name|andExpect
argument_list|(
name|serverOrReceiverFault
argument_list|()
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
literal|"spring-ws:rootqname:{http://www.webserviceX.NET/}GetQuote?endpointMapping=#endpointMapping"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|RuntimeException
argument_list|(
literal|"this is a test exception!"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

