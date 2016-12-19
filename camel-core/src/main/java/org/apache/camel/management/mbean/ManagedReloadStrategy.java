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
name|ManagedReloadStrategyMBean
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
name|ReloadStrategy
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
literal|"Managed ReloadStrategy"
argument_list|)
DECL|class|ManagedReloadStrategy
specifier|public
class|class
name|ManagedReloadStrategy
extends|extends
name|ManagedService
implements|implements
name|ManagedReloadStrategyMBean
block|{
DECL|field|reloadStrategy
specifier|private
specifier|final
name|ReloadStrategy
name|reloadStrategy
decl_stmt|;
DECL|method|ManagedReloadStrategy (CamelContext context, ReloadStrategy reloadStrategy)
specifier|public
name|ManagedReloadStrategy
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ReloadStrategy
name|reloadStrategy
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|reloadStrategy
argument_list|)
expr_stmt|;
name|this
operator|.
name|reloadStrategy
operator|=
name|reloadStrategy
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|forceReloadCamelContext ()
specifier|public
name|void
name|forceReloadCamelContext
parameter_list|()
block|{
name|reloadStrategy
operator|.
name|onReloadCamelContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getStrategy ()
specifier|public
name|String
name|getStrategy
parameter_list|()
block|{
return|return
name|reloadStrategy
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getReloadCounter ()
specifier|public
name|int
name|getReloadCounter
parameter_list|()
block|{
return|return
name|reloadStrategy
operator|.
name|getReloadCounter
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getFailedCounter ()
specifier|public
name|int
name|getFailedCounter
parameter_list|()
block|{
return|return
name|reloadStrategy
operator|.
name|getFailedCounter
argument_list|()
return|;
block|}
block|}
end_class

end_unit

