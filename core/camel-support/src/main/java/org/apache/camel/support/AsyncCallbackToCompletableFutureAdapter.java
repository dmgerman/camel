begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CompletableFuture
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
name|AsyncCallback
import|;
end_import

begin_comment
comment|/**  * AsyncCallback that provides a CompletableFuture completed when async action is done  */
end_comment

begin_class
DECL|class|AsyncCallbackToCompletableFutureAdapter
specifier|public
class|class
name|AsyncCallbackToCompletableFutureAdapter
parameter_list|<
name|T
parameter_list|>
implements|implements
name|AsyncCallback
block|{
DECL|field|future
specifier|private
specifier|final
name|CompletableFuture
argument_list|<
name|T
argument_list|>
name|future
decl_stmt|;
DECL|field|result
specifier|private
specifier|volatile
name|T
name|result
decl_stmt|;
DECL|method|AsyncCallbackToCompletableFutureAdapter ()
specifier|public
name|AsyncCallbackToCompletableFutureAdapter
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|AsyncCallbackToCompletableFutureAdapter (T result)
specifier|public
name|AsyncCallbackToCompletableFutureAdapter
parameter_list|(
name|T
name|result
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|AsyncCallbackToCompletableFutureAdapter (CompletableFuture<T> future, T result)
specifier|public
name|AsyncCallbackToCompletableFutureAdapter
parameter_list|(
name|CompletableFuture
argument_list|<
name|T
argument_list|>
name|future
parameter_list|,
name|T
name|result
parameter_list|)
block|{
name|this
operator|.
name|future
operator|=
name|future
operator|!=
literal|null
condition|?
name|future
else|:
operator|new
name|CompletableFuture
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|result
operator|=
name|result
expr_stmt|;
block|}
DECL|method|setResult (T result)
specifier|public
name|void
name|setResult
parameter_list|(
name|T
name|result
parameter_list|)
block|{
name|this
operator|.
name|result
operator|=
name|result
expr_stmt|;
block|}
DECL|method|getFuture ()
specifier|public
name|CompletableFuture
argument_list|<
name|T
argument_list|>
name|getFuture
parameter_list|()
block|{
return|return
name|future
return|;
block|}
annotation|@
name|Override
DECL|method|done (boolean doneSync)
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
name|future
operator|.
name|complete
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

