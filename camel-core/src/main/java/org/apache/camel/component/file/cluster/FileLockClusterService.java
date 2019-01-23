begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.cluster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|cluster
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
name|ScheduledExecutorService
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
name|TimeUnit
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
name|support
operator|.
name|cluster
operator|.
name|AbstractCamelClusterService
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

begin_class
DECL|class|FileLockClusterService
specifier|public
class|class
name|FileLockClusterService
extends|extends
name|AbstractCamelClusterService
argument_list|<
name|FileLockClusterView
argument_list|>
block|{
DECL|field|root
specifier|private
name|String
name|root
decl_stmt|;
DECL|field|acquireLockDelay
specifier|private
name|long
name|acquireLockDelay
decl_stmt|;
DECL|field|acquireLockDelayUnit
specifier|private
name|TimeUnit
name|acquireLockDelayUnit
decl_stmt|;
DECL|field|acquireLockInterval
specifier|private
name|long
name|acquireLockInterval
decl_stmt|;
DECL|field|acquireLockIntervalUnit
specifier|private
name|TimeUnit
name|acquireLockIntervalUnit
decl_stmt|;
DECL|field|executor
specifier|private
name|ScheduledExecutorService
name|executor
decl_stmt|;
DECL|method|FileLockClusterService ()
specifier|public
name|FileLockClusterService
parameter_list|()
block|{
name|this
operator|.
name|acquireLockDelay
operator|=
literal|1
expr_stmt|;
name|this
operator|.
name|acquireLockDelayUnit
operator|=
name|TimeUnit
operator|.
name|SECONDS
expr_stmt|;
name|this
operator|.
name|acquireLockInterval
operator|=
literal|10
expr_stmt|;
name|this
operator|.
name|acquireLockIntervalUnit
operator|=
name|TimeUnit
operator|.
name|SECONDS
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createView (String namespace)
specifier|protected
name|FileLockClusterView
name|createView
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|FileLockClusterView
argument_list|(
name|this
argument_list|,
name|namespace
argument_list|)
return|;
block|}
DECL|method|getRoot ()
specifier|public
name|String
name|getRoot
parameter_list|()
block|{
return|return
name|root
return|;
block|}
comment|/**      * Sets the root path.      */
DECL|method|setRoot (String root)
specifier|public
name|void
name|setRoot
parameter_list|(
name|String
name|root
parameter_list|)
block|{
name|this
operator|.
name|root
operator|=
name|root
expr_stmt|;
block|}
DECL|method|getAcquireLockDelay ()
specifier|public
name|long
name|getAcquireLockDelay
parameter_list|()
block|{
return|return
name|acquireLockDelay
return|;
block|}
comment|/**      * The time to wait before starting to try to acquire lock, default 1.      */
DECL|method|setAcquireLockDelay (long acquireLockDelay)
specifier|public
name|void
name|setAcquireLockDelay
parameter_list|(
name|long
name|acquireLockDelay
parameter_list|)
block|{
name|this
operator|.
name|acquireLockDelay
operator|=
name|acquireLockDelay
expr_stmt|;
block|}
DECL|method|setAcquireLockDelay (long pollDelay, TimeUnit pollDelayUnit)
specifier|public
name|void
name|setAcquireLockDelay
parameter_list|(
name|long
name|pollDelay
parameter_list|,
name|TimeUnit
name|pollDelayUnit
parameter_list|)
block|{
name|setAcquireLockDelay
argument_list|(
name|pollDelay
argument_list|)
expr_stmt|;
name|setAcquireLockDelayUnit
argument_list|(
name|pollDelayUnit
argument_list|)
expr_stmt|;
block|}
DECL|method|getAcquireLockDelayUnit ()
specifier|public
name|TimeUnit
name|getAcquireLockDelayUnit
parameter_list|()
block|{
return|return
name|acquireLockDelayUnit
return|;
block|}
comment|/**      * The time unit fo the acquireLockDelay, default to TimeUnit.SECONDS.      */
DECL|method|setAcquireLockDelayUnit (TimeUnit acquireLockDelayUnit)
specifier|public
name|void
name|setAcquireLockDelayUnit
parameter_list|(
name|TimeUnit
name|acquireLockDelayUnit
parameter_list|)
block|{
name|this
operator|.
name|acquireLockDelayUnit
operator|=
name|acquireLockDelayUnit
expr_stmt|;
block|}
DECL|method|getAcquireLockInterval ()
specifier|public
name|long
name|getAcquireLockInterval
parameter_list|()
block|{
return|return
name|acquireLockInterval
return|;
block|}
comment|/**      * The time to wait between attempts to try to acquire lock, default 10.      */
DECL|method|setAcquireLockInterval (long acquireLockInterval)
specifier|public
name|void
name|setAcquireLockInterval
parameter_list|(
name|long
name|acquireLockInterval
parameter_list|)
block|{
name|this
operator|.
name|acquireLockInterval
operator|=
name|acquireLockInterval
expr_stmt|;
block|}
DECL|method|setAcquireLockInterval (long pollInterval, TimeUnit pollIntervalUnit)
specifier|public
name|void
name|setAcquireLockInterval
parameter_list|(
name|long
name|pollInterval
parameter_list|,
name|TimeUnit
name|pollIntervalUnit
parameter_list|)
block|{
name|setAcquireLockInterval
argument_list|(
name|pollInterval
argument_list|)
expr_stmt|;
name|setAcquireLockIntervalUnit
argument_list|(
name|pollIntervalUnit
argument_list|)
expr_stmt|;
block|}
DECL|method|getAcquireLockIntervalUnit ()
specifier|public
name|TimeUnit
name|getAcquireLockIntervalUnit
parameter_list|()
block|{
return|return
name|acquireLockIntervalUnit
return|;
block|}
comment|/**      * The time unit fo the acquireLockInterva, default to TimeUnit.SECONDS.      */
DECL|method|setAcquireLockIntervalUnit (TimeUnit acquireLockIntervalUnit)
specifier|public
name|void
name|setAcquireLockIntervalUnit
parameter_list|(
name|TimeUnit
name|acquireLockIntervalUnit
parameter_list|)
block|{
name|this
operator|.
name|acquireLockIntervalUnit
operator|=
name|acquireLockIntervalUnit
expr_stmt|;
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|CamelContext
name|context
init|=
name|getCamelContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|executor
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdown
argument_list|(
name|executor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
name|executor
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|getExecutor ()
specifier|synchronized
name|ScheduledExecutorService
name|getExecutor
parameter_list|()
block|{
if|if
condition|(
name|executor
operator|==
literal|null
condition|)
block|{
comment|// Camel context should be set at this stage.
specifier|final
name|CamelContext
name|context
init|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|"CamelContext"
argument_list|)
decl_stmt|;
name|executor
operator|=
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadScheduledExecutor
argument_list|(
name|this
argument_list|,
literal|"FileLockClusterService-"
operator|+
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|executor
return|;
block|}
block|}
end_class

end_unit

