begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan.embedded
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
operator|.
name|embedded
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
name|component
operator|.
name|infinispan
operator|.
name|InfinispanConfiguration
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
name|component
operator|.
name|infinispan
operator|.
name|InfinispanConsumer
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
name|component
operator|.
name|infinispan
operator|.
name|InfinispanConsumerHandler
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
name|component
operator|.
name|infinispan
operator|.
name|InfinispanEventListener
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
name|component
operator|.
name|infinispan
operator|.
name|InfinispanUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|Cache
import|;
end_import

begin_class
DECL|class|InfinispanConsumerEmbeddedHandler
specifier|public
specifier|final
class|class
name|InfinispanConsumerEmbeddedHandler
implements|implements
name|InfinispanConsumerHandler
block|{
DECL|field|INSTANCE
specifier|public
specifier|static
specifier|final
name|InfinispanConsumerHandler
name|INSTANCE
init|=
operator|new
name|InfinispanConsumerEmbeddedHandler
argument_list|()
decl_stmt|;
DECL|method|InfinispanConsumerEmbeddedHandler ()
specifier|private
name|InfinispanConsumerEmbeddedHandler
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|start (InfinispanConsumer consumer)
specifier|public
name|InfinispanEventListener
name|start
parameter_list|(
name|InfinispanConsumer
name|consumer
parameter_list|)
block|{
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|embeddedCache
init|=
name|InfinispanUtil
operator|.
name|asEmbedded
argument_list|(
name|consumer
operator|.
name|getCache
argument_list|()
argument_list|)
decl_stmt|;
name|InfinispanConfiguration
name|configuration
init|=
name|consumer
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|InfinispanEventListener
name|listener
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|hasCustomListener
argument_list|()
condition|)
block|{
name|listener
operator|=
name|configuration
operator|.
name|getCustomListener
argument_list|()
expr_stmt|;
name|listener
operator|.
name|setInfinispanConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|configuration
operator|.
name|isClusteredListener
argument_list|()
condition|)
block|{
if|if
condition|(
name|configuration
operator|.
name|isSync
argument_list|()
condition|)
block|{
name|listener
operator|=
operator|new
name|InfinispanSyncClusteredEventListener
argument_list|(
name|consumer
argument_list|,
name|configuration
operator|.
name|getEventTypes
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|listener
operator|=
operator|new
name|InfinispanAsyncClusteredEventListener
argument_list|(
name|consumer
argument_list|,
name|configuration
operator|.
name|getEventTypes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|configuration
operator|.
name|isSync
argument_list|()
condition|)
block|{
name|listener
operator|=
operator|new
name|InfinispanSyncLocalEventListener
argument_list|(
name|consumer
argument_list|,
name|configuration
operator|.
name|getEventTypes
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|listener
operator|=
operator|new
name|InfinispanAsyncLocalEventListener
argument_list|(
name|consumer
argument_list|,
name|configuration
operator|.
name|getEventTypes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|embeddedCache
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
return|return
name|listener
return|;
block|}
annotation|@
name|Override
DECL|method|stop (InfinispanConsumer consumer)
specifier|public
name|void
name|stop
parameter_list|(
name|InfinispanConsumer
name|consumer
parameter_list|)
block|{
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|embeddedCache
init|=
name|InfinispanUtil
operator|.
name|asEmbedded
argument_list|(
name|consumer
operator|.
name|getCache
argument_list|()
argument_list|)
decl_stmt|;
name|embeddedCache
operator|.
name|removeListener
argument_list|(
name|consumer
operator|.
name|getListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

