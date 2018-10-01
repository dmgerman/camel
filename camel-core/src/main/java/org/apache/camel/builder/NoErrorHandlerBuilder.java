begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|processor
operator|.
name|DelegateAsyncProcessor
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
name|RouteContext
import|;
end_import

begin_comment
comment|/**  * A builder to disable the use of an error handler so that any exceptions are thrown.  * This not recommended in general, the  *<a href="http://camel.apache.org/dead-letter-channel.html">Dead Letter Channel</a> should be used  * if you are unsure; however it can be useful sometimes to disable an error handler inside a complex route  * so that exceptions bubble up to the parent {@link Processor}  */
end_comment

begin_class
DECL|class|NoErrorHandlerBuilder
specifier|public
class|class
name|NoErrorHandlerBuilder
extends|extends
name|ErrorHandlerBuilderSupport
block|{
DECL|method|createErrorHandler (RouteContext routeContext, Processor processor)
specifier|public
name|Processor
name|createErrorHandler
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
return|return
operator|new
name|DelegateAsyncProcessor
argument_list|(
name|processor
argument_list|)
block|{
annotation|@
name|Override
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
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_EXHAUSTED
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|processor
operator|==
literal|null
condition|)
block|{
comment|// if no output then dont do any description
return|return
literal|""
return|;
block|}
return|return
literal|"NoErrorHandler["
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
block|}
return|;
block|}
DECL|method|supportTransacted ()
specifier|public
name|boolean
name|supportTransacted
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|cloneBuilder ()
specifier|public
name|ErrorHandlerBuilder
name|cloneBuilder
parameter_list|()
block|{
name|NoErrorHandlerBuilder
name|answer
init|=
operator|new
name|NoErrorHandlerBuilder
argument_list|()
decl_stmt|;
name|cloneBuilder
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

