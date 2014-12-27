begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.commands.catalog
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
operator|.
name|catalog
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|CamelComponentCatalog
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
name|DefaultCamelComponentCatalog
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
DECL|class|CamelModelCatalogTest
specifier|public
class|class
name|CamelModelCatalogTest
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelModelCatalogTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testFindModelNames ()
specifier|public
name|void
name|testFindModelNames
parameter_list|()
block|{
name|CamelComponentCatalog
name|catalog
init|=
operator|new
name|DefaultCamelComponentCatalog
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|catalog
operator|.
name|findModelNames
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Found {} names"
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should find some models"
argument_list|,
name|names
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFindModelNamesFilter ()
specifier|public
name|void
name|testFindModelNamesFilter
parameter_list|()
block|{
name|CamelComponentCatalog
name|catalog
init|=
operator|new
name|DefaultCamelComponentCatalog
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|catalog
operator|.
name|findModelNames
argument_list|(
literal|"transformation"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Found {} names"
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should find some transformation models"
argument_list|,
name|names
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFindModelNamesFilterWildcard ()
specifier|public
name|void
name|testFindModelNamesFilterWildcard
parameter_list|()
block|{
name|CamelComponentCatalog
name|catalog
init|=
operator|new
name|DefaultCamelComponentCatalog
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|catalog
operator|.
name|findModelNames
argument_list|(
literal|"t*"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Found {} names"
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should find some t* models"
argument_list|,
name|names
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFindComponentNamesFilterNoMatch ()
specifier|public
name|void
name|testFindComponentNamesFilterNoMatch
parameter_list|()
block|{
name|CamelComponentCatalog
name|catalog
init|=
operator|new
name|DefaultCamelComponentCatalog
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|catalog
operator|.
name|findModelNames
argument_list|(
literal|"cannotmatchme"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should not match any models"
argument_list|,
name|names
operator|.
name|size
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCoreComponentJson ()
specifier|public
name|void
name|testCoreComponentJson
parameter_list|()
block|{
name|CamelComponentCatalog
name|catalog
init|=
operator|new
name|DefaultCamelComponentCatalog
argument_list|()
decl_stmt|;
name|String
name|json
init|=
name|catalog
operator|.
name|modelJSonSchema
argument_list|(
literal|"split"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should find to split"
argument_list|,
name|json
operator|.
name|contains
argument_list|(
literal|"split"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLabels ()
specifier|public
name|void
name|testLabels
parameter_list|()
block|{
name|CamelComponentCatalog
name|catalog
init|=
operator|new
name|DefaultCamelComponentCatalog
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|labels
init|=
name|catalog
operator|.
name|findModelLabels
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|labels
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should find labels"
argument_list|,
name|labels
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should find transformation label"
argument_list|,
name|labels
operator|.
name|contains
argument_list|(
literal|"transformation"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

