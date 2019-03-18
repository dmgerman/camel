begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atom
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atom
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
DECL|class|AtomHttpNoCamelParametersTest
specifier|public
class|class
name|AtomHttpNoCamelParametersTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testAtomHttpNoCamelParameters ()
specifier|public
name|void
name|testAtomHttpNoCamelParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|AtomEndpoint
name|atom
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"atom://http://www.iafrica.com/pls/cms/grapevine.xml?sortEntries=true&feedHeader=true"
argument_list|,
name|AtomEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|atom
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://www.iafrica.com/pls/cms/grapevine.xml"
argument_list|,
name|atom
operator|.
name|getFeedUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|atom
operator|.
name|isFeedHeader
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|atom
operator|.
name|isSortEntries
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAtomHttpNoCamelParametersAndOneFeedParameter ()
specifier|public
name|void
name|testAtomHttpNoCamelParametersAndOneFeedParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|AtomEndpoint
name|atom
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"atom://http://www.iafrica.com/pls/cms/grapevine.xml?sortEntries=true&feedHeader=true&foo=bar"
argument_list|,
name|AtomEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|atom
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://www.iafrica.com/pls/cms/grapevine.xml?foo=bar"
argument_list|,
name|atom
operator|.
name|getFeedUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|atom
operator|.
name|isFeedHeader
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|atom
operator|.
name|isSortEntries
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

