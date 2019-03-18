begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
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
name|management
operator|.
name|mbean
operator|.
name|LoadTriplet
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
DECL|class|LoadTripletTest
specifier|public
class|class
name|LoadTripletTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testConstantUpdate ()
specifier|public
name|void
name|testConstantUpdate
parameter_list|()
block|{
name|LoadTriplet
name|t
init|=
operator|new
name|LoadTriplet
argument_list|()
decl_stmt|;
name|t
operator|.
name|update
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0
argument_list|,
name|t
operator|.
name|getLoad1
argument_list|()
argument_list|,
name|Math
operator|.
name|ulp
argument_list|(
literal|1.0
argument_list|)
operator|*
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0
argument_list|,
name|t
operator|.
name|getLoad5
argument_list|()
argument_list|,
name|Math
operator|.
name|ulp
argument_list|(
literal|1.0
argument_list|)
operator|*
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0
argument_list|,
name|t
operator|.
name|getLoad15
argument_list|()
argument_list|,
name|Math
operator|.
name|ulp
argument_list|(
literal|1.0
argument_list|)
operator|*
literal|5
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|t
operator|.
name|update
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|1.0
argument_list|,
name|t
operator|.
name|getLoad1
argument_list|()
argument_list|,
name|Math
operator|.
name|ulp
argument_list|(
literal|1.0
argument_list|)
operator|*
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0
argument_list|,
name|t
operator|.
name|getLoad5
argument_list|()
argument_list|,
name|Math
operator|.
name|ulp
argument_list|(
literal|1.0
argument_list|)
operator|*
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0
argument_list|,
name|t
operator|.
name|getLoad15
argument_list|()
argument_list|,
name|Math
operator|.
name|ulp
argument_list|(
literal|1.0
argument_list|)
operator|*
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testChargeDischarge ()
specifier|public
name|void
name|testChargeDischarge
parameter_list|()
block|{
name|LoadTriplet
name|t
init|=
operator|new
name|LoadTriplet
argument_list|()
decl_stmt|;
name|t
operator|.
name|update
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|double
name|last
init|=
name|t
operator|.
name|getLoad15
argument_list|()
decl_stmt|;
name|double
name|lastDiff
init|=
name|Double
operator|.
name|MAX_VALUE
decl_stmt|;
name|double
name|diff
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|t
operator|.
name|update
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|diff
operator|=
name|t
operator|.
name|getLoad15
argument_list|()
operator|-
name|last
expr_stmt|;
name|assertTrue
argument_list|(
name|diff
operator|>
literal|0.0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|diff
operator|<
name|lastDiff
argument_list|)
expr_stmt|;
name|lastDiff
operator|=
name|diff
expr_stmt|;
name|last
operator|=
name|t
operator|.
name|getLoad15
argument_list|()
expr_stmt|;
block|}
name|lastDiff
operator|=
operator|-
name|Double
operator|.
name|MAX_VALUE
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|t
operator|.
name|update
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|diff
operator|=
name|t
operator|.
name|getLoad15
argument_list|()
operator|-
name|last
expr_stmt|;
name|assertTrue
argument_list|(
name|diff
operator|<
literal|0.0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%f is smaller than %f"
argument_list|,
name|diff
argument_list|,
name|lastDiff
argument_list|)
argument_list|,
name|diff
operator|>
name|lastDiff
argument_list|)
expr_stmt|;
name|lastDiff
operator|=
name|diff
expr_stmt|;
name|last
operator|=
name|t
operator|.
name|getLoad15
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

