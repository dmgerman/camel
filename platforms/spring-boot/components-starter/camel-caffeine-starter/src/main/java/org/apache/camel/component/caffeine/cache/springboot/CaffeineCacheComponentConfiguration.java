begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.caffeine.cache.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|caffeine
operator|.
name|cache
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|benmanes
operator|.
name|caffeine
operator|.
name|cache
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|benmanes
operator|.
name|caffeine
operator|.
name|cache
operator|.
name|CacheLoader
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Camel Caffeine support  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.caffeine-cache"
argument_list|)
DECL|class|CaffeineCacheComponentConfiguration
specifier|public
class|class
name|CaffeineCacheComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Sets the global component configuration      */
DECL|field|configuration
specifier|private
name|CaffeineConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|CaffeineConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( CaffeineConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|CaffeineConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|class|CaffeineConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|CaffeineConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|caffeine
operator|.
name|CaffeineConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * Configure if a cache need to be created if it does exist or can't be          * pre-configured.          */
DECL|field|createCacheIfNotExist
specifier|private
name|Boolean
name|createCacheIfNotExist
init|=
literal|true
decl_stmt|;
comment|/**          * To configure the default cache action. If an action is set in the          * message header, then the operation from the header takes precedence.          */
DECL|field|action
specifier|private
name|String
name|action
decl_stmt|;
comment|/**          * To configure the default action key. If a key is set in the message          * header, then the key from the header takes precedence.          */
DECL|field|key
specifier|private
name|Object
name|key
decl_stmt|;
comment|/**          * The cache key type, default "java.lang.Object"          */
DECL|field|keyType
specifier|private
name|String
name|keyType
init|=
literal|"java.lang.Object"
decl_stmt|;
comment|/**          * The cache value type, default "java.lang.Object"          */
DECL|field|valueType
specifier|private
name|String
name|valueType
init|=
literal|"java.lang.Object"
decl_stmt|;
comment|/**          * To configure the default an already instantianted cache to be used          */
DECL|field|cache
specifier|private
name|Cache
name|cache
decl_stmt|;
comment|/**          * To configure a CacheLoader in case of a LoadCache use          */
DECL|field|cacheLoader
specifier|private
name|CacheLoader
name|cacheLoader
decl_stmt|;
comment|/**          * To enable stats on the cache          */
DECL|field|statsEnabled
specifier|private
name|Boolean
name|statsEnabled
init|=
literal|false
decl_stmt|;
comment|/**          * Set the initial Capacity for the cache          */
DECL|field|initialCapacity
specifier|private
name|Integer
name|initialCapacity
init|=
literal|10000
decl_stmt|;
comment|/**          * Set the maximum size for the cache          */
DECL|field|maximumSize
specifier|private
name|Integer
name|maximumSize
init|=
literal|10000
decl_stmt|;
DECL|method|getCreateCacheIfNotExist ()
specifier|public
name|Boolean
name|getCreateCacheIfNotExist
parameter_list|()
block|{
return|return
name|createCacheIfNotExist
return|;
block|}
DECL|method|setCreateCacheIfNotExist (Boolean createCacheIfNotExist)
specifier|public
name|void
name|setCreateCacheIfNotExist
parameter_list|(
name|Boolean
name|createCacheIfNotExist
parameter_list|)
block|{
name|this
operator|.
name|createCacheIfNotExist
operator|=
name|createCacheIfNotExist
expr_stmt|;
block|}
DECL|method|getAction ()
specifier|public
name|String
name|getAction
parameter_list|()
block|{
return|return
name|action
return|;
block|}
DECL|method|setAction (String action)
specifier|public
name|void
name|setAction
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|Object
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|setKey (Object key)
specifier|public
name|void
name|setKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
DECL|method|getKeyType ()
specifier|public
name|String
name|getKeyType
parameter_list|()
block|{
return|return
name|keyType
return|;
block|}
DECL|method|setKeyType (String keyType)
specifier|public
name|void
name|setKeyType
parameter_list|(
name|String
name|keyType
parameter_list|)
block|{
name|this
operator|.
name|keyType
operator|=
name|keyType
expr_stmt|;
block|}
DECL|method|getValueType ()
specifier|public
name|String
name|getValueType
parameter_list|()
block|{
return|return
name|valueType
return|;
block|}
DECL|method|setValueType (String valueType)
specifier|public
name|void
name|setValueType
parameter_list|(
name|String
name|valueType
parameter_list|)
block|{
name|this
operator|.
name|valueType
operator|=
name|valueType
expr_stmt|;
block|}
DECL|method|getCache ()
specifier|public
name|Cache
name|getCache
parameter_list|()
block|{
return|return
name|cache
return|;
block|}
DECL|method|setCache (Cache cache)
specifier|public
name|void
name|setCache
parameter_list|(
name|Cache
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
DECL|method|getCacheLoader ()
specifier|public
name|CacheLoader
name|getCacheLoader
parameter_list|()
block|{
return|return
name|cacheLoader
return|;
block|}
DECL|method|setCacheLoader (CacheLoader cacheLoader)
specifier|public
name|void
name|setCacheLoader
parameter_list|(
name|CacheLoader
name|cacheLoader
parameter_list|)
block|{
name|this
operator|.
name|cacheLoader
operator|=
name|cacheLoader
expr_stmt|;
block|}
DECL|method|getStatsEnabled ()
specifier|public
name|Boolean
name|getStatsEnabled
parameter_list|()
block|{
return|return
name|statsEnabled
return|;
block|}
DECL|method|setStatsEnabled (Boolean statsEnabled)
specifier|public
name|void
name|setStatsEnabled
parameter_list|(
name|Boolean
name|statsEnabled
parameter_list|)
block|{
name|this
operator|.
name|statsEnabled
operator|=
name|statsEnabled
expr_stmt|;
block|}
DECL|method|getInitialCapacity ()
specifier|public
name|Integer
name|getInitialCapacity
parameter_list|()
block|{
return|return
name|initialCapacity
return|;
block|}
DECL|method|setInitialCapacity (Integer initialCapacity)
specifier|public
name|void
name|setInitialCapacity
parameter_list|(
name|Integer
name|initialCapacity
parameter_list|)
block|{
name|this
operator|.
name|initialCapacity
operator|=
name|initialCapacity
expr_stmt|;
block|}
DECL|method|getMaximumSize ()
specifier|public
name|Integer
name|getMaximumSize
parameter_list|()
block|{
return|return
name|maximumSize
return|;
block|}
DECL|method|setMaximumSize (Integer maximumSize)
specifier|public
name|void
name|setMaximumSize
parameter_list|(
name|Integer
name|maximumSize
parameter_list|)
block|{
name|this
operator|.
name|maximumSize
operator|=
name|maximumSize
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

