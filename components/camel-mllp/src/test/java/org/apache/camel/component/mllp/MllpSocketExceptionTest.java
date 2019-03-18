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
name|assertNull
import|;
end_import

begin_comment
comment|/**  * Tests for the  class.  */
end_comment

begin_class
DECL|class|MllpSocketExceptionTest
specifier|public
class|class
name|MllpSocketExceptionTest
extends|extends
name|MllpExceptionTestSupport
block|{
DECL|field|TEST_EXCEPTION_MESSAGE
specifier|static
specifier|final
name|String
name|TEST_EXCEPTION_MESSAGE
init|=
literal|"MLLP Socket Exception Message"
decl_stmt|;
DECL|field|instance
name|MllpSocketException
name|instance
decl_stmt|;
comment|/**      * Description of test.      *      * @throws Exception in the event of a test error.      */
annotation|@
name|Test
DECL|method|testConstructorOne ()
specifier|public
name|void
name|testConstructorOne
parameter_list|()
throws|throws
name|Exception
block|{
name|instance
operator|=
operator|new
name|MllpSocketException
argument_list|(
name|TEST_EXCEPTION_MESSAGE
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_EXCEPTION_MESSAGE
argument_list|,
name|instance
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Description of test.      *      * @throws Exception in the event of a test error.      */
annotation|@
name|Test
DECL|method|testConstructorTwo ()
specifier|public
name|void
name|testConstructorTwo
parameter_list|()
throws|throws
name|Exception
block|{
name|instance
operator|=
operator|new
name|MllpSocketException
argument_list|(
name|TEST_EXCEPTION_MESSAGE
argument_list|,
name|CAUSE
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|instance
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_EXCEPTION_MESSAGE
argument_list|,
name|instance
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

