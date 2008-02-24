begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|concurrent
operator|.
name|CountDownLatch
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
name|spi
operator|.
name|UnitOfWork
import|;
end_import

begin_comment
comment|/**  * The default implementation of {@link UnitOfWork}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultUnitOfWork
specifier|public
class|class
name|DefaultUnitOfWork
implements|implements
name|UnitOfWork
block|{
DECL|field|synchronizations
specifier|private
name|List
argument_list|<
name|Synchronization
argument_list|>
name|synchronizations
decl_stmt|;
DECL|field|asyncCallbacks
specifier|private
name|List
argument_list|<
name|AsyncCallback
argument_list|>
name|asyncCallbacks
decl_stmt|;
DECL|field|latch
specifier|private
name|CountDownLatch
name|latch
decl_stmt|;
DECL|method|DefaultUnitOfWork ()
specifier|public
name|DefaultUnitOfWork
parameter_list|()
block|{     }
DECL|method|addSynchronization (Synchronization synchronization)
specifier|public
specifier|synchronized
name|void
name|addSynchronization
parameter_list|(
name|Synchronization
name|synchronization
parameter_list|)
block|{
if|if
condition|(
name|synchronizations
operator|==
literal|null
condition|)
block|{
name|synchronizations
operator|=
operator|new
name|ArrayList
argument_list|<
name|Synchronization
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|synchronizations
operator|.
name|add
argument_list|(
name|synchronization
argument_list|)
expr_stmt|;
block|}
DECL|method|removeSynchronization (Synchronization synchronization)
specifier|public
specifier|synchronized
name|void
name|removeSynchronization
parameter_list|(
name|Synchronization
name|synchronization
parameter_list|)
block|{
if|if
condition|(
name|synchronizations
operator|!=
literal|null
condition|)
block|{
name|synchronizations
operator|.
name|remove
argument_list|(
name|synchronization
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{      }
DECL|method|done (Exchange exchange)
specifier|public
name|void
name|done
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|synchronizations
operator|!=
literal|null
condition|)
block|{
name|boolean
name|failed
init|=
name|exchange
operator|.
name|isFailed
argument_list|()
decl_stmt|;
for|for
control|(
name|Synchronization
name|synchronization
range|:
name|synchronizations
control|)
block|{
if|if
condition|(
name|failed
condition|)
block|{
name|synchronization
operator|.
name|onFailure
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|synchronization
operator|.
name|onComplete
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|isSynchronous ()
specifier|public
name|boolean
name|isSynchronous
parameter_list|()
block|{
return|return
name|asyncCallbacks
operator|==
literal|null
operator|||
name|asyncCallbacks
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/**      * Register some asynchronous processing step      */
comment|/*     public synchronized AsyncCallback addAsyncStep() {         AsyncCallback answer = new AsyncCallback() {             public void done(boolean doneSynchronously) {                 latch.countDown();             }         };         if (latch == null) {             latch = new CountDownLatch(1);         }         else {             // TODO increment latch!         }         if (asyncCallbacks == null) {             asyncCallbacks = new ArrayList<AsyncCallback>();         }         asyncCallbacks.add(answer);         return answer;     }     */
block|}
end_class

end_unit

