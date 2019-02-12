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
name|FutureTask
import|;
end_import

begin_comment
comment|/**  * A {@link Rejectable} {@link FutureTask} used by {@link RejectableThreadPoolExecutor}.  *  * @see RejectableThreadPoolExecutor  */
end_comment

begin_class
DECL|class|RejectableFutureTask
specifier|public
class|class
name|RejectableFutureTask
parameter_list|<
name|V
parameter_list|>
extends|extends
name|FutureTask
argument_list|<
name|V
argument_list|>
implements|implements
name|Rejectable
block|{
DECL|field|rejectable
specifier|private
specifier|final
name|Rejectable
name|rejectable
decl_stmt|;
DECL|method|RejectableFutureTask (Callable<V> callable)
specifier|public
name|RejectableFutureTask
parameter_list|(
name|Callable
argument_list|<
name|V
argument_list|>
name|callable
parameter_list|)
block|{
name|super
argument_list|(
name|callable
argument_list|)
expr_stmt|;
name|this
operator|.
name|rejectable
operator|=
name|callable
operator|instanceof
name|Rejectable
condition|?
operator|(
name|Rejectable
operator|)
name|callable
else|:
literal|null
expr_stmt|;
block|}
DECL|method|RejectableFutureTask (Runnable runnable, V result)
specifier|public
name|RejectableFutureTask
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|V
name|result
parameter_list|)
block|{
name|super
argument_list|(
name|runnable
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|this
operator|.
name|rejectable
operator|=
name|runnable
operator|instanceof
name|Rejectable
condition|?
operator|(
name|Rejectable
operator|)
name|runnable
else|:
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|reject ()
specifier|public
name|void
name|reject
parameter_list|()
block|{
if|if
condition|(
name|rejectable
operator|!=
literal|null
condition|)
block|{
name|rejectable
operator|.
name|reject
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
