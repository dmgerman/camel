begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|MatcherAssert
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|IsNot
operator|.
name|not
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|StringContains
operator|.
name|containsString
import|;
end_import

begin_class
DECL|class|AhcOperationFailedExceptionTest
specifier|public
class|class
name|AhcOperationFailedExceptionTest
block|{
annotation|@
name|Test
DECL|method|testUrlIsSanitized ()
specifier|public
name|void
name|testUrlIsSanitized
parameter_list|()
block|{
name|AhcOperationFailedException
name|ahcOperationFailedException
init|=
operator|new
name|AhcOperationFailedException
argument_list|(
literal|"http://user:password@host"
argument_list|,
literal|500
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|null
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|ahcOperationFailedException
operator|.
name|getMessage
argument_list|()
argument_list|,
name|not
argument_list|(
name|containsString
argument_list|(
literal|"password"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ahcOperationFailedException
operator|.
name|getUrl
argument_list|()
argument_list|,
name|not
argument_list|(
name|containsString
argument_list|(
literal|"password"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

