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
name|ProcessorType
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
name|InterceptStrategy
import|;
end_import

begin_comment
comment|/**  * An interceptor strategy for tracing routes  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|Tracer
specifier|public
class|class
name|Tracer
implements|implements
name|InterceptStrategy
block|{
DECL|field|formatter
specifier|private
name|TraceFormatter
name|formatter
init|=
operator|new
name|TraceFormatter
argument_list|()
decl_stmt|;
DECL|method|wrapProcessorInInterceptors (ProcessorType processorType, Processor target)
specifier|public
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
name|ProcessorType
name|processorType
parameter_list|,
name|Processor
name|target
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Force the creation of an id, otherwise the id is not available when the trace formatter is
comment|// outputting trace information
name|String
name|id
init|=
name|processorType
operator|.
name|idOrCreate
argument_list|()
decl_stmt|;
return|return
operator|new
name|TraceInterceptor
argument_list|(
name|processorType
argument_list|,
name|target
argument_list|,
name|formatter
argument_list|)
return|;
block|}
DECL|method|getFormatter ()
specifier|public
name|TraceFormatter
name|getFormatter
parameter_list|()
block|{
return|return
name|formatter
return|;
block|}
DECL|method|setFormatter (TraceFormatter formatter)
specifier|public
name|void
name|setFormatter
parameter_list|(
name|TraceFormatter
name|formatter
parameter_list|)
block|{
name|this
operator|.
name|formatter
operator|=
name|formatter
expr_stmt|;
block|}
block|}
end_class

end_unit

