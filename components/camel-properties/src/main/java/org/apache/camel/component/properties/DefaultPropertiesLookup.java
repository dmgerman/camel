begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.properties
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|properties
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|PropertiesSource
import|;
end_import

begin_comment
comment|/**  * Default {@link PropertiesLookup} which lookup properties from a {@link java.util.Properties} with all existing properties.  */
end_comment

begin_class
DECL|class|DefaultPropertiesLookup
specifier|public
class|class
name|DefaultPropertiesLookup
implements|implements
name|PropertiesLookup
block|{
DECL|field|component
specifier|private
specifier|final
name|PropertiesComponent
name|component
decl_stmt|;
DECL|method|DefaultPropertiesLookup (PropertiesComponent component)
specifier|public
name|DefaultPropertiesLookup
parameter_list|(
name|PropertiesComponent
name|component
parameter_list|)
block|{
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|lookup (String name)
specifier|public
name|String
name|lookup
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|answer
init|=
literal|null
decl_stmt|;
comment|// override takes precedence
if|if
condition|(
name|component
operator|.
name|getOverrideProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|component
operator|.
name|getOverrideProperties
argument_list|()
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// try till first found source
name|Iterator
argument_list|<
name|PropertiesSource
argument_list|>
name|it2
init|=
name|component
operator|.
name|getSources
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|answer
operator|==
literal|null
operator|&&
name|it2
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|answer
operator|=
name|it2
operator|.
name|next
argument_list|()
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
comment|// initial properties are last
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|component
operator|.
name|getInitialProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|component
operator|.
name|getInitialProperties
argument_list|()
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

