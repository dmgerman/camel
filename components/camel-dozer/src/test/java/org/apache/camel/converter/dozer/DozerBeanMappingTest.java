begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * #%L  * Wildfly Camel :: Testsuite  * %%  * Copyright (C) 2013 - 2014 RedHat  * %%  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  * #L%  */
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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ProducerTemplate
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
name|builder
operator|.
name|RouteBuilder
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
name|Customer
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
name|CustomerA
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
name|CustomerB
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
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|DozerBeanMappingTest
specifier|public
class|class
name|DozerBeanMappingTest
block|{
annotation|@
name|Test
DECL|method|testMarshalViaDozer ()
specifier|public
name|void
name|testMarshalViaDozer
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|camelctx
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|HashMap
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|DozerBeanMapperConfiguration
name|mconfig
init|=
operator|new
name|DozerBeanMapperConfiguration
argument_list|()
decl_stmt|;
name|mconfig
operator|.
name|setMappingFiles
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"bean-to-map-dozer-mappings.xml"
block|}
argument_list|)
argument_list|)
expr_stmt|;
operator|new
name|DozerTypeConverterLoader
argument_list|(
name|camelctx
argument_list|,
name|mconfig
argument_list|)
expr_stmt|;
name|camelctx
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|ProducerTemplate
name|producer
init|=
name|camelctx
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|result
init|=
name|producer
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Customer
argument_list|(
literal|"John"
argument_list|,
literal|"Doe"
argument_list|,
literal|null
argument_list|)
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"John"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|"firstName"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Doe"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|"lastName"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|camelctx
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testBeanMapping ()
specifier|public
name|void
name|testBeanMapping
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|camelctx
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|CustomerB
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|DozerBeanMapperConfiguration
name|mconfig
init|=
operator|new
name|DozerBeanMapperConfiguration
argument_list|()
decl_stmt|;
name|mconfig
operator|.
name|setMappingFiles
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"bean-to-bean-dozer-mappings.xml"
block|}
argument_list|)
argument_list|)
expr_stmt|;
operator|new
name|DozerTypeConverterLoader
argument_list|(
name|camelctx
argument_list|,
name|mconfig
argument_list|)
expr_stmt|;
name|CustomerA
name|customerA
init|=
operator|new
name|CustomerA
argument_list|(
literal|"Peter"
argument_list|,
literal|"Post"
argument_list|,
literal|"SomeStreet"
argument_list|,
literal|"12345"
argument_list|)
decl_stmt|;
name|camelctx
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|ProducerTemplate
name|producer
init|=
name|camelctx
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|CustomerB
name|result
init|=
name|producer
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
name|customerA
argument_list|,
name|CustomerB
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|customerA
operator|.
name|getFirstName
argument_list|()
argument_list|,
name|result
operator|.
name|getFirstName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|customerA
operator|.
name|getLastName
argument_list|()
argument_list|,
name|result
operator|.
name|getLastName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|customerA
operator|.
name|getStreet
argument_list|()
argument_list|,
name|result
operator|.
name|getAddress
argument_list|()
operator|.
name|getStreet
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|customerA
operator|.
name|getZip
argument_list|()
argument_list|,
name|result
operator|.
name|getAddress
argument_list|()
operator|.
name|getZip
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|camelctx
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

