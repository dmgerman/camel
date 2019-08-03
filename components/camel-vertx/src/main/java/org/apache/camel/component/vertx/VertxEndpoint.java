begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.vertx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|vertx
package|;
end_package

begin_import
import|import
name|io
operator|.
name|vertx
operator|.
name|core
operator|.
name|Vertx
import|;
end_import

begin_import
import|import
name|io
operator|.
name|vertx
operator|.
name|core
operator|.
name|eventbus
operator|.
name|EventBus
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
name|AsyncEndpoint
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
name|Consumer
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
name|MultipleConsumersSupport
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
name|spi
operator|.
name|Metadata
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
name|UriParam
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

begin_comment
comment|/**  * The vertx component is used for sending and receive messages from a vertx event bus.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.12.0"
argument_list|,
name|scheme
operator|=
literal|"vertx"
argument_list|,
name|title
operator|=
literal|"Vert.x"
argument_list|,
name|syntax
operator|=
literal|"vertx:address"
argument_list|,
name|label
operator|=
literal|"eventbus,reactive"
argument_list|)
DECL|class|VertxEndpoint
specifier|public
class|class
name|VertxEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|AsyncEndpoint
implements|,
name|MultipleConsumersSupport
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|address
specifier|private
name|String
name|address
decl_stmt|;
annotation|@
name|UriParam
DECL|field|pubSub
specifier|private
name|Boolean
name|pubSub
decl_stmt|;
DECL|method|VertxEndpoint (String uri, VertxComponent component, String address)
specifier|public
name|VertxEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|VertxComponent
name|component
parameter_list|,
name|String
name|address
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|VertxComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|VertxComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|VertxProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|VertxConsumer
name|consumer
init|=
operator|new
name|VertxConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|isMultipleConsumersSupported ()
specifier|public
name|boolean
name|isMultipleConsumersSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getEventBus ()
specifier|public
name|EventBus
name|getEventBus
parameter_list|()
block|{
if|if
condition|(
name|getVertx
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getVertx
argument_list|()
operator|.
name|eventBus
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|getVertx ()
specifier|public
name|Vertx
name|getVertx
parameter_list|()
block|{
return|return
name|getComponent
argument_list|()
operator|.
name|getVertx
argument_list|()
return|;
block|}
DECL|method|getAddress ()
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
comment|/**      * Sets the event bus address used to communicate      */
DECL|method|setAddress (String address)
specifier|public
name|void
name|setAddress
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
block|}
DECL|method|isPubSub ()
specifier|public
name|boolean
name|isPubSub
parameter_list|()
block|{
return|return
name|pubSub
operator|!=
literal|null
operator|&&
name|pubSub
return|;
block|}
DECL|method|getPubSub ()
specifier|public
name|Boolean
name|getPubSub
parameter_list|()
block|{
return|return
name|pubSub
return|;
block|}
comment|/**      * Whether to use publish/subscribe instead of point to point when sending to a vertx endpoint.      */
DECL|method|setPubSub (Boolean pubSub)
specifier|public
name|void
name|setPubSub
parameter_list|(
name|Boolean
name|pubSub
parameter_list|)
block|{
name|this
operator|.
name|pubSub
operator|=
name|pubSub
expr_stmt|;
block|}
block|}
end_class

end_unit

