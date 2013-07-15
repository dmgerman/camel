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
name|ArrayList
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
name|List
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
name|Set
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
name|EndpointCompleter
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
name|URISupport
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
name|UnsafeUriCharactersEncoder
import|;
end_import

begin_comment
comment|/**  * Useful base class for implementations of {@link ComponentConfiguration}  */
end_comment

begin_class
DECL|class|ComponentConfigurationSupport
specifier|public
specifier|abstract
class|class
name|ComponentConfigurationSupport
implements|implements
name|ComponentConfiguration
block|{
DECL|field|component
specifier|protected
specifier|final
name|Component
name|component
decl_stmt|;
DECL|field|propertyValues
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|propertyValues
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|baseUri
specifier|private
name|String
name|baseUri
decl_stmt|;
DECL|method|ComponentConfigurationSupport (Component component)
specifier|public
name|ComponentConfigurationSupport
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
DECL|method|getParameters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|propertyValues
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setParameters (Map<String, Object> newValues)
specifier|public
name|void
name|setParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|newValues
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|newValues
argument_list|,
literal|"propertyValues"
argument_list|)
expr_stmt|;
name|this
operator|.
name|propertyValues
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// lets validate each property as we set it
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entries
init|=
name|newValues
operator|.
name|entrySet
argument_list|()
decl_stmt|;
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
name|entry
range|:
name|entries
control|)
block|{
name|setParameter
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
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getParameter (String name)
specifier|public
name|Object
name|getParameter
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|validatePropertyName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|propertyValues
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setParameter (String name, Object value)
specifier|public
name|void
name|setParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|Object
name|convertedValue
init|=
name|validatePropertyValue
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|propertyValues
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|convertedValue
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the base URI without any scheme or URI query parameters (property values)      */
annotation|@
name|Override
DECL|method|getBaseUri ()
specifier|public
name|String
name|getBaseUri
parameter_list|()
block|{
return|return
name|baseUri
return|;
block|}
annotation|@
name|Override
DECL|method|setBaseUri (String baseUri)
specifier|public
name|void
name|setBaseUri
parameter_list|(
name|String
name|baseUri
parameter_list|)
block|{
name|this
operator|.
name|baseUri
operator|=
name|baseUri
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint ()
specifier|public
name|Endpoint
name|createEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|uri
init|=
name|getUriString
argument_list|()
decl_stmt|;
return|return
name|component
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|)
return|;
block|}
comment|/**      * Configures the properties on the given endpoint      */
annotation|@
name|Override
DECL|method|configureEndpoint (Endpoint endpoint)
specifier|public
name|void
name|configureEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|getParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entries
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
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
name|entry
range|:
name|entries
control|)
block|{
name|setEndpointParameter
argument_list|(
name|endpoint
argument_list|,
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
expr_stmt|;
block|}
block|}
comment|// TODO validate all the values are valid (e.g. mandatory)
block|}
annotation|@
name|Override
DECL|method|getUriString ()
specifier|public
name|String
name|getUriString
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|queryParams
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
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
name|entry
range|:
name|getParameters
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
comment|// convert to "param=value" format here, order will be preserved
if|if
condition|(
name|value
operator|instanceof
name|List
condition|)
block|{
for|for
control|(
name|Object
name|item
range|:
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|value
control|)
block|{
name|queryParams
operator|.
name|add
argument_list|(
name|key
operator|+
literal|"="
operator|+
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|item
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|queryParams
operator|.
name|add
argument_list|(
name|key
operator|+
literal|"="
operator|+
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|queryParams
argument_list|)
expr_stmt|;
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
name|base
init|=
name|getBaseUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|base
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|base
argument_list|)
expr_stmt|;
block|}
name|String
name|separator
init|=
literal|"?"
decl_stmt|;
for|for
control|(
name|String
name|entry
range|:
name|queryParams
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|separator
operator|=
literal|"&"
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setUriString (String uri)
specifier|public
name|void
name|setUriString
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|String
name|path
init|=
name|uri
decl_stmt|;
name|int
name|idx
init|=
name|path
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|newParameters
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|idx
operator|>=
literal|0
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|String
name|query
init|=
name|uri
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
decl_stmt|;
name|newParameters
operator|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
name|query
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|setBaseUri
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|setParameters
argument_list|(
name|newParameters
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getParameterConfiguration (String name)
specifier|public
name|ParameterConfiguration
name|getParameterConfiguration
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|getParameterConfigurationMap
argument_list|()
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|completeEndpointPath (String completionText)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|completeEndpointPath
parameter_list|(
name|String
name|completionText
parameter_list|)
block|{
if|if
condition|(
name|component
operator|instanceof
name|EndpointCompleter
condition|)
block|{
name|EndpointCompleter
name|completer
init|=
operator|(
name|EndpointCompleter
operator|)
name|component
decl_stmt|;
return|return
name|completer
operator|.
name|completeEndpointPath
argument_list|(
name|this
argument_list|,
name|completionText
argument_list|)
return|;
block|}
return|return
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
return|;
block|}
DECL|method|createParameterJsonSchema ()
specifier|public
name|String
name|createParameterJsonSchema
parameter_list|()
block|{
name|SortedMap
argument_list|<
name|String
argument_list|,
name|ParameterConfiguration
argument_list|>
name|map
init|=
name|getParameterConfigurationMap
argument_list|()
decl_stmt|;
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"{\n  \"properties\": {"
argument_list|)
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|ParameterConfiguration
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toJson
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"\n  }\n}\n"
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Allow implementations to validate whether a property name is valid      * and either throw an exception or log a warning of an unknown property being used      */
DECL|method|validatePropertyName (String name)
specifier|protected
name|void
name|validatePropertyName
parameter_list|(
name|String
name|name
parameter_list|)
block|{     }
comment|/**      * Allow implementations to validate whether a property name is valid      * and either throw an exception or log a warning of an unknown property being used      * and to convert the given value to the correct type before updating the value.      */
DECL|method|validatePropertyValue (String name, Object value)
specifier|protected
name|Object
name|validatePropertyValue
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|validatePropertyName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
block|}
end_class

end_unit

