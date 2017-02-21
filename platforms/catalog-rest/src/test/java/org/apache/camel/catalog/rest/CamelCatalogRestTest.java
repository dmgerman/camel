begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|rest
package|;
end_package

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|jaxrs
operator|.
name|json
operator|.
name|JacksonJsonProvider
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
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|JAXRSServerFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|lifecycle
operator|.
name|SingletonResourceProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
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
import|import static
name|io
operator|.
name|restassured
operator|.
name|RestAssured
operator|.
name|given
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|hasItems
import|;
end_import

begin_class
DECL|class|CamelCatalogRestTest
specifier|public
class|class
name|CamelCatalogRestTest
block|{
DECL|field|server
specifier|private
name|Server
name|server
decl_stmt|;
DECL|field|catalog
specifier|private
name|CamelCatalogRest
name|catalog
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
block|{
name|catalog
operator|=
operator|new
name|CamelCatalogRest
argument_list|()
expr_stmt|;
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|9000
argument_list|)
expr_stmt|;
comment|// setup Apache CXF REST server
name|JAXRSServerFactoryBean
name|sf
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setResourceClasses
argument_list|(
name|CamelCatalogRest
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setResourceProvider
argument_list|(
name|CamelCatalogRest
operator|.
name|class
argument_list|,
operator|new
name|SingletonResourceProvider
argument_list|(
name|catalog
argument_list|)
argument_list|)
expr_stmt|;
comment|// to use jackson for json
name|sf
operator|.
name|setProvider
argument_list|(
name|JacksonJsonProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|port
argument_list|)
expr_stmt|;
comment|// create and start the CXF server (non blocking)
name|server
operator|=
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testFindComponentLabels ()
specifier|public
name|void
name|testFindComponentLabels
parameter_list|()
throws|throws
name|Exception
block|{
name|given
argument_list|()
operator|.
name|baseUri
argument_list|(
literal|"http://localhost:"
operator|+
name|port
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|when
argument_list|()
operator|.
name|get
argument_list|(
literal|"/camel-catalog/findComponentLabels"
argument_list|)
operator|.
name|then
argument_list|()
operator|.
name|body
argument_list|(
literal|"$"
argument_list|,
name|hasItems
argument_list|(
literal|"bigdata"
argument_list|,
literal|"messaging"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testComponentJSonSchema ()
specifier|public
name|void
name|testComponentJSonSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|given
argument_list|()
operator|.
name|baseUri
argument_list|(
literal|"http://localhost:"
operator|+
name|port
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|when
argument_list|()
operator|.
name|get
argument_list|(
literal|"/camel-catalog/componentJSonSchema/quartz2"
argument_list|)
operator|.
name|then
argument_list|()
operator|.
name|body
argument_list|(
literal|"component.description"
argument_list|,
name|Matchers
operator|.
name|is
argument_list|(
literal|"Provides a scheduled delivery of messages using the Quartz 2.x scheduler."
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

