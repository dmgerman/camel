begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.braintree
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|braintree
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
name|util
operator|.
name|Collections
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|com
operator|.
name|braintreegateway
operator|.
name|BraintreeGateway
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
name|CamelExecutionException
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
name|braintree
operator|.
name|internal
operator|.
name|BraintreeApiCollection
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
name|braintree
operator|.
name|internal
operator|.
name|BraintreeApiName
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
name|braintree
operator|.
name|internal
operator|.
name|BraintreeConstants
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|IntrospectionSupport
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
name|component
operator|.
name|ApiMethod
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
name|lang3
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * Abstract base class for Braintree Integration tests generated by Camel API component maven plugin.  */
end_comment

begin_class
DECL|class|AbstractBraintreeTestSupport
specifier|public
class|class
name|AbstractBraintreeTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|TEST_OPTIONS_PROPERTIES
specifier|private
specifier|static
specifier|final
name|String
name|TEST_OPTIONS_PROPERTIES
init|=
literal|"/test-options.properties"
decl_stmt|;
DECL|field|gateway
specifier|private
name|BraintreeGateway
name|gateway
decl_stmt|;
DECL|method|AbstractBraintreeTestSupport ()
specifier|protected
name|AbstractBraintreeTestSupport
parameter_list|()
block|{
name|this
operator|.
name|gateway
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
comment|// read Braintree component configuration from TEST_OPTIONS_PROPERTIES
specifier|final
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
block|{
name|properties
operator|.
name|load
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|TEST_OPTIONS_PROPERTIES
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s could not be loaded: %s"
argument_list|,
name|TEST_OPTIONS_PROPERTIES
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|options
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|addOptionIfMissing
argument_list|(
name|options
argument_list|,
literal|"environment"
argument_list|,
literal|"CAMEL_BRAINTREE_ENVIRONMENT"
argument_list|)
expr_stmt|;
name|addOptionIfMissing
argument_list|(
name|options
argument_list|,
literal|"merchantId"
argument_list|,
literal|"CAMEL_BRAINTREE_MERCHANT_ID"
argument_list|)
expr_stmt|;
name|addOptionIfMissing
argument_list|(
name|options
argument_list|,
literal|"publicKey"
argument_list|,
literal|"CAMEL_BRAINTREE_PUBLIC_KEY"
argument_list|)
expr_stmt|;
name|addOptionIfMissing
argument_list|(
name|options
argument_list|,
literal|"privateKey"
argument_list|,
literal|"CAMEL_BRAINTREE_PRIVATE_KEY"
argument_list|)
expr_stmt|;
specifier|final
name|BraintreeConfiguration
name|configuration
init|=
operator|new
name|BraintreeConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setHttpLogLevel
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setHttpLogName
argument_list|(
literal|"org.apache.camel.component.braintree.camel-braintree"
argument_list|)
expr_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|options
argument_list|)
expr_stmt|;
comment|// add BraintreeComponent to Camel context
specifier|final
name|BraintreeComponent
name|component
init|=
operator|new
name|BraintreeComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"braintree"
argument_list|,
name|component
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|addOptionIfMissing (Map<String, Object> options, String name, String envName)
specifier|protected
name|void
name|addOptionIfMissing
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|envName
parameter_list|)
block|{
if|if
condition|(
operator|!
name|options
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|String
name|value
init|=
name|System
operator|.
name|getenv
argument_list|(
name|envName
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|options
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|isCreateCamelContextPerClass ()
specifier|public
name|boolean
name|isCreateCamelContextPerClass
parameter_list|()
block|{
comment|// only create the context once for this class
return|return
literal|false
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|requestBodyAndHeaders (String endpointUri, Object body, Map<String, Object> headers)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBodyAndHeaders
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|CamelExecutionException
block|{
return|return
operator|(
name|T
operator|)
name|template
argument_list|()
operator|.
name|requestBodyAndHeaders
argument_list|(
name|endpointUri
argument_list|,
name|body
argument_list|,
name|headers
argument_list|)
return|;
block|}
DECL|method|requestBodyAndHeaders (String endpointUri, Object body, Map<String, Object> headers, Class<T> type)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBodyAndHeaders
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|CamelExecutionException
block|{
return|return
name|template
argument_list|()
operator|.
name|requestBodyAndHeaders
argument_list|(
name|endpointUri
argument_list|,
name|body
argument_list|,
name|headers
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|requestBody (String endpoint, Object body)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBody
parameter_list|(
name|String
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|)
throws|throws
name|CamelExecutionException
block|{
return|return
operator|(
name|T
operator|)
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
name|endpoint
argument_list|,
name|body
argument_list|)
return|;
block|}
DECL|method|requestBody (String endpoint, Object body, Class<T> type)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBody
parameter_list|(
name|String
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|CamelExecutionException
block|{
return|return
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
name|endpoint
argument_list|,
name|body
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|getApiName (Class<? extends ApiMethod> apiMethod)
specifier|protected
specifier|static
name|BraintreeApiName
name|getApiName
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|ApiMethod
argument_list|>
name|apiMethod
parameter_list|)
block|{
return|return
name|BraintreeApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|apiMethod
argument_list|)
return|;
block|}
DECL|method|getApiNameAsString (Class<? extends ApiMethod> apiMethod)
specifier|protected
specifier|static
name|String
name|getApiNameAsString
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|ApiMethod
argument_list|>
name|apiMethod
parameter_list|)
block|{
return|return
name|getApiName
argument_list|(
name|apiMethod
argument_list|)
operator|.
name|getName
argument_list|()
return|;
block|}
DECL|method|getBraintreeComponent ()
specifier|protected
specifier|final
name|BraintreeComponent
name|getBraintreeComponent
parameter_list|()
block|{
return|return
operator|(
name|BraintreeComponent
operator|)
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"braintree"
argument_list|)
return|;
block|}
DECL|method|getGateway ()
specifier|protected
specifier|final
specifier|synchronized
name|BraintreeGateway
name|getGateway
parameter_list|()
block|{
if|if
condition|(
name|gateway
operator|==
literal|null
condition|)
block|{
name|gateway
operator|=
name|getBraintreeComponent
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|newBraintreeGateway
argument_list|()
expr_stmt|;
block|}
return|return
name|gateway
return|;
block|}
DECL|class|BraintreeHeaderBuilder
specifier|protected
specifier|final
class|class
name|BraintreeHeaderBuilder
block|{
DECL|field|headers
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
decl_stmt|;
DECL|method|BraintreeHeaderBuilder ()
specifier|public
name|BraintreeHeaderBuilder
parameter_list|()
block|{
name|headers
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|method|add (String key, Object value)
specifier|public
name|BraintreeHeaderBuilder
name|add
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
name|BraintreeConstants
operator|.
name|PROPERTY_PREFIX
argument_list|)
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|headers
operator|.
name|put
argument_list|(
name|BraintreeConstants
operator|.
name|PROPERTY_PREFIX
operator|+
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|build ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|build
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|headers
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

