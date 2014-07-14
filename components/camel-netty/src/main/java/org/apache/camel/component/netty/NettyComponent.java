begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|ThreadFactory
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
name|TimeUnit
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
name|impl
operator|.
name|UriEndpointComponent
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
name|IntrospectionSupport
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
name|concurrent
operator|.
name|CamelThreadFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|execution
operator|.
name|OrderedMemoryAwareThreadPoolExecutor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|util
operator|.
name|HashedWheelTimer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|util
operator|.
name|Timer
import|;
end_import

begin_class
DECL|class|NettyComponent
specifier|public
class|class
name|NettyComponent
extends|extends
name|UriEndpointComponent
block|{
comment|// use a shared timer for Netty (see javadoc for HashedWheelTimer)
DECL|field|timer
specifier|private
specifier|static
specifier|volatile
name|Timer
name|timer
decl_stmt|;
DECL|field|configuration
specifier|private
name|NettyConfiguration
name|configuration
decl_stmt|;
DECL|field|executorService
specifier|private
name|OrderedMemoryAwareThreadPoolExecutor
name|executorService
decl_stmt|;
DECL|method|NettyComponent ()
specifier|public
name|NettyComponent
parameter_list|()
block|{
name|super
argument_list|(
name|NettyEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|NettyComponent (Class<? extends Endpoint> endpointClass)
specifier|public
name|NettyComponent
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Endpoint
argument_list|>
name|endpointClass
parameter_list|)
block|{
name|super
argument_list|(
name|endpointClass
argument_list|)
expr_stmt|;
block|}
DECL|method|NettyComponent (CamelContext context)
specifier|public
name|NettyComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|NettyEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|NettyConfiguration
name|config
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|config
operator|=
name|configuration
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|config
operator|=
operator|new
name|NettyConfiguration
argument_list|()
expr_stmt|;
block|}
name|config
operator|=
name|parseConfiguration
argument_list|(
name|config
argument_list|,
name|remaining
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// merge any custom bootstrap configuration on the config
name|NettyServerBootstrapConfiguration
name|bootstrapConfiguration
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"bootstrapConfiguration"
argument_list|,
name|NettyServerBootstrapConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|bootstrapConfiguration
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|bootstrapConfiguration
argument_list|,
name|options
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|config
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
block|}
comment|// validate config
name|config
operator|.
name|validateConfiguration
argument_list|()
expr_stmt|;
name|NettyEndpoint
name|nettyEndpoint
init|=
operator|new
name|NettyEndpoint
argument_list|(
name|remaining
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
decl_stmt|;
name|nettyEndpoint
operator|.
name|setTimer
argument_list|(
name|getTimer
argument_list|()
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|nettyEndpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|nettyEndpoint
return|;
block|}
comment|/**      * Parses the configuration      *      * @return the parsed and valid configuration to use      */
DECL|method|parseConfiguration (NettyConfiguration configuration, String remaining, Map<String, Object> parameters)
specifier|protected
name|NettyConfiguration
name|parseConfiguration
parameter_list|(
name|NettyConfiguration
name|configuration
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|configuration
operator|.
name|parseURI
argument_list|(
operator|new
name|URI
argument_list|(
name|remaining
argument_list|)
argument_list|,
name|parameters
argument_list|,
name|this
argument_list|,
literal|"tcp"
argument_list|,
literal|"udp"
argument_list|)
expr_stmt|;
return|return
name|configuration
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|NettyConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (NettyConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|NettyConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getTimer ()
specifier|public
specifier|static
name|Timer
name|getTimer
parameter_list|()
block|{
return|return
name|timer
return|;
block|}
DECL|method|getExecutorService ()
specifier|public
specifier|synchronized
name|OrderedMemoryAwareThreadPoolExecutor
name|getExecutorService
parameter_list|()
block|{
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
name|executorService
operator|=
name|createExecutorService
argument_list|()
expr_stmt|;
block|}
return|return
name|executorService
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
if|if
condition|(
name|timer
operator|==
literal|null
condition|)
block|{
name|timer
operator|=
operator|new
name|HashedWheelTimer
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
name|configuration
operator|=
operator|new
name|NettyConfiguration
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|isOrderedThreadPoolExecutor
argument_list|()
condition|)
block|{
name|executorService
operator|=
name|createExecutorService
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
DECL|method|createExecutorService ()
specifier|protected
name|OrderedMemoryAwareThreadPoolExecutor
name|createExecutorService
parameter_list|()
block|{
comment|// use ordered thread pool, to ensure we process the events in order, and can send back
comment|// replies in the expected order. eg this is required by TCP.
comment|// and use a Camel thread factory so we have consistent thread namings
comment|// we should use a shared thread pool as recommended by Netty
name|String
name|pattern
init|=
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|getThreadNamePattern
argument_list|()
decl_stmt|;
name|ThreadFactory
name|factory
init|=
operator|new
name|CamelThreadFactory
argument_list|(
name|pattern
argument_list|,
literal|"NettyOrderedWorker"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
return|return
operator|new
name|OrderedMemoryAwareThreadPoolExecutor
argument_list|(
name|configuration
operator|.
name|getMaximumPoolSize
argument_list|()
argument_list|,
literal|0L
argument_list|,
literal|0L
argument_list|,
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
name|factory
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|timer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|timer
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|executorService
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

