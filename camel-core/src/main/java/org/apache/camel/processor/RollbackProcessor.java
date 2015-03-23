begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|AsyncCallback
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
name|AsyncProcessor
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
name|RollbackExchangeException
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
name|Traceable
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
name|IdAware
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
name|ServiceSupport
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
name|AsyncProcessorHelper
import|;
end_import

begin_comment
comment|/**  * Processor for marking an {@link org.apache.camel.Exchange} to rollback.  *  * @version   */
end_comment

begin_class
DECL|class|RollbackProcessor
specifier|public
class|class
name|RollbackProcessor
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
implements|,
name|Traceable
implements|,
name|IdAware
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|markRollbackOnly
specifier|private
name|boolean
name|markRollbackOnly
decl_stmt|;
DECL|field|markRollbackOnlyLast
specifier|private
name|boolean
name|markRollbackOnlyLast
decl_stmt|;
DECL|field|message
specifier|private
name|String
name|message
decl_stmt|;
DECL|method|RollbackProcessor ()
specifier|public
name|RollbackProcessor
parameter_list|()
block|{     }
DECL|method|RollbackProcessor (String message)
specifier|public
name|RollbackProcessor
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
name|isMarkRollbackOnlyLast
argument_list|()
condition|)
block|{
comment|// only mark the last route (current) as rollback
comment|// this is needed when you have multiple transactions in play
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ROLLBACK_ONLY_LAST
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// default to mark the entire route as rollback
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ROLLBACK_ONLY
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|markRollbackOnly
operator|||
name|markRollbackOnlyLast
condition|)
block|{
comment|// do not do anything more as we should only mark the rollback
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// throw exception to rollback
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RollbackExchangeException
argument_list|(
name|message
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RollbackExchangeException
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
return|return
literal|"Rollback["
operator|+
name|message
operator|+
literal|"]"
return|;
block|}
else|else
block|{
return|return
literal|"Rollback"
return|;
block|}
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"rollback"
return|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
DECL|method|isMarkRollbackOnly ()
specifier|public
name|boolean
name|isMarkRollbackOnly
parameter_list|()
block|{
return|return
name|markRollbackOnly
return|;
block|}
DECL|method|setMarkRollbackOnly (boolean markRollbackOnly)
specifier|public
name|void
name|setMarkRollbackOnly
parameter_list|(
name|boolean
name|markRollbackOnly
parameter_list|)
block|{
name|this
operator|.
name|markRollbackOnly
operator|=
name|markRollbackOnly
expr_stmt|;
block|}
DECL|method|isMarkRollbackOnlyLast ()
specifier|public
name|boolean
name|isMarkRollbackOnlyLast
parameter_list|()
block|{
return|return
name|markRollbackOnlyLast
return|;
block|}
DECL|method|setMarkRollbackOnlyLast (boolean markRollbackOnlyLast)
specifier|public
name|void
name|setMarkRollbackOnlyLast
parameter_list|(
name|boolean
name|markRollbackOnlyLast
parameter_list|)
block|{
name|this
operator|.
name|markRollbackOnlyLast
operator|=
name|markRollbackOnlyLast
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

