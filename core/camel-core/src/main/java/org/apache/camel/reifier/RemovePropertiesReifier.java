begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
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
name|model
operator|.
name|RemovePropertiesDefinition
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
name|RemovePropertiesProcessor
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
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|RemovePropertiesReifier
class|class
name|RemovePropertiesReifier
extends|extends
name|ProcessorReifier
argument_list|<
name|RemovePropertiesDefinition
argument_list|>
block|{
DECL|method|RemovePropertiesReifier (ProcessorDefinition<?> definition)
name|RemovePropertiesReifier
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|RemovePropertiesDefinition
operator|)
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|definition
operator|.
name|getPattern
argument_list|()
argument_list|,
literal|"patterns"
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|definition
operator|.
name|getExcludePatterns
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|RemovePropertiesProcessor
argument_list|(
name|definition
operator|.
name|getPattern
argument_list|()
argument_list|,
name|definition
operator|.
name|getExcludePatterns
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|definition
operator|.
name|getExcludePattern
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|RemovePropertiesProcessor
argument_list|(
name|definition
operator|.
name|getPattern
argument_list|()
argument_list|,
operator|new
name|String
index|[]
block|{
name|definition
operator|.
name|getExcludePattern
argument_list|()
block|}
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|RemovePropertiesProcessor
argument_list|(
name|definition
operator|.
name|getPattern
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

