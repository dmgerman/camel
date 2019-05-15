begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
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
name|Type
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

begin_class
DECL|class|AnnotatedDecorator
class|class
name|AnnotatedDecorator
implements|implements
name|Annotated
block|{
DECL|field|decorated
specifier|private
specifier|final
name|Annotated
name|decorated
decl_stmt|;
DECL|field|annotations
specifier|private
specifier|final
name|Set
argument_list|<
name|Annotation
argument_list|>
name|annotations
decl_stmt|;
DECL|method|AnnotatedDecorator (Annotated decorated, Set<Annotation> annotations)
name|AnnotatedDecorator
parameter_list|(
name|Annotated
name|decorated
parameter_list|,
name|Set
argument_list|<
name|Annotation
argument_list|>
name|annotations
parameter_list|)
block|{
name|this
operator|.
name|decorated
operator|=
name|decorated
expr_stmt|;
name|this
operator|.
name|annotations
operator|=
name|annotations
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getBaseType ()
specifier|public
name|Type
name|getBaseType
parameter_list|()
block|{
return|return
name|decorated
operator|.
name|getBaseType
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getTypeClosure ()
specifier|public
name|Set
argument_list|<
name|Type
argument_list|>
name|getTypeClosure
parameter_list|()
block|{
return|return
name|decorated
operator|.
name|getTypeClosure
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getAnnotation (Class<T> annotationType)
specifier|public
parameter_list|<
name|T
extends|extends
name|Annotation
parameter_list|>
name|T
name|getAnnotation
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|annotationType
parameter_list|)
block|{
name|T
name|annotation
init|=
name|getDecoratingAnnotation
argument_list|(
name|annotationType
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
block|{
return|return
name|annotation
return|;
block|}
else|else
block|{
return|return
name|decorated
operator|.
name|getAnnotation
argument_list|(
name|annotationType
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getAnnotations ()
specifier|public
name|Set
argument_list|<
name|Annotation
argument_list|>
name|getAnnotations
parameter_list|()
block|{
name|Set
argument_list|<
name|Annotation
argument_list|>
name|annotations
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|this
operator|.
name|annotations
argument_list|)
decl_stmt|;
name|annotations
operator|.
name|addAll
argument_list|(
name|decorated
operator|.
name|getAnnotations
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|annotations
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isAnnotationPresent (Class<? extends Annotation> annotationType)
specifier|public
name|boolean
name|isAnnotationPresent
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotationType
parameter_list|)
block|{
return|return
name|getDecoratingAnnotation
argument_list|(
name|annotationType
argument_list|)
operator|!=
literal|null
operator|||
name|decorated
operator|.
name|isAnnotationPresent
argument_list|(
name|annotationType
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getDecoratingAnnotation (Class<T> annotationType)
specifier|private
parameter_list|<
name|T
extends|extends
name|Annotation
parameter_list|>
name|T
name|getDecoratingAnnotation
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|annotationType
parameter_list|)
block|{
for|for
control|(
name|Annotation
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|annotationType
operator|.
name|isAssignableFrom
argument_list|(
name|annotation
operator|.
name|annotationType
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|annotation
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getAnnotations (Class<T> annotationType)
specifier|public
parameter_list|<
name|T
extends|extends
name|Annotation
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|getAnnotations
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|annotationType
parameter_list|)
block|{
return|return
name|decorated
operator|.
name|getAnnotations
argument_list|(
name|annotationType
argument_list|)
return|;
block|}
block|}
end_class

end_unit

