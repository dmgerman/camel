begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.transformer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|transformer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Consumer
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
name|Converter
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
name|ExchangePattern
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
name|Producer
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
name|TypeConverters
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
name|model
operator|.
name|DataFormatDefinition
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
name|DataType
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
name|DataTypeAware
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
name|Transformer
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
name|DefaultAsyncProducer
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
name|DefaultComponent
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
name|DefaultDataFormat
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
name|DefaultEndpoint
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
name|DefaultExchange
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A TransformerTest demonstrates contract based declarative transformation via  * Java DSL.  */
end_comment

begin_class
DECL|class|TransformerRouteTest
specifier|public
class|class
name|TransformerRouteTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|TransformerRouteTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testJavaTransformer ()
specifier|public
name|void
name|testJavaTransformer
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|abcresult
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:abcresult"
argument_list|)
decl_stmt|;
name|abcresult
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|abcresult
operator|.
name|whenAnyExchangeReceived
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Asserting String -> XOrderResponse convertion"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|XOrderResponse
operator|.
name|class
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|MockEndpoint
name|xyzresult
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:xyzresult"
argument_list|)
decl_stmt|;
name|xyzresult
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|xyzresult
operator|.
name|whenAnyExchangeReceived
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Asserting String -> XOrderResponse convertion is not yet performed"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"response"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|AOrder
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|answerEx
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:abc"
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|answerEx
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|answerEx
operator|.
name|getException
argument_list|()
throw|;
block|}
name|assertEquals
argument_list|(
name|AOrderResponse
operator|.
name|class
argument_list|,
name|answerEx
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDataFormatTransformer ()
specifier|public
name|void
name|testDataFormatTransformer
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|xyzresult
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:xyzresult"
argument_list|)
decl_stmt|;
name|xyzresult
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|xyzresult
operator|.
name|whenAnyExchangeReceived
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Asserting String -> XOrderResponse convertion is not yet performed"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"response"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
operator|(
operator|(
name|DataTypeAware
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|)
operator|.
name|setBody
argument_list|(
literal|"{name:XOrder}"
argument_list|,
operator|new
name|DataType
argument_list|(
literal|"json:JsonXOrder"
argument_list|)
argument_list|)
expr_stmt|;
name|Exchange
name|answerEx
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:dataFormat"
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|answerEx
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|answerEx
operator|.
name|getException
argument_list|()
throw|;
block|}
name|assertEquals
argument_list|(
literal|"{name:XOrderResponse}"
argument_list|,
name|answerEx
operator|.
name|getMessage
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
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointTransformer ()
specifier|public
name|void
name|testEndpointTransformer
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|xyzresult
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:xyzresult"
argument_list|)
decl_stmt|;
name|xyzresult
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|xyzresult
operator|.
name|whenAnyExchangeReceived
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Asserting String -> XOrderResponse convertion is not yet performed"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"response"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<XOrder/>"
argument_list|)
expr_stmt|;
name|Exchange
name|answerEx
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:endpoint"
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|answerEx
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|answerEx
operator|.
name|getException
argument_list|()
throw|;
block|}
name|assertEquals
argument_list|(
literal|"<XOrderResponse/>"
argument_list|,
name|answerEx
operator|.
name|getMessage
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
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCustomTransformer ()
specifier|public
name|void
name|testCustomTransformer
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|xyzresult
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:xyzresult"
argument_list|)
decl_stmt|;
name|xyzresult
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|xyzresult
operator|.
name|whenAnyExchangeReceived
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Asserting String -> XOrderResponse convertion is not yet performed"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"response"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"name=XOrder"
argument_list|)
expr_stmt|;
name|Exchange
name|answerEx
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:custom"
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|answerEx
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|answerEx
operator|.
name|getException
argument_list|()
throw|;
block|}
name|assertEquals
argument_list|(
literal|"name=XOrderResponse"
argument_list|,
name|answerEx
operator|.
name|getMessage
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
name|assertMockEndpointsSatisfied
argument_list|()
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
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|addTypeConverters
argument_list|(
operator|new
name|MyTypeConverters
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:abc"
argument_list|)
operator|.
name|inputType
argument_list|(
name|AOrder
operator|.
name|class
argument_list|)
operator|.
name|outputType
argument_list|(
name|AOrderResponse
operator|.
name|class
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Asserting input -> AOrder convertion"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AOrder
operator|.
name|class
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|inOut
argument_list|(
literal|"direct:xyz"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:abcresult"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:xyz"
argument_list|)
operator|.
name|inputType
argument_list|(
name|XOrder
operator|.
name|class
argument_list|)
operator|.
name|outputType
argument_list|(
name|XOrderResponse
operator|.
name|class
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Asserting input -> XOrder convertion"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|XOrder
operator|.
name|class
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"response"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:xyzresult"
argument_list|)
expr_stmt|;
name|transformer
argument_list|()
operator|.
name|scheme
argument_list|(
literal|"json"
argument_list|)
operator|.
name|withDataFormat
argument_list|(
operator|new
name|MyJsonDataFormatDefinition
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:dataFormat"
argument_list|)
operator|.
name|inputType
argument_list|(
literal|"json:JsonXOrder"
argument_list|)
operator|.
name|outputType
argument_list|(
literal|"json:JsonXOrderResponse"
argument_list|)
operator|.
name|inOut
argument_list|(
literal|"direct:xyz"
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"myxml"
argument_list|,
operator|new
name|MyXmlComponent
argument_list|()
argument_list|)
expr_stmt|;
name|transformer
argument_list|()
operator|.
name|fromType
argument_list|(
literal|"xml:XmlXOrder"
argument_list|)
operator|.
name|toType
argument_list|(
name|XOrder
operator|.
name|class
argument_list|)
operator|.
name|withUri
argument_list|(
literal|"myxml:endpoint"
argument_list|)
expr_stmt|;
name|transformer
argument_list|()
operator|.
name|fromType
argument_list|(
name|XOrderResponse
operator|.
name|class
argument_list|)
operator|.
name|toType
argument_list|(
literal|"xml:XmlXOrderResponse"
argument_list|)
operator|.
name|withUri
argument_list|(
literal|"myxml:endpoint"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:endpoint"
argument_list|)
operator|.
name|inputType
argument_list|(
literal|"xml:XmlXOrder"
argument_list|)
operator|.
name|outputType
argument_list|(
literal|"xml:XmlXOrderResponse"
argument_list|)
operator|.
name|inOut
argument_list|(
literal|"direct:xyz"
argument_list|)
expr_stmt|;
name|transformer
argument_list|()
operator|.
name|fromType
argument_list|(
literal|"other:OtherXOrder"
argument_list|)
operator|.
name|toType
argument_list|(
name|XOrder
operator|.
name|class
argument_list|)
operator|.
name|withJava
argument_list|(
name|OtherToXOrderTransformer
operator|.
name|class
argument_list|)
expr_stmt|;
name|transformer
argument_list|()
operator|.
name|fromType
argument_list|(
name|XOrderResponse
operator|.
name|class
argument_list|)
operator|.
name|toType
argument_list|(
literal|"other:OtherXOrderResponse"
argument_list|)
operator|.
name|withJava
argument_list|(
name|XOrderResponseToOtherTransformer
operator|.
name|class
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:custom"
argument_list|)
operator|.
name|inputType
argument_list|(
literal|"other:OtherXOrder"
argument_list|)
operator|.
name|outputType
argument_list|(
literal|"other:OtherXOrderResponse"
argument_list|)
operator|.
name|inOut
argument_list|(
literal|"direct:xyz"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyTypeConverters
specifier|public
specifier|static
class|class
name|MyTypeConverters
implements|implements
name|TypeConverters
block|{
annotation|@
name|Converter
DECL|method|toAOrder (String order)
specifier|public
name|AOrder
name|toAOrder
parameter_list|(
name|String
name|order
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"TypeConverter: String -> AOrder"
argument_list|)
expr_stmt|;
return|return
operator|new
name|AOrder
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toXOrder (AOrder aorder)
specifier|public
name|XOrder
name|toXOrder
parameter_list|(
name|AOrder
name|aorder
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"TypeConverter: AOrder -> XOrder"
argument_list|)
expr_stmt|;
return|return
operator|new
name|XOrder
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toXOrderResponse (String res)
specifier|public
name|XOrderResponse
name|toXOrderResponse
parameter_list|(
name|String
name|res
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"TypeConverter: String -> XOrderResponse"
argument_list|)
expr_stmt|;
return|return
operator|new
name|XOrderResponse
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toAOrderResponse (XOrderResponse xres)
specifier|public
name|AOrderResponse
name|toAOrderResponse
parameter_list|(
name|XOrderResponse
name|xres
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"TypeConverter: XOrderResponse -> AOrderResponse"
argument_list|)
expr_stmt|;
return|return
operator|new
name|AOrderResponse
argument_list|()
return|;
block|}
block|}
DECL|class|MyJsonDataFormatDefinition
specifier|public
specifier|static
class|class
name|MyJsonDataFormatDefinition
extends|extends
name|DataFormatDefinition
block|{
DECL|method|MyJsonDataFormatDefinition ()
specifier|public
name|MyJsonDataFormatDefinition
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|DefaultDataFormat
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|graph
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|graph
operator|.
name|toString
argument_list|()
argument_list|,
name|XOrderResponse
operator|.
name|class
argument_list|,
name|graph
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"DataFormat: XOrderResponse -> JSON"
argument_list|)
expr_stmt|;
name|stream
operator|.
name|write
argument_list|(
literal|"{name:XOrderResponse}"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|stream
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
init|=
literal|""
decl_stmt|;
name|String
name|input
init|=
literal|""
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|input
operator|+=
name|line
expr_stmt|;
block|}
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{name:XOrder}"
argument_list|,
name|input
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"DataFormat: JSON -> XOrder"
argument_list|)
expr_stmt|;
return|return
operator|new
name|XOrder
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|MyXmlComponent
specifier|public
specifier|static
class|class
name|MyXmlComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|MyXmlEndpoint
argument_list|()
return|;
block|}
block|}
DECL|class|MyXmlEndpoint
specifier|public
specifier|static
class|class
name|MyXmlEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|DefaultAsyncProducer
argument_list|(
name|this
argument_list|)
block|{
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
name|callback
parameter_list|)
block|{
name|Object
name|input
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|input
operator|instanceof
name|XOrderResponse
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Endpoint: XOrderResponse -> XML"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<XOrderResponse/>"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"<XOrder/>"
argument_list|,
name|input
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Endpoint: XML -> XOrder"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|XOrder
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|callback
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
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|"myxml:endpoint"
return|;
block|}
block|}
DECL|class|OtherToXOrderTransformer
specifier|public
specifier|static
class|class
name|OtherToXOrderTransformer
extends|extends
name|Transformer
block|{
annotation|@
name|Override
DECL|method|transform (Message message, DataType from, DataType to)
specifier|public
name|void
name|transform
parameter_list|(
name|Message
name|message
parameter_list|,
name|DataType
name|from
parameter_list|,
name|DataType
name|to
parameter_list|)
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"name=XOrder"
argument_list|,
name|message
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Bean: Other -> XOrder"
argument_list|)
expr_stmt|;
name|message
operator|.
name|setBody
argument_list|(
operator|new
name|XOrder
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|XOrderResponseToOtherTransformer
specifier|public
specifier|static
class|class
name|XOrderResponseToOtherTransformer
extends|extends
name|Transformer
block|{
annotation|@
name|Override
DECL|method|transform (Message message, DataType from, DataType to)
specifier|public
name|void
name|transform
parameter_list|(
name|Message
name|message
parameter_list|,
name|DataType
name|from
parameter_list|,
name|DataType
name|to
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Bean: XOrderResponse -> Other"
argument_list|)
expr_stmt|;
name|message
operator|.
name|setBody
argument_list|(
literal|"name=XOrderResponse"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|AOrder
specifier|public
specifier|static
class|class
name|AOrder
block|{     }
DECL|class|AOrderResponse
specifier|public
specifier|static
class|class
name|AOrderResponse
block|{     }
DECL|class|XOrder
specifier|public
specifier|static
class|class
name|XOrder
block|{     }
DECL|class|XOrderResponse
specifier|public
specifier|static
class|class
name|XOrderResponse
block|{     }
block|}
end_class

end_unit

