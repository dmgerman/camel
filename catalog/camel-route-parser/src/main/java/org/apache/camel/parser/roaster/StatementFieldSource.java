begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.parser.roaster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
operator|.
name|roaster
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|JavaType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|Type
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|Visibility
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|impl
operator|.
name|TypeImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|source
operator|.
name|AnnotationSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|source
operator|.
name|FieldSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|source
operator|.
name|JavaClassSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|source
operator|.
name|JavaDocSource
import|;
end_import

begin_class
DECL|class|StatementFieldSource
specifier|public
class|class
name|StatementFieldSource
implements|implements
name|FieldSource
block|{
comment|// this implementation should only implement the needed logic to support the parser
DECL|field|origin
specifier|private
specifier|final
name|JavaClassSource
name|origin
decl_stmt|;
DECL|field|internal
specifier|private
specifier|final
name|Object
name|internal
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Type
name|type
decl_stmt|;
DECL|method|StatementFieldSource (JavaClassSource origin, Object internal, Object typeInternal)
specifier|public
name|StatementFieldSource
parameter_list|(
name|JavaClassSource
name|origin
parameter_list|,
name|Object
name|internal
parameter_list|,
name|Object
name|typeInternal
parameter_list|)
block|{
name|this
operator|.
name|origin
operator|=
name|origin
expr_stmt|;
name|this
operator|.
name|internal
operator|=
name|internal
expr_stmt|;
name|this
operator|.
name|type
operator|=
operator|new
name|TypeImpl
argument_list|(
name|origin
argument_list|,
name|typeInternal
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setType (Class clazz)
specifier|public
name|FieldSource
name|setType
parameter_list|(
name|Class
name|clazz
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setType (String type)
specifier|public
name|FieldSource
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setLiteralInitializer (String value)
specifier|public
name|FieldSource
name|setLiteralInitializer
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setStringInitializer (String value)
specifier|public
name|FieldSource
name|setStringInitializer
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setTransient (boolean value)
specifier|public
name|FieldSource
name|setTransient
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setVolatile (boolean value)
specifier|public
name|FieldSource
name|setVolatile
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setType (JavaType entity)
specifier|public
name|FieldSource
name|setType
parameter_list|(
name|JavaType
name|entity
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getAnnotations ()
specifier|public
name|List
argument_list|<
name|AnnotationSource
argument_list|>
name|getAnnotations
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|hasAnnotation (String type)
specifier|public
name|boolean
name|hasAnnotation
parameter_list|(
name|String
name|type
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hasAnnotation (Class type)
specifier|public
name|boolean
name|hasAnnotation
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|getAnnotation (String type)
specifier|public
name|AnnotationSource
name|getAnnotation
parameter_list|(
name|String
name|type
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|addAnnotation ()
specifier|public
name|AnnotationSource
name|addAnnotation
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|addAnnotation (String className)
specifier|public
name|AnnotationSource
name|addAnnotation
parameter_list|(
name|String
name|className
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|removeAllAnnotations ()
specifier|public
name|void
name|removeAllAnnotations
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|removeAnnotation (Annotation annotation)
specifier|public
name|Object
name|removeAnnotation
parameter_list|(
name|Annotation
name|annotation
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|addAnnotation (Class type)
specifier|public
name|AnnotationSource
name|addAnnotation
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getAnnotation (Class type)
specifier|public
name|AnnotationSource
name|getAnnotation
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getType ()
specifier|public
name|Type
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
annotation|@
name|Override
DECL|method|getStringInitializer ()
specifier|public
name|String
name|getStringInitializer
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getLiteralInitializer ()
specifier|public
name|String
name|getLiteralInitializer
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|isTransient ()
specifier|public
name|boolean
name|isTransient
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|isVolatile ()
specifier|public
name|boolean
name|isVolatile
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|setFinal (boolean finl)
specifier|public
name|Object
name|setFinal
parameter_list|(
name|boolean
name|finl
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|isFinal ()
specifier|public
name|boolean
name|isFinal
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|getInternal ()
specifier|public
name|Object
name|getInternal
parameter_list|()
block|{
return|return
name|internal
return|;
block|}
annotation|@
name|Override
DECL|method|getJavaDoc ()
specifier|public
name|JavaDocSource
name|getJavaDoc
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|hasJavaDoc ()
specifier|public
name|boolean
name|hasJavaDoc
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|removeJavaDoc ()
specifier|public
name|Object
name|removeJavaDoc
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setName (String name)
specifier|public
name|Object
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getOrigin ()
specifier|public
name|Object
name|getOrigin
parameter_list|()
block|{
return|return
name|origin
return|;
block|}
annotation|@
name|Override
DECL|method|setStatic (boolean value)
specifier|public
name|Object
name|setStatic
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|isStatic ()
specifier|public
name|boolean
name|isStatic
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|setPackagePrivate ()
specifier|public
name|Object
name|setPackagePrivate
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setPublic ()
specifier|public
name|Object
name|setPublic
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setPrivate ()
specifier|public
name|Object
name|setPrivate
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setProtected ()
specifier|public
name|Object
name|setProtected
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setVisibility (Visibility scope)
specifier|public
name|Object
name|setVisibility
parameter_list|(
name|Visibility
name|scope
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|isPackagePrivate ()
specifier|public
name|boolean
name|isPackagePrivate
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|isPublic ()
specifier|public
name|boolean
name|isPublic
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|isPrivate ()
specifier|public
name|boolean
name|isPrivate
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|isProtected ()
specifier|public
name|boolean
name|isProtected
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|getVisibility ()
specifier|public
name|Visibility
name|getVisibility
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getColumnNumber ()
specifier|public
name|int
name|getColumnNumber
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|getStartPosition ()
specifier|public
name|int
name|getStartPosition
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|getEndPosition ()
specifier|public
name|int
name|getEndPosition
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|getLineNumber ()
specifier|public
name|int
name|getLineNumber
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit
