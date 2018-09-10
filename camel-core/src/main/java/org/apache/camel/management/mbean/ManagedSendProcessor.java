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
name|ManagedSendProcessorMBean
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
name|ProcessorDefinition
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
name|SendProcessor
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed SendProcessor"
argument_list|)
DECL|class|ManagedSendProcessor
specifier|public
class|class
name|ManagedSendProcessor
extends|extends
name|ManagedProcessor
implements|implements
name|ManagedSendProcessorMBean
block|{
DECL|field|processor
specifier|private
specifier|final
name|SendProcessor
name|processor
decl_stmt|;
DECL|field|destination
specifier|private
name|String
name|destination
decl_stmt|;
DECL|method|ManagedSendProcessor (CamelContext context, SendProcessor processor, ProcessorDefinition<?> definition)
specifier|public
name|ManagedSendProcessor
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|SendProcessor
name|processor
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
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
name|boolean
name|sanitize
init|=
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
decl_stmt|;
if|if
condition|(
name|sanitize
condition|)
block|{
name|destination
operator|=
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|processor
operator|.
name|getDestination
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|destination
operator|=
name|processor
operator|.
name|getDestination
argument_list|()
operator|.
name|getEndpointUri
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
name|processor
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
DECL|method|getProcessor ()
specifier|public
name|SendProcessor
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
DECL|method|getDestination ()
specifier|public
name|String
name|getDestination
parameter_list|()
block|{
return|return
name|destination
return|;
block|}
DECL|method|getMessageExchangePattern ()
specifier|public
name|String
name|getMessageExchangePattern
parameter_list|()
block|{
if|if
condition|(
name|processor
operator|.
name|getPattern
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|processor
operator|.
name|getPattern
argument_list|()
operator|.
name|name
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
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
comment|// we only have 1 endpoint
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
name|getDestination
argument_list|()
decl_stmt|;
name|Long
name|hits
init|=
name|processor
operator|.
name|getCounter
argument_list|()
decl_stmt|;
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
name|ObjectHelper
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

