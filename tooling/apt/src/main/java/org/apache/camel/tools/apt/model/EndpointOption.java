begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tools.apt.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
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
name|Set
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
name|tools
operator|.
name|apt
operator|.
name|helper
operator|.
name|CollectionStringBuffer
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
operator|.
name|helper
operator|.
name|Strings
operator|.
name|isNullOrEmpty
import|;
end_import

begin_class
DECL|class|EndpointOption
specifier|public
specifier|final
class|class
name|EndpointOption
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
DECL|field|required
specifier|private
name|String
name|required
decl_stmt|;
DECL|field|defaultValue
specifier|private
name|String
name|defaultValue
decl_stmt|;
DECL|field|defaultValueNote
specifier|private
name|String
name|defaultValueNote
decl_stmt|;
DECL|field|documentation
specifier|private
name|String
name|documentation
decl_stmt|;
DECL|field|optionalPrefix
specifier|private
name|String
name|optionalPrefix
decl_stmt|;
DECL|field|prefix
specifier|private
name|String
name|prefix
decl_stmt|;
DECL|field|multiValue
specifier|private
name|boolean
name|multiValue
decl_stmt|;
DECL|field|deprecated
specifier|private
name|boolean
name|deprecated
decl_stmt|;
DECL|field|secret
specifier|private
name|boolean
name|secret
decl_stmt|;
DECL|field|group
specifier|private
name|String
name|group
decl_stmt|;
DECL|field|label
specifier|private
name|String
name|label
decl_stmt|;
DECL|field|enumType
specifier|private
name|boolean
name|enumType
decl_stmt|;
DECL|field|enums
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|enums
decl_stmt|;
DECL|method|EndpointOption (String name, String type, String required, String defaultValue, String defaultValueNote, String documentation, String optionalPrefix, String prefix, boolean multiValue, boolean deprecated, boolean secret, String group, String label, boolean enumType, Set<String> enums)
specifier|public
name|EndpointOption
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|required
parameter_list|,
name|String
name|defaultValue
parameter_list|,
name|String
name|defaultValueNote
parameter_list|,
name|String
name|documentation
parameter_list|,
name|String
name|optionalPrefix
parameter_list|,
name|String
name|prefix
parameter_list|,
name|boolean
name|multiValue
parameter_list|,
name|boolean
name|deprecated
parameter_list|,
name|boolean
name|secret
parameter_list|,
name|String
name|group
parameter_list|,
name|String
name|label
parameter_list|,
name|boolean
name|enumType
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|enums
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|required
operator|=
name|required
expr_stmt|;
name|this
operator|.
name|defaultValue
operator|=
name|defaultValue
expr_stmt|;
name|this
operator|.
name|defaultValueNote
operator|=
name|defaultValueNote
expr_stmt|;
name|this
operator|.
name|documentation
operator|=
name|documentation
expr_stmt|;
name|this
operator|.
name|optionalPrefix
operator|=
name|optionalPrefix
expr_stmt|;
name|this
operator|.
name|prefix
operator|=
name|prefix
expr_stmt|;
name|this
operator|.
name|multiValue
operator|=
name|multiValue
expr_stmt|;
name|this
operator|.
name|deprecated
operator|=
name|deprecated
expr_stmt|;
name|this
operator|.
name|secret
operator|=
name|secret
expr_stmt|;
name|this
operator|.
name|group
operator|=
name|group
expr_stmt|;
name|this
operator|.
name|label
operator|=
name|label
expr_stmt|;
name|this
operator|.
name|enumType
operator|=
name|enumType
expr_stmt|;
name|this
operator|.
name|enums
operator|=
name|enums
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
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
DECL|method|getRequired ()
specifier|public
name|String
name|getRequired
parameter_list|()
block|{
return|return
name|required
return|;
block|}
DECL|method|getDefaultValue ()
specifier|public
name|String
name|getDefaultValue
parameter_list|()
block|{
return|return
name|defaultValue
return|;
block|}
DECL|method|getDocumentation ()
specifier|public
name|String
name|getDocumentation
parameter_list|()
block|{
return|return
name|documentation
return|;
block|}
DECL|method|getOptionalPrefix ()
specifier|public
name|String
name|getOptionalPrefix
parameter_list|()
block|{
return|return
name|optionalPrefix
return|;
block|}
DECL|method|getPrefix ()
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|prefix
return|;
block|}
DECL|method|isMultiValue ()
specifier|public
name|boolean
name|isMultiValue
parameter_list|()
block|{
return|return
name|multiValue
return|;
block|}
DECL|method|isDeprecated ()
specifier|public
name|boolean
name|isDeprecated
parameter_list|()
block|{
return|return
name|deprecated
return|;
block|}
DECL|method|isSecret ()
specifier|public
name|boolean
name|isSecret
parameter_list|()
block|{
return|return
name|secret
return|;
block|}
DECL|method|getEnumValuesAsHtml ()
specifier|public
name|String
name|getEnumValuesAsHtml
parameter_list|()
block|{
name|CollectionStringBuffer
name|csb
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
literal|"<br/>"
argument_list|)
decl_stmt|;
if|if
condition|(
name|enums
operator|!=
literal|null
operator|&&
name|enums
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|String
name|e
range|:
name|enums
control|)
block|{
name|csb
operator|.
name|append
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|csb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getDocumentationWithNotes ()
specifier|public
name|String
name|getDocumentationWithNotes
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|documentation
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isNullOrEmpty
argument_list|(
name|defaultValueNote
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|". Default value notice: "
argument_list|)
operator|.
name|append
argument_list|(
name|defaultValueNote
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|isEnumType ()
specifier|public
name|boolean
name|isEnumType
parameter_list|()
block|{
return|return
name|enumType
return|;
block|}
DECL|method|getEnums ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getEnums
parameter_list|()
block|{
return|return
name|enums
return|;
block|}
DECL|method|getGroup ()
specifier|public
name|String
name|getGroup
parameter_list|()
block|{
return|return
name|group
return|;
block|}
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
name|label
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|EndpointOption
name|that
init|=
operator|(
name|EndpointOption
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|name
operator|.
name|equals
argument_list|(
name|that
operator|.
name|name
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|name
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit

