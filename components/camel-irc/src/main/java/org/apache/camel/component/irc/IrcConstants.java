begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.irc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|irc
package|;
end_package

begin_comment
comment|/**  * IRC Constants  */
end_comment

begin_class
DECL|class|IrcConstants
specifier|public
specifier|final
class|class
name|IrcConstants
block|{
DECL|field|IRC_MESSAGE_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|IRC_MESSAGE_TYPE
init|=
literal|"irc.messageType"
decl_stmt|;
DECL|field|IRC_TARGET
specifier|public
specifier|static
specifier|final
name|String
name|IRC_TARGET
init|=
literal|"irc.target"
decl_stmt|;
DECL|field|IRC_SEND_TO
specifier|public
specifier|static
specifier|final
name|String
name|IRC_SEND_TO
init|=
literal|"irc.sendTo"
decl_stmt|;
DECL|field|IRC_USER_KICKED
specifier|public
specifier|static
specifier|final
name|String
name|IRC_USER_KICKED
init|=
literal|"irc.user.kicked"
decl_stmt|;
DECL|field|IRC_USER_HOST
specifier|public
specifier|static
specifier|final
name|String
name|IRC_USER_HOST
init|=
literal|"irc.user.host"
decl_stmt|;
DECL|field|IRC_USER_NICK
specifier|public
specifier|static
specifier|final
name|String
name|IRC_USER_NICK
init|=
literal|"irc.user.nick"
decl_stmt|;
DECL|field|IRC_USER_SERVERNAME
specifier|public
specifier|static
specifier|final
name|String
name|IRC_USER_SERVERNAME
init|=
literal|"irc.user.servername"
decl_stmt|;
DECL|field|IRC_USER_USERNAME
specifier|public
specifier|static
specifier|final
name|String
name|IRC_USER_USERNAME
init|=
literal|"irc.user.username"
decl_stmt|;
DECL|field|IRC_NUM
specifier|public
specifier|static
specifier|final
name|String
name|IRC_NUM
init|=
literal|"irc.num"
decl_stmt|;
DECL|field|IRC_VALUE
specifier|public
specifier|static
specifier|final
name|String
name|IRC_VALUE
init|=
literal|"irc.value"
decl_stmt|;
DECL|method|IrcConstants ()
specifier|private
name|IrcConstants
parameter_list|()
block|{
comment|// Utility class
block|}
block|}
end_class

end_unit

