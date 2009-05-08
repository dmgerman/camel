begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quickfix
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quickfix
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|ExchangePattern
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
name|Service
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|DefaultResourceLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|ResourceLoader
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Application
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|ConfigError
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|FileStoreFactory
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|MessageStoreFactory
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|ScreenLogFactory
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|SessionID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|SessionSettings
import|;
end_import

begin_comment
comment|/**  * QuickfixEndpoint is the common class for quickfix endpoints  *<p/>  * Usage example:  *<p/>  * from("quickfix-server:acceptor.cfg[?params]").to("someBean", "someMethod").to("quickfix-client:initiator.cfg[?params]");  *<p/>  *  * @author Anton Arhipov  * @see org.apache.camel.quickfix.QuickfixInitiator  * @see org.apache.camel.quickfix.QuickfixAcceptor  */
end_comment

begin_class
DECL|class|QuickfixEndpoint
specifier|public
specifier|abstract
class|class
name|QuickfixEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|Service
block|{
DECL|field|strict
specifier|private
name|boolean
name|strict
decl_stmt|;
DECL|field|sessionID
specifier|private
name|SessionID
name|sessionID
decl_stmt|;
DECL|field|configuration
specifier|private
name|String
name|configuration
decl_stmt|;
DECL|field|logFactory
specifier|private
name|LogFactory
name|logFactory
decl_stmt|;
DECL|field|messageStoreFactory
specifier|private
name|MessageStoreFactory
name|messageStoreFactory
decl_stmt|;
DECL|field|processor
specifier|private
name|Processor
name|processor
init|=
operator|new
name|QuickfixProcessor
argument_list|()
decl_stmt|;
DECL|field|application
specifier|private
name|QuickfixApplication
name|application
decl_stmt|;
DECL|method|QuickfixEndpoint (String uri, CamelContext context, String configuration)
specifier|public
name|QuickfixEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|String
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
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
DECL|method|onMessage (Message message)
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|message
argument_list|)
decl_stmt|;
try|try
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createExchange (Message message)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|Exchange
name|answer
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|answer
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|// for initiator
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|QuickfixProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|// for acceptor
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
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
return|return
operator|new
name|QuickfixConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|getSessionID ()
specifier|public
name|SessionID
name|getSessionID
parameter_list|()
block|{
return|return
name|sessionID
return|;
comment|// can find Session using
comment|// Session.lookupSession(sessionID);
block|}
DECL|method|setSessionID (SessionID sessionID)
specifier|public
name|void
name|setSessionID
parameter_list|(
name|SessionID
name|sessionID
parameter_list|)
block|{
name|this
operator|.
name|sessionID
operator|=
name|sessionID
expr_stmt|;
comment|// can find Session using
comment|// Session.lookupSession(sessionID);
block|}
DECL|method|getSession ()
specifier|public
name|Session
name|getSession
parameter_list|()
block|{
return|return
name|Session
operator|.
name|lookupSession
argument_list|(
name|sessionID
argument_list|)
return|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|Session
name|session
init|=
name|Session
operator|.
name|lookupSession
argument_list|(
name|sessionID
argument_list|)
decl_stmt|;
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|session
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|Resource
name|configResource
init|=
name|getResource
argument_list|()
decl_stmt|;
name|InputStream
name|inputStream
init|=
name|configResource
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|inputStream
argument_list|,
literal|"Could not load "
operator|+
name|configuration
argument_list|)
expr_stmt|;
name|SessionSettings
name|settings
init|=
operator|new
name|SessionSettings
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
name|MessageStoreFactory
name|storeFactory
init|=
name|createMessageStoreFactory
argument_list|(
name|settings
argument_list|)
decl_stmt|;
name|LogFactory
name|logFactory
init|=
name|createLogFactory
argument_list|(
name|settings
argument_list|)
decl_stmt|;
name|createApplication
argument_list|()
expr_stmt|;
comment|/*          * ClassLoader ccl = Thread.currentThread().getContextClassLoader(); try          * {          * Thread.currentThread().setContextClassLoader(getClass().getClassLoader          * ());          */
name|start
argument_list|(
name|application
argument_list|,
name|storeFactory
argument_list|,
name|settings
argument_list|,
name|logFactory
argument_list|)
expr_stmt|;
comment|/*          * } finally { Thread.currentThread().setContextClassLoader(ccl); }          */
block|}
DECL|method|start (Application application, MessageStoreFactory storeFactory, SessionSettings settings, LogFactory logFactory)
specifier|protected
specifier|abstract
name|void
name|start
parameter_list|(
name|Application
name|application
parameter_list|,
name|MessageStoreFactory
name|storeFactory
parameter_list|,
name|SessionSettings
name|settings
parameter_list|,
name|LogFactory
name|logFactory
parameter_list|)
throws|throws
name|ConfigError
function_decl|;
DECL|method|createApplication ()
specifier|private
name|void
name|createApplication
parameter_list|()
block|{
if|if
condition|(
name|application
operator|==
literal|null
condition|)
block|{
name|application
operator|=
operator|new
name|QuickfixApplication
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|application
operator|.
name|setEndpoint
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createLogFactory (SessionSettings settings)
specifier|private
name|LogFactory
name|createLogFactory
parameter_list|(
name|SessionSettings
name|settings
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|logFactory
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|strict
condition|)
block|{
comment|// if the logFactory is still not set and we are fine to use
comment|// non-strict logging,
comment|// then the screen logging factory will be used by default
name|logFactory
operator|=
operator|new
name|ScreenLogFactory
argument_list|(
name|settings
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The strict option is switched on. "
operator|+
literal|"You should either inject the required logging factory via spring context, "
operator|+
literal|"or specify the logging factory parameters via endpoint URI"
argument_list|)
throw|;
block|}
block|}
return|return
name|logFactory
return|;
block|}
DECL|method|createMessageStoreFactory (SessionSettings settings)
specifier|private
name|MessageStoreFactory
name|createMessageStoreFactory
parameter_list|(
name|SessionSettings
name|settings
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|messageStoreFactory
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|strict
condition|)
block|{
name|messageStoreFactory
operator|=
operator|new
name|FileStoreFactory
argument_list|(
name|settings
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The strict option is switched on. "
operator|+
literal|"You should either inject the required logging factory via spring context, "
operator|+
literal|"or specify the logging factory parameters via endpoint URI"
argument_list|)
throw|;
block|}
block|}
return|return
name|messageStoreFactory
return|;
block|}
DECL|method|setLogFactory (LogFactory logFactory)
specifier|public
name|void
name|setLogFactory
parameter_list|(
name|LogFactory
name|logFactory
parameter_list|)
block|{
name|this
operator|.
name|logFactory
operator|=
name|logFactory
expr_stmt|;
block|}
DECL|method|setMessageStoreFactory (MessageStoreFactory messageStoreFactory)
specifier|public
name|void
name|setMessageStoreFactory
parameter_list|(
name|MessageStoreFactory
name|messageStoreFactory
parameter_list|)
block|{
name|this
operator|.
name|messageStoreFactory
operator|=
name|messageStoreFactory
expr_stmt|;
block|}
DECL|method|setStrict (boolean strict)
specifier|public
name|void
name|setStrict
parameter_list|(
name|boolean
name|strict
parameter_list|)
block|{
name|this
operator|.
name|strict
operator|=
name|strict
expr_stmt|;
block|}
DECL|method|getResource ()
specifier|private
name|Resource
name|getResource
parameter_list|()
block|{
name|ResourceLoader
name|loader
init|=
operator|new
name|DefaultResourceLoader
argument_list|()
decl_stmt|;
return|return
name|loader
operator|.
name|getResource
argument_list|(
name|this
operator|.
name|configuration
argument_list|)
return|;
block|}
block|}
end_class

end_unit

