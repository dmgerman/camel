begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ehcache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ehcache
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|RuntimeCamelException
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
name|UriParam
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
name|UriParams
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
name|ObjectHelper
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
name|function
operator|.
name|ThrowingHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|config
operator|.
name|CacheConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|config
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|event
operator|.
name|EventFiring
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|event
operator|.
name|EventOrdering
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|event
operator|.
name|EventType
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|EhcacheConfiguration
specifier|public
class|class
name|EhcacheConfiguration
implements|implements
name|Cloneable
block|{
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|createCacheIfNotExist
specifier|private
name|boolean
name|createCacheIfNotExist
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|action
specifier|private
name|String
name|action
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|key
specifier|private
name|Object
name|key
decl_stmt|;
annotation|@
name|UriParam
DECL|field|cacheManager
specifier|private
name|CacheManager
name|cacheManager
decl_stmt|;
annotation|@
name|UriParam
DECL|field|cacheManagerConfiguration
specifier|private
name|Configuration
name|cacheManagerConfiguration
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configurationUri
specifier|private
name|String
name|configurationUri
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configuration
specifier|private
name|CacheConfiguration
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|configuration
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configurations
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|CacheConfiguration
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|configurations
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|javaType
operator|=
literal|"java.lang.String"
argument_list|,
name|defaultValue
operator|=
literal|"java.lang.Object"
argument_list|)
DECL|field|keyType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|keyType
init|=
name|Object
operator|.
name|class
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|javaType
operator|=
literal|"java.lang.String"
argument_list|,
name|defaultValue
operator|=
literal|"java.lang.Object"
argument_list|)
DECL|field|valueType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|valueType
init|=
name|Object
operator|.
name|class
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"ORDERED"
argument_list|)
DECL|field|eventOrdering
specifier|private
name|EventOrdering
name|eventOrdering
init|=
name|EventOrdering
operator|.
name|ORDERED
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"ASYNCHRONOUS"
argument_list|)
DECL|field|eventFiring
specifier|private
name|EventFiring
name|eventFiring
init|=
name|EventFiring
operator|.
name|ASYNCHRONOUS
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|enums
operator|=
literal|"EVICTED,EXPIRED,REMOVED,CREATED,UPDATED"
argument_list|,
name|defaultValue
operator|=
literal|"EVICTED,EXPIRED,REMOVED,CREATED,UPDATED"
argument_list|)
DECL|field|eventTypes
specifier|private
name|Set
argument_list|<
name|EventType
argument_list|>
name|eventTypes
init|=
name|EnumSet
operator|.
name|of
argument_list|(
name|EventType
operator|.
name|values
argument_list|()
index|[
literal|0
index|]
argument_list|,
name|EventType
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|EhcacheConfiguration ()
specifier|public
name|EhcacheConfiguration
parameter_list|()
block|{     }
comment|/**      * URI pointing to the Ehcache XML configuration file's location      */
DECL|method|setConfigurationUri (String configurationUri)
specifier|public
name|void
name|setConfigurationUri
parameter_list|(
name|String
name|configurationUri
parameter_list|)
block|{
name|this
operator|.
name|configurationUri
operator|=
name|configurationUri
expr_stmt|;
block|}
DECL|method|getConfigurationUri ()
specifier|public
name|String
name|getConfigurationUri
parameter_list|()
block|{
return|return
name|configurationUri
return|;
block|}
DECL|method|hasConfigurationUri ()
specifier|public
name|boolean
name|hasConfigurationUri
parameter_list|()
block|{
return|return
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configurationUri
argument_list|)
return|;
block|}
comment|/**      * @deprecated use {@link #getConfigurationUri()} instead      */
annotation|@
name|Deprecated
DECL|method|getConfigUri ()
specifier|public
name|String
name|getConfigUri
parameter_list|()
block|{
return|return
name|getConfigurationUri
argument_list|()
return|;
block|}
comment|/**      * URI pointing to the Ehcache XML configuration file's location      *      * @deprecated use {@link #setConfigurationUri(String)} instead      */
annotation|@
name|Deprecated
DECL|method|setConfigUri (String configUri)
specifier|public
name|void
name|setConfigUri
parameter_list|(
name|String
name|configUri
parameter_list|)
block|{
name|setConfigurationUri
argument_list|(
name|configUri
argument_list|)
expr_stmt|;
block|}
DECL|method|isCreateCacheIfNotExist ()
specifier|public
name|boolean
name|isCreateCacheIfNotExist
parameter_list|()
block|{
return|return
name|createCacheIfNotExist
return|;
block|}
comment|/**      * Configure if a cache need to be created if it does exist or can't be      * pre-configured.      */
DECL|method|setCreateCacheIfNotExist (boolean createCacheIfNotExist)
specifier|public
name|void
name|setCreateCacheIfNotExist
parameter_list|(
name|boolean
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
comment|/**      * To configure the default cache action. If an action is set in the message      * header, then the operation from the header takes precedence.      */
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
comment|/**      * To configure the default action key. If a key is set in the message      * header, then the key from the header takes precedence.      */
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
DECL|method|getCacheManager ()
specifier|public
name|CacheManager
name|getCacheManager
parameter_list|()
block|{
return|return
name|cacheManager
return|;
block|}
comment|/**      * The cache manager      */
DECL|method|setCacheManager (CacheManager cacheManager)
specifier|public
name|void
name|setCacheManager
parameter_list|(
name|CacheManager
name|cacheManager
parameter_list|)
block|{
name|this
operator|.
name|cacheManager
operator|=
name|cacheManager
expr_stmt|;
block|}
DECL|method|hasCacheManager ()
specifier|public
name|boolean
name|hasCacheManager
parameter_list|()
block|{
return|return
name|this
operator|.
name|cacheManager
operator|!=
literal|null
return|;
block|}
DECL|method|getCacheManagerConfiguration ()
specifier|public
name|Configuration
name|getCacheManagerConfiguration
parameter_list|()
block|{
return|return
name|cacheManagerConfiguration
return|;
block|}
comment|/**      * The cache manager configuration      */
DECL|method|setCacheManagerConfiguration (Configuration cacheManagerConfiguration)
specifier|public
name|void
name|setCacheManagerConfiguration
parameter_list|(
name|Configuration
name|cacheManagerConfiguration
parameter_list|)
block|{
name|this
operator|.
name|cacheManagerConfiguration
operator|=
name|cacheManagerConfiguration
expr_stmt|;
block|}
DECL|method|hasCacheManagerConfiguration ()
specifier|public
name|boolean
name|hasCacheManagerConfiguration
parameter_list|()
block|{
return|return
name|this
operator|.
name|cacheManagerConfiguration
operator|!=
literal|null
return|;
block|}
DECL|method|getEventOrdering ()
specifier|public
name|EventOrdering
name|getEventOrdering
parameter_list|()
block|{
return|return
name|eventOrdering
return|;
block|}
comment|/**      * Set the delivery mode (ordered, unordered)      */
DECL|method|setEventOrdering (String eventOrdering)
specifier|public
name|void
name|setEventOrdering
parameter_list|(
name|String
name|eventOrdering
parameter_list|)
block|{
name|setEventOrdering
argument_list|(
name|EventOrdering
operator|.
name|valueOf
argument_list|(
name|eventOrdering
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|setEventOrdering (EventOrdering eventOrdering)
specifier|public
name|void
name|setEventOrdering
parameter_list|(
name|EventOrdering
name|eventOrdering
parameter_list|)
block|{
name|this
operator|.
name|eventOrdering
operator|=
name|eventOrdering
expr_stmt|;
block|}
DECL|method|getEventFiring ()
specifier|public
name|EventFiring
name|getEventFiring
parameter_list|()
block|{
return|return
name|eventFiring
return|;
block|}
comment|/**      * Set the delivery mode (synchronous, asynchronous)      */
DECL|method|setEventFiring (String eventFiring)
specifier|public
name|void
name|setEventFiring
parameter_list|(
name|String
name|eventFiring
parameter_list|)
block|{
name|setEventFiring
argument_list|(
name|EventFiring
operator|.
name|valueOf
argument_list|(
name|eventFiring
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|setEventFiring (EventFiring eventFiring)
specifier|public
name|void
name|setEventFiring
parameter_list|(
name|EventFiring
name|eventFiring
parameter_list|)
block|{
name|this
operator|.
name|eventFiring
operator|=
name|eventFiring
expr_stmt|;
block|}
DECL|method|getEventTypes ()
specifier|public
name|Set
argument_list|<
name|EventType
argument_list|>
name|getEventTypes
parameter_list|()
block|{
return|return
name|eventTypes
return|;
block|}
comment|/**      * Set the type of events to listen for      */
DECL|method|setEventTypes (String eventTypesString)
specifier|public
name|void
name|setEventTypes
parameter_list|(
name|String
name|eventTypesString
parameter_list|)
block|{
name|Set
argument_list|<
name|EventType
argument_list|>
name|eventTypes
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|String
index|[]
name|events
init|=
name|eventTypesString
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|event
range|:
name|events
control|)
block|{
name|eventTypes
operator|.
name|add
argument_list|(
name|EventType
operator|.
name|valueOf
argument_list|(
name|event
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setEventTypes
argument_list|(
name|eventTypes
argument_list|)
expr_stmt|;
block|}
DECL|method|setEventTypes (Set<EventType> eventTypes)
specifier|public
name|void
name|setEventTypes
parameter_list|(
name|Set
argument_list|<
name|EventType
argument_list|>
name|eventTypes
parameter_list|)
block|{
name|this
operator|.
name|eventTypes
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|eventTypes
argument_list|)
expr_stmt|;
block|}
comment|// ****************************
comment|// Cache Configuration
comment|// ****************************
comment|/**      * The default cache configuration to be used to create caches.      */
DECL|method|setConfiguration (CacheConfiguration<?, ?> configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|CacheConfiguration
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
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
DECL|method|getConfiguration ()
specifier|public
name|CacheConfiguration
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|hasConfiguration ()
specifier|public
name|boolean
name|hasConfiguration
parameter_list|()
block|{
return|return
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
argument_list|)
return|;
block|}
DECL|method|hasConfiguration (String name)
specifier|public
name|boolean
name|hasConfiguration
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|ThrowingHelper
operator|.
name|applyIfNotEmpty
argument_list|(
name|configurations
argument_list|,
name|c
lambda|->
name|c
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
argument_list|,
parameter_list|()
lambda|->
literal|false
argument_list|)
return|;
block|}
comment|/**      * A map of cache configuration to be used to create caches.      */
DECL|method|getConfigurations ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|CacheConfiguration
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|getConfigurations
parameter_list|()
block|{
return|return
name|configurations
return|;
block|}
DECL|method|setConfigurations (Map<String, CacheConfiguration<?, ?>> configurations)
specifier|public
name|void
name|setConfigurations
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|CacheConfiguration
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|configurations
parameter_list|)
block|{
name|this
operator|.
name|configurations
operator|=
name|Map
operator|.
name|class
operator|.
name|cast
argument_list|(
name|configurations
argument_list|)
expr_stmt|;
block|}
DECL|method|addConfigurations (Map<String, CacheConfiguration<?, ?>> configurations)
specifier|public
name|void
name|addConfigurations
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|CacheConfiguration
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|configurations
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|configurations
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|configurations
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|configurations
operator|.
name|putAll
argument_list|(
name|configurations
argument_list|)
expr_stmt|;
block|}
DECL|method|getKeyType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getKeyType
parameter_list|()
block|{
return|return
name|keyType
return|;
block|}
comment|/**      * The cache key type, default "java.lang.Object"      */
DECL|method|setKeyType (Class<?> keyType)
specifier|public
name|void
name|setKeyType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
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
name|Class
argument_list|<
name|?
argument_list|>
name|getValueType
parameter_list|()
block|{
return|return
name|valueType
return|;
block|}
comment|/**      * The cache value type, default "java.lang.Object"      */
DECL|method|setValueType (Class<?> valueType)
specifier|public
name|void
name|setValueType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
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
comment|// ****************************
comment|// Cloneable
comment|// ****************************
DECL|method|copy ()
specifier|public
name|EhcacheConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|EhcacheConfiguration
operator|)
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

