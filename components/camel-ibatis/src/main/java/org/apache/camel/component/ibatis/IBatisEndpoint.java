begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ibatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ibatis
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
name|com
operator|.
name|ibatis
operator|.
name|sqlmap
operator|.
name|client
operator|.
name|SqlMapClient
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
name|ibatis
operator|.
name|strategy
operator|.
name|DefaultIBatisProcessingStategy
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
name|ibatis
operator|.
name|strategy
operator|.
name|IBatisProcessingStrategy
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * An<a href="http://camel.apache.org/ibatis.html>iBatis Endpoint</a>  * for performing SQL operations using an XML mapping file to abstract away the SQL  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|IBatisEndpoint
specifier|public
class|class
name|IBatisEndpoint
extends|extends
name|DefaultPollingEndpoint
block|{
DECL|field|strategy
specifier|private
name|IBatisProcessingStrategy
name|strategy
decl_stmt|;
DECL|field|useTransactions
specifier|private
name|boolean
name|useTransactions
decl_stmt|;
DECL|field|statement
specifier|private
name|String
name|statement
decl_stmt|;
DECL|field|statementType
specifier|private
name|StatementType
name|statementType
decl_stmt|;
DECL|field|maxMessagesPerPoll
specifier|private
name|int
name|maxMessagesPerPoll
decl_stmt|;
DECL|method|IBatisEndpoint ()
specifier|public
name|IBatisEndpoint
parameter_list|()
block|{     }
DECL|method|IBatisEndpoint (String uri, IBatisComponent component, String statement)
specifier|public
name|IBatisEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|IBatisComponent
name|component
parameter_list|,
name|String
name|statement
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|setUseTransactions
argument_list|(
name|component
operator|.
name|isUseTransactions
argument_list|()
argument_list|)
expr_stmt|;
name|setStatement
argument_list|(
name|statement
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|IBatisComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|IBatisComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
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
return|return
operator|new
name|IBatisProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|IBatisConsumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|IBatisConsumer
name|consumer
init|=
operator|new
name|IBatisConsumer
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
comment|/**      * Gets the iBatis SqlMapClient      */
DECL|method|getSqlMapClient ()
specifier|public
name|SqlMapClient
name|getSqlMapClient
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getComponent
argument_list|()
operator|.
name|getSqlMapClient
argument_list|()
return|;
block|}
comment|/**      * Gets the IbatisProcessingStrategy to to use when consuming messages from the database      */
DECL|method|getProcessingStrategy ()
specifier|public
name|IBatisProcessingStrategy
name|getProcessingStrategy
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|strategy
operator|==
literal|null
condition|)
block|{
name|strategy
operator|=
operator|new
name|DefaultIBatisProcessingStategy
argument_list|()
expr_stmt|;
block|}
return|return
name|strategy
return|;
block|}
DECL|method|setStrategy (IBatisProcessingStrategy strategy)
specifier|public
name|void
name|setStrategy
parameter_list|(
name|IBatisProcessingStrategy
name|strategy
parameter_list|)
block|{
name|this
operator|.
name|strategy
operator|=
name|strategy
expr_stmt|;
block|}
comment|/**      * Statement to run when polling or processing     */
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
comment|/**      * Statement to run when polling or processing      */
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
comment|/**      * Indicates if transactions should be used when calling statements.  Useful if using a comma separated list when      * consuming records.      */
DECL|method|isUseTransactions ()
specifier|public
name|boolean
name|isUseTransactions
parameter_list|()
block|{
return|return
name|useTransactions
return|;
block|}
comment|/**      * Sets indicator to use transactions for consuming and error handling statements.      */
DECL|method|setUseTransactions (boolean useTransactions)
specifier|public
name|void
name|setUseTransactions
parameter_list|(
name|boolean
name|useTransactions
parameter_list|)
block|{
name|this
operator|.
name|useTransactions
operator|=
name|useTransactions
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

