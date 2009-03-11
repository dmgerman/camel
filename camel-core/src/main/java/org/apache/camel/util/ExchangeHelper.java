begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|InvalidPayloadException
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
name|InvalidTypeException
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
name|NoSuchBeanException
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
name|NoSuchEndpointException
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
name|NoSuchHeaderException
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
name|NoSuchPropertyException
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
name|NoTypeConversionAvailableException
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
name|TypeConverter
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

begin_comment
comment|/**  * Some helper methods for working with {@link Exchange} objects  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ExchangeHelper
specifier|public
specifier|final
class|class
name|ExchangeHelper
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ExchangeHelper
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|ExchangeHelper ()
specifier|private
name|ExchangeHelper
parameter_list|()
block|{     }
comment|/**      * Extracts the exchange property of the given name and type; if it is not present then the      * default value will be used      *      * @param exchange the message exchange      * @param propertyName the name of the property on the exchange      * @param type the expected type of the property      * @param defaultValue the default value to be used if the property name does not exist or could not be      * converted to the given type      * @return the property value as the given type or the defaultValue if it could not be found or converted      */
DECL|method|getExchangeProperty (Exchange exchange, String propertyName, Class<T> type, T defaultValue)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getExchangeProperty
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|T
name|defaultValue
parameter_list|)
block|{
name|T
name|answer
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|propertyName
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Attempts to resolve the endpoint for the given value      *      * @param exchange the message exchange being processed      * @param value the value which can be an {@link Endpoint} or an object      *                which provides a String representation of an endpoint via      *                {@link #toString()}      *      * @return the endpoint      * @throws NoSuchEndpointException if the endpoint cannot be resolved      */
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
DECL|method|resolveEndpoint (Exchange exchange, Object value)
specifier|public
specifier|static
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|NoSuchEndpointException
block|{
name|Endpoint
name|endpoint
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Endpoint
condition|)
block|{
name|endpoint
operator|=
operator|(
name|Endpoint
operator|)
name|value
expr_stmt|;
block|}
else|else
block|{
name|String
name|uri
init|=
name|value
operator|.
name|toString
argument_list|()
decl_stmt|;
name|endpoint
operator|=
name|CamelContextHelper
operator|.
name|getMandatoryEndpoint
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
DECL|method|getMandatoryProperty (Exchange exchange, String propertyName, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getMandatoryProperty
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|NoSuchPropertyException
block|{
try|try
block|{
name|T
name|result
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|propertyName
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
return|return
name|result
return|;
block|}
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|ex
parameter_list|)
block|{
comment|// will throw NoSuchPropertyException below
block|}
throw|throw
operator|new
name|NoSuchPropertyException
argument_list|(
name|exchange
argument_list|,
name|propertyName
argument_list|,
name|type
argument_list|)
throw|;
block|}
DECL|method|getMandatoryHeader (Exchange exchange, String propertyName, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getMandatoryHeader
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|NoSuchHeaderException
block|{
name|T
name|answer
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|propertyName
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchHeaderException
argument_list|(
name|exchange
argument_list|,
name|propertyName
argument_list|,
name|type
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Returns the mandatory inbound message body of the correct type or throws      * an exception if it is not present      */
DECL|method|getMandatoryInBody (Exchange exchange)
specifier|public
specifier|static
name|Object
name|getMandatoryInBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
name|Object
name|answer
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
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvalidPayloadException
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Returns the mandatory inbound message body of the correct type or throws      * an exception if it is not present      */
DECL|method|getMandatoryInBody (Exchange exchange, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getMandatoryInBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
name|T
name|answer
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvalidPayloadException
argument_list|(
name|exchange
argument_list|,
name|type
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Returns the mandatory outbound message body of the correct type or throws      * an exception if it is not present      */
DECL|method|getMandatoryOutBody (Exchange exchange)
specifier|public
specifier|static
name|Object
name|getMandatoryOutBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|Object
name|answer
init|=
name|out
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvalidPayloadException
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|,
name|out
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Returns the mandatory outbound message body of the correct type or throws      * an exception if it is not present      */
DECL|method|getMandatoryOutBody (Exchange exchange, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getMandatoryOutBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|T
name|answer
init|=
name|out
operator|.
name|getBody
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvalidPayloadException
argument_list|(
name|exchange
argument_list|,
name|type
argument_list|,
name|out
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Converts the value to the given expected type or throws an exception      */
DECL|method|convertToMandatoryType (Exchange exchange, Class<T> type, Object value)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|convertToMandatoryType
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|InvalidTypeException
block|{
name|T
name|answer
init|=
name|convertToType
argument_list|(
name|exchange
argument_list|,
name|type
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvalidTypeException
argument_list|(
name|exchange
argument_list|,
name|value
argument_list|,
name|type
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Converts the value to the given expected type returning null if it could      * not be converted      */
DECL|method|convertToType (Exchange exchange, Class<T> type, Object value)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|convertToType
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|CamelContext
name|camelContext
init|=
name|exchange
operator|.
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|camelContext
operator|!=
literal|null
condition|)
block|{
name|TypeConverter
name|converter
init|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
if|if
condition|(
name|converter
operator|!=
literal|null
condition|)
block|{
return|return
name|converter
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
name|LOG
operator|.
name|warn
argument_list|(
literal|"No CamelContext and type converter available to convert types for exchange "
operator|+
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Copies the results of a message exchange from the source exchange to the result exchange      * which will copy the out and fault message contents and the exception      *      * @param result the result exchange which will have the output and error state added      * @param source the source exchange which is not modified      */
DECL|method|copyResults (Exchange result, Exchange source)
specifier|public
specifier|static
name|void
name|copyResults
parameter_list|(
name|Exchange
name|result
parameter_list|,
name|Exchange
name|source
parameter_list|)
block|{
comment|// --------------------------------------------------------------------
comment|//  TODO: merge logic with that of copyResultsPreservePattern()
comment|// --------------------------------------------------------------------
if|if
condition|(
name|result
operator|!=
name|source
condition|)
block|{
name|result
operator|.
name|setException
argument_list|(
name|source
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|fault
init|=
name|source
operator|.
name|getFault
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|fault
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|getFault
argument_list|(
literal|true
argument_list|)
operator|.
name|copyFrom
argument_list|(
name|fault
argument_list|)
expr_stmt|;
block|}
name|Message
name|out
init|=
name|source
operator|.
name|getOut
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|getOut
argument_list|(
literal|true
argument_list|)
operator|.
name|copyFrom
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|result
operator|.
name|getPattern
argument_list|()
operator|==
name|ExchangePattern
operator|.
name|InOptionalOut
condition|)
block|{
comment|// special case where the result is InOptionalOut and with no OUT response
comment|// so we should return null to indicate this fact
name|result
operator|.
name|setOut
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// no results so lets copy the last input
comment|// as the final processor on a pipeline might not
comment|// have created any OUT; such as a mock:endpoint
comment|// so lets assume the last IN is the OUT
if|if
condition|(
name|result
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
comment|// only set OUT if its OUT capable
name|result
operator|.
name|getOut
argument_list|(
literal|true
argument_list|)
operator|.
name|copyFrom
argument_list|(
name|source
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if not replace IN instead to keep the MEP
name|result
operator|.
name|getIn
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|source
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|result
operator|.
name|getProperties
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|result
operator|.
name|getProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|source
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Copies the<code>source</code> exchange to<code>target</code> exchange      * preserving the {@link ExchangePattern} of<code>target</code>.        *       * @param source source exchange.      * @param result target exchange.      */
DECL|method|copyResultsPreservePattern (Exchange result, Exchange source)
specifier|public
specifier|static
name|void
name|copyResultsPreservePattern
parameter_list|(
name|Exchange
name|result
parameter_list|,
name|Exchange
name|source
parameter_list|)
block|{
comment|// --------------------------------------------------------------------
comment|//  TODO: merge logic with that of copyResults()
comment|// --------------------------------------------------------------------
if|if
condition|(
name|source
operator|==
name|result
condition|)
block|{
comment|// no need to copy
return|return;
block|}
comment|// copy in message
name|Message
name|m
init|=
name|source
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|result
operator|.
name|getIn
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|m
argument_list|)
expr_stmt|;
comment|// copy out message
name|m
operator|=
name|source
operator|.
name|getOut
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
comment|// exchange pattern sensitive
name|getResultMessage
argument_list|(
name|result
argument_list|)
operator|.
name|copyFrom
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
comment|// copy fault message
name|m
operator|=
name|source
operator|.
name|getFault
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|getFault
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
comment|// copy exception
name|result
operator|.
name|setException
argument_list|(
name|source
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
comment|// copy properties
name|result
operator|.
name|getProperties
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|result
operator|.
name|getProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|source
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the message where to write results in an      * exchange-pattern-sensitive way.      *       * @param exchange      *            message exchange.      * @return result message.      */
DECL|method|getResultMessage (Exchange exchange)
specifier|public
specifier|static
name|Message
name|getResultMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
return|return
name|exchange
operator|.
name|getOut
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
return|;
block|}
block|}
comment|/**      * Returns true if the given exchange pattern (if defined) can support IN messagea      *      * @param exchange the exchange to interrogate      * @return true if the exchange is defined as an {@link ExchangePattern} which supports      * IN messages      */
DECL|method|isInCapable (Exchange exchange)
specifier|public
specifier|static
name|boolean
name|isInCapable
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|ExchangePattern
name|pattern
init|=
name|exchange
operator|.
name|getPattern
argument_list|()
decl_stmt|;
return|return
name|pattern
operator|!=
literal|null
operator|&&
name|pattern
operator|.
name|isInCapable
argument_list|()
return|;
block|}
comment|/**      * Returns true if the given exchange pattern (if defined) can support OUT messagea      *      * @param exchange the exchange to interrogate      * @return true if the exchange is defined as an {@link ExchangePattern} which supports      * OUT messages      */
DECL|method|isOutCapable (Exchange exchange)
specifier|public
specifier|static
name|boolean
name|isOutCapable
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|ExchangePattern
name|pattern
init|=
name|exchange
operator|.
name|getPattern
argument_list|()
decl_stmt|;
return|return
name|pattern
operator|!=
literal|null
operator|&&
name|pattern
operator|.
name|isOutCapable
argument_list|()
return|;
block|}
comment|/**      * Creates a new instance of the given type from the injector      */
DECL|method|newInstance (Exchange exchange, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|newInstance
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**      * Creates a Map of the variables which are made available to a script or template      *      * @param exchange the exchange to make available      * @return a Map populated with the require dvariables      */
DECL|method|createVariableMap (Exchange exchange)
specifier|public
specifier|static
name|Map
name|createVariableMap
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Map
name|answer
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|populateVariableMap
argument_list|(
name|exchange
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Populates the Map with the variables which are made available to a script or template      *      * @param exchange the exchange to make available      * @param map      the map to populate      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|populateVariableMap (Exchange exchange, Map map)
specifier|public
specifier|static
name|void
name|populateVariableMap
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Map
name|map
parameter_list|)
block|{
name|map
operator|.
name|put
argument_list|(
literal|"exchange"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"in"
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"request"
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"headers"
argument_list|,
name|in
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"body"
argument_list|,
name|in
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"out"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"response"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
name|map
operator|.
name|put
argument_list|(
literal|"camelContext"
argument_list|,
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the MIME content type on the input message or null if one is not defined      */
DECL|method|getContentType (Exchange exchange)
specifier|public
specifier|static
name|String
name|getContentType
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Content-Type"
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Performs a lookup in the registry of the mandatory bean name and throws an exception if it could not be found      */
DECL|method|lookupMandatoryBean (Exchange exchange, String name)
specifier|public
specifier|static
name|Object
name|lookupMandatoryBean
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|Object
name|value
init|=
name|lookupBean
argument_list|(
name|exchange
argument_list|,
name|name
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
name|NoSuchBeanException
argument_list|(
name|name
argument_list|)
throw|;
block|}
return|return
name|value
return|;
block|}
comment|/**      * Performs a lookup in the registry of the mandatory bean name and throws an exception if it could not be found      */
DECL|method|lookupMandatoryBean (Exchange exchange, String name, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|lookupMandatoryBean
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|T
name|value
init|=
name|lookupBean
argument_list|(
name|exchange
argument_list|,
name|name
argument_list|,
name|type
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
name|NoSuchBeanException
argument_list|(
name|name
argument_list|)
throw|;
block|}
return|return
name|value
return|;
block|}
comment|/**      * Performs a lookup in the registry of the bean name      */
DECL|method|lookupBean (Exchange exchange, String name)
specifier|public
specifier|static
name|Object
name|lookupBean
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Performs a lookup in the registry of the bean name and type      */
DECL|method|lookupBean (Exchange exchange, String name, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|lookupBean
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      * Returns the first exchange in the given collection of exchanges which has the same exchange ID as the one given      * or null if none could be found      */
DECL|method|getExchangeById (Iterable<Exchange> exchanges, String exchangeId)
specifier|public
specifier|static
name|Exchange
name|getExchangeById
parameter_list|(
name|Iterable
argument_list|<
name|Exchange
argument_list|>
name|exchanges
parameter_list|,
name|String
name|exchangeId
parameter_list|)
block|{
for|for
control|(
name|Exchange
name|exchange
range|:
name|exchanges
control|)
block|{
name|String
name|id
init|=
name|exchange
operator|.
name|getExchangeId
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
operator|&&
name|id
operator|.
name|equals
argument_list|(
name|exchangeId
argument_list|)
condition|)
block|{
return|return
name|exchange
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|isFailureHandled (Exchange exchange)
specifier|public
specifier|static
name|boolean
name|isFailureHandled
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Boolean
name|handled
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_HANDLED
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|handled
operator|!=
literal|null
operator|&&
name|handled
return|;
block|}
DECL|method|setFailureHandled (Exchange exchange)
specifier|public
specifier|static
name|void
name|setFailureHandled
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_HANDLED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
comment|// clear exception since its failure handled
name|exchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

