begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.concurrent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|concurrent
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
name|Callable
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
name|locks
operator|.
name|StampedLock
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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
name|function
operator|.
name|ThrowingRunnable
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
name|function
operator|.
name|ThrowingSupplier
import|;
end_import

begin_class
DECL|class|LockHelper
specifier|public
specifier|final
class|class
name|LockHelper
block|{
DECL|method|LockHelper ()
specifier|private
name|LockHelper
parameter_list|()
block|{     }
DECL|method|doWithReadLock (StampedLock lock, Runnable task)
specifier|public
specifier|static
name|void
name|doWithReadLock
parameter_list|(
name|StampedLock
name|lock
parameter_list|,
name|Runnable
name|task
parameter_list|)
block|{
name|long
name|stamp
init|=
name|lock
operator|.
name|readLock
argument_list|()
decl_stmt|;
try|try
block|{
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlockRead
argument_list|(
name|stamp
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|callWithReadLock (StampedLock lock, Callable<R> task)
specifier|public
specifier|static
parameter_list|<
name|R
parameter_list|>
name|R
name|callWithReadLock
parameter_list|(
name|StampedLock
name|lock
parameter_list|,
name|Callable
argument_list|<
name|R
argument_list|>
name|task
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|stamp
init|=
name|lock
operator|.
name|readLock
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|task
operator|.
name|call
argument_list|()
return|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlockRead
argument_list|(
name|stamp
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doWithReadLockT (StampedLock lock, ThrowingRunnable<T> task)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Throwable
parameter_list|>
name|void
name|doWithReadLockT
parameter_list|(
name|StampedLock
name|lock
parameter_list|,
name|ThrowingRunnable
argument_list|<
name|T
argument_list|>
name|task
parameter_list|)
throws|throws
name|T
block|{
name|long
name|stamp
init|=
name|lock
operator|.
name|readLock
argument_list|()
decl_stmt|;
try|try
block|{
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlockRead
argument_list|(
name|stamp
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|supplyWithReadLock (StampedLock lock, Supplier<R> task)
specifier|public
specifier|static
parameter_list|<
name|R
parameter_list|>
name|R
name|supplyWithReadLock
parameter_list|(
name|StampedLock
name|lock
parameter_list|,
name|Supplier
argument_list|<
name|R
argument_list|>
name|task
parameter_list|)
block|{
name|long
name|stamp
init|=
name|lock
operator|.
name|readLock
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|task
operator|.
name|get
argument_list|()
return|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlockRead
argument_list|(
name|stamp
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|supplyWithReadLockT (StampedLock lock, ThrowingSupplier<R, T> task)
specifier|public
specifier|static
parameter_list|<
name|R
parameter_list|,
name|T
extends|extends
name|Throwable
parameter_list|>
name|R
name|supplyWithReadLockT
parameter_list|(
name|StampedLock
name|lock
parameter_list|,
name|ThrowingSupplier
argument_list|<
name|R
argument_list|,
name|T
argument_list|>
name|task
parameter_list|)
throws|throws
name|T
block|{
name|long
name|stamp
init|=
name|lock
operator|.
name|readLock
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|task
operator|.
name|get
argument_list|()
return|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlockRead
argument_list|(
name|stamp
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doWithWriteLock (StampedLock lock, Runnable task)
specifier|public
specifier|static
name|void
name|doWithWriteLock
parameter_list|(
name|StampedLock
name|lock
parameter_list|,
name|Runnable
name|task
parameter_list|)
block|{
name|long
name|stamp
init|=
name|lock
operator|.
name|writeLock
argument_list|()
decl_stmt|;
try|try
block|{
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlockWrite
argument_list|(
name|stamp
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|callWithWriteLock (StampedLock lock, Callable<R> task)
specifier|public
specifier|static
parameter_list|<
name|R
parameter_list|>
name|R
name|callWithWriteLock
parameter_list|(
name|StampedLock
name|lock
parameter_list|,
name|Callable
argument_list|<
name|R
argument_list|>
name|task
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|stamp
init|=
name|lock
operator|.
name|writeLock
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|task
operator|.
name|call
argument_list|()
return|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlockWrite
argument_list|(
name|stamp
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|supplyWithWriteLock (StampedLock lock, Supplier<R> task)
specifier|public
specifier|static
parameter_list|<
name|R
parameter_list|>
name|R
name|supplyWithWriteLock
parameter_list|(
name|StampedLock
name|lock
parameter_list|,
name|Supplier
argument_list|<
name|R
argument_list|>
name|task
parameter_list|)
block|{
name|long
name|stamp
init|=
name|lock
operator|.
name|writeLock
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|task
operator|.
name|get
argument_list|()
return|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlockWrite
argument_list|(
name|stamp
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doWithWriteLockT (StampedLock lock, ThrowingRunnable<T> task)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Throwable
parameter_list|>
name|void
name|doWithWriteLockT
parameter_list|(
name|StampedLock
name|lock
parameter_list|,
name|ThrowingRunnable
argument_list|<
name|T
argument_list|>
name|task
parameter_list|)
throws|throws
name|T
block|{
name|long
name|stamp
init|=
name|lock
operator|.
name|writeLock
argument_list|()
decl_stmt|;
try|try
block|{
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlockWrite
argument_list|(
name|stamp
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|supplyWithWriteLockT (StampedLock lock, ThrowingSupplier<R, T> task)
specifier|public
specifier|static
parameter_list|<
name|R
parameter_list|,
name|T
extends|extends
name|Throwable
parameter_list|>
name|R
name|supplyWithWriteLockT
parameter_list|(
name|StampedLock
name|lock
parameter_list|,
name|ThrowingSupplier
argument_list|<
name|R
argument_list|,
name|T
argument_list|>
name|task
parameter_list|)
throws|throws
name|T
block|{
name|long
name|stamp
init|=
name|lock
operator|.
name|writeLock
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|task
operator|.
name|get
argument_list|()
return|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlockWrite
argument_list|(
name|stamp
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
