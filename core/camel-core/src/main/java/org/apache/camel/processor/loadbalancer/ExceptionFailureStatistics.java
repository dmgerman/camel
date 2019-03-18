begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.loadbalancer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|loadbalancer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Map
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
name|AtomicLong
import|;
end_import

begin_comment
comment|/**  * Statistics about exception failures for load balancers that reacts on exceptions  */
end_comment

begin_class
DECL|class|ExceptionFailureStatistics
specifier|public
class|class
name|ExceptionFailureStatistics
block|{
DECL|field|counters
specifier|private
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|AtomicLong
argument_list|>
name|counters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|fallbackCounter
specifier|private
specifier|final
name|AtomicLong
name|fallbackCounter
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|method|init (List<Class<?>> exceptions)
specifier|public
name|void
name|init
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|exceptions
parameter_list|)
block|{
if|if
condition|(
name|exceptions
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|exception
range|:
name|exceptions
control|)
block|{
name|counters
operator|.
name|put
argument_list|(
name|exception
argument_list|,
operator|new
name|AtomicLong
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getExceptions ()
specifier|public
name|Iterator
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getExceptions
parameter_list|()
block|{
return|return
name|counters
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
DECL|method|getFailureCounter (Class<?> exception)
specifier|public
name|long
name|getFailureCounter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|exception
parameter_list|)
block|{
name|AtomicLong
name|counter
init|=
name|counters
operator|.
name|get
argument_list|(
name|exception
argument_list|)
decl_stmt|;
if|if
condition|(
name|counter
operator|!=
literal|null
condition|)
block|{
return|return
name|counter
operator|.
name|get
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|fallbackCounter
operator|.
name|get
argument_list|()
return|;
block|}
block|}
DECL|method|onHandledFailure (Exception exception)
specifier|public
name|void
name|onHandledFailure
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|exception
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|AtomicLong
name|counter
init|=
name|counters
operator|.
name|get
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|counter
operator|!=
literal|null
condition|)
block|{
name|counter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|fallbackCounter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
for|for
control|(
name|AtomicLong
name|counter
range|:
name|counters
operator|.
name|values
argument_list|()
control|)
block|{
name|counter
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|fallbackCounter
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

