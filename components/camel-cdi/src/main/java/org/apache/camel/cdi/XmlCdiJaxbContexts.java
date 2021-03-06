begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
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
name|cdi
operator|.
name|xml
operator|.
name|ApplicationContextFactoryBean
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
name|model
operator|.
name|Constants
import|;
end_import

begin_enum
DECL|enum|XmlCdiJaxbContexts
enum|enum
name|XmlCdiJaxbContexts
block|{
DECL|enumConstant|CAMEL_CDI
name|CAMEL_CDI
argument_list|(
name|Constants
operator|.
name|JAXB_CONTEXT_PACKAGES
argument_list|,
name|ApplicationContextFactoryBean
operator|.
name|class
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
block|;
DECL|field|context
specifier|private
specifier|final
name|JAXBContext
name|context
decl_stmt|;
DECL|method|XmlCdiJaxbContexts (String... packages)
name|XmlCdiJaxbContexts
parameter_list|(
name|String
modifier|...
name|packages
parameter_list|)
block|{
try|try
block|{
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|String
operator|.
name|join
argument_list|(
literal|":"
argument_list|,
name|packages
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Error while creating JAXB context for packages "
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|packages
argument_list|)
argument_list|,
name|cause
argument_list|)
throw|;
block|}
block|}
DECL|method|instance ()
name|JAXBContext
name|instance
parameter_list|()
block|{
return|return
name|context
return|;
block|}
block|}
end_enum

end_unit

