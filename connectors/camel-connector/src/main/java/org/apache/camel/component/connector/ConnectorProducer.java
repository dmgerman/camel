begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.connector
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|connector
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
name|RejectedExecutionException
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
name|Endpoint
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
name|Producer
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
name|DefaultProducer
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

begin_class
DECL|class|ConnectorProducer
specifier|public
class|class
name|ConnectorProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|producer
specifier|private
specifier|final
name|Producer
name|producer
decl_stmt|;
DECL|field|beforeProducer
specifier|private
specifier|final
name|Processor
name|beforeProducer
decl_stmt|;
DECL|field|afterProducer
specifier|private
specifier|final
name|Processor
name|afterProducer
decl_stmt|;
DECL|method|ConnectorProducer (Endpoint endpoint, Producer producer, Processor beforeProducer, Processor afterProducer)
specifier|public
name|ConnectorProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Producer
name|producer
parameter_list|,
name|Processor
name|beforeProducer
parameter_list|,
name|Processor
name|afterProducer
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|producer
operator|=
name|producer
expr_stmt|;
name|this
operator|.
name|beforeProducer
operator|=
name|beforeProducer
expr_stmt|;
name|this
operator|.
name|afterProducer
operator|=
name|afterProducer
expr_stmt|;
block|}
annotation|@
name|Override
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
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RejectedExecutionException
argument_list|()
throw|;
block|}
if|if
condition|(
name|beforeProducer
operator|!=
literal|null
condition|)
block|{
name|beforeProducer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|afterProducer
operator|!=
literal|null
condition|)
block|{
name|afterProducer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
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
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|producer
argument_list|)
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
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doSuspend ()
specifier|protected
name|void
name|doSuspend
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|suspendService
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doResume ()
specifier|protected
name|void
name|doResume
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|resumeService
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownService
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

