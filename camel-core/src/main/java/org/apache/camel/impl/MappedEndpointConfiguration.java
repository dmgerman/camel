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
name|LinkedHashMap
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
name|TypeConverter
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
comment|/**  * Fallback implementation of {@link EndpointConfiguration} used by {@link Component}s  * that did not yet define a configuration type.  */
end_comment

begin_class
annotation|@
name|Deprecated
DECL|class|MappedEndpointConfiguration
specifier|public
specifier|final
class|class
name|MappedEndpointConfiguration
extends|extends
name|DefaultEndpointConfiguration
block|{
comment|// TODO: need 2 sets to differentiate between user keys and fixed keys
DECL|field|params
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|MappedEndpointConfiguration (CamelContext camelContext)
name|MappedEndpointConfiguration
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|MappedEndpointConfiguration (CamelContext camelContext, String uri)
name|MappedEndpointConfiguration
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|setURI
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getParameter (String name)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getParameter
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|(
name|T
operator|)
name|params
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setParameter (String name, T value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|setParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|T
name|value
parameter_list|)
block|{
name|params
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object other)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
block|{
if|if
condition|(
name|other
operator|==
literal|null
operator|||
operator|!
operator|(
name|other
operator|instanceof
name|MappedEndpointConfiguration
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// if all parameters including scheme are the same, the component and uri must be the same too
return|return
name|this
operator|==
name|other
operator|||
operator|(
name|this
operator|.
name|getClass
argument_list|()
operator|==
name|other
operator|.
name|getClass
argument_list|()
operator|&&
name|params
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|MappedEndpointConfiguration
operator|)
name|other
operator|)
operator|.
name|params
argument_list|)
operator|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|params
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|parseURI ()
specifier|protected
name|void
name|parseURI
parameter_list|()
block|{
name|ConfigurationHelper
operator|.
name|populateFromURI
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|this
argument_list|,
operator|new
name|ConfigurationHelper
operator|.
name|ParameterSetter
argument_list|()
block|{
annotation|@
name|Override
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
if|if
condition|(
name|name
operator|!=
literal|null
operator|&&
name|value
operator|!=
literal|null
condition|)
block|{
name|params
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
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toUriString (UriFormat format)
specifier|public
name|String
name|toUriString
parameter_list|(
name|UriFormat
name|format
parameter_list|)
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
name|params
operator|.
name|entrySet
argument_list|()
decl_stmt|;
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
name|String
name|scheme
init|=
literal|null
decl_stmt|;
name|String
name|schemeSpecificPart
init|=
literal|null
decl_stmt|;
name|String
name|authority
init|=
literal|null
decl_stmt|;
name|String
name|path
init|=
literal|null
decl_stmt|;
name|String
name|fragment
init|=
literal|null
decl_stmt|;
name|TypeConverter
name|converter
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
comment|// Separate URI values from query parameters
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
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_SCHEME
argument_list|)
condition|)
block|{
name|scheme
operator|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_SCHEME_SPECIFIC_PART
argument_list|)
condition|)
block|{
name|schemeSpecificPart
operator|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_AUTHORITY
argument_list|)
condition|)
block|{
name|authority
operator|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_USER_INFO
argument_list|)
condition|)
block|{
comment|// ignore, part of authority
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_HOST
argument_list|)
condition|)
block|{
comment|// ignore, part of authority
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_PORT
argument_list|)
condition|)
block|{
comment|// ignore, part of authority
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_PATH
argument_list|)
condition|)
block|{
name|path
operator|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_QUERY
argument_list|)
condition|)
block|{
comment|// ignore, but this should not be the case, may be a good idea to log...
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|EndpointConfiguration
operator|.
name|URI_FRAGMENT
argument_list|)
condition|)
block|{
name|fragment
operator|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
block|}
name|queryParams
operator|.
name|sort
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|String
name|q
init|=
literal|""
decl_stmt|;
for|for
control|(
name|String
name|entry
range|:
name|queryParams
control|)
block|{
name|q
operator|+=
name|q
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|?
literal|""
else|:
literal|"&"
expr_stmt|;
name|q
operator|+=
name|entry
expr_stmt|;
block|}
name|StringBuilder
name|u
init|=
operator|new
name|StringBuilder
argument_list|(
literal|64
argument_list|)
decl_stmt|;
if|if
condition|(
name|scheme
operator|!=
literal|null
condition|)
block|{
name|u
operator|.
name|append
argument_list|(
name|scheme
argument_list|)
expr_stmt|;
comment|// SHOULD NOT be null
name|u
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|authority
operator|!=
literal|null
condition|)
block|{
name|u
operator|.
name|append
argument_list|(
literal|"//"
argument_list|)
expr_stmt|;
name|u
operator|.
name|append
argument_list|(
name|authority
argument_list|)
expr_stmt|;
name|u
operator|.
name|append
argument_list|(
name|path
argument_list|)
expr_stmt|;
if|if
condition|(
name|q
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|u
operator|.
name|append
argument_list|(
literal|"?"
argument_list|)
expr_stmt|;
name|u
operator|.
name|append
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fragment
operator|!=
literal|null
condition|)
block|{
name|u
operator|.
name|append
argument_list|(
literal|"#"
argument_list|)
expr_stmt|;
name|u
operator|.
name|append
argument_list|(
name|fragment
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// add leading // if not provided
if|if
condition|(
operator|!
name|schemeSpecificPart
operator|.
name|startsWith
argument_list|(
literal|"//"
argument_list|)
condition|)
block|{
name|u
operator|.
name|append
argument_list|(
literal|"//"
argument_list|)
expr_stmt|;
block|}
name|u
operator|.
name|append
argument_list|(
name|schemeSpecificPart
argument_list|)
expr_stmt|;
block|}
return|return
name|u
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

