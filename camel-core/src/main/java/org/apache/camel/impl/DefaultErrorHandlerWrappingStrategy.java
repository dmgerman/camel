begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|model
operator|.
name|ProcessorDefinition
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
name|ErrorHandler
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
name|ErrorHandlerWrappingStrategy
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
comment|/**  * The default error handler wrapping strategy used when JMX is disabled.  *  * @version $Revision$  * @deprecated is replaced by {@link org.apache.camel.Channel}  */
end_comment

begin_class
DECL|class|DefaultErrorHandlerWrappingStrategy
specifier|public
class|class
name|DefaultErrorHandlerWrappingStrategy
implements|implements
name|ErrorHandlerWrappingStrategy
block|{
DECL|field|routeContext
specifier|private
specifier|final
name|RouteContext
name|routeContext
decl_stmt|;
DECL|method|DefaultErrorHandlerWrappingStrategy (RouteContext routeContext)
specifier|public
name|DefaultErrorHandlerWrappingStrategy
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|this
operator|.
name|routeContext
operator|=
name|routeContext
expr_stmt|;
block|}
DECL|method|wrapProcessorInErrorHandler (ProcessorDefinition processorDefinition, Processor target)
specifier|public
name|Processor
name|wrapProcessorInErrorHandler
parameter_list|(
name|ProcessorDefinition
name|processorDefinition
parameter_list|,
name|Processor
name|target
parameter_list|)
throws|throws
name|Exception
block|{
comment|// dont double wrap error handlers
if|if
condition|(
name|target
operator|instanceof
name|ErrorHandler
condition|)
block|{
return|return
name|target
return|;
block|}
return|return
name|processorDefinition
operator|.
name|getErrorHandlerBuilder
argument_list|()
operator|.
name|createErrorHandler
argument_list|(
name|routeContext
argument_list|,
name|target
argument_list|)
return|;
block|}
block|}
end_class

end_unit

