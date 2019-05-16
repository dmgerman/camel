begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.webhook
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|webhook
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Endpoint
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
name|spi
operator|.
name|RestConfiguration
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
name|RestConsumerFactory
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
name|service
operator|.
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * MultiRestConsumer allows to bind the webhook to multiple local rest endpoints.  * It is useful for services that need to respond to multiple kinds of requests.  *<p>  * E.g. some webhook providers operate over POST but they do require that a specific endpoint replies also to  * GET requests during handshake.  */
end_comment

begin_class
DECL|class|MultiRestConsumer
specifier|public
class|class
name|MultiRestConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|delegateConsumers
specifier|private
name|List
argument_list|<
name|Consumer
argument_list|>
name|delegateConsumers
decl_stmt|;
DECL|method|MultiRestConsumer (CamelContext context, RestConsumerFactory factory, Endpoint endpoint, Processor processor, List<String> methods, String url, String path, RestConfiguration config, ConsumerConfigurer configurer)
specifier|public
name|MultiRestConsumer
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|RestConsumerFactory
name|factory
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|methods
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|path
parameter_list|,
name|RestConfiguration
name|config
parameter_list|,
name|ConsumerConfigurer
name|configurer
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|delegateConsumers
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|method
range|:
name|methods
control|)
block|{
name|Consumer
name|consumer
init|=
name|factory
operator|.
name|createConsumer
argument_list|(
name|context
argument_list|,
name|processor
argument_list|,
name|method
argument_list|,
name|path
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|config
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
decl_stmt|;
name|configurer
operator|.
name|configure
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRestRegistry
argument_list|()
operator|.
name|addRestService
argument_list|(
name|consumer
argument_list|,
name|url
argument_list|,
name|url
argument_list|,
name|path
argument_list|,
literal|null
argument_list|,
name|method
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|delegateConsumers
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doInit ()
specifier|protected
name|void
name|doInit
parameter_list|()
block|{
for|for
control|(
name|Consumer
name|consumer
range|:
name|this
operator|.
name|delegateConsumers
control|)
block|{
name|consumer
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|public
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
for|for
control|(
name|Consumer
name|consumer
range|:
name|this
operator|.
name|delegateConsumers
control|)
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
for|for
control|(
name|Consumer
name|consumer
range|:
name|this
operator|.
name|delegateConsumers
control|)
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
for|for
control|(
name|Consumer
name|consumer
range|:
name|this
operator|.
name|delegateConsumers
control|)
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|FunctionalInterface
DECL|interface|ConsumerConfigurer
interface|interface
name|ConsumerConfigurer
block|{
DECL|method|configure (Consumer consumer)
name|void
name|configure
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
block|}
end_class

end_unit

