begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|validator
package|;
end_package

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
name|ValidationException
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
name|impl
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
name|impl
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
name|impl
operator|.
name|DefaultExchange
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
name|Validator
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
comment|/**  * A ValidatorRouteTest demonstrates contract based declarative validation via Java DSL.  */
end_comment

begin_class
DECL|class|ValidatorRouteTest
specifier|public
class|class
name|ValidatorRouteTest
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
name|ValidatorRouteTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|VALIDATOR_INVOKED
specifier|private
specifier|static
specifier|final
name|String
name|VALIDATOR_INVOKED
init|=
literal|"validator-invoked"
decl_stmt|;
DECL|method|testPredicateValidator ()
specifier|public
name|void
name|testPredicateValidator
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"{name:XOrder}"
argument_list|)
expr_stmt|;
name|Exchange
name|answerEx
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:predicate"
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
block|}
DECL|method|testEndpointValidator ()
specifier|public
name|void
name|testEndpointValidator
parameter_list|()
throws|throws
name|Exception
block|{
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
name|getOut
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
name|assertEquals
argument_list|(
name|MyXmlEndpoint
operator|.
name|class
argument_list|,
name|answerEx
operator|.
name|getProperty
argument_list|(
name|VALIDATOR_INVOKED
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCustomValidator ()
specifier|public
name|void
name|testCustomValidator
parameter_list|()
throws|throws
name|Exception
block|{
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
name|getOut
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
name|assertEquals
argument_list|(
name|OtherXOrderResponseValidator
operator|.
name|class
argument_list|,
name|answerEx
operator|.
name|getProperty
argument_list|(
name|VALIDATOR_INVOKED
argument_list|)
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
name|validator
argument_list|()
operator|.
name|type
argument_list|(
literal|"json"
argument_list|)
operator|.
name|withExpression
argument_list|(
name|bodyAs
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"{name:XOrder}"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:predicate"
argument_list|)
operator|.
name|inputTypeWithValidate
argument_list|(
literal|"json:JsonXOrder"
argument_list|)
operator|.
name|outputType
argument_list|(
literal|"json:JsonXOrderResponse"
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"{name:XOrderResponse}"
argument_list|)
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
name|validator
argument_list|()
operator|.
name|type
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
name|outputTypeWithValidate
argument_list|(
literal|"xml:XmlXOrderResponse"
argument_list|)
operator|.
name|validate
argument_list|(
name|exchangeProperty
argument_list|(
name|VALIDATOR_INVOKED
argument_list|)
operator|.
name|isNull
argument_list|()
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"<XOrderResponse/>"
argument_list|)
argument_list|)
expr_stmt|;
name|validator
argument_list|()
operator|.
name|type
argument_list|(
literal|"other:OtherXOrder"
argument_list|)
operator|.
name|withJava
argument_list|(
name|OtherXOrderValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|validator
argument_list|()
operator|.
name|type
argument_list|(
literal|"other:OtherXOrderResponse"
argument_list|)
operator|.
name|withJava
argument_list|(
name|OtherXOrderResponseValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:custom"
argument_list|)
operator|.
name|inputTypeWithValidate
argument_list|(
literal|"other:OtherXOrder"
argument_list|)
operator|.
name|outputTypeWithValidate
argument_list|(
literal|"other:OtherXOrderResponse"
argument_list|)
operator|.
name|validate
argument_list|(
name|exchangeProperty
argument_list|(
name|VALIDATOR_INVOKED
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|OtherXOrderValidator
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"name=XOrderResponse"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
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
name|exchange
operator|.
name|setProperty
argument_list|(
name|VALIDATOR_INVOKED
argument_list|,
name|MyXmlEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<XOrderResponse/>"
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
DECL|class|OtherXOrderValidator
specifier|public
specifier|static
class|class
name|OtherXOrderValidator
extends|extends
name|Validator
block|{
annotation|@
name|Override
DECL|method|validate (Message message, DataType type)
specifier|public
name|void
name|validate
parameter_list|(
name|Message
name|message
parameter_list|,
name|DataType
name|type
parameter_list|)
throws|throws
name|ValidationException
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setProperty
argument_list|(
name|VALIDATOR_INVOKED
argument_list|,
name|OtherXOrderValidator
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Java validation: other XOrder"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|OtherXOrderResponseValidator
specifier|public
specifier|static
class|class
name|OtherXOrderResponseValidator
extends|extends
name|Validator
block|{
annotation|@
name|Override
DECL|method|validate (Message message, DataType type)
specifier|public
name|void
name|validate
parameter_list|(
name|Message
name|message
parameter_list|,
name|DataType
name|type
parameter_list|)
throws|throws
name|ValidationException
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setProperty
argument_list|(
name|VALIDATOR_INVOKED
argument_list|,
name|OtherXOrderResponseValidator
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"name=XOrderResponse"
argument_list|,
name|message
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Java validation: other XOrderResponse"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

