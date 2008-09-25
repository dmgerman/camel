begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|interceptor
package|;
end_package

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
name|NoTypeConversionAvailableException
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
name|converter
operator|.
name|stream
operator|.
name|StreamCache
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
name|model
operator|.
name|InterceptorRef
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
name|model
operator|.
name|InterceptorType
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
name|processor
operator|.
name|Interceptor
import|;
end_import

begin_comment
comment|/**  * {@link Interceptor} that converts a message into a re-readable format  */
end_comment

begin_class
DECL|class|StreamCachingInterceptor
specifier|public
class|class
name|StreamCachingInterceptor
extends|extends
name|Interceptor
block|{
DECL|method|StreamCachingInterceptor ()
specifier|public
name|StreamCachingInterceptor
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|setInterceptorLogic
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
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
try|try
block|{
name|StreamCache
name|newBody
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|StreamCache
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|newBody
operator|!=
literal|null
condition|)
block|{
name|newBody
operator|.
name|reset
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|newBody
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|ex
parameter_list|)
block|{
comment|// ignore if in is not of StreamCache type
block|}
name|proceed
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|StreamCachingInterceptor (Processor processor)
specifier|public
name|StreamCachingInterceptor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setProcessor
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"StreamCachingInterceptor"
return|;
block|}
comment|/**      * Remove the {@link StreamCachingInterceptor} type of interceptor from the given list of interceptors      *      * @param interceptors the list of interceptors      */
DECL|method|noStreamCaching (List<InterceptorType> interceptors)
specifier|public
specifier|static
name|void
name|noStreamCaching
parameter_list|(
name|List
argument_list|<
name|InterceptorType
argument_list|>
name|interceptors
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|interceptors
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|InterceptorType
name|interceptor
init|=
name|interceptors
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|interceptor
operator|instanceof
name|InterceptorRef
operator|&&
operator|(
operator|(
name|InterceptorRef
operator|)
name|interceptor
operator|)
operator|.
name|getInterceptor
argument_list|()
operator|instanceof
name|StreamCachingInterceptor
condition|)
block|{
name|interceptors
operator|.
name|remove
argument_list|(
name|interceptor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

