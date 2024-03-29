begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.jpa
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|jpa
package|;
end_package

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
name|List
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
name|component
operator|.
name|jpa
operator|.
name|JpaConstants
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
name|examples
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
name|impl
operator|.
name|DefaultCamelContext
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
name|support
operator|.
name|service
operator|.
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|Before
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

begin_class
DECL|class|JpaProducerWithQueryParametersHeaderTest
specifier|public
class|class
name|JpaProducerWithQueryParametersHeaderTest
extends|extends
name|Assert
block|{
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JpaProducerWithQueryParametersHeaderTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|protected
name|DefaultCamelContext
name|camelContext
decl_stmt|;
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|testProducerWithNamedQuery ()
specifier|public
name|void
name|testProducerWithNamedQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:deleteCustomers"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|Customer
name|c1
init|=
operator|new
name|Customer
argument_list|()
decl_stmt|;
name|c1
operator|.
name|setName
argument_list|(
literal|"Willem"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:addCustomer"
argument_list|,
name|c1
argument_list|)
expr_stmt|;
name|Customer
name|c2
init|=
operator|new
name|Customer
argument_list|()
decl_stmt|;
name|c2
operator|.
name|setName
argument_list|(
literal|"Dummy"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:addCustomer"
argument_list|,
name|c2
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"custName"
argument_list|,
literal|"${body}"
argument_list|)
expr_stmt|;
name|List
name|list
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:namedQuery"
argument_list|,
literal|"Willem"
argument_list|,
name|JpaConstants
operator|.
name|JPA_PARAMETERS_HEADER
argument_list|,
name|params
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Willem"
argument_list|,
operator|(
operator|(
name|Customer
operator|)
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|integer
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:deleteCustomers"
argument_list|,
literal|null
argument_list|,
name|int
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|integer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|camelContext
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|camelContext
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
literal|"direct:namedQuery"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jpa://"
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?namedQuery=findAllCustomersWithName"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:addCustomer"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jpa://"
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:deleteCustomers"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jpa://"
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?query=delete from "
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|template
operator|=
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|template
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|template
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

