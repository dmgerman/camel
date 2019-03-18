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

begin_class
DECL|class|Property
specifier|public
class|class
name|Property
block|{
DECL|field|type
name|GenericType
name|type
decl_stmt|;
DECL|field|name
name|String
name|name
decl_stmt|;
DECL|field|field
name|Field
name|field
decl_stmt|;
DECL|field|accessor
name|Method
name|accessor
decl_stmt|;
DECL|field|mutator
name|Method
name|mutator
decl_stmt|;
DECL|method|Property (GenericType type, String name)
specifier|public
name|Property
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
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|field
operator|=
operator|new
name|Field
argument_list|()
operator|.
name|setPrivate
argument_list|()
operator|.
name|setType
argument_list|(
name|type
argument_list|)
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|accessor
operator|=
operator|new
name|Method
argument_list|()
operator|.
name|setPublic
argument_list|()
operator|.
name|setName
argument_list|(
literal|"get"
operator|+
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|.
name|toUpperCase
argument_list|()
operator|+
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|setReturnType
argument_list|(
name|type
argument_list|)
operator|.
name|setBody
argument_list|(
literal|"return "
operator|+
name|name
operator|+
literal|";\n"
argument_list|)
expr_stmt|;
name|mutator
operator|=
operator|new
name|Method
argument_list|()
operator|.
name|setPublic
argument_list|()
operator|.
name|setName
argument_list|(
literal|"set"
operator|+
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|.
name|toUpperCase
argument_list|()
operator|+
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|addParameter
argument_list|(
name|type
argument_list|,
name|name
argument_list|)
operator|.
name|setReturnType
argument_list|(
name|void
operator|.
name|class
argument_list|)
operator|.
name|setBody
argument_list|(
literal|"this."
operator|+
name|name
operator|+
literal|" = "
operator|+
name|name
operator|+
literal|";\n"
argument_list|)
expr_stmt|;
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
name|void
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
block|}
DECL|method|getType ()
specifier|public
name|GenericType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (GenericType type)
specifier|public
name|void
name|setType
parameter_list|(
name|GenericType
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getField ()
specifier|public
name|Field
name|getField
parameter_list|()
block|{
return|return
name|field
return|;
block|}
DECL|method|getAccessor ()
specifier|public
name|Method
name|getAccessor
parameter_list|()
block|{
return|return
name|accessor
return|;
block|}
DECL|method|getMutator ()
specifier|public
name|Method
name|getMutator
parameter_list|()
block|{
return|return
name|mutator
return|;
block|}
DECL|method|removeAccessor ()
specifier|public
name|void
name|removeAccessor
parameter_list|()
block|{
name|accessor
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|removeMutator ()
specifier|public
name|void
name|removeMutator
parameter_list|()
block|{
name|mutator
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|removeField ()
specifier|public
name|void
name|removeField
parameter_list|()
block|{
name|field
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|isMutable ()
specifier|public
name|boolean
name|isMutable
parameter_list|()
block|{
return|return
name|mutator
operator|!=
literal|null
return|;
block|}
DECL|method|hasField ()
specifier|public
name|boolean
name|hasField
parameter_list|()
block|{
return|return
name|field
operator|!=
literal|null
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
name|field
operator|!=
literal|null
operator|&&
name|field
operator|.
name|hasAnnotation
argument_list|(
name|clazz
argument_list|)
operator|||
name|accessor
operator|!=
literal|null
operator|&&
name|accessor
operator|.
name|hasAnnotation
argument_list|(
name|clazz
argument_list|)
operator|||
name|mutator
operator|!=
literal|null
operator|&&
name|mutator
operator|.
name|hasAnnotation
argument_list|(
name|clazz
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
if|if
condition|(
name|field
operator|!=
literal|null
operator|&&
name|field
operator|.
name|hasAnnotation
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
name|field
operator|.
name|getAnnotation
argument_list|(
name|clazz
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|accessor
operator|!=
literal|null
operator|&&
name|accessor
operator|.
name|hasAnnotation
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
name|accessor
operator|.
name|getAnnotation
argument_list|(
name|clazz
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|mutator
operator|!=
literal|null
operator|&&
name|mutator
operator|.
name|hasAnnotation
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
name|mutator
operator|.
name|getAnnotation
argument_list|(
name|clazz
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

