begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mybatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mybatis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|impl
operator|.
name|DefaultPollingEndpoint
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ibatis
operator|.
name|session
operator|.
name|ExecutorType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ibatis
operator|.
name|session
operator|.
name|SqlSessionFactory
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"mybatis"
argument_list|,
name|consumerClass
operator|=
name|MyBatisConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"database"
argument_list|)
DECL|class|MyBatisEndpoint
specifier|public
class|class
name|MyBatisEndpoint
extends|extends
name|DefaultPollingEndpoint
block|{
DECL|field|processingStrategy
specifier|private
name|MyBatisProcessingStrategy
name|processingStrategy
init|=
operator|new
name|DefaultMyBatisProcessingStrategy
argument_list|()
decl_stmt|;
DECL|field|executorType
specifier|private
name|ExecutorType
name|executorType
decl_stmt|;
annotation|@
name|UriParam
DECL|field|statement
specifier|private
name|String
name|statement
decl_stmt|;
annotation|@
name|UriParam
DECL|field|statementType
specifier|private
name|StatementType
name|statementType
decl_stmt|;
annotation|@
name|UriParam
DECL|field|maxMessagesPerPoll
specifier|private
name|int
name|maxMessagesPerPoll
decl_stmt|;
DECL|method|MyBatisEndpoint ()
specifier|public
name|MyBatisEndpoint
parameter_list|()
block|{     }
DECL|method|MyBatisEndpoint (String endpointUri, Component component, String statement)
specifier|public
name|MyBatisEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|String
name|statement
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|statement
operator|=
name|statement
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|statementType
argument_list|,
literal|"statementType"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|statement
argument_list|,
literal|"statement"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
operator|new
name|MyBatisProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|statement
argument_list|,
literal|"statement"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|MyBatisConsumer
name|consumer
init|=
operator|new
name|MyBatisConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setMaxMessagesPerPoll
argument_list|(
name|getMaxMessagesPerPoll
argument_list|()
argument_list|)
expr_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|MyBatisComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|MyBatisComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|getSqlSessionFactory ()
specifier|public
name|SqlSessionFactory
name|getSqlSessionFactory
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getComponent
argument_list|()
operator|.
name|getSqlSessionFactory
argument_list|()
return|;
block|}
DECL|method|getStatement ()
specifier|public
name|String
name|getStatement
parameter_list|()
block|{
return|return
name|statement
return|;
block|}
DECL|method|setStatement (String statement)
specifier|public
name|void
name|setStatement
parameter_list|(
name|String
name|statement
parameter_list|)
block|{
name|this
operator|.
name|statement
operator|=
name|statement
expr_stmt|;
block|}
DECL|method|getStatementType ()
specifier|public
name|StatementType
name|getStatementType
parameter_list|()
block|{
return|return
name|statementType
return|;
block|}
DECL|method|setStatementType (StatementType statementType)
specifier|public
name|void
name|setStatementType
parameter_list|(
name|StatementType
name|statementType
parameter_list|)
block|{
name|this
operator|.
name|statementType
operator|=
name|statementType
expr_stmt|;
block|}
DECL|method|getExecutorType ()
specifier|public
name|ExecutorType
name|getExecutorType
parameter_list|()
block|{
return|return
name|executorType
return|;
block|}
DECL|method|setExecutorType (ExecutorType executorType)
specifier|public
name|void
name|setExecutorType
parameter_list|(
name|ExecutorType
name|executorType
parameter_list|)
block|{
name|this
operator|.
name|executorType
operator|=
name|executorType
expr_stmt|;
block|}
DECL|method|setExecutorType (String executorType)
specifier|public
name|void
name|setExecutorType
parameter_list|(
name|String
name|executorType
parameter_list|)
block|{
name|this
operator|.
name|executorType
operator|=
name|ExecutorType
operator|.
name|valueOf
argument_list|(
name|executorType
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getProcessingStrategy ()
specifier|public
name|MyBatisProcessingStrategy
name|getProcessingStrategy
parameter_list|()
block|{
return|return
name|processingStrategy
return|;
block|}
DECL|method|setProcessingStrategy (MyBatisProcessingStrategy processingStrategy)
specifier|public
name|void
name|setProcessingStrategy
parameter_list|(
name|MyBatisProcessingStrategy
name|processingStrategy
parameter_list|)
block|{
name|this
operator|.
name|processingStrategy
operator|=
name|processingStrategy
expr_stmt|;
block|}
DECL|method|getMaxMessagesPerPoll ()
specifier|public
name|int
name|getMaxMessagesPerPoll
parameter_list|()
block|{
return|return
name|maxMessagesPerPoll
return|;
block|}
DECL|method|setMaxMessagesPerPoll (int maxMessagesPerPoll)
specifier|public
name|void
name|setMaxMessagesPerPoll
parameter_list|(
name|int
name|maxMessagesPerPoll
parameter_list|)
block|{
name|this
operator|.
name|maxMessagesPerPoll
operator|=
name|maxMessagesPerPoll
expr_stmt|;
block|}
block|}
end_class

end_unit

