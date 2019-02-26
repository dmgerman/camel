begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|spi
operator|.
name|BeanRepository
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
comment|/**  * The default {@link Registry} which supports using a given first-choice repository to lookup the beans,  * such as Spring, JNDI, OSGi etc. And to use a secondary {@link SimpleRegistry} as the fallback repository  * to lookup and bind beans.  *<p/>  * Notice that beans in the fallback registry are not managed by the first-choice registry, so these beans  * may not support dependency injection and other features that the first-choice registry may offer.  */
end_comment

begin_class
DECL|class|DefaultRegistry
specifier|public
class|class
name|DefaultRegistry
implements|implements
name|Registry
block|{
DECL|field|repositories
specifier|private
name|List
argument_list|<
name|BeanRepository
argument_list|>
name|repositories
decl_stmt|;
DECL|field|simple
specifier|private
specifier|final
name|Registry
name|simple
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
comment|/**      * Creates a default registry that only uses {@link SimpleRegistry} as the repository.      */
DECL|method|DefaultRegistry ()
specifier|public
name|DefaultRegistry
parameter_list|()
block|{
comment|// noop
block|}
comment|/**      * Creates a registry that uses the given {@link BeanRepository} as first choice bean repository to lookup beans.      * Will fallback and use {@link SimpleRegistry} as internal registry if the beans cannot be found in the first      * choice bean repository.      *      * @param repositories the first choice repositories such as Spring, JNDI, OSGi etc.      */
DECL|method|DefaultRegistry (BeanRepository... repositories)
specifier|public
name|DefaultRegistry
parameter_list|(
name|BeanRepository
modifier|...
name|repositories
parameter_list|)
block|{
if|if
condition|(
name|repositories
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|repositories
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repositories
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates a registry that uses the given {@link BeanRepository} as first choice bean repository to lookup beans.      * Will fallback and use {@link SimpleRegistry} as internal registry if the beans cannot be found in the first      * choice bean repository.      *      * @param repositories the first choice repositories such as Spring, JNDI, OSGi etc.      */
DECL|method|DefaultRegistry (Collection<BeanRepository> repositories)
specifier|public
name|DefaultRegistry
parameter_list|(
name|Collection
argument_list|<
name|BeanRepository
argument_list|>
name|repositories
parameter_list|)
block|{
if|if
condition|(
name|repositories
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|repositories
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|repositories
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Gets the bean repositories.      *      * @return the bean repositories, or<tt>null</tt> if none are in use.      */
DECL|method|getRepositories ()
specifier|public
name|List
argument_list|<
name|BeanRepository
argument_list|>
name|getRepositories
parameter_list|()
block|{
if|if
condition|(
name|repositories
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|repositories
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|bind (String id, Object bean)
specifier|public
name|void
name|bind
parameter_list|(
name|String
name|id
parameter_list|,
name|Object
name|bean
parameter_list|)
block|{
name|simple
operator|.
name|bind
argument_list|(
name|id
argument_list|,
name|bean
argument_list|)
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
if|if
condition|(
name|repositories
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BeanRepository
name|r
range|:
name|repositories
control|)
block|{
name|Object
name|answer
init|=
name|r
operator|.
name|lookupByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
block|}
block|}
return|return
name|simple
operator|.
name|lookupByName
argument_list|(
name|name
argument_list|)
return|;
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
if|if
condition|(
name|repositories
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BeanRepository
name|r
range|:
name|repositories
control|)
block|{
name|T
name|answer
init|=
name|r
operator|.
name|lookupByNameAndType
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
block|}
block|}
return|return
name|simple
operator|.
name|lookupByNameAndType
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
return|;
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
if|if
condition|(
name|repositories
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BeanRepository
name|r
range|:
name|repositories
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|answer
init|=
name|r
operator|.
name|findByTypeWithName
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
block|}
block|}
return|return
name|simple
operator|.
name|findByTypeWithName
argument_list|(
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
if|if
condition|(
name|repositories
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BeanRepository
name|r
range|:
name|repositories
control|)
block|{
name|Set
argument_list|<
name|T
argument_list|>
name|answer
init|=
name|r
operator|.
name|findByType
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
block|}
block|}
return|return
name|simple
operator|.
name|findByType
argument_list|(
name|type
argument_list|)
return|;
block|}
block|}
end_class

end_unit

