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
name|CamelContext
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
name|api
operator|.
name|management
operator|.
name|PerformanceCounter
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
name|management
operator|.
name|mbean
operator|.
name|ManagedPerformanceCounter
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
name|KeyValueHolder
import|;
end_import

begin_comment
comment|/**  * This strategy class wraps targeted processors with a  * {@link InstrumentationProcessor}. Each InstrumentationProcessor has an  * embedded {@link ManagedPerformanceCounter} for monitoring performance metrics.  *<p/>  * This class looks up a map to determine which PerformanceCounter should go into the  * InstrumentationProcessor for any particular target processor.  *  * @version   */
end_comment

begin_class
DECL|class|InstrumentationInterceptStrategy
specifier|public
class|class
name|InstrumentationInterceptStrategy
implements|implements
name|InterceptStrategy
block|{
DECL|field|registeredCounters
specifier|private
name|Map
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|,
name|PerformanceCounter
argument_list|>
name|registeredCounters
decl_stmt|;
DECL|field|wrappedProcessors
specifier|private
specifier|final
name|Map
argument_list|<
name|Processor
argument_list|,
name|KeyValueHolder
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|,
name|InstrumentationProcessor
argument_list|>
argument_list|>
name|wrappedProcessors
decl_stmt|;
DECL|method|InstrumentationInterceptStrategy (Map<ProcessorDefinition<?>, PerformanceCounter> registeredCounters, Map<Processor, KeyValueHolder<ProcessorDefinition<?>, InstrumentationProcessor>> wrappedProcessors)
specifier|public
name|InstrumentationInterceptStrategy
parameter_list|(
name|Map
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|,
name|PerformanceCounter
argument_list|>
name|registeredCounters
parameter_list|,
name|Map
argument_list|<
name|Processor
argument_list|,
name|KeyValueHolder
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|,
name|InstrumentationProcessor
argument_list|>
argument_list|>
name|wrappedProcessors
parameter_list|)
block|{
name|this
operator|.
name|registeredCounters
operator|=
name|registeredCounters
expr_stmt|;
name|this
operator|.
name|wrappedProcessors
operator|=
name|wrappedProcessors
expr_stmt|;
block|}
DECL|method|wrapProcessorInInterceptors (CamelContext context, ProcessorDefinition<?> definition, Processor target, Processor nextTarget)
specifier|public
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|,
name|Processor
name|target
parameter_list|,
name|Processor
name|nextTarget
parameter_list|)
throws|throws
name|Exception
block|{
comment|// do not double wrap it
if|if
condition|(
name|target
operator|instanceof
name|InstrumentationProcessor
condition|)
block|{
return|return
name|target
return|;
block|}
comment|// only wrap a performance counter if we have it registered in JMX by the jmx agent
name|PerformanceCounter
name|counter
init|=
name|registeredCounters
operator|.
name|get
argument_list|(
name|definition
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
name|wrapper
operator|.
name|setType
argument_list|(
name|definition
operator|.
name|getShortName
argument_list|()
argument_list|)
expr_stmt|;
comment|// add it to the mapping of wrappers so we can later change it to a decorated counter
comment|// that when we register the processor
name|KeyValueHolder
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|,
name|InstrumentationProcessor
argument_list|>
name|holder
init|=
operator|new
name|KeyValueHolder
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|,
name|InstrumentationProcessor
argument_list|>
argument_list|(
name|definition
argument_list|,
name|wrapper
argument_list|)
decl_stmt|;
name|wrappedProcessors
operator|.
name|put
argument_list|(
name|target
argument_list|,
name|holder
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"InstrumentProcessor"
return|;
block|}
block|}
end_class

end_unit

