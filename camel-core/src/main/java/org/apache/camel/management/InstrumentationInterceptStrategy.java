begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|spi
operator|.
name|InterceptStrategy
import|;
end_import

begin_comment
comment|/**  * This strategy class wraps targeted processors with a  * {@link InstrumentationProcessor}. Each InstrumentationProcessor has an  * embedded {@link PerformanceCounter} for monitoring performance metrics.  *<p/>  * This class looks up a map to determine which PerformanceCounter should go into the  * InstrumentationProcessor for any particular target processor.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|InstrumentationInterceptStrategy
specifier|public
class|class
name|InstrumentationInterceptStrategy
implements|implements
name|InterceptStrategy
block|{
DECL|field|counterMap
specifier|private
specifier|final
name|Map
argument_list|<
name|ProcessorDefinition
argument_list|,
name|PerformanceCounter
argument_list|>
name|counterMap
decl_stmt|;
DECL|method|InstrumentationInterceptStrategy (Map<ProcessorDefinition, PerformanceCounter> counterMap)
specifier|public
name|InstrumentationInterceptStrategy
parameter_list|(
name|Map
argument_list|<
name|ProcessorDefinition
argument_list|,
name|PerformanceCounter
argument_list|>
name|counterMap
parameter_list|)
block|{
name|this
operator|.
name|counterMap
operator|=
name|counterMap
expr_stmt|;
block|}
DECL|method|wrapProcessorInInterceptors (ProcessorDefinition processorType, Processor target)
specifier|public
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
name|ProcessorDefinition
name|processorType
parameter_list|,
name|Processor
name|target
parameter_list|)
throws|throws
name|Exception
block|{
name|PerformanceCounter
name|counter
init|=
name|counterMap
operator|.
name|get
argument_list|(
name|processorType
argument_list|)
decl_stmt|;
if|if
condition|(
name|counter
operator|!=
literal|null
condition|)
block|{
name|InstrumentationProcessor
name|wrapper
init|=
operator|new
name|InstrumentationProcessor
argument_list|(
name|counter
argument_list|)
decl_stmt|;
name|wrapper
operator|.
name|setProcessor
argument_list|(
name|target
argument_list|)
expr_stmt|;
return|return
name|wrapper
return|;
block|}
return|return
name|target
return|;
block|}
block|}
end_class

end_unit

