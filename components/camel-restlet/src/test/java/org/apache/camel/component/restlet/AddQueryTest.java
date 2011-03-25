begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
package|;
end_package

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

begin_comment
comment|/**  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|AddQueryTest
specifier|public
class|class
name|AddQueryTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testAddQuery ()
specifier|public
name|void
name|testAddQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"http://a/b/c?a=b"
argument_list|,
name|RestletProducer
operator|.
name|addQueryToUri
argument_list|(
literal|"http://a/b/c"
argument_list|,
literal|"a=b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://a/b/c?a=b&c=b"
argument_list|,
name|RestletProducer
operator|.
name|addQueryToUri
argument_list|(
literal|"http://a/b/c"
argument_list|,
literal|"a=b&c=b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://a/b/c?a=b&c=b&l=m"
argument_list|,
name|RestletProducer
operator|.
name|addQueryToUri
argument_list|(
literal|"http://a/b/c?c=b&l=m"
argument_list|,
literal|"a=b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://a/b/c?a=b&i=j&c=b&l=m"
argument_list|,
name|RestletProducer
operator|.
name|addQueryToUri
argument_list|(
literal|"http://a/b/c?c=b&l=m"
argument_list|,
literal|"a=b&i=j"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

