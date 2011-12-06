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
name|ManagedConsumerCacheMBean
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
name|impl
operator|.
name|ConsumerCache
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
literal|"Managed ConsumerCache"
argument_list|)
DECL|class|ManagedConsumerCache
specifier|public
class|class
name|ManagedConsumerCache
extends|extends
name|ManagedService
implements|implements
name|ManagedConsumerCacheMBean
block|{
DECL|field|consumerCache
specifier|private
specifier|final
name|ConsumerCache
name|consumerCache
decl_stmt|;
DECL|method|ManagedConsumerCache (CamelContext context, ConsumerCache consumerCache)
specifier|public
name|ManagedConsumerCache
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ConsumerCache
name|consumerCache
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|consumerCache
argument_list|)
expr_stmt|;
name|this
operator|.
name|consumerCache
operator|=
name|consumerCache
expr_stmt|;
block|}
DECL|method|getConsumerCache ()
specifier|public
name|ConsumerCache
name|getConsumerCache
parameter_list|()
block|{
return|return
name|consumerCache
return|;
block|}
DECL|method|getSource ()
specifier|public
name|String
name|getSource
parameter_list|()
block|{
if|if
condition|(
name|consumerCache
operator|.
name|getSource
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|consumerCache
operator|.
name|getSource
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getSize ()
specifier|public
name|Integer
name|getSize
parameter_list|()
block|{
return|return
name|consumerCache
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
name|consumerCache
operator|.
name|getCapacity
argument_list|()
return|;
block|}
DECL|method|getHits ()
specifier|public
name|Long
name|getHits
parameter_list|()
block|{
return|return
name|consumerCache
operator|.
name|getHits
argument_list|()
return|;
block|}
DECL|method|getMisses ()
specifier|public
name|Long
name|getMisses
parameter_list|()
block|{
return|return
name|consumerCache
operator|.
name|getMisses
argument_list|()
return|;
block|}
DECL|method|resetStatistics ()
specifier|public
name|void
name|resetStatistics
parameter_list|()
block|{
name|consumerCache
operator|.
name|resetCacheStatistics
argument_list|()
expr_stmt|;
block|}
DECL|method|purge ()
specifier|public
name|void
name|purge
parameter_list|()
block|{
name|consumerCache
operator|.
name|purge
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

