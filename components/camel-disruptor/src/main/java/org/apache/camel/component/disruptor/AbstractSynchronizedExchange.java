begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
package|;
end_package

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
name|spi
operator|.
name|Synchronization
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
name|UnitOfWorkHelper
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
DECL|class|AbstractSynchronizedExchange
specifier|public
specifier|abstract
class|class
name|AbstractSynchronizedExchange
implements|implements
name|SynchronizedExchange
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SynchronizedExchange
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|synchronizations
specifier|protected
specifier|final
name|List
argument_list|<
name|Synchronization
argument_list|>
name|synchronizations
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|method|AbstractSynchronizedExchange (Exchange exchange)
specifier|public
name|AbstractSynchronizedExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|synchronizations
operator|=
name|exchange
operator|.
name|handoverCompletions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getExchange ()
specifier|public
name|Exchange
name|getExchange
parameter_list|()
block|{
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
DECL|method|cancelAndGetOriginalExchange ()
specifier|public
name|Exchange
name|cancelAndGetOriginalExchange
parameter_list|()
block|{
if|if
condition|(
name|synchronizations
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Synchronization
name|synchronization
range|:
name|synchronizations
control|)
block|{
name|exchange
operator|.
name|addOnCompletion
argument_list|(
name|synchronization
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|exchange
return|;
block|}
DECL|method|performSynchronization ()
specifier|protected
name|void
name|performSynchronization
parameter_list|()
block|{
comment|//call synchronizations with the result
name|UnitOfWorkHelper
operator|.
name|doneSynchronizations
argument_list|(
name|getExchange
argument_list|()
argument_list|,
name|synchronizations
argument_list|,
name|AbstractSynchronizedExchange
operator|.
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

