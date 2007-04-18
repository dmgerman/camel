begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|Expression
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
name|processor
operator|.
name|idempotent
operator|.
name|IdempotentConsumer
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
name|processor
operator|.
name|idempotent
operator|.
name|MessageIdRepository
import|;
end_import

begin_comment
comment|/**  * A builder of an {@link IdempotentConsumer}  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|IdempotentConsumerBuilder
specifier|public
class|class
name|IdempotentConsumerBuilder
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|FromBuilder
argument_list|<
name|E
argument_list|>
implements|implements
name|ProcessorFactory
argument_list|<
name|E
argument_list|>
block|{
DECL|field|messageIdExpression
specifier|private
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|messageIdExpression
decl_stmt|;
DECL|field|messageIdRegistry
specifier|private
specifier|final
name|MessageIdRepository
name|messageIdRegistry
decl_stmt|;
DECL|method|IdempotentConsumerBuilder (FromBuilder<E> fromBuilder, Expression<E> messageIdExpression, MessageIdRepository messageIdRegistry)
specifier|public
name|IdempotentConsumerBuilder
parameter_list|(
name|FromBuilder
argument_list|<
name|E
argument_list|>
name|fromBuilder
parameter_list|,
name|Expression
argument_list|<
name|E
argument_list|>
name|messageIdExpression
parameter_list|,
name|MessageIdRepository
name|messageIdRegistry
parameter_list|)
block|{
name|super
argument_list|(
name|fromBuilder
argument_list|)
expr_stmt|;
name|this
operator|.
name|messageIdRegistry
operator|=
name|messageIdRegistry
expr_stmt|;
name|this
operator|.
name|messageIdExpression
operator|=
name|messageIdExpression
expr_stmt|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getMessageIdRegistry ()
specifier|public
name|MessageIdRepository
name|getMessageIdRegistry
parameter_list|()
block|{
return|return
name|messageIdRegistry
return|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|wrapInErrorHandler (Processor<E> processor)
specifier|protected
name|Processor
argument_list|<
name|E
argument_list|>
name|wrapInErrorHandler
parameter_list|(
name|Processor
argument_list|<
name|E
argument_list|>
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
comment|// lets do no wrapping in error handlers as the parent FromBuilder will do that
return|return
name|processor
return|;
block|}
annotation|@
name|Override
DECL|method|wrapProcessor (Processor<E> processor)
specifier|protected
name|Processor
argument_list|<
name|E
argument_list|>
name|wrapProcessor
parameter_list|(
name|Processor
argument_list|<
name|E
argument_list|>
name|processor
parameter_list|)
block|{
return|return
operator|new
name|IdempotentConsumer
argument_list|<
name|E
argument_list|>
argument_list|(
name|messageIdExpression
argument_list|,
name|messageIdRegistry
argument_list|,
name|processor
argument_list|)
return|;
block|}
block|}
end_class

end_unit

