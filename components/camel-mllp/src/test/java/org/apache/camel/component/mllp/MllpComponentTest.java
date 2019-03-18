begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
package|;
end_package

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
name|assertNull
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

begin_comment
comment|/**  * Tests for the  class.  */
end_comment

begin_class
DECL|class|MllpComponentTest
specifier|public
class|class
name|MllpComponentTest
block|{
DECL|field|initialLogPhiValue
name|Boolean
name|initialLogPhiValue
decl_stmt|;
DECL|field|initialLogPhiMaxBytesValue
name|Integer
name|initialLogPhiMaxBytesValue
decl_stmt|;
DECL|field|instance
name|MllpComponent
name|instance
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|initialLogPhiValue
operator|=
name|MllpComponent
operator|.
name|logPhi
expr_stmt|;
name|initialLogPhiMaxBytesValue
operator|=
name|MllpComponent
operator|.
name|logPhiMaxBytes
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpComponent
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|MllpComponent
operator|.
name|logPhi
operator|=
name|initialLogPhiValue
expr_stmt|;
name|MllpComponent
operator|.
name|logPhiMaxBytes
operator|=
name|initialLogPhiMaxBytesValue
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHasLogPhi ()
specifier|public
name|void
name|testHasLogPhi
parameter_list|()
throws|throws
name|Exception
block|{
name|MllpComponent
operator|.
name|logPhi
operator|=
literal|null
expr_stmt|;
name|assertFalse
argument_list|(
name|MllpComponent
operator|.
name|hasLogPhi
argument_list|()
argument_list|)
expr_stmt|;
name|MllpComponent
operator|.
name|logPhi
operator|=
literal|false
expr_stmt|;
name|assertTrue
argument_list|(
name|MllpComponent
operator|.
name|hasLogPhi
argument_list|()
argument_list|)
expr_stmt|;
name|MllpComponent
operator|.
name|logPhi
operator|=
literal|true
expr_stmt|;
name|assertTrue
argument_list|(
name|MllpComponent
operator|.
name|hasLogPhi
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIsLogPhi ()
specifier|public
name|void
name|testIsLogPhi
parameter_list|()
throws|throws
name|Exception
block|{
name|MllpComponent
operator|.
name|logPhi
operator|=
literal|null
expr_stmt|;
name|assertEquals
argument_list|(
name|MllpComponent
operator|.
name|DEFAULT_LOG_PHI
argument_list|,
name|MllpComponent
operator|.
name|isLogPhi
argument_list|()
argument_list|)
expr_stmt|;
name|MllpComponent
operator|.
name|logPhi
operator|=
literal|false
expr_stmt|;
name|assertFalse
argument_list|(
name|MllpComponent
operator|.
name|isLogPhi
argument_list|()
argument_list|)
expr_stmt|;
name|MllpComponent
operator|.
name|logPhi
operator|=
literal|true
expr_stmt|;
name|assertTrue
argument_list|(
name|MllpComponent
operator|.
name|isLogPhi
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSetLogPhi ()
specifier|public
name|void
name|testSetLogPhi
parameter_list|()
throws|throws
name|Exception
block|{
name|MllpComponent
operator|.
name|setLogPhi
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|MllpComponent
operator|.
name|logPhi
argument_list|)
expr_stmt|;
name|MllpComponent
operator|.
name|setLogPhi
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|MllpComponent
operator|.
name|logPhi
argument_list|)
expr_stmt|;
name|MllpComponent
operator|.
name|setLogPhi
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|MllpComponent
operator|.
name|logPhi
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHasLogPhiMaxBytes ()
specifier|public
name|void
name|testHasLogPhiMaxBytes
parameter_list|()
throws|throws
name|Exception
block|{
name|MllpComponent
operator|.
name|logPhiMaxBytes
operator|=
literal|null
expr_stmt|;
name|assertFalse
argument_list|(
name|MllpComponent
operator|.
name|hasLogPhiMaxBytes
argument_list|()
argument_list|)
expr_stmt|;
name|MllpComponent
operator|.
name|logPhiMaxBytes
operator|=
operator|-
literal|1
expr_stmt|;
name|assertTrue
argument_list|(
name|MllpComponent
operator|.
name|hasLogPhiMaxBytes
argument_list|()
argument_list|)
expr_stmt|;
name|MllpComponent
operator|.
name|logPhiMaxBytes
operator|=
literal|1024
expr_stmt|;
name|assertTrue
argument_list|(
name|MllpComponent
operator|.
name|hasLogPhiMaxBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetLogPhiMaxBytes ()
specifier|public
name|void
name|testGetLogPhiMaxBytes
parameter_list|()
throws|throws
name|Exception
block|{
name|MllpComponent
operator|.
name|logPhiMaxBytes
operator|=
literal|null
expr_stmt|;
name|assertEquals
argument_list|(
name|MllpComponent
operator|.
name|DEFAULT_LOG_PHI_MAX_BYTES
argument_list|,
name|MllpComponent
operator|.
name|getLogPhiMaxBytes
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|expected
init|=
operator|-
literal|1
decl_stmt|;
name|MllpComponent
operator|.
name|logPhiMaxBytes
operator|=
name|expected
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|MllpComponent
operator|.
name|getLogPhiMaxBytes
argument_list|()
argument_list|)
expr_stmt|;
name|expected
operator|=
literal|1024
expr_stmt|;
name|MllpComponent
operator|.
name|logPhiMaxBytes
operator|=
name|expected
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|MllpComponent
operator|.
name|getLogPhiMaxBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSetLogPhiMaxBytes ()
specifier|public
name|void
name|testSetLogPhiMaxBytes
parameter_list|()
throws|throws
name|Exception
block|{
name|Integer
name|expected
init|=
literal|null
decl_stmt|;
name|MllpComponent
operator|.
name|setLogPhiMaxBytes
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|MllpComponent
operator|.
name|logPhiMaxBytes
argument_list|)
expr_stmt|;
name|expected
operator|=
operator|-
literal|1
expr_stmt|;
name|MllpComponent
operator|.
name|setLogPhiMaxBytes
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|MllpComponent
operator|.
name|logPhiMaxBytes
argument_list|)
expr_stmt|;
name|expected
operator|=
literal|1024
expr_stmt|;
name|MllpComponent
operator|.
name|setLogPhiMaxBytes
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|MllpComponent
operator|.
name|logPhiMaxBytes
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

