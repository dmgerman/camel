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
name|quickfix
operator|.
name|ConfigError
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Dictionary
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

begin_class
DECL|class|QuickfixjConfiguration
specifier|public
class|class
name|QuickfixjConfiguration
block|{
DECL|field|defaultSettings
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|defaultSettings
decl_stmt|;
DECL|field|sessionSettings
specifier|private
name|Map
argument_list|<
name|SessionID
argument_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|sessionSettings
decl_stmt|;
DECL|method|QuickfixjConfiguration ()
specifier|public
name|QuickfixjConfiguration
parameter_list|()
block|{     }
DECL|method|getDefaultSettings ()
specifier|public
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|getDefaultSettings
parameter_list|()
block|{
return|return
name|defaultSettings
return|;
block|}
DECL|method|setDefaultSettings (Map<Object, Object> defaultSettings)
specifier|public
name|void
name|setDefaultSettings
parameter_list|(
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|defaultSettings
parameter_list|)
block|{
name|this
operator|.
name|defaultSettings
operator|=
name|defaultSettings
expr_stmt|;
block|}
DECL|method|getSessionSettings ()
specifier|public
name|Map
argument_list|<
name|SessionID
argument_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|getSessionSettings
parameter_list|()
block|{
return|return
name|sessionSettings
return|;
block|}
DECL|method|setSessionSettings (Map<SessionID, Map<Object, Object>> sessionSettings)
specifier|public
name|void
name|setSessionSettings
parameter_list|(
name|Map
argument_list|<
name|SessionID
argument_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|sessionSettings
parameter_list|)
block|{
name|this
operator|.
name|sessionSettings
operator|=
name|sessionSettings
expr_stmt|;
block|}
DECL|method|addSessionSetting (SessionID sessionID, Map<Object, Object> settings)
specifier|public
name|void
name|addSessionSetting
parameter_list|(
name|SessionID
name|sessionID
parameter_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|settings
parameter_list|)
block|{
if|if
condition|(
name|sessionSettings
operator|==
literal|null
condition|)
block|{
name|sessionSettings
operator|=
operator|new
name|HashMap
argument_list|<
name|SessionID
argument_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|sessionSettings
operator|.
name|put
argument_list|(
name|sessionID
argument_list|,
name|settings
argument_list|)
expr_stmt|;
block|}
DECL|method|createSessionSettings ()
specifier|public
name|SessionSettings
name|createSessionSettings
parameter_list|()
throws|throws
name|ConfigError
block|{
name|SessionSettings
name|settings
init|=
operator|new
name|SessionSettings
argument_list|()
decl_stmt|;
if|if
condition|(
name|defaultSettings
operator|!=
literal|null
operator|&&
operator|!
name|defaultSettings
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|settings
operator|.
name|set
argument_list|(
operator|new
name|Dictionary
argument_list|(
literal|"defaults"
argument_list|,
name|defaultSettings
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sessionSettings
operator|!=
literal|null
operator|&&
operator|!
name|sessionSettings
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|SessionID
argument_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|sessionSetting
range|:
name|sessionSettings
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|settings
operator|.
name|set
argument_list|(
name|sessionSetting
operator|.
name|getKey
argument_list|()
argument_list|,
operator|new
name|Dictionary
argument_list|(
literal|"session"
argument_list|,
name|sessionSetting
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|settings
return|;
block|}
block|}
end_class

end_unit

