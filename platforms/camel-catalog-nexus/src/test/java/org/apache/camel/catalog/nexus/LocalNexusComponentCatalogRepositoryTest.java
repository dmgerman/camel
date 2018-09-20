begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog.nexus
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|nexus
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|Test
import|;
end_import

begin_class
DECL|class|LocalNexusComponentCatalogRepositoryTest
specifier|public
class|class
name|LocalNexusComponentCatalogRepositoryTest
extends|extends
name|Assert
block|{
DECL|field|catalog
specifier|private
specifier|final
name|CamelCatalog
name|catalog
init|=
operator|new
name|DefaultCamelCatalog
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testLocalNexus ()
specifier|public
name|void
name|testLocalNexus
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|before
init|=
name|catalog
operator|.
name|findComponentNames
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|LocalFileComponentCatalogNexusRepository
name|repo
init|=
operator|new
name|LocalFileComponentCatalogNexusRepository
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setCamelCatalog
argument_list|(
name|catalog
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setInitialDelay
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setDelay
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setNexusUrl
argument_list|(
literal|"dummy"
argument_list|)
expr_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|repo
operator|.
name|setOnAddComponent
argument_list|(
name|latch
operator|::
name|countDown
argument_list|)
expr_stmt|;
name|repo
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should have found component"
argument_list|,
name|latch
operator|.
name|await
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|repo
operator|.
name|stop
argument_list|()
expr_stmt|;
name|int
name|after
init|=
name|catalog
operator|.
name|findComponentNames
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"There should be 1 component found"
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

