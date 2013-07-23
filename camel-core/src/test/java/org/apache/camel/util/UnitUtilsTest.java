begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|UnitUtils
operator|.
name|printUnitFromBytes
import|;
end_import

begin_class
DECL|class|UnitUtilsTest
specifier|public
class|class
name|UnitUtilsTest
extends|extends
name|TestCase
block|{
DECL|method|testPrintUnitFromBytes ()
specifier|public
name|void
name|testPrintUnitFromBytes
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"999 B"
argument_list|,
name|printUnitFromBytes
argument_list|(
literal|999
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0 kB"
argument_list|,
name|printUnitFromBytes
argument_list|(
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0 kB"
argument_list|,
name|printUnitFromBytes
argument_list|(
literal|1001
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1000.0 kB"
argument_list|,
name|printUnitFromBytes
argument_list|(
literal|999999
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0 MB"
argument_list|,
name|printUnitFromBytes
argument_list|(
literal|1000000
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0 MB"
argument_list|,
name|printUnitFromBytes
argument_list|(
literal|1000001
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.5 MB"
argument_list|,
name|printUnitFromBytes
argument_list|(
literal|1500001
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

