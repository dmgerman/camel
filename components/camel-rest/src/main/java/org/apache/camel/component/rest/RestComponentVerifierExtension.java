begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rest
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
name|java
operator|.
name|util
operator|.
name|Optional
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
name|component
operator|.
name|extension
operator|.
name|verifier
operator|.
name|CatalogVerifierCustomizer
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
name|verifier
operator|.
name|DefaultComponentVerifierExtension
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
name|verifier
operator|.
name|ResultBuilder
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
name|verifier
operator|.
name|ResultErrorBuilder
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
name|spi
operator|.
name|RestConsumerFactory
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
name|RestProducerFactory
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
name|JSonSchemaHelper
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
name|ObjectHelper
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
name|function
operator|.
name|Suppliers
import|;
end_import

begin_class
DECL|class|RestComponentVerifierExtension
specifier|public
class|class
name|RestComponentVerifierExtension
extends|extends
name|DefaultComponentVerifierExtension
block|{
DECL|field|CUSTOMIZER
specifier|private
specifier|static
specifier|final
name|CatalogVerifierCustomizer
name|CUSTOMIZER
init|=
operator|new
name|CatalogVerifierCustomizer
argument_list|()
operator|.
name|excludeUnknown
argument_list|()
decl_stmt|;
DECL|method|RestComponentVerifierExtension ()
name|RestComponentVerifierExtension
parameter_list|()
block|{
name|super
argument_list|(
literal|"rest"
argument_list|)
expr_stmt|;
block|}
comment|// *********************************
comment|// Parameters validation
comment|// *********************************
annotation|@
name|Override
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
comment|// Validate using the catalog but do not report unknown options as error
comment|// as the may be used to customize the underlying component
name|super
operator|.
name|verifyParametersAgainstCatalog
argument_list|(
name|builder
argument_list|,
name|parameters
argument_list|,
name|CUSTOMIZER
argument_list|)
expr_stmt|;
name|verifyUnderlyingComponent
argument_list|(
name|Scope
operator|.
name|PARAMETERS
argument_list|,
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
comment|// *********************************
comment|// Connectivity validation
comment|// *********************************
annotation|@
name|Override
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
name|CONNECTIVITY
argument_list|)
decl_stmt|;
name|verifyUnderlyingComponent
argument_list|(
name|Scope
operator|.
name|CONNECTIVITY
argument_list|,
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
comment|// *********************************
comment|// Helpers
comment|// *********************************
DECL|method|verifyUnderlyingComponent (Scope scope, ResultBuilder builder, Map<String, Object> parameters)
specifier|protected
name|void
name|verifyUnderlyingComponent
parameter_list|(
name|Scope
name|scope
parameter_list|,
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
comment|// componentName is required for validation even at runtime camel might
comment|// be able to find a suitable component at runtime.
name|String
name|componentName
init|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"componentName"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|componentName
argument_list|)
condition|)
block|{
try|try
block|{
specifier|final
name|Component
name|component
init|=
name|getTransportComponent
argument_list|(
name|componentName
argument_list|)
decl_stmt|;
specifier|final
name|Optional
argument_list|<
name|ComponentVerifierExtension
argument_list|>
name|extension
init|=
name|component
operator|.
name|getExtension
argument_list|(
name|ComponentVerifierExtension
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|extension
operator|.
name|isPresent
argument_list|()
condition|)
block|{
specifier|final
name|ComponentVerifierExtension
name|verifier
init|=
name|extension
operator|.
name|get
argument_list|()
decl_stmt|;
specifier|final
name|RuntimeCamelCatalog
name|catalog
init|=
name|getCamelContext
argument_list|()
operator|.
name|getExtension
argument_list|(
name|RuntimeCamelCatalog
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|json
init|=
name|catalog
operator|.
name|componentJSonSchema
argument_list|(
literal|"rest"
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|restParameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|m
range|:
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"componentProperties"
argument_list|,
name|json
argument_list|,
literal|true
argument_list|)
control|)
block|{
name|String
name|name
init|=
name|m
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|Object
name|val
init|=
name|restParameters
operator|.
name|remove
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
comment|// Add rest prefix to properties belonging to the rest
comment|// component so the underlying component know we want
comment|// to validate rest-related stuffs.
name|restParameters
operator|.
name|put
argument_list|(
literal|"rest."
operator|+
name|name
argument_list|,
name|parameters
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|m
range|:
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"properties"
argument_list|,
name|json
argument_list|,
literal|true
argument_list|)
control|)
block|{
name|String
name|name
init|=
name|m
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|Object
name|val
init|=
name|restParameters
operator|.
name|remove
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
comment|// Add rest prefix to properties belonging to the rest
comment|// component so the underlying component know we want
comment|// to validate rest-related stuffs.
name|restParameters
operator|.
name|put
argument_list|(
literal|"rest."
operator|+
name|name
argument_list|,
name|parameters
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// restParameters now should contains rest-component related
comment|// properties with "rest." prefix and all the remaining can
comment|// be used to customize the underlying component (i.e. http
comment|// proxies, auth, etc)
name|Result
name|result
init|=
name|verifier
operator|.
name|verify
argument_list|(
name|scope
argument_list|,
name|restParameters
argument_list|)
decl_stmt|;
comment|// Combine errors and add an information about the component
comment|// they comes from
for|for
control|(
name|VerificationError
name|error
range|:
name|result
operator|.
name|getErrors
argument_list|()
control|)
block|{
name|builder
operator|.
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|fromError
argument_list|(
name|error
argument_list|)
operator|.
name|detail
argument_list|(
literal|"component"
argument_list|,
name|componentName
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|builder
operator|.
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withUnsupportedComponent
argument_list|(
name|componentName
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|builder
operator|.
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withException
argument_list|(
name|e
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|builder
operator|.
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withMissingOption
argument_list|(
literal|"componentName"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getTransportComponent (String componentName)
specifier|private
name|Component
name|getTransportComponent
parameter_list|(
name|String
name|componentName
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|Suppliers
operator|.
name|firstMatching
argument_list|(
name|comp
lambda|->
name|comp
operator|!=
literal|null
operator|&&
operator|(
name|comp
operator|instanceof
name|RestConsumerFactory
operator|||
name|comp
operator|instanceof
name|RestProducerFactory
operator|)
argument_list|,
parameter_list|()
lambda|->
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|componentName
argument_list|,
name|Component
operator|.
name|class
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|getCamelContext
argument_list|()
operator|.
name|getComponent
argument_list|(
name|componentName
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
return|;
block|}
block|}
end_class

end_unit

