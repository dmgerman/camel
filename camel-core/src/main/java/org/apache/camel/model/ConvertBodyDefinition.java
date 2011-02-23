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
name|RuntimeCamelException
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
name|converter
operator|.
name|IOConverter
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
name|ConvertBodyProcessor
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

begin_comment
comment|/**  * Represents an XML&lt;convertBodyTo/&gt; element  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"convertBodyTo"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ConvertBodyDefinition
specifier|public
class|class
name|ConvertBodyDefinition
extends|extends
name|NoOutputDefinition
argument_list|<
name|ConvertBodyDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|charset
specifier|private
name|String
name|charset
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|typeClass
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
decl_stmt|;
DECL|method|ConvertBodyDefinition ()
specifier|public
name|ConvertBodyDefinition
parameter_list|()
block|{     }
DECL|method|ConvertBodyDefinition (String type)
specifier|public
name|ConvertBodyDefinition
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
DECL|method|ConvertBodyDefinition (Class<?> typeClass)
specifier|public
name|ConvertBodyDefinition
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
parameter_list|)
block|{
name|setTypeClass
argument_list|(
name|typeClass
argument_list|)
expr_stmt|;
name|setType
argument_list|(
name|typeClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|ConvertBodyDefinition (Class<?> typeClass, String charset)
specifier|public
name|ConvertBodyDefinition
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
parameter_list|,
name|String
name|charset
parameter_list|)
block|{
name|setTypeClass
argument_list|(
name|typeClass
argument_list|)
expr_stmt|;
name|setType
argument_list|(
name|typeClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setCharset
argument_list|(
name|charset
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
literal|"convertBodyTo["
operator|+
name|getType
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
literal|"convertBodyTo"
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
if|if
condition|(
name|getTypeClass
argument_list|()
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|typeClass
operator|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|getType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getTypeClass
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot load the class with the class name: "
operator|+
name|getType
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|// validate charset
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
name|IOConverter
operator|.
name|validateCharset
argument_list|(
name|charset
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ConvertBodyProcessor
argument_list|(
name|getTypeClass
argument_list|()
argument_list|,
name|getCharset
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
annotation|@
name|Required
DECL|method|setType (String type)
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getTypeClass ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getTypeClass
parameter_list|()
block|{
return|return
name|typeClass
return|;
block|}
DECL|method|setTypeClass (Class<?> typeClass)
specifier|public
name|void
name|setTypeClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
parameter_list|)
block|{
name|this
operator|.
name|typeClass
operator|=
name|typeClass
expr_stmt|;
block|}
DECL|method|getCharset ()
specifier|public
name|String
name|getCharset
parameter_list|()
block|{
return|return
name|charset
return|;
block|}
DECL|method|setCharset (String charset)
specifier|public
name|void
name|setCharset
parameter_list|(
name|String
name|charset
parameter_list|)
block|{
name|this
operator|.
name|charset
operator|=
name|charset
expr_stmt|;
block|}
block|}
end_class

end_unit

