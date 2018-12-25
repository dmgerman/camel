begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.chatscript
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|chatscript
package|;
end_package

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonProperty
import|;
end_import

begin_class
DECL|class|ChatScriptMessage
specifier|public
class|class
name|ChatScriptMessage
block|{
annotation|@
name|JsonProperty
argument_list|(
literal|"username"
argument_list|)
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"botname"
argument_list|)
DECL|field|botname
specifier|private
name|String
name|botname
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"message"
argument_list|)
DECL|field|body
specifier|private
name|String
name|body
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"response"
argument_list|)
DECL|field|reply
specifier|private
name|String
name|reply
decl_stmt|;
DECL|method|ChatScriptMessage (final String userName, final String botName, final String iBody)
specifier|public
name|ChatScriptMessage
parameter_list|(
specifier|final
name|String
name|userName
parameter_list|,
specifier|final
name|String
name|botName
parameter_list|,
specifier|final
name|String
name|iBody
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|userName
expr_stmt|;
name|this
operator|.
name|botname
operator|=
name|botName
expr_stmt|;
name|this
operator|.
name|body
operator|=
name|iBody
expr_stmt|;
block|}
DECL|method|ChatScriptMessage ()
specifier|public
name|ChatScriptMessage
parameter_list|()
block|{      }
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|username
return|;
block|}
DECL|method|setUserName (String userName)
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|userName
expr_stmt|;
block|}
DECL|method|getBotName ()
specifier|public
name|String
name|getBotName
parameter_list|()
block|{
return|return
name|botname
return|;
block|}
DECL|method|setBotName (String botName)
specifier|public
name|void
name|setBotName
parameter_list|(
name|String
name|botName
parameter_list|)
block|{
name|this
operator|.
name|botname
operator|=
name|botName
expr_stmt|;
block|}
DECL|method|getBody ()
specifier|public
name|String
name|getBody
parameter_list|()
block|{
return|return
name|body
return|;
block|}
DECL|method|setBody (String iBody)
specifier|public
name|void
name|setBody
parameter_list|(
name|String
name|iBody
parameter_list|)
block|{
name|this
operator|.
name|body
operator|=
name|iBody
expr_stmt|;
block|}
DECL|method|getReply ()
specifier|public
name|String
name|getReply
parameter_list|()
block|{
return|return
name|reply
return|;
block|}
DECL|method|setReply (String iReply)
specifier|public
name|void
name|setReply
parameter_list|(
name|String
name|iReply
parameter_list|)
block|{
name|this
operator|.
name|reply
operator|=
name|iReply
expr_stmt|;
block|}
DECL|method|toCSFormat ()
specifier|public
name|String
name|toCSFormat
parameter_list|()
block|{
name|String
name|s
decl_stmt|;
specifier|final
name|char
name|nullChar
init|=
operator|(
name|char
operator|)
literal|0
decl_stmt|;
name|s
operator|=
name|this
operator|.
name|username
operator|+
name|nullChar
operator|+
name|this
operator|.
name|botname
operator|+
name|nullChar
operator|+
name|this
operator|.
name|body
operator|+
name|nullChar
expr_stmt|;
return|return
name|s
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ChatScriptMessage [username="
operator|+
name|username
operator|+
literal|", botname="
operator|+
name|botname
operator|+
literal|", message="
operator|+
name|body
operator|+
literal|", reply="
operator|+
name|reply
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

