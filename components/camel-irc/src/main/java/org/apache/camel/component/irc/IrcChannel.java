begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_class
DECL|class|IrcChannel
specifier|public
specifier|final
class|class
name|IrcChannel
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|key
specifier|private
name|String
name|key
decl_stmt|;
DECL|method|IrcChannel ()
specifier|public
name|IrcChannel
parameter_list|()
block|{     }
DECL|method|IrcChannel (String name, String key)
specifier|public
name|IrcChannel
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|name
operator|=
literal|""
expr_stmt|;
return|return;
block|}
name|this
operator|.
name|name
operator|=
name|name
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
operator|||
name|name
operator|.
name|startsWith
argument_list|(
literal|"&"
argument_list|)
condition|?
name|name
else|:
literal|"#"
operator|+
name|name
expr_stmt|;
block|}
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
DECL|method|setKey (String key)
specifier|public
name|void
name|setKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
operator|!=
literal|null
condition|?
name|key
else|:
literal|""
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
block|}
end_class

end_unit

