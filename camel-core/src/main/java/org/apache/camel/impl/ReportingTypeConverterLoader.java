begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|ArrayList
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
name|Comparator
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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|converter
operator|.
name|AnnotationTypeConverterLoader
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
name|impl
operator|.
name|converter
operator|.
name|TypeConverterRegistry
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
comment|/**  * Type converter loader that is capable of reporting the loaded type converters.  *<p/>  * Used by the camel-maven-plugin.  */
end_comment

begin_class
DECL|class|ReportingTypeConverterLoader
specifier|public
class|class
name|ReportingTypeConverterLoader
extends|extends
name|AnnotationTypeConverterLoader
block|{
DECL|field|COMPARE_LAST_LOADED_FIRST
specifier|private
specifier|static
specifier|final
name|Comparator
argument_list|<
name|TypeMapping
argument_list|>
name|COMPARE_LAST_LOADED_FIRST
init|=
operator|new
name|Comparator
argument_list|<
name|TypeMapping
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|TypeMapping
name|t1
parameter_list|,
name|TypeMapping
name|t2
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|t1
operator|.
name|fromType
argument_list|,
name|t2
operator|.
name|fromType
argument_list|)
condition|)
block|{
return|return
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|t1
operator|.
name|toType
argument_list|,
name|t2
operator|.
name|toType
argument_list|)
condition|?
name|t1
operator|.
name|index
operator|-
name|t2
operator|.
name|index
else|:
name|ObjectHelper
operator|.
name|compare
argument_list|(
name|getTypeName
argument_list|(
name|t1
operator|.
name|toType
argument_list|)
argument_list|,
name|getTypeName
argument_list|(
name|t2
operator|.
name|toType
argument_list|)
argument_list|)
return|;
block|}
return|return
name|ObjectHelper
operator|.
name|compare
argument_list|(
name|getTypeName
argument_list|(
name|t1
operator|.
name|fromType
argument_list|)
argument_list|,
name|getTypeName
argument_list|(
name|t2
operator|.
name|fromType
argument_list|)
argument_list|)
return|;
block|}
block|}
decl_stmt|;
DECL|field|typeMappings
specifier|private
name|List
argument_list|<
name|TypeMapping
argument_list|>
name|typeMappings
init|=
operator|new
name|ArrayList
argument_list|<
name|TypeMapping
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|getTypeConversions ()
specifier|public
name|TypeMapping
index|[]
name|getTypeConversions
parameter_list|()
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|typeMappings
argument_list|,
name|COMPARE_LAST_LOADED_FIRST
argument_list|)
expr_stmt|;
return|return
name|typeMappings
operator|.
name|toArray
argument_list|(
operator|new
name|TypeMapping
index|[
name|typeMappings
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
DECL|method|registerTypeConverter (TypeConverterRegistry registry, Method method, Class toType, Class fromType, TypeConverter typeConverter)
specifier|protected
name|void
name|registerTypeConverter
parameter_list|(
name|TypeConverterRegistry
name|registry
parameter_list|,
name|Method
name|method
parameter_list|,
name|Class
name|toType
parameter_list|,
name|Class
name|fromType
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|)
block|{
name|TypeMapping
name|mapping
init|=
operator|new
name|TypeMapping
argument_list|(
name|toType
argument_list|,
name|fromType
argument_list|,
name|typeConverter
operator|.
name|getClass
argument_list|()
argument_list|,
name|method
argument_list|)
decl_stmt|;
name|typeMappings
operator|.
name|add
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
block|}
DECL|method|getTypeName (Class type)
specifier|private
specifier|static
name|String
name|getTypeName
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
return|return
name|type
operator|!=
literal|null
condition|?
name|type
operator|.
name|getName
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**      * Represents a mapping from one type (which can be null) to another      *      * @deprecated not used      */
DECL|class|TypeMapping
specifier|public
specifier|static
class|class
name|TypeMapping
block|{
DECL|field|counter
specifier|private
specifier|static
name|int
name|counter
decl_stmt|;
DECL|field|toType
specifier|private
name|Class
name|toType
decl_stmt|;
DECL|field|fromType
specifier|private
name|Class
name|fromType
decl_stmt|;
DECL|field|converterType
specifier|private
name|Class
name|converterType
decl_stmt|;
DECL|field|method
specifier|private
name|Method
name|method
decl_stmt|;
DECL|field|index
specifier|private
name|int
name|index
decl_stmt|;
DECL|method|TypeMapping (Class toType, Class fromType, Class converterType, Method method)
specifier|public
name|TypeMapping
parameter_list|(
name|Class
name|toType
parameter_list|,
name|Class
name|fromType
parameter_list|,
name|Class
name|converterType
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
name|this
operator|.
name|toType
operator|=
name|toType
expr_stmt|;
name|this
operator|.
name|fromType
operator|=
name|fromType
expr_stmt|;
name|this
operator|.
name|converterType
operator|=
name|converterType
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
name|this
operator|.
name|index
operator|=
name|counter
operator|++
expr_stmt|;
block|}
DECL|method|getFromType ()
specifier|public
name|Class
name|getFromType
parameter_list|()
block|{
return|return
name|fromType
return|;
block|}
DECL|method|getToType ()
specifier|public
name|Class
name|getToType
parameter_list|()
block|{
return|return
name|toType
return|;
block|}
DECL|method|getConverterType ()
specifier|public
name|Class
name|getConverterType
parameter_list|()
block|{
return|return
name|converterType
return|;
block|}
DECL|method|getMethod ()
specifier|public
name|Method
name|getMethod
parameter_list|()
block|{
return|return
name|method
return|;
block|}
DECL|method|getIndex ()
specifier|public
name|int
name|getIndex
parameter_list|()
block|{
return|return
name|index
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
if|if
condition|(
name|object
operator|instanceof
name|TypeMapping
condition|)
block|{
name|TypeMapping
name|that
init|=
operator|(
name|TypeMapping
operator|)
name|object
decl_stmt|;
return|return
name|this
operator|.
name|index
operator|==
name|that
operator|.
name|index
return|;
block|}
return|return
literal|false
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
name|int
name|answer
init|=
name|toType
operator|.
name|hashCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|fromType
operator|!=
literal|null
condition|)
block|{
name|answer
operator|*=
literal|37
operator|+
name|fromType
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
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
literal|"["
operator|+
name|fromType
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"=>"
operator|+
name|toType
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
block|}
end_class

end_unit

