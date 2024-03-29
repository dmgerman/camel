begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|blueprint
package|;
end_package

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
name|LinkedHashMap
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
name|BeanRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|NoSuchComponentException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|reflect
operator|.
name|BeanMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|reflect
operator|.
name|ComponentMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|reflect
operator|.
name|ReferenceMetadata
import|;
end_import

begin_class
DECL|class|BlueprintContainerBeanRepository
specifier|public
class|class
name|BlueprintContainerBeanRepository
implements|implements
name|BeanRepository
block|{
DECL|field|blueprintContainer
specifier|private
specifier|final
name|BlueprintContainer
name|blueprintContainer
decl_stmt|;
DECL|method|BlueprintContainerBeanRepository (BlueprintContainer blueprintContainer)
specifier|public
name|BlueprintContainerBeanRepository
parameter_list|(
name|BlueprintContainer
name|blueprintContainer
parameter_list|)
block|{
name|this
operator|.
name|blueprintContainer
operator|=
name|blueprintContainer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|lookupByName (String name)
specifier|public
name|Object
name|lookupByName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
return|return
name|blueprintContainer
operator|.
name|getComponentInstance
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchComponentException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|lookupByNameAndType (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|lookupByNameAndType
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
name|Object
name|answer
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|blueprintContainer
operator|.
name|getComponentInstance
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchComponentException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
comment|// just to be safe
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|answer
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Found bean: "
operator|+
name|name
operator|+
literal|" in BlueprintContainer: "
operator|+
name|blueprintContainer
operator|+
literal|" of type: "
operator|+
name|answer
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" expected type was: "
operator|+
name|type
decl_stmt|;
throw|throw
operator|new
name|NoSuchBeanException
argument_list|(
name|name
argument_list|,
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|findByTypeWithName (Class<T> type)
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
name|findByTypeWithName
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|lookupByType
argument_list|(
name|blueprintContainer
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|findByType (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|findByType
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
name|map
init|=
name|lookupByType
argument_list|(
name|blueprintContainer
argument_list|,
name|type
argument_list|)
decl_stmt|;
return|return
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|map
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
DECL|method|lookupByType (BlueprintContainer blueprintContainer, Class<T> type)
specifier|public
specifier|static
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
name|BlueprintContainer
name|blueprintContainer
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|lookupByType
argument_list|(
name|blueprintContainer
argument_list|,
name|type
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|lookupByType (BlueprintContainer blueprintContainer, Class<T> type, boolean includeNonSingletons)
specifier|public
specifier|static
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
name|BlueprintContainer
name|blueprintContainer
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|boolean
name|includeNonSingletons
parameter_list|)
block|{
name|Bundle
name|bundle
init|=
operator|(
name|Bundle
operator|)
name|blueprintContainer
operator|.
name|getComponentInstance
argument_list|(
literal|"blueprintBundle"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|objects
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|ids
init|=
name|blueprintContainer
operator|.
name|getComponentIds
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|id
range|:
name|ids
control|)
block|{
try|try
block|{
name|ComponentMetadata
name|metadata
init|=
name|blueprintContainer
operator|.
name|getComponentMetadata
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|cl
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|metadata
operator|instanceof
name|BeanMetadata
condition|)
block|{
name|BeanMetadata
name|beanMetadata
init|=
operator|(
name|BeanMetadata
operator|)
name|metadata
decl_stmt|;
comment|// should we skip the bean if its prototype and we are only looking for singletons?
if|if
condition|(
operator|!
name|includeNonSingletons
condition|)
block|{
name|String
name|scope
init|=
name|beanMetadata
operator|.
name|getScope
argument_list|()
decl_stmt|;
if|if
condition|(
name|BeanMetadata
operator|.
name|SCOPE_PROTOTYPE
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
condition|)
block|{
continue|continue;
block|}
block|}
name|String
name|clazz
init|=
name|beanMetadata
operator|.
name|getClassName
argument_list|()
decl_stmt|;
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
name|cl
operator|=
name|bundle
operator|.
name|loadClass
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|metadata
operator|instanceof
name|ReferenceMetadata
condition|)
block|{
name|ReferenceMetadata
name|referenceMetadata
init|=
operator|(
name|ReferenceMetadata
operator|)
name|metadata
decl_stmt|;
name|cl
operator|=
name|bundle
operator|.
name|loadClass
argument_list|(
name|referenceMetadata
operator|.
name|getInterface
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cl
operator|!=
literal|null
operator|&&
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|cl
argument_list|)
condition|)
block|{
name|Object
name|o
init|=
name|blueprintContainer
operator|.
name|getComponentInstance
argument_list|(
name|metadata
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|objects
operator|.
name|put
argument_list|(
name|metadata
operator|.
name|getId
argument_list|()
argument_list|,
name|type
operator|.
name|cast
argument_list|(
name|o
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
name|objects
return|;
block|}
block|}
end_class

end_unit

