begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.linkedin.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|linkedin
operator|.
name|api
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
name|InvocationTargetException
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|ParamConverter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|ParamConverterProvider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|Provider
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

begin_comment
comment|/**  * Converter provider for Enum Query params.  */
end_comment

begin_class
annotation|@
name|Provider
DECL|class|EnumQueryParamConverterProvider
specifier|public
class|class
name|EnumQueryParamConverterProvider
parameter_list|<
name|T
extends|extends
name|Enum
parameter_list|<
name|T
parameter_list|>
parameter_list|>
implements|implements
name|ParamConverterProvider
block|{
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
name|EnumQueryParamConverterProvider
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|getConverter (Class<T> rawType, Type genericType, Annotation[] annotations)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ParamConverter
argument_list|<
name|T
argument_list|>
name|getConverter
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|rawType
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|)
block|{
if|if
condition|(
name|rawType
operator|.
name|isEnum
argument_list|()
condition|)
block|{
try|try
block|{
specifier|final
name|Method
name|valueMethod
init|=
name|rawType
operator|.
name|getMethod
argument_list|(
literal|"value"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
specifier|final
name|Method
name|fromValueMethod
init|=
name|rawType
operator|.
name|getMethod
argument_list|(
literal|"fromValue"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|new
name|ParamConverter
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|T
name|fromString
parameter_list|(
name|String
name|value
parameter_list|)
block|{
try|try
block|{
return|return
operator|(
name|T
operator|)
name|fromValueMethod
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|(
name|T
name|value
parameter_list|)
block|{
try|try
block|{
return|return
operator|(
name|String
operator|)
name|valueMethod
operator|.
name|invoke
argument_list|(
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Enumeration {} does not follow JAXB convention for conversion"
argument_list|,
name|rawType
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

