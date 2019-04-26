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
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|PulsarClient
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
name|PulsarClientException
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
annotation|@
name|UriParam
DECL|field|pulsarConfiguration
specifier|private
name|PulsarConfiguration
name|pulsarConfiguration
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|label
operator|=
literal|"consumer,producer"
argument_list|,
name|description
operator|=
literal|"The Topic's full URI path including type, tenant and namespace"
argument_list|)
DECL|field|topic
specifier|private
specifier|final
name|String
name|topic
decl_stmt|;
DECL|method|PulsarEndpoint (String uri, String path, PulsarConfiguration pulsarConfiguration, PulsarComponent component, PulsarClient pulsarClient)
specifier|public
name|PulsarEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|path
parameter_list|,
name|PulsarConfiguration
name|pulsarConfiguration
parameter_list|,
name|PulsarComponent
name|component
parameter_list|,
name|PulsarClient
name|pulsarClient
parameter_list|)
throws|throws
name|PulsarClientException
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
name|topic
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|pulsarConfiguration
operator|=
name|pulsarConfiguration
expr_stmt|;
name|this
operator|.
name|pulsarClient
operator|=
name|pulsarClient
expr_stmt|;
block|}
DECL|method|create (final String uri, final String path, final PulsarConfiguration pulsarConfiguration, final PulsarComponent component, final PulsarClient pulsarClient)
specifier|public
specifier|static
name|PulsarEndpoint
name|create
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|path
parameter_list|,
specifier|final
name|PulsarConfiguration
name|pulsarConfiguration
parameter_list|,
specifier|final
name|PulsarComponent
name|component
parameter_list|,
specifier|final
name|PulsarClient
name|pulsarClient
parameter_list|)
throws|throws
name|PulsarClientException
throws|,
name|IllegalArgumentException
block|{
if|if
condition|(
literal|null
operator|==
name|pulsarConfiguration
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"PulsarEndpointConfiguration cannot be null"
argument_list|)
throw|;
block|}
return|return
operator|new
name|PulsarEndpoint
argument_list|(
name|uri
argument_list|,
name|path
argument_list|,
name|pulsarConfiguration
argument_list|,
name|component
argument_list|,
name|pulsarClient
argument_list|)
return|;
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
annotation|@
name|Override
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
annotation|@
name|Override
DECL|method|createExchange ()
specifier|public
name|Exchange
name|createExchange
parameter_list|()
block|{
return|return
name|super
operator|.
name|createExchange
argument_list|()
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
block|}
end_class

end_unit

