begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.git
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|git
package|;
end_package

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
name|component
operator|.
name|git
operator|.
name|consumer
operator|.
name|GitBranchConsumer
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
name|git
operator|.
name|consumer
operator|.
name|GitCommitConsumer
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
name|git
operator|.
name|consumer
operator|.
name|GitTagConsumer
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
name|git
operator|.
name|consumer
operator|.
name|GitType
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
name|git
operator|.
name|producer
operator|.
name|GitProducer
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
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * The git component is used for working with git repositories.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.16.0"
argument_list|,
name|scheme
operator|=
literal|"git"
argument_list|,
name|title
operator|=
literal|"Git"
argument_list|,
name|syntax
operator|=
literal|"git:localPath"
argument_list|,
name|label
operator|=
literal|"file"
argument_list|)
DECL|class|GitEndpoint
specifier|public
class|class
name|GitEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|localPath
specifier|private
name|String
name|localPath
decl_stmt|;
annotation|@
name|UriParam
DECL|field|branchName
specifier|private
name|String
name|branchName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|tagName
specifier|private
name|String
name|tagName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|enums
operator|=
literal|"commit,tag,branch"
argument_list|,
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|type
specifier|private
name|GitType
name|type
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
annotation|@
name|UriParam
DECL|field|remotePath
specifier|private
name|String
name|remotePath
decl_stmt|;
annotation|@
name|UriParam
DECL|field|remoteName
specifier|private
name|String
name|remoteName
decl_stmt|;
comment|// Set to true for backward compatibility , better to set to false (native git behavior)
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|allowEmpty
specifier|private
name|boolean
name|allowEmpty
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|enums
operator|=
literal|"clone,init,add,remove,commit,commitAll,createBranch,deleteBranch,createTag,deleteTag,status,log,push,pull,showBranches,cherryPick,remoteAdd,remoteList"
argument_list|,
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|operation
specifier|private
name|String
name|operation
decl_stmt|;
DECL|method|GitEndpoint (String uri, GitComponent component)
specifier|public
name|GitEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|GitComponent
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
name|GitProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
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
if|if
condition|(
name|type
operator|==
name|GitType
operator|.
name|COMMIT
condition|)
block|{
return|return
operator|new
name|GitCommitConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|GitType
operator|.
name|TAG
condition|)
block|{
return|return
operator|new
name|GitTagConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|GitType
operator|.
name|BRANCH
condition|)
block|{
return|return
operator|new
name|GitBranchConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot create consumer with type "
operator|+
name|type
argument_list|)
throw|;
block|}
block|}
comment|/**      * The remote repository path      */
DECL|method|getRemotePath ()
specifier|public
name|String
name|getRemotePath
parameter_list|()
block|{
return|return
name|remotePath
return|;
block|}
DECL|method|setRemotePath (String remotePath)
specifier|public
name|void
name|setRemotePath
parameter_list|(
name|String
name|remotePath
parameter_list|)
block|{
name|this
operator|.
name|remotePath
operator|=
name|remotePath
expr_stmt|;
block|}
comment|/**      * The branch name to work on      */
DECL|method|getBranchName ()
specifier|public
name|String
name|getBranchName
parameter_list|()
block|{
return|return
name|branchName
return|;
block|}
DECL|method|setBranchName (String branchName)
specifier|public
name|void
name|setBranchName
parameter_list|(
name|String
name|branchName
parameter_list|)
block|{
name|this
operator|.
name|branchName
operator|=
name|branchName
expr_stmt|;
block|}
comment|/**      * Remote repository username      */
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
comment|/**      * Remote repository password      */
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
comment|/**      * Local repository path      */
DECL|method|getLocalPath ()
specifier|public
name|String
name|getLocalPath
parameter_list|()
block|{
return|return
name|localPath
return|;
block|}
DECL|method|setLocalPath (String localPath)
specifier|public
name|void
name|setLocalPath
parameter_list|(
name|String
name|localPath
parameter_list|)
block|{
name|this
operator|.
name|localPath
operator|=
name|localPath
expr_stmt|;
block|}
comment|/**      * The operation to do on the repository      */
DECL|method|getOperation ()
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
comment|/**      * The consumer type      */
DECL|method|getType ()
specifier|public
name|GitType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (GitType type)
specifier|public
name|void
name|setType
parameter_list|(
name|GitType
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
comment|/**      * The tag name to work on      */
DECL|method|getTagName ()
specifier|public
name|String
name|getTagName
parameter_list|()
block|{
return|return
name|tagName
return|;
block|}
DECL|method|setTagName (String tagName)
specifier|public
name|void
name|setTagName
parameter_list|(
name|String
name|tagName
parameter_list|)
block|{
name|this
operator|.
name|tagName
operator|=
name|tagName
expr_stmt|;
block|}
comment|/**      * The remote repository name to use in particular operation like pull      */
DECL|method|getRemoteName ()
specifier|public
name|String
name|getRemoteName
parameter_list|()
block|{
return|return
name|remoteName
return|;
block|}
DECL|method|setRemoteName (String remoteName)
specifier|public
name|void
name|setRemoteName
parameter_list|(
name|String
name|remoteName
parameter_list|)
block|{
name|this
operator|.
name|remoteName
operator|=
name|remoteName
expr_stmt|;
block|}
comment|/**      * The flag to manage empty git commits      */
DECL|method|isAllowEmpty ()
specifier|public
name|boolean
name|isAllowEmpty
parameter_list|()
block|{
return|return
name|allowEmpty
return|;
block|}
DECL|method|setAllowEmpty (boolean allowEmpty)
specifier|public
name|void
name|setAllowEmpty
parameter_list|(
name|boolean
name|allowEmpty
parameter_list|)
block|{
name|this
operator|.
name|allowEmpty
operator|=
name|allowEmpty
expr_stmt|;
block|}
block|}
end_class

end_unit

