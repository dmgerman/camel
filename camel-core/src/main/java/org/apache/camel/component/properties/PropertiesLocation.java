begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_class
DECL|class|PropertiesLocation
specifier|public
class|class
name|PropertiesLocation
block|{
DECL|field|resolver
specifier|private
specifier|final
name|String
name|resolver
decl_stmt|;
DECL|field|path
specifier|private
specifier|final
name|String
name|path
decl_stmt|;
DECL|field|optional
specifier|private
specifier|final
name|boolean
name|optional
decl_stmt|;
DECL|method|PropertiesLocation (String location)
specifier|public
name|PropertiesLocation
parameter_list|(
name|String
name|location
parameter_list|)
block|{
comment|// make sure to trim as people may use new lines when configuring using XML
comment|// and do this in the setter as Spring/Blueprint resolves placeholders before
comment|// Camel is being started
name|location
operator|=
name|location
operator|.
name|trim
argument_list|()
expr_stmt|;
name|int
name|idx
init|=
name|location
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|this
operator|.
name|resolver
operator|=
name|location
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|location
operator|=
name|location
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|resolver
operator|=
literal|"classpath"
expr_stmt|;
block|}
name|idx
operator|=
name|location
operator|.
name|lastIndexOf
argument_list|(
literal|';'
argument_list|)
expr_stmt|;
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|this
operator|.
name|optional
operator|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|location
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
argument_list|,
literal|"optional="
argument_list|,
name|Boolean
operator|::
name|valueOf
argument_list|)
operator|.
name|orElse
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|location
operator|=
name|location
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|optional
operator|=
literal|false
expr_stmt|;
block|}
name|this
operator|.
name|path
operator|=
name|location
expr_stmt|;
block|}
DECL|method|PropertiesLocation (String resolver, String path)
specifier|public
name|PropertiesLocation
parameter_list|(
name|String
name|resolver
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|this
argument_list|(
name|resolver
argument_list|,
name|path
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|PropertiesLocation (String resolver, String path, Boolean optional)
specifier|public
name|PropertiesLocation
parameter_list|(
name|String
name|resolver
parameter_list|,
name|String
name|path
parameter_list|,
name|Boolean
name|optional
parameter_list|)
block|{
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|optional
operator|=
name|optional
expr_stmt|;
block|}
comment|// *****************************
comment|// Getters
comment|// *****************************
DECL|method|getResolver ()
specifier|public
name|String
name|getResolver
parameter_list|()
block|{
return|return
name|resolver
return|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
DECL|method|isOptional ()
specifier|public
name|boolean
name|isOptional
parameter_list|()
block|{
return|return
name|optional
return|;
block|}
comment|// *****************************
comment|// Equals/HashCode/ToString
comment|// *****************************
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
name|PropertiesLocation
name|location
init|=
operator|(
name|PropertiesLocation
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|optional
operator|!=
name|location
operator|.
name|optional
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|resolver
operator|!=
literal|null
condition|?
operator|!
name|resolver
operator|.
name|equals
argument_list|(
name|location
operator|.
name|resolver
argument_list|)
else|:
name|location
operator|.
name|resolver
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|path
operator|!=
literal|null
condition|?
name|path
operator|.
name|equals
argument_list|(
name|location
operator|.
name|path
argument_list|)
else|:
name|location
operator|.
name|path
operator|==
literal|null
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
name|int
name|result
init|=
name|resolver
operator|!=
literal|null
condition|?
name|resolver
operator|.
name|hashCode
argument_list|()
else|:
literal|0
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|path
operator|!=
literal|null
condition|?
name|path
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|optional
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
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
literal|"PropertiesLocation{"
operator|+
literal|"resolver='"
operator|+
name|resolver
operator|+
literal|'\''
operator|+
literal|", path='"
operator|+
name|path
operator|+
literal|'\''
operator|+
literal|", optional="
operator|+
name|optional
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

