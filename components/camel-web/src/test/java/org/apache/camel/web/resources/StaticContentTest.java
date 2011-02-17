begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.resources
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|resources
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|StaticContentTest
specifier|public
class|class
name|StaticContentTest
extends|extends
name|TestSupport
block|{
annotation|@
name|Test
DECL|method|testCssFile ()
specifier|public
name|void
name|testCssFile
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|response
init|=
name|resource
argument_list|(
literal|"/css/site.css"
argument_list|)
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have returned a response"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertResponseContains
argument_list|(
name|response
argument_list|,
literal|"Rounded Box Styles"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

