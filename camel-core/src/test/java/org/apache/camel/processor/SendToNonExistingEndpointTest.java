begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ContextTestSupport
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
name|NoSuchEndpointException
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|SendToNonExistingEndpointTest
specifier|public
class|class
name|SendToNonExistingEndpointTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testSendToNonExistingEndpoint ()
specifier|public
name|void
name|testSendToNonExistingEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"thisUriDoesNotExist"
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have failed to send this message!"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchEndpointException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Caught expected exception: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"uri"
argument_list|,
literal|"thisUriDoesNotExist"
argument_list|,
name|e
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

