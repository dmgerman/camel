begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|NoSuchBeanException
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
name|Registry
import|;
end_import

begin_comment
comment|/**  * This registry will look up the object with the sequence of the registry list until it finds the Object.  */
end_comment

begin_class
DECL|class|CompositeRegistry
specifier|public
class|class
name|CompositeRegistry
implements|implements
name|Registry
block|{
DECL|field|registryList
specifier|private
name|List
argument_list|<
name|Registry
argument_list|>
name|registryList
decl_stmt|;
DECL|method|CompositeRegistry ()
specifier|public
name|CompositeRegistry
parameter_list|()
block|{
name|registryList
operator|=
operator|new
name|ArrayList
argument_list|<
name|Registry
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|CompositeRegistry (List<Registry> registries)
specifier|public
name|CompositeRegistry
parameter_list|(
name|List
argument_list|<
name|Registry
argument_list|>
name|registries
parameter_list|)
block|{
name|registryList
operator|=
name|registries
expr_stmt|;
block|}
DECL|method|addRegistry (Registry registry)
specifier|public
name|void
name|addRegistry
parameter_list|(
name|Registry
name|registry
parameter_list|)
block|{
name|registryList
operator|.
name|add
argument_list|(
name|registry
argument_list|)
expr_stmt|;
block|}
DECL|method|lookup (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|lookup
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|T
name|answer
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Registry
name|registry
range|:
name|registryList
control|)
block|{
try|try
block|{
name|answer
operator|=
name|registry
operator|.
name|lookup
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// do not double wrap the exception
if|if
condition|(
name|e
operator|instanceof
name|NoSuchBeanException
condition|)
block|{
throw|throw
operator|(
name|NoSuchBeanException
operator|)
name|e
throw|;
block|}
throw|throw
operator|new
name|NoSuchBeanException
argument_list|(
name|name
argument_list|,
literal|"Cannot lookup: "
operator|+
name|name
operator|+
literal|" from registry: "
operator|+
name|registry
operator|+
literal|" with expected type: "
operator|+
name|type
operator|+
literal|" due: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|lookup (String name)
specifier|public
name|Object
name|lookup
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Object
name|answer
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Registry
name|registry
range|:
name|registryList
control|)
block|{
name|answer
operator|=
name|registry
operator|.
name|lookup
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|lookupByType (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|lookupByType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|answer
init|=
name|Collections
operator|.
expr|<
name|String
decl_stmt|,
name|T
decl|>
name|emptyMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Registry
name|registry
range|:
name|registryList
control|)
block|{
name|answer
operator|=
name|registry
operator|.
name|lookupByType
argument_list|(
name|type
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|answer
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
break|break;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

