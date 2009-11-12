begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.scan
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|scan
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
name|spi
operator|.
name|PackageScanFilter
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

begin_comment
comment|/**  * Package scan filter for testing if a given class is annotated with any of the annotations.  */
end_comment

begin_class
DECL|class|AnnotatedWithAnyPackageScanFilter
specifier|public
class|class
name|AnnotatedWithAnyPackageScanFilter
implements|implements
name|PackageScanFilter
block|{
DECL|field|annotations
specifier|private
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotations
decl_stmt|;
DECL|field|checkMetaAnnotations
specifier|private
name|boolean
name|checkMetaAnnotations
decl_stmt|;
DECL|method|AnnotatedWithAnyPackageScanFilter (Set<Class<? extends Annotation>> annotations)
specifier|public
name|AnnotatedWithAnyPackageScanFilter
parameter_list|(
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotations
parameter_list|)
block|{
name|this
argument_list|(
name|annotations
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|AnnotatedWithAnyPackageScanFilter (Set<Class<? extends Annotation>> annotations, boolean checkMetaAnnotations)
specifier|public
name|AnnotatedWithAnyPackageScanFilter
parameter_list|(
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotations
parameter_list|,
name|boolean
name|checkMetaAnnotations
parameter_list|)
block|{
name|this
operator|.
name|annotations
operator|=
name|annotations
expr_stmt|;
name|this
operator|.
name|checkMetaAnnotations
operator|=
name|checkMetaAnnotations
expr_stmt|;
block|}
DECL|method|matches (Class<?> type)
specifier|public
name|boolean
name|matches
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
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
name|ObjectHelper
operator|.
name|hasAnnotation
argument_list|(
name|type
argument_list|,
name|annotation
argument_list|,
name|checkMetaAnnotations
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"annotated with any @["
operator|+
name|annotations
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

