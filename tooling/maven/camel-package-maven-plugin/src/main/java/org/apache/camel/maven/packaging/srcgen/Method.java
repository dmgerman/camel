begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven.packaging.srcgen
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|packaging
operator|.
name|srcgen
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
name|Type
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_class
DECL|class|Method
specifier|public
class|class
name|Method
block|{
DECL|field|name
name|String
name|name
decl_stmt|;
DECL|field|returnType
name|GenericType
name|returnType
decl_stmt|;
DECL|field|isDefault
name|boolean
name|isDefault
decl_stmt|;
DECL|field|isPublic
name|boolean
name|isPublic
decl_stmt|;
DECL|field|isConstructor
name|boolean
name|isConstructor
decl_stmt|;
DECL|field|body
name|String
name|body
decl_stmt|;
DECL|field|parameters
name|List
argument_list|<
name|Param
argument_list|>
name|parameters
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|exceptions
name|List
argument_list|<
name|GenericType
argument_list|>
name|exceptions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|annotations
name|List
argument_list|<
name|Annotation
argument_list|>
name|annotations
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|javadoc
name|Javadoc
name|javadoc
init|=
operator|new
name|Javadoc
argument_list|()
decl_stmt|;
DECL|method|setPublic ()
specifier|public
name|Method
name|setPublic
parameter_list|()
block|{
name|isPublic
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setDefault ()
specifier|public
name|Method
name|setDefault
parameter_list|()
block|{
name|isDefault
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|Method
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getReturnType ()
specifier|public
name|GenericType
name|getReturnType
parameter_list|()
block|{
return|return
name|returnType
return|;
block|}
DECL|method|setReturnType (Type returnType)
specifier|public
name|Method
name|setReturnType
parameter_list|(
name|Type
name|returnType
parameter_list|)
block|{
return|return
name|setReturnType
argument_list|(
operator|new
name|GenericType
argument_list|(
name|returnType
argument_list|)
argument_list|)
return|;
block|}
DECL|method|setReturnType (GenericType returnType)
specifier|public
name|Method
name|setReturnType
parameter_list|(
name|GenericType
name|returnType
parameter_list|)
block|{
name|this
operator|.
name|returnType
operator|=
name|returnType
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|addParameter (Class<?> type, String name)
specifier|public
name|Method
name|addParameter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|addParameter
argument_list|(
operator|new
name|GenericType
argument_list|(
name|type
argument_list|)
argument_list|,
name|name
argument_list|)
return|;
block|}
DECL|method|addParameter (GenericType type, String name)
specifier|public
name|Method
name|addParameter
parameter_list|(
name|GenericType
name|type
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|parameters
operator|.
name|add
argument_list|(
operator|new
name|Param
argument_list|(
name|type
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getBody ()
specifier|public
name|String
name|getBody
parameter_list|()
block|{
return|return
name|body
return|;
block|}
DECL|method|setBody (String body)
specifier|public
name|Method
name|setBody
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|addThrows (Class<?> type)
specifier|public
name|Method
name|addThrows
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|addThrows
argument_list|(
operator|new
name|GenericType
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
DECL|method|addThrows (GenericType type)
specifier|public
name|Method
name|addThrows
parameter_list|(
name|GenericType
name|type
parameter_list|)
block|{
name|this
operator|.
name|exceptions
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|addAnnotation (Class<? extends java.lang.annotation.Annotation> clazz)
specifier|public
name|Annotation
name|addAnnotation
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
argument_list|>
name|clazz
parameter_list|)
block|{
name|Annotation
name|ann
init|=
operator|new
name|Annotation
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|this
operator|.
name|annotations
operator|.
name|add
argument_list|(
name|ann
argument_list|)
expr_stmt|;
return|return
name|ann
return|;
block|}
DECL|method|hasAnnotation (Class<? extends java.lang.annotation.Annotation> clazz)
specifier|public
name|boolean
name|hasAnnotation
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|annotations
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Annotation
operator|::
name|getType
argument_list|)
operator|.
name|anyMatch
argument_list|(
name|clazz
operator|::
name|equals
argument_list|)
return|;
block|}
DECL|method|getAnnotation (Class<? extends java.lang.annotation.Annotation> clazz)
specifier|public
name|Annotation
name|getAnnotation
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
argument_list|>
name|clazz
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
name|a
lambda|->
name|Objects
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getType
argument_list|()
argument_list|,
name|clazz
argument_list|)
argument_list|)
operator|.
name|findAny
argument_list|()
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
return|;
block|}
DECL|method|getJavaDoc ()
specifier|public
name|Javadoc
name|getJavaDoc
parameter_list|()
block|{
return|return
name|javadoc
return|;
block|}
DECL|method|setConstructor (boolean cns)
specifier|public
name|Method
name|setConstructor
parameter_list|(
name|boolean
name|cns
parameter_list|)
block|{
name|this
operator|.
name|isConstructor
operator|=
name|cns
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|hasJavaDoc ()
specifier|public
name|boolean
name|hasJavaDoc
parameter_list|()
block|{
return|return
name|javadoc
operator|.
name|text
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

