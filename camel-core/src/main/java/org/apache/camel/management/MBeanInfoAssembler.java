begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
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
name|LinkedHashSet
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
name|javax
operator|.
name|management
operator|.
name|Descriptor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|IntrospectionException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|JMException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|modelmbean
operator|.
name|ModelMBeanAttributeInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|modelmbean
operator|.
name|ModelMBeanInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|modelmbean
operator|.
name|ModelMBeanInfoSupport
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|modelmbean
operator|.
name|ModelMBeanNotificationInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|modelmbean
operator|.
name|ModelMBeanOperationInfo
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
name|CamelContext
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
name|Service
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedNotification
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
name|api
operator|.
name|management
operator|.
name|ManagedNotifications
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|IntrospectionSupport
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
name|LRUCache
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
name|LRUCacheFactory
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A Camel specific {@link javax.management.MBeanInfo} assembler that reads the  * details from the {@link ManagedResource}, {@link ManagedAttribute}, {@link ManagedOperation},  * {@link ManagedNotification}, and {@link ManagedNotifications} annotations.  */
end_comment

begin_class
DECL|class|MBeanInfoAssembler
specifier|public
class|class
name|MBeanInfoAssembler
implements|implements
name|Service
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MBeanInfoAssembler
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// use a cache to speedup gathering JMX MBeanInfo for known classes
comment|// use a weak cache as we dont want the cache to keep around as it reference classes
comment|// which could prevent classloader to unload classes if being referenced from this cache
DECL|field|cache
specifier|private
name|LRUCache
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|MBeanAttributesAndOperations
argument_list|>
name|cache
decl_stmt|;
DECL|method|MBeanInfoAssembler ()
specifier|public
name|MBeanInfoAssembler
parameter_list|()
block|{     }
annotation|@
name|Deprecated
DECL|method|MBeanInfoAssembler (CamelContext camelContext)
specifier|public
name|MBeanInfoAssembler
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{     }
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|cache
operator|=
name|LRUCacheFactory
operator|.
name|newLRUWeakCache
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Clearing cache[size={}, hits={}, misses={}, evicted={}]"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|cache
operator|.
name|size
argument_list|()
block|,
name|cache
operator|.
name|getHits
argument_list|()
block|,
name|cache
operator|.
name|getMisses
argument_list|()
block|,
name|cache
operator|.
name|getEvicted
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cache
operator|!=
literal|null
condition|)
block|{
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Structure to hold cached mbean attributes and operations for a given class.      */
DECL|class|MBeanAttributesAndOperations
specifier|private
specifier|static
specifier|final
class|class
name|MBeanAttributesAndOperations
block|{
DECL|field|attributes
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedAttributeInfo
argument_list|>
name|attributes
decl_stmt|;
DECL|field|operations
specifier|private
name|Set
argument_list|<
name|ManagedOperationInfo
argument_list|>
name|operations
decl_stmt|;
block|}
comment|/**      * Gets the {@link ModelMBeanInfo} for the given managed bean      *      * @param defaultManagedBean  the default managed bean      * @param customManagedBean   an optional custom managed bean      * @param objectName   the object name      * @return the model info, or<tt>null</tt> if not possible to create, for example due the managed bean is a proxy class      * @throws JMException is thrown if error creating the model info      */
DECL|method|getMBeanInfo (Object defaultManagedBean, Object customManagedBean, String objectName)
specifier|public
name|ModelMBeanInfo
name|getMBeanInfo
parameter_list|(
name|Object
name|defaultManagedBean
parameter_list|,
name|Object
name|customManagedBean
parameter_list|,
name|String
name|objectName
parameter_list|)
throws|throws
name|JMException
block|{
comment|// skip proxy classes
if|if
condition|(
name|defaultManagedBean
operator|!=
literal|null
operator|&&
name|Proxy
operator|.
name|isProxyClass
argument_list|(
name|defaultManagedBean
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Skip creating ModelMBeanInfo due proxy class {}"
argument_list|,
name|defaultManagedBean
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|// maps and lists to contain information about attributes and operations
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedAttributeInfo
argument_list|>
name|attributes
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|ManagedAttributeInfo
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ManagedOperationInfo
argument_list|>
name|operations
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|ManagedOperationInfo
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ModelMBeanAttributeInfo
argument_list|>
name|mBeanAttributes
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|ModelMBeanAttributeInfo
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ModelMBeanOperationInfo
argument_list|>
name|mBeanOperations
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|ModelMBeanOperationInfo
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ModelMBeanNotificationInfo
argument_list|>
name|mBeanNotifications
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|ModelMBeanNotificationInfo
argument_list|>
argument_list|()
decl_stmt|;
comment|// extract details from default managed bean
if|if
condition|(
name|defaultManagedBean
operator|!=
literal|null
condition|)
block|{
name|extractAttributesAndOperations
argument_list|(
name|defaultManagedBean
operator|.
name|getClass
argument_list|()
argument_list|,
name|attributes
argument_list|,
name|operations
argument_list|)
expr_stmt|;
name|extractMbeanAttributes
argument_list|(
name|defaultManagedBean
argument_list|,
name|attributes
argument_list|,
name|mBeanAttributes
argument_list|,
name|mBeanOperations
argument_list|)
expr_stmt|;
name|extractMbeanOperations
argument_list|(
name|defaultManagedBean
argument_list|,
name|operations
argument_list|,
name|mBeanOperations
argument_list|)
expr_stmt|;
name|extractMbeanNotifications
argument_list|(
name|defaultManagedBean
argument_list|,
name|mBeanNotifications
argument_list|)
expr_stmt|;
block|}
comment|// extract details from custom managed bean
if|if
condition|(
name|customManagedBean
operator|!=
literal|null
condition|)
block|{
name|extractAttributesAndOperations
argument_list|(
name|customManagedBean
operator|.
name|getClass
argument_list|()
argument_list|,
name|attributes
argument_list|,
name|operations
argument_list|)
expr_stmt|;
name|extractMbeanAttributes
argument_list|(
name|customManagedBean
argument_list|,
name|attributes
argument_list|,
name|mBeanAttributes
argument_list|,
name|mBeanOperations
argument_list|)
expr_stmt|;
name|extractMbeanOperations
argument_list|(
name|customManagedBean
argument_list|,
name|operations
argument_list|,
name|mBeanOperations
argument_list|)
expr_stmt|;
name|extractMbeanNotifications
argument_list|(
name|customManagedBean
argument_list|,
name|mBeanNotifications
argument_list|)
expr_stmt|;
block|}
comment|// create the ModelMBeanInfo
name|String
name|name
init|=
name|getName
argument_list|(
name|customManagedBean
operator|!=
literal|null
condition|?
name|customManagedBean
else|:
name|defaultManagedBean
argument_list|,
name|objectName
argument_list|)
decl_stmt|;
name|String
name|description
init|=
name|getDescription
argument_list|(
name|customManagedBean
operator|!=
literal|null
condition|?
name|customManagedBean
else|:
name|defaultManagedBean
argument_list|,
name|objectName
argument_list|)
decl_stmt|;
name|ModelMBeanAttributeInfo
index|[]
name|arrayAttributes
init|=
name|mBeanAttributes
operator|.
name|toArray
argument_list|(
operator|new
name|ModelMBeanAttributeInfo
index|[
name|mBeanAttributes
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|ModelMBeanOperationInfo
index|[]
name|arrayOperations
init|=
name|mBeanOperations
operator|.
name|toArray
argument_list|(
operator|new
name|ModelMBeanOperationInfo
index|[
name|mBeanOperations
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|ModelMBeanNotificationInfo
index|[]
name|arrayNotifications
init|=
name|mBeanNotifications
operator|.
name|toArray
argument_list|(
operator|new
name|ModelMBeanNotificationInfo
index|[
name|mBeanNotifications
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|ModelMBeanInfo
name|info
init|=
operator|new
name|ModelMBeanInfoSupport
argument_list|(
name|name
argument_list|,
name|description
argument_list|,
name|arrayAttributes
argument_list|,
literal|null
argument_list|,
name|arrayOperations
argument_list|,
name|arrayNotifications
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Created ModelMBeanInfo {}"
argument_list|,
name|info
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|method|extractAttributesAndOperations (Class<?> managedClass, Map<String, ManagedAttributeInfo> attributes, Set<ManagedOperationInfo> operations)
specifier|private
name|void
name|extractAttributesAndOperations
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|managedClass
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedAttributeInfo
argument_list|>
name|attributes
parameter_list|,
name|Set
argument_list|<
name|ManagedOperationInfo
argument_list|>
name|operations
parameter_list|)
block|{
name|MBeanAttributesAndOperations
name|cached
init|=
name|cache
operator|.
name|get
argument_list|(
name|managedClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|cached
operator|==
literal|null
condition|)
block|{
name|doExtractAttributesAndOperations
argument_list|(
name|managedClass
argument_list|,
name|attributes
argument_list|,
name|operations
argument_list|)
expr_stmt|;
name|cached
operator|=
operator|new
name|MBeanAttributesAndOperations
argument_list|()
expr_stmt|;
name|cached
operator|.
name|attributes
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|ManagedAttributeInfo
argument_list|>
argument_list|(
name|attributes
argument_list|)
expr_stmt|;
name|cached
operator|.
name|operations
operator|=
operator|new
name|LinkedHashSet
argument_list|<
name|ManagedOperationInfo
argument_list|>
argument_list|(
name|operations
argument_list|)
expr_stmt|;
comment|// clear before we re-add them
name|attributes
operator|.
name|clear
argument_list|()
expr_stmt|;
name|operations
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// add to cache
name|cache
operator|.
name|put
argument_list|(
name|managedClass
argument_list|,
name|cached
argument_list|)
expr_stmt|;
block|}
name|attributes
operator|.
name|putAll
argument_list|(
name|cached
operator|.
name|attributes
argument_list|)
expr_stmt|;
name|operations
operator|.
name|addAll
argument_list|(
name|cached
operator|.
name|operations
argument_list|)
expr_stmt|;
block|}
DECL|method|doExtractAttributesAndOperations (Class<?> managedClass, Map<String, ManagedAttributeInfo> attributes, Set<ManagedOperationInfo> operations)
specifier|private
name|void
name|doExtractAttributesAndOperations
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|managedClass
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedAttributeInfo
argument_list|>
name|attributes
parameter_list|,
name|Set
argument_list|<
name|ManagedOperationInfo
argument_list|>
name|operations
parameter_list|)
block|{
comment|// extract the class
name|doDoExtractAttributesAndOperations
argument_list|(
name|managedClass
argument_list|,
name|attributes
argument_list|,
name|operations
argument_list|)
expr_stmt|;
comment|// and then any sub classes
if|if
condition|(
name|managedClass
operator|.
name|getSuperclass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|managedClass
operator|.
name|getSuperclass
argument_list|()
decl_stmt|;
comment|// skip any JDK classes
if|if
condition|(
operator|!
name|clazz
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"java"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Extracting attributes and operations from sub class: {}"
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
name|doExtractAttributesAndOperations
argument_list|(
name|clazz
argument_list|,
name|attributes
argument_list|,
name|operations
argument_list|)
expr_stmt|;
block|}
block|}
comment|// and then any additional interfaces (as interfaces can be annotated as well)
if|if
condition|(
name|managedClass
operator|.
name|getInterfaces
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
range|:
name|managedClass
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
comment|// recursive as there may be multiple interfaces
if|if
condition|(
name|clazz
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"java"
argument_list|)
condition|)
block|{
comment|// skip any JDK classes
continue|continue;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Extracting attributes and operations from implemented interface: {}"
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
name|doExtractAttributesAndOperations
argument_list|(
name|clazz
argument_list|,
name|attributes
argument_list|,
name|operations
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|doDoExtractAttributesAndOperations (Class<?> managedClass, Map<String, ManagedAttributeInfo> attributes, Set<ManagedOperationInfo> operations)
specifier|private
name|void
name|doDoExtractAttributesAndOperations
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|managedClass
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedAttributeInfo
argument_list|>
name|attributes
parameter_list|,
name|Set
argument_list|<
name|ManagedOperationInfo
argument_list|>
name|operations
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Extracting attributes and operations from class: {}"
argument_list|,
name|managedClass
argument_list|)
expr_stmt|;
comment|// introspect the class, and leverage the cache to have better performance
name|IntrospectionSupport
operator|.
name|ClassInfo
name|cache
init|=
name|IntrospectionSupport
operator|.
name|cacheClass
argument_list|(
name|managedClass
argument_list|)
decl_stmt|;
for|for
control|(
name|IntrospectionSupport
operator|.
name|MethodInfo
name|cacheInfo
range|:
name|cache
operator|.
name|methods
control|)
block|{
comment|// must be from declaring class
if|if
condition|(
name|cacheInfo
operator|.
name|method
operator|.
name|getDeclaringClass
argument_list|()
operator|!=
name|managedClass
condition|)
block|{
continue|continue;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Extracting attributes and operations from method: {}"
argument_list|,
name|cacheInfo
operator|.
name|method
argument_list|)
expr_stmt|;
name|ManagedAttribute
name|ma
init|=
name|cacheInfo
operator|.
name|method
operator|.
name|getAnnotation
argument_list|(
name|ManagedAttribute
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ma
operator|!=
literal|null
condition|)
block|{
name|String
name|key
decl_stmt|;
name|String
name|desc
init|=
name|ma
operator|.
name|description
argument_list|()
decl_stmt|;
name|Method
name|getter
init|=
literal|null
decl_stmt|;
name|Method
name|setter
init|=
literal|null
decl_stmt|;
name|boolean
name|mask
init|=
name|ma
operator|.
name|mask
argument_list|()
decl_stmt|;
if|if
condition|(
name|cacheInfo
operator|.
name|isGetter
condition|)
block|{
name|key
operator|=
name|cacheInfo
operator|.
name|getterOrSetterShorthandName
expr_stmt|;
name|getter
operator|=
name|cacheInfo
operator|.
name|method
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cacheInfo
operator|.
name|isSetter
condition|)
block|{
name|key
operator|=
name|cacheInfo
operator|.
name|getterOrSetterShorthandName
expr_stmt|;
name|setter
operator|=
name|cacheInfo
operator|.
name|method
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"@ManagedAttribute can only be used on Java bean methods, was: "
operator|+
name|cacheInfo
operator|.
name|method
operator|+
literal|" on bean: "
operator|+
name|managedClass
argument_list|)
throw|;
block|}
comment|// they key must be capitalized
name|key
operator|=
name|ObjectHelper
operator|.
name|capitalize
argument_list|(
name|key
argument_list|)
expr_stmt|;
comment|// lookup first
name|ManagedAttributeInfo
name|info
init|=
name|attributes
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|==
literal|null
condition|)
block|{
name|info
operator|=
operator|new
name|ManagedAttributeInfo
argument_list|(
name|key
argument_list|,
name|desc
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getter
operator|!=
literal|null
condition|)
block|{
name|info
operator|.
name|setGetter
argument_list|(
name|getter
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|setter
operator|!=
literal|null
condition|)
block|{
name|info
operator|.
name|setSetter
argument_list|(
name|setter
argument_list|)
expr_stmt|;
block|}
name|info
operator|.
name|setMask
argument_list|(
name|mask
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
comment|// operations
name|ManagedOperation
name|mo
init|=
name|cacheInfo
operator|.
name|method
operator|.
name|getAnnotation
argument_list|(
name|ManagedOperation
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|mo
operator|!=
literal|null
condition|)
block|{
name|String
name|desc
init|=
name|mo
operator|.
name|description
argument_list|()
decl_stmt|;
name|Method
name|operation
init|=
name|cacheInfo
operator|.
name|method
decl_stmt|;
name|boolean
name|mask
init|=
name|mo
operator|.
name|mask
argument_list|()
decl_stmt|;
name|operations
operator|.
name|add
argument_list|(
operator|new
name|ManagedOperationInfo
argument_list|(
name|desc
argument_list|,
name|operation
argument_list|,
name|mask
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|extractMbeanAttributes (Object managedBean, Map<String, ManagedAttributeInfo> attributes, Set<ModelMBeanAttributeInfo> mBeanAttributes, Set<ModelMBeanOperationInfo> mBeanOperations)
specifier|private
name|void
name|extractMbeanAttributes
parameter_list|(
name|Object
name|managedBean
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedAttributeInfo
argument_list|>
name|attributes
parameter_list|,
name|Set
argument_list|<
name|ModelMBeanAttributeInfo
argument_list|>
name|mBeanAttributes
parameter_list|,
name|Set
argument_list|<
name|ModelMBeanOperationInfo
argument_list|>
name|mBeanOperations
parameter_list|)
throws|throws
name|IntrospectionException
block|{
for|for
control|(
name|ManagedAttributeInfo
name|info
range|:
name|attributes
operator|.
name|values
argument_list|()
control|)
block|{
name|ModelMBeanAttributeInfo
name|mbeanAttribute
init|=
operator|new
name|ModelMBeanAttributeInfo
argument_list|(
name|info
operator|.
name|getKey
argument_list|()
argument_list|,
name|info
operator|.
name|getDescription
argument_list|()
argument_list|,
name|info
operator|.
name|getGetter
argument_list|()
argument_list|,
name|info
operator|.
name|getSetter
argument_list|()
argument_list|)
decl_stmt|;
comment|// add missing attribute descriptors, this is needed to have attributes accessible
name|Descriptor
name|desc
init|=
name|mbeanAttribute
operator|.
name|getDescriptor
argument_list|()
decl_stmt|;
name|desc
operator|.
name|setField
argument_list|(
literal|"mask"
argument_list|,
name|info
operator|.
name|isMask
argument_list|()
condition|?
literal|"true"
else|:
literal|"false"
argument_list|)
expr_stmt|;
if|if
condition|(
name|info
operator|.
name|getGetter
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|desc
operator|.
name|setField
argument_list|(
literal|"getMethod"
argument_list|,
name|info
operator|.
name|getGetter
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// attribute must also be added as mbean operation
name|ModelMBeanOperationInfo
name|mbeanOperation
init|=
operator|new
name|ModelMBeanOperationInfo
argument_list|(
name|info
operator|.
name|getKey
argument_list|()
argument_list|,
name|info
operator|.
name|getGetter
argument_list|()
argument_list|)
decl_stmt|;
name|Descriptor
name|opDesc
init|=
name|mbeanOperation
operator|.
name|getDescriptor
argument_list|()
decl_stmt|;
name|opDesc
operator|.
name|setField
argument_list|(
literal|"mask"
argument_list|,
name|info
operator|.
name|isMask
argument_list|()
condition|?
literal|"true"
else|:
literal|"false"
argument_list|)
expr_stmt|;
name|mbeanOperation
operator|.
name|setDescriptor
argument_list|(
name|opDesc
argument_list|)
expr_stmt|;
name|mBeanOperations
operator|.
name|add
argument_list|(
name|mbeanOperation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|info
operator|.
name|getSetter
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|desc
operator|.
name|setField
argument_list|(
literal|"setMethod"
argument_list|,
name|info
operator|.
name|getSetter
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// attribute must also be added as mbean operation
name|ModelMBeanOperationInfo
name|mbeanOperation
init|=
operator|new
name|ModelMBeanOperationInfo
argument_list|(
name|info
operator|.
name|getKey
argument_list|()
argument_list|,
name|info
operator|.
name|getSetter
argument_list|()
argument_list|)
decl_stmt|;
name|mBeanOperations
operator|.
name|add
argument_list|(
name|mbeanOperation
argument_list|)
expr_stmt|;
block|}
name|mbeanAttribute
operator|.
name|setDescriptor
argument_list|(
name|desc
argument_list|)
expr_stmt|;
name|mBeanAttributes
operator|.
name|add
argument_list|(
name|mbeanAttribute
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Assembled attribute: {}"
argument_list|,
name|mbeanAttribute
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|extractMbeanOperations (Object managedBean, Set<ManagedOperationInfo> operations, Set<ModelMBeanOperationInfo> mBeanOperations)
specifier|private
name|void
name|extractMbeanOperations
parameter_list|(
name|Object
name|managedBean
parameter_list|,
name|Set
argument_list|<
name|ManagedOperationInfo
argument_list|>
name|operations
parameter_list|,
name|Set
argument_list|<
name|ModelMBeanOperationInfo
argument_list|>
name|mBeanOperations
parameter_list|)
block|{
for|for
control|(
name|ManagedOperationInfo
name|info
range|:
name|operations
control|)
block|{
name|ModelMBeanOperationInfo
name|mbean
init|=
operator|new
name|ModelMBeanOperationInfo
argument_list|(
name|info
operator|.
name|getDescription
argument_list|()
argument_list|,
name|info
operator|.
name|getOperation
argument_list|()
argument_list|)
decl_stmt|;
name|Descriptor
name|opDesc
init|=
name|mbean
operator|.
name|getDescriptor
argument_list|()
decl_stmt|;
name|opDesc
operator|.
name|setField
argument_list|(
literal|"mask"
argument_list|,
name|info
operator|.
name|isMask
argument_list|()
condition|?
literal|"true"
else|:
literal|"false"
argument_list|)
expr_stmt|;
name|mbean
operator|.
name|setDescriptor
argument_list|(
name|opDesc
argument_list|)
expr_stmt|;
name|mBeanOperations
operator|.
name|add
argument_list|(
name|mbean
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Assembled operation: {}"
argument_list|,
name|mbean
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|extractMbeanNotifications (Object managedBean, Set<ModelMBeanNotificationInfo> mBeanNotifications)
specifier|private
name|void
name|extractMbeanNotifications
parameter_list|(
name|Object
name|managedBean
parameter_list|,
name|Set
argument_list|<
name|ModelMBeanNotificationInfo
argument_list|>
name|mBeanNotifications
parameter_list|)
block|{
name|ManagedNotifications
name|notifications
init|=
name|managedBean
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|ManagedNotifications
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|notifications
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ManagedNotification
name|notification
range|:
name|notifications
operator|.
name|value
argument_list|()
control|)
block|{
name|ModelMBeanNotificationInfo
name|info
init|=
operator|new
name|ModelMBeanNotificationInfo
argument_list|(
name|notification
operator|.
name|notificationTypes
argument_list|()
argument_list|,
name|notification
operator|.
name|name
argument_list|()
argument_list|,
name|notification
operator|.
name|description
argument_list|()
argument_list|)
decl_stmt|;
name|mBeanNotifications
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Assembled notification: {}"
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getDescription (Object managedBean, String objectName)
specifier|private
name|String
name|getDescription
parameter_list|(
name|Object
name|managedBean
parameter_list|,
name|String
name|objectName
parameter_list|)
block|{
name|ManagedResource
name|mr
init|=
name|ObjectHelper
operator|.
name|getAnnotation
argument_list|(
name|managedBean
argument_list|,
name|ManagedResource
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|mr
operator|!=
literal|null
condition|?
name|mr
operator|.
name|description
argument_list|()
else|:
literal|""
return|;
block|}
DECL|method|getName (Object managedBean, String objectName)
specifier|private
name|String
name|getName
parameter_list|(
name|Object
name|managedBean
parameter_list|,
name|String
name|objectName
parameter_list|)
block|{
return|return
name|managedBean
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
DECL|class|ManagedAttributeInfo
specifier|private
specifier|static
specifier|final
class|class
name|ManagedAttributeInfo
block|{
DECL|field|key
specifier|private
name|String
name|key
decl_stmt|;
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|field|getter
specifier|private
name|Method
name|getter
decl_stmt|;
DECL|field|setter
specifier|private
name|Method
name|setter
decl_stmt|;
DECL|field|mask
specifier|private
name|boolean
name|mask
decl_stmt|;
DECL|method|ManagedAttributeInfo (String key, String description)
specifier|private
name|ManagedAttributeInfo
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|getGetter ()
specifier|public
name|Method
name|getGetter
parameter_list|()
block|{
return|return
name|getter
return|;
block|}
DECL|method|setGetter (Method getter)
specifier|public
name|void
name|setGetter
parameter_list|(
name|Method
name|getter
parameter_list|)
block|{
name|this
operator|.
name|getter
operator|=
name|getter
expr_stmt|;
block|}
DECL|method|getSetter ()
specifier|public
name|Method
name|getSetter
parameter_list|()
block|{
return|return
name|setter
return|;
block|}
DECL|method|setSetter (Method setter)
specifier|public
name|void
name|setSetter
parameter_list|(
name|Method
name|setter
parameter_list|)
block|{
name|this
operator|.
name|setter
operator|=
name|setter
expr_stmt|;
block|}
DECL|method|isMask ()
specifier|public
name|boolean
name|isMask
parameter_list|()
block|{
return|return
name|mask
return|;
block|}
DECL|method|setMask (boolean mask)
specifier|public
name|void
name|setMask
parameter_list|(
name|boolean
name|mask
parameter_list|)
block|{
name|this
operator|.
name|mask
operator|=
name|mask
expr_stmt|;
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
literal|"ManagedAttributeInfo: ["
operator|+
name|key
operator|+
literal|" + getter: "
operator|+
name|getter
operator|+
literal|", setter: "
operator|+
name|setter
operator|+
literal|"]"
return|;
block|}
block|}
DECL|class|ManagedOperationInfo
specifier|private
specifier|static
specifier|final
class|class
name|ManagedOperationInfo
block|{
DECL|field|description
specifier|private
specifier|final
name|String
name|description
decl_stmt|;
DECL|field|operation
specifier|private
specifier|final
name|Method
name|operation
decl_stmt|;
DECL|field|mask
specifier|private
specifier|final
name|boolean
name|mask
decl_stmt|;
DECL|method|ManagedOperationInfo (String description, Method operation, boolean mask)
specifier|private
name|ManagedOperationInfo
parameter_list|(
name|String
name|description
parameter_list|,
name|Method
name|operation
parameter_list|,
name|boolean
name|mask
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
name|this
operator|.
name|mask
operator|=
name|mask
expr_stmt|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|getOperation ()
specifier|public
name|Method
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|isMask ()
specifier|public
name|boolean
name|isMask
parameter_list|()
block|{
return|return
name|mask
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
literal|"ManagedOperationInfo: ["
operator|+
name|operation
operator|+
literal|"]"
return|;
block|}
block|}
block|}
end_class

end_unit

