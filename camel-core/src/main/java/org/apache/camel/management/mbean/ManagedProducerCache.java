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
name|impl
operator|.
name|ProducerCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedAttribute
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedOperation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedResource
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
literal|"Managed ProducerCache"
argument_list|)
DECL|class|ManagedProducerCache
specifier|public
class|class
name|ManagedProducerCache
extends|extends
name|ManagedService
block|{
DECL|field|producerCache
specifier|private
specifier|final
name|ProducerCache
name|producerCache
decl_stmt|;
DECL|method|ManagedProducerCache (CamelContext context, ProducerCache producerCache)
specifier|public
name|ManagedProducerCache
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ProducerCache
name|producerCache
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|producerCache
argument_list|)
expr_stmt|;
name|this
operator|.
name|producerCache
operator|=
name|producerCache
expr_stmt|;
block|}
DECL|method|getProducerCache ()
specifier|public
name|ProducerCache
name|getProducerCache
parameter_list|()
block|{
return|return
name|producerCache
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Source"
argument_list|)
DECL|method|getSource ()
specifier|public
name|String
name|getSource
parameter_list|()
block|{
if|if
condition|(
name|producerCache
operator|.
name|getSource
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|producerCache
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
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of elements cached"
argument_list|)
DECL|method|getSize ()
specifier|public
name|Integer
name|getSize
parameter_list|()
block|{
return|return
name|producerCache
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Maximum cache size (capacity)"
argument_list|)
DECL|method|getMaximumCacheSize ()
specifier|public
name|Integer
name|getMaximumCacheSize
parameter_list|()
block|{
return|return
name|producerCache
operator|.
name|getCapacity
argument_list|()
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Purges the cache"
argument_list|)
DECL|method|purge ()
specifier|public
name|void
name|purge
parameter_list|()
block|{
name|producerCache
operator|.
name|purge
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

