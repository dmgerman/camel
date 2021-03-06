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
name|Type
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
name|AnnotatedCallable
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
name|AnnotatedParameter
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

begin_class
DECL|class|FrameworkAnnotatedParameter
specifier|final
class|class
name|FrameworkAnnotatedParameter
parameter_list|<
name|X
parameter_list|>
implements|implements
name|AnnotatedParameter
argument_list|<
name|X
argument_list|>
block|{
DECL|field|position
specifier|private
specifier|final
name|int
name|position
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Type
name|type
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
DECL|field|manager
specifier|private
specifier|final
name|BeanManager
name|manager
decl_stmt|;
DECL|method|FrameworkAnnotatedParameter (Method method, int position, BeanManager manager)
name|FrameworkAnnotatedParameter
parameter_list|(
name|Method
name|method
parameter_list|,
name|int
name|position
parameter_list|,
name|BeanManager
name|manager
parameter_list|)
block|{
name|this
operator|.
name|position
operator|=
name|position
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|method
operator|.
name|getGenericParameterTypes
argument_list|()
index|[
name|position
index|]
expr_stmt|;
name|this
operator|.
name|annotations
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|method
operator|.
name|getParameterAnnotations
argument_list|()
index|[
name|position
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|manager
operator|=
name|manager
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDeclaringCallable ()
specifier|public
name|AnnotatedCallable
argument_list|<
name|X
argument_list|>
name|getDeclaringCallable
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getPosition ()
specifier|public
name|int
name|getPosition
parameter_list|()
block|{
return|return
name|position
return|;
block|}
annotation|@
name|Override
DECL|method|getAnnotation (Class<Y> type)
specifier|public
parameter_list|<
name|Y
extends|extends
name|Annotation
parameter_list|>
name|Y
name|getAnnotation
parameter_list|(
name|Class
argument_list|<
name|Y
argument_list|>
name|type
parameter_list|)
block|{
for|for
control|(
name|Annotation
name|annotation
range|:
name|getAnnotations
argument_list|()
control|)
block|{
if|if
condition|(
name|annotation
operator|.
name|annotationType
argument_list|()
operator|==
name|type
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|annotation
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
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
DECL|method|getBaseType ()
specifier|public
name|Type
name|getBaseType
parameter_list|()
block|{
return|return
name|type
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
if|if
condition|(
name|type
operator|instanceof
name|Class
condition|)
block|{
return|return
name|manager
operator|.
name|createAnnotatedType
argument_list|(
operator|(
name|Class
operator|)
name|type
argument_list|)
operator|.
name|getTypeClosure
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|type
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|isAnnotationPresent (Class<? extends Annotation> type)
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
name|type
parameter_list|)
block|{
return|return
name|getAnnotation
argument_list|(
name|type
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

