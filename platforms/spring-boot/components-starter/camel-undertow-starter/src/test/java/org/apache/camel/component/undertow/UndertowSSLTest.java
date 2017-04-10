begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
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
name|undertow
operator|.
name|springboot
operator|.
name|UndertowComponentAutoConfiguration
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
name|spring
operator|.
name|boot
operator|.
name|CamelAutoConfiguration
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
name|test
operator|.
name|AvailablePortFinder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
name|SpringBootApplication
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
name|stereotype
operator|.
name|Component
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
name|ContextConfiguration
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_comment
comment|/**  * Testing the ssl configuration  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringRunner
operator|.
name|class
argument_list|)
annotation|@
name|SpringBootApplication
annotation|@
name|DirtiesContext
annotation|@
name|ContextConfiguration
argument_list|(
name|classes
operator|=
block|{
name|UndertowComponentAutoConfiguration
operator|.
name|class
block|,
name|CamelAutoConfiguration
operator|.
name|class
block|}
argument_list|)
annotation|@
name|SpringBootTest
argument_list|(
name|properties
operator|=
block|{
literal|"camel.ssl.enabled=true"
block|,
literal|"camel.ssl.config.cert-alias=web"
block|,
literal|"camel.ssl.config.key-managers.key-password=changeit"
block|,
literal|"camel.ssl.config.key-managers.key-store.resource=/keystore.p12"
block|,
literal|"camel.ssl.config.key-managers.key-store.password=changeit"
block|,
literal|"camel.ssl.config.key-managers.key-store.type=PKCS12"
block|,
literal|"camel.ssl.config.trust-managers.key-store.resource=/cacerts"
block|,
literal|"camel.ssl.config.trust-managers.key-store.password=changeit"
block|,
literal|"camel.ssl.config.trust-managers.key-store.type=jks"
block|}
argument_list|)
DECL|class|UndertowSSLTest
specifier|public
class|class
name|UndertowSSLTest
block|{
DECL|field|port
specifier|private
specifier|static
name|int
name|port
decl_stmt|;
annotation|@
name|Autowired
DECL|field|producerTemplate
specifier|private
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|init ()
specifier|public
specifier|static
name|void
name|init
parameter_list|()
block|{
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpoint ()
specifier|public
name|void
name|testEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|result
init|=
name|producerTemplate
operator|.
name|requestBody
argument_list|(
literal|"undertow:https://localhost:"
operator|+
name|port
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Component
DECL|class|TestRoutes
specifier|public
specifier|static
class|class
name|TestRoutes
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"undertow:https://localhost:"
operator|+
name|port
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

