begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Collection
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
name|ArrayBlockingQueue
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
name|BlockingQueue
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
name|ConcurrentHashMap
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
name|ServicePool
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
name|ServiceHelper
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

begin_comment
comment|/**  * Default implementation to inherit for a basic service pool.  *  * @version   */
end_comment

begin_class
DECL|class|DefaultServicePool
specifier|public
specifier|abstract
class|class
name|DefaultServicePool
parameter_list|<
name|Key
parameter_list|,
name|Service
parameter_list|>
extends|extends
name|ServiceSupport
implements|implements
name|ServicePool
argument_list|<
name|Key
argument_list|,
name|Service
argument_list|>
block|{
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|pool
specifier|protected
specifier|final
name|ConcurrentHashMap
argument_list|<
name|Key
argument_list|,
name|BlockingQueue
argument_list|<
name|Service
argument_list|>
argument_list|>
name|pool
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Key
argument_list|,
name|BlockingQueue
argument_list|<
name|Service
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|capacity
specifier|protected
name|int
name|capacity
init|=
literal|100
decl_stmt|;
DECL|method|DefaultServicePool ()
specifier|protected
name|DefaultServicePool
parameter_list|()
block|{     }
DECL|method|DefaultServicePool (int capacity)
specifier|public
name|DefaultServicePool
parameter_list|(
name|int
name|capacity
parameter_list|)
block|{
name|this
operator|.
name|capacity
operator|=
name|capacity
expr_stmt|;
block|}
DECL|method|getCapacity ()
specifier|public
name|int
name|getCapacity
parameter_list|()
block|{
return|return
name|capacity
return|;
block|}
DECL|method|setCapacity (int capacity)
specifier|public
name|void
name|setCapacity
parameter_list|(
name|int
name|capacity
parameter_list|)
block|{
name|this
operator|.
name|capacity
operator|=
name|capacity
expr_stmt|;
block|}
DECL|method|size ()
specifier|public
specifier|synchronized
name|int
name|size
parameter_list|()
block|{
name|int
name|size
init|=
literal|0
decl_stmt|;
for|for
control|(
name|BlockingQueue
argument_list|<
name|Service
argument_list|>
name|entry
range|:
name|pool
operator|.
name|values
argument_list|()
control|)
block|{
name|size
operator|+=
name|entry
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
return|return
name|size
return|;
block|}
DECL|method|addAndAcquire (Key key, Service service)
specifier|public
specifier|synchronized
name|Service
name|addAndAcquire
parameter_list|(
name|Key
name|key
parameter_list|,
name|Service
name|service
parameter_list|)
block|{
name|BlockingQueue
argument_list|<
name|Service
argument_list|>
name|entry
init|=
name|pool
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
name|entry
operator|=
operator|new
name|ArrayBlockingQueue
argument_list|<
name|Service
argument_list|>
argument_list|(
name|capacity
argument_list|)
expr_stmt|;
name|pool
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|entry
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"AddAndAcquire key: {} service: {}"
argument_list|,
name|key
argument_list|,
name|service
argument_list|)
expr_stmt|;
comment|// test if queue will be full
if|if
condition|(
name|entry
operator|.
name|size
argument_list|()
operator|>=
name|capacity
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Queue full"
argument_list|)
throw|;
block|}
return|return
name|service
return|;
block|}
DECL|method|acquire (Key key)
specifier|public
specifier|synchronized
name|Service
name|acquire
parameter_list|(
name|Key
name|key
parameter_list|)
block|{
name|BlockingQueue
argument_list|<
name|Service
argument_list|>
name|services
init|=
name|pool
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|services
operator|==
literal|null
operator|||
name|services
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"No free services in pool to acquire for key: {}"
argument_list|,
name|key
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|Service
name|answer
init|=
name|services
operator|.
name|poll
argument_list|()
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Acquire: {} service: {}"
argument_list|,
name|key
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|release (Key key, Service service)
specifier|public
specifier|synchronized
name|void
name|release
parameter_list|(
name|Key
name|key
parameter_list|,
name|Service
name|service
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Release: {} service: {}"
argument_list|,
name|key
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|BlockingQueue
argument_list|<
name|Service
argument_list|>
name|services
init|=
name|pool
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|services
operator|!=
literal|null
condition|)
block|{
name|services
operator|.
name|add
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|purge ()
specifier|public
name|void
name|purge
parameter_list|()
block|{
name|pool
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Starting service pool: {}"
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Stopping service pool: {}"
argument_list|,
name|this
argument_list|)
expr_stmt|;
for|for
control|(
name|BlockingQueue
argument_list|<
name|Service
argument_list|>
name|entry
range|:
name|pool
operator|.
name|values
argument_list|()
control|)
block|{
name|Collection
argument_list|<
name|Service
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|Service
argument_list|>
argument_list|()
decl_stmt|;
name|entry
operator|.
name|drainTo
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|entry
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|pool
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

