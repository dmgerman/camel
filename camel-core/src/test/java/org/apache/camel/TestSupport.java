begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|Builder
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
name|builder
operator|.
name|ValueBuilder
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
name|processor
operator|.
name|ErrorHandlerSupport
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
name|PredicateAssertHelper
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
comment|/**  * A bunch of useful testing methods  */
end_comment

begin_class
DECL|class|TestSupport
specifier|public
specifier|abstract
class|class
name|TestSupport
extends|extends
name|TestCase
block|{
DECL|field|LS
specifier|protected
specifier|static
specifier|final
name|String
name|LS
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|TestSupport
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|log
specifier|protected
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
comment|// Builder methods for expressions used when testing
comment|// -------------------------------------------------------------------------
comment|/**      * Runs the bare test sequence only if this platform is supported      *      * @throws Throwable if any exception is thrown      */
annotation|@
name|Override
DECL|method|runBare ()
specifier|public
name|void
name|runBare
parameter_list|()
throws|throws
name|Throwable
block|{
if|if
condition|(
name|canRunOnThisPlatform
argument_list|()
condition|)
block|{
comment|//start with a clean slate
name|DefaultCamelContext
operator|.
name|setContextCounter
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|TestSupportNodeIdFactory
operator|.
name|resetCounters
argument_list|()
expr_stmt|;
name|super
operator|.
name|runBare
argument_list|()
expr_stmt|;
comment|// make sure we cleanup the platform mbean server
name|TestSupportJmxCleanup
operator|.
name|removeMBeans
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|canRunOnThisPlatform ()
specifier|protected
name|boolean
name|canRunOnThisPlatform
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Returns a value builder for the given header      */
DECL|method|header (String name)
specifier|public
specifier|static
name|ValueBuilder
name|header
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|header
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Returns a value builder for the given property      */
DECL|method|property (String name)
specifier|public
specifier|static
name|ValueBuilder
name|property
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|property
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the inbound body on an exchange      */
DECL|method|body ()
specifier|public
specifier|static
name|ValueBuilder
name|body
parameter_list|()
block|{
return|return
name|Builder
operator|.
name|body
argument_list|()
return|;
block|}
comment|/**      * Returns a predicate and value builder for the inbound message body as a      * specific type      */
DECL|method|bodyAs (Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ValueBuilder
name|bodyAs
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|bodyAs
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the outbound body on an      * exchange      */
DECL|method|outBody ()
specifier|public
specifier|static
name|ValueBuilder
name|outBody
parameter_list|()
block|{
return|return
name|Builder
operator|.
name|outBody
argument_list|()
return|;
block|}
comment|/**      * Returns a predicate and value builder for the outbound message body as a      * specific type      */
DECL|method|outBodyAs (Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ValueBuilder
name|outBodyAs
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|outBodyAs
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**      * Returns a predicate and value builder for the fault body on an      * exchange      */
DECL|method|faultBody ()
specifier|public
specifier|static
name|ValueBuilder
name|faultBody
parameter_list|()
block|{
return|return
name|Builder
operator|.
name|faultBody
argument_list|()
return|;
block|}
comment|/**      * Returns a predicate and value builder for the fault message body as a      * specific type      */
DECL|method|faultBodyAs (Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ValueBuilder
name|faultBodyAs
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|faultBodyAs
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**      * Returns a value builder for the given system property      */
DECL|method|systemProperty (String name)
specifier|public
specifier|static
name|ValueBuilder
name|systemProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|systemProperty
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Returns a value builder for the given system property      */
DECL|method|systemProperty (String name, String defaultValue)
specifier|public
specifier|static
name|ValueBuilder
name|systemProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
return|return
name|Builder
operator|.
name|systemProperty
argument_list|(
name|name
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
comment|// Assertions
comment|// -----------------------------------------------------------------------
DECL|method|assertIsInstanceOf (Class<T> expectedType, Object value)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|assertIsInstanceOf
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|expectedType
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"Expected an instance of type: "
operator|+
name|expectedType
operator|.
name|getName
argument_list|()
operator|+
literal|" but was null"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"object should be a "
operator|+
name|expectedType
operator|.
name|getName
argument_list|()
operator|+
literal|" but was: "
operator|+
name|value
operator|+
literal|" with type: "
operator|+
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|expectedType
operator|.
name|isInstance
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|expectedType
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|method|assertEndpointUri (Endpoint endpoint, String uri)
specifier|public
specifier|static
name|void
name|assertEndpointUri
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"Endpoint is null when expecting endpoint for: "
operator|+
name|uri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Endoint uri for: "
operator|+
name|endpoint
argument_list|,
name|uri
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Asserts the In message on the exchange contains the expected value      */
DECL|method|assertInMessageHeader (Exchange exchange, String name, Object expected)
specifier|public
specifier|static
name|Object
name|assertInMessageHeader
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|expected
parameter_list|)
block|{
return|return
name|assertMessageHeader
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|name
argument_list|,
name|expected
argument_list|)
return|;
block|}
comment|/**      * Asserts the Out message on the exchange contains the expected value      */
DECL|method|assertOutMessageHeader (Exchange exchange, String name, Object expected)
specifier|public
specifier|static
name|Object
name|assertOutMessageHeader
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|expected
parameter_list|)
block|{
return|return
name|assertMessageHeader
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
name|name
argument_list|,
name|expected
argument_list|)
return|;
block|}
comment|/**      * Asserts that the given exchange has an OUT message of the given body value      *      * @param exchange the exchange which should have an OUT message      * @param expected the expected value of the OUT message      * @throws InvalidPayloadException is thrown if the payload is not the expected class type      */
DECL|method|assertInMessageBodyEquals (Exchange exchange, Object expected)
specifier|public
specifier|static
name|void
name|assertInMessageBodyEquals
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|expected
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
name|assertNotNull
argument_list|(
literal|"Should have a response exchange!"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|Object
name|actual
decl_stmt|;
if|if
condition|(
name|expected
operator|==
literal|null
condition|)
block|{
name|actual
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"in body of: "
operator|+
name|exchange
argument_list|,
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|actual
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|expected
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"in body of: "
operator|+
name|exchange
argument_list|,
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received response: "
operator|+
name|exchange
operator|+
literal|" with in: "
operator|+
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Asserts that the given exchange has an OUT message of the given body value      *      * @param exchange the exchange which should have an OUT message      * @param expected the expected value of the OUT message      * @throws InvalidPayloadException is thrown if the payload is not the expected class type      */
DECL|method|assertOutMessageBodyEquals (Exchange exchange, Object expected)
specifier|public
specifier|static
name|void
name|assertOutMessageBodyEquals
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|expected
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
name|assertNotNull
argument_list|(
literal|"Should have a response exchange!"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|Object
name|actual
decl_stmt|;
if|if
condition|(
name|expected
operator|==
literal|null
condition|)
block|{
name|actual
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getMandatoryBody
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"output body of: "
operator|+
name|exchange
argument_list|,
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|actual
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|expected
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"output body of: "
operator|+
name|exchange
argument_list|,
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received response: "
operator|+
name|exchange
operator|+
literal|" with out: "
operator|+
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertMessageHeader (Message message, String name, Object expected)
specifier|public
specifier|static
name|Object
name|assertMessageHeader
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|expected
parameter_list|)
block|{
name|Object
name|value
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Header: "
operator|+
name|name
operator|+
literal|" on Message: "
operator|+
name|message
argument_list|,
name|expected
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
DECL|method|assertProperty (Exchange exchange, String name, Object expected)
specifier|public
specifier|static
name|Object
name|assertProperty
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|expected
parameter_list|)
block|{
name|Object
name|value
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Property: "
operator|+
name|name
operator|+
literal|" on Exchange: "
operator|+
name|exchange
argument_list|,
name|expected
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
comment|/**      * Asserts that the given expression when evaluated returns the given answer      */
DECL|method|assertExpression (Expression expression, Exchange exchange, Object expected)
specifier|public
specifier|static
name|Object
name|assertExpression
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|expected
parameter_list|)
block|{
name|Object
name|value
decl_stmt|;
if|if
condition|(
name|expected
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|expected
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|value
operator|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Evaluated expression: "
operator|+
name|expression
operator|+
literal|" on exchange: "
operator|+
name|exchange
operator|+
literal|" result: "
operator|+
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expression: "
operator|+
name|expression
operator|+
literal|" on Exchange: "
operator|+
name|exchange
argument_list|,
name|expected
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
comment|/**      * Asserts that the predicate returns the expected value on the exchange      */
DECL|method|assertPredicateMatches (Predicate predicate, Exchange exchange)
specifier|public
specifier|static
name|void
name|assertPredicateMatches
parameter_list|(
name|Predicate
name|predicate
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|assertPredicate
argument_list|(
name|predicate
argument_list|,
name|exchange
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Asserts that the predicate returns the expected value on the exchange      */
DECL|method|assertPredicateDoesNotMatch (Predicate predicate, Exchange exchange)
specifier|public
specifier|static
name|void
name|assertPredicateDoesNotMatch
parameter_list|(
name|Predicate
name|predicate
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|PredicateAssertHelper
operator|.
name|assertMatches
argument_list|(
name|predicate
argument_list|,
literal|"Predicate should match: "
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionError
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Caught expected assertion error: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
name|assertPredicate
argument_list|(
name|predicate
argument_list|,
name|exchange
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Asserts that the predicate returns the expected value on the exchange      */
DECL|method|assertPredicate (final Predicate predicate, Exchange exchange, boolean expected)
specifier|public
specifier|static
name|boolean
name|assertPredicate
parameter_list|(
specifier|final
name|Predicate
name|predicate
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|expected
parameter_list|)
block|{
if|if
condition|(
name|expected
condition|)
block|{
name|PredicateAssertHelper
operator|.
name|assertMatches
argument_list|(
name|predicate
argument_list|,
literal|"Predicate failed: "
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|boolean
name|value
init|=
name|predicate
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Evaluated predicate: "
operator|+
name|predicate
operator|+
literal|" on exchange: "
operator|+
name|exchange
operator|+
literal|" result: "
operator|+
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Predicate: "
operator|+
name|predicate
operator|+
literal|" on Exchange: "
operator|+
name|exchange
argument_list|,
name|expected
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
comment|/**      * Resolves an endpoint and asserts that it is found      */
DECL|method|resolveMandatoryEndpoint (CamelContext context, String uri)
specifier|public
specifier|static
name|Endpoint
name|resolveMandatoryEndpoint
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No endpoint found for URI: "
operator|+
name|uri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
comment|/**      * Resolves an endpoint and asserts that it is found      */
DECL|method|resolveMandatoryEndpoint (CamelContext context, String uri, Class<T> endpointType)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Endpoint
parameter_list|>
name|T
name|resolveMandatoryEndpoint
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|uri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|endpointType
parameter_list|)
block|{
name|T
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|,
name|endpointType
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No endpoint found for URI: "
operator|+
name|uri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
comment|/**      * Creates an exchange with the given body      */
DECL|method|createExchangeWithBody (CamelContext camelContext, Object body)
specifier|protected
name|Exchange
name|createExchangeWithBody
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"testName"
argument_list|,
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"testClass"
argument_list|,
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|assertOneElement (List<T> list)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|assertOneElement
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|list
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Size of list should be 1: "
operator|+
name|list
argument_list|,
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
comment|/**      * Asserts that a list is of the given size      */
DECL|method|assertListSize (List<T> list, int size)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|assertListSize
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|list
parameter_list|,
name|int
name|size
parameter_list|)
block|{
return|return
name|assertListSize
argument_list|(
literal|"List"
argument_list|,
name|list
argument_list|,
name|size
argument_list|)
return|;
block|}
comment|/**      * Asserts that a list is of the given size      */
DECL|method|assertListSize (String message, List<T> list, int size)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|assertListSize
parameter_list|(
name|String
name|message
parameter_list|,
name|List
argument_list|<
name|T
argument_list|>
name|list
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|message
operator|+
literal|" should be of size: "
operator|+
name|size
operator|+
literal|" but is: "
operator|+
name|list
argument_list|,
name|size
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
comment|/**      * Asserts that a list is of the given size      */
DECL|method|assertCollectionSize (Collection<T> list, int size)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Collection
argument_list|<
name|T
argument_list|>
name|assertCollectionSize
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|list
parameter_list|,
name|int
name|size
parameter_list|)
block|{
return|return
name|assertCollectionSize
argument_list|(
literal|"List"
argument_list|,
name|list
argument_list|,
name|size
argument_list|)
return|;
block|}
comment|/**      * Asserts that a list is of the given size      */
DECL|method|assertCollectionSize (String message, Collection<T> list, int size)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Collection
argument_list|<
name|T
argument_list|>
name|assertCollectionSize
parameter_list|(
name|String
name|message
parameter_list|,
name|Collection
argument_list|<
name|T
argument_list|>
name|list
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|message
operator|+
literal|" should be of size: "
operator|+
name|size
operator|+
literal|" but is: "
operator|+
name|list
argument_list|,
name|size
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
comment|/**      * A helper method to create a list of Route objects for a given route builder      */
DECL|method|getRouteList (RouteBuilder builder)
specifier|public
specifier|static
name|List
argument_list|<
name|Route
argument_list|>
name|getRouteList
parameter_list|(
name|RouteBuilder
name|builder
parameter_list|)
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Route
argument_list|>
name|answer
init|=
name|context
operator|.
name|getRoutes
argument_list|()
decl_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Asserts that the text contains the given string      *      * @param text          the text to compare      * @param containedText the text which must be contained inside the other text parameter      */
DECL|method|assertStringContains (String text, String containedText)
specifier|public
specifier|static
name|void
name|assertStringContains
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|containedText
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"Text should not be null!"
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Text: "
operator|+
name|text
operator|+
literal|" does not contain: "
operator|+
name|containedText
argument_list|,
name|text
operator|.
name|contains
argument_list|(
name|containedText
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * If a processor is wrapped with a bunch of DelegateProcessor or DelegateAsyncProcessor objects      * this call will drill through them and return the wrapped Processor.      */
DECL|method|unwrap (Processor processor)
specifier|public
specifier|static
name|Processor
name|unwrap
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
while|while
condition|(
literal|true
condition|)
block|{
if|if
condition|(
name|processor
operator|instanceof
name|DelegateProcessor
condition|)
block|{
name|processor
operator|=
operator|(
operator|(
name|DelegateProcessor
operator|)
name|processor
operator|)
operator|.
name|getProcessor
argument_list|()
expr_stmt|;
block|}
else|else
block|{
return|return
name|processor
return|;
block|}
block|}
block|}
comment|/**      * If a processor is wrapped with a bunch of DelegateProcessor or DelegateAsyncProcessor objects      * this call will drill through them and return the Channel.      *<p/>      * Returns null if no channel is found.      */
DECL|method|unwrapChannel (Processor processor)
specifier|public
specifier|static
name|Channel
name|unwrapChannel
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
while|while
condition|(
literal|true
condition|)
block|{
if|if
condition|(
name|processor
operator|instanceof
name|Channel
condition|)
block|{
return|return
operator|(
name|Channel
operator|)
name|processor
return|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|DelegateProcessor
condition|)
block|{
name|processor
operator|=
operator|(
operator|(
name|DelegateProcessor
operator|)
name|processor
operator|)
operator|.
name|getProcessor
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|ErrorHandlerSupport
condition|)
block|{
name|processor
operator|=
operator|(
operator|(
name|ErrorHandlerSupport
operator|)
name|processor
operator|)
operator|.
name|getOutput
argument_list|()
expr_stmt|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
comment|/**      * Recursively delete a directory, useful to zapping test data      *      * @param file the directory to be deleted      */
DECL|method|deleteDirectory (String file)
specifier|public
specifier|static
name|void
name|deleteDirectory
parameter_list|(
name|String
name|file
parameter_list|)
block|{
name|deleteDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Recursively delete a directory, useful to zapping test data      *      * @param file the directory to be deleted      */
DECL|method|deleteDirectory (File file)
specifier|public
specifier|static
name|void
name|deleteDirectory
parameter_list|(
name|File
name|file
parameter_list|)
block|{
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
index|[]
name|files
init|=
name|file
operator|.
name|listFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|child
range|:
name|files
control|)
block|{
name|deleteDirectory
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
comment|/**      * create the directory      *      * @param file the directory to be created      */
DECL|method|createDirectory (String file)
specifier|public
specifier|static
name|void
name|createDirectory
parameter_list|(
name|String
name|file
parameter_list|)
block|{
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|dir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
comment|/**      * To be used for folder/directory comparison that works across different platforms such      * as Window, Mac and Linux.      */
DECL|method|assertDirectoryEquals (String expected, String actual)
specifier|public
specifier|static
name|void
name|assertDirectoryEquals
parameter_list|(
name|String
name|expected
parameter_list|,
name|String
name|actual
parameter_list|)
block|{
name|assertDirectoryEquals
argument_list|(
literal|null
argument_list|,
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
comment|/**      * To be used for folder/directory comparison that works across different platforms such      * as Window, Mac and Linux.      */
DECL|method|assertDirectoryEquals (String message, String expected, String actual)
specifier|public
specifier|static
name|void
name|assertDirectoryEquals
parameter_list|(
name|String
name|message
parameter_list|,
name|String
name|expected
parameter_list|,
name|String
name|actual
parameter_list|)
block|{
comment|// must use single / as path separators
name|String
name|expectedPath
init|=
name|expected
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
name|String
name|actualPath
init|=
name|actual
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
name|message
argument_list|,
name|expectedPath
argument_list|,
name|actualPath
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
name|expectedPath
argument_list|,
name|actualPath
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * To be used to check is a file is found in the file system      */
DECL|method|assertFileExists (String filename)
specifier|public
specifier|static
name|void
name|assertFileExists
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File "
operator|+
name|filename
operator|+
literal|" should exist"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * To be used to check is a file is<b>not</b> found in the file system      */
DECL|method|assertFileNotExists (String filename)
specifier|public
specifier|static
name|void
name|assertFileNotExists
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"File "
operator|+
name|filename
operator|+
literal|" should not exist"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Is this OS the given platform.      *<p/>      * Uses<tt>os.name</tt> from the system properties to determine the OS.      *      * @param platform such as Windows      * @return<tt>true</tt> if its that platform.      */
DECL|method|isPlatform (String platform)
specifier|public
specifier|static
name|boolean
name|isPlatform
parameter_list|(
name|String
name|platform
parameter_list|)
block|{
name|String
name|osName
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
decl_stmt|;
return|return
name|osName
operator|.
name|contains
argument_list|(
name|platform
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Is this Java by the given vendor.      *<p/>      * Uses<tt>java.vendor</tt> from the system properties to determine the vendor.      *      * @param vendor such as IBM      * @return<tt>true</tt> if its that vendor.      */
DECL|method|isJavaVendor (String vendor)
specifier|public
specifier|static
name|boolean
name|isJavaVendor
parameter_list|(
name|String
name|vendor
parameter_list|)
block|{
name|String
name|javaVendor
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.vendor"
argument_list|)
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
decl_stmt|;
return|return
name|javaVendor
operator|.
name|contains
argument_list|(
name|vendor
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Is this version the given Java version.      *<p/>      * Uses<tt>java.version</tt> from the system properties to determine the version.      *      * @param version such as 1.6      * @return<tt>true</tt> if its that vendor.      */
DECL|method|isJavaVersion (String version)
specifier|public
specifier|static
name|boolean
name|isJavaVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|String
name|javaVersion
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.version"
argument_list|)
decl_stmt|;
return|return
name|javaVersion
operator|.
name|contains
argument_list|(
name|version
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

