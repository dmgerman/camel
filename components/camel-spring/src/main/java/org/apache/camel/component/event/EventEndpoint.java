begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.event
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|event
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
name|Producer
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
name|loadbalancer
operator|.
name|LoadBalancer
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
name|loadbalancer
operator|.
name|TopicLoadBalancer
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
name|UriPath
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
name|support
operator|.
name|DefaultEndpoint
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
name|support
operator|.
name|DefaultProducer
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
name|springframework
operator|.
name|beans
operator|.
name|BeansException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContextAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationEvent
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * The spring-event component allows to listen for Spring Application Events.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"1.4.0"
argument_list|,
name|scheme
operator|=
literal|"spring-event"
argument_list|,
name|title
operator|=
literal|"Spring Event"
argument_list|,
name|syntax
operator|=
literal|"spring-event:name"
argument_list|,
name|label
operator|=
literal|"spring,eventbus"
argument_list|)
DECL|class|EventEndpoint
specifier|public
class|class
name|EventEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|ApplicationContextAware
block|{
DECL|field|loadBalancer
specifier|private
name|LoadBalancer
name|loadBalancer
decl_stmt|;
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Name of endpoint"
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|method|EventEndpoint (String endpointUri, EventComponent component, String name)
specifier|public
name|EventEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|EventComponent
name|component
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|applicationContext
operator|=
name|component
operator|.
name|getApplicationContext
argument_list|()
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|setApplicationContext (ApplicationContext applicationContext)
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
throws|throws
name|BeansException
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
block|}
DECL|method|getApplicationContext ()
specifier|public
name|ApplicationContext
name|getApplicationContext
parameter_list|()
block|{
return|return
name|applicationContext
return|;
block|}
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
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|applicationContext
argument_list|,
literal|"applicationContext"
argument_list|)
expr_stmt|;
return|return
operator|new
name|DefaultProducer
argument_list|(
name|this
argument_list|)
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|ApplicationEvent
name|event
init|=
name|toApplicationEvent
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|applicationContext
operator|.
name|publishEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|EventConsumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|applicationContext
argument_list|,
literal|"applicationContext"
argument_list|)
expr_stmt|;
name|EventConsumer
name|answer
init|=
operator|new
name|EventConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|onApplicationEvent (ApplicationEvent event)
specifier|public
name|void
name|onApplicationEvent
parameter_list|(
name|ApplicationEvent
name|event
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|event
argument_list|)
expr_stmt|;
try|try
block|{
name|getLoadBalancer
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getLoadBalancer ()
specifier|public
name|LoadBalancer
name|getLoadBalancer
parameter_list|()
block|{
if|if
condition|(
name|loadBalancer
operator|==
literal|null
condition|)
block|{
name|loadBalancer
operator|=
name|createLoadBalancer
argument_list|()
expr_stmt|;
block|}
return|return
name|loadBalancer
return|;
block|}
DECL|method|setLoadBalancer (LoadBalancer loadBalancer)
specifier|public
name|void
name|setLoadBalancer
parameter_list|(
name|LoadBalancer
name|loadBalancer
parameter_list|)
block|{
name|this
operator|.
name|loadBalancer
operator|=
name|loadBalancer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|EventComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|EventComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|consumerStarted (EventConsumer consumer)
specifier|public
specifier|synchronized
name|void
name|consumerStarted
parameter_list|(
name|EventConsumer
name|consumer
parameter_list|)
block|{
name|getComponent
argument_list|()
operator|.
name|consumerStarted
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|getLoadBalancer
argument_list|()
operator|.
name|addProcessor
argument_list|(
name|consumer
operator|.
name|getAsyncProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|consumerStopped (EventConsumer consumer)
specifier|public
specifier|synchronized
name|void
name|consumerStopped
parameter_list|(
name|EventConsumer
name|consumer
parameter_list|)
block|{
name|getComponent
argument_list|()
operator|.
name|consumerStopped
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|getLoadBalancer
argument_list|()
operator|.
name|removeProcessor
argument_list|(
name|consumer
operator|.
name|getAsyncProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createLoadBalancer ()
specifier|protected
name|LoadBalancer
name|createLoadBalancer
parameter_list|()
block|{
return|return
operator|new
name|TopicLoadBalancer
argument_list|()
return|;
block|}
DECL|method|toApplicationEvent (Exchange exchange)
specifier|protected
name|ApplicationEvent
name|toApplicationEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|ApplicationEvent
name|event
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ApplicationEvent
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|event
operator|!=
literal|null
condition|)
block|{
return|return
name|event
return|;
block|}
return|return
operator|new
name|CamelEvent
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
return|;
block|}
block|}
end_class

end_unit

