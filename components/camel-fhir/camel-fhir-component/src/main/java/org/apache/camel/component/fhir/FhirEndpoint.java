begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.fhir
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|fhir
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
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|IGenericClient
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
name|component
operator|.
name|fhir
operator|.
name|api
operator|.
name|ExtraParameters
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
name|fhir
operator|.
name|api
operator|.
name|FhirCapabilities
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
name|fhir
operator|.
name|api
operator|.
name|FhirCreate
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
name|fhir
operator|.
name|api
operator|.
name|FhirDelete
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
name|fhir
operator|.
name|api
operator|.
name|FhirHistory
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
name|fhir
operator|.
name|api
operator|.
name|FhirLoadPage
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
name|fhir
operator|.
name|api
operator|.
name|FhirMeta
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
name|fhir
operator|.
name|api
operator|.
name|FhirPatch
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
name|fhir
operator|.
name|api
operator|.
name|FhirRead
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
name|fhir
operator|.
name|api
operator|.
name|FhirSearch
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
name|fhir
operator|.
name|api
operator|.
name|FhirTransaction
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
name|fhir
operator|.
name|api
operator|.
name|FhirUpdate
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
name|fhir
operator|.
name|api
operator|.
name|FhirValidate
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
name|fhir
operator|.
name|internal
operator|.
name|FhirApiCollection
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
name|fhir
operator|.
name|internal
operator|.
name|FhirApiName
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
name|fhir
operator|.
name|internal
operator|.
name|FhirConstants
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
name|fhir
operator|.
name|internal
operator|.
name|FhirPropertiesHelper
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
name|UriEndpoint
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
name|UriParam
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
name|AbstractApiEndpoint
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
name|camel
operator|.
name|util
operator|.
name|component
operator|.
name|ApiMethodPropertiesHelper
import|;
end_import

begin_comment
comment|/**  * The fhir component is used for working with the FHIR protocol (health care).  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.23.0"
argument_list|,
name|scheme
operator|=
literal|"fhir"
argument_list|,
name|title
operator|=
literal|"FHIR"
argument_list|,
name|syntax
operator|=
literal|"fhir:apiName/methodName"
argument_list|,
name|consumerClass
operator|=
name|FhirConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"hl7,api"
argument_list|)
DECL|class|FhirEndpoint
specifier|public
class|class
name|FhirEndpoint
extends|extends
name|AbstractApiEndpoint
argument_list|<
name|FhirApiName
argument_list|,
name|FhirConfiguration
argument_list|>
block|{
DECL|field|EXTRA_PARAMETERS_PROPERTY
specifier|private
specifier|static
specifier|final
name|String
name|EXTRA_PARAMETERS_PROPERTY
init|=
literal|"extraParameters"
decl_stmt|;
DECL|field|apiProxy
specifier|private
name|Object
name|apiProxy
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|FhirConfiguration
name|configuration
decl_stmt|;
DECL|method|FhirEndpoint (String uri, FhirComponent component, FhirApiName apiName, String methodName, FhirConfiguration endpointConfiguration)
specifier|public
name|FhirEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|FhirComponent
name|component
parameter_list|,
name|FhirApiName
name|apiName
parameter_list|,
name|String
name|methodName
parameter_list|,
name|FhirConfiguration
name|endpointConfiguration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|apiName
argument_list|,
name|methodName
argument_list|,
name|FhirApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getHelper
argument_list|(
name|apiName
argument_list|)
argument_list|,
name|endpointConfiguration
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|endpointConfiguration
expr_stmt|;
block|}
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
name|FhirProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
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
comment|// make sure inBody is not set for consumers
if|if
condition|(
name|inBody
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Option inBody is not supported for consumer endpoint"
argument_list|)
throw|;
block|}
specifier|final
name|FhirConsumer
name|consumer
init|=
operator|new
name|FhirConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
comment|// also set consumer.* properties
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|getPropertiesHelper ()
specifier|protected
name|ApiMethodPropertiesHelper
argument_list|<
name|FhirConfiguration
argument_list|>
name|getPropertiesHelper
parameter_list|()
block|{
return|return
name|FhirPropertiesHelper
operator|.
name|getHelper
argument_list|()
return|;
block|}
DECL|method|getThreadProfileName ()
specifier|protected
name|String
name|getThreadProfileName
parameter_list|()
block|{
return|return
name|FhirConstants
operator|.
name|THREAD_PROFILE_NAME
return|;
block|}
annotation|@
name|Override
DECL|method|afterConfigureProperties ()
specifier|protected
name|void
name|afterConfigureProperties
parameter_list|()
block|{
name|IGenericClient
name|client
init|=
name|getClient
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|apiName
condition|)
block|{
case|case
name|CAPABILITIES
case|:
name|apiProxy
operator|=
operator|new
name|FhirCapabilities
argument_list|(
name|client
argument_list|)
expr_stmt|;
break|break;
case|case
name|CREATE
case|:
name|apiProxy
operator|=
operator|new
name|FhirCreate
argument_list|(
name|client
argument_list|)
expr_stmt|;
break|break;
case|case
name|DELETE
case|:
name|apiProxy
operator|=
operator|new
name|FhirDelete
argument_list|(
name|client
argument_list|)
expr_stmt|;
break|break;
case|case
name|HISTORY
case|:
name|apiProxy
operator|=
operator|new
name|FhirHistory
argument_list|(
name|client
argument_list|)
expr_stmt|;
break|break;
case|case
name|LOAD_PAGE
case|:
name|apiProxy
operator|=
operator|new
name|FhirLoadPage
argument_list|(
name|client
argument_list|)
expr_stmt|;
break|break;
case|case
name|META
case|:
name|apiProxy
operator|=
operator|new
name|FhirMeta
argument_list|(
name|client
argument_list|)
expr_stmt|;
break|break;
case|case
name|PATCH
case|:
name|apiProxy
operator|=
operator|new
name|FhirPatch
argument_list|(
name|client
argument_list|)
expr_stmt|;
break|break;
case|case
name|READ
case|:
name|apiProxy
operator|=
operator|new
name|FhirRead
argument_list|(
name|client
argument_list|)
expr_stmt|;
break|break;
case|case
name|SEARCH
case|:
name|apiProxy
operator|=
operator|new
name|FhirSearch
argument_list|(
name|client
argument_list|)
expr_stmt|;
break|break;
case|case
name|TRANSACTION
case|:
name|apiProxy
operator|=
operator|new
name|FhirTransaction
argument_list|(
name|client
argument_list|)
expr_stmt|;
break|break;
case|case
name|UPDATE
case|:
name|apiProxy
operator|=
operator|new
name|FhirUpdate
argument_list|(
name|client
argument_list|)
expr_stmt|;
break|break;
case|case
name|VALIDATE
case|:
name|apiProxy
operator|=
operator|new
name|FhirValidate
argument_list|(
name|client
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid API name "
operator|+
name|apiName
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|interceptProperties (Map<String, Object> properties)
specifier|public
name|void
name|interceptProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|Map
argument_list|<
name|ExtraParameters
argument_list|,
name|Object
argument_list|>
name|extraProperties
init|=
name|getExtraParameters
argument_list|(
name|properties
argument_list|)
decl_stmt|;
for|for
control|(
name|ExtraParameters
name|extraParameter
range|:
name|ExtraParameters
operator|.
name|values
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|properties
operator|.
name|get
argument_list|(
name|extraParameter
operator|.
name|getParam
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|extraProperties
operator|.
name|put
argument_list|(
name|extraParameter
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|properties
operator|.
name|put
argument_list|(
name|EXTRA_PARAMETERS_PROPERTY
argument_list|,
name|extraProperties
argument_list|)
expr_stmt|;
block|}
DECL|method|getClient ()
name|IGenericClient
name|getClient
parameter_list|()
block|{
return|return
operator|(
operator|(
name|FhirComponent
operator|)
name|getComponent
argument_list|()
operator|)
operator|.
name|getClient
argument_list|(
name|configuration
argument_list|)
return|;
block|}
DECL|method|getExtraParameters (Map<String, Object> properties)
specifier|private
name|Map
argument_list|<
name|ExtraParameters
argument_list|,
name|Object
argument_list|>
name|getExtraParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|Object
name|extraParameters
init|=
name|properties
operator|.
name|get
argument_list|(
name|EXTRA_PARAMETERS_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|extraParameters
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|HashMap
argument_list|<>
argument_list|()
return|;
block|}
return|return
operator|(
name|Map
argument_list|<
name|ExtraParameters
argument_list|,
name|Object
argument_list|>
operator|)
name|extraParameters
return|;
block|}
annotation|@
name|Override
DECL|method|getApiProxy (ApiMethod method, Map<String, Object> args)
specifier|public
name|Object
name|getApiProxy
parameter_list|(
name|ApiMethod
name|method
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|args
parameter_list|)
block|{
return|return
name|apiProxy
return|;
block|}
block|}
end_class

end_unit

