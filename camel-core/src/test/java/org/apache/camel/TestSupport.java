begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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

begin_comment
comment|/**  * A bunch of useful testing methods  *   * @version $Revision$  */
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
DECL|field|log
specifier|protected
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|assertIsInstanceOf (Class<T> expectedType, Object value)
specifier|protected
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
DECL|method|assertEndpointUri (Endpoint<Exchange> endpoint, String uri)
specifier|protected
name|void
name|assertEndpointUri
parameter_list|(
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
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
specifier|protected
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
specifier|protected
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
DECL|method|assertMessageHeader (Message message, String name, Object expected)
specifier|protected
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
comment|/**      * Asserts that the given expression when evaluated returns the given answer      */
DECL|method|assertExpression (Expression expression, Exchange exchange, Object expected)
specifier|protected
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
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|log
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
specifier|protected
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
specifier|protected
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
name|predicate
operator|.
name|assertMatches
argument_list|(
literal|"Predicate should match"
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
name|log
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
DECL|method|assertPredicate (Predicate predicate, Exchange exchange, boolean expected)
specifier|protected
name|boolean
name|assertPredicate
parameter_list|(
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
name|predicate
operator|.
name|assertMatches
argument_list|(
literal|"Predicate failed"
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
name|log
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
specifier|protected
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
block|}
end_class

end_unit

