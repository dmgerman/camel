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
name|Collection
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|TypeConverter
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
comment|/**  * A convenient support class for binding String valued properties to an instance which  * uses a set of conventions:  *<ul>  *<li>nested - Properties can be nested using the dot syntax (OGNL), eg foo.bar=123</li>  *<li>reference by id - Values can refer to other beans in the registry by prefixing with # syntax, eg #myBean</li>  *</ul>  * This implementations reuses parts of {@link IntrospectionSupport}.  */
end_comment

begin_class
DECL|class|PropertyBindingSupport
specifier|public
specifier|final
class|class
name|PropertyBindingSupport
block|{
comment|// TODO: Add support for auto binding to singleton instance by type from registry (boolean on|off)
comment|// TODO: builder pattern with naming prefix: withXXX
DECL|field|SECRETS
specifier|private
specifier|static
specifier|final
name|Pattern
name|SECRETS
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|".*(passphrase|password|secretKey).*"
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
decl_stmt|;
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
name|PropertyBindingSupport
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|PropertyBindingSupport ()
specifier|private
name|PropertyBindingSupport
parameter_list|()
block|{     }
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
throws|throws
name|Exception
block|{
name|boolean
name|answer
init|=
literal|true
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
name|answer
operator|&=
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
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
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
throws|throws
name|Exception
block|{
return|return
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|camelContext
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
literal|null
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * This method supports two modes to set a property:      *      * 1. Setting a property that has already been resolved, this is the case when {@code context} and {@code refName} are      * NULL and {@code value} is non-NULL.      *      * 2. Setting a property that has not yet been resolved, the property will be resolved based on the suitable methods      * found matching the property name on the {@code target} bean. For this mode to be triggered the parameters      * {@code context} and {@code refName} must NOT be NULL, and {@code value} MUST be NULL.      */
DECL|method|setProperty (CamelContext context, TypeConverter typeConverter, Object target, String name, Object value, String refName, boolean allowBuilderPattern, boolean allowNestedProperties)
specifier|private
specifier|static
name|boolean
name|setProperty
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|TypeConverter
name|typeConverter
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
name|String
name|refName
parameter_list|,
name|boolean
name|allowBuilderPattern
parameter_list|,
name|boolean
name|allowNestedProperties
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|target
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Method
argument_list|>
name|setters
decl_stmt|;
comment|// if name has dot then we need to OGNL walk it
if|if
condition|(
name|allowNestedProperties
operator|&&
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
name|clazz
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
comment|// okay is there a setter so we can create a new instance and set it automatic
name|Set
argument_list|<
name|Method
argument_list|>
name|newSetters
init|=
name|findSetterMethods
argument_list|(
name|newClass
argument_list|,
name|part
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|newSetters
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|Method
name|method
init|=
name|newSetters
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
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
comment|// okay we found a nested property, then lets change to use that
name|target
operator|=
name|newTarget
expr_stmt|;
name|clazz
operator|=
name|newTarget
operator|.
name|getClass
argument_list|()
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
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
if|if
condition|(
name|EndpointHelper
operator|.
name|isReferenceParameter
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
comment|// okay its a reference so swap to lookup this
name|refName
operator|=
name|value
operator|.
name|toString
argument_list|()
expr_stmt|;
name|value
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
comment|// TODO: At this point we can likely just call IntrospectionSupport directly
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
literal|true
argument_list|)
return|;
block|}
block|}
end_class

end_unit

