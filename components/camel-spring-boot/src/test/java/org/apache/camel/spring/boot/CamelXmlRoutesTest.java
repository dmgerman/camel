begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Route
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
name|classes
operator|=
block|{
name|CamelXmlRoutesTest
operator|.
name|class
block|,
name|RouteConfigWithCamelContextInjected
operator|.
name|class
block|}
argument_list|,
name|properties
operator|=
block|{
literal|"camel.springboot.xml-routes=file:src/test/resources/routes/foo.xml,file:src/test/resources/routes/bar.xml"
block|}
argument_list|)
DECL|class|CamelXmlRoutesTest
specifier|public
class|class
name|CamelXmlRoutesTest
extends|extends
name|Assert
block|{
comment|// Collaborators fixtures
annotation|@
name|Autowired
DECL|field|camelContext
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldDetectRoutes ()
specifier|public
name|void
name|shouldDetectRoutes
parameter_list|()
block|{
comment|// When
name|Route
name|route
init|=
name|camelContext
operator|.
name|getRoute
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
comment|// Then
name|assertNotNull
argument_list|(
name|route
argument_list|)
expr_stmt|;
comment|// When
name|route
operator|=
name|camelContext
operator|.
name|getRoute
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
comment|// Then
name|assertNotNull
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

