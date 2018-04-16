begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|Service
import|;
end_import

begin_comment
comment|/**  * A simple converter that can convert any {@link Processor} to an {@link AsyncProcessor}.  * Processing will still occur synchronously but it will provide the required  * notifications that the caller expects.  *  * @version   */
end_comment

begin_class
DECL|class|AsyncProcessorConverterHelper
specifier|public
specifier|final
class|class
name|AsyncProcessorConverterHelper
block|{
DECL|method|AsyncProcessorConverterHelper ()
specifier|private
name|AsyncProcessorConverterHelper
parameter_list|()
block|{
comment|// Helper class
block|}
comment|/**      * Creates a {@link AsyncProcessor} that delegates to the given processor.      * It is important that this implements {@link DelegateProcessor}      */
DECL|class|ProcessorToAsyncProcessorBridge
specifier|private
specifier|static
specifier|final
class|class
name|ProcessorToAsyncProcessorBridge
implements|implements
name|DelegateProcessor
implements|,
name|AsyncProcessor
implements|,
name|Navigate
argument_list|<
name|Processor
argument_list|>
implements|,
name|Service
block|{
DECL|field|processor
specifier|protected
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|method|ProcessorToAsyncProcessorBridge (Processor processor)
specifier|private
name|ProcessorToAsyncProcessorBridge
parameter_list|(
name|Processor
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
if|if
condition|(
name|processor
operator|==
literal|null
condition|)
block|{
comment|// no processor then we are done
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
comment|// must catch throwable so we catch all
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// we are bridging a sync processor as async so callback with true
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
return|return
name|processor
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|"Processor is null"
return|;
block|}
block|}
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
name|processNext
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|processNext (Exchange exchange)
specifier|protected
name|void
name|processNext
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|processor
argument_list|)
expr_stmt|;
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
annotation|@
name|Override
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|processor
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ProcessorToAsyncProcessorBridge
name|that
init|=
operator|(
name|ProcessorToAsyncProcessorBridge
operator|)
name|o
decl_stmt|;
return|return
name|processor
operator|.
name|equals
argument_list|(
name|that
operator|.
name|processor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
return|return
name|processor
operator|.
name|hashCode
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
block|}
block|}
DECL|method|convert (Processor value)
specifier|public
specifier|static
name|AsyncProcessor
name|convert
parameter_list|(
name|Processor
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|AsyncProcessor
condition|)
block|{
return|return
operator|(
name|AsyncProcessor
operator|)
name|value
return|;
block|}
return|return
operator|new
name|ProcessorToAsyncProcessorBridge
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

