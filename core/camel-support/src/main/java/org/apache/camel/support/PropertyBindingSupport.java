begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|PropertyBindingException
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
name|Method
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
name|Iterator
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|IntrospectionSupport
operator|.
name|findSetterMethods
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|IntrospectionSupport
operator|.
name|getOrElseProperty
import|;
end_import

begin_comment
comment|/**  * A convenient support class for binding String valued properties to an instance which  * uses a set of conventions:  *<ul>  *<li>property placeholders - Keys and values using Camels property placeholder will be resolved</li>  *<li>nested - Properties can be nested using the dot syntax (OGNL and builder pattern using with as prefix), eg foo.bar=123</li>  *<li>reference by bean id - Values can refer to other beans in the registry by prefixing with #nean: eg #bean:myBean</li>  *<li>reference by type - Values can refer to singleton beans by their type in the registry by prefixing with #type: syntax, eg #type:com.foo.MyClassType</li>  *<li>autowire by type - Values can refer to singleton beans by auto wiring by setting the value to #autowire</li>  *<li>reference new class - Values can refer to creating new beans by their class name by prefixing with #class, eg #class:com.foo.MyClassType</li>  *</ul>  * This implementations reuses parts of {@link IntrospectionSupport}.  */
end_comment

begin_class
DECL|class|PropertyBindingSupport
specifier|public
specifier|final
class|class
name|PropertyBindingSupport
block|{
comment|// TODO: Add support for Map/List
comment|/**      * To use a fluent builder style to configure this property binding support.      */
DECL|class|Builder
specifier|public
specifier|static
class|class
name|Builder
block|{
DECL|field|nesting
specifier|private
name|boolean
name|nesting
init|=
literal|true
decl_stmt|;
DECL|field|deepNesting
specifier|private
name|boolean
name|deepNesting
init|=
literal|true
decl_stmt|;
DECL|field|reference
specifier|private
name|boolean
name|reference
init|=
literal|true
decl_stmt|;
DECL|field|placeholder
specifier|private
name|boolean
name|placeholder
init|=
literal|true
decl_stmt|;
DECL|field|fluentBuilder
specifier|private
name|boolean
name|fluentBuilder
init|=
literal|true
decl_stmt|;
comment|/**          * Whether nesting is in use          */
DECL|method|withNesting (boolean nesting)
specifier|public
name|Builder
name|withNesting
parameter_list|(
name|boolean
name|nesting
parameter_list|)
block|{
name|this
operator|.
name|nesting
operator|=
name|nesting
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether deep nesting is in use, where Camel will attempt to walk as deep as possible by creating new objects in the OGNL graph if          * a property has a setter and the object can be created from a default no-arg constructor.          */
DECL|method|withDeepNesting (boolean deepNesting)
specifier|public
name|Builder
name|withDeepNesting
parameter_list|(
name|boolean
name|deepNesting
parameter_list|)
block|{
name|this
operator|.
name|deepNesting
operator|=
name|deepNesting
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether reference parameter (syntax starts with #) is in use          */
DECL|method|withReference (boolean reference)
specifier|public
name|Builder
name|withReference
parameter_list|(
name|boolean
name|reference
parameter_list|)
block|{
name|this
operator|.
name|reference
operator|=
name|reference
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to use Camels property placeholder to resolve placeholders on keys and values          */
DECL|method|withPlaceholder (boolean placeholder)
specifier|public
name|Builder
name|withPlaceholder
parameter_list|(
name|boolean
name|placeholder
parameter_list|)
block|{
name|this
operator|.
name|placeholder
operator|=
name|placeholder
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether fluent builder is allowed as a valid getter/setter          */
DECL|method|withFluentBuilder (boolean fluentBuilder)
specifier|public
name|Builder
name|withFluentBuilder
parameter_list|(
name|boolean
name|fluentBuilder
parameter_list|)
block|{
name|this
operator|.
name|fluentBuilder
operator|=
name|fluentBuilder
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Binds the properties to the target object, and removes the property that was bound from properties.          *          * @param camelContext  the camel context          * @param target        the target object          * @param properties    the properties where the bound properties will be removed from          * @return              true if one or more properties was bound          */
DECL|method|bind (CamelContext camelContext, Object target, Map<String, Object> properties)
specifier|public
name|boolean
name|bind
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|target
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|target
argument_list|,
literal|"target"
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|properties
argument_list|,
literal|"properties"
argument_list|)
expr_stmt|;
return|return
name|bindProperties
argument_list|(
name|camelContext
argument_list|,
name|target
argument_list|,
name|properties
argument_list|,
name|nesting
argument_list|,
name|deepNesting
argument_list|,
name|fluentBuilder
argument_list|,
name|reference
argument_list|,
name|placeholder
argument_list|)
return|;
block|}
block|}
DECL|method|PropertyBindingSupport ()
specifier|private
name|PropertyBindingSupport
parameter_list|()
block|{     }
DECL|method|build ()
specifier|public
specifier|static
name|Builder
name|build
parameter_list|()
block|{
return|return
operator|new
name|Builder
argument_list|()
return|;
block|}
annotation|@
name|FunctionalInterface
DECL|interface|OnAutowiring
specifier|public
interface|interface
name|OnAutowiring
block|{
comment|/**          * Callback when a property was autowired on a bean          *          * @param target        the targeted bean          * @param propertyName  the name of the property          * @param propertyType  the type of the property          * @param value         the property value          */
DECL|method|onAutowire (Object target, String propertyName, Class propertyType, Object value)
name|void
name|onAutowire
parameter_list|(
name|Object
name|target
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Class
name|propertyType
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
block|}
comment|/**      * This will discover all the properties on the target, and automatic bind the properties that are null by      * looking up in the registry to see if there is a single instance of the same type as the property.      * This is used for convention over configuration to automatic configure resources such as DataSource, Amazon Logins and      * so on.      *      * @param camelContext  the camel context      * @param target        the target object      * @return              true if one ore more properties was auto wired      */
DECL|method|autowireSingletonPropertiesFromRegistry (CamelContext camelContext, Object target)
specifier|public
specifier|static
name|boolean
name|autowireSingletonPropertiesFromRegistry
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|target
parameter_list|)
block|{
return|return
name|autowireSingletonPropertiesFromRegistry
argument_list|(
name|camelContext
argument_list|,
name|target
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * This will discover all the properties on the target, and automatic bind the properties by      * looking up in the registry to see if there is a single instance of the same type as the property.      * This is used for convention over configuration to automatic configure resources such as DataSource, Amazon Logins and      * so on.      *      * @param camelContext  the camel context      * @param target        the target object      * @param bindNullOnly  whether to only autowire if the property has no default value or has not been configured explicit      * @param callback      optional callback when a property was auto wired      * @return              true if one ore more properties was auto wired      */
DECL|method|autowireSingletonPropertiesFromRegistry (CamelContext camelContext, Object target, boolean bindNullOnly, OnAutowiring callback)
specifier|public
specifier|static
name|boolean
name|autowireSingletonPropertiesFromRegistry
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|target
parameter_list|,
name|boolean
name|bindNullOnly
parameter_list|,
name|OnAutowiring
name|callback
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|parents
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
return|return
name|doAutowireSingletonPropertiesFromRegistry
argument_list|(
name|camelContext
argument_list|,
name|target
argument_list|,
name|parents
argument_list|,
name|bindNullOnly
argument_list|,
name|callback
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|PropertyBindingException
argument_list|(
name|target
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|doAutowireSingletonPropertiesFromRegistry (CamelContext camelContext, Object target, Set<Object> parents, boolean bindNullOnly, OnAutowiring callback)
specifier|private
specifier|static
name|boolean
name|doAutowireSingletonPropertiesFromRegistry
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|target
parameter_list|,
name|Set
argument_list|<
name|Object
argument_list|>
name|parents
parameter_list|,
name|boolean
name|bindNullOnly
parameter_list|,
name|OnAutowiring
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
comment|// when adding a component then support auto-configuring complex types
comment|// by looking up from registry, such as DataSource etc
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|target
argument_list|,
name|properties
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|boolean
name|hit
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|getGetterType
argument_list|(
name|target
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|boolean
name|skip
init|=
name|parents
operator|.
name|contains
argument_list|(
name|value
argument_list|)
operator|||
name|value
operator|instanceof
name|CamelContext
decl_stmt|;
if|if
condition|(
name|skip
condition|)
block|{
comment|// we have already covered this as parent of parents so dont walk down this as we want to avoid
comment|// circular dependencies when walking the OGNL graph, also we dont want to walk down CamelContext
continue|continue;
block|}
if|if
condition|(
name|isComplexUserType
argument_list|(
name|type
argument_list|)
condition|)
block|{
comment|// if the property has not been set and its a complex type (not simple or string etc)
if|if
condition|(
operator|!
name|bindNullOnly
operator|||
name|value
operator|==
literal|null
condition|)
block|{
name|Set
name|lookup
init|=
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByType
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|lookup
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|value
operator|=
name|lookup
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|hit
operator||=
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|target
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|hit
operator|&&
name|callback
operator|!=
literal|null
condition|)
block|{
name|callback
operator|.
name|onAutowire
argument_list|(
name|target
argument_list|,
name|key
argument_list|,
name|type
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// TODO: Support creating new instances to walk down the tree if its null (deepNesting option)
comment|// remember this as parent and also autowire nested properties
comment|// do not walk down if it point to our-selves (circular reference)
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|parents
operator|.
name|add
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|hit
operator||=
name|doAutowireSingletonPropertiesFromRegistry
argument_list|(
name|camelContext
argument_list|,
name|value
argument_list|,
name|parents
argument_list|,
name|bindNullOnly
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|hit
return|;
block|}
comment|/**      * Binds the properties to the target object, and removes the property that was bound from properties.      *      * @param camelContext  the camel context      * @param target        the target object      * @param properties    the properties where the bound properties will be removed from      * @return              true if one or more properties was bound      */
DECL|method|bindProperties (CamelContext camelContext, Object target, Map<String, Object> properties)
specifier|public
specifier|static
name|boolean
name|bindProperties
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|target
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
return|return
name|bindProperties
argument_list|(
name|camelContext
argument_list|,
name|target
argument_list|,
name|properties
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Binds the properties to the target object, and removes the property that was bound from properties.      *      * @param camelContext  the camel context      * @param target        the target object      * @param properties    the properties where the bound properties will be removed from      * @param nesting       whether nesting is in use      * @param deepNesting   whether deep nesting is in use, where Camel will attempt to walk as deep as possible by creating new objects in the OGNL graph if      *                      a property has a setter and the object can be created from a default no-arg constructor.      * @param fluentBuilder whether fluent builder is allowed as a valid getter/setter      * @param reference     whether reference parameter (syntax starts with #) is in use      * @param placeholder   whether to use Camels property placeholder to resolve placeholders on keys and values      * @return              true if one or more properties was bound      */
DECL|method|bindProperties (CamelContext camelContext, Object target, Map<String, Object> properties, boolean nesting, boolean deepNesting, boolean fluentBuilder, boolean reference, boolean placeholder)
specifier|public
specifier|static
name|boolean
name|bindProperties
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|target
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|boolean
name|nesting
parameter_list|,
name|boolean
name|deepNesting
parameter_list|,
name|boolean
name|fluentBuilder
parameter_list|,
name|boolean
name|reference
parameter_list|,
name|boolean
name|placeholder
parameter_list|)
block|{
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|target
argument_list|,
literal|"target"
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|properties
argument_list|,
literal|"properties"
argument_list|)
expr_stmt|;
name|boolean
name|rc
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|iter
init|=
name|properties
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|bindProperty
argument_list|(
name|camelContext
argument_list|,
name|target
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|nesting
argument_list|,
name|deepNesting
argument_list|,
name|fluentBuilder
argument_list|,
name|reference
argument_list|,
name|placeholder
argument_list|)
condition|)
block|{
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
name|rc
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|rc
return|;
block|}
comment|/**      * Binds the property to the target object.      *      * @param camelContext  the camel context      * @param target        the target object      * @param name          name of property      * @param value         value of property      * @return              true if property was bound, false otherwise      */
DECL|method|bindProperty (CamelContext camelContext, Object target, String name, Object value)
specifier|public
specifier|static
name|boolean
name|bindProperty
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|target
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|target
operator|!=
literal|null
operator|&&
name|name
operator|!=
literal|null
condition|)
block|{
return|return
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|target
argument_list|,
name|name
argument_list|,
name|value
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|PropertyBindingException
argument_list|(
name|target
argument_list|,
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|bindProperty (CamelContext camelContext, Object target, String name, Object value, boolean nesting, boolean deepNesting, boolean fluentBuilder, boolean reference, boolean placeholder)
specifier|private
specifier|static
name|boolean
name|bindProperty
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|target
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|,
name|boolean
name|nesting
parameter_list|,
name|boolean
name|deepNesting
parameter_list|,
name|boolean
name|fluentBuilder
parameter_list|,
name|boolean
name|reference
parameter_list|,
name|boolean
name|placeholder
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|target
operator|!=
literal|null
operator|&&
name|name
operator|!=
literal|null
condition|)
block|{
return|return
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|target
argument_list|,
name|name
argument_list|,
name|value
argument_list|,
name|nesting
argument_list|,
name|deepNesting
argument_list|,
name|fluentBuilder
argument_list|,
name|reference
argument_list|,
name|placeholder
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|PropertyBindingException
argument_list|(
name|target
argument_list|,
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Binds the mandatory property to the target object (will fail if not set/bound).      *      * @param camelContext  the camel context      * @param target        the target object      * @param name          name of property      * @param value         value of property      */
DECL|method|bindMandatoryProperty (CamelContext camelContext, Object target, String name, Object value)
specifier|public
specifier|static
name|void
name|bindMandatoryProperty
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|target
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|target
operator|!=
literal|null
operator|&&
name|name
operator|!=
literal|null
condition|)
block|{
name|boolean
name|bound
init|=
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|target
argument_list|,
name|name
argument_list|,
name|value
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|bound
condition|)
block|{
throw|throw
operator|new
name|PropertyBindingException
argument_list|(
name|target
argument_list|,
name|name
argument_list|)
throw|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|PropertyBindingException
argument_list|(
name|target
argument_list|,
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|setProperty (CamelContext context, Object target, String name, Object value, boolean nesting, boolean deepNesting, boolean fluentBuilder, boolean reference, boolean placeholder)
specifier|private
specifier|static
name|boolean
name|setProperty
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Object
name|target
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|,
name|boolean
name|nesting
parameter_list|,
name|boolean
name|deepNesting
parameter_list|,
name|boolean
name|fluentBuilder
parameter_list|,
name|boolean
name|reference
parameter_list|,
name|boolean
name|placeholder
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|refName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|placeholder
condition|)
block|{
comment|// resolve property placeholders
name|name
operator|=
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
comment|// resolve property placeholders
name|value
operator|=
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// if name has dot then we need to OGNL walk it
if|if
condition|(
name|nesting
condition|)
block|{
if|if
condition|(
name|name
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|>
literal|0
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|name
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
name|Object
name|newTarget
init|=
name|target
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|newClass
init|=
name|target
operator|.
name|getClass
argument_list|()
decl_stmt|;
comment|// we should only iterate until until 2nd last so we use -1 in the for loop
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|parts
operator|.
name|length
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|String
name|part
init|=
name|parts
index|[
name|i
index|]
decl_stmt|;
name|Object
name|prop
init|=
name|getOrElseProperty
argument_list|(
name|newTarget
argument_list|,
name|part
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|prop
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|deepNesting
condition|)
block|{
comment|// okay we cannot go further down
break|break;
block|}
comment|// okay is there a setter so we can create a new instance and set it automatic
name|Method
name|method
init|=
name|findBestSetterMethod
argument_list|(
name|newClass
argument_list|,
name|part
argument_list|,
name|fluentBuilder
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|parameterType
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|parameterType
operator|!=
literal|null
operator|&&
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|hasDefaultPublicNoArgConstructor
argument_list|(
name|parameterType
argument_list|)
condition|)
block|{
name|Object
name|instance
init|=
name|context
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|parameterType
argument_list|)
decl_stmt|;
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|method
argument_list|,
name|newTarget
argument_list|,
name|instance
argument_list|)
expr_stmt|;
name|newTarget
operator|=
name|instance
expr_stmt|;
name|newClass
operator|=
name|newTarget
operator|.
name|getClass
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|newTarget
operator|=
name|prop
expr_stmt|;
name|newClass
operator|=
name|newTarget
operator|.
name|getClass
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|newTarget
operator|!=
name|target
condition|)
block|{
comment|// okay we found a nested property, then lets change to use that
name|target
operator|=
name|newTarget
expr_stmt|;
name|name
operator|=
name|parts
index|[
name|parts
operator|.
name|length
operator|-
literal|1
index|]
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|reference
operator|&&
name|value
operator|instanceof
name|String
condition|)
block|{
if|if
condition|(
name|value
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"#class:"
argument_list|)
condition|)
block|{
comment|// its a new class to be created
name|String
name|className
init|=
name|value
operator|.
name|toString
argument_list|()
operator|.
name|substring
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|className
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|context
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|value
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"#type:"
argument_list|)
condition|)
block|{
comment|// its reference by type, so lookup the actual value and use it if there is only one instance in the registry
name|String
name|typeName
init|=
name|value
operator|.
name|toString
argument_list|()
operator|.
name|substring
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|typeName
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|?
argument_list|>
name|types
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByType
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|types
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|value
operator|=
name|types
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|value
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
literal|"#autowire"
argument_list|)
condition|)
block|{
comment|// we should get the type from the setter
name|Method
name|method
init|=
name|findBestSetterMethod
argument_list|(
name|target
operator|.
name|getClass
argument_list|()
argument_list|,
name|name
argument_list|,
name|fluentBuilder
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|parameterType
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|parameterType
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|?
argument_list|>
name|types
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByType
argument_list|(
name|parameterType
argument_list|)
decl_stmt|;
if|if
condition|(
name|types
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|value
operator|=
name|types
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|value
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"#bean:"
argument_list|)
condition|)
block|{
comment|// okay its a reference so swap to lookup this which is already supported in IntrospectionSupport
name|refName
operator|=
operator|(
operator|(
name|String
operator|)
name|value
operator|)
operator|.
name|substring
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|value
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|context
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|target
argument_list|,
name|name
argument_list|,
name|value
argument_list|,
name|refName
argument_list|,
name|fluentBuilder
argument_list|)
return|;
block|}
DECL|method|findBestSetterMethod (Class clazz, String name, boolean fluentBuilder)
specifier|private
specifier|static
name|Method
name|findBestSetterMethod
parameter_list|(
name|Class
name|clazz
parameter_list|,
name|String
name|name
parameter_list|,
name|boolean
name|fluentBuilder
parameter_list|)
block|{
comment|// is there a direct setter?
name|Set
argument_list|<
name|Method
argument_list|>
name|candidates
init|=
name|findSetterMethods
argument_list|(
name|clazz
argument_list|,
name|name
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|candidates
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|candidates
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
comment|// okay now try with builder pattern
if|if
condition|(
name|fluentBuilder
condition|)
block|{
name|candidates
operator|=
name|findSetterMethods
argument_list|(
name|clazz
argument_list|,
name|name
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|candidates
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|candidates
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getGetterType (Object target, String name)
specifier|private
specifier|static
name|Class
name|getGetterType
parameter_list|(
name|Object
name|target
parameter_list|,
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|Method
name|getter
init|=
name|IntrospectionSupport
operator|.
name|getPropertyGetter
argument_list|(
name|target
operator|.
name|getClass
argument_list|()
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|getter
operator|!=
literal|null
condition|)
block|{
return|return
name|getter
operator|.
name|getReturnType
argument_list|()
return|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|null
return|;
block|}
DECL|method|isComplexUserType (Class type)
specifier|private
specifier|static
name|boolean
name|isComplexUserType
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
comment|// lets consider all non java, as complex types
return|return
name|type
operator|!=
literal|null
operator|&&
operator|!
name|type
operator|.
name|isPrimitive
argument_list|()
operator|&&
operator|!
name|type
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"java"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

