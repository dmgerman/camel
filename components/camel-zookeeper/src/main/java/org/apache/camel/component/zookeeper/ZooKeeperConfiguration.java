begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|RuntimeCamelException
import|;
end_import

begin_comment
comment|/**  *<code>ZookeeperConfiguration</code> encapsulates the configuration used to  * interact with a ZooKeeper cluster. Most typically it is parsed from endpoint  * uri but may also be configured programatically and applied to a  * {@link ZooKeeperComponent}. A copy of this component's configuration will be  * injected into any {@link ZooKeeperEndpoint}s the component creates.  *  * @version $  */
end_comment

begin_class
DECL|class|ZooKeeperConfiguration
specifier|public
class|class
name|ZooKeeperConfiguration
implements|implements
name|Cloneable
block|{
DECL|field|timeout
specifier|private
name|int
name|timeout
init|=
literal|5000
decl_stmt|;
DECL|field|backoff
specifier|private
name|long
name|backoff
init|=
literal|5000
decl_stmt|;
DECL|field|servers
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|servers
decl_stmt|;
DECL|field|changed
specifier|private
name|boolean
name|changed
decl_stmt|;
DECL|field|sessionId
specifier|private
name|int
name|sessionId
decl_stmt|;
DECL|field|password
specifier|private
name|byte
index|[]
name|password
decl_stmt|;
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
DECL|field|awaitCreation
specifier|private
name|boolean
name|awaitCreation
init|=
literal|true
decl_stmt|;
DECL|field|repeat
specifier|private
name|boolean
name|repeat
decl_stmt|;
DECL|field|listChildren
specifier|private
name|boolean
name|listChildren
decl_stmt|;
DECL|field|shouldCreate
specifier|private
name|boolean
name|shouldCreate
decl_stmt|;
DECL|field|createMode
specifier|private
name|String
name|createMode
decl_stmt|;
DECL|method|addZookeeperServer (String server)
specifier|public
name|void
name|addZookeeperServer
parameter_list|(
name|String
name|server
parameter_list|)
block|{
if|if
condition|(
name|servers
operator|==
literal|null
condition|)
block|{
name|servers
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|servers
operator|.
name|add
argument_list|(
name|server
argument_list|)
expr_stmt|;
name|changed
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|getServers ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getServers
parameter_list|()
block|{
return|return
name|servers
return|;
block|}
DECL|method|setServers (List<String> servers)
specifier|public
name|void
name|setServers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|servers
parameter_list|)
block|{
name|this
operator|.
name|servers
operator|=
name|servers
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|int
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (int timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
name|changed
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|listChildren ()
specifier|public
name|boolean
name|listChildren
parameter_list|()
block|{
return|return
name|listChildren
return|;
block|}
DECL|method|setListChildren (boolean listChildren)
specifier|public
name|void
name|setListChildren
parameter_list|(
name|boolean
name|listChildren
parameter_list|)
block|{
name|this
operator|.
name|listChildren
operator|=
name|listChildren
expr_stmt|;
block|}
DECL|method|clearChanged ()
specifier|public
name|void
name|clearChanged
parameter_list|()
block|{
name|changed
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|isChanged ()
specifier|public
name|boolean
name|isChanged
parameter_list|()
block|{
return|return
name|changed
return|;
block|}
DECL|method|getConnectString ()
specifier|public
name|String
name|getConnectString
parameter_list|()
block|{
comment|// return StringHelper.toStringCommaSeparated(servers);
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|server
range|:
name|servers
control|)
block|{
name|b
operator|.
name|append
argument_list|(
name|server
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|setLength
argument_list|(
name|b
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getSessionPassword ()
specifier|public
name|byte
index|[]
name|getSessionPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|getSessionId ()
specifier|public
name|int
name|getSessionId
parameter_list|()
block|{
return|return
name|sessionId
return|;
block|}
DECL|method|setPath (String path)
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
DECL|method|shouldRepeat ()
specifier|public
name|boolean
name|shouldRepeat
parameter_list|()
block|{
return|return
name|repeat
return|;
block|}
DECL|method|setRepeat (boolean repeat)
specifier|public
name|void
name|setRepeat
parameter_list|(
name|boolean
name|repeat
parameter_list|)
block|{
name|this
operator|.
name|repeat
operator|=
name|repeat
expr_stmt|;
block|}
DECL|method|copy ()
specifier|public
name|ZooKeeperConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|ZooKeeperConfiguration
operator|)
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|shouldAwaitExistence ()
specifier|public
name|boolean
name|shouldAwaitExistence
parameter_list|()
block|{
return|return
name|awaitCreation
return|;
block|}
DECL|method|setAwaitExistance (boolean awaitCreation)
specifier|public
name|void
name|setAwaitExistance
parameter_list|(
name|boolean
name|awaitCreation
parameter_list|)
block|{
name|this
operator|.
name|awaitCreation
operator|=
name|awaitCreation
expr_stmt|;
block|}
DECL|method|getBackoff ()
specifier|public
name|long
name|getBackoff
parameter_list|()
block|{
return|return
name|backoff
return|;
block|}
DECL|method|setBackoff (long backoff)
specifier|public
name|void
name|setBackoff
parameter_list|(
name|long
name|backoff
parameter_list|)
block|{
name|this
operator|.
name|backoff
operator|=
name|backoff
expr_stmt|;
block|}
DECL|method|setCreate (boolean shouldCreate)
specifier|public
name|void
name|setCreate
parameter_list|(
name|boolean
name|shouldCreate
parameter_list|)
block|{
name|this
operator|.
name|shouldCreate
operator|=
name|shouldCreate
expr_stmt|;
block|}
DECL|method|shouldCreate ()
specifier|public
name|boolean
name|shouldCreate
parameter_list|()
block|{
return|return
name|shouldCreate
return|;
block|}
DECL|method|getCreateMode ()
specifier|public
name|String
name|getCreateMode
parameter_list|()
block|{
return|return
name|createMode
return|;
block|}
DECL|method|setCreateMode (String createMode)
specifier|public
name|void
name|setCreateMode
parameter_list|(
name|String
name|createMode
parameter_list|)
block|{
name|this
operator|.
name|createMode
operator|=
name|createMode
expr_stmt|;
block|}
block|}
end_class

end_unit

