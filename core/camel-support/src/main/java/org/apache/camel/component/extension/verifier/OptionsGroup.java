begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.extension.verifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|extension
operator|.
name|verifier
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * A named group of options. A group of options requires that a set of  * component parameters is given as a whole.  *  *<a id="#syntax">The option syntax can be  * {@code "propertyName"} to denote required property and  * {@code "!propertyName"} to denote required absence of a property.  */
end_comment

begin_class
DECL|class|OptionsGroup
specifier|public
specifier|final
class|class
name|OptionsGroup
implements|implements
name|Serializable
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|options
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|options
decl_stmt|;
comment|/**      * Creates new named {@link OptionsGroup}.      *      * @param name the name of the group      */
DECL|method|OptionsGroup (String name)
specifier|public
name|OptionsGroup
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
name|this
operator|.
name|options
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates new named {@link OptionsGroup} with a set of option      * definitions.      *      * @param name the name of the group      * @param options names of properties in the syntax mentioned in {@link OptionsGroup}      */
DECL|method|OptionsGroup (String name, Collection<String> options)
specifier|public
name|OptionsGroup
parameter_list|(
name|String
name|name
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|options
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
name|options
operator|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a option definition to this group. The option syntax can be      * {@code "propertyName"} to denote required property and      * {@code "!propertyName"} to denote required absence of a property.      *      * @param option definition.      */
DECL|method|addOption (String option)
specifier|public
name|void
name|addOption
parameter_list|(
name|String
name|option
parameter_list|)
block|{
name|this
operator|.
name|options
operator|.
name|add
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
comment|/**      * The name of the group.      */
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
comment|/**      * The option definitions in this group.      */
DECL|method|getOptions ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getOptions
parameter_list|()
block|{
return|return
name|this
operator|.
name|options
return|;
block|}
comment|/**      * Adds a option definition to this group. The option syntax can be      * {@code "propertyName"} to denote required property and      * {@code "!propertyName"} to denote required absence of a property.      *      * @param option definition.      */
DECL|method|option (String option)
specifier|public
name|OptionsGroup
name|option
parameter_list|(
name|String
name|option
parameter_list|)
block|{
name|this
operator|.
name|options
operator|.
name|add
argument_list|(
name|option
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds a number of option definitions to this group. The option      * syntax can be {@code "propertyName"} to denote required      * property and {@code "!propertyName"} to denote required absence      * of a property.      *      * @param options options definition      */
DECL|method|options (String... options)
specifier|public
name|OptionsGroup
name|options
parameter_list|(
name|String
modifier|...
name|options
parameter_list|)
block|{
for|for
control|(
name|String
name|option
range|:
name|options
control|)
block|{
name|addOption
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Creates new group with the specified name.      *      * @param name the name of the group      */
DECL|method|withName (String name)
specifier|public
specifier|static
name|OptionsGroup
name|withName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|OptionsGroup
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Creates new group with the specified name of the given      * {@link Enum} name.      *      * @param enumItem the name of the group      * @see Enum#name()      */
DECL|method|withName (Enum<?> enumItem)
specifier|public
specifier|static
name|OptionsGroup
name|withName
parameter_list|(
name|Enum
argument_list|<
name|?
argument_list|>
name|enumItem
parameter_list|)
block|{
return|return
operator|new
name|OptionsGroup
argument_list|(
name|enumItem
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Creates new group with the specified name and option definitions.      *      * @param name the name of the group      * @param options options definition       */
DECL|method|withNameAndOptions (String name, String... options)
specifier|public
specifier|static
name|OptionsGroup
name|withNameAndOptions
parameter_list|(
name|String
name|name
parameter_list|,
name|String
modifier|...
name|options
parameter_list|)
block|{
return|return
operator|new
name|OptionsGroup
argument_list|(
name|name
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|options
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

