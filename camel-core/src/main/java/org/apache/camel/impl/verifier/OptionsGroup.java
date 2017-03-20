begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.verifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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

