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
comment|/**  * {@link InterceptStrategy} implementation to configure stream caching on a RouteContext  */
end_comment

begin_class
DECL|class|StreamCaching
specifier|public
specifier|final
class|class
name|StreamCaching
implements|implements
name|InterceptStrategy
block|{
comment|/*      * Hide constructor -- instances will be created through static enable() methods      */
DECL|method|StreamCaching ()
specifier|private
name|StreamCaching
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
return|return
operator|new
name|StreamCachingInterceptor
argument_list|(
name|target
argument_list|)
return|;
block|}
comment|/**      * Enable stream caching for a RouteContext      *       * @param context the route context      */
DECL|method|enable (RouteContext context)
specifier|public
specifier|static
name|void
name|enable
parameter_list|(
name|RouteContext
name|context
parameter_list|)
block|{
for|for
control|(
name|InterceptStrategy
name|strategy
range|:
name|context
operator|.
name|getInterceptStrategies
argument_list|()
control|)
block|{
if|if
condition|(
name|strategy
operator|instanceof
name|StreamCaching
condition|)
block|{
return|return;
block|}
block|}
name|context
operator|.
name|addInterceptStrategy
argument_list|(
operator|new
name|StreamCaching
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Disable stream caching for a RouteContext      *       * @param context the route context      */
DECL|method|disable (RouteContext context)
specifier|public
specifier|static
name|void
name|disable
parameter_list|(
name|RouteContext
name|context
parameter_list|)
block|{
for|for
control|(
name|InterceptStrategy
name|strategy
range|:
name|context
operator|.
name|getInterceptStrategies
argument_list|()
control|)
block|{
if|if
condition|(
name|strategy
operator|instanceof
name|StreamCaching
condition|)
block|{
name|context
operator|.
name|getInterceptStrategies
argument_list|()
operator|.
name|remove
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
block|}
end_class

end_unit

