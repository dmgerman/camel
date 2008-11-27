begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|AsyncProcessor
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
name|util
operator|.
name|AsyncProcessorHelper
import|;
end_import

begin_class
DECL|class|HandleFaultProcessor
specifier|public
class|class
name|HandleFaultProcessor
extends|extends
name|DelegateProcessor
implements|implements
name|AsyncProcessor
block|{
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"HandleFaultProcessor("
operator|+
name|processor
operator|+
literal|")"
return|;
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
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|processor
operator|==
literal|null
condition|)
block|{
comment|// no processor so nothing to process, so return
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
if|if
condition|(
name|processor
operator|instanceof
name|AsyncProcessor
condition|)
block|{
return|return
operator|(
operator|(
name|AsyncProcessor
operator|)
name|processor
operator|)
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
name|doneSynchronously
parameter_list|)
block|{
comment|// Take the fault message out before we keep on going
name|Message
name|faultMessage
init|=
name|exchange
operator|.
name|getFault
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|faultMessage
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Object
name|faultBody
init|=
name|faultMessage
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|faultBody
operator|!=
literal|null
condition|)
block|{
name|faultMessage
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// Reset it since we are handling it.
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
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelException
argument_list|(
literal|"Message contains fault of type "
operator|+
name|faultBody
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|":\n"
operator|+
name|faultBody
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|callback
operator|.
name|done
argument_list|(
name|doneSynchronously
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
try|try
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Message
name|faultMessage
init|=
name|exchange
operator|.
name|getFault
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|faultMessage
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Object
name|faultBody
init|=
name|faultMessage
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|faultBody
operator|!=
literal|null
condition|)
block|{
name|faultMessage
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// Reset it since we are handling it.
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
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelException
argument_list|(
literal|"Message contains fault of type "
operator|+
name|faultBody
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|":\n"
operator|+
name|faultBody
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

