begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|maven
package|;
end_package

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
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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
annotation|@
name|Ignore
argument_list|(
literal|"Cannot run on CI servers so run manually"
argument_list|)
DECL|class|MavenArtifactProviderTest
specifier|public
class|class
name|MavenArtifactProviderTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testAddComponent ()
specifier|public
name|void
name|testAddComponent
parameter_list|()
block|{
name|CamelCatalog
name|camelCatalog
init|=
operator|new
name|DefaultCamelCatalog
argument_list|()
decl_stmt|;
name|MavenArtifactProvider
name|provider
init|=
operator|new
name|DefaultMavenArtifactProvider
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setCacheDirectory
argument_list|(
literal|"target/cache"
argument_list|)
expr_stmt|;
name|int
name|before
init|=
name|camelCatalog
operator|.
name|findComponentNames
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
name|provider
operator|.
name|addArtifactToCatalog
argument_list|(
name|camelCatalog
argument_list|,
literal|"org.apache.camel"
argument_list|,
literal|"dummy-component"
argument_list|,
name|camelCatalog
operator|.
name|getCatalogVersion
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"dummy"
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|after
init|=
name|camelCatalog
operator|.
name|findComponentNames
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should find 1 new component"
argument_list|,
name|after
operator|-
name|before
operator|==
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

