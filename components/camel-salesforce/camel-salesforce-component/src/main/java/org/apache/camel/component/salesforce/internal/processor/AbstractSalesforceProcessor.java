begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|internal
operator|.
name|processor
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
name|component
operator|.
name|salesforce
operator|.
name|SalesforceComponent
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
name|salesforce
operator|.
name|SalesforceEndpoint
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
name|salesforce
operator|.
name|api
operator|.
name|SalesforceException
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
name|salesforce
operator|.
name|internal
operator|.
name|OperationName
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
name|salesforce
operator|.
name|internal
operator|.
name|SalesforceSession
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
name|salesforce
operator|.
name|SalesforceHttpClient
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

begin_class
DECL|class|AbstractSalesforceProcessor
specifier|public
specifier|abstract
class|class
name|AbstractSalesforceProcessor
implements|implements
name|SalesforceProcessor
block|{
DECL|field|NOT_OPTIONAL
specifier|protected
specifier|static
specifier|final
name|boolean
name|NOT_OPTIONAL
init|=
literal|false
decl_stmt|;
DECL|field|IS_OPTIONAL
specifier|protected
specifier|static
specifier|final
name|boolean
name|IS_OPTIONAL
init|=
literal|true
decl_stmt|;
DECL|field|USE_BODY
specifier|protected
specifier|static
specifier|final
name|boolean
name|USE_BODY
init|=
literal|true
decl_stmt|;
DECL|field|IGNORE_BODY
specifier|protected
specifier|static
specifier|final
name|boolean
name|IGNORE_BODY
init|=
literal|false
decl_stmt|;
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|protected
specifier|final
name|SalesforceEndpoint
name|endpoint
decl_stmt|;
DECL|field|endpointConfigMap
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|endpointConfigMap
decl_stmt|;
DECL|field|operationName
specifier|protected
specifier|final
name|OperationName
name|operationName
decl_stmt|;
DECL|field|session
specifier|protected
specifier|final
name|SalesforceSession
name|session
decl_stmt|;
DECL|field|httpClient
specifier|protected
specifier|final
name|SalesforceHttpClient
name|httpClient
decl_stmt|;
DECL|method|AbstractSalesforceProcessor (SalesforceEndpoint endpoint)
specifier|public
name|AbstractSalesforceProcessor
parameter_list|(
name|SalesforceEndpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|operationName
operator|=
name|endpoint
operator|.
name|getOperationName
argument_list|()
expr_stmt|;
name|this
operator|.
name|endpointConfigMap
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|toValueMap
argument_list|()
expr_stmt|;
specifier|final
name|SalesforceComponent
name|component
init|=
name|endpoint
operator|.
name|getComponent
argument_list|()
decl_stmt|;
name|this
operator|.
name|session
operator|=
name|component
operator|.
name|getSession
argument_list|()
expr_stmt|;
name|this
operator|.
name|httpClient
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getHttpClient
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
specifier|abstract
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Gets String value for a parameter from header, endpoint config, or exchange body (optional).      *      * @param exchange          exchange to inspect      * @param convertInBody     converts In body to String value if true      * @param propName          name of property      * @param optional          if {@code true} returns null, otherwise throws RestException      * @return value of property, or {@code null} for optional parameters if not found.      * @throws org.apache.camel.component.salesforce.api.SalesforceException      *          if the property can't be found or on conversion errors.      */
DECL|method|getParameter (String propName, Exchange exchange, boolean convertInBody, boolean optional)
specifier|protected
specifier|final
name|String
name|getParameter
parameter_list|(
name|String
name|propName
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|convertInBody
parameter_list|,
name|boolean
name|optional
parameter_list|)
throws|throws
name|SalesforceException
block|{
return|return
name|getParameter
argument_list|(
name|propName
argument_list|,
name|exchange
argument_list|,
name|convertInBody
argument_list|,
name|optional
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Gets value for a parameter from header, endpoint config, or exchange body (optional).      *      * @param exchange          exchange to inspect      * @param convertInBody     converts In body to parameterClass value if true      * @param propName          name of property      * @param optional          if {@code true} returns null, otherwise throws RestException      * @param parameterClass    parameter type      * @return value of property, or {@code null} for optional parameters if not found.      * @throws org.apache.camel.component.salesforce.api.SalesforceException      *          if the property can't be found or on conversion errors.      */
DECL|method|getParameter (String propName, Exchange exchange, boolean convertInBody, boolean optional, Class<T> parameterClass)
specifier|protected
specifier|final
parameter_list|<
name|T
parameter_list|>
name|T
name|getParameter
parameter_list|(
name|String
name|propName
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|convertInBody
parameter_list|,
name|boolean
name|optional
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|parameterClass
parameter_list|)
throws|throws
name|SalesforceException
block|{
specifier|final
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|T
name|propValue
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|propName
argument_list|,
name|parameterClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|propValue
operator|==
literal|null
condition|)
block|{
comment|// check if type conversion failed
if|if
condition|(
name|in
operator|.
name|getHeader
argument_list|(
name|propName
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Header "
operator|+
name|propName
operator|+
literal|" could not be converted to type "
operator|+
name|parameterClass
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|Object
name|value
init|=
name|endpointConfigMap
operator|.
name|get
argument_list|(
name|propName
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
operator|||
name|parameterClass
operator|.
name|isInstance
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|propValue
operator|=
name|parameterClass
operator|.
name|cast
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|propValue
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|parameterClass
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
name|propValue
operator|=
operator|(
name|propValue
operator|==
literal|null
operator|&&
name|convertInBody
operator|)
condition|?
name|in
operator|.
name|getBody
argument_list|(
name|parameterClass
argument_list|)
else|:
name|propValue
expr_stmt|;
comment|// error if property was not set
if|if
condition|(
name|propValue
operator|==
literal|null
operator|&&
operator|!
name|optional
condition|)
block|{
name|String
name|msg
init|=
literal|"Missing property "
operator|+
name|propName
operator|+
operator|(
name|convertInBody
condition|?
literal|", message body could not be converted to type "
operator|+
name|parameterClass
operator|.
name|getName
argument_list|()
else|:
literal|""
operator|)
decl_stmt|;
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
literal|null
argument_list|)
throw|;
block|}
return|return
name|propValue
return|;
block|}
block|}
end_class

end_unit

