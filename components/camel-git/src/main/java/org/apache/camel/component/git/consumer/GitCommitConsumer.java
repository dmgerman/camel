begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.git.consumer
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
name|git
operator|.
name|GitEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevCommit
import|;
end_import

begin_class
DECL|class|GitCommitConsumer
specifier|public
class|class
name|GitCommitConsumer
extends|extends
name|AbstractGitConsumer
block|{
DECL|field|used
specifier|private
name|List
name|used
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
DECL|method|GitCommitConsumer (GitEndpoint endpoint, Processor processor)
specifier|public
name|GitCommitConsumer
parameter_list|(
name|GitEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
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
name|int
name|count
init|=
literal|0
decl_stmt|;
name|Iterable
argument_list|<
name|RevCommit
argument_list|>
name|commits
init|=
name|getGit
argument_list|()
operator|.
name|log
argument_list|()
operator|.
name|all
argument_list|()
operator|.
name|call
argument_list|()
decl_stmt|;
for|for
control|(
name|RevCommit
name|commit
range|:
name|commits
control|)
block|{
if|if
condition|(
operator|!
name|used
operator|.
name|contains
argument_list|(
name|commit
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
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
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|commit
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
name|used
operator|.
name|add
argument_list|(
name|commit
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
block|}
return|return
name|count
return|;
block|}
block|}
end_class

end_unit

