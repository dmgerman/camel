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
name|Collection
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
name|AOPProcessor
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
comment|/**  * Represents an XML&lt;aop/&gt; element  *  * @deprecated will be removed in the future. You can for example use {@link Processor} and  * {@link org.apache.camel.spi.InterceptStrategy} to do AOP in Camel.  * @version   */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"aop"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|Deprecated
DECL|class|AOPDefinition
specifier|public
class|class
name|AOPDefinition
extends|extends
name|OutputDefinition
argument_list|<
name|AOPDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
DECL|field|beforeUri
specifier|private
name|String
name|beforeUri
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|afterUri
specifier|private
name|String
name|afterUri
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|afterFinallyUri
specifier|private
name|String
name|afterFinallyUri
decl_stmt|;
DECL|method|AOPDefinition ()
specifier|public
name|AOPDefinition
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
literal|"AOP["
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|getBeforeUri ()
specifier|public
name|String
name|getBeforeUri
parameter_list|()
block|{
return|return
name|beforeUri
return|;
block|}
DECL|method|setBeforeUri (String beforeUri)
specifier|public
name|void
name|setBeforeUri
parameter_list|(
name|String
name|beforeUri
parameter_list|)
block|{
name|this
operator|.
name|beforeUri
operator|=
name|beforeUri
expr_stmt|;
block|}
DECL|method|getAfterUri ()
specifier|public
name|String
name|getAfterUri
parameter_list|()
block|{
return|return
name|afterUri
return|;
block|}
DECL|method|setAfterUri (String afterUri)
specifier|public
name|void
name|setAfterUri
parameter_list|(
name|String
name|afterUri
parameter_list|)
block|{
name|this
operator|.
name|afterUri
operator|=
name|afterUri
expr_stmt|;
block|}
DECL|method|getAfterFinallyUri ()
specifier|public
name|String
name|getAfterFinallyUri
parameter_list|()
block|{
return|return
name|afterFinallyUri
return|;
block|}
DECL|method|setAfterFinallyUri (String afterFinallyUri)
specifier|public
name|void
name|setAfterFinallyUri
parameter_list|(
name|String
name|afterFinallyUri
parameter_list|)
block|{
name|this
operator|.
name|afterFinallyUri
operator|=
name|afterFinallyUri
expr_stmt|;
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
literal|"aop"
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
literal|"aop"
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (final RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
specifier|final
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
comment|// either before or after must be provided
if|if
condition|(
name|beforeUri
operator|==
literal|null
operator|&&
name|afterUri
operator|==
literal|null
operator|&&
name|afterFinallyUri
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"At least one of before, after or afterFinally must be provided on: "
operator|+
name|this
argument_list|)
throw|;
block|}
comment|// use a pipeline to assemble the before and target processor
comment|// and the after if not afterFinally
name|Collection
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|pipe
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Processor
name|finallyProcessor
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|beforeUri
operator|!=
literal|null
condition|)
block|{
name|pipe
operator|.
name|add
argument_list|(
operator|new
name|ToDefinition
argument_list|(
name|beforeUri
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|pipe
operator|.
name|addAll
argument_list|(
name|getOutputs
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|afterUri
operator|!=
literal|null
condition|)
block|{
name|pipe
operator|.
name|add
argument_list|(
operator|new
name|ToDefinition
argument_list|(
name|afterUri
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|afterFinallyUri
operator|!=
literal|null
condition|)
block|{
name|finallyProcessor
operator|=
name|createProcessor
argument_list|(
name|routeContext
argument_list|,
operator|new
name|ToDefinition
argument_list|(
name|afterFinallyUri
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Processor
name|tryProcessor
init|=
name|createOutputsProcessor
argument_list|(
name|routeContext
argument_list|,
name|pipe
argument_list|)
decl_stmt|;
comment|// the AOP processor is based on TryProcessor so we do not have any catches
return|return
operator|new
name|AOPProcessor
argument_list|(
name|tryProcessor
argument_list|,
literal|null
argument_list|,
name|finallyProcessor
argument_list|)
return|;
block|}
comment|/**      * Uses a AOP around.      *      * @param beforeUri the uri of the before endpoint      * @param afterUri  the uri of the after endpoint      * @return the builder      */
DECL|method|around (String beforeUri, String afterUri)
specifier|public
name|AOPDefinition
name|around
parameter_list|(
name|String
name|beforeUri
parameter_list|,
name|String
name|afterUri
parameter_list|)
block|{
name|this
operator|.
name|beforeUri
operator|=
name|beforeUri
expr_stmt|;
name|this
operator|.
name|afterUri
operator|=
name|afterUri
expr_stmt|;
name|this
operator|.
name|afterFinallyUri
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses a AOP around with after being invoked in a finally block      *      * @param beforeUri the uri of the before endpoint      * @param afterUri  the uri of the after endpoint      * @return the builder      */
DECL|method|aroundFinally (String beforeUri, String afterUri)
specifier|public
name|AOPDefinition
name|aroundFinally
parameter_list|(
name|String
name|beforeUri
parameter_list|,
name|String
name|afterUri
parameter_list|)
block|{
name|this
operator|.
name|beforeUri
operator|=
name|beforeUri
expr_stmt|;
name|this
operator|.
name|afterUri
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|afterFinallyUri
operator|=
name|afterUri
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses a AOP before.      *      * @param beforeUri the uri of the before endpoint      * @return the builder      */
DECL|method|before (String beforeUri)
specifier|public
name|AOPDefinition
name|before
parameter_list|(
name|String
name|beforeUri
parameter_list|)
block|{
name|this
operator|.
name|beforeUri
operator|=
name|beforeUri
expr_stmt|;
name|this
operator|.
name|afterUri
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|afterFinallyUri
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses a AOP after.      *      * @param afterUri  the uri of the after endpoint      * @return the builder      */
DECL|method|after (String afterUri)
specifier|public
name|AOPDefinition
name|after
parameter_list|(
name|String
name|afterUri
parameter_list|)
block|{
name|this
operator|.
name|beforeUri
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|afterUri
operator|=
name|afterUri
expr_stmt|;
name|this
operator|.
name|afterFinallyUri
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses a AOP after with after being invoked in a finally block.      *      * @param afterUri  the uri of the after endpoint      * @return the builder      */
DECL|method|afterFinally (String afterUri)
specifier|public
name|AOPDefinition
name|afterFinally
parameter_list|(
name|String
name|afterUri
parameter_list|)
block|{
name|this
operator|.
name|beforeUri
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|afterUri
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|afterFinallyUri
operator|=
name|afterUri
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

