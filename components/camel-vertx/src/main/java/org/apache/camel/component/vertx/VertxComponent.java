begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|List
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
name|CountDownLatch
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
name|io
operator|.
name|vertx
operator|.
name|core
operator|.
name|AsyncResult
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
name|AsyncResultHandler
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
name|VertxOptions
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
name|impl
operator|.
name|VertxFactoryImpl
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
name|spi
operator|.
name|VertxFactory
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
name|ComponentConfiguration
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
name|spi
operator|.
name|EndpointCompleter
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A Camel Component for<a href="http://vertx.io/">vert.x</a>  */
end_comment

begin_class
DECL|class|VertxComponent
specifier|public
class|class
name|VertxComponent
extends|extends
name|UriEndpointComponent
implements|implements
name|EndpointCompleter
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|VertxComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|vertxFactory
specifier|private
name|VertxFactory
name|vertxFactory
decl_stmt|;
DECL|field|createdVertx
specifier|private
specifier|volatile
name|boolean
name|createdVertx
decl_stmt|;
DECL|field|vertx
specifier|private
name|Vertx
name|vertx
decl_stmt|;
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|field|timeout
specifier|private
name|int
name|timeout
init|=
literal|60
decl_stmt|;
DECL|field|vertxOptions
specifier|private
name|VertxOptions
name|vertxOptions
decl_stmt|;
DECL|method|VertxComponent ()
specifier|public
name|VertxComponent
parameter_list|()
block|{
name|super
argument_list|(
name|VertxEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|VertxComponent (CamelContext context)
specifier|public
name|VertxComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|VertxEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|getVertxFactory ()
specifier|public
name|VertxFactory
name|getVertxFactory
parameter_list|()
block|{
return|return
name|vertxFactory
return|;
block|}
comment|/**      * To use a custom VertxFactory implementation      */
DECL|method|setVertxFactory (VertxFactory vertxFactory)
specifier|public
name|void
name|setVertxFactory
parameter_list|(
name|VertxFactory
name|vertxFactory
parameter_list|)
block|{
name|this
operator|.
name|vertxFactory
operator|=
name|vertxFactory
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
comment|/**      * Hostname for creating an embedded clustered EventBus      */
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
comment|/**      * Port for creating an embedded clustered EventBus      */
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getVertxOptions ()
specifier|public
name|VertxOptions
name|getVertxOptions
parameter_list|()
block|{
return|return
name|vertxOptions
return|;
block|}
comment|/**      * Options to use for creating vertx      */
DECL|method|setVertxOptions (VertxOptions vertxOptions)
specifier|public
name|void
name|setVertxOptions
parameter_list|(
name|VertxOptions
name|vertxOptions
parameter_list|)
block|{
name|this
operator|.
name|vertxOptions
operator|=
name|vertxOptions
expr_stmt|;
block|}
DECL|method|getVertx ()
specifier|public
name|Vertx
name|getVertx
parameter_list|()
block|{
return|return
name|vertx
return|;
block|}
comment|/**      * To use the given vertx EventBus instead of creating a new embedded EventBus      */
DECL|method|setVertx (Vertx vertx)
specifier|public
name|void
name|setVertx
parameter_list|(
name|Vertx
name|vertx
parameter_list|)
block|{
name|this
operator|.
name|vertx
operator|=
name|vertx
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|int
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
comment|/**      * Timeout in seconds to wait for clustered Vertx EventBus to be ready.      *<p/>      * The default value is 60.      */
DECL|method|setTimeout (int timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
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
name|VertxEndpoint
name|endpoint
init|=
operator|new
name|VertxEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|completeEndpointPath (ComponentConfiguration componentConfiguration, String text)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|completeEndpointPath
parameter_list|(
name|ComponentConfiguration
name|componentConfiguration
parameter_list|,
name|String
name|text
parameter_list|)
block|{
comment|// TODO is there any way to find out the list of endpoint names in vertx?
return|return
literal|null
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|vertx
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|vertxFactory
operator|==
literal|null
condition|)
block|{
name|vertxFactory
operator|=
operator|new
name|VertxFactoryImpl
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|vertxOptions
operator|==
literal|null
condition|)
block|{
name|vertxOptions
operator|=
operator|new
name|VertxOptions
argument_list|()
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|host
argument_list|)
condition|)
block|{
name|vertxOptions
operator|.
name|setClusterHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
name|vertxOptions
operator|.
name|setClustered
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|port
operator|>
literal|0
condition|)
block|{
name|vertxOptions
operator|.
name|setClusterPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|vertxOptions
operator|.
name|setClustered
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
comment|// we are creating vertx so we should handle its lifecycle
name|createdVertx
operator|=
literal|true
expr_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// lets using a host / port if a host name is specified
if|if
condition|(
name|vertxOptions
operator|.
name|isClustered
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating Clustered Vertx {}:{}"
argument_list|,
name|vertxOptions
operator|.
name|getClusterHost
argument_list|()
argument_list|,
name|vertxOptions
operator|.
name|getClusterPort
argument_list|()
argument_list|)
expr_stmt|;
comment|// use the async api as we want to wait for the eventbus to be ready before we are in started state
name|vertxFactory
operator|.
name|clusteredVertx
argument_list|(
name|vertxOptions
argument_list|,
operator|new
name|AsyncResultHandler
argument_list|<
name|Vertx
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|AsyncResult
argument_list|<
name|Vertx
argument_list|>
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|cause
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error creating Clustered Vertx "
operator|+
name|host
operator|+
literal|":"
operator|+
name|port
operator|+
literal|" due "
operator|+
name|event
operator|.
name|cause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|,
name|event
operator|.
name|cause
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|.
name|succeeded
argument_list|()
condition|)
block|{
name|vertx
operator|=
name|event
operator|.
name|result
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"EventBus is ready: {}"
argument_list|,
name|vertx
argument_list|)
expr_stmt|;
block|}
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating Non-Clustered Vertx"
argument_list|)
expr_stmt|;
name|vertx
operator|=
name|vertxFactory
operator|.
name|vertx
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"EventBus is ready: {}"
argument_list|,
name|vertx
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|latch
operator|.
name|getCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Waiting for EventBus to be ready using {} sec as timeout"
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using Vert.x instance set on the component level."
argument_list|)
expr_stmt|;
block|}
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|createdVertx
operator|&&
name|vertx
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Stopping Vertx {}"
argument_list|,
name|vertx
argument_list|)
expr_stmt|;
name|vertx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

