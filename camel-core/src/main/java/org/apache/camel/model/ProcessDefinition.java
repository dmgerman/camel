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
name|Service
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
name|WrapProcessor
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
name|Required
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

begin_comment
comment|/**  * Represents an XML&lt;process/&gt; element  *  * @version   */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"process"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ProcessDefinition
specifier|public
class|class
name|ProcessDefinition
extends|extends
name|OutputDefinition
argument_list|<
name|ProcessDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|ref
specifier|private
name|String
name|ref
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
DECL|method|ProcessDefinition ()
specifier|public
name|ProcessDefinition
parameter_list|()
block|{     }
DECL|method|ProcessDefinition (Processor processor)
specifier|public
name|ProcessDefinition
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|processor
operator|=
name|processor
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
literal|"process"
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
literal|"process["
operator|+
operator|(
operator|(
name|ref
operator|!=
literal|null
operator|)
condition|?
literal|"ref:"
operator|+
name|ref
else|:
name|processor
operator|)
operator|+
literal|"]"
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
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
return|return
literal|"ref:"
operator|+
name|ref
return|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
return|return
name|processor
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
DECL|method|getRef ()
specifier|public
name|String
name|getRef
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
annotation|@
name|Required
DECL|method|setRef (String ref)
specifier|public
name|void
name|setRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
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
block|{
if|if
condition|(
name|processor
operator|==
literal|null
condition|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|ref
argument_list|,
literal|"ref"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|processor
operator|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|getRef
argument_list|()
argument_list|,
name|Processor
operator|.
name|class
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|processor
argument_list|,
literal|"registry entry called "
operator|+
name|getRef
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
comment|// ensure its wrapped in a Service so we can manage it from eg. JMX
comment|// (a Processor must be a Service to be enlisted in JMX)
if|if
condition|(
operator|!
operator|(
name|processor
operator|instanceof
name|Service
operator|)
condition|)
block|{
name|processor
operator|=
operator|new
name|WrapProcessor
argument_list|(
name|processor
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
return|return
name|processor
return|;
block|}
block|}
end_class

end_unit

