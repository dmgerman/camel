begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sparkrest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sparkrest
package|;
end_package

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
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|RestApiConsumerFactory
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
name|spi
operator|.
name|annotations
operator|.
name|Component
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
name|DefaultComponent
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
name|FileUtil
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
name|HostUtils
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
name|camel
operator|.
name|util
operator|.
name|StringHelper
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
name|URISupport
import|;
end_import

begin_import
import|import
name|spark
operator|.
name|Service
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"spark-rest"
argument_list|)
DECL|class|SparkComponent
specifier|public
class|class
name|SparkComponent
extends|extends
name|DefaultComponent
implements|implements
name|RestConsumerFactory
implements|,
name|RestApiConsumerFactory
block|{
DECL|field|PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\{(.*?)\\}"
argument_list|)
decl_stmt|;
comment|/**      * SPARK instance for the component      */
DECL|field|sparkInstance
specifier|private
name|Service
name|sparkInstance
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"4567"
argument_list|)
DECL|field|port
specifier|private
name|int
name|port
init|=
literal|4567
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"0.0.0.0"
argument_list|)
DECL|field|ipAddress
specifier|private
name|String
name|ipAddress
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|minThreads
specifier|private
name|int
name|minThreads
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|maxThreads
specifier|private
name|int
name|maxThreads
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|timeOutMillis
specifier|private
name|int
name|timeOutMillis
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|keystoreFile
specifier|private
name|String
name|keystoreFile
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|keystorePassword
specifier|private
name|String
name|keystorePassword
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|truststoreFile
specifier|private
name|String
name|truststoreFile
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|truststorePassword
specifier|private
name|String
name|truststorePassword
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|sparkConfiguration
specifier|private
name|SparkConfiguration
name|sparkConfiguration
init|=
operator|new
name|SparkConfiguration
argument_list|()
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|sparkBinding
specifier|private
name|SparkBinding
name|sparkBinding
init|=
operator|new
name|DefaultSparkBinding
argument_list|()
decl_stmt|;
DECL|method|getSparkInstance ()
specifier|public
name|Service
name|getSparkInstance
parameter_list|()
block|{
return|return
name|sparkInstance
return|;
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
comment|/**      * Port number.      *<p/>      * Will by default use 4567      */
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
DECL|method|getIpAddress ()
specifier|public
name|String
name|getIpAddress
parameter_list|()
block|{
return|return
name|ipAddress
return|;
block|}
comment|/**      * Set the IP address that Spark should listen on. If not called the default address is '0.0.0.0'.      */
DECL|method|setIpAddress (String ipAddress)
specifier|public
name|void
name|setIpAddress
parameter_list|(
name|String
name|ipAddress
parameter_list|)
block|{
name|this
operator|.
name|ipAddress
operator|=
name|ipAddress
expr_stmt|;
block|}
DECL|method|getMinThreads ()
specifier|public
name|int
name|getMinThreads
parameter_list|()
block|{
return|return
name|minThreads
return|;
block|}
comment|/**      * Minimum number of threads in Spark thread-pool (shared globally)      */
DECL|method|setMinThreads (int minThreads)
specifier|public
name|void
name|setMinThreads
parameter_list|(
name|int
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
DECL|method|getMaxThreads ()
specifier|public
name|int
name|getMaxThreads
parameter_list|()
block|{
return|return
name|maxThreads
return|;
block|}
comment|/**      * Maximum number of threads in Spark thread-pool (shared globally)      */
DECL|method|setMaxThreads (int maxThreads)
specifier|public
name|void
name|setMaxThreads
parameter_list|(
name|int
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
DECL|method|getTimeOutMillis ()
specifier|public
name|int
name|getTimeOutMillis
parameter_list|()
block|{
return|return
name|timeOutMillis
return|;
block|}
comment|/**      * Thread idle timeout in millis where threads that has been idle for a longer period will be terminated from the thread pool      */
DECL|method|setTimeOutMillis (int timeOutMillis)
specifier|public
name|void
name|setTimeOutMillis
parameter_list|(
name|int
name|timeOutMillis
parameter_list|)
block|{
name|this
operator|.
name|timeOutMillis
operator|=
name|timeOutMillis
expr_stmt|;
block|}
DECL|method|getKeystoreFile ()
specifier|public
name|String
name|getKeystoreFile
parameter_list|()
block|{
return|return
name|keystoreFile
return|;
block|}
comment|/**      * Configures connection to be secure to use the keystore file      */
DECL|method|setKeystoreFile (String keystoreFile)
specifier|public
name|void
name|setKeystoreFile
parameter_list|(
name|String
name|keystoreFile
parameter_list|)
block|{
name|this
operator|.
name|keystoreFile
operator|=
name|keystoreFile
expr_stmt|;
block|}
DECL|method|getKeystorePassword ()
specifier|public
name|String
name|getKeystorePassword
parameter_list|()
block|{
return|return
name|keystorePassword
return|;
block|}
comment|/**      * Configures connection to be secure to use the keystore password      */
DECL|method|setKeystorePassword (String keystorePassword)
specifier|public
name|void
name|setKeystorePassword
parameter_list|(
name|String
name|keystorePassword
parameter_list|)
block|{
name|this
operator|.
name|keystorePassword
operator|=
name|keystorePassword
expr_stmt|;
block|}
DECL|method|getTruststoreFile ()
specifier|public
name|String
name|getTruststoreFile
parameter_list|()
block|{
return|return
name|truststoreFile
return|;
block|}
comment|/**      * Configures connection to be secure to use the truststore file      */
DECL|method|setTruststoreFile (String truststoreFile)
specifier|public
name|void
name|setTruststoreFile
parameter_list|(
name|String
name|truststoreFile
parameter_list|)
block|{
name|this
operator|.
name|truststoreFile
operator|=
name|truststoreFile
expr_stmt|;
block|}
DECL|method|getTruststorePassword ()
specifier|public
name|String
name|getTruststorePassword
parameter_list|()
block|{
return|return
name|truststorePassword
return|;
block|}
comment|/**      * Configures connection to be secure to use the truststore password      */
DECL|method|setTruststorePassword (String truststorePassword)
specifier|public
name|void
name|setTruststorePassword
parameter_list|(
name|String
name|truststorePassword
parameter_list|)
block|{
name|this
operator|.
name|truststorePassword
operator|=
name|truststorePassword
expr_stmt|;
block|}
DECL|method|getSparkConfiguration ()
specifier|public
name|SparkConfiguration
name|getSparkConfiguration
parameter_list|()
block|{
return|return
name|sparkConfiguration
return|;
block|}
comment|/**      * To use the shared SparkConfiguration      */
DECL|method|setSparkConfiguration (SparkConfiguration sparkConfiguration)
specifier|public
name|void
name|setSparkConfiguration
parameter_list|(
name|SparkConfiguration
name|sparkConfiguration
parameter_list|)
block|{
name|this
operator|.
name|sparkConfiguration
operator|=
name|sparkConfiguration
expr_stmt|;
block|}
DECL|method|getSparkBinding ()
specifier|public
name|SparkBinding
name|getSparkBinding
parameter_list|()
block|{
return|return
name|sparkBinding
return|;
block|}
comment|/**      * To use a custom SparkBinding to map to/from Camel message.      */
DECL|method|setSparkBinding (SparkBinding sparkBinding)
specifier|public
name|void
name|setSparkBinding
parameter_list|(
name|SparkBinding
name|sparkBinding
parameter_list|)
block|{
name|this
operator|.
name|sparkBinding
operator|=
name|sparkBinding
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
name|SparkConfiguration
name|config
init|=
name|getSparkConfiguration
argument_list|()
operator|.
name|copy
argument_list|()
decl_stmt|;
name|SparkEndpoint
name|answer
init|=
operator|new
name|SparkEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setSparkConfiguration
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setSparkBinding
argument_list|(
name|getSparkBinding
argument_list|()
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|answer
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|remaining
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid syntax. Must be spark-rest:verb:path"
argument_list|)
throw|;
block|}
name|String
name|verb
init|=
name|StringHelper
operator|.
name|before
argument_list|(
name|remaining
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|StringHelper
operator|.
name|after
argument_list|(
name|remaining
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setVerb
argument_list|(
name|verb
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
return|return
name|answer
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
name|sparkInstance
operator|=
name|Service
operator|.
name|ignite
argument_list|()
expr_stmt|;
if|if
condition|(
name|getPort
argument_list|()
operator|!=
literal|4567
condition|)
block|{
name|sparkInstance
operator|.
name|port
argument_list|(
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if no explicit port configured, then use port from rest configuration
name|RestConfiguration
name|config
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRestConfiguration
argument_list|(
literal|"spark-rest"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|int
name|port
init|=
name|config
operator|.
name|getPort
argument_list|()
decl_stmt|;
if|if
condition|(
name|port
operator|>
literal|0
condition|)
block|{
name|sparkInstance
operator|.
name|port
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|host
init|=
name|getIpAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|host
operator|!=
literal|null
condition|)
block|{
name|sparkInstance
operator|.
name|ipAddress
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if no explicit port configured, then use port from rest configuration
name|RestConfiguration
name|config
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRestConfiguration
argument_list|(
literal|"spark-rest"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|host
operator|=
name|config
operator|.
name|getHost
argument_list|()
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|host
argument_list|)
condition|)
block|{
if|if
condition|(
name|config
operator|.
name|getHostNameResolver
argument_list|()
operator|==
name|RestConfiguration
operator|.
name|RestHostNameResolver
operator|.
name|allLocalIp
condition|)
block|{
name|host
operator|=
literal|"0.0.0.0"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|config
operator|.
name|getHostNameResolver
argument_list|()
operator|==
name|RestConfiguration
operator|.
name|RestHostNameResolver
operator|.
name|localHostName
condition|)
block|{
name|host
operator|=
name|HostUtils
operator|.
name|getLocalHostName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|config
operator|.
name|getHostNameResolver
argument_list|()
operator|==
name|RestConfiguration
operator|.
name|RestHostNameResolver
operator|.
name|localIp
condition|)
block|{
name|host
operator|=
name|HostUtils
operator|.
name|getLocalIp
argument_list|()
expr_stmt|;
block|}
block|}
name|sparkInstance
operator|.
name|ipAddress
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keystoreFile
operator|!=
literal|null
operator|||
name|truststoreFile
operator|!=
literal|null
condition|)
block|{
name|sparkInstance
operator|.
name|secure
argument_list|(
name|keystoreFile
argument_list|,
name|keystorePassword
argument_list|,
name|truststoreFile
argument_list|,
name|truststorePassword
argument_list|)
expr_stmt|;
block|}
name|CamelSpark
operator|.
name|threadPool
argument_list|(
name|sparkInstance
argument_list|,
name|minThreads
argument_list|,
name|maxThreads
argument_list|,
name|timeOutMillis
argument_list|)
expr_stmt|;
comment|// configure component options
name|RestConfiguration
name|config
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRestConfiguration
argument_list|(
literal|"spark-rest"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// configure additional options on spark configuration
if|if
condition|(
name|config
operator|.
name|getComponentProperties
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|config
operator|.
name|getComponentProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setProperties
argument_list|(
name|sparkConfiguration
argument_list|,
name|config
operator|.
name|getComponentProperties
argument_list|()
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
name|sparkInstance
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (CamelContext camelContext, Processor processor, String verb, String basePath, String uriTemplate, String consumes, String produces, RestConfiguration configuration, Map<String, Object> parameters)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|verb
parameter_list|,
name|String
name|basePath
parameter_list|,
name|String
name|uriTemplate
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|,
name|RestConfiguration
name|configuration
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
return|return
name|doCreateConsumer
argument_list|(
name|camelContext
argument_list|,
name|processor
argument_list|,
name|verb
argument_list|,
name|basePath
argument_list|,
name|uriTemplate
argument_list|,
name|consumes
argument_list|,
name|produces
argument_list|,
name|configuration
argument_list|,
name|parameters
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createApiConsumer (CamelContext camelContext, Processor processor, String contextPath, RestConfiguration configuration, Map<String, Object> parameters)
specifier|public
name|Consumer
name|createApiConsumer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|contextPath
parameter_list|,
name|RestConfiguration
name|configuration
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
comment|// reuse the createConsumer method we already have. The api need to use GET and match on uri prefix
return|return
name|doCreateConsumer
argument_list|(
name|camelContext
argument_list|,
name|processor
argument_list|,
literal|"get"
argument_list|,
name|contextPath
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|configuration
argument_list|,
name|parameters
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|doCreateConsumer (CamelContext camelContext, Processor processor, String verb, String basePath, String uriTemplate, String consumes, String produces, RestConfiguration configuration, Map<String, Object> parameters, boolean api)
name|Consumer
name|doCreateConsumer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|verb
parameter_list|,
name|String
name|basePath
parameter_list|,
name|String
name|uriTemplate
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|,
name|RestConfiguration
name|configuration
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|boolean
name|api
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|path
init|=
name|basePath
decl_stmt|;
if|if
condition|(
name|uriTemplate
operator|!=
literal|null
condition|)
block|{
comment|// make sure to avoid double slashes
if|if
condition|(
name|uriTemplate
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|+
name|uriTemplate
expr_stmt|;
block|}
else|else
block|{
name|path
operator|=
name|path
operator|+
literal|"/"
operator|+
name|uriTemplate
expr_stmt|;
block|}
block|}
name|path
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|RestConfiguration
name|config
init|=
name|configuration
decl_stmt|;
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
name|config
operator|=
name|camelContext
operator|.
name|getRestConfiguration
argument_list|(
literal|"spark-rest"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|consumes
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
literal|"accept"
argument_list|,
name|consumes
argument_list|)
expr_stmt|;
block|}
comment|// setup endpoint options
if|if
condition|(
name|config
operator|.
name|getEndpointProperties
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|config
operator|.
name|getEndpointProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|map
operator|.
name|putAll
argument_list|(
name|config
operator|.
name|getEndpointProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|path
argument_list|)
condition|)
block|{
comment|// spark-rest uses :name syntax instead of {name} so we need to replace those
name|Matcher
name|matcher
init|=
name|PATTERN
operator|.
name|matcher
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|path
operator|=
name|matcher
operator|.
name|replaceAll
argument_list|(
literal|":$1"
argument_list|)
expr_stmt|;
block|}
comment|// prefix path with context-path if configured in rest-dsl configuration
name|String
name|contextPath
init|=
name|config
operator|.
name|getContextPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|contextPath
argument_list|)
condition|)
block|{
name|contextPath
operator|=
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
name|contextPath
argument_list|)
expr_stmt|;
name|contextPath
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|contextPath
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|contextPath
argument_list|)
condition|)
block|{
name|path
operator|=
name|contextPath
operator|+
literal|"/"
operator|+
name|path
expr_stmt|;
block|}
block|}
name|String
name|url
decl_stmt|;
if|if
condition|(
name|api
condition|)
block|{
name|url
operator|=
literal|"spark-rest:%s:%s?matchOnUriPrefix=true"
expr_stmt|;
block|}
else|else
block|{
name|url
operator|=
literal|"spark-rest:%s:%s"
expr_stmt|;
block|}
name|url
operator|=
name|String
operator|.
name|format
argument_list|(
name|url
argument_list|,
name|verb
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|String
name|query
init|=
name|URISupport
operator|.
name|createQueryString
argument_list|(
name|map
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|query
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|url
operator|=
name|url
operator|+
literal|"?"
operator|+
name|query
expr_stmt|;
block|}
comment|// get the endpoint
name|SparkEndpoint
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|url
argument_list|,
name|SparkEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|camelContext
argument_list|,
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// configure consumer properties
name|Consumer
name|consumer
init|=
name|endpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|isEnableCORS
argument_list|()
condition|)
block|{
comment|// if CORS is enabled then configure that on the spark consumer
if|if
condition|(
name|config
operator|.
name|getConsumerProperties
argument_list|()
operator|==
literal|null
condition|)
block|{
name|config
operator|.
name|setConsumerProperties
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|config
operator|.
name|getConsumerProperties
argument_list|()
operator|.
name|put
argument_list|(
literal|"enableCors"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getConsumerProperties
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|config
operator|.
name|getConsumerProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setProperties
argument_list|(
name|camelContext
argument_list|,
name|consumer
argument_list|,
name|config
operator|.
name|getConsumerProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|consumer
return|;
block|}
block|}
end_class

end_unit

