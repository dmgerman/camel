begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|Array
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
name|GenericArrayType
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
name|ParameterizedType
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
name|Type
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
name|TypeVariable
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
name|WildcardType
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
name|HashSet
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
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|Annotated
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|AnnotatedConstructor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|AnnotatedField
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|AnnotatedMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|AnnotatedType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|BeanManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|InjectionPoint
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

begin_class
annotation|@
name|Vetoed
DECL|class|CdiSpiHelper
specifier|final
class|class
name|CdiSpiHelper
block|{
DECL|method|CdiSpiHelper ()
specifier|private
name|CdiSpiHelper
parameter_list|()
block|{     }
DECL|method|getQualifierByType (InjectionPoint ip, Class<T> type)
specifier|static
parameter_list|<
name|T
extends|extends
name|Annotation
parameter_list|>
name|T
name|getQualifierByType
parameter_list|(
name|InjectionPoint
name|ip
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|getFirstElementOfType
argument_list|(
name|ip
operator|.
name|getQualifiers
argument_list|()
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|getFirstElementOfType (Collection<E> collection, Class<T> type)
specifier|static
parameter_list|<
name|E
parameter_list|,
name|T
extends|extends
name|E
parameter_list|>
name|T
name|getFirstElementOfType
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|collection
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
for|for
control|(
name|E
name|item
range|:
name|collection
control|)
block|{
if|if
condition|(
operator|(
name|item
operator|!=
literal|null
operator|)
operator|&&
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|item
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|type
argument_list|,
name|item
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|SafeVarargs
DECL|method|excludeElementOfTypes (Set<T> annotations, Class<? extends T>... exclusions)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|excludeElementOfTypes
parameter_list|(
name|Set
argument_list|<
name|T
argument_list|>
name|annotations
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
modifier|...
name|exclusions
parameter_list|)
block|{
name|Set
argument_list|<
name|T
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|T
name|annotation
range|:
name|annotations
control|)
block|{
name|boolean
name|exclude
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|exclusion
range|:
name|exclusions
control|)
block|{
if|if
condition|(
name|exclusion
operator|.
name|isAssignableFrom
argument_list|(
name|annotation
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|exclude
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|exclude
condition|)
block|{
name|set
operator|.
name|add
argument_list|(
name|annotation
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|set
return|;
block|}
DECL|method|getRawType (Type type)
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|getRawType
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|instanceof
name|Class
argument_list|<
name|?
argument_list|>
condition|)
block|{
return|return
name|Class
operator|.
name|class
operator|.
name|cast
argument_list|(
name|type
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
return|return
name|getRawType
argument_list|(
name|ParameterizedType
operator|.
name|class
operator|.
name|cast
argument_list|(
name|type
argument_list|)
operator|.
name|getRawType
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|instanceof
name|TypeVariable
argument_list|<
name|?
argument_list|>
condition|)
block|{
return|return
name|getBound
argument_list|(
name|TypeVariable
operator|.
name|class
operator|.
name|cast
argument_list|(
name|type
argument_list|)
operator|.
name|getBounds
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|instanceof
name|WildcardType
condition|)
block|{
return|return
name|getBound
argument_list|(
name|WildcardType
operator|.
name|class
operator|.
name|cast
argument_list|(
name|type
argument_list|)
operator|.
name|getUpperBounds
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|instanceof
name|GenericArrayType
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|rawType
init|=
name|getRawType
argument_list|(
name|GenericArrayType
operator|.
name|class
operator|.
name|cast
argument_list|(
name|type
argument_list|)
operator|.
name|getGenericComponentType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|rawType
operator|!=
literal|null
condition|)
block|{
return|return
name|Array
operator|.
name|newInstance
argument_list|(
name|rawType
argument_list|,
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
return|;
block|}
block|}
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Unable to retrieve raw type for ["
operator|+
name|type
operator|+
literal|"]"
argument_list|)
throw|;
block|}
DECL|method|getBound (Type[] bounds)
specifier|private
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|getBound
parameter_list|(
name|Type
index|[]
name|bounds
parameter_list|)
block|{
if|if
condition|(
name|bounds
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|Object
operator|.
name|class
return|;
block|}
else|else
block|{
return|return
name|getRawType
argument_list|(
name|bounds
index|[
literal|0
index|]
argument_list|)
return|;
block|}
block|}
annotation|@
name|SafeVarargs
DECL|method|hasAnnotation (AnnotatedType<?> type, Class<? extends Annotation>... annotations)
specifier|static
name|boolean
name|hasAnnotation
parameter_list|(
name|AnnotatedType
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
modifier|...
name|annotations
parameter_list|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|hasAnnotation
argument_list|(
name|type
argument_list|,
name|annotation
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|hasAnnotation (AnnotatedType<?> type, Class<? extends Annotation> annotation)
specifier|static
name|boolean
name|hasAnnotation
parameter_list|(
name|AnnotatedType
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotation
parameter_list|)
block|{
if|if
condition|(
name|type
operator|.
name|isAnnotationPresent
argument_list|(
name|annotation
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
for|for
control|(
name|AnnotatedMethod
argument_list|<
name|?
argument_list|>
name|method
range|:
name|type
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|method
operator|.
name|isAnnotationPresent
argument_list|(
name|annotation
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
for|for
control|(
name|AnnotatedConstructor
argument_list|<
name|?
argument_list|>
name|constructor
range|:
name|type
operator|.
name|getConstructors
argument_list|()
control|)
block|{
if|if
condition|(
name|constructor
operator|.
name|isAnnotationPresent
argument_list|(
name|annotation
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
for|for
control|(
name|AnnotatedField
argument_list|<
name|?
argument_list|>
name|field
range|:
name|type
operator|.
name|getFields
argument_list|()
control|)
block|{
if|if
condition|(
name|field
operator|.
name|isAnnotationPresent
argument_list|(
name|annotation
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|getQualifiers (Annotated annotated, BeanManager manager)
specifier|static
name|Set
argument_list|<
name|Annotation
argument_list|>
name|getQualifiers
parameter_list|(
name|Annotated
name|annotated
parameter_list|,
name|BeanManager
name|manager
parameter_list|)
block|{
name|Set
argument_list|<
name|Annotation
argument_list|>
name|qualifiers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Annotation
name|annotation
range|:
name|annotated
operator|.
name|getAnnotations
argument_list|()
control|)
block|{
if|if
condition|(
name|manager
operator|.
name|isQualifier
argument_list|(
name|annotation
operator|.
name|annotationType
argument_list|()
argument_list|)
condition|)
block|{
name|qualifiers
operator|.
name|add
argument_list|(
name|annotation
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|qualifiers
return|;
block|}
block|}
end_class

end_unit

