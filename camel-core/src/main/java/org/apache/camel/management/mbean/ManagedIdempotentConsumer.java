begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
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
name|CamelContext
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedIdempotentConsumerMBean
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
name|model
operator|.
name|ProcessorDefinition
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

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Idempotent Consumer"
argument_list|)
DECL|class|ManagedIdempotentConsumer
specifier|public
class|class
name|ManagedIdempotentConsumer
extends|extends
name|ManagedProcessor
implements|implements
name|ManagedIdempotentConsumerMBean
block|{
DECL|method|ManagedIdempotentConsumer (CamelContext context, IdempotentConsumer idempotentConsumer, ProcessorDefinition<?> definition)
specifier|public
name|ManagedIdempotentConsumer
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|IdempotentConsumer
name|idempotentConsumer
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|idempotentConsumer
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getProcessor ()
specifier|public
name|IdempotentConsumer
name|getProcessor
parameter_list|()
block|{
return|return
operator|(
name|IdempotentConsumer
operator|)
name|super
operator|.
name|getProcessor
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isEager ()
specifier|public
name|Boolean
name|isEager
parameter_list|()
block|{
return|return
name|getProcessor
argument_list|()
operator|.
name|isEager
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isCompletionEager ()
specifier|public
name|Boolean
name|isCompletionEager
parameter_list|()
block|{
return|return
name|getProcessor
argument_list|()
operator|.
name|isCompletionEager
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isSkipDuplicate ()
specifier|public
name|Boolean
name|isSkipDuplicate
parameter_list|()
block|{
return|return
name|getProcessor
argument_list|()
operator|.
name|isSkipDuplicate
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isRemoveOnFailure ()
specifier|public
name|Boolean
name|isRemoveOnFailure
parameter_list|()
block|{
return|return
name|getProcessor
argument_list|()
operator|.
name|isRemoveOnFailure
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getDuplicateMessageCount ()
specifier|public
name|long
name|getDuplicateMessageCount
parameter_list|()
block|{
return|return
name|getProcessor
argument_list|()
operator|.
name|getDuplicateMessageCount
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|resetDuplicateMessageCount ()
specifier|public
name|void
name|resetDuplicateMessageCount
parameter_list|()
block|{
name|getProcessor
argument_list|()
operator|.
name|resetDuplicateMessageCount
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|getProcessor
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

