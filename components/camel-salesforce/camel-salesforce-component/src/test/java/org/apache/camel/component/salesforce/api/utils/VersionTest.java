begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.utils
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|utils
package|;
end_package

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
name|assertTrue
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
name|fail
import|;
end_import

begin_class
DECL|class|VersionTest
specifier|public
class|class
name|VersionTest
block|{
DECL|field|V34_0
specifier|private
specifier|static
specifier|final
name|Version
name|V34_0
init|=
name|Version
operator|.
name|create
argument_list|(
literal|"34.0"
argument_list|)
decl_stmt|;
DECL|field|V34_3
specifier|private
specifier|static
specifier|final
name|Version
name|V34_3
init|=
name|Version
operator|.
name|create
argument_list|(
literal|"34.3"
argument_list|)
decl_stmt|;
DECL|field|V35_0
specifier|private
specifier|static
specifier|final
name|Version
name|V35_0
init|=
name|Version
operator|.
name|create
argument_list|(
literal|"35.0"
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldCreate ()
specifier|public
name|void
name|shouldCreate
parameter_list|()
block|{
specifier|final
name|Version
name|version
init|=
name|V34_3
decl_stmt|;
name|assertEquals
argument_list|(
literal|34
argument_list|,
name|version
operator|.
name|getMajor
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|version
operator|.
name|getMinor
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldObserveApiLimits ()
specifier|public
name|void
name|shouldObserveApiLimits
parameter_list|()
block|{
name|V34_0
operator|.
name|requireAtLeast
argument_list|(
literal|34
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|V34_0
operator|.
name|requireAtLeast
argument_list|(
literal|33
argument_list|,
literal|9
argument_list|)
expr_stmt|;
name|V35_0
operator|.
name|requireAtLeast
argument_list|(
literal|34
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
DECL|method|shouldObserveApiLimitsOnMajorVersions ()
specifier|public
name|void
name|shouldObserveApiLimitsOnMajorVersions
parameter_list|()
block|{
name|V35_0
operator|.
name|requireAtLeast
argument_list|(
literal|36
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"No UnsupportedOperationException thrown, but expected"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
DECL|method|shouldObserveApiLimitsOnMinorVersions ()
specifier|public
name|void
name|shouldObserveApiLimitsOnMinorVersions
parameter_list|()
block|{
name|V35_0
operator|.
name|requireAtLeast
argument_list|(
literal|35
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"No UnsupportedOperationException thrown, but expected"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testComparator ()
specifier|public
name|void
name|testComparator
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|V34_0
operator|.
name|compareTo
argument_list|(
name|V34_3
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|V34_0
operator|.
name|compareTo
argument_list|(
name|V35_0
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|V34_3
operator|.
name|compareTo
argument_list|(
name|V35_0
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|V34_3
operator|.
name|compareTo
argument_list|(
name|V34_0
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|V35_0
operator|.
name|compareTo
argument_list|(
name|V34_0
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|V35_0
operator|.
name|compareTo
argument_list|(
name|V34_3
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|V34_0
operator|.
name|compareTo
argument_list|(
name|V34_0
argument_list|)
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|V34_3
operator|.
name|compareTo
argument_list|(
name|V34_3
argument_list|)
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|V35_0
operator|.
name|compareTo
argument_list|(
name|V35_0
argument_list|)
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

