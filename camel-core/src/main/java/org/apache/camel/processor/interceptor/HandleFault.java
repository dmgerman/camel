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
comment|/**  * {@link org.apache.camel.spi.InterceptStrategy} implementation to handle faults as exceptions on a RouteContext  */
end_comment

begin_class
DECL|class|HandleFault
specifier|public
specifier|final
class|class
name|HandleFault
implements|implements
name|InterceptStrategy
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|wrapProcessorInInterceptors (ProcessorDefinition processorDefinition, Processor target)
specifier|public
name|Processor
name|wrapProcessorInInterceptors
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
return|return
operator|new
name|HandleFaultInterceptor
argument_list|(
name|target
argument_list|)
return|;
block|}
comment|/**      * A helper method to return the HandleFault instance      * for a given {@link org.apache.camel.CamelContext} if one is enabled      *      * @param context the camel context the handlefault intercept strategy is connected to      * @return the stream cache or null if none can be found      */
DECL|method|getHandleFault (CamelContext context)
specifier|public
specifier|static
name|HandleFault
name|getHandleFault
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|list
init|=
name|context
operator|.
name|getInterceptStrategies
argument_list|()
decl_stmt|;
for|for
control|(
name|InterceptStrategy
name|interceptStrategy
range|:
name|list
control|)
block|{
if|if
condition|(
name|interceptStrategy
operator|instanceof
name|HandleFault
condition|)
block|{
return|return
operator|(
name|HandleFault
operator|)
name|interceptStrategy
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

