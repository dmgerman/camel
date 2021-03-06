begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.extension.verifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|extension
operator|.
name|verifier
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
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|CamelContextAware
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
name|Component
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
name|ComponentAware
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
name|extension
operator|.
name|ComponentVerifierExtension
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
name|runtimecatalog
operator|.
name|EndpointValidationResult
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
name|runtimecatalog
operator|.
name|RuntimeCamelCatalog
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
name|CamelContextHelper
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
name|PropertyBindingSupport
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
name|PropertiesHelper
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|StreamUtils
operator|.
name|stream
import|;
end_import

begin_class
DECL|class|DefaultComponentVerifierExtension
specifier|public
class|class
name|DefaultComponentVerifierExtension
implements|implements
name|ComponentVerifierExtension
implements|,
name|CamelContextAware
implements|,
name|ComponentAware
block|{
DECL|field|defaultScheme
specifier|private
specifier|final
name|String
name|defaultScheme
decl_stmt|;
DECL|field|component
specifier|private
name|Component
name|component
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|DefaultComponentVerifierExtension (String defaultScheme)
specifier|protected
name|DefaultComponentVerifierExtension
parameter_list|(
name|String
name|defaultScheme
parameter_list|)
block|{
name|this
argument_list|(
name|defaultScheme
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultComponentVerifierExtension (String defaultScheme, CamelContext camelContext)
specifier|protected
name|DefaultComponentVerifierExtension
parameter_list|(
name|String
name|defaultScheme
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
argument_list|(
name|defaultScheme
argument_list|,
name|camelContext
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultComponentVerifierExtension (String defaultScheme, CamelContext camelContext, Component component)
specifier|protected
name|DefaultComponentVerifierExtension
parameter_list|(
name|String
name|defaultScheme
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|this
operator|.
name|defaultScheme
operator|=
name|defaultScheme
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
block|}
comment|// *************************************
comment|//
comment|// *************************************
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|Component
name|getComponent
parameter_list|()
block|{
return|return
name|component
return|;
block|}
annotation|@
name|Override
DECL|method|setComponent (Component component)
specifier|public
name|void
name|setComponent
parameter_list|(
name|Component
name|component
parameter_list|)
block|{
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|verify (Scope scope, Map<String, Object> parameters)
specifier|public
name|Result
name|verify
parameter_list|(
name|Scope
name|scope
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
comment|// Camel context is mandatory
if|if
condition|(
name|this
operator|.
name|camelContext
operator|==
literal|null
condition|)
block|{
return|return
name|ResultBuilder
operator|.
name|withStatusAndScope
argument_list|(
name|Result
operator|.
name|Status
operator|.
name|ERROR
argument_list|,
name|scope
argument_list|)
operator|.
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withCodeAndDescription
argument_list|(
name|VerificationError
operator|.
name|StandardCode
operator|.
name|INTERNAL
argument_list|,
literal|"Missing camel-context"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
if|if
condition|(
name|scope
operator|==
name|Scope
operator|.
name|PARAMETERS
condition|)
block|{
return|return
name|verifyParameters
argument_list|(
name|parameters
argument_list|)
return|;
block|}
if|if
condition|(
name|scope
operator|==
name|Scope
operator|.
name|CONNECTIVITY
condition|)
block|{
return|return
name|verifyConnectivity
argument_list|(
name|parameters
argument_list|)
return|;
block|}
return|return
name|ResultBuilder
operator|.
name|unsupportedScope
argument_list|(
name|scope
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|verifyConnectivity (Map<String, Object> parameters)
specifier|protected
name|Result
name|verifyConnectivity
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
return|return
name|ResultBuilder
operator|.
name|withStatusAndScope
argument_list|(
name|Result
operator|.
name|Status
operator|.
name|UNSUPPORTED
argument_list|,
name|Scope
operator|.
name|CONNECTIVITY
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|verifyParameters (Map<String, Object> parameters)
specifier|protected
name|Result
name|verifyParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|ResultBuilder
name|builder
init|=
name|ResultBuilder
operator|.
name|withStatusAndScope
argument_list|(
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|Scope
operator|.
name|PARAMETERS
argument_list|)
decl_stmt|;
comment|// Validate against catalog
name|verifyParametersAgainstCatalog
argument_list|(
name|builder
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
comment|// *************************************
comment|// Helpers :: Parameters validation
comment|// *************************************
DECL|method|verifyParametersAgainstCatalog (ResultBuilder builder, Map<String, Object> parameters)
specifier|protected
name|void
name|verifyParametersAgainstCatalog
parameter_list|(
name|ResultBuilder
name|builder
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|verifyParametersAgainstCatalog
argument_list|(
name|builder
argument_list|,
name|parameters
argument_list|,
operator|new
name|CatalogVerifierCustomizer
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|verifyParametersAgainstCatalog (ResultBuilder builder, Map<String, Object> parameters, CatalogVerifierCustomizer customizer)
specifier|protected
name|void
name|verifyParametersAgainstCatalog
parameter_list|(
name|ResultBuilder
name|builder
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|CatalogVerifierCustomizer
name|customizer
parameter_list|)
block|{
name|String
name|scheme
init|=
name|defaultScheme
decl_stmt|;
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"scheme"
argument_list|)
condition|)
block|{
name|scheme
operator|=
name|parameters
operator|.
name|get
argument_list|(
literal|"scheme"
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
comment|// Grab the runtime catalog to check parameters
name|RuntimeCamelCatalog
name|catalog
init|=
name|camelContext
operator|.
name|getExtension
argument_list|(
name|RuntimeCamelCatalog
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Convert from Map<String, Object> to  Map<String, String> as required
comment|// by the Camel Catalog
name|EndpointValidationResult
name|result
init|=
name|catalog
operator|.
name|validateProperties
argument_list|(
name|scheme
argument_list|,
name|parameters
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toMap
argument_list|(
name|Map
operator|.
name|Entry
operator|::
name|getKey
argument_list|,
name|e
lambda|->
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|result
operator|.
name|isSuccess
argument_list|()
condition|)
block|{
if|if
condition|(
name|customizer
operator|.
name|isIncludeUnknown
argument_list|()
condition|)
block|{
name|stream
argument_list|(
name|result
operator|.
name|getUnknown
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|option
lambda|->
name|ResultErrorBuilder
operator|.
name|withUnknownOption
argument_list|(
name|option
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|forEach
argument_list|(
name|builder
operator|::
name|error
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|customizer
operator|.
name|isIncludeRequired
argument_list|()
condition|)
block|{
name|stream
argument_list|(
name|result
operator|.
name|getRequired
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|option
lambda|->
name|ResultErrorBuilder
operator|.
name|withMissingOption
argument_list|(
name|option
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|forEach
argument_list|(
name|builder
operator|::
name|error
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|customizer
operator|.
name|isIncludeInvalidBoolean
argument_list|()
condition|)
block|{
name|stream
argument_list|(
name|result
operator|.
name|getInvalidBoolean
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|entry
lambda|->
name|ResultErrorBuilder
operator|.
name|withIllegalOption
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|forEach
argument_list|(
name|builder
operator|::
name|error
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|customizer
operator|.
name|isIncludeInvalidInteger
argument_list|()
condition|)
block|{
name|stream
argument_list|(
name|result
operator|.
name|getInvalidInteger
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|entry
lambda|->
name|ResultErrorBuilder
operator|.
name|withIllegalOption
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|forEach
argument_list|(
name|builder
operator|::
name|error
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|customizer
operator|.
name|isIncludeInvalidNumber
argument_list|()
condition|)
block|{
name|stream
argument_list|(
name|result
operator|.
name|getInvalidNumber
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|entry
lambda|->
name|ResultErrorBuilder
operator|.
name|withIllegalOption
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|forEach
argument_list|(
name|builder
operator|::
name|error
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|customizer
operator|.
name|isIncludeInvalidEnum
argument_list|()
condition|)
block|{
name|stream
argument_list|(
name|result
operator|.
name|getInvalidEnum
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|entry
lambda|->
name|ResultErrorBuilder
operator|.
name|withIllegalOption
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|detail
argument_list|(
literal|"enum.values"
argument_list|,
name|result
operator|.
name|getEnumChoices
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|forEach
argument_list|(
name|builder
operator|::
name|error
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// *************************************
comment|// Helpers
comment|// *************************************
DECL|method|setProperties (T instance, Map<String, Object> properties)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|setProperties
parameter_list|(
name|T
name|instance
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|camelContext
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Camel context is null"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|properties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|PropertyBindingSupport
operator|.
name|build
argument_list|()
operator|.
name|bind
argument_list|(
name|camelContext
argument_list|,
name|instance
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
DECL|method|setProperties (T instance, String prefix, Map<String, Object> properties)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|setProperties
parameter_list|(
name|T
name|instance
parameter_list|,
name|String
name|prefix
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|setProperties
argument_list|(
name|instance
argument_list|,
name|PropertiesHelper
operator|.
name|extractProperties
argument_list|(
name|properties
argument_list|,
name|prefix
argument_list|,
literal|false
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getOption (Map<String, Object> parameters, String key, Class<T> type)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|getOption
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|String
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|value
init|=
name|parameters
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|CamelContextHelper
operator|.
name|convertTo
argument_list|(
name|camelContext
argument_list|,
name|type
argument_list|,
name|value
argument_list|)
argument_list|)
return|;
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
DECL|method|getOption (Map<String, Object> parameters, String key, Class<T> type, Supplier<T> defaultSupplier)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getOption
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|String
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Supplier
argument_list|<
name|T
argument_list|>
name|defaultSupplier
parameter_list|)
block|{
return|return
name|getOption
argument_list|(
name|parameters
argument_list|,
name|key
argument_list|,
name|type
argument_list|)
operator|.
name|orElseGet
argument_list|(
name|defaultSupplier
argument_list|)
return|;
block|}
DECL|method|getMandatoryOption (Map<String, Object> parameters, String key, Class<T> type)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getMandatoryOption
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|String
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|NoSuchOptionException
block|{
return|return
name|getOption
argument_list|(
name|parameters
argument_list|,
name|key
argument_list|,
name|type
argument_list|)
operator|.
name|orElseThrow
argument_list|(
parameter_list|()
lambda|->
operator|new
name|NoSuchOptionException
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

