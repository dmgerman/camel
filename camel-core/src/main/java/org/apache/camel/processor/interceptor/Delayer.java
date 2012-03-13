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
comment|/**  * An interceptor strategy for delaying routes.  */
end_comment

begin_class
DECL|class|Delayer
specifier|public
class|class
name|Delayer
implements|implements
name|InterceptStrategy
block|{
DECL|field|enabled
specifier|private
specifier|volatile
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
DECL|field|delay
specifier|private
specifier|final
name|long
name|delay
decl_stmt|;
DECL|method|Delayer (long delay)
specifier|public
name|Delayer
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
comment|/**      * A helper method to return the Delayer instance for a given {@link org.apache.camel.CamelContext} if one is enabled      *      * @param context the camel context the delayer is connected to      * @return the delayer or null if none can be found      */
DECL|method|getDelayer (CamelContext context)
specifier|public
specifier|static
name|Delayer
name|getDelayer
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
name|Delayer
condition|)
block|{
return|return
operator|(
name|Delayer
operator|)
name|interceptStrategy
return|;
block|}
block|}
return|return
literal|null
return|;
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
return|return
operator|new
name|DelayInterceptor
argument_list|(
name|context
argument_list|,
name|definition
argument_list|,
name|target
argument_list|,
name|this
argument_list|)
return|;
block|}
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Delayer["
operator|+
name|delay
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

