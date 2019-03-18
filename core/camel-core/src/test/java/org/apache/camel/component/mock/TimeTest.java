begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mock
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mock
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
name|TimeUnit
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
DECL|class|TimeTest
specifier|public
class|class
name|TimeTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testTimeSeconds ()
specifier|public
name|void
name|testTimeSeconds
parameter_list|()
block|{
name|Time
name|time
init|=
operator|new
name|Time
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|time
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|time
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
name|time
operator|.
name|getTimeUnit
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|time
operator|.
name|toMillis
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|time
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTimeMinutes ()
specifier|public
name|void
name|testTimeMinutes
parameter_list|()
block|{
name|Time
name|time
init|=
operator|new
name|Time
argument_list|(
literal|3
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|time
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|time
operator|.
name|toMillis
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|time
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTimeHours ()
specifier|public
name|void
name|testTimeHours
parameter_list|()
block|{
name|Time
name|time
init|=
operator|new
name|Time
argument_list|(
literal|4
argument_list|,
name|TimeUnit
operator|.
name|HOURS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|time
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|time
operator|.
name|toMillis
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|time
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTimeDays ()
specifier|public
name|void
name|testTimeDays
parameter_list|()
block|{
name|Time
name|time
init|=
operator|new
name|Time
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|DAYS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|time
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|time
operator|.
name|toMillis
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|time
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

