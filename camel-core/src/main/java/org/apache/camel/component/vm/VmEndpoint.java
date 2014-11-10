begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.vm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|vm
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|BlockingQueue
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
name|Processor
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
name|seda
operator|.
name|BlockingQueueFactory
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
name|seda
operator|.
name|SedaEndpoint
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
name|UriEndpoint
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"vm"
argument_list|,
name|consumerClass
operator|=
name|VmConsumer
operator|.
name|class
argument_list|)
DECL|class|VmEndpoint
specifier|public
class|class
name|VmEndpoint
extends|extends
name|SedaEndpoint
block|{
DECL|method|VmEndpoint (String endpointUri, Component component, BlockingQueue<Exchange> queue)
specifier|public
name|VmEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|queue
argument_list|)
expr_stmt|;
block|}
DECL|method|VmEndpoint (String endpointUri, Component component, BlockingQueue<Exchange> queue, int concurrentConsumers)
specifier|public
name|VmEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
parameter_list|,
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|queue
argument_list|,
name|concurrentConsumers
argument_list|)
expr_stmt|;
block|}
DECL|method|VmEndpoint (String endpointUri, Component component, BlockingQueueFactory<Exchange> queueFactory, int concurrentConsumers)
specifier|public
name|VmEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|BlockingQueueFactory
argument_list|<
name|Exchange
argument_list|>
name|queueFactory
parameter_list|,
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|queueFactory
argument_list|,
name|concurrentConsumers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createNewConsumer (Processor processor)
specifier|protected
name|VmConsumer
name|createNewConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
return|return
operator|new
name|VmConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
block|}
end_class

end_unit

