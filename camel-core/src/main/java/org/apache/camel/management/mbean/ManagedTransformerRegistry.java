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
name|Collection
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
name|ManagedTransformerRegistryMBean
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
name|EndpointRegistry
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
name|spi
operator|.
name|Transformer
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
name|TransformerRegistry
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
literal|"Managed TransformerRegistry"
argument_list|)
DECL|class|ManagedTransformerRegistry
specifier|public
class|class
name|ManagedTransformerRegistry
extends|extends
name|ManagedService
implements|implements
name|ManagedTransformerRegistryMBean
block|{
DECL|field|transformerRegistry
specifier|private
specifier|final
name|TransformerRegistry
name|transformerRegistry
decl_stmt|;
DECL|method|ManagedTransformerRegistry (CamelContext context, TransformerRegistry endpointRegistry)
specifier|public
name|ManagedTransformerRegistry
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|TransformerRegistry
name|endpointRegistry
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|endpointRegistry
argument_list|)
expr_stmt|;
name|this
operator|.
name|transformerRegistry
operator|=
name|endpointRegistry
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
block|}
DECL|method|getTransformerRegistry ()
specifier|public
name|TransformerRegistry
name|getTransformerRegistry
parameter_list|()
block|{
return|return
name|transformerRegistry
return|;
block|}
DECL|method|getSource ()
specifier|public
name|String
name|getSource
parameter_list|()
block|{
return|return
name|transformerRegistry
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getDynamicSize ()
specifier|public
name|Integer
name|getDynamicSize
parameter_list|()
block|{
return|return
name|transformerRegistry
operator|.
name|dynamicSize
argument_list|()
return|;
block|}
DECL|method|getStaticSize ()
specifier|public
name|Integer
name|getStaticSize
parameter_list|()
block|{
return|return
name|transformerRegistry
operator|.
name|staticSize
argument_list|()
return|;
block|}
DECL|method|getSize ()
specifier|public
name|Integer
name|getSize
parameter_list|()
block|{
return|return
name|transformerRegistry
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|getMaximumCacheSize ()
specifier|public
name|Integer
name|getMaximumCacheSize
parameter_list|()
block|{
return|return
name|transformerRegistry
operator|.
name|getMaximumCacheSize
argument_list|()
return|;
block|}
DECL|method|purge ()
specifier|public
name|void
name|purge
parameter_list|()
block|{
name|transformerRegistry
operator|.
name|purge
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|listTransformers ()
specifier|public
name|TabularData
name|listTransformers
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
name|listTransformersTabularType
argument_list|()
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|Transformer
argument_list|>
name|transformers
init|=
name|transformerRegistry
operator|.
name|values
argument_list|()
decl_stmt|;
for|for
control|(
name|Transformer
name|transformer
range|:
name|transformers
control|)
block|{
name|CompositeType
name|ct
init|=
name|CamelOpenMBeanTypes
operator|.
name|listTransformersCompositeType
argument_list|()
decl_stmt|;
name|String
name|transformerString
init|=
name|transformer
operator|.
name|toString
argument_list|()
decl_stmt|;
name|boolean
name|fromStatic
init|=
name|transformerRegistry
operator|.
name|isStatic
argument_list|(
name|transformerString
argument_list|)
decl_stmt|;
name|boolean
name|fromDynamic
init|=
name|transformerRegistry
operator|.
name|isDynamic
argument_list|(
name|transformerString
argument_list|)
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
literal|"string"
block|,
literal|"static"
block|,
literal|"dynamic"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|transformerString
block|,
name|fromStatic
block|,
name|fromDynamic
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

