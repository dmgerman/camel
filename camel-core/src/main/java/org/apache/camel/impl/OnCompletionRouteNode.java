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
name|RouteNode
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|OnCompletionRouteNode
specifier|public
class|class
name|OnCompletionRouteNode
implements|implements
name|RouteNode
block|{
DECL|method|OnCompletionRouteNode ()
specifier|public
name|OnCompletionRouteNode
parameter_list|()
block|{     }
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|getProcessorDefinition ()
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|getProcessorDefinition
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|getLabel (Exchange exchange)
specifier|public
name|String
name|getLabel
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
literal|"OnCompletion["
operator|+
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|CORRELATION_ID
argument_list|)
operator|+
literal|"]"
return|;
block|}
DECL|method|isAbstract ()
specifier|public
name|boolean
name|isAbstract
parameter_list|()
block|{
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
return|return
literal|"OnCompletionRouteNode"
return|;
block|}
block|}
end_class

end_unit

