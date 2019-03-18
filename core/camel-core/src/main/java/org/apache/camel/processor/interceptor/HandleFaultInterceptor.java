begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|CamelException
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
name|Message
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
name|support
operator|.
name|processor
operator|.
name|DelegateAsyncProcessor
import|;
end_import

begin_class
DECL|class|HandleFaultInterceptor
specifier|public
class|class
name|HandleFaultInterceptor
extends|extends
name|DelegateAsyncProcessor
block|{
DECL|method|HandleFaultInterceptor ()
specifier|public
name|HandleFaultInterceptor
parameter_list|()
block|{     }
DECL|method|HandleFaultInterceptor (Processor processor)
specifier|public
name|HandleFaultInterceptor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|super
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
literal|"HandleFaultInterceptor["
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
return|return
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
try|try
block|{
comment|// handle fault after we are done
name|handleFault
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// and let the original callback know we are done as well
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
return|;
block|}
comment|/**      * Handles the fault message by converting it to an Exception      */
DECL|method|handleFault (Exchange exchange)
specifier|protected
name|void
name|handleFault
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// Take the fault message out before we keep on going
name|Message
name|msg
init|=
name|exchange
operator|.
name|hasOut
argument_list|()
condition|?
name|exchange
operator|.
name|getOut
argument_list|()
else|:
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|.
name|isFault
argument_list|()
condition|)
block|{
specifier|final
name|Object
name|faultBody
init|=
name|msg
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|faultBody
operator|!=
literal|null
operator|&&
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// remove fault as we are converting it to an exception
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setOut
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|setIn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|faultBody
operator|instanceof
name|Throwable
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|(
name|Throwable
operator|)
name|faultBody
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// wrap it in an exception
name|String
name|data
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|faultBody
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelException
argument_list|(
name|data
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

