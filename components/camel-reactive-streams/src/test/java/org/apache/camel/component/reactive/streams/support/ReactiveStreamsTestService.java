begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|support
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
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|ReactiveStreamsConsumer
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreamsService
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|DispatchCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Publisher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Subscriber
import|;
end_import

begin_comment
comment|/**  * Test (dummy) service for reactive streams.  */
end_comment

begin_class
DECL|class|ReactiveStreamsTestService
specifier|public
class|class
name|ReactiveStreamsTestService
implements|implements
name|CamelReactiveStreamsService
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|method|ReactiveStreamsTestService ()
specifier|public
name|ReactiveStreamsTestService
parameter_list|()
block|{     }
DECL|method|ReactiveStreamsTestService (String name)
specifier|public
name|ReactiveStreamsTestService
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{      }
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{      }
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{      }
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getPublisher (String name)
specifier|public
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|getPublisher
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getPublisher (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Publisher
argument_list|<
name|T
argument_list|>
name|getPublisher
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getSubscriber (String name)
specifier|public
name|Subscriber
argument_list|<
name|Exchange
argument_list|>
name|getSubscriber
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getSubscriber (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Subscriber
argument_list|<
name|T
argument_list|>
name|getSubscriber
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|process (String name, Exchange exchange, DispatchCallback<Exchange> callback)
specifier|public
name|void
name|process
parameter_list|(
name|String
name|name
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|DispatchCallback
argument_list|<
name|Exchange
argument_list|>
name|callback
parameter_list|)
block|{      }
annotation|@
name|Override
DECL|method|attachConsumer (String name, ReactiveStreamsConsumer consumer)
specifier|public
name|void
name|attachConsumer
parameter_list|(
name|String
name|name
parameter_list|,
name|ReactiveStreamsConsumer
name|consumer
parameter_list|)
block|{      }
annotation|@
name|Override
DECL|method|detachConsumer (String name)
specifier|public
name|void
name|detachConsumer
parameter_list|(
name|String
name|name
parameter_list|)
block|{      }
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
block|}
end_class

end_unit

