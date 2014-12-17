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
name|processor
operator|.
name|RemoveHeadersProcessor
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
comment|/**  * Represents an XML&lt;removeHeaders/&gt; element  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"removeHeaders"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|RemoveHeadersDefinition
specifier|public
class|class
name|RemoveHeadersDefinition
extends|extends
name|NoOutputDefinition
argument_list|<
name|RemoveHeadersDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|pattern
specifier|private
name|String
name|pattern
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|excludePattern
specifier|private
name|String
name|excludePattern
decl_stmt|;
comment|// in XML we cannot use String[] for attributes, so we provide a single attribute instead
annotation|@
name|XmlTransient
DECL|field|excludePatterns
specifier|private
name|String
index|[]
name|excludePatterns
decl_stmt|;
DECL|method|RemoveHeadersDefinition ()
specifier|public
name|RemoveHeadersDefinition
parameter_list|()
block|{     }
DECL|method|RemoveHeadersDefinition (String pattern)
specifier|public
name|RemoveHeadersDefinition
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|setPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
DECL|method|RemoveHeadersDefinition (String pattern, String... excludePatterns)
specifier|public
name|RemoveHeadersDefinition
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
modifier|...
name|excludePatterns
parameter_list|)
block|{
name|setPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
name|setExcludePatterns
argument_list|(
name|excludePatterns
argument_list|)
expr_stmt|;
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
literal|"RemoveHeaders["
operator|+
name|getPattern
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
literal|"removeHeaders"
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
literal|"removeHeaders["
operator|+
name|getPattern
argument_list|()
operator|+
literal|"]"
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
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
name|getExcludePatterns
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|RemoveHeadersProcessor
argument_list|(
name|getPattern
argument_list|()
argument_list|,
name|getExcludePatterns
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|getExcludePattern
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|RemoveHeadersProcessor
argument_list|(
name|getPattern
argument_list|()
argument_list|,
operator|new
name|String
index|[]
block|{
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
name|RemoveHeadersProcessor
argument_list|(
name|getPattern
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
DECL|method|setPattern (String pattern)
specifier|public
name|void
name|setPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
DECL|method|getPattern ()
specifier|public
name|String
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
DECL|method|getExcludePatterns ()
specifier|public
name|String
index|[]
name|getExcludePatterns
parameter_list|()
block|{
return|return
name|excludePatterns
return|;
block|}
DECL|method|setExcludePatterns (String[] excludePatterns)
specifier|public
name|void
name|setExcludePatterns
parameter_list|(
name|String
index|[]
name|excludePatterns
parameter_list|)
block|{
name|this
operator|.
name|excludePatterns
operator|=
name|excludePatterns
expr_stmt|;
block|}
DECL|method|getExcludePattern ()
specifier|public
name|String
name|getExcludePattern
parameter_list|()
block|{
return|return
name|excludePattern
return|;
block|}
DECL|method|setExcludePattern (String excludePattern)
specifier|public
name|void
name|setExcludePattern
parameter_list|(
name|String
name|excludePattern
parameter_list|)
block|{
name|this
operator|.
name|excludePattern
operator|=
name|excludePattern
expr_stmt|;
block|}
block|}
end_class

end_unit

