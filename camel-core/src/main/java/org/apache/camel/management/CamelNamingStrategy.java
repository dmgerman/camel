begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MalformedObjectNameException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|Route
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
name|model
operator|.
name|ProcessorType
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
name|model
operator|.
name|RouteType
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
name|RouteContext
import|;
end_import

begin_class
DECL|class|CamelNamingStrategy
specifier|public
class|class
name|CamelNamingStrategy
block|{
DECL|field|VALUE_UNKNOWN
specifier|public
specifier|static
specifier|final
name|String
name|VALUE_UNKNOWN
init|=
literal|"unknown"
decl_stmt|;
DECL|field|KEY_NAME
specifier|public
specifier|static
specifier|final
name|String
name|KEY_NAME
init|=
literal|"name"
decl_stmt|;
DECL|field|KEY_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|KEY_TYPE
init|=
literal|"type"
decl_stmt|;
DECL|field|KEY_CONTEXT
specifier|public
specifier|static
specifier|final
name|String
name|KEY_CONTEXT
init|=
literal|"context"
decl_stmt|;
DECL|field|KEY_GROUP
specifier|public
specifier|static
specifier|final
name|String
name|KEY_GROUP
init|=
literal|"group"
decl_stmt|;
DECL|field|KEY_COMPONENT
specifier|public
specifier|static
specifier|final
name|String
name|KEY_COMPONENT
init|=
literal|"component"
decl_stmt|;
DECL|field|KEY_ROUTE
specifier|public
specifier|static
specifier|final
name|String
name|KEY_ROUTE
init|=
literal|"route"
decl_stmt|;
DECL|field|TYPE_CONTEXT
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_CONTEXT
init|=
literal|"context"
decl_stmt|;
DECL|field|TYPE_ENDPOINT
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_ENDPOINT
init|=
literal|"endpoint"
decl_stmt|;
DECL|field|TYPE_PROCESSOR
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_PROCESSOR
init|=
literal|"processor"
decl_stmt|;
DECL|field|TYPE_ROUTE
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_ROUTE
init|=
literal|"route"
decl_stmt|;
DECL|field|TYPE_SERVICE
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_SERVICE
init|=
literal|"service"
decl_stmt|;
DECL|field|domainName
specifier|protected
name|String
name|domainName
decl_stmt|;
DECL|field|hostName
specifier|protected
name|String
name|hostName
init|=
literal|"locahost"
decl_stmt|;
DECL|method|CamelNamingStrategy ()
specifier|public
name|CamelNamingStrategy
parameter_list|()
block|{
name|this
argument_list|(
literal|"org.apache.camel"
argument_list|)
expr_stmt|;
block|}
DECL|method|CamelNamingStrategy (String domainName)
specifier|public
name|CamelNamingStrategy
parameter_list|(
name|String
name|domainName
parameter_list|)
block|{
if|if
condition|(
name|domainName
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|domainName
operator|=
name|domainName
expr_stmt|;
block|}
try|try
block|{
name|hostName
operator|=
name|InetAddress
operator|.
name|getLocalHost
argument_list|()
operator|.
name|getHostName
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownHostException
name|ex
parameter_list|)
block|{
comment|// ignore, use the default "locahost"
block|}
block|}
comment|/**      * Implements the naming strategy for a {@link CamelContext}.      * The convention used for a {@link CamelContext} ObjectName is:      *<tt>&lt;domain&gt;:context=&lt;context-name&gt;,type=context,name=&lt;context-name&gt;</tt>      *      * @param context the camel context      * @return generated ObjectName      * @throws MalformedObjectNameException      */
DECL|method|getObjectName (CamelContext context)
specifier|public
name|ObjectName
name|getObjectName
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|MalformedObjectNameException
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getContextId
argument_list|(
name|context
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
operator|+
name|TYPE_CONTEXT
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getContextId
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
comment|/**      * Implements the naming strategy for a {@link ManagedEndpoint}.      * The convention used for a {@link ManagedEndpoint} ObjectName is:      *<tt>&lt;domain&gt;:context=&lt;context-name&gt;,type=endpoint,component=&lt;component-name&gt;name=&lt;endpoint-name&gt;</tt>      */
DECL|method|getObjectName (ManagedEndpoint mbean)
specifier|public
name|ObjectName
name|getObjectName
parameter_list|(
name|ManagedEndpoint
name|mbean
parameter_list|)
throws|throws
name|MalformedObjectNameException
block|{
name|Endpoint
argument_list|<
name|?
extends|extends
name|Exchange
argument_list|>
name|ep
init|=
name|mbean
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getContextId
argument_list|(
name|ep
operator|.
name|getCamelContext
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
operator|+
name|TYPE_ENDPOINT
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_COMPONENT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getComponentId
argument_list|(
name|ep
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getEndpointId
argument_list|(
name|ep
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
comment|/**      * Implements the naming strategy for a {@link org.apache.camel.impl.ServiceSupport Service}.      * The convention used for a {@link org.apache.camel.Service Service} ObjectName is      *<tt>&lt;domain&gt;:context=&lt;context-name&gt;,type=service,name=&lt;service-name&gt;</tt>      */
DECL|method|getObjectName (CamelContext context, ManagedService mbean)
specifier|public
name|ObjectName
name|getObjectName
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ManagedService
name|mbean
parameter_list|)
throws|throws
name|MalformedObjectNameException
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|getContextId
argument_list|(
name|context
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
operator|+
name|TYPE_SERVICE
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toHexString
argument_list|(
name|mbean
operator|.
name|getService
argument_list|()
operator|.
name|hashCode
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
comment|/**      * Implements the naming strategy for a {@link ManagedRoute}.      * The convention used for a {@link ManagedRoute} ObjectName is:      *<tt>&lt;domain&gt;:context=&lt;context-name&gt;,route=&lt;route-name&gt;,type=route,name=&lt;route-name&gt;</tt>      */
DECL|method|getObjectName (ManagedRoute mbean)
specifier|public
name|ObjectName
name|getObjectName
parameter_list|(
name|ManagedRoute
name|mbean
parameter_list|)
throws|throws
name|MalformedObjectNameException
block|{
name|Route
argument_list|<
name|?
extends|extends
name|Exchange
argument_list|>
name|route
init|=
name|mbean
operator|.
name|getRoute
argument_list|()
decl_stmt|;
name|Endpoint
argument_list|<
name|?
extends|extends
name|Exchange
argument_list|>
name|ep
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|ctxid
init|=
name|ep
operator|!=
literal|null
condition|?
name|getContextId
argument_list|(
name|ep
operator|.
name|getCamelContext
argument_list|()
argument_list|)
else|:
name|VALUE_UNKNOWN
decl_stmt|;
name|String
name|cid
init|=
name|getComponentId
argument_list|(
name|ep
argument_list|)
decl_stmt|;
name|String
name|id
init|=
name|VALUE_UNKNOWN
operator|.
name|equals
argument_list|(
name|cid
argument_list|)
condition|?
name|getEndpointId
argument_list|(
name|ep
argument_list|)
else|:
literal|"["
operator|+
name|cid
operator|+
literal|"]"
operator|+
name|getEndpointId
argument_list|(
name|ep
argument_list|)
decl_stmt|;
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|ctxid
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_ROUTE
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|id
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
operator|+
name|TYPE_ROUTE
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|id
argument_list|)
expr_stmt|;
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
comment|/**      * Implements the naming strategy for a {@link ProcessorType}.      * The convention used for a {@link ProcessorType} ObjectName is:      *<tt>&lt;domain&gt;:context=&lt;context-name&gt;,route=&lt;route-name&gt;,type=processor,name=&lt;processor-name&gt;</tt>      */
DECL|method|getObjectName (RouteContext routeContext, ProcessorType processor)
specifier|public
name|ObjectName
name|getObjectName
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|ProcessorType
name|processor
parameter_list|)
throws|throws
name|MalformedObjectNameException
block|{
name|Endpoint
argument_list|<
name|?
extends|extends
name|Exchange
argument_list|>
name|ep
init|=
name|routeContext
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|ctxid
init|=
name|ep
operator|!=
literal|null
condition|?
name|getContextId
argument_list|(
name|ep
operator|.
name|getCamelContext
argument_list|()
argument_list|)
else|:
name|VALUE_UNKNOWN
decl_stmt|;
name|String
name|cid
init|=
name|getComponentId
argument_list|(
name|ep
argument_list|)
decl_stmt|;
name|String
name|id
init|=
name|VALUE_UNKNOWN
operator|.
name|equals
argument_list|(
name|cid
argument_list|)
condition|?
name|getEndpointId
argument_list|(
name|ep
argument_list|)
else|:
literal|"["
operator|+
name|cid
operator|+
literal|"]"
operator|+
name|getEndpointId
argument_list|(
name|ep
argument_list|)
decl_stmt|;
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|domainName
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_CONTEXT
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|ctxid
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_ROUTE
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|id
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_TYPE
operator|+
literal|"="
operator|+
name|TYPE_PROCESSOR
operator|+
literal|","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|KEY_NAME
operator|+
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|ObjectName
operator|.
name|quote
argument_list|(
name|processor
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|createObjectName
argument_list|(
name|buffer
argument_list|)
return|;
block|}
DECL|method|getDomainName ()
specifier|public
name|String
name|getDomainName
parameter_list|()
block|{
return|return
name|domainName
return|;
block|}
DECL|method|setDomainName (String domainName)
specifier|public
name|void
name|setDomainName
parameter_list|(
name|String
name|domainName
parameter_list|)
block|{
name|this
operator|.
name|domainName
operator|=
name|domainName
expr_stmt|;
block|}
DECL|method|getHostName ()
specifier|public
name|String
name|getHostName
parameter_list|()
block|{
return|return
name|hostName
return|;
block|}
DECL|method|setHostName (String hostName)
specifier|public
name|void
name|setHostName
parameter_list|(
name|String
name|hostName
parameter_list|)
block|{
name|this
operator|.
name|hostName
operator|=
name|hostName
expr_stmt|;
block|}
DECL|method|getContextId (CamelContext context)
specifier|protected
name|String
name|getContextId
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|String
name|id
init|=
name|context
operator|!=
literal|null
condition|?
name|context
operator|.
name|getName
argument_list|()
else|:
name|VALUE_UNKNOWN
decl_stmt|;
return|return
name|hostName
operator|+
literal|"/"
operator|+
name|id
return|;
block|}
DECL|method|getComponentId (Endpoint<? extends Exchange> ep)
specifier|protected
name|String
name|getComponentId
parameter_list|(
name|Endpoint
argument_list|<
name|?
extends|extends
name|Exchange
argument_list|>
name|ep
parameter_list|)
block|{
name|String
name|uri
init|=
name|ep
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
name|int
name|pos
init|=
name|uri
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
return|return
operator|(
name|pos
operator|==
operator|-
literal|1
operator|)
condition|?
name|VALUE_UNKNOWN
else|:
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
return|;
block|}
DECL|method|getEndpointId (Endpoint<? extends Exchange> ep)
specifier|protected
name|String
name|getEndpointId
parameter_list|(
name|Endpoint
argument_list|<
name|?
extends|extends
name|Exchange
argument_list|>
name|ep
parameter_list|)
block|{
name|String
name|uri
init|=
name|ep
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
name|int
name|pos
init|=
name|uri
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|String
name|id
init|=
operator|(
name|pos
operator|==
operator|-
literal|1
operator|)
condition|?
name|uri
else|:
name|uri
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ep
operator|.
name|isSingleton
argument_list|()
condition|)
block|{
name|id
operator|+=
literal|"."
operator|+
name|Integer
operator|.
name|toString
argument_list|(
name|ep
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|ObjectNameEncoder
operator|.
name|encode
argument_list|(
name|id
argument_list|)
return|;
block|}
comment|/**      * Factory method to create an ObjectName escaping any required characters      */
DECL|method|createObjectName (StringBuffer buffer)
specifier|protected
name|ObjectName
name|createObjectName
parameter_list|(
name|StringBuffer
name|buffer
parameter_list|)
throws|throws
name|MalformedObjectNameException
block|{
name|String
name|text
init|=
name|buffer
operator|.
name|toString
argument_list|()
decl_stmt|;
try|try
block|{
return|return
operator|new
name|ObjectName
argument_list|(
name|text
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedObjectNameException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MalformedObjectNameException
argument_list|(
literal|"Could not create ObjectName from: "
operator|+
name|text
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

