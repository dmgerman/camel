begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
package|;
end_package

begin_comment
comment|/**  * Options to configure on a {@link RestletHost}  */
end_comment

begin_class
DECL|class|RestletHostOptions
specifier|public
specifier|final
class|class
name|RestletHostOptions
block|{
comment|/**      * Indicates if the controller thread should be a daemon.      */
DECL|field|controllerDaemon
specifier|private
name|Boolean
name|controllerDaemon
decl_stmt|;
comment|/**      * Time for the controller thread to sleep between each control.      */
DECL|field|controllerSleepTimeMs
specifier|private
name|Integer
name|controllerSleepTimeMs
decl_stmt|;
comment|/**      * Size of the content buffer for receiving messages.      */
DECL|field|inboundBufferSize
specifier|private
name|Integer
name|inboundBufferSize
decl_stmt|;
comment|/**      * Maximum number of calls that can be queued if there arenât any worker thread available to service them.      */
DECL|field|maxQueued
specifier|private
name|Integer
name|maxQueued
decl_stmt|;
comment|/**      * Maximum number of concurrent connections per host.      */
DECL|field|maxConnectionsPerHost
specifier|private
name|Integer
name|maxConnectionsPerHost
decl_stmt|;
comment|/**      * Maximum number of worker threads waiting to service requests.      */
DECL|field|maxThreads
specifier|private
name|Integer
name|maxThreads
decl_stmt|;
comment|/**      * Maximum number of concurrent connections in total.      */
DECL|field|maxTotalConnections
specifier|private
name|Integer
name|maxTotalConnections
decl_stmt|;
comment|/**      * Minimum number of worker threads waiting to service requests, even if they are idle.      */
DECL|field|minThreads
specifier|private
name|Integer
name|minThreads
decl_stmt|;
comment|/**      * Number of worker threads determining when the connector is considered overloaded.      */
DECL|field|lowThreads
specifier|private
name|Integer
name|lowThreads
decl_stmt|;
comment|/**      * Size of the content buffer for sending messages.      */
DECL|field|outboundBufferSize
specifier|private
name|Integer
name|outboundBufferSize
decl_stmt|;
comment|/**      * Indicates if connections should be kept alive after a call.      */
DECL|field|persistingConnections
specifier|private
name|Boolean
name|persistingConnections
decl_stmt|;
comment|/**      * Indicates if pipelining connections are supported.      */
DECL|field|pipeliningConnections
specifier|private
name|Boolean
name|pipeliningConnections
decl_stmt|;
comment|/**      * Enable/disable the SO_REUSEADDR socket option. See java.io.ServerSocket#reuseAddress property for additional details.      */
DECL|field|reuseAddress
specifier|private
name|Boolean
name|reuseAddress
decl_stmt|;
comment|/**      * Time for an idle thread to wait for an operation before being collected.      */
DECL|field|threadMaxIdleTimeMs
specifier|private
name|Integer
name|threadMaxIdleTimeMs
decl_stmt|;
comment|/**      * Lookup the "X-Forwarded-For" header supported by popular proxies and caches and uses it to populate the Request.getClientAddresses() method result.      */
DECL|field|useForwardedForHeader
specifier|private
name|Boolean
name|useForwardedForHeader
decl_stmt|;
DECL|method|getControllerDaemon ()
specifier|public
name|Boolean
name|getControllerDaemon
parameter_list|()
block|{
return|return
name|controllerDaemon
return|;
block|}
DECL|method|setControllerDaemon (Boolean controllerDaemon)
specifier|public
name|void
name|setControllerDaemon
parameter_list|(
name|Boolean
name|controllerDaemon
parameter_list|)
block|{
name|this
operator|.
name|controllerDaemon
operator|=
name|controllerDaemon
expr_stmt|;
block|}
DECL|method|getControllerSleepTimeMs ()
specifier|public
name|Integer
name|getControllerSleepTimeMs
parameter_list|()
block|{
return|return
name|controllerSleepTimeMs
return|;
block|}
DECL|method|setControllerSleepTimeMs (Integer controllerSleepTimeMs)
specifier|public
name|void
name|setControllerSleepTimeMs
parameter_list|(
name|Integer
name|controllerSleepTimeMs
parameter_list|)
block|{
name|this
operator|.
name|controllerSleepTimeMs
operator|=
name|controllerSleepTimeMs
expr_stmt|;
block|}
DECL|method|getInboundBufferSize ()
specifier|public
name|Integer
name|getInboundBufferSize
parameter_list|()
block|{
return|return
name|inboundBufferSize
return|;
block|}
DECL|method|setInboundBufferSize (Integer inboundBufferSize)
specifier|public
name|void
name|setInboundBufferSize
parameter_list|(
name|Integer
name|inboundBufferSize
parameter_list|)
block|{
name|this
operator|.
name|inboundBufferSize
operator|=
name|inboundBufferSize
expr_stmt|;
block|}
DECL|method|getMaxQueued ()
specifier|public
name|Integer
name|getMaxQueued
parameter_list|()
block|{
return|return
name|maxQueued
return|;
block|}
DECL|method|setMaxQueued (Integer maxQueued)
specifier|public
name|void
name|setMaxQueued
parameter_list|(
name|Integer
name|maxQueued
parameter_list|)
block|{
name|this
operator|.
name|maxQueued
operator|=
name|maxQueued
expr_stmt|;
block|}
DECL|method|getMaxConnectionsPerHost ()
specifier|public
name|Integer
name|getMaxConnectionsPerHost
parameter_list|()
block|{
return|return
name|maxConnectionsPerHost
return|;
block|}
DECL|method|setMaxConnectionsPerHost (Integer maxConnectionsPerHost)
specifier|public
name|void
name|setMaxConnectionsPerHost
parameter_list|(
name|Integer
name|maxConnectionsPerHost
parameter_list|)
block|{
name|this
operator|.
name|maxConnectionsPerHost
operator|=
name|maxConnectionsPerHost
expr_stmt|;
block|}
DECL|method|getMaxThreads ()
specifier|public
name|Integer
name|getMaxThreads
parameter_list|()
block|{
return|return
name|maxThreads
return|;
block|}
DECL|method|setMaxThreads (Integer maxThreads)
specifier|public
name|void
name|setMaxThreads
parameter_list|(
name|Integer
name|maxThreads
parameter_list|)
block|{
name|this
operator|.
name|maxThreads
operator|=
name|maxThreads
expr_stmt|;
block|}
DECL|method|getMaxTotalConnections ()
specifier|public
name|Integer
name|getMaxTotalConnections
parameter_list|()
block|{
return|return
name|maxTotalConnections
return|;
block|}
DECL|method|setMaxTotalConnections (Integer maxTotalConnections)
specifier|public
name|void
name|setMaxTotalConnections
parameter_list|(
name|Integer
name|maxTotalConnections
parameter_list|)
block|{
name|this
operator|.
name|maxTotalConnections
operator|=
name|maxTotalConnections
expr_stmt|;
block|}
DECL|method|getMinThreads ()
specifier|public
name|Integer
name|getMinThreads
parameter_list|()
block|{
return|return
name|minThreads
return|;
block|}
DECL|method|setMinThreads (Integer minThreads)
specifier|public
name|void
name|setMinThreads
parameter_list|(
name|Integer
name|minThreads
parameter_list|)
block|{
name|this
operator|.
name|minThreads
operator|=
name|minThreads
expr_stmt|;
block|}
DECL|method|getLowThreads ()
specifier|public
name|Integer
name|getLowThreads
parameter_list|()
block|{
return|return
name|lowThreads
return|;
block|}
DECL|method|setLowThreads (Integer lowThreads)
specifier|public
name|void
name|setLowThreads
parameter_list|(
name|Integer
name|lowThreads
parameter_list|)
block|{
name|this
operator|.
name|lowThreads
operator|=
name|lowThreads
expr_stmt|;
block|}
DECL|method|getOutboundBufferSize ()
specifier|public
name|Integer
name|getOutboundBufferSize
parameter_list|()
block|{
return|return
name|outboundBufferSize
return|;
block|}
DECL|method|setOutboundBufferSize (Integer outboundBufferSize)
specifier|public
name|void
name|setOutboundBufferSize
parameter_list|(
name|Integer
name|outboundBufferSize
parameter_list|)
block|{
name|this
operator|.
name|outboundBufferSize
operator|=
name|outboundBufferSize
expr_stmt|;
block|}
DECL|method|getPersistingConnections ()
specifier|public
name|Boolean
name|getPersistingConnections
parameter_list|()
block|{
return|return
name|persistingConnections
return|;
block|}
DECL|method|setPersistingConnections (Boolean persistingConnections)
specifier|public
name|void
name|setPersistingConnections
parameter_list|(
name|Boolean
name|persistingConnections
parameter_list|)
block|{
name|this
operator|.
name|persistingConnections
operator|=
name|persistingConnections
expr_stmt|;
block|}
DECL|method|getPipeliningConnections ()
specifier|public
name|Boolean
name|getPipeliningConnections
parameter_list|()
block|{
return|return
name|pipeliningConnections
return|;
block|}
DECL|method|setPipeliningConnections (Boolean pipeliningConnections)
specifier|public
name|void
name|setPipeliningConnections
parameter_list|(
name|Boolean
name|pipeliningConnections
parameter_list|)
block|{
name|this
operator|.
name|pipeliningConnections
operator|=
name|pipeliningConnections
expr_stmt|;
block|}
DECL|method|getReuseAddress ()
specifier|public
name|Boolean
name|getReuseAddress
parameter_list|()
block|{
return|return
name|reuseAddress
return|;
block|}
DECL|method|setReuseAddress (Boolean reuseAddress)
specifier|public
name|void
name|setReuseAddress
parameter_list|(
name|Boolean
name|reuseAddress
parameter_list|)
block|{
name|this
operator|.
name|reuseAddress
operator|=
name|reuseAddress
expr_stmt|;
block|}
DECL|method|getThreadMaxIdleTimeMs ()
specifier|public
name|Integer
name|getThreadMaxIdleTimeMs
parameter_list|()
block|{
return|return
name|threadMaxIdleTimeMs
return|;
block|}
DECL|method|setThreadMaxIdleTimeMs (Integer threadMaxIdleTimeMs)
specifier|public
name|void
name|setThreadMaxIdleTimeMs
parameter_list|(
name|Integer
name|threadMaxIdleTimeMs
parameter_list|)
block|{
name|this
operator|.
name|threadMaxIdleTimeMs
operator|=
name|threadMaxIdleTimeMs
expr_stmt|;
block|}
DECL|method|getUseForwardedForHeader ()
specifier|public
name|Boolean
name|getUseForwardedForHeader
parameter_list|()
block|{
return|return
name|useForwardedForHeader
return|;
block|}
DECL|method|setUseForwardedForHeader (Boolean useForwardedForHeader)
specifier|public
name|void
name|setUseForwardedForHeader
parameter_list|(
name|Boolean
name|useForwardedForHeader
parameter_list|)
block|{
name|this
operator|.
name|useForwardedForHeader
operator|=
name|useForwardedForHeader
expr_stmt|;
block|}
block|}
end_class

end_unit

