begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quickfixj
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quickfixj
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
name|net
operator|.
name|URISyntaxException
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
name|UnsafeUriCharactersEncoder
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
name|MessageFactory
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
name|SessionSettings
import|;
end_import

begin_class
DECL|class|QuickfixjComponent
specifier|public
class|class
name|QuickfixjComponent
extends|extends
name|DefaultComponent
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
name|QuickfixjComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|engineInstancesLock
specifier|private
specifier|final
name|Object
name|engineInstancesLock
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
DECL|field|engines
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|QuickfixjEngine
argument_list|>
name|engines
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|QuickfixjEngine
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|endpoints
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|QuickfixjEndpoint
argument_list|>
name|endpoints
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|QuickfixjEndpoint
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|messageStoreFactory
specifier|private
name|MessageStoreFactory
name|messageStoreFactory
decl_stmt|;
DECL|field|logFactory
specifier|private
name|LogFactory
name|logFactory
decl_stmt|;
DECL|field|messageFactory
specifier|private
name|MessageFactory
name|messageFactory
decl_stmt|;
DECL|field|forcedShutdown
specifier|private
name|boolean
name|forcedShutdown
decl_stmt|;
DECL|field|configurations
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|QuickfixjConfiguration
argument_list|>
name|configurations
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|QuickfixjConfiguration
argument_list|>
argument_list|()
decl_stmt|;
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
comment|// Look up the engine instance based on the settings file ("remaining")
name|QuickfixjEngine
name|engine
decl_stmt|;
synchronized|synchronized
init|(
name|engineInstancesLock
init|)
block|{
name|QuickfixjEndpoint
name|endpoint
init|=
name|endpoints
operator|.
name|get
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
name|engine
operator|=
name|engines
operator|.
name|get
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
if|if
condition|(
name|engine
operator|==
literal|null
condition|)
block|{
name|QuickfixjConfiguration
name|configuration
init|=
name|configurations
operator|.
name|get
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|SessionSettings
name|settings
init|=
name|configuration
operator|.
name|createSessionSettings
argument_list|()
decl_stmt|;
name|engine
operator|=
operator|new
name|QuickfixjEngine
argument_list|(
name|uri
argument_list|,
name|settings
argument_list|,
name|forcedShutdown
argument_list|,
name|messageStoreFactory
argument_list|,
name|logFactory
argument_list|,
name|messageFactory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|engine
operator|=
operator|new
name|QuickfixjEngine
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|forcedShutdown
argument_list|,
name|messageStoreFactory
argument_list|,
name|logFactory
argument_list|,
name|messageFactory
argument_list|)
expr_stmt|;
block|}
name|engines
operator|.
name|put
argument_list|(
name|remaining
argument_list|,
name|engine
argument_list|)
expr_stmt|;
if|if
condition|(
name|isStarted
argument_list|()
condition|)
block|{
name|startQuickfixjEngine
argument_list|(
name|engine
argument_list|)
expr_stmt|;
block|}
block|}
name|endpoint
operator|=
operator|new
name|QuickfixjEndpoint
argument_list|(
name|engine
argument_list|,
name|uri
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|engine
operator|.
name|addEventListener
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|endpoints
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
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
synchronized|synchronized
init|(
name|engineInstancesLock
init|)
block|{
for|for
control|(
name|QuickfixjEngine
name|engine
range|:
name|engines
operator|.
name|values
argument_list|()
control|)
block|{
name|startQuickfixjEngine
argument_list|(
name|engine
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|startQuickfixjEngine (QuickfixjEngine engine)
specifier|private
name|void
name|startQuickfixjEngine
parameter_list|(
name|QuickfixjEngine
name|engine
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting QuickFIX/J engine: uri="
argument_list|,
name|engine
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|engine
operator|.
name|start
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
synchronized|synchronized
init|(
name|engineInstancesLock
init|)
block|{
for|for
control|(
name|QuickfixjEngine
name|engine
range|:
name|engines
operator|.
name|values
argument_list|()
control|)
block|{
name|engine
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|// Test Support
DECL|method|getEngines ()
name|Map
argument_list|<
name|String
argument_list|,
name|QuickfixjEngine
argument_list|>
name|getEngines
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|engines
argument_list|)
return|;
block|}
DECL|method|setMessageFactory (MessageFactory messageFactory)
specifier|public
name|void
name|setMessageFactory
parameter_list|(
name|MessageFactory
name|messageFactory
parameter_list|)
block|{
name|this
operator|.
name|messageFactory
operator|=
name|messageFactory
expr_stmt|;
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
DECL|method|setForcedShutdown (boolean forcedShutdown)
specifier|public
name|void
name|setForcedShutdown
parameter_list|(
name|boolean
name|forcedShutdown
parameter_list|)
block|{
name|this
operator|.
name|forcedShutdown
operator|=
name|forcedShutdown
expr_stmt|;
block|}
DECL|method|getConfigurations ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|QuickfixjConfiguration
argument_list|>
name|getConfigurations
parameter_list|()
block|{
return|return
name|configurations
return|;
block|}
DECL|method|setConfigurations (Map<String, QuickfixjConfiguration> configurations)
specifier|public
name|void
name|setConfigurations
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|QuickfixjConfiguration
argument_list|>
name|configurations
parameter_list|)
block|{
name|this
operator|.
name|configurations
operator|=
name|configurations
expr_stmt|;
block|}
block|}
end_class

end_unit

