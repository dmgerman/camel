begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.asterisk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|asterisk
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
name|asteriskjava
operator|.
name|manager
operator|.
name|AuthenticationFailedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|ManagerEventListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|TimeoutException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|action
operator|.
name|ManagerAction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|event
operator|.
name|ManagerEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|response
operator|.
name|ManagerResponse
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
comment|/**  * Represents a Asterisk endpoint.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"asterisk"
argument_list|,
name|title
operator|=
literal|"Asterisk"
argument_list|,
name|syntax
operator|=
literal|"asterisk:name"
argument_list|,
name|consumerClass
operator|=
name|AsteriskConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"voip"
argument_list|)
DECL|class|AsteriskEndpoint
specifier|public
class|class
name|AsteriskEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
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
name|AsteriskProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|enum|ActionsEnum
specifier|protected
enum|enum
name|ActionsEnum
block|{
DECL|enumConstant|QUEUE_STATUS
DECL|enumConstant|SIP_PEERS
DECL|enumConstant|EXTENSION_STATE
name|QUEUE_STATUS
block|,
name|SIP_PEERS
block|,
name|EXTENSION_STATE
block|;     }
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Name of component"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|UriParam
DECL|field|hostname
specifier|private
name|String
name|hostname
decl_stmt|;
annotation|@
name|UriParam
DECL|field|action
specifier|private
name|String
name|action
decl_stmt|;
annotation|@
name|UriParam
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|UriParam
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|field|asteriskConnection
specifier|private
name|AsteriskConnection
name|asteriskConnection
decl_stmt|;
DECL|method|AsteriskEndpoint (String uri, AsteriskComponent component)
specifier|public
name|AsteriskEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|AsteriskComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
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
name|asteriskConnection
operator|=
operator|new
name|AsteriskConnection
argument_list|(
name|hostname
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
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
comment|// do not exist disconnect operation!!!
name|asteriskConnection
operator|=
literal|null
expr_stmt|;
block|}
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
name|action
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Missing required action parameter"
argument_list|)
throw|;
block|}
comment|// validate action value
name|ActionsEnum
operator|.
name|valueOf
argument_list|(
name|action
argument_list|)
expr_stmt|;
return|return
operator|new
name|AsteriskProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
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
return|return
operator|new
name|AsteriskConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
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
DECL|method|addListener (ManagerEventListener listener)
specifier|public
name|void
name|addListener
parameter_list|(
name|ManagerEventListener
name|listener
parameter_list|)
throws|throws
name|CamelAsteriskException
block|{
name|asteriskConnection
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
DECL|method|login ()
specifier|public
name|void
name|login
parameter_list|()
throws|throws
name|IllegalStateException
throws|,
name|IOException
throws|,
name|AuthenticationFailedException
throws|,
name|TimeoutException
throws|,
name|CamelAsteriskException
block|{
name|asteriskConnection
operator|.
name|login
argument_list|()
expr_stmt|;
block|}
DECL|method|logoff ()
specifier|public
name|void
name|logoff
parameter_list|()
throws|throws
name|CamelAsteriskException
block|{
name|asteriskConnection
operator|.
name|logoff
argument_list|()
expr_stmt|;
block|}
DECL|method|createExchange (ManagerEvent event)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ManagerEvent
name|event
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|super
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|AsteriskConstants
operator|.
name|EVENT_NAME
argument_list|,
name|event
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|event
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|sendAction (ManagerAction action)
specifier|public
name|ManagerResponse
name|sendAction
parameter_list|(
name|ManagerAction
name|action
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|IllegalStateException
throws|,
name|IOException
throws|,
name|TimeoutException
block|{
return|return
name|asteriskConnection
operator|.
name|sendAction
argument_list|(
name|action
argument_list|)
return|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
comment|/**      * AsteriskServer username      *       * @param host      */
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
comment|/**      * AsteriskServer password      *       * @param host      */
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getAction ()
specifier|public
name|String
name|getAction
parameter_list|()
block|{
return|return
name|action
return|;
block|}
comment|/**      * action      *       * @param host      */
DECL|method|setAction (String action)
specifier|public
name|void
name|setAction
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
block|}
DECL|method|getHostname ()
specifier|public
name|String
name|getHostname
parameter_list|()
block|{
return|return
name|hostname
return|;
block|}
comment|/**      * Hostname       *       * @param hostname      */
DECL|method|setHostname (String hostname)
specifier|public
name|void
name|setHostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|hostname
expr_stmt|;
block|}
comment|/**      * name       *       * @return      */
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
block|}
end_class

end_unit

