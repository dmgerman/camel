begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet.springboot
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
operator|.
name|springboot
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
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Component for consuming and producing Restful resources using Restlet.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.restlet"
argument_list|)
DECL|class|RestletComponentConfiguration
specifier|public
class|class
name|RestletComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the restlet component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * Indicates if the controller thread should be a daemon (not blocking JVM      * exit).      */
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
comment|/**      * To use a custom org.apache.camel.spi.HeaderFilterStrategy to filter      * header to and from Camel message. The option is a      * org.apache.camel.spi.HeaderFilterStrategy type.      */
DECL|field|headerFilterStrategy
specifier|private
name|String
name|headerFilterStrategy
decl_stmt|;
comment|/**      * The size of the buffer when reading messages.      */
DECL|field|inboundBufferSize
specifier|private
name|Integer
name|inboundBufferSize
decl_stmt|;
comment|/**      * Maximum number of concurrent connections per host (IP address).      */
DECL|field|maxConnectionsPerHost
specifier|private
name|Integer
name|maxConnectionsPerHost
decl_stmt|;
comment|/**      * Maximum threads that will service requests.      */
DECL|field|maxThreads
specifier|private
name|Integer
name|maxThreads
decl_stmt|;
comment|/**      * Number of worker threads determining when the connector is considered      * overloaded.      */
DECL|field|lowThreads
specifier|private
name|Integer
name|lowThreads
decl_stmt|;
comment|/**      * Maximum number of concurrent connections in total.      */
DECL|field|maxTotalConnections
specifier|private
name|Integer
name|maxTotalConnections
decl_stmt|;
comment|/**      * Minimum threads waiting to service requests.      */
DECL|field|minThreads
specifier|private
name|Integer
name|minThreads
decl_stmt|;
comment|/**      * The size of the buffer when writing messages.      */
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
comment|/**      * Time for an idle thread to wait for an operation before being collected.      */
DECL|field|threadMaxIdleTimeMs
specifier|private
name|Integer
name|threadMaxIdleTimeMs
decl_stmt|;
comment|/**      * Lookup the X-Forwarded-For header supported by popular proxies and caches      * and uses it to populate the Request.getClientAddresses() method result.      * This information is only safe for intermediary components within your      * local network. Other addresses could easily be changed by setting a fake      * header and should not be trusted for serious security checks.      */
DECL|field|useForwardedForHeader
specifier|private
name|Boolean
name|useForwardedForHeader
decl_stmt|;
comment|/**      * Enable/disable the SO_REUSEADDR socket option. See      * java.io.ServerSocket#reuseAddress property for additional details.      */
DECL|field|reuseAddress
specifier|private
name|Boolean
name|reuseAddress
decl_stmt|;
comment|/**      * Maximum number of calls that can be queued if there aren't any worker      * thread available to service them. If the value is '0', then no queue is      * used and calls are rejected if no worker thread is immediately available.      * If the value is '-1', then an unbounded queue is used and calls are never      * rejected.      */
DECL|field|maxQueued
specifier|private
name|Integer
name|maxQueued
decl_stmt|;
comment|/**      * Determines whether or not the raw input stream from Restlet is cached or      * not (Camel will read the stream into a in memory/overflow to file, Stream      * caching) cache. By default Camel will cache the Restlet input stream to      * support reading it multiple times to ensure Camel can retrieve all data      * from the stream. However you can set this option to true when you for      * example need to access the raw stream, such as streaming it directly to a      * file or other persistent store. DefaultRestletBinding will copy the      * request input stream into a stream cache and put it into message body if      * this option is false to support reading the stream multiple times.      */
DECL|field|disableStreamCache
specifier|private
name|Boolean
name|disableStreamCache
init|=
literal|false
decl_stmt|;
comment|/**      * To configure the port number for the restlet consumer routes. This allows      * to configure this once to reuse the same port for these consumers.      */
DECL|field|port
specifier|private
name|Integer
name|port
decl_stmt|;
comment|/**      * Whether to use synchronous Restlet Client for the producer. Setting this      * option to true can yield faster performance as it seems the Restlet      * synchronous Client works better.      */
DECL|field|synchronous
specifier|private
name|Boolean
name|synchronous
decl_stmt|;
comment|/**      * A list of converters to enable as full class name or simple class name.      * All the converters automatically registered are enabled if empty or null      */
DECL|field|enabledConverters
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|enabledConverters
decl_stmt|;
comment|/**      * Enable usage of global SSL context parameters.      */
DECL|field|useGlobalSslContextParameters
specifier|private
name|Boolean
name|useGlobalSslContextParameters
init|=
literal|false
decl_stmt|;
comment|/**      * To configure security using SSLContextParameters. The option is a      * org.apache.camel.support.jsse.SSLContextParameters type.      */
DECL|field|sslContextParameters
specifier|private
name|String
name|sslContextParameters
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
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
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|String
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy (String headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|String
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
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
DECL|method|getDisableStreamCache ()
specifier|public
name|Boolean
name|getDisableStreamCache
parameter_list|()
block|{
return|return
name|disableStreamCache
return|;
block|}
DECL|method|setDisableStreamCache (Boolean disableStreamCache)
specifier|public
name|void
name|setDisableStreamCache
parameter_list|(
name|Boolean
name|disableStreamCache
parameter_list|)
block|{
name|this
operator|.
name|disableStreamCache
operator|=
name|disableStreamCache
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
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
DECL|method|getSynchronous ()
specifier|public
name|Boolean
name|getSynchronous
parameter_list|()
block|{
return|return
name|synchronous
return|;
block|}
DECL|method|setSynchronous (Boolean synchronous)
specifier|public
name|void
name|setSynchronous
parameter_list|(
name|Boolean
name|synchronous
parameter_list|)
block|{
name|this
operator|.
name|synchronous
operator|=
name|synchronous
expr_stmt|;
block|}
DECL|method|getEnabledConverters ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getEnabledConverters
parameter_list|()
block|{
return|return
name|enabledConverters
return|;
block|}
DECL|method|setEnabledConverters (List<String> enabledConverters)
specifier|public
name|void
name|setEnabledConverters
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|enabledConverters
parameter_list|)
block|{
name|this
operator|.
name|enabledConverters
operator|=
name|enabledConverters
expr_stmt|;
block|}
DECL|method|getUseGlobalSslContextParameters ()
specifier|public
name|Boolean
name|getUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|useGlobalSslContextParameters
return|;
block|}
DECL|method|setUseGlobalSslContextParameters ( Boolean useGlobalSslContextParameters)
specifier|public
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
name|Boolean
name|useGlobalSslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|useGlobalSslContextParameters
operator|=
name|useGlobalSslContextParameters
expr_stmt|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|String
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
DECL|method|setSslContextParameters (String sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|String
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
block|}
end_class

end_unit

