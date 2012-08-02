begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|support
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
name|Component
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
name|Consumer
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
name|DefaultEndpoint
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
name|SynchronousDelegateProducer
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|MyAsyncEndpoint
specifier|public
class|class
name|MyAsyncEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|reply
specifier|private
name|String
name|reply
decl_stmt|;
DECL|field|delay
specifier|private
name|long
name|delay
init|=
literal|25
decl_stmt|;
DECL|field|failFirstAttempts
specifier|private
name|int
name|failFirstAttempts
decl_stmt|;
DECL|method|MyAsyncEndpoint (String endpointUri, Component component)
specifier|public
name|MyAsyncEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|Producer
name|answer
init|=
operator|new
name|MyAsyncProducer
argument_list|(
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|isSynchronous
argument_list|()
condition|)
block|{
comment|// force it to be synchronously
return|return
operator|new
name|SynchronousDelegateProducer
argument_list|(
name|answer
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|answer
return|;
block|}
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Consumer not supported"
argument_list|)
throw|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|getReply ()
specifier|public
name|String
name|getReply
parameter_list|()
block|{
return|return
name|reply
return|;
block|}
DECL|method|setReply (String reply)
specifier|public
name|void
name|setReply
parameter_list|(
name|String
name|reply
parameter_list|)
block|{
name|this
operator|.
name|reply
operator|=
name|reply
expr_stmt|;
block|}
DECL|method|getDelay ()
specifier|public
name|long
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
DECL|method|setDelay (long delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
block|}
DECL|method|getFailFirstAttempts ()
specifier|public
name|int
name|getFailFirstAttempts
parameter_list|()
block|{
return|return
name|failFirstAttempts
return|;
block|}
DECL|method|setFailFirstAttempts (int failFirstAttempts)
specifier|public
name|void
name|setFailFirstAttempts
parameter_list|(
name|int
name|failFirstAttempts
parameter_list|)
block|{
name|this
operator|.
name|failFirstAttempts
operator|=
name|failFirstAttempts
expr_stmt|;
block|}
block|}
end_class

end_unit

