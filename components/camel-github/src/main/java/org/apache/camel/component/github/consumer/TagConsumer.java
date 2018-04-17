begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.github.consumer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|github
operator|.
name|consumer
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
name|java
operator|.
name|util
operator|.
name|Stack
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
name|component
operator|.
name|github
operator|.
name|GitHubEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|egit
operator|.
name|github
operator|.
name|core
operator|.
name|RepositoryTag
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

begin_class
DECL|class|TagConsumer
specifier|public
class|class
name|TagConsumer
extends|extends
name|AbstractGitHubConsumer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|TagConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|tagNames
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|tagNames
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|TagConsumer (GitHubEndpoint endpoint, Processor processor)
specifier|public
name|TagConsumer
parameter_list|(
name|GitHubEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"GitHub TagConsumer: Indexing current tags..."
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|RepositoryTag
argument_list|>
name|tags
init|=
name|getRepositoryService
argument_list|()
operator|.
name|getTags
argument_list|(
name|getRepository
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|RepositoryTag
name|tag
range|:
name|tags
control|)
block|{
name|tagNames
operator|.
name|add
argument_list|(
name|tag
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|RepositoryTag
argument_list|>
name|tags
init|=
name|getRepositoryService
argument_list|()
operator|.
name|getTags
argument_list|(
name|getRepository
argument_list|()
argument_list|)
decl_stmt|;
comment|// In the end, we want tags oldest to newest.
name|Stack
argument_list|<
name|RepositoryTag
argument_list|>
name|newTags
init|=
operator|new
name|Stack
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|RepositoryTag
name|tag
range|:
name|tags
control|)
block|{
if|if
condition|(
operator|!
name|tagNames
operator|.
name|contains
argument_list|(
name|tag
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|newTags
operator|.
name|push
argument_list|(
name|tag
argument_list|)
expr_stmt|;
name|tagNames
operator|.
name|add
argument_list|(
name|tag
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
while|while
condition|(
operator|!
name|newTags
operator|.
name|empty
argument_list|()
condition|)
block|{
name|RepositoryTag
name|newTag
init|=
name|newTags
operator|.
name|pop
argument_list|()
decl_stmt|;
name|Exchange
name|e
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|newTag
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|newTags
operator|.
name|size
argument_list|()
return|;
block|}
block|}
end_class

end_unit

