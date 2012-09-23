begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.jaxb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|CamelContext
import|;
end_import

begin_comment
comment|/**  * Factory for creating {@link JaxbNamespacePrefixMapper} which supports various JAXB-RI implementations.  */
end_comment

begin_class
DECL|class|NamespacePrefixMapperFactory
specifier|public
specifier|final
class|class
name|NamespacePrefixMapperFactory
block|{
DECL|field|SUN_JAXB_21_MAPPER
specifier|private
specifier|static
specifier|final
name|String
name|SUN_JAXB_21_MAPPER
init|=
literal|"org.apache.camel.converter.jaxb.mapper.SunJaxb21NamespacePrefixMapper"
decl_stmt|;
DECL|method|NamespacePrefixMapperFactory ()
specifier|private
name|NamespacePrefixMapperFactory
parameter_list|()
block|{     }
DECL|method|newNamespacePrefixMapper (CamelContext camelContext, Map<String, String> namespaces)
specifier|static
name|JaxbNamespacePrefixMapper
name|newNamespacePrefixMapper
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
comment|// try to load the Sun JAXB 2.1 based
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|SUN_JAXB_21_MAPPER
argument_list|)
decl_stmt|;
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
name|JaxbNamespacePrefixMapper
name|mapper
init|=
operator|(
name|JaxbNamespacePrefixMapper
operator|)
name|camelContext
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|mapper
operator|.
name|setNamespaces
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
return|return
name|mapper
return|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot load CamelNamespacePrefixMapper class"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

