begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|ComponentConfiguration
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
name|spi
operator|.
name|UriParams
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
name|ReflectionHelper
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
comment|/**  * A component implementation for endpoints which are annotated with UriEndpoint to describe  * their configurable parameters via annotations  *  * @deprecated use {@link DefaultComponent}  */
end_comment

begin_class
annotation|@
name|Deprecated
DECL|class|UriEndpointComponent
specifier|public
specifier|abstract
class|class
name|UriEndpointComponent
extends|extends
name|DefaultComponent
block|{
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
name|UriEndpointComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpointClass
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|Endpoint
argument_list|>
name|endpointClass
decl_stmt|;
DECL|field|parameterConfigurationMap
specifier|private
name|SortedMap
argument_list|<
name|String
argument_list|,
name|ParameterConfiguration
argument_list|>
name|parameterConfigurationMap
decl_stmt|;
DECL|method|UriEndpointComponent (Class<? extends Endpoint> endpointClass)
specifier|public
name|UriEndpointComponent
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Endpoint
argument_list|>
name|endpointClass
parameter_list|)
block|{
name|this
operator|.
name|endpointClass
operator|=
name|endpointClass
expr_stmt|;
block|}
DECL|method|UriEndpointComponent (CamelContext context, Class<? extends Endpoint> endpointClass)
specifier|public
name|UriEndpointComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Endpoint
argument_list|>
name|endpointClass
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpointClass
operator|=
name|endpointClass
expr_stmt|;
block|}
comment|/**      * To use a specific endpoint class, instead of what has been provided by the constructors.      *      * @param endpointClass the endpoint class to use      */
DECL|method|setEndpointClass (Class<? extends Endpoint> endpointClass)
specifier|public
name|void
name|setEndpointClass
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Endpoint
argument_list|>
name|endpointClass
parameter_list|)
block|{
name|this
operator|.
name|endpointClass
operator|=
name|endpointClass
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createComponentConfiguration ()
specifier|public
name|ComponentConfiguration
name|createComponentConfiguration
parameter_list|()
block|{
return|return
operator|new
name|UriComponentConfiguration
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Returns a newly created sorted map, indexed by name of all the parameter configurations      * of the given endpoint class using introspection for the various annotations like      * {@link org.apache.camel.spi.UriEndpoint}, {@link org.apache.camel.spi.UriParam}, {@link org.apache.camel.spi.UriParams}      */
DECL|method|createParameterConfigurationMap ( Class<? extends Endpoint> endpointClass)
specifier|public
specifier|static
name|SortedMap
argument_list|<
name|String
argument_list|,
name|ParameterConfiguration
argument_list|>
name|createParameterConfigurationMap
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Endpoint
argument_list|>
name|endpointClass
parameter_list|)
block|{
name|SortedMap
argument_list|<
name|String
argument_list|,
name|ParameterConfiguration
argument_list|>
name|answer
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|ParameterConfiguration
argument_list|>
argument_list|()
decl_stmt|;
name|populateParameterConfigurationMap
argument_list|(
name|answer
argument_list|,
name|endpointClass
argument_list|,
literal|""
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|populateParameterConfigurationMap ( final SortedMap<String, ParameterConfiguration> parameterMap, Class<?> aClass, final String prefix)
specifier|protected
specifier|static
name|void
name|populateParameterConfigurationMap
parameter_list|(
specifier|final
name|SortedMap
argument_list|<
name|String
argument_list|,
name|ParameterConfiguration
argument_list|>
name|parameterMap
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
parameter_list|,
specifier|final
name|String
name|prefix
parameter_list|)
block|{
name|ReflectionHelper
operator|.
name|doWithFields
argument_list|(
name|aClass
argument_list|,
operator|new
name|ReflectionHelper
operator|.
name|FieldCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|doWith
parameter_list|(
name|Field
name|field
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|IllegalAccessException
block|{
name|UriParam
name|uriParam
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|UriParam
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|uriParam
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|uriParam
operator|.
name|name
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|name
operator|=
name|field
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|String
name|propertyName
init|=
name|prefix
operator|+
name|name
decl_stmt|;
comment|// is the parameter a nested configuration object
name|Class
argument_list|<
name|?
argument_list|>
name|fieldType
init|=
name|field
operator|.
name|getType
argument_list|()
decl_stmt|;
name|UriParams
name|uriParams
init|=
name|fieldType
operator|.
name|getAnnotation
argument_list|(
name|UriParams
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|uriParams
operator|!=
literal|null
condition|)
block|{
name|String
name|nestedPrefix
init|=
name|uriParams
operator|.
name|prefix
argument_list|()
decl_stmt|;
if|if
condition|(
name|nestedPrefix
operator|==
literal|null
condition|)
block|{
name|nestedPrefix
operator|=
literal|""
expr_stmt|;
block|}
name|nestedPrefix
operator|=
operator|(
name|prefix
operator|+
name|nestedPrefix
operator|)
operator|.
name|trim
argument_list|()
expr_stmt|;
name|populateParameterConfigurationMap
argument_list|(
name|parameterMap
argument_list|,
name|fieldType
argument_list|,
name|nestedPrefix
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|parameterMap
operator|.
name|containsKey
argument_list|(
name|propertyName
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Duplicate property name {} defined on field {}"
argument_list|,
name|propertyName
argument_list|,
name|field
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|parameterMap
operator|.
name|put
argument_list|(
name|propertyName
argument_list|,
name|ParameterConfiguration
operator|.
name|newInstance
argument_list|(
name|propertyName
argument_list|,
name|field
argument_list|,
name|uriParam
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|getEndpointClass ()
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Endpoint
argument_list|>
name|getEndpointClass
parameter_list|()
block|{
return|return
name|endpointClass
return|;
block|}
comment|/**      * Returns the sorted map of all the URI query parameter names to their {@link ParameterConfiguration} objects      */
DECL|method|getParameterConfigurationMap ()
specifier|public
name|SortedMap
argument_list|<
name|String
argument_list|,
name|ParameterConfiguration
argument_list|>
name|getParameterConfigurationMap
parameter_list|()
block|{
if|if
condition|(
name|parameterConfigurationMap
operator|==
literal|null
condition|)
block|{
name|parameterConfigurationMap
operator|=
name|createParameterConfigurationMap
argument_list|(
name|getEndpointClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|ParameterConfiguration
argument_list|>
argument_list|(
name|parameterConfigurationMap
argument_list|)
return|;
block|}
block|}
end_class

end_unit

