begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quickfixj.springboot
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
name|Map
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
name|component
operator|.
name|quickfixj
operator|.
name|QuickfixjConfiguration
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
comment|/**  * The quickfix component allows to send Financial Interchange (FIX) messages to  * the QuickFix engine.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.quickfix"
argument_list|)
DECL|class|QuickfixjComponentConfiguration
specifier|public
class|class
name|QuickfixjComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the quickfix component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To use the given MessageFactory. The option is a quickfix.MessageFactory      * type.      */
DECL|field|messageFactory
specifier|private
name|String
name|messageFactory
decl_stmt|;
comment|/**      * To use the given LogFactory. The option is a quickfix.LogFactory type.      */
DECL|field|logFactory
specifier|private
name|String
name|logFactory
decl_stmt|;
comment|/**      * To use the given MessageStoreFactory. The option is a      * quickfix.MessageStoreFactory type.      */
DECL|field|messageStoreFactory
specifier|private
name|String
name|messageStoreFactory
decl_stmt|;
comment|/**      * To use the given map of pre configured QuickFix configurations mapped to      * the key      */
DECL|field|configurations
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|QuickfixjConfiguration
argument_list|>
name|configurations
decl_stmt|;
comment|/**      * If set to true, the engines will be created and started when needed (when      * first message is send)      */
DECL|field|lazyCreateEngines
specifier|private
name|Boolean
name|lazyCreateEngines
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
DECL|method|getMessageFactory ()
specifier|public
name|String
name|getMessageFactory
parameter_list|()
block|{
return|return
name|messageFactory
return|;
block|}
DECL|method|setMessageFactory (String messageFactory)
specifier|public
name|void
name|setMessageFactory
parameter_list|(
name|String
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
DECL|method|getLogFactory ()
specifier|public
name|String
name|getLogFactory
parameter_list|()
block|{
return|return
name|logFactory
return|;
block|}
DECL|method|setLogFactory (String logFactory)
specifier|public
name|void
name|setLogFactory
parameter_list|(
name|String
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
DECL|method|getMessageStoreFactory ()
specifier|public
name|String
name|getMessageStoreFactory
parameter_list|()
block|{
return|return
name|messageStoreFactory
return|;
block|}
DECL|method|setMessageStoreFactory (String messageStoreFactory)
specifier|public
name|void
name|setMessageStoreFactory
parameter_list|(
name|String
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
DECL|method|setConfigurations ( Map<String, QuickfixjConfiguration> configurations)
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
DECL|method|getLazyCreateEngines ()
specifier|public
name|Boolean
name|getLazyCreateEngines
parameter_list|()
block|{
return|return
name|lazyCreateEngines
return|;
block|}
DECL|method|setLazyCreateEngines (Boolean lazyCreateEngines)
specifier|public
name|void
name|setLazyCreateEngines
parameter_list|(
name|Boolean
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
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
block|}
end_class

end_unit

