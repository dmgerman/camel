begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nitrite
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nitrite
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
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|Endpoint
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
name|annotations
operator|.
name|Component
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
name|DefaultComponent
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
name|util
operator|.
name|FileUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dizitart
operator|.
name|no2
operator|.
name|Nitrite
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dizitart
operator|.
name|no2
operator|.
name|NitriteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dizitart
operator|.
name|no2
operator|.
name|PersistentCollection
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link NitriteEndpoint}.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"nitrite"
argument_list|)
DECL|class|NitriteComponent
specifier|public
class|class
name|NitriteComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|databaseCache
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Nitrite
argument_list|>
name|databaseCache
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|collectionCache
specifier|private
name|Map
argument_list|<
name|CollectionCacheKey
argument_list|,
name|PersistentCollection
argument_list|>
name|collectionCache
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|NitriteEndpoint
name|endpoint
init|=
operator|new
name|NitriteEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getCollection
argument_list|()
operator|!=
literal|null
operator|&&
name|endpoint
operator|.
name|getRepositoryClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Options collection and repositoryClass cannot be used together"
argument_list|)
throw|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getCollection
argument_list|()
operator|==
literal|null
operator|&&
name|endpoint
operator|.
name|getRepositoryClass
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Either collection or repositoryClass option is required"
argument_list|)
throw|;
block|}
name|String
name|normalizedPath
init|=
name|FileUtil
operator|.
name|compactPath
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setDatabase
argument_list|(
name|normalizedPath
argument_list|)
expr_stmt|;
name|Nitrite
name|nitriteDatabase
init|=
name|databaseCache
operator|.
name|computeIfAbsent
argument_list|(
name|normalizedPath
argument_list|,
name|path
lambda|->
block|{
name|NitriteBuilder
name|builder
init|=
name|Nitrite
operator|.
name|builder
argument_list|()
operator|.
name|compressed
argument_list|()
operator|.
name|filePath
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getUsername
argument_list|()
operator|==
literal|null
operator|&&
name|endpoint
operator|.
name|getPassword
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|builder
operator|.
name|openOrCreate
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|builder
operator|.
name|openOrCreate
argument_list|(
name|endpoint
operator|.
name|getUsername
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getPassword
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setNitriteDatabase
argument_list|(
name|nitriteDatabase
argument_list|)
expr_stmt|;
name|PersistentCollection
name|nitriteCollection
init|=
name|collectionCache
operator|.
name|computeIfAbsent
argument_list|(
operator|new
name|CollectionCacheKey
argument_list|(
name|endpoint
argument_list|)
argument_list|,
name|key
lambda|->
block|{
if|if
condition|(
name|key
operator|.
name|collection
operator|!=
literal|null
condition|)
block|{
return|return
name|key
operator|.
name|database
operator|.
name|getCollection
argument_list|(
name|key
operator|.
name|collection
argument_list|)
return|;
block|}
else|else
block|{
if|if
condition|(
name|key
operator|.
name|repositoryName
operator|!=
literal|null
condition|)
block|{
return|return
name|key
operator|.
name|database
operator|.
name|getRepository
argument_list|(
name|key
operator|.
name|repositoryName
argument_list|,
name|key
operator|.
name|repositoryClass
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|repositoryClass
operator|!=
literal|null
condition|)
block|{
return|return
name|key
operator|.
name|database
operator|.
name|getRepository
argument_list|(
name|key
operator|.
name|repositoryClass
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Required one of option: collection or repositoryClass"
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setNitriteCollection
argument_list|(
name|nitriteCollection
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|closeDb (String path)
specifier|protected
name|void
name|closeDb
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|String
name|normalized
init|=
name|FileUtil
operator|.
name|compactPath
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|Nitrite
name|db
init|=
name|databaseCache
operator|.
name|get
argument_list|(
name|normalized
argument_list|)
decl_stmt|;
if|if
condition|(
name|db
operator|!=
literal|null
operator|&&
operator|!
name|db
operator|.
name|isClosed
argument_list|()
condition|)
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|path
range|:
name|databaseCache
operator|.
name|keySet
argument_list|()
control|)
block|{
name|closeDb
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
name|databaseCache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|collectionCache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|class|CollectionCacheKey
specifier|private
specifier|static
class|class
name|CollectionCacheKey
block|{
DECL|field|database
name|Nitrite
name|database
decl_stmt|;
DECL|field|collection
name|String
name|collection
decl_stmt|;
DECL|field|repositoryName
name|String
name|repositoryName
decl_stmt|;
DECL|field|repositoryClass
name|Class
argument_list|<
name|?
argument_list|>
name|repositoryClass
decl_stmt|;
DECL|method|CollectionCacheKey (NitriteEndpoint endpoint)
name|CollectionCacheKey
parameter_list|(
name|NitriteEndpoint
name|endpoint
parameter_list|)
block|{
name|database
operator|=
name|endpoint
operator|.
name|getNitriteDatabase
argument_list|()
expr_stmt|;
name|collection
operator|=
name|endpoint
operator|.
name|getCollection
argument_list|()
expr_stmt|;
name|repositoryName
operator|=
name|endpoint
operator|.
name|getRepositoryName
argument_list|()
expr_stmt|;
name|repositoryClass
operator|=
name|endpoint
operator|.
name|getRepositoryClass
argument_list|()
expr_stmt|;
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
name|CollectionCacheKey
name|that
init|=
operator|(
name|CollectionCacheKey
operator|)
name|o
decl_stmt|;
return|return
name|database
operator|.
name|equals
argument_list|(
name|that
operator|.
name|database
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|collection
argument_list|,
name|that
operator|.
name|collection
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|repositoryName
argument_list|,
name|that
operator|.
name|repositoryName
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|repositoryClass
argument_list|,
name|that
operator|.
name|repositoryClass
argument_list|)
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
name|Objects
operator|.
name|hash
argument_list|(
name|database
argument_list|,
name|collection
argument_list|,
name|repositoryName
argument_list|,
name|repositoryClass
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

