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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|EndpointConfiguration
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
name|RuntimeCamelException
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
name|URIField
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
name|URISupport
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
comment|/**  * Some helper methods for working with {@link EndpointConfiguration} instances  *  */
end_comment

begin_class
annotation|@
name|Deprecated
DECL|class|ConfigurationHelper
specifier|public
specifier|final
class|class
name|ConfigurationHelper
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
name|ConfigurationHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|ConfigurationHelper ()
specifier|private
name|ConfigurationHelper
parameter_list|()
block|{
comment|//Utility Class
block|}
DECL|interface|ParameterSetter
specifier|public
interface|interface
name|ParameterSetter
block|{
comment|/**          * Sets the parameter on the configuration.          *          * @param camelContext  the camel context          * @param config        the configuration          * @param name          the name of the parameter          * @param value         the value to set          * @throws RuntimeCamelException is thrown if error setting the parameter          */
DECL|method|set (CamelContext camelContext, EndpointConfiguration config, String name, T value)
parameter_list|<
name|T
parameter_list|>
name|void
name|set
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|EndpointConfiguration
name|config
parameter_list|,
name|String
name|name
parameter_list|,
name|T
name|value
parameter_list|)
throws|throws
name|RuntimeCamelException
function_decl|;
block|}
DECL|method|createConfiguration (String uri, CamelContext context)
specifier|public
specifier|static
name|EndpointConfiguration
name|createConfiguration
parameter_list|(
name|String
name|uri
parameter_list|,
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|schemeSeparator
init|=
name|uri
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|schemeSeparator
operator|==
operator|-
literal|1
condition|)
block|{
comment|// not an URIConfiguration
return|return
literal|null
return|;
block|}
name|String
name|scheme
init|=
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|schemeSeparator
argument_list|)
decl_stmt|;
name|Component
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
name|scheme
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Lookup for Component handling \"{}:\" configuration returned {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|scheme
block|,
name|component
operator|!=
literal|null
condition|?
name|component
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|"<null>"
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|component
operator|!=
literal|null
condition|)
block|{
name|EndpointConfiguration
name|config
init|=
name|component
operator|.
name|createConfiguration
argument_list|(
name|scheme
argument_list|)
decl_stmt|;
if|if
condition|(
name|config
operator|instanceof
name|DefaultEndpointConfiguration
condition|)
block|{
operator|(
operator|(
name|DefaultEndpointConfiguration
operator|)
name|config
operator|)
operator|.
name|setURI
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
return|return
name|config
return|;
block|}
else|else
block|{
comment|// no component to create the configuration
return|return
literal|null
return|;
block|}
block|}
DECL|method|populateFromURI (CamelContext camelContext, EndpointConfiguration config, ParameterSetter setter)
specifier|public
specifier|static
name|void
name|populateFromURI
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|EndpointConfiguration
name|config
parameter_list|,
name|ParameterSetter
name|setter
parameter_list|)
block|{
name|URI
name|uri
init|=
name|config
operator|.
name|getURI
argument_list|()
decl_stmt|;
name|setter
operator|.
name|set
argument_list|(
name|camelContext
argument_list|,
name|config
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_SCHEME
argument_list|,
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
expr_stmt|;
name|setter
operator|.
name|set
argument_list|(
name|camelContext
argument_list|,
name|config
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_SCHEME_SPECIFIC_PART
argument_list|,
name|uri
operator|.
name|getSchemeSpecificPart
argument_list|()
argument_list|)
expr_stmt|;
name|setter
operator|.
name|set
argument_list|(
name|camelContext
argument_list|,
name|config
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_AUTHORITY
argument_list|,
name|uri
operator|.
name|getAuthority
argument_list|()
argument_list|)
expr_stmt|;
name|setter
operator|.
name|set
argument_list|(
name|camelContext
argument_list|,
name|config
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_USER_INFO
argument_list|,
name|uri
operator|.
name|getUserInfo
argument_list|()
argument_list|)
expr_stmt|;
name|setter
operator|.
name|set
argument_list|(
name|camelContext
argument_list|,
name|config
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_HOST
argument_list|,
name|uri
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|setter
operator|.
name|set
argument_list|(
name|camelContext
argument_list|,
name|config
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_PORT
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|uri
operator|.
name|getPort
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setter
operator|.
name|set
argument_list|(
name|camelContext
argument_list|,
name|config
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_PATH
argument_list|,
name|uri
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|setter
operator|.
name|set
argument_list|(
name|camelContext
argument_list|,
name|config
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_QUERY
argument_list|,
name|uri
operator|.
name|getQuery
argument_list|()
argument_list|)
expr_stmt|;
name|setter
operator|.
name|set
argument_list|(
name|camelContext
argument_list|,
name|config
argument_list|,
name|EndpointConfiguration
operator|.
name|URI_FRAGMENT
argument_list|,
name|uri
operator|.
name|getFragment
argument_list|()
argument_list|)
expr_stmt|;
comment|// now parse query and set custom parameters
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
decl_stmt|;
try|try
block|{
name|parameters
operator|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|pair
range|:
name|parameters
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|setter
operator|.
name|set
argument_list|(
name|camelContext
argument_list|,
name|config
argument_list|,
name|pair
operator|.
name|getKey
argument_list|()
argument_list|,
name|pair
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|findConfigurationField (EndpointConfiguration config, String name)
specifier|public
specifier|static
name|Field
name|findConfigurationField
parameter_list|(
name|EndpointConfiguration
name|config
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|config
operator|!=
literal|null
operator|&&
name|name
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|config
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Field
index|[]
name|fields
init|=
name|clazz
operator|.
name|getDeclaredFields
argument_list|()
decl_stmt|;
name|Field
name|found
decl_stmt|;
name|URIField
name|anno
decl_stmt|;
for|for
control|(
specifier|final
name|Field
name|field
range|:
name|fields
control|)
block|{
name|anno
operator|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|URIField
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|anno
operator|==
literal|null
condition|?
name|field
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
else|:
name|anno
operator|.
name|component
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
operator|(
name|anno
operator|.
name|component
argument_list|()
operator|.
name|equals
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_QUERY
argument_list|)
operator|&&
name|anno
operator|.
name|parameter
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|)
condition|)
block|{
name|found
operator|=
name|field
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Found field {}.{} as candidate for parameter {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|clazz
operator|.
name|getName
argument_list|()
block|,
name|found
operator|.
name|getName
argument_list|()
block|,
name|name
block|}
argument_list|)
expr_stmt|;
return|return
name|found
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getConfigurationParameter (EndpointConfiguration config, String name)
specifier|public
specifier|static
name|Object
name|getConfigurationParameter
parameter_list|(
name|EndpointConfiguration
name|config
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|Field
name|field
init|=
name|findConfigurationField
argument_list|(
name|config
argument_list|,
name|name
argument_list|)
decl_stmt|;
return|return
name|getConfigurationParameter
argument_list|(
name|config
argument_list|,
name|field
argument_list|)
return|;
block|}
DECL|method|getConfigurationParameter (EndpointConfiguration config, Field field)
specifier|public
specifier|static
name|Object
name|getConfigurationParameter
parameter_list|(
name|EndpointConfiguration
name|config
parameter_list|,
name|Field
name|field
parameter_list|)
block|{
if|if
condition|(
name|field
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|IntrospectionSupport
operator|.
name|getProperty
argument_list|(
name|config
argument_list|,
name|field
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Failed to get property '"
operator|+
name|field
operator|.
name|getName
argument_list|()
operator|+
literal|"' on "
operator|+
name|config
operator|+
literal|" due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|setConfigurationField (CamelContext camelContext, EndpointConfiguration config, String name, T value)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|setConfigurationField
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|EndpointConfiguration
name|config
parameter_list|,
name|String
name|name
parameter_list|,
name|T
name|value
parameter_list|)
block|{
name|Field
name|field
init|=
name|findConfigurationField
argument_list|(
name|config
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|field
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|config
argument_list|,
name|name
argument_list|,
name|value
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
name|RuntimeCamelException
argument_list|(
literal|"Failed to set property '"
operator|+
name|name
operator|+
literal|"' on "
operator|+
name|config
operator|+
literal|" due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|class|FieldParameterSetter
specifier|public
specifier|static
class|class
name|FieldParameterSetter
implements|implements
name|ParameterSetter
block|{
annotation|@
name|Override
DECL|method|set (CamelContext camelContext, EndpointConfiguration config, String name, T value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|set
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|EndpointConfiguration
name|config
parameter_list|,
name|String
name|name
parameter_list|,
name|T
name|value
parameter_list|)
block|{
name|setConfigurationField
argument_list|(
name|camelContext
argument_list|,
name|config
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

