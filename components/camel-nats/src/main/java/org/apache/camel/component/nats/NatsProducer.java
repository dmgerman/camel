begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nats
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nats
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|TimeoutException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
import|;
end_import

begin_import
import|import
name|io
operator|.
name|nats
operator|.
name|client
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|io
operator|.
name|nats
operator|.
name|client
operator|.
name|Connection
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|io
operator|.
name|nats
operator|.
name|client
operator|.
name|Nats
import|;
end_import

begin_import
import|import
name|io
operator|.
name|nats
operator|.
name|client
operator|.
name|Options
import|;
end_import

begin_import
import|import
name|io
operator|.
name|nats
operator|.
name|client
operator|.
name|Options
operator|.
name|Builder
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
name|impl
operator|.
name|DefaultProducer
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

begin_class
DECL|class|NatsProducer
specifier|public
class|class
name|NatsProducer
extends|extends
name|DefaultProducer
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
name|NatsProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|connection
specifier|private
name|Connection
name|connection
decl_stmt|;
DECL|method|NatsProducer (NatsEndpoint endpoint)
specifier|public
name|NatsProducer
parameter_list|(
name|NatsEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|NatsEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|NatsEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|NatsConfiguration
name|config
init|=
name|getEndpoint
argument_list|()
operator|.
name|getNatsConfiguration
argument_list|()
decl_stmt|;
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Publishing to topic: {}"
argument_list|,
name|config
operator|.
name|getTopic
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|config
operator|.
name|getReplySubject
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|replySubject
init|=
name|config
operator|.
name|getReplySubject
argument_list|()
decl_stmt|;
name|connection
operator|.
name|publish
argument_list|(
name|config
operator|.
name|getTopic
argument_list|()
argument_list|,
name|replySubject
argument_list|,
name|body
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|connection
operator|.
name|publish
argument_list|(
name|config
operator|.
name|getTopic
argument_list|()
argument_list|,
name|body
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting Nats Producer"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Getting Nats Connection"
argument_list|)
expr_stmt|;
name|connection
operator|=
name|getConnection
argument_list|()
expr_stmt|;
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stopping Nats Producer"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Closing Nats Connection"
argument_list|)
expr_stmt|;
if|if
condition|(
name|connection
operator|!=
literal|null
operator|&&
operator|!
name|connection
operator|.
name|getStatus
argument_list|()
operator|.
name|equals
argument_list|(
name|Status
operator|.
name|CLOSED
argument_list|)
condition|)
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getNatsConfiguration
argument_list|()
operator|.
name|isFlushConnection
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Flushing Nats Connection"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|flush
argument_list|(
name|Duration
operator|.
name|ofMillis
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getNatsConfiguration
argument_list|()
operator|.
name|getFlushTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getConnection ()
specifier|private
name|Connection
name|getConnection
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IllegalArgumentException
throws|,
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|Builder
name|builder
init|=
name|getEndpoint
argument_list|()
operator|.
name|getNatsConfiguration
argument_list|()
operator|.
name|createOptions
argument_list|()
decl_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getNatsConfiguration
argument_list|()
operator|.
name|getSslContextParameters
argument_list|()
operator|!=
literal|null
operator|&&
name|getEndpoint
argument_list|()
operator|.
name|getNatsConfiguration
argument_list|()
operator|.
name|isSecure
argument_list|()
condition|)
block|{
name|SSLContext
name|sslCtx
init|=
name|getEndpoint
argument_list|()
operator|.
name|getNatsConfiguration
argument_list|()
operator|.
name|getSslContextParameters
argument_list|()
operator|.
name|createSSLContext
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|builder
operator|.
name|sslContext
argument_list|(
name|sslCtx
argument_list|)
expr_stmt|;
block|}
name|Options
name|options
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|connection
operator|=
name|Nats
operator|.
name|connect
argument_list|(
name|options
argument_list|)
expr_stmt|;
return|return
name|connection
return|;
block|}
block|}
end_class

end_unit

