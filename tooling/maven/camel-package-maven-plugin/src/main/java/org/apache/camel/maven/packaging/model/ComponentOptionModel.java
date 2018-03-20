begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven.packaging.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|packaging
operator|.
name|model
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|packaging
operator|.
name|StringHelper
operator|.
name|wrapCamelCaseWords
import|;
end_import

begin_class
DECL|class|ComponentOptionModel
specifier|public
class|class
name|ComponentOptionModel
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|displayName
specifier|private
name|String
name|displayName
decl_stmt|;
DECL|field|kind
specifier|private
name|String
name|kind
decl_stmt|;
DECL|field|group
specifier|private
name|String
name|group
decl_stmt|;
DECL|field|required
specifier|private
name|String
name|required
decl_stmt|;
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
DECL|field|javaType
specifier|private
name|String
name|javaType
decl_stmt|;
DECL|field|deprecated
specifier|private
name|String
name|deprecated
decl_stmt|;
DECL|field|deprecationNote
specifier|private
name|String
name|deprecationNote
decl_stmt|;
DECL|field|secret
specifier|private
name|String
name|secret
decl_stmt|;
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|field|defaultValue
specifier|private
name|String
name|defaultValue
decl_stmt|;
DECL|field|enums
specifier|private
name|String
name|enums
decl_stmt|;
comment|// special for documentation rendering
DECL|field|newGroup
specifier|private
name|boolean
name|newGroup
decl_stmt|;
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
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getDisplayName ()
specifier|public
name|String
name|getDisplayName
parameter_list|()
block|{
return|return
name|displayName
return|;
block|}
DECL|method|setDisplayName (String displayName)
specifier|public
name|void
name|setDisplayName
parameter_list|(
name|String
name|displayName
parameter_list|)
block|{
name|this
operator|.
name|displayName
operator|=
name|displayName
expr_stmt|;
block|}
DECL|method|getKind ()
specifier|public
name|String
name|getKind
parameter_list|()
block|{
return|return
name|kind
return|;
block|}
DECL|method|setKind (String kind)
specifier|public
name|void
name|setKind
parameter_list|(
name|String
name|kind
parameter_list|)
block|{
name|this
operator|.
name|kind
operator|=
name|kind
expr_stmt|;
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
DECL|method|setGroup (String group)
specifier|public
name|void
name|setGroup
parameter_list|(
name|String
name|group
parameter_list|)
block|{
name|this
operator|.
name|group
operator|=
name|group
expr_stmt|;
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
DECL|method|setRequired (String required)
specifier|public
name|void
name|setRequired
parameter_list|(
name|String
name|required
parameter_list|)
block|{
name|this
operator|.
name|required
operator|=
name|required
expr_stmt|;
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
DECL|method|getJavaType ()
specifier|public
name|String
name|getJavaType
parameter_list|()
block|{
return|return
name|javaType
return|;
block|}
DECL|method|setJavaType (String javaType)
specifier|public
name|void
name|setJavaType
parameter_list|(
name|String
name|javaType
parameter_list|)
block|{
name|this
operator|.
name|javaType
operator|=
name|javaType
expr_stmt|;
block|}
DECL|method|getDeprecated ()
specifier|public
name|String
name|getDeprecated
parameter_list|()
block|{
return|return
name|deprecated
return|;
block|}
DECL|method|setDeprecated (String deprecated)
specifier|public
name|void
name|setDeprecated
parameter_list|(
name|String
name|deprecated
parameter_list|)
block|{
name|this
operator|.
name|deprecated
operator|=
name|deprecated
expr_stmt|;
block|}
DECL|method|getDeprecationNote ()
specifier|public
name|String
name|getDeprecationNote
parameter_list|()
block|{
return|return
name|deprecationNote
return|;
block|}
DECL|method|setDeprecationNote (String deprecationNote)
specifier|public
name|void
name|setDeprecationNote
parameter_list|(
name|String
name|deprecationNote
parameter_list|)
block|{
name|this
operator|.
name|deprecationNote
operator|=
name|deprecationNote
expr_stmt|;
block|}
DECL|method|getSecret ()
specifier|public
name|String
name|getSecret
parameter_list|()
block|{
return|return
name|secret
return|;
block|}
DECL|method|setSecret (String secret)
specifier|public
name|void
name|setSecret
parameter_list|(
name|String
name|secret
parameter_list|)
block|{
name|this
operator|.
name|secret
operator|=
name|secret
expr_stmt|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|setDescription (String description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
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
DECL|method|setDefaultValue (String defaultValue)
specifier|public
name|void
name|setDefaultValue
parameter_list|(
name|String
name|defaultValue
parameter_list|)
block|{
name|this
operator|.
name|defaultValue
operator|=
name|defaultValue
expr_stmt|;
block|}
DECL|method|getEnums ()
specifier|public
name|String
name|getEnums
parameter_list|()
block|{
return|return
name|enums
return|;
block|}
DECL|method|setEnums (String enums)
specifier|public
name|void
name|setEnums
parameter_list|(
name|String
name|enums
parameter_list|)
block|{
name|this
operator|.
name|enums
operator|=
name|enums
expr_stmt|;
block|}
DECL|method|isNewGroup ()
specifier|public
name|boolean
name|isNewGroup
parameter_list|()
block|{
return|return
name|newGroup
return|;
block|}
DECL|method|setNewGroup (boolean newGroup)
specifier|public
name|void
name|setNewGroup
parameter_list|(
name|boolean
name|newGroup
parameter_list|)
block|{
name|this
operator|.
name|newGroup
operator|=
name|newGroup
expr_stmt|;
block|}
DECL|method|getShortJavaType ()
specifier|public
name|String
name|getShortJavaType
parameter_list|()
block|{
comment|// TODO: use watermark in the others
return|return
name|getShortJavaType
argument_list|(
literal|40
argument_list|)
return|;
block|}
DECL|method|getShortJavaType (int watermark)
specifier|public
name|String
name|getShortJavaType
parameter_list|(
name|int
name|watermark
parameter_list|)
block|{
if|if
condition|(
name|javaType
operator|.
name|startsWith
argument_list|(
literal|"java.util.Map"
argument_list|)
condition|)
block|{
return|return
literal|"Map"
return|;
block|}
elseif|else
if|if
condition|(
name|javaType
operator|.
name|startsWith
argument_list|(
literal|"java.util.Set"
argument_list|)
condition|)
block|{
return|return
literal|"Set"
return|;
block|}
elseif|else
if|if
condition|(
name|javaType
operator|.
name|startsWith
argument_list|(
literal|"java.util.List"
argument_list|)
condition|)
block|{
return|return
literal|"List"
return|;
block|}
name|String
name|text
init|=
name|javaType
decl_stmt|;
name|int
name|pos
init|=
name|text
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|!=
operator|-
literal|1
condition|)
block|{
name|text
operator|=
name|text
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// if its some kind of java object then lets wrap it as its long
if|if
condition|(
literal|"object"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|text
operator|=
name|wrapCamelCaseWords
argument_list|(
name|text
argument_list|,
name|watermark
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
block|}
return|return
name|text
return|;
block|}
DECL|method|getShortGroup ()
specifier|public
name|String
name|getShortGroup
parameter_list|()
block|{
if|if
condition|(
name|group
operator|.
name|endsWith
argument_list|(
literal|" (advanced)"
argument_list|)
condition|)
block|{
return|return
name|group
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|group
operator|.
name|length
argument_list|()
operator|-
literal|11
argument_list|)
return|;
block|}
return|return
name|group
return|;
block|}
DECL|method|getShortDefaultValue (int watermark)
specifier|public
name|String
name|getShortDefaultValue
parameter_list|(
name|int
name|watermark
parameter_list|)
block|{
if|if
condition|(
name|defaultValue
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|""
return|;
block|}
name|String
name|text
init|=
name|defaultValue
decl_stmt|;
if|if
condition|(
name|text
operator|.
name|endsWith
argument_list|(
literal|"<T>"
argument_list|)
condition|)
block|{
name|text
operator|=
name|text
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|text
operator|.
name|length
argument_list|()
operator|-
literal|3
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|text
operator|.
name|endsWith
argument_list|(
literal|"<T>>"
argument_list|)
condition|)
block|{
name|text
operator|=
name|text
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|text
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
block|}
comment|// TODO: dirty hack for AUTO_ACKNOWLEDGE which we should wrap
if|if
condition|(
literal|"AUTO_ACKNOWLEDGE"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
return|return
literal|"AUTO_ ACKNOWLEDGE"
return|;
block|}
return|return
name|text
return|;
block|}
DECL|method|getShortName (int watermark)
specifier|public
name|String
name|getShortName
parameter_list|(
name|int
name|watermark
parameter_list|)
block|{
name|String
name|text
init|=
name|wrapCamelCaseWords
argument_list|(
name|name
argument_list|,
name|watermark
argument_list|,
literal|" "
argument_list|)
decl_stmt|;
comment|// ensure the option name starts with lower-case
return|return
name|Character
operator|.
name|toLowerCase
argument_list|(
name|text
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|+
name|text
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
end_class

end_unit

