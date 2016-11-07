begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|springboot
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|CamelCatalog
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
name|catalog
operator|.
name|DefaultCamelCatalog
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
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
name|assertNotNull
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
name|assertTrue
import|;
end_import

begin_class
DECL|class|SpringBootRuntimeProviderTest
specifier|public
class|class
name|SpringBootRuntimeProviderTest
block|{
DECL|field|catalog
specifier|static
name|CamelCatalog
name|catalog
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|createCamelCatalog ()
specifier|public
specifier|static
name|void
name|createCamelCatalog
parameter_list|()
block|{
name|catalog
operator|=
operator|new
name|DefaultCamelCatalog
argument_list|()
expr_stmt|;
name|catalog
operator|.
name|setRuntimeProvider
argument_list|(
operator|new
name|SpringBootRuntimeProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetVersion ()
specifier|public
name|void
name|testGetVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|version
init|=
name|catalog
operator|.
name|getCatalogVersion
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|String
name|loaded
init|=
name|catalog
operator|.
name|getLoadedVersion
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|loaded
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|version
argument_list|,
name|loaded
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProviderName ()
specifier|public
name|void
name|testProviderName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"springboot"
argument_list|,
name|catalog
operator|.
name|getRuntimeProvider
argument_list|()
operator|.
name|getProviderName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFindComponentNames ()
specifier|public
name|void
name|testFindComponentNames
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|catalog
operator|.
name|findComponentNames
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|names
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"file"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"ftp"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"jms"
argument_list|)
argument_list|)
expr_stmt|;
comment|// camel-ejb does not work in spring-boot
name|assertFalse
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"ejb"
argument_list|)
argument_list|)
expr_stmt|;
comment|// camel-pac-logging does not work in spring-boot
name|assertFalse
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"paxlogging"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFindDataFormatNames ()
specifier|public
name|void
name|testFindDataFormatNames
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|catalog
operator|.
name|findDataFormatNames
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|names
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"bindy-csv"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"zip"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"zipfile"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFindLanguageNames ()
specifier|public
name|void
name|testFindLanguageNames
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|catalog
operator|.
name|findLanguageNames
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|names
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"simple"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"spel"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"xpath"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testComponentArtifactId ()
specifier|public
name|void
name|testComponentArtifactId
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
name|catalog
operator|.
name|componentJSonSchema
argument_list|(
literal|"ftp"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"camel-ftp-starter"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDataFormatArtifactId ()
specifier|public
name|void
name|testDataFormatArtifactId
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
name|catalog
operator|.
name|dataFormatJSonSchema
argument_list|(
literal|"bindy-csv"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"camel-bindy-starter"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLanguageArtifactId ()
specifier|public
name|void
name|testLanguageArtifactId
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
name|catalog
operator|.
name|languageJSonSchema
argument_list|(
literal|"spel"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"camel-spring-starter"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

