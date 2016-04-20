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
name|Type
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|CdiSpiHelper
operator|.
name|isAnnotationType
import|;
end_import

begin_class
DECL|class|AnnotatedDelegate
class|class
name|AnnotatedDelegate
implements|implements
name|Annotated
block|{
DECL|field|delegate
specifier|private
specifier|final
name|Annotated
name|delegate
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
DECL|method|AnnotatedDelegate (Annotated delegate, Set<Annotation> annotations)
name|AnnotatedDelegate
parameter_list|(
name|Annotated
name|delegate
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
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|annotations
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|annotations
argument_list|)
expr_stmt|;
block|}
DECL|method|AnnotatedDelegate (Annotated delegate)
name|AnnotatedDelegate
parameter_list|(
name|Annotated
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|annotations
operator|=
name|delegate
operator|.
name|getAnnotations
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getAnnotation (Class<T> type)
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
name|type
parameter_list|)
block|{
return|return
name|annotations
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|isAnnotationType
argument_list|(
name|type
argument_list|)
argument_list|)
operator|.
name|findAny
argument_list|()
operator|.
name|map
argument_list|(
name|type
operator|::
name|cast
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
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
name|annotations
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
name|delegate
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
name|delegate
operator|.
name|getTypeClosure
argument_list|()
return|;
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
name|annotations
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|isAnnotationType
argument_list|(
name|type
argument_list|)
argument_list|)
operator|.
name|findAny
argument_list|()
operator|.
name|isPresent
argument_list|()
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
name|delegate
operator|.
name|toString
argument_list|()
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
name|delegate
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|equals
argument_list|(
name|object
argument_list|)
return|;
block|}
block|}
end_class

end_unit

