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
name|List
import|;
end_import

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
name|Iterator
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
name|XmlAttribute
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
name|processor
operator|.
name|OnCompletionProcessor
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
comment|/**  * Represents an XML&lt;onCompletion/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"onCompletion"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|OnCompletionDefinition
specifier|public
class|class
name|OnCompletionDefinition
extends|extends
name|OutputDefinition
argument_list|<
name|OnCompletionDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|onCompleteOnly
specifier|private
name|Boolean
name|onCompleteOnly
init|=
name|Boolean
operator|.
name|TRUE
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|onFailureOnly
specifier|private
name|Boolean
name|onFailureOnly
init|=
name|Boolean
operator|.
name|TRUE
decl_stmt|;
DECL|method|OnCompletionDefinition ()
specifier|public
name|OnCompletionDefinition
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
literal|"Synchronize["
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
literal|"onCompletion"
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
literal|"onCompletion"
return|;
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
name|Processor
name|childProcessor
init|=
name|createOutputsProcessor
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
return|return
operator|new
name|OnCompletionProcessor
argument_list|(
name|childProcessor
argument_list|,
name|onCompleteOnly
argument_list|,
name|onFailureOnly
argument_list|)
return|;
block|}
comment|/**      * Removes all existing {@link org.apache.camel.model.OnCompletionDefinition} from the defintion.      *<p/>      * This is used to let route scoped<tt>onCompletion</tt> overrule any global<tt>onCompletion</tt>.      * Hence we remove all existing as they are global.      *      * @param definition the parent defintion that is the route       */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|removeAllOnCompletionDefinition (ProcessorDefinition definition)
specifier|public
name|void
name|removeAllOnCompletionDefinition
parameter_list|(
name|ProcessorDefinition
name|definition
parameter_list|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
init|=
name|definition
operator|.
name|getOutputs
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ProcessorDefinition
name|out
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|out
operator|instanceof
name|OnCompletionDefinition
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|end ()
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
extends|extends
name|ProcessorDefinition
argument_list|>
name|end
parameter_list|()
block|{
comment|// pop parent block, as we added outself as block to parent when synchronized was defined in the route
name|getParent
argument_list|()
operator|.
name|popBlock
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|end
argument_list|()
return|;
block|}
comment|/**      * Will only synchronize when the {@link org.apache.camel.Exchange} completed succesfully (no errors).      *      * @return the builder      */
DECL|method|onCompleteOnly ()
specifier|public
name|OutputDefinition
name|onCompleteOnly
parameter_list|()
block|{
comment|// must define return type as OutputDefinition and not this type to avoid end user being able
comment|// to invoke onFailureOnly/onCompleteOnly more than once
name|setOnCompleteOnly
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|setOnFailureOnly
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Will only synchronize when the {@link org.apache.camel.Exchange} ended with failure (exception or FAULT message).      *      * @return the builder      */
DECL|method|onFailureOnly ()
specifier|public
name|OutputDefinition
name|onFailureOnly
parameter_list|()
block|{
comment|// must define return type as OutputDefinition and not this type to avoid end user being able
comment|// to invoke onFailureOnly/onCompleteOnly more than once
name|setOnCompleteOnly
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|setOnFailureOnly
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getOnCompleteOnly ()
specifier|public
name|Boolean
name|getOnCompleteOnly
parameter_list|()
block|{
return|return
name|onCompleteOnly
return|;
block|}
DECL|method|setOnCompleteOnly (Boolean onCompleteOnly)
specifier|public
name|void
name|setOnCompleteOnly
parameter_list|(
name|Boolean
name|onCompleteOnly
parameter_list|)
block|{
name|this
operator|.
name|onCompleteOnly
operator|=
name|onCompleteOnly
expr_stmt|;
block|}
DECL|method|getOnFailureOnly ()
specifier|public
name|Boolean
name|getOnFailureOnly
parameter_list|()
block|{
return|return
name|onFailureOnly
return|;
block|}
DECL|method|setOnFailureOnly (Boolean onFailureOnly)
specifier|public
name|void
name|setOnFailureOnly
parameter_list|(
name|Boolean
name|onFailureOnly
parameter_list|)
block|{
name|this
operator|.
name|onFailureOnly
operator|=
name|onFailureOnly
expr_stmt|;
block|}
block|}
end_class

end_unit

