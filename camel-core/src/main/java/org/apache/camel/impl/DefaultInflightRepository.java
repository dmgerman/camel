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
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|atomic
operator|.
name|AtomicInteger
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
name|spi
operator|.
name|InflightRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Default implement which just uses a counter  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultInflightRepository
specifier|public
class|class
name|DefaultInflightRepository
extends|extends
name|ServiceSupport
implements|implements
name|InflightRepository
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DefaultInflightRepository
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|totalCount
specifier|private
specifier|final
name|AtomicInteger
name|totalCount
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
comment|// us endpoint key as key so endpoints with lenient properties is registered using the same key (eg dynamic http endpoints)
DECL|field|endpointCount
specifier|private
specifier|final
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|AtomicInteger
argument_list|>
name|endpointCount
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|AtomicInteger
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|add (Exchange exchange)
specifier|public
name|void
name|add
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|totalCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|String
name|key
init|=
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
operator|.
name|getEndpointKey
argument_list|()
decl_stmt|;
name|AtomicInteger
name|existing
init|=
name|endpointCount
operator|.
name|putIfAbsent
argument_list|(
name|key
argument_list|,
operator|new
name|AtomicInteger
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
operator|!=
literal|null
condition|)
block|{
name|existing
operator|.
name|addAndGet
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|remove (Exchange exchange)
specifier|public
name|void
name|remove
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|totalCount
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|String
name|key
init|=
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
operator|.
name|getEndpointKey
argument_list|()
decl_stmt|;
name|AtomicInteger
name|existing
init|=
name|endpointCount
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
operator|!=
literal|null
condition|)
block|{
name|existing
operator|.
name|addAndGet
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|totalCount
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|size (Endpoint endpoint)
specifier|public
name|int
name|size
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|AtomicInteger
name|answer
init|=
name|endpointCount
operator|.
name|get
argument_list|(
name|endpoint
operator|.
name|getEndpointKey
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|answer
operator|!=
literal|null
condition|?
name|answer
operator|.
name|get
argument_list|()
else|:
literal|0
return|;
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
block|{     }
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
name|int
name|count
init|=
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|count
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Shutting down while there are still "
operator|+
name|count
operator|+
literal|" in flight exchanges."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Shutting down with no inflight exchanges."
argument_list|)
expr_stmt|;
block|}
name|endpointCount
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

