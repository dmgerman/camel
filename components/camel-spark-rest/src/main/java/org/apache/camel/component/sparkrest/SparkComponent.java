begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|URISupport
import|;
end_import

begin_import
import|import
name|spark
operator|.
name|Spark
import|;
end_import

begin_import
import|import
name|spark
operator|.
name|SparkBase
import|;
end_import

begin_class
DECL|class|SparkComponent
specifier|public
class|class
name|SparkComponent
extends|extends
name|UriEndpointComponent
implements|implements
name|RestConsumerFactory
block|{
DECL|field|pattern
specifier|private
specifier|final
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\{(.*?)\\}"
argument_list|)
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
init|=
name|SparkBase
operator|.
name|SPARK_DEFAULT_PORT
decl_stmt|;
DECL|field|sparkConfiguration
specifier|private
name|SparkConfiguration
name|sparkConfiguration
init|=
operator|new
name|SparkConfiguration
argument_list|()
decl_stmt|;
DECL|field|sparkBinding
specifier|private
name|SparkBinding
name|sparkBinding
init|=
operator|new
name|DefaultSparkBinding
argument_list|()
decl_stmt|;
DECL|method|SparkComponent ()
specifier|public
name|SparkComponent
parameter_list|()
block|{
name|super
argument_list|(
name|SparkEndpoint
operator|.
name|class
argument_list|)
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
name|getSparkConfiguration
argument_list|()
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
name|ObjectHelper
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
name|ObjectHelper
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
if|if
condition|(
name|getPort
argument_list|()
operator|!=
name|SparkBase
operator|.
name|SPARK_DEFAULT_PORT
condition|)
block|{
name|Spark
operator|.
name|setPort
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
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|!=
literal|null
operator|&&
operator|(
name|config
operator|.
name|getComponent
argument_list|()
operator|==
literal|null
operator|||
name|config
operator|.
name|getComponent
argument_list|()
operator|.
name|equals
argument_list|(
literal|"spark-rest"
argument_list|)
operator|)
condition|)
block|{
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
name|Spark
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// configure component options
name|RestConfiguration
name|config
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRestConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|!=
literal|null
operator|&&
operator|(
name|config
operator|.
name|getComponent
argument_list|()
operator|==
literal|null
operator|||
name|config
operator|.
name|getComponent
argument_list|()
operator|.
name|equals
argument_list|(
literal|"spark-rest"
argument_list|)
operator|)
condition|)
block|{
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
name|Spark
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (CamelContext camelContext, Processor processor, String verb, String path, String consumes, String produces, Map<String, Object> parameters)
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
name|path
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
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
name|pattern
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
name|String
name|uri
init|=
name|String
operator|.
name|format
argument_list|(
literal|"spark-rest:%s:%s"
argument_list|,
name|verb
argument_list|,
name|path
argument_list|)
decl_stmt|;
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
comment|// build query string, and append any endpoint configuration properties
name|RestConfiguration
name|config
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRestConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|!=
literal|null
operator|&&
operator|(
name|config
operator|.
name|getComponent
argument_list|()
operator|==
literal|null
operator|||
name|config
operator|.
name|getComponent
argument_list|()
operator|.
name|equals
argument_list|(
literal|"spark-rest"
argument_list|)
operator|)
condition|)
block|{
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
block|}
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
name|String
name|url
init|=
name|uri
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
operator|!=
literal|null
operator|&&
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

