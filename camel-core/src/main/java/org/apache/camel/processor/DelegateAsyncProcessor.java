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
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|DelegateProcessor
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
name|Navigate
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
name|spi
operator|.
name|AsyncProcessorAwaitManager
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
name|AsyncProcessorConverterHelper
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
name|ServiceHelper
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
name|ServiceSupport
import|;
end_import

begin_comment
comment|/**  * A Delegate pattern which delegates processing to a nested {@link AsyncProcessor} which can  * be useful for implementation inheritance when writing an {@link org.apache.camel.spi.Policy}  *<p/>  *<b>Important:</b> This implementation<b>does</b> support the asynchronous routing engine.  * If you are implementing a EIP pattern please use this as the delegate.  * @see DelegateSyncProcessor  * @see org.apache.camel.processor.DelegateProcessor  */
end_comment

begin_class
DECL|class|DelegateAsyncProcessor
specifier|public
class|class
name|DelegateAsyncProcessor
extends|extends
name|ServiceSupport
implements|implements
name|DelegateProcessor
implements|,
name|AsyncProcessor
implements|,
name|Navigate
argument_list|<
name|Processor
argument_list|>
block|{
DECL|field|processor
specifier|protected
name|AsyncProcessor
name|processor
decl_stmt|;
DECL|method|DelegateAsyncProcessor ()
specifier|public
name|DelegateAsyncProcessor
parameter_list|()
block|{     }
DECL|method|DelegateAsyncProcessor (AsyncProcessor processor)
specifier|public
name|DelegateAsyncProcessor
parameter_list|(
name|AsyncProcessor
name|processor
parameter_list|)
block|{
if|if
condition|(
name|processor
operator|==
name|this
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Recursive DelegateAsyncProcessor!"
argument_list|)
throw|;
block|}
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
DECL|method|DelegateAsyncProcessor (Processor processor)
specifier|public
name|DelegateAsyncProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|this
argument_list|(
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
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
literal|"DelegateAsync["
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
DECL|method|getProcessor ()
specifier|public
name|AsyncProcessor
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
DECL|method|setProcessor (AsyncProcessor processor)
specifier|public
name|void
name|setProcessor
parameter_list|(
name|AsyncProcessor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
DECL|method|setProcessor (Processor processor)
specifier|public
name|void
name|setProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|processor
operator|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
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
name|startService
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|processor
argument_list|)
expr_stmt|;
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
specifier|final
name|AsyncProcessorAwaitManager
name|awaitManager
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getAsyncProcessorAwaitManager
argument_list|()
decl_stmt|;
name|awaitManager
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
return|return
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|processor
operator|!=
literal|null
return|;
block|}
DECL|method|next ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|Processor
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|processor
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

