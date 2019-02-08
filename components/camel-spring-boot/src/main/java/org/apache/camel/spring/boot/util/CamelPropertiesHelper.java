begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.util
package|package
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
name|util
package|;
end_package

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
name|Iterator
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
name|support
operator|.
name|CamelContextHelper
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
name|EndpointHelper
operator|.
name|isReferenceParameter
import|;
end_import

begin_comment
comment|/**  * To help configuring Camel properties that have been defined in Spring Boot configuration files.  */
end_comment

begin_class
DECL|class|CamelPropertiesHelper
specifier|public
specifier|final
class|class
name|CamelPropertiesHelper
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
name|CamelContextHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|CamelPropertiesHelper ()
specifier|private
name|CamelPropertiesHelper
parameter_list|()
block|{     }
comment|/**      * Sets the properties on the target bean.      *<p/>      * This implementation sets the properties using the following algorithm:      *<ul>      *<li>Value as reference lookup - If the value uses Camel reference syntax, eg #beanId then the bean is looked up from Registry and set on the target</li>      *<li>Value as-is - The value is attempted to be converted to the class type of the bean setter method; this is for regular types like String, numbers etc</li>      *<li>Value as lookup - the bean is looked up from Registry and if there is a bean then its set on the target</li>      *</ul>      * When an option has been set on the target bean, then its removed from the given properties map. If all the options has been set, then the map will be empty.      *      * @param context        the CamelContext      * @param target         the target bean      * @param properties     the properties      * @param failIfNotSet   whether to fail if an option either does not exists on the target bean or if the option cannot be due no suitable setter methods with the given type      * @return<tt>true</tt> if at least one option was configured      * @throws IllegalArgumentException is thrown if an option cannot be configured on the bean because there is no suitable setter method and failOnNoSet is true.      * @throws Exception for any other kind of error      */
DECL|method|setCamelProperties (CamelContext context, Object target, Map<String, Object> properties, boolean failIfNotSet)
specifier|public
specifier|static
name|boolean
name|setCamelProperties
parameter_list|(
name|CamelContext
name|context
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
name|failIfNotSet
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|context
argument_list|,
literal|"context"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|target
argument_list|,
literal|"target"
argument_list|)
expr_stmt|;
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
name|it
init|=
name|properties
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
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
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|name
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
name|String
name|stringValue
init|=
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|toString
argument_list|()
else|:
literal|null
decl_stmt|;
name|boolean
name|hit
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|stringValue
operator|!=
literal|null
operator|&&
name|isReferenceParameter
argument_list|(
name|stringValue
argument_list|)
condition|)
block|{
comment|// its a #beanId reference lookup
name|hit
operator|=
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
literal|null
argument_list|,
name|stringValue
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
comment|// its a value to be used as-is (or type converted)
try|try
block|{
name|hit
operator|=
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
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// no we could not and this would be thrown if we attempted to set a value on a property which we cannot do type conversion as
comment|// then maybe the value refers to a spring bean in the registry so try this
name|hit
operator|=
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
literal|null
argument_list|,
name|stringValue
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|hit
condition|)
block|{
comment|// must remove as its a valid option and we could configure it
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
name|rc
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|failIfNotSet
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot configure option ["
operator|+
name|name
operator|+
literal|"] with value ["
operator|+
name|stringValue
operator|+
literal|"] as the bean class ["
operator|+
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|target
argument_list|)
operator|+
literal|"] has no suitable setter method, or not possible to lookup a bean with the id ["
operator|+
name|stringValue
operator|+
literal|"] in Spring Boot registry"
argument_list|)
throw|;
block|}
block|}
return|return
name|rc
return|;
block|}
comment|/**      * Inspects the given object and resolves any property placeholders from its properties.      *<p/>      * This implementation will check all the getter/setter pairs on this instance and for all the values      * (which is a String type) will be property placeholder resolved.      *      * @param camelContext the Camel context      * @param target       the object that should have the properties (eg getter/setter) resolved      * @throws Exception is thrown if property placeholders was used and there was an error resolving them      * @see CamelContext#resolvePropertyPlaceholders(String)      * @see org.apache.camel.component.properties.PropertiesComponent      */
DECL|method|resolvePropertyPlaceholders (CamelContext camelContext, Object target)
specifier|public
specifier|static
name|void
name|resolvePropertyPlaceholders
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|target
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Resolving property placeholders for: {}"
argument_list|,
name|target
argument_list|)
expr_stmt|;
comment|// find all getter/setter which we can use for property placeholders
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|changedProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|properties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"There are {} properties on: {}"
argument_list|,
name|properties
operator|.
name|size
argument_list|()
argument_list|,
name|target
argument_list|)
expr_stmt|;
comment|// lookup and resolve properties for String based properties
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
comment|// the name is always a String
name|String
name|name
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
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
comment|// value must be a String, as a String is the key for a property placeholder
name|String
name|text
init|=
operator|(
name|String
operator|)
name|value
decl_stmt|;
name|text
operator|=
name|camelContext
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|text
argument_list|)
expr_stmt|;
if|if
condition|(
name|text
operator|!=
name|value
condition|)
block|{
comment|// invoke setter as the text has changed
name|boolean
name|changed
init|=
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|target
argument_list|,
name|name
argument_list|,
name|text
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|changed
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No setter to set property: "
operator|+
name|name
operator|+
literal|" to: "
operator|+
name|text
operator|+
literal|" on: "
operator|+
name|target
argument_list|)
throw|;
block|}
name|changedProperties
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
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
literal|"Changed property [{}] from: {} to: {}"
argument_list|,
name|name
argument_list|,
name|value
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

