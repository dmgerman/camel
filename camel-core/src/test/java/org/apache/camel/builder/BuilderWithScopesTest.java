begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|CamelContext
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
name|TestSupport
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
name|impl
operator|.
name|DefaultCamelContext
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
name|processor
operator|.
name|DelegateProcessor
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|BuilderWithScopesTest
specifier|public
class|class
name|BuilderWithScopesTest
extends|extends
name|TestSupport
block|{
DECL|field|order
specifier|final
name|ArrayList
argument_list|<
name|String
argument_list|>
name|order
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|interceptor1
specifier|final
name|DelegateProcessor
name|interceptor1
init|=
operator|new
name|DelegateProcessor
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
name|order
operator|.
name|add
argument_list|(
literal|"START:1"
argument_list|)
expr_stmt|;
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|order
operator|.
name|add
argument_list|(
literal|"END:1"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
DECL|field|interceptor2
specifier|final
name|DelegateProcessor
name|interceptor2
init|=
operator|new
name|DelegateProcessor
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
name|order
operator|.
name|add
argument_list|(
literal|"START:2"
argument_list|)
expr_stmt|;
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|order
operator|.
name|add
argument_list|(
literal|"END:2"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
DECL|field|orderProcessor
specifier|final
name|Processor
name|orderProcessor
init|=
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
block|{
name|order
operator|.
name|add
argument_list|(
literal|"INVOKED"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
DECL|field|orderProcessor2
specifier|final
name|Processor
name|orderProcessor2
init|=
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
block|{
name|order
operator|.
name|add
argument_list|(
literal|"INVOKED2"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
DECL|field|orderProcessor3
specifier|final
name|Processor
name|orderProcessor3
init|=
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
block|{
name|order
operator|.
name|add
argument_list|(
literal|"INVOKED3"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
DECL|field|toProcessor
specifier|final
name|Processor
name|toProcessor
init|=
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
block|{
name|order
operator|.
name|add
argument_list|(
literal|"TO"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
DECL|field|validator
specifier|final
name|Processor
name|validator
init|=
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
name|order
operator|.
name|add
argument_list|(
literal|"VALIDATE"
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The foo header is not present."
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|value
operator|.
name|equals
argument_list|(
literal|"bar"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
name|exchange
argument_list|,
literal|"The foo header does not equal bar! Was: "
operator|+
name|value
argument_list|)
throw|;
block|}
block|}
block|}
decl_stmt|;
DECL|method|runTest (RouteBuilder builder, ArrayList<String> expected)
specifier|protected
name|void
name|runTest
parameter_list|(
name|RouteBuilder
name|builder
parameter_list|,
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
name|builder
argument_list|,
name|expected
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|runTest (RouteBuilder builder, ArrayList<String> expected, String header)
specifier|protected
name|void
name|runTest
parameter_list|(
name|RouteBuilder
name|builder
parameter_list|,
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
parameter_list|,
name|String
name|header
parameter_list|)
throws|throws
name|Exception
block|{
name|order
operator|.
name|clear
argument_list|()
expr_stmt|;
name|CamelContext
name|container
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|container
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|container
operator|.
name|start
argument_list|()
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|container
operator|.
name|getEndpoint
argument_list|(
literal|"direct:a"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|header
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
name|header
argument_list|)
expr_stmt|;
block|}
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Invocation order:"
operator|+
name|order
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|order
argument_list|)
expr_stmt|;
block|}
DECL|method|testRouteWithFilterEnd ()
specifier|public
name|void
name|testRouteWithFilterEnd
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"TO"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|filter
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|orderProcessor
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|process
argument_list|(
name|toProcessor
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
name|expected
argument_list|,
literal|"banana"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRouteWithFilterNoEnd ()
specifier|public
name|void
name|testRouteWithFilterNoEnd
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|runTest
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|filter
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|orderProcessor
argument_list|)
operator|.
name|process
argument_list|(
name|toProcessor
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
name|expected
argument_list|,
literal|"banana"
argument_list|)
expr_stmt|;
block|}
DECL|method|createChoiceBuilder ()
specifier|protected
name|RouteBuilder
name|createChoiceBuilder
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
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|orderProcessor
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"cheese"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|orderProcessor2
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|process
argument_list|(
name|toProcessor
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|testRouteWithChoice1 ()
specifier|public
name|void
name|testRouteWithChoice1
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"TO"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createChoiceBuilder
argument_list|()
argument_list|,
name|expected
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRouteWithChoice2 ()
specifier|public
name|void
name|testRouteWithChoice2
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED2"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"TO"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createChoiceBuilder
argument_list|()
argument_list|,
name|expected
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRouteWithChoice3 ()
specifier|public
name|void
name|testRouteWithChoice3
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"TO"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createChoiceBuilder
argument_list|()
argument_list|,
name|expected
argument_list|,
literal|"banana"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRouteWithChoiceNoEnd ()
specifier|public
name|void
name|testRouteWithChoiceNoEnd
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|orderProcessor
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"cheese"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|orderProcessor2
argument_list|)
operator|.
name|process
argument_list|(
name|toProcessor
argument_list|)
expr_stmt|;
comment|// continuation of the second when clause
block|}
block|}
argument_list|,
name|expected
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
block|}
DECL|method|createChoiceWithOtherwiseBuilder ()
specifier|protected
name|RouteBuilder
name|createChoiceWithOtherwiseBuilder
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
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|orderProcessor
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"cheese"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|orderProcessor2
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|process
argument_list|(
name|orderProcessor3
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|process
argument_list|(
name|toProcessor
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|testRouteWithChoiceOtherwise1 ()
specifier|public
name|void
name|testRouteWithChoiceOtherwise1
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"TO"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createChoiceWithOtherwiseBuilder
argument_list|()
argument_list|,
name|expected
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRouteWithChoiceOtherwise2 ()
specifier|public
name|void
name|testRouteWithChoiceOtherwise2
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED2"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"TO"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createChoiceWithOtherwiseBuilder
argument_list|()
argument_list|,
name|expected
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRouteWithChoiceOtherwise3 ()
specifier|public
name|void
name|testRouteWithChoiceOtherwise3
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED3"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"TO"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createChoiceWithOtherwiseBuilder
argument_list|()
argument_list|,
name|expected
argument_list|,
literal|"banana"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRouteWithChoiceOtherwiseNoEnd ()
specifier|public
name|void
name|testRouteWithChoiceOtherwiseNoEnd
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|orderProcessor
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"cheese"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|orderProcessor2
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|process
argument_list|(
name|orderProcessor3
argument_list|)
operator|.
name|process
argument_list|(
name|toProcessor
argument_list|)
expr_stmt|;
comment|// continuation of the otherwise clause
block|}
block|}
argument_list|,
name|expected
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
block|}
DECL|method|createTryCatchNoEnd ()
specifier|protected
name|RouteBuilder
name|createTryCatchNoEnd
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|doTry
argument_list|()
operator|.
name|process
argument_list|(
name|validator
argument_list|)
operator|.
name|process
argument_list|(
name|toProcessor
argument_list|)
operator|.
name|doCatch
argument_list|(
name|ValidationException
operator|.
name|class
argument_list|)
operator|.
name|process
argument_list|(
name|orderProcessor
argument_list|)
operator|.
name|process
argument_list|(
name|orderProcessor3
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|testRouteWithTryCatchNoEndNoException ()
specifier|public
name|void
name|testRouteWithTryCatchNoEndNoException
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"VALIDATE"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"TO"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createTryCatchNoEnd
argument_list|()
argument_list|,
name|expected
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRouteWithTryCatchNoEndWithCaughtException ()
specifier|public
name|void
name|testRouteWithTryCatchNoEndWithCaughtException
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"VALIDATE"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED3"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createTryCatchNoEnd
argument_list|()
argument_list|,
name|expected
argument_list|,
literal|"banana"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRouteWithTryCatchNoEndWithUncaughtException ()
specifier|public
name|void
name|testRouteWithTryCatchNoEndWithUncaughtException
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"VALIDATE"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createTryCatchNoEnd
argument_list|()
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
DECL|method|createTryCatchEnd ()
specifier|protected
name|RouteBuilder
name|createTryCatchEnd
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|doTry
argument_list|()
operator|.
name|process
argument_list|(
name|validator
argument_list|)
operator|.
name|process
argument_list|(
name|toProcessor
argument_list|)
operator|.
name|doCatch
argument_list|(
name|ValidationException
operator|.
name|class
argument_list|)
operator|.
name|process
argument_list|(
name|orderProcessor
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|process
argument_list|(
name|orderProcessor3
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|testRouteWithTryCatchEndNoException ()
specifier|public
name|void
name|testRouteWithTryCatchEndNoException
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"VALIDATE"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"TO"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED3"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createTryCatchEnd
argument_list|()
argument_list|,
name|expected
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRouteWithTryCatchEndWithCaughtException ()
specifier|public
name|void
name|testRouteWithTryCatchEndWithCaughtException
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"VALIDATE"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED3"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createTryCatchEnd
argument_list|()
argument_list|,
name|expected
argument_list|,
literal|"banana"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRouteWithTryCatchEndWithUncaughtException ()
specifier|public
name|void
name|testRouteWithTryCatchEndWithUncaughtException
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"VALIDATE"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createTryCatchEnd
argument_list|()
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
DECL|method|createTryCatchFinallyNoEnd ()
specifier|protected
name|RouteBuilder
name|createTryCatchFinallyNoEnd
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|doTry
argument_list|()
operator|.
name|process
argument_list|(
name|validator
argument_list|)
operator|.
name|process
argument_list|(
name|toProcessor
argument_list|)
operator|.
name|doCatch
argument_list|(
name|ValidationException
operator|.
name|class
argument_list|)
operator|.
name|process
argument_list|(
name|orderProcessor
argument_list|)
operator|.
name|doFinally
argument_list|()
operator|.
name|process
argument_list|(
name|orderProcessor2
argument_list|)
operator|.
name|process
argument_list|(
name|orderProcessor3
argument_list|)
expr_stmt|;
comment|// continuation of the finallyBlock clause
block|}
block|}
return|;
block|}
DECL|method|testRouteWithTryCatchFinallyNoEndNoException ()
specifier|public
name|void
name|testRouteWithTryCatchFinallyNoEndNoException
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"VALIDATE"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"TO"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED2"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED3"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createTryCatchFinallyNoEnd
argument_list|()
argument_list|,
name|expected
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRouteWithTryCatchFinallyNoEndWithCaughtException ()
specifier|public
name|void
name|testRouteWithTryCatchFinallyNoEndWithCaughtException
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"VALIDATE"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED2"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED3"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createTryCatchFinallyNoEnd
argument_list|()
argument_list|,
name|expected
argument_list|,
literal|"banana"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRouteWithTryCatchFinallyNoEndWithUncaughtException ()
specifier|public
name|void
name|testRouteWithTryCatchFinallyNoEndWithUncaughtException
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"VALIDATE"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED2"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED3"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createTryCatchFinallyNoEnd
argument_list|()
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
DECL|method|createTryCatchFinallyEnd ()
specifier|protected
name|RouteBuilder
name|createTryCatchFinallyEnd
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|doTry
argument_list|()
operator|.
name|process
argument_list|(
name|validator
argument_list|)
operator|.
name|process
argument_list|(
name|toProcessor
argument_list|)
operator|.
name|doCatch
argument_list|(
name|ValidationException
operator|.
name|class
argument_list|)
operator|.
name|process
argument_list|(
name|orderProcessor
argument_list|)
operator|.
name|doFinally
argument_list|()
operator|.
name|process
argument_list|(
name|orderProcessor2
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|process
argument_list|(
name|orderProcessor3
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|testRouteWithTryCatchFinallyEndNoException ()
specifier|public
name|void
name|testRouteWithTryCatchFinallyEndNoException
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"VALIDATE"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"TO"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED2"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED3"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createTryCatchFinallyEnd
argument_list|()
argument_list|,
name|expected
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRouteWithTryCatchFinallyEndWithCaughtException ()
specifier|public
name|void
name|testRouteWithTryCatchFinallyEndWithCaughtException
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"VALIDATE"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED2"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED3"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createTryCatchFinallyEnd
argument_list|()
argument_list|,
name|expected
argument_list|,
literal|"banana"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRouteWithTryCatchFinallyEndWithUncaughtException ()
specifier|public
name|void
name|testRouteWithTryCatchFinallyEndWithUncaughtException
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"VALIDATE"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"INVOKED2"
argument_list|)
expr_stmt|;
name|runTest
argument_list|(
name|createTryCatchFinallyEnd
argument_list|()
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

