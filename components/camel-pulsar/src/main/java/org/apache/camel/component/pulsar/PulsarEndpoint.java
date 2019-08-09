begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pulsar
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pulsar
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
name|component
operator|.
name|pulsar
operator|.
name|configuration
operator|.
name|PulsarConfiguration
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
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|PulsarClient
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"pulsar"
argument_list|,
name|firstVersion
operator|=
literal|"2.24.0"
argument_list|,
name|title
operator|=
literal|"Apache Pulsar"
argument_list|,
name|syntax
operator|=
literal|"pulsar:persistence://tenant/namespace/topic"
argument_list|,
name|label
operator|=
literal|"messaging"
argument_list|)
DECL|class|PulsarEndpoint
specifier|public
class|class
name|PulsarEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|pulsarClient
specifier|private
name|PulsarClient
name|pulsarClient
decl_stmt|;
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|enums
operator|=
literal|"persistent,non-persistent"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|persistence
specifier|private
name|String
name|persistence
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|tenant
specifier|private
name|String
name|tenant
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|namespace
specifier|private
name|String
name|namespace
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|topic
specifier|private
name|String
name|topic
decl_stmt|;
annotation|@
name|UriParam
DECL|field|pulsarConfiguration
specifier|private
name|PulsarConfiguration
name|pulsarConfiguration
decl_stmt|;
DECL|method|PulsarEndpoint (String uri, PulsarComponent component)
specifier|public
name|PulsarEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|PulsarComponent
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
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
block|{
return|return
operator|new
name|PulsarProducer
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
name|PulsarConsumer
name|consumer
init|=
operator|new
name|PulsarConsumer
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
DECL|method|getPulsarClient ()
specifier|public
name|PulsarClient
name|getPulsarClient
parameter_list|()
block|{
return|return
name|pulsarClient
return|;
block|}
comment|/**      * To use a custom pulsar client      */
DECL|method|setPulsarClient (PulsarClient pulsarClient)
specifier|public
name|void
name|setPulsarClient
parameter_list|(
name|PulsarClient
name|pulsarClient
parameter_list|)
block|{
name|this
operator|.
name|pulsarClient
operator|=
name|pulsarClient
expr_stmt|;
block|}
DECL|method|getPersistence ()
specifier|public
name|String
name|getPersistence
parameter_list|()
block|{
return|return
name|persistence
return|;
block|}
comment|/**      * Whether the topic is persistent or non-persistent      */
DECL|method|setPersistence (String persistence)
specifier|public
name|void
name|setPersistence
parameter_list|(
name|String
name|persistence
parameter_list|)
block|{
name|this
operator|.
name|persistence
operator|=
name|persistence
expr_stmt|;
block|}
DECL|method|getTenant ()
specifier|public
name|String
name|getTenant
parameter_list|()
block|{
return|return
name|tenant
return|;
block|}
comment|/**      * The tenant      */
DECL|method|setTenant (String tenant)
specifier|public
name|void
name|setTenant
parameter_list|(
name|String
name|tenant
parameter_list|)
block|{
name|this
operator|.
name|tenant
operator|=
name|tenant
expr_stmt|;
block|}
DECL|method|getNamespace ()
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
comment|/**      * The namespace      */
DECL|method|setNamespace (String namespace)
specifier|public
name|void
name|setNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
block|}
DECL|method|getTopic ()
specifier|public
name|String
name|getTopic
parameter_list|()
block|{
return|return
name|topic
return|;
block|}
comment|/**      * The topic      */
DECL|method|setTopic (String topic)
specifier|public
name|void
name|setTopic
parameter_list|(
name|String
name|topic
parameter_list|)
block|{
name|this
operator|.
name|topic
operator|=
name|topic
expr_stmt|;
block|}
DECL|method|getPulsarConfiguration ()
specifier|public
name|PulsarConfiguration
name|getPulsarConfiguration
parameter_list|()
block|{
return|return
name|pulsarConfiguration
return|;
block|}
DECL|method|setPulsarConfiguration (PulsarConfiguration pulsarConfiguration)
specifier|public
name|void
name|setPulsarConfiguration
parameter_list|(
name|PulsarConfiguration
name|pulsarConfiguration
parameter_list|)
block|{
name|this
operator|.
name|pulsarConfiguration
operator|=
name|pulsarConfiguration
expr_stmt|;
block|}
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|persistence
operator|+
literal|"://"
operator|+
name|tenant
operator|+
literal|"/"
operator|+
name|namespace
operator|+
literal|"/"
operator|+
name|topic
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|persistence
argument_list|,
literal|"persistence"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|tenant
argument_list|,
literal|"tenant"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|namespace
argument_list|,
literal|"namespace"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|topic
argument_list|,
literal|"topic"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|uri
operator|=
name|persistence
operator|+
literal|"://"
operator|+
name|tenant
operator|+
literal|"/"
operator|+
name|namespace
operator|+
literal|"/"
operator|+
name|topic
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|PulsarComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|PulsarComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
block|}
end_class

end_unit

