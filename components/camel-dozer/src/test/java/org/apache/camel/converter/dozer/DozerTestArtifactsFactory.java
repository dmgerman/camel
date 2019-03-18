begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.dozer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|dozer
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
name|com
operator|.
name|github
operator|.
name|dozermapper
operator|.
name|core
operator|.
name|Mapper
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|dozer
operator|.
name|dto
operator|.
name|AddressDTO
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
name|converter
operator|.
name|dozer
operator|.
name|dto
operator|.
name|CustomerDTO
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
name|converter
operator|.
name|dozer
operator|.
name|model
operator|.
name|Address
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
name|converter
operator|.
name|dozer
operator|.
name|service
operator|.
name|Customer
import|;
end_import

begin_class
DECL|class|DozerTestArtifactsFactory
specifier|public
specifier|final
class|class
name|DozerTestArtifactsFactory
block|{
DECL|method|DozerTestArtifactsFactory ()
specifier|private
name|DozerTestArtifactsFactory
parameter_list|()
block|{     }
DECL|method|createServiceCustomer ()
specifier|public
specifier|static
name|Customer
name|createServiceCustomer
parameter_list|()
block|{
return|return
operator|new
name|Customer
argument_list|(
literal|"Bob"
argument_list|,
literal|"Roberts"
argument_list|,
literal|"12345"
argument_list|,
literal|"1 main st"
argument_list|)
return|;
block|}
DECL|method|createModelCustomer ()
specifier|public
specifier|static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|dozer
operator|.
name|model
operator|.
name|Customer
name|createModelCustomer
parameter_list|()
block|{
return|return
operator|new
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|dozer
operator|.
name|model
operator|.
name|Customer
argument_list|(
literal|"Bob"
argument_list|,
literal|"Roberts"
argument_list|,
operator|new
name|Address
argument_list|(
literal|"12345"
argument_list|,
literal|"1 main st"
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createDtoCustomer ()
specifier|public
specifier|static
name|CustomerDTO
name|createDtoCustomer
parameter_list|()
block|{
return|return
operator|new
name|CustomerDTO
argument_list|(
literal|"Bob"
argument_list|,
literal|"Roberts"
argument_list|,
operator|new
name|AddressDTO
argument_list|(
literal|"12345"
argument_list|,
literal|"1 main st"
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createMapper (CamelContext camelContext)
specifier|public
specifier|static
name|Mapper
name|createMapper
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|DozerBeanMapperConfiguration
name|config
init|=
operator|new
name|DozerBeanMapperConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|setMappingFiles
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"mapping.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|MapperFactory
name|factory
init|=
operator|new
name|MapperFactory
argument_list|(
name|camelContext
argument_list|,
name|config
argument_list|)
decl_stmt|;
return|return
name|factory
operator|.
name|create
argument_list|()
return|;
block|}
DECL|method|createCleanMapper (CamelContext camelContext)
specifier|public
specifier|static
name|Mapper
name|createCleanMapper
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|MapperFactory
name|factory
init|=
operator|new
name|MapperFactory
argument_list|(
name|camelContext
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
name|factory
operator|.
name|create
argument_list|()
return|;
block|}
block|}
end_class

end_unit

