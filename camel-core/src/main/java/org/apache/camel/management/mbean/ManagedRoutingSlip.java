begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
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
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeDataSupport
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularDataSupport
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|CamelOpenMBeanTypes
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedRoutingSlipMBean
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
name|RoutingSlipDefinition
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
name|processor
operator|.
name|RoutingSlip
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
name|EndpointUtilizationStatistics
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
name|ManagementStrategy
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

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed RoutingSlip"
argument_list|)
DECL|class|ManagedRoutingSlip
specifier|public
class|class
name|ManagedRoutingSlip
extends|extends
name|ManagedProcessor
implements|implements
name|ManagedRoutingSlipMBean
block|{
DECL|field|processor
specifier|private
specifier|final
name|RoutingSlip
name|processor
decl_stmt|;
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
DECL|field|sanitize
specifier|private
name|boolean
name|sanitize
decl_stmt|;
DECL|method|ManagedRoutingSlip (CamelContext context, RoutingSlip processor, RoutingSlipDefinition definition)
specifier|public
name|ManagedRoutingSlip
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|RoutingSlip
name|processor
parameter_list|,
name|RoutingSlipDefinition
name|definition
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init (ManagementStrategy strategy)
specifier|public
name|void
name|init
parameter_list|(
name|ManagementStrategy
name|strategy
parameter_list|)
block|{
name|super
operator|.
name|init
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
name|sanitize
operator|=
name|strategy
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getMask
argument_list|()
operator|!=
literal|null
condition|?
name|strategy
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getMask
argument_list|()
else|:
literal|false
expr_stmt|;
name|uri
operator|=
name|getDefinition
argument_list|()
operator|.
name|getExpression
argument_list|()
operator|.
name|getExpression
argument_list|()
expr_stmt|;
if|if
condition|(
name|sanitize
condition|)
block|{
name|uri
operator|=
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|super
operator|.
name|reset
argument_list|()
expr_stmt|;
if|if
condition|(
name|processor
operator|.
name|getEndpointUtilizationStatistics
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|getEndpointUtilizationStatistics
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getSupportExtendedInformation ()
specifier|public
name|Boolean
name|getSupportExtendedInformation
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getDefinition ()
specifier|public
name|RoutingSlipDefinition
name|getDefinition
parameter_list|()
block|{
return|return
operator|(
name|RoutingSlipDefinition
operator|)
name|super
operator|.
name|getDefinition
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getExpressionLanguage ()
specifier|public
name|String
name|getExpressionLanguage
parameter_list|()
block|{
return|return
name|getDefinition
argument_list|()
operator|.
name|getExpression
argument_list|()
operator|.
name|getLanguage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getExpression ()
specifier|public
name|String
name|getExpression
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
annotation|@
name|Override
DECL|method|getUriDelimiter ()
specifier|public
name|String
name|getUriDelimiter
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getUriDelimiter
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getCacheSize ()
specifier|public
name|Integer
name|getCacheSize
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getCacheSize
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isIgnoreInvalidEndpoints ()
specifier|public
name|Boolean
name|isIgnoreInvalidEndpoints
parameter_list|()
block|{
return|return
name|processor
operator|.
name|isIgnoreInvalidEndpoints
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|extendedInformation ()
specifier|public
name|TabularData
name|extendedInformation
parameter_list|()
block|{
try|try
block|{
name|TabularData
name|answer
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|CamelOpenMBeanTypes
operator|.
name|endpointsUtilizationTabularType
argument_list|()
argument_list|)
decl_stmt|;
name|EndpointUtilizationStatistics
name|stats
init|=
name|processor
operator|.
name|getEndpointUtilizationStatistics
argument_list|()
decl_stmt|;
if|if
condition|(
name|stats
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|entry
range|:
name|stats
operator|.
name|getStatistics
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|CompositeType
name|ct
init|=
name|CamelOpenMBeanTypes
operator|.
name|endpointsUtilizationCompositeType
argument_list|()
decl_stmt|;
name|String
name|url
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|sanitize
condition|)
block|{
name|url
operator|=
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
name|Long
name|hits
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|hits
operator|==
literal|null
condition|)
block|{
name|hits
operator|=
literal|0L
expr_stmt|;
block|}
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|ct
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"url"
block|,
literal|"hits"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|url
block|,
name|hits
block|}
argument_list|)
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

