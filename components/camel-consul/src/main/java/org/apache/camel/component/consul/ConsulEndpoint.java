begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
package|;
end_package

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|Consul
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
name|impl
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The camel consul component allows you to work with<a href="https://www.consul.io/">Consul</a>, a distributed, highly available, datacenter-aware, service discovery and configuration system.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.18.0"
argument_list|,
name|scheme
operator|=
literal|"consul"
argument_list|,
name|title
operator|=
literal|"Consul"
argument_list|,
name|syntax
operator|=
literal|"consul:apiEndpoint"
argument_list|,
name|label
operator|=
literal|"api,cloud"
argument_list|)
DECL|class|ConsulEndpoint
specifier|public
class|class
name|ConsulEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"The consul configuration"
argument_list|)
annotation|@
name|Metadata
DECL|field|configuration
specifier|private
specifier|final
name|ConsulConfiguration
name|configuration
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The API endpoint"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|apiEndpoint
specifier|private
specifier|final
name|String
name|apiEndpoint
decl_stmt|;
DECL|field|producerFactory
specifier|private
specifier|final
name|ProducerFactory
name|producerFactory
decl_stmt|;
DECL|field|consumerFactory
specifier|private
specifier|final
name|ConsumerFactory
name|consumerFactory
decl_stmt|;
DECL|field|consul
specifier|private
name|Consul
name|consul
decl_stmt|;
DECL|method|ConsulEndpoint ( String apiEndpoint, String uri, ConsulComponent component, ConsulConfiguration configuration, ProducerFactory producerFactory, ConsumerFactory consumerFactory)
specifier|public
name|ConsulEndpoint
parameter_list|(
name|String
name|apiEndpoint
parameter_list|,
name|String
name|uri
parameter_list|,
name|ConsulComponent
name|component
parameter_list|,
name|ConsulConfiguration
name|configuration
parameter_list|,
name|ProducerFactory
name|producerFactory
parameter_list|,
name|ConsumerFactory
name|consumerFactory
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
name|configuration
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
argument_list|,
literal|"configuration"
argument_list|)
expr_stmt|;
name|this
operator|.
name|apiEndpoint
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|apiEndpoint
argument_list|,
literal|"apiEndpoint"
argument_list|)
expr_stmt|;
name|this
operator|.
name|producerFactory
operator|=
name|producerFactory
expr_stmt|;
name|this
operator|.
name|consumerFactory
operator|=
name|consumerFactory
expr_stmt|;
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
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|producerFactory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No producer for "
operator|+
name|apiEndpoint
argument_list|)
throw|;
block|}
return|return
name|producerFactory
operator|.
name|create
argument_list|(
name|this
argument_list|,
name|configuration
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
if|if
condition|(
name|consumerFactory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No consumer for "
operator|+
name|apiEndpoint
argument_list|)
throw|;
block|}
return|return
name|consumerFactory
operator|.
name|create
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|,
name|processor
argument_list|)
return|;
block|}
comment|// *************************************************************************
comment|//
comment|// *************************************************************************
DECL|method|getConfiguration ()
specifier|public
name|ConsulConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|this
operator|.
name|configuration
return|;
block|}
DECL|method|getApiEndpoint ()
specifier|public
name|String
name|getApiEndpoint
parameter_list|()
block|{
return|return
name|this
operator|.
name|apiEndpoint
return|;
block|}
DECL|method|getConsul ()
specifier|public
specifier|synchronized
name|Consul
name|getConsul
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|consul
operator|==
literal|null
condition|)
block|{
name|consul
operator|=
name|configuration
operator|.
name|createConsulClient
argument_list|()
expr_stmt|;
block|}
return|return
name|consul
return|;
block|}
comment|// *************************************************************************
comment|//
comment|// *************************************************************************
annotation|@
name|FunctionalInterface
DECL|interface|ProducerFactory
specifier|public
interface|interface
name|ProducerFactory
block|{
DECL|method|create (ConsulEndpoint endpoint, ConsulConfiguration configuration)
name|Producer
name|create
parameter_list|(
name|ConsulEndpoint
name|endpoint
parameter_list|,
name|ConsulConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
annotation|@
name|FunctionalInterface
DECL|interface|ConsumerFactory
specifier|public
interface|interface
name|ConsumerFactory
block|{
DECL|method|create (ConsulEndpoint endpoint, ConsulConfiguration configuration, Processor processor)
name|Consumer
name|create
parameter_list|(
name|ConsulEndpoint
name|endpoint
parameter_list|,
name|ConsulConfiguration
name|configuration
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
block|}
end_class

end_unit

