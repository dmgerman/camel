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
name|impl
operator|.
name|ServiceSupport
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
name|converter
operator|.
name|AsyncProcessorTypeConverter
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

begin_comment
comment|/**  * A bridge to have regular interceptors implemented as {@link org.apache.camel.Processor}  * work with the asynchronous routing engine without causing side effects.  *  * @version   */
end_comment

begin_class
DECL|class|InterceptorToAsyncProcessorBridge
specifier|public
class|class
name|InterceptorToAsyncProcessorBridge
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
block|{
DECL|field|interceptor
specifier|private
specifier|final
name|AsyncProcessor
name|interceptor
decl_stmt|;
DECL|field|target
specifier|private
specifier|volatile
name|AsyncProcessor
name|target
decl_stmt|;
DECL|field|callback
specifier|private
specifier|volatile
name|ThreadLocal
argument_list|<
name|AsyncCallback
argument_list|>
name|callback
init|=
operator|new
name|ThreadLocal
argument_list|<
name|AsyncCallback
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|interceptorDone
specifier|private
specifier|volatile
name|ThreadLocal
argument_list|<
name|Boolean
argument_list|>
name|interceptorDone
init|=
operator|new
name|ThreadLocal
argument_list|<
name|Boolean
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Constructs the bridge      *      * @param interceptor the interceptor to bridge      */
DECL|method|InterceptorToAsyncProcessorBridge (Processor interceptor)
specifier|public
name|InterceptorToAsyncProcessorBridge
parameter_list|(
name|Processor
name|interceptor
parameter_list|)
block|{
name|this
argument_list|(
name|interceptor
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructs the bridge      *      * @param interceptor the interceptor to bridge      * @param target the target      */
DECL|method|InterceptorToAsyncProcessorBridge (Processor interceptor, AsyncProcessor target)
specifier|public
name|InterceptorToAsyncProcessorBridge
parameter_list|(
name|Processor
name|interceptor
parameter_list|,
name|AsyncProcessor
name|target
parameter_list|)
block|{
name|this
operator|.
name|interceptor
operator|=
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|interceptor
argument_list|)
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
block|}
comment|/**      * Process invoked by the interceptor      * @param exchange the message exchange      * @throws Exception      */
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
comment|// invoke when interceptor wants to invoke
name|boolean
name|done
init|=
name|interceptor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|interceptorDone
operator|.
name|set
argument_list|(
name|done
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
comment|// remember the callback to be used by the interceptor
name|this
operator|.
name|callback
operator|.
name|set
argument_list|(
name|callback
argument_list|)
expr_stmt|;
try|try
block|{
comment|// invoke the target
name|boolean
name|done
init|=
name|target
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
decl_stmt|;
if|if
condition|(
name|interceptorDone
operator|.
name|get
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// return the result from the interceptor if it was invoked
return|return
name|interceptorDone
operator|.
name|get
argument_list|()
return|;
block|}
else|else
block|{
comment|// otherwise from the target
return|return
name|done
return|;
block|}
block|}
finally|finally
block|{
comment|// cleanup
name|this
operator|.
name|callback
operator|.
name|remove
argument_list|()
expr_stmt|;
name|this
operator|.
name|interceptorDone
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|setTarget (Processor target)
specifier|public
name|void
name|setTarget
parameter_list|(
name|Processor
name|target
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|target
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
literal|"AsyncBridge["
operator|+
name|interceptor
operator|.
name|toString
argument_list|()
operator|+
literal|"]"
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
block|{
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|target
argument_list|,
name|interceptor
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
name|callback
operator|.
name|remove
argument_list|()
expr_stmt|;
name|interceptorDone
operator|.
name|remove
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|interceptor
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

