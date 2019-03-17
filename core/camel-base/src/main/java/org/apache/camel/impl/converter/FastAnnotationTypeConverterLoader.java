begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Converter
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
name|PackageScanClassResolver
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
DECL|class|FastAnnotationTypeConverterLoader
specifier|public
specifier|final
class|class
name|FastAnnotationTypeConverterLoader
extends|extends
name|AnnotationTypeConverterLoader
block|{
DECL|method|FastAnnotationTypeConverterLoader (PackageScanClassResolver resolver)
specifier|public
name|FastAnnotationTypeConverterLoader
parameter_list|(
name|PackageScanClassResolver
name|resolver
parameter_list|)
block|{
name|super
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|acceptClass (Class<?> clazz)
specifier|protected
name|boolean
name|acceptClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|Converter
name|conv
init|=
name|ObjectHelper
operator|.
name|getAnnotation
argument_list|(
name|clazz
argument_list|,
name|Converter
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|conv
operator|!=
literal|null
condition|)
block|{
comment|// skip all loader classes as they have already been loaded
return|return
operator|!
name|conv
operator|.
name|loader
argument_list|()
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

