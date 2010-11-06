begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|IOException
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
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
name|TransformerException
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
name|EndpointInject
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
name|test
operator|.
name|junit4
operator|.
name|CamelSpringTestSupport
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
name|Test
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
name|support
operator|.
name|AbstractXmlApplicationContext
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
name|support
operator|.
name|ClassPathXmlApplicationContext
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
name|WebServiceIOException
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
name|SourceExtractor
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|client
operator|.
name|core
operator|.
name|SoapActionCallback
import|;
end_import

begin_class
DECL|class|ConsumerEndpointMappingRouteTest
specifier|public
class|class
name|ConsumerEndpointMappingRouteTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|NOOP_SOURCE_EXTRACTOR
specifier|private
specifier|static
specifier|final
name|SourceExtractor
name|NOOP_SOURCE_EXTRACTOR
init|=
operator|new
name|SourceExtractor
argument_list|()
block|{
specifier|public
name|Object
name|extractData
parameter_list|(
name|Source
name|source
parameter_list|)
throws|throws
name|IOException
throws|,
name|TransformerException
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
DECL|field|xmlRequestForGoogleStockQuote
specifier|private
specifier|final
name|String
name|xmlRequestForGoogleStockQuote
init|=
literal|"<GetQuote xmlns=\"http://www.stockquotes.edu/\"><symbol>GOOG</symbol></GetQuote>"
decl_stmt|;
DECL|field|xmlRequestForGoogleStockQuoteNoNamespace
specifier|private
specifier|final
name|String
name|xmlRequestForGoogleStockQuoteNoNamespace
init|=
literal|"<GetQuote><symbol>GOOG</symbol></GetQuote>"
decl_stmt|;
DECL|field|xmlRequestForGoogleStockQuoteNoNamespaceDifferentBody
specifier|private
specifier|final
name|String
name|xmlRequestForGoogleStockQuoteNoNamespaceDifferentBody
init|=
literal|"<GetQuote><symbol>GRABME</symbol></GetQuote>"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:testRootQName"
argument_list|)
DECL|field|resultEndpointRootQName
specifier|private
name|MockEndpoint
name|resultEndpointRootQName
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:testSoapAction"
argument_list|)
DECL|field|resultEndpointSoapAction
specifier|private
name|MockEndpoint
name|resultEndpointSoapAction
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:testUri"
argument_list|)
DECL|field|resultEndpointUri
specifier|private
name|MockEndpoint
name|resultEndpointUri
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:testXPath"
argument_list|)
DECL|field|resultEndpointXPath
specifier|private
name|MockEndpoint
name|resultEndpointXPath
decl_stmt|;
DECL|field|webServiceTemplate
specifier|private
name|WebServiceTemplate
name|webServiceTemplate
decl_stmt|;
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
name|webServiceTemplate
operator|=
operator|(
name|WebServiceTemplate
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"webServiceTemplate"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRootQName ()
specifier|public
name|void
name|testRootQName
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
name|webServiceTemplate
operator|.
name|sendSourceAndReceive
argument_list|(
name|source
argument_list|,
name|NOOP_SOURCE_EXTRACTOR
argument_list|)
expr_stmt|;
name|resultEndpointRootQName
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpointRootQName
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSoapAction ()
specifier|public
name|void
name|testSoapAction
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
name|xmlRequestForGoogleStockQuoteNoNamespace
argument_list|)
argument_list|)
decl_stmt|;
name|webServiceTemplate
operator|.
name|sendSourceAndReceive
argument_list|(
name|source
argument_list|,
operator|new
name|SoapActionCallback
argument_list|(
literal|"http://www.stockquotes.edu/GetQuote"
argument_list|)
argument_list|,
name|NOOP_SOURCE_EXTRACTOR
argument_list|)
expr_stmt|;
name|resultEndpointSoapAction
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpointSoapAction
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|WebServiceIOException
operator|.
name|class
argument_list|)
DECL|method|testWrongSoapAction ()
specifier|public
name|void
name|testWrongSoapAction
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isJava15
argument_list|()
condition|)
block|{
comment|// does not work on JDK 1.5 due net.javacrumbs.spring-ws-test is not JDK 1.5 compatible
throw|throw
operator|new
name|WebServiceIOException
argument_list|(
literal|"Forced by JDK 1.5"
argument_list|)
throw|;
block|}
name|StreamSource
name|source
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|xmlRequestForGoogleStockQuoteNoNamespace
argument_list|)
argument_list|)
decl_stmt|;
name|webServiceTemplate
operator|.
name|sendSourceAndReceive
argument_list|(
name|source
argument_list|,
operator|new
name|SoapActionCallback
argument_list|(
literal|"http://this-is-a-wrong-soap-action"
argument_list|)
argument_list|,
name|NOOP_SOURCE_EXTRACTOR
argument_list|)
expr_stmt|;
name|resultEndpointSoapAction
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXPath ()
specifier|public
name|void
name|testXPath
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
name|xmlRequestForGoogleStockQuoteNoNamespaceDifferentBody
argument_list|)
argument_list|)
decl_stmt|;
name|webServiceTemplate
operator|.
name|sendSourceAndReceive
argument_list|(
name|source
argument_list|,
name|NOOP_SOURCE_EXTRACTOR
argument_list|)
expr_stmt|;
name|resultEndpointXPath
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpointXPath
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUri ()
specifier|public
name|void
name|testUri
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
name|xmlRequestForGoogleStockQuoteNoNamespace
argument_list|)
argument_list|)
decl_stmt|;
name|webServiceTemplate
operator|.
name|sendSourceAndReceive
argument_list|(
literal|"http://localhost/stockquote2"
argument_list|,
name|source
argument_list|,
name|NOOP_SOURCE_EXTRACTOR
argument_list|)
expr_stmt|;
name|resultEndpointUri
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpointUri
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|WebServiceIOException
operator|.
name|class
argument_list|)
DECL|method|testWrongUri ()
specifier|public
name|void
name|testWrongUri
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isJava15
argument_list|()
condition|)
block|{
comment|// does not work on JDK 1.5 due net.javacrumbs.spring-ws-test is not JDK 1.5 compatible
throw|throw
operator|new
name|WebServiceIOException
argument_list|(
literal|"Forced by JDK 1.5"
argument_list|)
throw|;
block|}
name|StreamSource
name|source
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|xmlRequestForGoogleStockQuoteNoNamespace
argument_list|)
argument_list|)
decl_stmt|;
name|webServiceTemplate
operator|.
name|sendSourceAndReceive
argument_list|(
literal|"http://localhost/wrong"
argument_list|,
name|source
argument_list|,
name|NOOP_SOURCE_EXTRACTOR
argument_list|)
expr_stmt|;
name|resultEndpointUri
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"org/apache/camel/component/spring/ws/ConsumerEndpointMappingRouteTest-context.xml"
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

