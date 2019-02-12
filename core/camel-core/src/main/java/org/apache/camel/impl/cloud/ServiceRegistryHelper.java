begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|cloud
package|;
end_package

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
name|cloud
operator|.
name|ServiceRegistry
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

begin_class
DECL|class|ServiceRegistryHelper
specifier|public
specifier|final
class|class
name|ServiceRegistryHelper
block|{
DECL|method|ServiceRegistryHelper ()
specifier|private
name|ServiceRegistryHelper
parameter_list|()
block|{     }
DECL|method|lookupService (CamelContext context)
specifier|public
specifier|static
name|Optional
argument_list|<
name|ServiceRegistry
argument_list|>
name|lookupService
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
name|lookupService
argument_list|(
name|context
argument_list|,
name|ServiceRegistrySelectors
operator|.
name|DEFAULT_SELECTOR
argument_list|)
return|;
block|}
DECL|method|lookupService (CamelContext context, ServiceRegistry.Selector selector)
specifier|public
specifier|static
name|Optional
argument_list|<
name|ServiceRegistry
argument_list|>
name|lookupService
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ServiceRegistry
operator|.
name|Selector
name|selector
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|context
argument_list|,
literal|"Camel Context"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|selector
argument_list|,
literal|"ServiceRegistry selector"
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|ServiceRegistry
argument_list|>
name|services
init|=
name|context
operator|.
name|hasServices
argument_list|(
name|ServiceRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|services
argument_list|)
condition|)
block|{
return|return
name|selector
operator|.
name|select
argument_list|(
name|services
argument_list|)
return|;
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
DECL|method|mandatoryLookupService (CamelContext context)
specifier|public
specifier|static
name|ServiceRegistry
name|mandatoryLookupService
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
name|lookupService
argument_list|(
name|context
argument_list|)
operator|.
name|orElseThrow
argument_list|(
parameter_list|()
lambda|->
operator|new
name|IllegalStateException
argument_list|(
literal|"ServiceRegistry service not found"
argument_list|)
argument_list|)
return|;
block|}
DECL|method|mandatoryLookupService (CamelContext context, ServiceRegistry.Selector selector)
specifier|public
specifier|static
name|ServiceRegistry
name|mandatoryLookupService
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ServiceRegistry
operator|.
name|Selector
name|selector
parameter_list|)
block|{
return|return
name|lookupService
argument_list|(
name|context
argument_list|,
name|selector
argument_list|)
operator|.
name|orElseThrow
argument_list|(
parameter_list|()
lambda|->
operator|new
name|IllegalStateException
argument_list|(
literal|"ServiceRegistry service not found"
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit
