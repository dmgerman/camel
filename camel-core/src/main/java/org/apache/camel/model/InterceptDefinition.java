begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|Predicate
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
name|spi
operator|.
name|AsPredicate
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
name|Metadata
import|;
end_import

begin_comment
comment|/**  * Intercepts a message at each step in the route  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"configuration"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"intercept"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|InterceptDefinition
specifier|public
class|class
name|InterceptDefinition
extends|extends
name|OutputDefinition
argument_list|<
name|InterceptDefinition
argument_list|>
block|{
annotation|@
name|XmlTransient
DECL|field|intercepted
specifier|protected
specifier|final
name|List
argument_list|<
name|Processor
argument_list|>
name|intercepted
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|InterceptDefinition ()
specifier|public
name|InterceptDefinition
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Intercept["
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"intercept"
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
literal|"intercept"
return|;
block|}
annotation|@
name|Override
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
DECL|method|isTopLevelOnly ()
specifier|public
name|boolean
name|isTopLevelOnly
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Applies this interceptor only if the given predicate is true      *      * @param predicate the predicate      * @return the builder      */
DECL|method|when (@sPredicate Predicate predicate)
specifier|public
name|InterceptDefinition
name|when
parameter_list|(
annotation|@
name|AsPredicate
name|Predicate
name|predicate
parameter_list|)
block|{
name|WhenDefinition
name|when
init|=
operator|new
name|WhenDefinition
argument_list|(
name|predicate
argument_list|)
decl_stmt|;
name|addOutput
argument_list|(
name|when
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * This method is<b>only</b> for handling some post configuration      * that is needed since this is an interceptor, and we have to do      * a bit of magic logic to fixup to handle predicates      * with or without proceed/stop set as well.      */
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
block|{
if|if
condition|(
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// no outputs
return|return;
block|}
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|first
init|=
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|first
operator|instanceof
name|WhenDefinition
condition|)
block|{
name|WhenDefinition
name|when
init|=
operator|(
name|WhenDefinition
operator|)
name|first
decl_stmt|;
comment|// move this outputs to the when, expect the first one
comment|// as the first one is the interceptor itself
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|outputs
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|out
init|=
name|outputs
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|when
operator|.
name|addOutput
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
comment|// remove the moved from the original output, by just keeping the first one
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|keep
init|=
name|outputs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|clearOutput
argument_list|()
expr_stmt|;
name|outputs
operator|.
name|add
argument_list|(
name|keep
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getIntercepted ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|getIntercepted
parameter_list|()
block|{
return|return
name|intercepted
return|;
block|}
DECL|method|getInterceptedProcessor (int index)
specifier|public
name|Processor
name|getInterceptedProcessor
parameter_list|(
name|int
name|index
parameter_list|)
block|{
comment|// avoid out of bounds
if|if
condition|(
name|index
operator|<=
name|intercepted
operator|.
name|size
argument_list|()
operator|-
literal|1
condition|)
block|{
return|return
name|intercepted
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

