begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.apns
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|apns
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArraySet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|notnoop
operator|.
name|apns
operator|.
name|ApnsService
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
name|DefaultConsumer
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
name|ScheduledPollEndpoint
import|;
end_import

begin_comment
comment|/**  * For sending notifications to Apple iOS devices.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.8.0"
argument_list|,
name|scheme
operator|=
literal|"apns"
argument_list|,
name|title
operator|=
literal|"APNS"
argument_list|,
name|syntax
operator|=
literal|"apns:name"
argument_list|,
name|label
operator|=
literal|"eventbus,mobile"
argument_list|)
DECL|class|ApnsEndpoint
specifier|public
class|class
name|ApnsEndpoint
extends|extends
name|ScheduledPollEndpoint
block|{
DECL|field|consumers
specifier|private
specifier|final
name|CopyOnWriteArraySet
argument_list|<
name|DefaultConsumer
argument_list|>
name|consumers
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Name of the endpoint"
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|UriParam
DECL|field|tokens
specifier|private
name|String
name|tokens
decl_stmt|;
DECL|method|ApnsEndpoint (String uri, ApnsComponent component)
specifier|public
name|ApnsEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|ApnsComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
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
DECL|method|getTokens ()
specifier|public
name|String
name|getTokens
parameter_list|()
block|{
return|return
name|tokens
return|;
block|}
comment|/**      * Configure this property in case you want to statically declare tokens related to devices you want to notify. Tokens are separated by comma.      */
DECL|method|setTokens (String tokens)
specifier|public
name|void
name|setTokens
parameter_list|(
name|String
name|tokens
parameter_list|)
block|{
name|this
operator|.
name|tokens
operator|=
name|tokens
expr_stmt|;
block|}
DECL|method|getApnsComponent ()
specifier|private
name|ApnsComponent
name|getApnsComponent
parameter_list|()
block|{
return|return
operator|(
name|ApnsComponent
operator|)
name|getComponent
argument_list|()
return|;
block|}
DECL|method|getApnsService ()
specifier|public
name|ApnsService
name|getApnsService
parameter_list|()
block|{
return|return
name|getApnsComponent
argument_list|()
operator|.
name|getApnsService
argument_list|()
return|;
block|}
DECL|method|getConsumers ()
specifier|protected
name|Set
argument_list|<
name|DefaultConsumer
argument_list|>
name|getConsumers
parameter_list|()
block|{
return|return
name|consumers
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
name|ApnsConsumer
name|apnsConsumer
init|=
operator|new
name|ApnsConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|apnsConsumer
argument_list|)
expr_stmt|;
return|return
name|apnsConsumer
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
name|ApnsProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

