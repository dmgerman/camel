begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|EnableAutoConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|context
operator|.
name|SpringBootTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|annotation
operator|.
name|DirtiesContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringRunner
import|;
end_import

begin_class
annotation|@
name|DirtiesContext
annotation|@
name|RunWith
argument_list|(
name|SpringRunner
operator|.
name|class
argument_list|)
annotation|@
name|EnableAutoConfiguration
annotation|@
name|SpringBootTest
argument_list|(
name|properties
operator|=
literal|"camel.springboot.file-configurations=file:src/test/secret/*.properties"
argument_list|)
DECL|class|CamelConfigurationLocationsTest
specifier|public
class|class
name|CamelConfigurationLocationsTest
extends|extends
name|Assert
block|{
annotation|@
name|Configuration
DECL|class|Config
specifier|static
class|class
name|Config
block|{
annotation|@
name|Bean
DECL|method|routeBuilder ()
name|RouteBuilder
name|routeBuilder
parameter_list|()
block|{
return|return
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
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"stub:foo?password={{mypassword}}"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
annotation|@
name|Autowired
DECL|field|camelContext
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Autowired
DECL|field|producerTemplate
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldSecret ()
specifier|public
name|void
name|shouldSecret
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|Object
name|endpoint
init|=
name|camelContext
operator|.
name|hasEndpoint
argument_list|(
literal|"stub:foo?password=1234"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

