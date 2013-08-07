begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * Stores state for {@link NettyProducer} when sending messages.  *<p/>  * This allows the {@link org.apache.camel.component.netty.handlers.ClientChannelHandler} to access  * this state, which is needed so we can get hold of the current {@link Exchange} and the  * {@link AsyncCallback} so we can continue routing the message in the Camel routing engine.  */
end_comment

begin_class
DECL|class|NettyCamelState
specifier|public
specifier|final
class|class
name|NettyCamelState
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|callback
specifier|private
specifier|final
name|AsyncCallback
name|callback
decl_stmt|;
DECL|method|NettyCamelState (AsyncCallback callback, Exchange exchange)
specifier|public
name|NettyCamelState
parameter_list|(
name|AsyncCallback
name|callback
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
block|}
DECL|method|getCallback ()
specifier|public
name|AsyncCallback
name|getCallback
parameter_list|()
block|{
return|return
name|callback
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
block|}
end_class

end_unit

