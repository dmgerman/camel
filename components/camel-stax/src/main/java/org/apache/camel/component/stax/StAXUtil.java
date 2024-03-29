begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stax
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stax
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|XmlType
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
name|support
operator|.
name|LRUCacheFactory
import|;
end_import

begin_class
DECL|class|StAXUtil
specifier|public
specifier|final
class|class
name|StAXUtil
block|{
DECL|field|TAG_NAMES
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|String
argument_list|>
name|TAG_NAMES
init|=
name|LRUCacheFactory
operator|.
name|newLRUSoftCache
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
DECL|method|StAXUtil ()
specifier|private
name|StAXUtil
parameter_list|()
block|{
comment|// no-op
block|}
DECL|method|getTagName (Class<?> handled)
specifier|public
specifier|static
name|String
name|getTagName
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|handled
parameter_list|)
block|{
if|if
condition|(
name|TAG_NAMES
operator|.
name|containsKey
argument_list|(
name|handled
argument_list|)
condition|)
block|{
return|return
name|TAG_NAMES
operator|.
name|get
argument_list|(
name|handled
argument_list|)
return|;
block|}
name|XmlType
name|xmlType
init|=
name|handled
operator|.
name|getAnnotation
argument_list|(
name|XmlType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|xmlType
operator|!=
literal|null
operator|&&
name|xmlType
operator|.
name|name
argument_list|()
operator|!=
literal|null
operator|&&
name|xmlType
operator|.
name|name
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|TAG_NAMES
operator|.
name|put
argument_list|(
name|handled
argument_list|,
name|xmlType
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|xmlType
operator|.
name|name
argument_list|()
return|;
block|}
else|else
block|{
name|XmlRootElement
name|xmlRoot
init|=
name|handled
operator|.
name|getAnnotation
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|xmlRoot
operator|!=
literal|null
operator|&&
name|xmlRoot
operator|.
name|name
argument_list|()
operator|!=
literal|null
operator|&&
name|xmlRoot
operator|.
name|name
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|TAG_NAMES
operator|.
name|put
argument_list|(
name|handled
argument_list|,
name|xmlRoot
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|xmlRoot
operator|.
name|name
argument_list|()
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"XML name not found for "
operator|+
name|handled
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

