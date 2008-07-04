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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_comment
comment|/**  * Represents an exception that occurred when processing an exchange  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ExceptionEvent
specifier|public
class|class
name|ExceptionEvent
block|{
DECL|field|interceptor
specifier|private
specifier|final
name|DebugInterceptor
name|interceptor
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|exception
specifier|private
specifier|final
name|Throwable
name|exception
decl_stmt|;
DECL|method|ExceptionEvent (DebugInterceptor interceptor, Exchange exchange, Throwable exception)
specifier|public
name|ExceptionEvent
parameter_list|(
name|DebugInterceptor
name|interceptor
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|exception
parameter_list|)
block|{
name|this
operator|.
name|interceptor
operator|=
name|interceptor
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|exception
operator|=
name|exception
expr_stmt|;
block|}
DECL|method|getException ()
specifier|public
name|Throwable
name|getException
parameter_list|()
block|{
return|return
name|exception
return|;
block|}
DECL|method|getExchange ()
specifier|public
name|Exchange
name|getExchange
parameter_list|()
block|{
return|return
name|exchange
return|;
block|}
DECL|method|getInterceptor ()
specifier|public
name|DebugInterceptor
name|getInterceptor
parameter_list|()
block|{
return|return
name|interceptor
return|;
block|}
block|}
end_class

end_unit

