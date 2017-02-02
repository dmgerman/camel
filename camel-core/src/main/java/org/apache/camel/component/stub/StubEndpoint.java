begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stub
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stub
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
name|vm
operator|.
name|VmConsumer
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
name|vm
operator|.
name|VmEndpoint
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

begin_comment
comment|/**  * The stub component provides a simple way to stub out any physical endpoints while in development or testing.  *  * For example to run a route without needing to actually connect to a specific SMTP or Http endpoint.  * Just add stub: in front of any endpoint URI to stub out the endpoint.  * Internally the Stub component creates VM endpoints. The main difference between Stub and VM is that VM  * will validate the URI and parameters you give it, so putting vm: in front of a typical URI with  * query arguments will usually fail. Stub won't though, as it basically ignores all query parameters  * to let you quickly stub out one or more endpoints in your route temporarily.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.10.0"
argument_list|,
name|scheme
operator|=
literal|"stub"
argument_list|,
name|title
operator|=
literal|"Stub"
argument_list|,
name|syntax
operator|=
literal|"stub:name"
argument_list|,
name|consumerClass
operator|=
name|VmConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"core,testing"
argument_list|)
DECL|class|StubEndpoint
specifier|public
class|class
name|StubEndpoint
extends|extends
name|VmEndpoint
block|{
DECL|method|StubEndpoint (String endpointUri, Component component, BlockingQueue<Exchange> queue)
specifier|public
name|StubEndpoint
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
DECL|method|StubEndpoint (String endpointUri, Component component, BlockingQueue<Exchange> queue, int concurrentConsumers)
specifier|public
name|StubEndpoint
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
DECL|method|StubEndpoint (String endpointUri, Component component, BlockingQueueFactory<Exchange> queueFactory, int concurrentConsumers)
specifier|public
name|StubEndpoint
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
name|StubConsumer
name|createNewConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
return|return
operator|new
name|StubConsumer
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

