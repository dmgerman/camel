begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|StartupListener
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
annotation|@
name|Component
argument_list|(
literal|"quickfix"
argument_list|)
DECL|class|QuickfixjComponent
specifier|public
class|class
name|QuickfixjComponent
extends|extends
name|DefaultComponent
implements|implements
name|StartupListener
block|{
DECL|field|PARAMETER_LAZY_CREATE_ENGINE
specifier|private
specifier|static
specifier|final
name|String
name|PARAMETER_LAZY_CREATE_ENGINE
init|=
literal|"lazyCreateEngine"
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
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|provisionalEngines
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|QuickfixjEngine
argument_list|>
name|provisionalEngines
init|=
operator|new
name|HashMap
argument_list|<>
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
argument_list|<>
argument_list|()
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
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|messageStoreFactory
specifier|private
name|MessageStoreFactory
name|messageStoreFactory
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|logFactory
specifier|private
name|LogFactory
name|logFactory
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|messageFactory
specifier|private
name|MessageFactory
name|messageFactory
decl_stmt|;
DECL|field|lazyCreateEngines
specifier|private
name|boolean
name|lazyCreateEngines
decl_stmt|;
DECL|method|QuickfixjComponent ()
specifier|public
name|QuickfixjComponent
parameter_list|()
block|{     }
DECL|method|QuickfixjComponent (CamelContext context)
specifier|public
name|QuickfixjComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
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
name|engine
operator|=
name|provisionalEngines
operator|.
name|get
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
block|}
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
name|SessionSettings
name|settings
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|settings
operator|=
name|configuration
operator|.
name|createSessionSettings
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|settings
operator|=
name|QuickfixjEngine
operator|.
name|loadSettings
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
block|}
name|Boolean
name|lazyCreateEngineForEndpoint
init|=
name|super
operator|.
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
name|PARAMETER_LAZY_CREATE_ENGINE
argument_list|,
name|Boolean
operator|.
name|TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|lazyCreateEngineForEndpoint
operator|==
literal|null
condition|)
block|{
name|lazyCreateEngineForEndpoint
operator|=
name|isLazyCreateEngines
argument_list|()
expr_stmt|;
block|}
name|engine
operator|=
operator|new
name|QuickfixjEngine
argument_list|(
name|uri
argument_list|,
name|settings
argument_list|,
name|messageStoreFactory
argument_list|,
name|logFactory
argument_list|,
name|messageFactory
argument_list|,
name|lazyCreateEngineForEndpoint
argument_list|)
expr_stmt|;
comment|// only start engine if CamelContext is already started, otherwise the engines gets started
comment|// automatic later when CamelContext has been started using the StartupListener
if|if
condition|(
name|getCamelContext
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
name|startQuickfixjEngine
argument_list|(
name|engine
argument_list|)
expr_stmt|;
name|engines
operator|.
name|put
argument_list|(
name|remaining
argument_list|,
name|engine
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// engines to be started later
name|provisionalEngines
operator|.
name|put
argument_list|(
name|remaining
argument_list|,
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
name|endpoint
operator|.
name|setConfigurationName
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setLazyCreateEngine
argument_list|(
name|engine
operator|.
name|isLazy
argument_list|()
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
comment|// we defer starting quickfix engines till the onCamelContextStarted callback
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
comment|// stop engines when stopping component
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
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
comment|// cleanup when shutting down
name|engines
operator|.
name|clear
argument_list|()
expr_stmt|;
name|provisionalEngines
operator|.
name|clear
argument_list|()
expr_stmt|;
name|endpoints
operator|.
name|clear
argument_list|()
expr_stmt|;
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
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
if|if
condition|(
operator|!
name|engine
operator|.
name|isLazy
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Starting QuickFIX/J engine: {}"
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
else|else
block|{
name|log
operator|.
name|info
argument_list|(
literal|"QuickFIX/J engine: {} will start lazily"
argument_list|,
name|engine
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
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
comment|// Test Support
DECL|method|getProvisionalEngines ()
name|Map
argument_list|<
name|String
argument_list|,
name|QuickfixjEngine
argument_list|>
name|getProvisionalEngines
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|provisionalEngines
argument_list|)
return|;
block|}
comment|/**      * To use the given MessageFactory      */
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
comment|/**      * To use the given LogFactory      */
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
comment|/**      * To use the given MessageStoreFactory      */
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
comment|/**      * To use the given map of pre configured QuickFix configurations mapped to the key      */
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
DECL|method|isLazyCreateEngines ()
specifier|public
name|boolean
name|isLazyCreateEngines
parameter_list|()
block|{
return|return
name|this
operator|.
name|lazyCreateEngines
return|;
block|}
comment|/**      * If set to<code>true</code>, the engines will be created and started when needed (when first message      * is send)      */
DECL|method|setLazyCreateEngines (boolean lazyCreateEngines)
specifier|public
name|void
name|setLazyCreateEngines
parameter_list|(
name|boolean
name|lazyCreateEngines
parameter_list|)
block|{
name|this
operator|.
name|lazyCreateEngines
operator|=
name|lazyCreateEngines
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onCamelContextStarted (CamelContext camelContext, boolean alreadyStarted)
specifier|public
name|void
name|onCamelContextStarted
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|boolean
name|alreadyStarted
parameter_list|)
throws|throws
name|Exception
block|{
comment|// only start quickfix engines when CamelContext have finished starting
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
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|QuickfixjEngine
argument_list|>
name|entry
range|:
name|provisionalEngines
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|startQuickfixjEngine
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|engines
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|provisionalEngines
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

