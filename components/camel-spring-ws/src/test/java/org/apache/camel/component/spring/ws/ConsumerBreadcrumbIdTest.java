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
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|jaxb
operator|.
name|QuoteRequest
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
name|model
operator|.
name|dataformat
operator|.
name|JaxbDataFormat
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
name|client
operator|.
name|core
operator|.
name|WebServiceTemplate
import|;
end_import

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"TODO: investigate for Camel 3.0"
argument_list|)
annotation|@
name|ContextConfiguration
annotation|@
name|RunWith
argument_list|(
name|SpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
DECL|class|ConsumerBreadcrumbIdTest
specifier|public
class|class
name|ConsumerBreadcrumbIdTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Autowired
DECL|field|endpointMapping
specifier|private
name|CamelEndpointMapping
name|endpointMapping
decl_stmt|;
annotation|@
name|Autowired
DECL|field|webServiceTemplate
specifier|private
name|WebServiceTemplate
name|webServiceTemplate
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
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
name|registry
operator|.
name|bind
argument_list|(
literal|"webServiceTemplate"
argument_list|,
name|this
operator|.
name|webServiceTemplate
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Test
DECL|method|consumeWebServiceWithPojoRequestWhichIsWithBreadcrumb ()
specifier|public
name|void
name|consumeWebServiceWithPojoRequestWhichIsWithBreadcrumb
parameter_list|()
throws|throws
name|Exception
block|{
name|QuoteRequest
name|request
init|=
operator|new
name|QuoteRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setSymbol
argument_list|(
literal|"GOOG"
argument_list|)
expr_stmt|;
name|template
operator|.
name|request
argument_list|(
literal|"direct:webservice-marshall-asin"
argument_list|,
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
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"breadcrumbId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"breadcrumbId"
argument_list|)
argument_list|,
literal|"ID-Ralfs-MacBook-Pro-local-50523-1423553069254-0-5"
argument_list|)
expr_stmt|;
block|}
block|}
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
name|JaxbDataFormat
name|jaxb
init|=
operator|new
name|JaxbDataFormat
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|jaxb
operator|.
name|setContextPath
argument_list|(
literal|"org.apache.camel.component.spring.ws.jaxb"
argument_list|)
expr_stmt|;
comment|// request webservice
name|from
argument_list|(
literal|"direct:webservice-marshall-asin"
argument_list|)
operator|.
name|marshal
argument_list|(
name|jaxb
argument_list|)
operator|.
name|to
argument_list|(
literal|"spring-ws:http://localhost/?soapAction=http://www.stockquotes.edu/GetQuoteAsIn&webServiceTemplate=#webServiceTemplate"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// provide web service
name|from
argument_list|(
literal|"spring-ws:soapaction:http://www.stockquotes.edu/GetQuoteAsIn?endpointMapping=#endpointMapping"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"setin"
argument_list|,
name|constant
argument_list|(
literal|"true"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|StockQuoteResponseProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

