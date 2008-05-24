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
name|impl
operator|.
name|DefaultUnitOfWork
import|;
end_import

begin_comment
comment|/**   * Handles calling the UnitOfWork.done() method when processing of an exchange  * is complete.  */
end_comment

begin_class
DECL|class|UnitOfWorkProcessor
specifier|public
specifier|final
class|class
name|UnitOfWorkProcessor
extends|extends
name|DelegateAsyncProcessor
block|{
DECL|method|UnitOfWorkProcessor (AsyncProcessor processor)
specifier|public
name|UnitOfWorkProcessor
parameter_list|(
name|AsyncProcessor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// If there is no existing UoW, then we should start one and
comment|// terminate it once processing is completed for the exchange.
name|exchange
operator|.
name|setUnitOfWork
argument_list|(
operator|new
name|DefaultUnitOfWork
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|sync
parameter_list|)
block|{
comment|// Order here matters. We need to complete the callbacks
comment|// since they will likely update the exchange with
comment|// some final results.
name|callback
operator|.
name|done
argument_list|(
name|sync
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|done
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setUnitOfWork
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
else|else
block|{
comment|// There was an existing UoW, so we should just pass through..
comment|// so that the guy the initiated the UoW can terminate it.
return|return
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

